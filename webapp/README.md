# Rapport Technique : Module `WebApp` (exo-demande-conge-extension)

Ce document présente une analyse détaillée du sous-module `webapp` de l'extension de demande de congé. Il couvre l'architecture, le processus de build, l'intégration avec eXo Platform et l'analyse du code source Vue.js.

## 1. Vue d'Ensemble & Architecture

Le module `webapp` est une application **hybride Java/JavaScript**.

- **Côté Java (Maven)** : Il s'agit d'un projet Maven standard configuré pour produire un fichier `.war`.
- **Côté Frontend (Node/Webpack)** : Le cœur de l'interface utilisateur est une application **Vue.js** (v3.x) qui est compilée et minifiée par Webpack.
- **Intégration** : Le `.war` final contient les assets compilés (JS/CSS) qui sont servis par eXo Platform via le mécanisme de "Portlet" et "GateIn Resources".

## 2. Configuration et Build

### 2.1 Backend (Maven - `pom.xml`)

- **Type d'artifact** : `war`
- **Rôle** : Empacketer les ressources statiques générées par le build frontend.
- **Build Maven** : Copie le contenu de `src/main/webapp` vers `META-INF/resources` dans le WAR final. Cela permet aux assets d'être accessibles publiquement via le serveur d'application.

### 2.2 Frontend (Node/Webpack - `package.json`)

- **Stack** : Vue.js 3.5.28, Webpack 5.105.0.
- **Scripts** :
  - `npm run build` : Utilise `webpack.prod.js` pour générer les bundles de production.
  - `npm run watch` : Utilise `webpack.watch.js` pour le développement (surveille les changements).
- **Particularité** : Bien que `vite` soit listé dans les scripts (`dev`), la configuration de production repose entièrement sur **Webpack**.

### 2.3 Configuration Webpack (`webpack.prod.js`)

- **Entrée** : `src/main/webapp/vue-app/main.js`.
- **Sortie** : `src/main/webapp/js/[name].bundle.js`.
- **Target `amd`** : Le code est compilé au format **AMD (Asynchronous Module Definition)**. C'est crucial car eXo Platform utilise un chargeur de modules (type RequireJS) pour charger les scripts de manière asynchrone et modulaire.
- **Externes** : `vuetify` et `jquery` sont exclus du bundle et sont supposés être fournis par la plateforme eXo.

## 3. Intégration eXo Platform

L'intégration se fait via deux fichiers XML clés situés dans `WEB-INF/`.

### 3.1 `portlet.xml` (Standard Java Portlet)

- Définit le portlet `demandeCongePortlet`.
- Utilise `GenericDispatchedViewPortlet` pour servir un fichier statique : `/index.html`.
- **Index.html** : Ce fichier sert de coquille vide (`<div id="demandeCongeApp"></div>`) où l'application Vue.js viendra se monter.
  - **Note Importante** : `index.html` inclut des dépendances CSS externes (DaisyUI, Tailwind, FontAwesome) via CDN. Cela nécessite un accès Internet pour le client final.

### 3.2 `gatein-resources.xml` (Spécifique eXo)

- **Déclaration du Module JS** :
  - Le fichier JS compilé (`/js/demandeCongeApp.bundle.js`) est déclaré comme un module GateIn.
  - Dépendances déclarées : `vue` et `eXoVueI18n`. Cela signifie que c'est eXo qui fournit l'instance de Vue.js et le système de traduction à l'exécution.
- **Déclaration du Skin (CSS)** :
  - Charge `/css/demandeCongeApp.css` uniquement pour ce portlet.

## 4. Analyse du Code Source (Vue.js)

### 4.1 Point d'Entrée (`main.js`)

- **Initialisation** :
  1. Détecte la langue de l'utilisateur (`eXo.env.portal.language`).
  2. Charge les traductions via `exoi18n.loadLanguageAsync` (API eXo).
  3. Une fois chargé, initialise l'instance Vue et la monte sur `#demandeCongeApp`.
- **Mécanisme de Polling (Point d'Attention)** :
  - Un `setInterval` de **3 secondes** est utilisé pour rafraîchir les données.
  - Il itère "brutalement" sur les enfants du composant racine (`vm.$children[0]...`) pour appeler `fetchMyDemandes`, `fetchRelationsDemandes`, etc.
  - **Critique** : Cette approche est inefficace et crée une charge réseau constante. L'utilisation de WebSockets ou au moins d'un état global (Store) avec une logique de rafraîchissement contrôlée serait préférable.

### 4.2 Composants (`components/`)

- **`congeAppMain.vue`** : Composant racine. Gère l'affichage général et vérifie si l'utilisateur est administrateur (`/conges/enattente`).
- **`congeList.vue`** : Affiche la liste des congés de l'utilisateur et de ses relations.
  - Utilise `fetch` natif pour appeler les REST APIs (`/conges/my`, `/conges/relations`).
  - Gère les états de chargement basic via `.then/.catch`.
- **`congeForm.vue`** : Formulaire de création de demande.

### 4.3 Données et API

- Les appels API se font directement dans les composants (pas de couche Service séparée).
- Les endpoints utilisés semblent suivre le pattern REST d'eXo : `${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/...`.

## 5. Résumé des Points Techniques

| Aspect                  | Détail                                                                                |
| :---------------------- | :------------------------------------------------------------------------------------ |
| **Framework JS**        | Vue 3 (Syntaxe Options API)                                                           |
| **Module System**       | AMD (via Webpack)                                                                     |
| **Parsing CSS**         | TailwindCSS + DaisyUI (via CDN)                                                       |
| **Communication**       | `fetch` native API + Polling (3s)                                                     |
| **Dépendances Runtime** | Vue.js et i18n fournis par eXo ("Externals")                                          |
| **Authentification**    | Basée sur la session eXo (cookie `JSESSIONID` implicite via `credentials: 'include'`) |

## 6. Recommandations

1. **Supprimer le Polling** : Remplacer le `setInterval` global par une gestion plus fine ou des notifications serveur si possible.
2. **Gestion des Styles** : Intégrer TailwindCSS dans le build Webpack plutôt que via CDN pour éviter les dépendances externes à l'exécution.
3. **Refactoring Service** : Extraire les appels `fetch` dans un fichier `services/CongeService.js` pour une meilleure maintenabilité.
