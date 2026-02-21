# Projet de Gestion des Conges - Extension eXo Platform

## Description du projet

Ce projet est une extension developpee specifiquement pour eXo Platform, ayant pour but de gerer le cycle d'approbation et l'historique complet des demandes de conges au sein d'une organisation.

L'application integre un workflow natif permettant :

- Aux employes de declarer leurs indisponibilites et de consulter leur solde.
- Aux responsables d'equipe d'approuver ou refuser les demandes avec un systeme de commentaires obligatoires.
- Aux administrateurs de superviser l'integralite du processus et de configurer globalement les catalogues de types de conges.

Ce projet se divise strategiquement en deux modules Maven independants, delimitant la responsabilite du code backend lie a la logique metier et du code frontend integre a l'UI du portail.

## Architecture Technique

Le composant logiciel s'articule autour des developpements suivants :

### 1. Module Services (Cœur Backend)

Ce module garantit la securite fonctionnelle de l'application, les points d'acces API pour le frontend ainsi que l'interaction native avec la base de donnees.

- **API REST (JAX-RS)** : Les methodes `RestService` integrent les appels du repertoire frontend et protegent les routes metier.
- **Pattern Repository (JDBC Pur)** : Pour maximiser les performances et le contole des transactions, l'interrogation de la base de donnees se fait exclusivement au travers d'interfaces et de leurs implementations JDBC respectives (Exemple: `TypeCongeRepositoryImpl`, `UtilisateurRepositoryImpl`).
- **Pattern Mapper** : La restitution des reponses SQL (`ResultSet`) est centralisee dans le package `mapper`, deleguant l'instantiation des entites metier a des classes de conversion (Exemples: `UtilisateurMapper.fromResultSet`).
- **Persistance native (SQLite)** : **Point materiel fondamental**, l'extension gere souverainement sa propre base de donnees embarquee. Le projet s'appuie sur `SQLite` (`demande_conge.db`) optimise activement via des directives PRAGMA (`journal_mode = WAL`, configuration memoire tampon, etc.) implantees dans `DatabaseConnection.java`. En l'etat actuel du code source, l'extension est agnostique de la connexion base de donnees principale du portail eXo.

### 2. Module Webapp (Interface Vue.js)

Il compose le portlet qui sera affiche au sein du portail de collaboration.

- **Technologie front** : Entierement realisee a l'aide du framework Vue.js. Les fichiers sources applicatifs se situent sous `webapp/src/main/webapp/vue-app/`.
- **Ecosystème Component-Based** : Les formulaires (ex: `congeForm.vue`), les inventaires administrateurs et les etats individuels ont des logiques disociees pour un rendu optimisé a l'execution.

## Environnement et Prerequis

- **Version Java Requise** : JDK 17 / JDK 21.
- **Gestionnaire de Build** : Apache Maven (version recommandee : 3.6+).
- **Plateforme Cible** : eXo Platform Community 7.0.0.
- **Frontend Build Tooling** : Node.js installe en local.

## Deploiement Local et Tests

### 1. Clonage de l'espace de developpement

```bash
git clone https://github.com/RyanNeuville/exo-demande-conge-extension.git
cd exo-demande-conge-extension
```

### 2. Package de l'application

Le build complet necessite la generation concurrente du portlet (npm) et l'assembly des differents JAR/WAR via Maven.

```bash
# Etape 1 : Construction de l'interface Vue.js
cd webapp
npm install
npm run build

# Etape 2 : Construction globale des paquets Java
cd ..
mvn clean install
```

A la de la sequence, le JAR appele a regir la logique serveur est localise dans `services/target` et l'archive WAR destinee au web-container sera dans `webapp/target`.

### 3. Deploiement Docker sur eXo Platform

Le repertoire racine donne acces a l'arretract `docker` pour bootstrapper un tenant eXo Platform fonctionnel en isolation, et deployer l'extension au vol.

#### A propos de la base MySQL

Bien qu'une instance `mysql` soit necessaire et declaree dans votre section de `docker-compose.yml`, elle est confiee **strictement au noyau dur eXo Platform** (`EXO_DB_TYPE=mysql`). L'extension traitera son registre de conges sur la base du driver `org.sqlite.JDBC` de maniere silencieuse.

#### Demarer l'infrastructure complete :

Le montage de fichiers integre nativement dans `docker-compose` projetera le `.jar` backend et le `.war` frontend compile au bon endroit dans le tomcat conteneurise de l'applicatif eXo Platform.

Apres la compilation validee :

```bash
cd docker
docker-compose up -d
```

L'URL d'accees du collaboratif est standardement : http://localhost:9099/portal.

## Conformite de developpement

Le design pattern et le code impliquent les contraintes d'execution suivantes pour la conformite continue :

- Lancer le build sans erreurs grace a `mvn test`.
- Les nouvelles methodes interagissant avec JDBC ont obligation de se plier a l'architecture du framework interne (Implementation Repository -> Passage au mapper -> Retour API REST).

## Mentions de propriete

Le repository concerne les developpements prives. Les requetes de redistribution non accordees ne sont pas autorisees sur le domaine applicatif complet des services.
