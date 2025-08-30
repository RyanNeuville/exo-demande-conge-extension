# 🚀 Projet de Gestion des Congés – Extension (Java, Maven, eXo SDK)

## 📌 Description du projet

Ce projet est une **extension pour eXo Platform** permettant de gérer les demandes de congés au sein d’une organisation.
Il fournit des fonctionnalités pour :

* 📥 Soumettre une demande de congé
* ✔️ Valider ou rejeter une demande (workflow)
* 📊 Suivre l’historique des congés des employés
* 🔗 Intégration avec eXo Platform (services, UI, déploiement dans le container eXo)

Ce projet est structuré en **modules Maven** (services, webapp) afin de séparer clairement les responsabilités et un dossier docker pour la contenerisation et le deploiement. 

---

## ⚙️ Prérequis

Avant de commencer, assurez-vous d’avoir installé :

* **Java JDK 17 ou 21**
* **Maven 3.6+**
* **IDE Eclipce ou IntelliJ-(preference)**
* **Docker & Docker Compose** (pour déploiement sur eXo)
* **exo platform commutity:7.0.0 (via docker)**
* **SQBD (mysql)**
* **Git**

---

## 📂 Structure du projet

```
exo-demande-conge-extension/
│── pom.xml                  # Parent Maven
│
├── services/                # Couche service (logique métier, DAO, mapping, services, controller)
│   ├── src/main/java/       # Code source Java
│   ├── src/test/java/       # Tests unitaires (Junit 5)
│   └── pom.xml
│
├── webapp/                  # Module web (UI, intégration eXo, REST)
│   ├── src/main/java/
│   ├── src/main/webapp/     # Ressources front (Vue.js)
│   └── pom.xml
│
└── docker/        # Fichier de configurartion d'installation d'exo platform
    ├── sql   # init database
    ├── .env         # variables d'environnement (port, etc..)
    ├── docker-compose.yml   # exécution des services (exo, mysql, elasticsearch, onlyoffice)
    └── Dockerfile    # pour personnaliser exo
```

---

## 🛠️ Installation de l’environnement de développement

1. **Cloner le dépôt**

   ```bash
   git clone https://github.com/RyanNeuville/exo-demande-conge-extension.git
   cd exo-demande-conge-extension
   ```

2. **Configurer les variables d’environnement (Linux)**
   Ajouter dans `~/.bashrc` ou `~/.zshrc` :

   ```bash
   export JAVA_HOME=/usr/lib/jvm/java-8-openjdk
   export MAVEN_HOME=/usr/share/maven
   export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
   ```

3. **Vérifier les versions**

   ```bash
   java -version
   mvn -v
   ```
   
4. **Configurer la base de donnees local**

    - allez dans la classe Constants du package utils dans le module services et modifier les constatnte de connexion a la base de donnees
   
   ```java
    /** constantes pour la connexion à la base de donnees */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/exo_demande_conges";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "NOUVEAU_MOT_DE_PASSE";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
   ```
   - importer fichier database.sql du sous dossier sql du dossier docker. (contient la base de donnees initial du projet)
---

## 📦 Installation & Compilation

1. **Nettoyer et compiler le projet**

   ```bash
   mvn clean install
   ```

2. **Générer les artefacts (JAR/WAR)**

    * Le **module services** produit un JAR
    * Le **module webapp** produit un WAR déployable dans eXo

---

## ✅ Tests

Pour exécuter les tests unitaires :

```bash
mvn test
```

Les rapports de tests sont disponibles dans :

```
services/target/surefire-reports/
```

---

## 🏗️ Build & Packaging

Créer le package complet :
- dans le wepapp
```bash
npm run build
```
- dans la racine du projet
```bash
mvn package
```

📦 Les artefacts seront générés dans :

```
services/target/*.jar
webapp/target/*.war
```

---

## 🚢 Déploiement (Docker + eXo)

1. **Ajouter les artefacts generer comme volume dans le contenair d'exo**

   ```yaml
   volumes:
      - exo_data:/srv/exo
      - exo_logs:/var/log/exo
      - ./demande-conge-extension-services.jar:/opt/exo/lib/demande-conge-extension-services.jar
      - ./demande-conge-extension-webapp.war:/opt/exo/webapps/demande-conge-extension-webapp.war
   ```
    - ajuster le chemin d'acces à l'artefact .war et .jar en fonction de pour vous il est recommander de les copier dans le dossier docker
2. **Exécuter le container**

   ```bash
   docker compose docker-compose.yml restart exo
   ```

3. **Accéder à l’application**
   👉 [http://localhost:9099/portal](http://localhost:9099/portal)

---

## 🔧 Maintenance & Bonnes pratiques

* Utiliser `mvn clean install -DskipTests` pour gagner du temps lors du build.
* Toujours lancer les tests avant un push (`mvn test`).
* Respecter la convention de code Java et la structure Maven.
* Documenter les services et DTO avec JavaDoc.

---

## 👨‍💻 Contributeurs

* **\[Ryan Feussi]** – Développeur Java
* **Équipe Code X Maker** – Support et contributions

---

## 📄 Licence

Projet interne – non destiné à un usage public.

---