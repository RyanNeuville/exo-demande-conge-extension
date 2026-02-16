# Rapport Technique : Module `Services` (exo-demande-conge-extension)

Ce document présente une analyse détaillée du sous-module `services` de l'extension de demande de congé. Il se concentre sur l'implémentation de l'API REST, la gestion des données et l'intégration avec eXo Platform.

## 1. Vue d'Ensemble & Architecture

Le module `services` est un projet Maven produisant un `.jar` (ou potentiellement inclus dans un WAR global) qui expose des **services REST JAX-RS**.

- **Nature** : Backend Java pur exposant une API REST.
- **Framework** : JAX-RS (standard Java EE), intégré via le conteneur eXo.
- **Rôle** : Fournir les endpoints pour créer, lire et gérer les demandes de congés.

## 2. Analyse des Dépendances (`pom.xml`)

- **eXo Platform Stack** : Dépend de `exo.ws.rest.core`, `social-component-core`, `exo.kernel.commons`. Cela confirme une intégration native.
- **Base de Données** : La dépendance `mysql-connector-j` (v8.4.0) est présente mais **INUTILISÉE** dans le code actuel (voir section 3).
- **Tests** : JUnit 5 et Mockito sont déclarés, ainsi que Allure pour le reporting, mais aucun test n'est implémenté.

## 3. Analyse du Code Source (Java)

L'implémentation réside dans le package `com.codexmaker.services.rest`.

### 3.1 Service REST (`DemandeCongeRestService.java`)

C'est le cœur du système. Il définit les endpoints suivants sous `/conges` :

- `POST /submit` : Soumettre une demande.
- `GET /all` : (Admin) Lister toutes les demandes.
- `GET /my` : Lister mes demandes.
- `GET /relations` : Lister les demandes de mes connexions sociales (utilise `RelationshipManager`).
- `GET /enattente` : (Admin) Lister les demandes en attente.
- `PUT /update` : (Admin) Mettre à jour une demande.
- `POST /approve` / `/reject` / `/cancel` : Workflow de validation.

### 3.2 Modèle de Données

- **`DemandeConge`** : POJO simple (ID, dates, type, motif, statut).
- **`UserDemandes`** : Conteneur regroupant l'utilisateur et sa liste de demandes.

### 3.3 Gestion des Données (CRITIQUE)

Le système de persistance est **très problématique** pour un environnement de production :

1.  **Stockage Fichier JSON** : Les données sont stockées dans un fichier JSON (`demandes.json`).
2.  **Localisation** : Le code cherche ce fichier via `DemandeCongeRestService.class.getResource("/com/codexmaker/services/rest/data/demandes.json")`.
3.  **Problème Majeur** :
    - Le fichier `demandes.json` est **ABSENT** des sources (`src/main/resources/...` n'existe pas). L'application risque de **planter au démarrage** (NullPointerException dans l'initialiseur statique).
    - L'écriture (`writeDemandesFile`) tente de modifier ce fichier **dans le classpath** (probablement à l'intérieur du WAR explosé ou du JAR). Ces modifications sont **volatiles** (perdues au redéploiement) et non thread-safe (malgré le mot-clé `synchronized`, cela ne fonctionne pas en cluster).

## 4. Sécurité

- **Authentification** : Utilise `ConversationState.getCurrent()` pour identifier l'utilisateur (standard eXo).
- **Autorisation** : Hérite de la sécurité JAX-RS (`@RolesAllowed`), mais implémente aussi une vérification manuelle du groupe `/platform/administrators` via `OrganizationService`.

## 5. Tests

- **État** : Une classe de test existe (`DemandeCongeServiceImplTest.java`) mais elle est **VIDE**.
- **Couverture** : 0%. Aucune validation automatique n'est possible actuellement.

## 6. Résumé des Points Techniques

| Aspect            | Détail                               | État                           |
| :---------------- | :----------------------------------- | :----------------------------- |
| **Framework**     | JAX-RS / eXo Kernel                  | Standard                    |
| **Persistance**   | Fichier JSON dans le Classpath       | **CRITIQUE** (Non viable)   |
| **Date Handling** | `SimpleDateFormat` (Non Thread-Safe) | À remplacer par `java.time` |
| **Concurrence**   | `synchronized` sur méthodes fichier  | Goulot d'étranglement       |
| **Tests**         | Exists mais vide                     | Manquant                    |
| **Dépendances**   | MySQL Connecteur inutile             | À nettoyer                  |

## 7. Recommandations Prioritaires

1.  **Implémenter une Vraie Persistance** : Utiliser JPA/Hibernate ou JCR (Java Content Repository) pour stocker les demandes. Supprimer la gestion par fichier JSON.
2.  **Créer le fichier ressource manquant** : Si la persistance fichier est maintenue temporairement, il faut impérativement créer `src/main/resources/com/codexmaker/services/rest/data/demandes.json` avec un contenu vide `[]`.
3.  **Sécuriser l'Accès Concurrent** : Si fichier maintenu, utiliser un verrouillage plus robuste ou passer à une base de données.
4.  **Nettoyage** : Supprimer le connecteur MySQL s'il n'est pas utilisé, ou l'utiliser vraiment.