# Documentation Technique : Module `webapp`

Ce document detaille l'architecture et le processus de compilation du module `webapp`. Ce sous-module constitue l'interface utilisateur (UI) de l'extension de gestion des demandes de conges, specifique a eXo Platform.

## 1. Role et Structure Globale

Le module `webapp` repose sur une architecture hybride. Son but est de fournir une interface reactive et dynamique hebergee en tant qu'application Java web portlet :

- **Frontend asynchrone** : Le code applicatif deployee est code en **Vue.js** (version 3.x), compilé par Webpack.
- **Backend d'empaquetage** : Le resultat binaire final prend la forme d'un **WAR Java**, standard decrit par `pom.xml`, lui permettant d'etre ingéré par Tomcat/eXo Platform.

## 2. Processus de Build Hybride (Node / Maven)

La construction de l'interface depend d'une double compilation chainee : ECMAScript -> Java Packaging.

### Etape 2.1 : Compilation Frontend (Node.js & Vue)

Le dossier applicatif de developpement se trouve physiquement sous `src/main/webapp/vue-app/`. Les dependances `package.json` et les logiques de bundling en ont le controle :

1. Installer la chaine d'outillage front via `npm install`.
2. Packager la logue metier Vue.js via `npm run build`.
   - Webpack utilise le point d'entree statique `main.js`.
   - Le module exporte un code cible AMD (Asynchronous Module Definition), format intrinsequement reclame par le kernel eXo JavaScript.
   - L'output est recrachie dynamiquement sous `src/main/webapp/js/[name].bundle.js`.

### Etape 2.2 : Packaging Backend (Maven)

Une fois les assets javascript rafraichis, Maven prend le relai (`mvn clean install`).
L'operation specifique du `pom.xml` consiste a detecter et copier la source `/src/main/webapp/` vers le manifest internal `META-INF/resources` de la future archive `demande-conge-extension-webapp.war`.
Les ressources deviennent alors des "GateIn Resources" accessibles au container web.

## 3. Architecture Component-Based (Vue.js)

L'arborescence UI Vue.js est concue par separation des espaces d'interfaces.

### Point de montage (`main.js`)

Point d'injection dynamique. Il agit pour instancier _Vue_ selon le contexte d'eXo Platform (detection locale, langue d'eXo : `eXo.env.portal.language`, et chargement asynchrone des traductions `exoi18n.loadLanguageAsync`).

### Les Composants Cles

- **`congeAppMain.vue`** : Composant maitre. Sorte de controleur de vues, distribuant le rendu general vers les vues enfants et filtrant via roles les onglets d'administration.
- **`congeList.vue`** : Formate le tableau d'historique utilisateur au format de donnees JSON eues depuis le serveur. S'ajoute la possibilite d'appeler asynchroniquement `fetchRelationsDemandes()` pour les gestionnaires d'equipes.
- **`congeForm.vue`** : S'occupe de compiler de facon statefull des variables input, interagit avec l'API pour instancier la verification du motif et l'enregistrement statique de `congeType`.
- **`congeFeatures.vue`** / **`congeAdminList.vue`** : Exclues des profils standards (uniquement administrateurs).

_L'usage global de la fonction `fetch` native recupere les structures payload JSON du module `services`._

## 4. Methodes d'Integration eXo Platform

Le couplage avec le panel eXo (le portail) rend necessaire la description d'une injection par le systeme **Portlet**. Cette etape transite par les descripteurs XML definis dans `WEB-INF/`.

### 4.1 Le moteur de la Vue (`portlet.xml`)

Declares formellement l'existence du composant applicatif `demandeCongePortlet`. Il delègue la resolution HTML pure à la classe systeme eXo : `GenericDispatchedViewPortlet`.
La reponse va distribuer la page basique `index.html`. Celle ci precharge les CDN (DaisyUI, FontAwesome, Tailwind) et offre le receptacle vide `<div id="demandeCongeApp"></div>` aux scripts ECMAScript de Vue.js de monter en memoire.

### 4.2 La declaration asynchrone (`gatein-resources.xml`)

Document sensible decrivant a eXo qu'un script client a besoin d'etre resolut a l'ecran.

- Associe le bundle genéré par Webpack (`/js/demandeCongeApp.bundle.js`).
- Impose a eXo Platform d'injecter **dans ce context isole** l'instance tierce de `"vue"` et le dictionnaire `"eXoVueI18n"`. Vue et le module d'i18n ne sont pas embarques dans le `war`, ils sont fournis par l'hote (eXo Platform) a l'execution pour preserver une legere empreinte.
- Declares isolément le bloc stylistique global `demandeCongeApp.css`.

## Mentions

- **Developpement asynchrone** (Polling/API). Les developpeurs peuvent observer le temps de rafraichissement declare au coeur de `main.js` ou dans les hooks cycles the vie vue (`mounted()`, etc).
