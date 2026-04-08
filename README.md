# Gestion des Conges - Extension native pour eXo Platform

Ce projet constitue une solution logicielle complete pour la gestion du cycle de vie des demandes de conges au sein de l'ecosysteme eXo Platform. Il a ete concu pour offrir une experience utilisateur fluide tout en garantissant une rigueur administrative maximale.

## 1. Vision et Mission (Volet Humain)

La gestion des absences est un pilier de la satisfaction collaborateur et de l'efficacite organisationnelle. Notre mission avec cette extension est de :
- Simplifier la communication entre les collaborateurs et leur hierarchie.
- Garantir l'equite par un calcul automatisé et transparent des droits aux conges.
- Offrir aux gestionnaires une vision claire des disponibilites de leurs equipes pour optimiser le pilotage de l'activite.

Ce projet se veut un trait d'union entre les exigences RH et le confort de travail des employes.

## 2. Architecture et Concept Technique

L'application repose sur une architecture modulaire Maven, separee en deux couches distinctes pour assurer une maintenance isolee et une robustesse accrue.

### 2.1 Services Backend (Module services)
Le cœur de l'application est un service Java s'appuyant sur les standards JAX-RS pour l'exposition REST.
- Persistance : Utilisation de SQLite pour une base de donnees embarquee, performante et sans dependance externe lourde.
- Pattern DAO/Repository : Acces aux donnees via JDBC natif pour un controle total sur les performances.
- Mappage : Conversion manuelle des ResultSets vers des entites POJO pour eviter la surcharge d'un ORM.

### 2.2 Interface Utilisateur (Module webapp)
Le frontend est une application Vue.js dynamique integree sous forme de portlet eXo.
- Bundle : Compilation Webpack produisant un module AMD compatible avec le kernel JavaScript de GateIn (eXo Platform).
- i18n : Chargement asynchrone des ressources linguistiques via le module exoi18n natif.

---

## 3. Mise en œuvre et Deploiement Cloud

### 3.1 Deploiement sur Railway
Cette extension est prete pour un deploiement sur Railway via le Dockerfile multi-stage present a la racine.

- Prerequis de Persistence : Vous devez configurer un Railway Volume monté sur le chemin `/opt/exo/gatein/data`. Sans ce volume, la base SQLite sera reinitialisee a chaque deploiement.
- Variables d'Environnement :
    - DB_PATH : Definit l'emplacement du fichier .db (Defaut: /opt/exo/gatein/data/demande_conge.db).
    - JAVA_OPTS : Configuration de la memoire (Ex: -Xmx4g).

---

## 4. Installation et Developpement Local

### 4.1 Prerequis
- JDK 21 & Maven 3.8+
- Node.js 18+

### 4.2 Compilation Complète
```bash
# 1. Build de l'interface Vue.js
cd webapp && npm install && npm run build && cd ..

# 2. Package Java (JAR & WAR)
mvn clean install -DskipTests
```

### 4.3 Execution avec Docker Compose
```bash
cd docker && docker-compose up -d
```
Lien local : http://localhost:9099/portal

---

## 5. Documentation Detaillee par Module

- Documentation Technique du Backend : services/README.md
- Documentation Technique du Frontend : webapp/README.md

## 6. Gouvernance et Propriete
Ce projet est une propriete de Kozao Africa. Toute modification ou redistribution doit faire l'objet d'une autorisation officielle.
