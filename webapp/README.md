# Documentation Technique et Humaine : Module webapp

Ce document detaille l'architecture et le fonctionnement du module frontend, qui constitue l'interface utilisateur (UI) reactive pour la gestion des conges dans eXo Platform.

## 1. Experience Utilisateur (Aspect Humain)

L'interface a ete conçue pour etre inclusive et intuitive. Nous savons que la soumission d'un conge est une interaction sensible ; l'application propose donc :
- Une navigation fluide par onglets pour separer l'activite personnelle de l'activite de gestion.
- Des retours visuels immediats sur la validité des dates (ex: calcul de durée en temps reel).
- Une transparence totale sur l'etat d'avancement des demandes via un historique detaille.
- Des composants UI modernes (via DaisyUI et Tailwind) pour reduire la fatigue cognitive des utilisateurs.

## 2. Architecture Frontend (Aspect Technique)

Le module repose sur Vue.js (version standard eXo) et utilise Webpack pour produire un livrable compatible avec l'ecosysteme GateIn.

### 2.1 Cycle de Build Hybride
Le build suit une chaine de transformation specifique :
1. Compilation JavaScript : Webpack transforme le code Vue.js moderne en un bundle AMD (Asynchronous Module Definition), format requis pour l'injection propre de scripts dans eXo Platform.
2. Packaging JEE : Maven encapsule les fichiers compiles dans une archive .war. Les ressources statiques sont placees sous META-INF/resources pour etre servies par le container.

### 2.2 Composants et Gestion d'Etat
L'application est decoupee en composants atomiques :
- Dashboard : Affiche les KPIs et les statistiques individuelles à l'aide de graphiques.
- Formulaire : Gère la saisie intelligente avec validation dynamique des champs.
- Administration : Interface reservee aux managers et administrateurs pour le pilotage d'equipe.
- API Service : Couche d'abstraction (apiService.js) simulant un client REST pour communiquer avec le module services.

## 3. Integration avec eXo Platform

### 3.1 Mode Portlet
Le module webapp n'est pas une application autonome mais un Portlet. Son integration est definie par plusieurs descripteurs XML :
- portlet.xml : Declare le point d'entree Java eXo (GenericDispatchedViewPortlet).
- gatein-resources.xml : Orchestre le chargement asynchrone des bundles JS et des feuilles de style CSS.

### 3.2 i18n et Localisation
L'application detecte automatiquement la langue de l'utilisateur declaree dans eXo. Elle utilise le service exoi18n pour charger dynamiquement les fichiers de traduction (.properties ou .json) sans necessiter un rafraichissement de page.

## 4. Maintenance et Developpement

### 4.1 Commandes de Build
```bash
# Installation des dependances Node
npm install

# Compilation Frontend
npm run build

# Packaging Final
mvn clean install
```

### 4.2 Styles et Design System
Le projet utilise TailwindCSS et DaisyUI via CDN dans la page index.html pour garantir une interface legere et facilement personnalisable sans recompiler l'integralité du framework CSS.
