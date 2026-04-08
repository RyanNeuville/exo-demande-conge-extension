# Infrastructure de Deploiement Local et Conteneurisation

Ce repertoire contient l'integralité des configurations necessaires pour l'orchestration et le deploiement conteneurise de l'extension de gestion des conges dans un environnement de test identique a la production.

## 1. Utilité Organisationnelle (Aspect Humain)

La mise en place de cette infrastructure Docker repond a plusieurs besoins critiques pour l'equipe et les parties prenantes :
- Reproductibilité : Garantir que chaque developpeur travaille sur le meme environnement eXo Platform que ses collegues.
- Simplification : Permettre a un nouvel arrivant de lancer l'application en une seule commande sans installer de bases de donnees complexes.
- Demonstration : Offrir un bac a sable isole pour tester les nouvelles fonctionnalites sans risque pour les donnees de production.

## 2. Details Techniques de l'Infrastructure

L'orchestration s'appuie sur Docker Compose et s'articule autour de trois services principaux.

### 2.1 eXo Platform (Image principale)
Le service applicatif utilise l'image officielle exoplatform/exo-community.
- Volume Mounting : Les binaires JAR et WAR compiles sont injectes directement dans les repertoires /opt/exo/lib et /opt/exo/webapp du conteneur.
- Persistence : Un volume specifique est configuré pour le stockage de la base SQLite (/opt/exo/gatein/data).

### 2.2 Base de donnees MySQL
Bien que notre extension utilise SQLite, eXo Platform necessite une base de donnees transactionnelle pour ses propres operations internes (portails, documents, profils).
- Rôle : Gestion du noyau dur d'eXo (GateIn et JCR).
- Interaction : L'extension de conge ignore volontairement cette base pour garder son independance.

### 2.3 Configuration d'Environnement (.env)
Le fichier .env permet de centraliser les variables de configuration telles que :
- Les ports d'exposition (Standard: 9099).
- Les identifiants de base de donnees internes.
- La localisation des donnees persistantes.

## 3. Utilisation Operatoire

Pour demarrer l'infrastructure complete :
```bash
docker-compose up -d
```

Pour verifier les logs et s'assurer que l'extension a bien ete chargee :
```bash
docker-compose logs -f portal
```
Une fois le demarrage terminé, l'acces se fait via http://localhost:9099/portal.
