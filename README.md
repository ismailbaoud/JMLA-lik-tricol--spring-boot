# 🏭 Écosystème Tricol - Gestion des Approvisionnements

Système de microservices pour la gestion des approvisionnements de l'entreprise Tricol, déployé avec Docker Compose.

## 📋 Architecture

L'écosystème comprend **3 microservices** et une base de données PostgreSQL partagée :

```
┌─────────────────────────────────────────────────────────┐
│                    PostgreSQL Database                   │
│                      tricol_db (port 5442)              │
└──────────────┬──────────────┬──────────────┬────────────┘
               │              │              │
       ┌───────▼──────┐ ┌────▼─────┐ ┌──────▼────────┐
       │ Auth Service │ │ Produits │ │ Fournisseurs  │
       │  (port 8081) │ │ (8080)   │ │  (port 8082)  │
       │ Spring Boot  │ │ Boot     │ │ Spring Core   │
       └──────────────┘ └──────────┘ └───────────────┘
```

### 🎯 Services

| Service | Port | Type | Description |
|---------|------|------|-------------|
| **PostgreSQL** | 5442 | Database | Base de données partagée |
| **Auth** | 8081 | Spring Boot WAR | Service d'authentification JWT |
| **Produits** | 8080 | Spring Boot WAR | Gestion des produits |
| **Fournisseurs** | 8082 | Spring Framework | Gestion des fournisseurs (Spring Core) |

## 🚀 Démarrage Rapide

### Prérequis

- Docker 20.10+
- Docker Compose 2.0+
- Java 17+ (pour développement local)
- Maven 3.8+ (pour builds locaux)

### 1. Compiler les projets

```bash
# Compiler le service Auth
cd Authontification
mvn clean package
cd ..

# Compiler le service Produits
cd "Gestion des produits-spring-boot"
mvn clean package
cd ..

# Compiler le service Fournisseurs (Spring Core)
cd Gestion-des-Fournisseurs-spring-core
mvn clean package
cd ..
```

### 2. Lancer l'écosystème complet

```bash
# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Vérifier l'état des services
docker-compose ps
```

### 3. Accéder aux services

- **Auth Service**: http://localhost:8081
- **Produits Service**: http://localhost:8080
- **Fournisseurs Service**: http://localhost:8082
- **PostgreSQL**: localhost:5442

## 📊 Configuration Base de Données

Tous les services partagent la même base PostgreSQL :

```yaml
Database: tricol_db
User: tricol_user
Password: tricol_pass123
Port externe: 5442
Port interne (Docker): 5432
```

### Script d'initialisation

Le fichier `init-db.sql` est automatiquement exécuté au premier démarrage de PostgreSQL.

## 🔧 Endpoints API

### Auth Service (port 8081)

```http
POST   /auth/register    - Créer un utilisateur
POST   /auth/login       - Se connecter (obtenir JWT)
GET    /auth/users       - Liste des utilisateurs
```

### Produits Service (port 8080)

```http
GET    /produits         - Liste des produits
POST   /produits         - Créer un produit
GET    /produits/{id}    - Détails d'un produit
PUT    /produits/{id}    - Modifier un produit
DELETE /produits/{id}    - Supprimer un produit
```

### Fournisseurs Service (port 8082)

```http
GET    /fournisseurs     - Liste des fournisseurs
POST   /fournisseurs     - Créer un fournisseur
GET    /fournisseurs/{id} - Détails d'un fournisseur
PUT    /fournisseurs/{id} - Modifier un fournisseur
DELETE /fournisseurs/{id} - Supprimer un fournisseur
```

## 🛠️ Développement

### Travailler sur un service spécifique

```bash
# Redémarrer un seul service
docker-compose restart auth

# Reconstruire et redémarrer
docker-compose up -d --build auth

# Voir les logs d'un service
docker-compose logs -f fournisseurs
```

### Build local (sans Docker)

#### Service Fournisseurs (Spring Core + Jetty)

```bash
cd Gestion-des-Fournisseurs-spring-core
mvn clean package
mvn jetty:run
# Accessible sur http://localhost:8080
```

#### Service Auth (Spring Boot)

```bash
cd Authontification
mvn clean package
mvn spring-boot:run
# Accessible sur http://localhost:8080
```

#### Service Produits (Spring Boot)

```bash
cd "Gestion des produits-spring-boot"
mvn clean package
mvn spring-boot:run
# Accessible sur http://localhost:8080
```

## 🔍 Tests

### Tester les endpoints

Fichiers de test HTTP disponibles :
- `Authontification/authTest.http`
- `Gestion-des-Fournisseurs-spring-core/testEndPoints.http`

### Exemple de requête Auth

```http
### Créer un utilisateur
POST http://localhost:8081/auth/register
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "email": "admin@tricol.com"
}

### Login
POST http://localhost:8081/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

## 📦 Arrêt et Nettoyage

```bash
# Arrêter tous les services
docker-compose down

# Arrêter et supprimer les volumes (⚠️ perte de données)
docker-compose down -v

# Supprimer les images construites
docker-compose down --rmi local
```

## 🏗️ Structure Technique

### Service Fournisseurs (Spring Framework)
- **Framework**: Spring Core 6.1.18 (non-Boot)
- **Configuration**: XML (`applicationContext.xml`)
- **Serveur**: Tomcat 10.1 (en Docker)
- **ORM**: Spring Data JPA + Hibernate 6.5.2
- **Packaging**: WAR

### Services Auth & Produits (Spring Boot)
- **Framework**: Spring Boot 3.x
- **Configuration**: `application.properties`
- **Serveur**: Tomcat embarqué
- **ORM**: Spring Data JPA
- **Packaging**: WAR

## 🔐 Sécurité

- JWT pour l'authentification (service Auth)
- Secrets configurables via variables d'environnement
- Isolation réseau Docker

## 📝 Variables d'Environnement

```bash
# JWT Configuration (optionnel)
JWT_SECRET=your-secret-key-change-this-in-production
JWT_EXPIRATION=3600000

# Database (configuré par défaut)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tricol_db
SPRING_DATASOURCE_USERNAME=tricol_user
SPRING_DATASOURCE_PASSWORD=tricol_pass123
```

## 🐛 Dépannage

### Les services ne démarrent pas

```bash
# Vérifier les logs
docker-compose logs postgres
docker-compose logs auth

# Vérifier la santé de PostgreSQL
docker-compose exec postgres pg_isready -U tricol_user
```

### Erreur de connexion DB

```bash
# Recréer la base
docker-compose down -v
docker-compose up -d
```

### Port déjà utilisé

```bash
# Vérifier les ports
netstat -tuln | grep -E '8080|8081|8082|5442'

# Changer les ports dans docker-compose.yml
```

## 📚 Documentation Additionnelle

- [Docker Compose Guide](./README-DOCKER.md)
- [Schema Base de Données](./init-db.sql)

## 👥 Équipe

Développé par l'équipe Tricol

## 📄 Licence

Propriétaire - Tricol © 2025
