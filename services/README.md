# Documentation Technique : Module `services`

Ce document detaille l'architecture et le fonctionnement du module `services`, qui constitue le cœur backend Java de l'extension de gestion des demandes de conges pour eXo Platform.

## 1. Role et Structure

Le module `services` est un projet Maven produisant un executable `.jar`. Son role est triple :

- Assurer l'integralite de la logique metier (regles de validation, calculs de soldes, workflows d'approbation).
- Exposer des endpoints API REST (via JAX-RS) pour le frontend Vue.js.
- Garantir la persistance native et isolee des donnees via SQLite.

Il est decoupe en plusieurs sous-repertories (packages) respectant la separation des responsabilites :

### 1.1 Couches Applicatives Principal

1.  **`api` & `service`** : Ce triptyque (Service, Interface, Implementation) encapsule la logique d'exposition. `DemandeCongeRestService.java` orchestre les endpoints exposes pour le client, tandis que les services associes determinent le traitement profond des regles de l'entreprise.
2.  **`repository`** : Il s'agit de la couche d'Acces aux Donnees (DAO). Les interfaces (ex: `UtilisateurRepository`, `TypeCongeRepository`) declarent les contrats d'operation CRUD. Les implementations (ex: `UtilisateurRepositoryImpl`) effectuent les requetes concretes.
3.  **`mapper`** : Ce package (ex: `UtilisateurMapper`, `TypeCongeMapper`) a la responsabiliet exclusive de convertir les reponses de la base de donnees (`ResultSet` JDBC) en objets Java de la couche modele.

### 1.2 Couches Modeles et Transit

- **`model.entity`** : Contient le paradigme POJO. L'architecture utilise l'heritage objet avec une classe generique `Utilisateur` etendue en `Employe`, `Responsable`, `Administrateur`. S'y trouvent egalement `DemandeConge` et `TypeConge`.
- **`model.enums`** : Type fort des metadonnees critiques (ex: `Role`, `StatutDemande`).
- **`dto`** : Data Transfer Objects. Ces objets evitent l'exposition directe du domaine entity a la couche presentation Web.

### 1.3 Utilitaires, Configuration et Exceptions

- **`config`** : Abrite `DatabaseConnection.java`, le gestionnaire de cycle de vie et d'optimisations de la connexion `SQLite`.
- **`utils`** : Integre `Constants.java` (regroupement de toutes les cles statiques, logs, formats) et `SqlQueries.java` (isolation complete de la grammaire SQL native en variables constantes).
- **`exception`** : Hierarchie d'exceptions specifiques au metier (ex: `InsufficientLeaveBalanceException` pour capturer un solde insuffisant lors de la soumission).

## 2. Infrastructure de Persistance (SQLite)

Contrairement l'historique de developpement ou a la configuration coeur d'eXo Platform (qui tourne sous MySQL), le cycle de vie **des demandes de conges seules** (ce module ci present) repose exclusivement sur **SQLite**.

### 2.1 Configuration

Toute la logique est chargee dans `DatabaseConnection.java` en utilisant le driver standart JDBC org.sqlite.JDBC.

- La base de production locale generee ou visee s'apelle : `demande_conge.db`.
- Afin de maximiser les debits parallelistes ou l'usage en RAM, la connection force les configurations PRAGMA `journal_mode = WAL`, `synchronous = NORMAL`, `temp_store = MEMORY`.

### 2.2 Gestion des Requetes (JDBC Natif)

Le projet n'utilise volontairement pas d'ORM lourds (pas d'Hibernate/JPA presentement employe pour le code). Le code ecrit et instancie des `PreparedStatement` purs.
_Avantage_ : Controle absolu sur le temps de cycle CPU/RAM sans surcharge.

## 3. Trajectoire d'Execution Normale (Flow Exemple)

Voici le deroulement architectural standard suite a une requete API (ex: chercher un Utilsateur) :

1.  **Exposition** : L'URL REST repond et delegue l'operation a l'implementation metier.
2.  **DAO Call** : Le service interroge `UtilisateurRepository`.
3.  **SQL Exe** : `UtilisateurRepositoryImpl` va chercher l'ordre SQL pre-compile dans `SqlQueries.SELECT_UTILISATEUR_BY_ID`.
4.  **Transaction** : L'execution se fait via la connection `DatabaseConnection`.
5.  **Mapping** : Le `ResultSet` est transfere a `UtilisateurMapper.fromResultSet(rs)`.
6.  **Domaine** : Le mapper identifie l'utilisateur comme de sous-type `Employe`, il peuple et retourne l'entite Java associee.

## 4. Compilation Methodologique Maven

Le `services/pom.xml` instruit les dependances suivantes :

- Le moteur `SQLite JDBC`.
- L'integration runtime de conteneurs tiers `exo.ws.rest.core`.
- Les supports de test `JUnit 5`.

Compilez le module individuellement via la racine du sous-dossier `services` avec :

```bash
mvn clean install
```

Le JAR binaire et compilé sera généré dans `/target/demande-conge-extension-services.jar`.
