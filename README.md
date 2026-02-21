# 🚀 Projet de Gestion des Congés – Extension eXo Platform

[![Java](https://img.shields.io/badge/Java-17%2F21-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![eXo Platform](https://img.shields.io/badge/eXo%20Platform-7.0-blue)](https://www.exoplatform.com/)
[![License](https://img.shields.io/badge/License-Internal-red.svg)](#)

## 📌 Description du projet

Cette extension pour **eXo Platform** offre une solution complète et intégrée pour la gestion du cycle de vie des congés au sein d’une organisation. Elle permet une séparation stricte des rôles et une automatisation du flux de validation.

### 🌟 Fonctionnalités clés par rôle

- **👤 Employé** :
  - Soumission de demandes avec gestion des demi-journées.
  - Consultation en temps réel du solde de congés.
  - Suivi de l'historique personnel et des états des demandes (`BROUILLON`, `EN_ATTENTE`, `VALIDEE`, etc.).
- **👥 Responsable** :
  - Tableau de bord des demandes à traiter pour son équipe.
  - Validation ou refus avec commentaires obligatoires.
- **🛡️ Administrateur** :
  - Gestion globale des types de congés (plafonds, règles de déduction).
  - Vue d'ensemble de toutes les demandes du système.
  - Exportation de rapports consolidés.

---

## 🏗️ Architecture Technique

Le projet suit une architecture multicouche robuste pour assurer la maintenabilité et l'extensibilité.

### 🛠️ Patterns & Organisation

- **Repository Pattern** : Abstraction totale de l'accès aux données.
- **Mapper Pattern** : Centralisation de la logique de transformation `ResultSet ↔ Entity` (ex: `UtilisateurMapper`, `TypeCongeMapper`).
- **Centralized Constants & Queries** : Utilisation de `Constants.java` pour les messages/logs et `SqlQueries.java` pour le SQL, évitant les chaînes "en dur".
- **Modules Maven** :
  - `services/` : Cœur métier (Logique, DAO, API REST).
  - `webapp/` : Interface utilisateur moderne (Vue.js) intégrée au portail eXo.

---

## ⚙️ Environnement & Prérequis

- **Runtime** : Java JDK 17 ou 21.
- **Build** : Maven 3.6+.
- **Containers** : Docker & Docker Compose.
- **Platform** : eXo Platform Community 7.0.0.
- **Database** :
  - **Développement** : SQLite (via JDBC).
  - **Production** : MySQL (déployé via Docker).

---

## 📂 Structure du projet

```text
exo-demande-conge-extension/
├── services/               # Backend : Logique métier & Persistence
│   ├── src/main/java/.../api/          # Contrôleurs REST
│   ├── src/main/java/.../mapper/       # Logique de mapping JDBC
│   ├── src/main/java/.../repository/   # Abstraction des données
│   └── src/main/java/.../utils/        # Constants & SQL Queries
├── webapp/                 # Frontend : UI Vue.js & Intégration eXo
│   └── src/main/webapp/    # Assets, Composants Vue, Webpack
└── docker/                 # Infrastructure & Déploiement
    ├── sql/                # Scripts d'initialisation DB
    └── docker-compose.yml  # Stack complète (eXo, MySQL, ES)
```

---

## 🛠️ Installation & Configuration

### 1. Clonage et Build

```bash
git clone https://github.com/RyanNeuville/exo-demande-conge-extension.git
cd exo-demande-conge-extension
mvn clean install
```

### 2. Configuration Base de Données

Modifiez les paramètres dans `Constants.java` (ou via variables d'environnement dans le futur) :

```java
public static final String DB_URL = "jdbc:mysql://localhost:3306/exo_demande_conges";
public static final String DB_USER = "root";
public static final String DB_PASSWORD = "VOTRE_MOT_DE_PASSE";
```

_Note : Importez `docker/sql/database.sql` pour initialiser le schéma._

---

## 🚢 Déploiement

Le déploiement s'appuie sur Docker pour garantir la parité entre les environnements.

1.  **Build du Frontend** (dans `webapp/`) : `npm run build`
2.  **Packaging Maven** : `mvn package`
3.  **Docker** :
    ```bash
    cd docker
    docker-compose up -d
    ```
    _L'application est ensuite accessible sur [http://localhost:9099/portal](http://localhost:9099/portal)._

---

## 🧪 Tests & Maintenance

Exécutez la suite de tests unitaires (JUnit 5) :

```bash
mvn test
```

_Les rapports sont générés dans `services/target/surefire-reports/`._

---

## 👨‍💻 Équipe

- **Ryan Feussi** – Lead Java Developer
- **Code X Maker** – Engineering Support

---

## 📄 Licence

Projet interne - Propriété exclusive.
