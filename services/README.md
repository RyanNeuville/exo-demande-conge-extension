# Documentation Technique et Humaine : Module services

Ce document detaille les fondations et le fonctionnement du module services, qui constitue le moteur intelligent et le garant de l'integrite des donnees pour l'extension de gestion des conges.

## 1. Philosophie de Conception (Aspect Humain)

Le module services a ete conçu avec une priorite absolue : la fiabilité des donnees. Dans un contexte RH, une erreur de calcul de solde ou une perte d'historique peut entrainer des tensions sociales. C'est pourquoi nous avons opté pour :
- Une persistence locale robuste (SQLite) pour eviter de polluer la base de donnees principale du portail.
- Un workflow de validation strict ou chaque changement d'etat est tracé et audité.
- Des messages d'erreur metiers explicites pour guider l'utilisateur en cas de refus (ex: solde insuffisant, chevauchement).

## 2. Architecture Technique (Aspect Technique)

Le module est un projet Maven produisant un executable .jar injecté dans le classpath d'eXo Platform.

### 2.1 Couches Logicielles
L'architecture suit un pattern de separation des responsabilites (SoC) :

- API REST (api package) : Point d'entree JAX-RS. Gere l'authentification via ConversationState d'eXo et la securisation des routes par roles (@RolesAllowed).
- Logique Metier (service package) : Centralise les calculs critiques (jours ouvres hors weekends, ajustement progressif des soldes, workflow JIT de creation d'utilisateurs).
- Persistance (repository package) : Implementation JDBC pure. Pas d'ORM pour garantir des performances previsible et une empreinte memoire minimale.
- Mapping (mapper package) : Transformateurs de donnees isolés pour convertir les ResultSets SQL en objets metiers.

### 2.2 Moteur de Persistance : SQLite
Le choix de SQLite repond a un besoin d'isolation totale.
- Performance : Optimise avec les modes WAL (Write-Ahead Logging) et les connexions synchrones normales dans DatabaseConnection.java.
- Schema Dynamique : DatabaseInitializer.java assure la creation et la mise a jour automatique des tables au demarrage de l'extension.
- Flexibilite : Le chemin de la base est configurable via la variable d'environnement DB_PATH.

## 3. Gestion des Flux et Securite

### 3.1 Synchronisation JIT (Just-In-Time)
L'application ne demande pas de pre-population manuelle des utilisateurs. A la premiere connexion ou demande, le service extrait l'identite eXo (ConversationState) et cree un profil local avec un solde par defaut. Cela simplifie radicalement l'administration.

### 3.2 Audit Trail
Chaque action sur une demande (soumission, validation, refus, modification) genere une entree dans la table HistoriqueEtat. Cela permet d'assurer une tracabilite "humaine" totale : qui a fait quoi, quand, et pourquoi.

## 4. Maintenance et Tests

### 4.1 Build Maven
```bash
mvn clean install
```
Cette commande genere le binaire services/target/demande-conge-extension-services.jar.

### 4.2 Recommandations techniques
Toute modification des requetes SQL doit etre reportee dans SqlQueries.java pour garder une source de verite unique et eviter les injections SQL.
