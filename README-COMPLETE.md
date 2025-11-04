# 🏢 TRICOL - Système de Gestion Multi-Services

## 📋 Vue d'ensemble du projet

**TRICOL** est un écosystème de microservices développé en Java avec Spring Framework et Spring Boot, conçu pour gérer un système complet de fournisseurs, produits, commandes et authentification. Le projet utilise une architecture microservices avec une base de données PostgreSQL partagée et Docker pour le déploiement.

---

## 🏗️ Architecture du Système

### Schéma d'Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    TRICOL ECOSYSTEM                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Auth Service │  │ Fournisseurs │  │   Produits   │     │
│  │   :8081      │  │   :8082      │  │    :8085     │     │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘     │
│         │                 │                  │              │
│         │                 │                  │              │
│         └─────────────────┼──────────────────┘              │
│                           │                                 │
│                  ┌────────▼────────┐                        │
│                  │   Commandes     │                        │
│                  │     :8083       │                        │
│                  └────────┬────────┘                        │
│                           │                                 │
│         ┌─────────────────┴─────────────────┐              │
│         │                                     │              │
│    ┌────▼─────────────────────────────────┐  │              │
│    │   PostgreSQL Database (tricol_db)    │  │              │
│    │           :5442                      │  │              │
│    └──────────────────────────────────────┘  │              │
│                                               │              │
│              Docker Network: tricol-network   │              │
└───────────────────────────────────────────────┴──────────────┘
```

---

## 🎯 Les 4 Microservices

### 1️⃣ **Service d'Authentification** (Port: 8081)

**Technologie**: Spring Boot  
**Base Path**: `/auth`  
**Description**: Gère l'inscription et la connexion des utilisateurs avec gestion des rôles.

#### 📍 Endpoints:

| Méthode | Endpoint | Description | Body |
|---------|----------|-------------|------|
| `POST` | `/auth/register` | Créer un nouveau utilisateur | `User` (email, userName, password, role) |
| `POST` | `/auth/login` | Se connecter | `LoginDTO` (email, password) |
| `GET` | `/auth/users` | Récupérer tous les utilisateurs | - |

#### 🗂️ Modèle de données:

```java
User {
    id: String (UUID)
    email: String (unique)
    userName: String
    password: String
    role: Role (ADMIN, USER, MANAGER)
}
```

#### 📝 Exemple de requête:

```json
POST http://localhost:8081/auth/register
{
    "email": "admin@tricol.com",
    "userName": "Admin",
    "password": "secure123",
    "role": "ADMIN"
}
```

---

### 2️⃣ **Service Gestion des Fournisseurs** (Port: 8082)

**Technologie**: Spring Framework (Core - Non Boot)  
**Base Path**: `/api/v0/fournisseurs`  
**Description**: CRUD complet pour la gestion des fournisseurs.

#### 📍 Endpoints:

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/v0/fournisseurs` | Créer un fournisseur |
| `GET` | `/api/v0/fournisseurs` | Liste de tous les fournisseurs |
| `GET` | `/api/v0/fournisseurs/{id}` | Récupérer un fournisseur par ID |
| `GET` | `/api/v0/fournisseurs/nom/{nom}` | Rechercher par nom |
| `GET` | `/api/v0/fournisseurs/sorted` | Liste triée par nom (ASC) |
| `PUT` | `/api/v0/fournisseurs/{id}` | Mettre à jour un fournisseur |
| `DELETE` | `/api/v0/fournisseurs/{id}` | Supprimer un fournisseur |

#### 🗂️ Modèle de données:

```java
Fournisseur {
    id: Long
    nom: String (required)
    prenom: String (required)
    email: String (required, email format)
    societe: String (required)
    adresse: String (required)
    contact: String (required)
    telephone: String (required)
    ville: String
}
```

#### 📝 Exemple de requête:

```json
POST http://localhost:8082/api/v0/fournisseurs
{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com",
    "societe": "TechSupply",
    "adresse": "123 Rue de Paris",
    "contact": "Jean Dupont",
    "telephone": "+33123456789",
    "ville": "Paris"
}
```

---

### 3️⃣ **Service Gestion des Produits** (Port: 8085)

**Technologie**: Spring Boot  
**Base Path**: `/api/v1/products`  
**Description**: Gestion complète du catalogue de produits.

#### 📍 Endpoints:

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/v1/products` | Créer un produit |
| `GET` | `/api/v1/products` | Liste de tous les produits |
| `GET` | `/api/v1/products/{id}` | Récupérer un produit par ID |
| `PUT` | `/api/v1/products` | Mettre à jour un produit |
| `DELETE` | `/api/v1/products` | Supprimer un produit |

#### 🗂️ Modèle de données:

```java
Produit {
    id: Long
    name: String (unique)
    unitPrice: Double
    description: String
    quantity: Integer
}
```

#### 📝 Exemple de requête:

```json
POST http://localhost:8085/api/v1/products
{
    "name": "Ordinateur Portable Dell",
    "unitPrice": 899.99,
    "description": "Dell XPS 13 - Intel i7",
    "quantity": 50
}
```

---

### 4️⃣ **Service Gestion des Commandes** (Port: 8083)

**Technologie**: Spring Boot  
**Base Path**: `/api/v1/commandes`  
**Description**: Gestion des commandes avec communication inter-services (appels REST vers Fournisseurs et Produits).

#### 📍 Endpoints:

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/v1/commandes/with-products` | Créer une commande avec plusieurs produits |
| `GET` | `/api/v1/commandes` | Liste de toutes les commandes |
| `GET` | `/api/v1/commandes/{id}` | Récupérer une commande par ID |
| `PATCH` | `/api/v1/commandes/{id}` | Mettre à jour le statut d'une commande |
| `DELETE` | `/api/v1/commandes/{id}` | Supprimer une commande |

#### 🗂️ Modèles de données:

**Commande:**
```java
Commande {
    id: Long
    nomProduit: String
    status: CommendeStatus (EN_ATTENTE, VALIDEE, LIVREE, ANNULEE)
    idFournisseur: Long
}
```

**CommandeProduit** (table de liaison):
```java
CommandeProduit {
    id: Long
    commandeId: Long
    produitId: Long
    quantite: Integer
    unitPrice: Double
}
```

#### 📝 Exemple de requête pour créer une commande:

```json
POST http://localhost:8083/api/v1/commandes/with-products
{
    "nomProduit": "Commande Mars 2025",
    "idFournisseur": 1,
    "status": "EN_ATTENTE",
    "produits": [
        {
            "produitId": 1,
            "quantite": 10
        },
        {
            "produitId": 2,
            "quantite": 5
        }
    ]
}
```

#### 📤 Réponse:

```json
{
    "id": 1,
    "nomProduit": "Commande Mars 2025",
    "status": "EN_ATTENTE",
    "fournisseur": {
        "id": 1,
        "nom": "Dupont",
        "email": "jean.dupont@example.com",
        "societe": "TechSupply"
    },
    "produits": [
        {
            "produitId": 1,
            "nomProduit": "Ordinateur Portable Dell",
            "quantite": 10,
            "unitPrice": 899.99,
            "lineTotal": 8999.90
        },
        {
            "produitId": 2,
            "nomProduit": "Souris Sans Fil",
            "quantite": 5,
            "unitPrice": 29.99,
            "lineTotal": 149.95
        }
    ],
    "totalPrice": 9149.85
}
```

#### 🔄 Communication Inter-Services:

Le service Commandes communique avec:
- **Service Fournisseurs** → Pour récupérer les détails du fournisseur
- **Service Produits** → Pour récupérer les détails des produits et valider leur disponibilité

Configuration dans `application.properties`:
```properties
fournisseurs.service.url=http://fournisseurs:8080
produits.service.url=http://produits:8080
```

---

## 🗄️ Base de Données PostgreSQL

### Configuration:

- **Nom de la base**: `tricol_db`
- **Utilisateur**: `tricol_user`
- **Mot de passe**: `tricol_pass123`
- **Port exposé**: `5442` (mappé vers `5432` dans le container)

### Tables créées automatiquement (JPA/Hibernate):

1. **users** - Utilisateurs du système
2. **fournisseurs** - Informations des fournisseurs
3. **produit** - Catalogue des produits
4. **commande** - Commandes principales
5. **commande_produit** - Liaison entre commandes et produits

### Connexion depuis l'hôte:

```bash
psql -h localhost -p 5442 -U tricol_user -d tricol_db
# Mot de passe: tricol_pass123
```

---

## 🐳 Docker & Déploiement

### Structure Docker Compose:

Le projet utilise **Docker Compose** pour orchestrer 5 services:
- 1 base de données PostgreSQL
- 4 microservices Java

### Fichiers Dockerfile:

#### **Service Auth & Fournisseurs** (build préalable requis):
```dockerfile
FROM tomcat:10.1-jdk17
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tricol_db
ENV SPRING_DATASOURCE_USERNAME=tricol_user
ENV SPRING_DATASOURCE_PASSWORD=tricol_pass123
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

#### **Services Produits & Commandes** (build multi-stage):
```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tricol_db
ENV SPRING_DATASOURCE_USERNAME=tricol_user
ENV SPRING_DATASOURCE_PASSWORD=tricol_pass123
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

---

## 🚀 Comment Exécuter le Projet

### Prérequis:

- Docker & Docker Compose installés
- Port 8081, 8082, 8083, 8085 et 5442 disponibles

### Étape 1: Build des projets nécessitant un build préalable

Certains services nécessitent un build Maven avant le Docker build:

```bash
# Build du service d'authentification
cd Authontification
./mvnw clean package -DskipTests
cd ..

# Build du service fournisseurs
cd Gestion-des-Fournisseurs-spring-core
./mvnw clean package -DskipTests
cd ..
```

### Étape 2: Lancer l'écosystème avec Docker Compose

```bash
# Depuis la racine du projet
docker-compose up --build
```

### Étape 3: Vérifier que les services sont actifs

```bash
# PostgreSQL
curl http://localhost:5442

# Service Auth
curl http://localhost:8081/auth/users

# Service Fournisseurs
curl http://localhost:8082/api/v0/fournisseurs

# Service Produits
curl http://localhost:8085/api/v1/products

# Service Commandes
curl http://localhost:8083/api/v1/commandes
```

### Étape 4: Arrêter les services

```bash
docker-compose down

# Pour supprimer aussi les volumes (base de données)
docker-compose down -v
```

---

## 🔧 Configuration des Services

### Variables d'environnement Docker:

Tous les services partagent la même configuration de base de données via des variables d'environnement définies dans `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/tricol_db
  SPRING_DATASOURCE_USERNAME: tricol_user
  SPRING_DATASOURCE_PASSWORD: tricol_pass123
```

### Configuration locale (développement):

Pour exécuter en local sans Docker, modifiez `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5442/tricol_db
spring.datasource.username=tricol_user
spring.datasource.password=tricol_pass123
```

---

## 📊 Flux de Travail Typique

### Scénario: Créer une commande complète

```bash
# 1. Créer un utilisateur
POST http://localhost:8081/auth/register
{
    "email": "user@tricol.com",
    "userName": "User Test",
    "password": "pass123",
    "role": "USER"
}

# 2. Créer un fournisseur
POST http://localhost:8082/api/v0/fournisseurs
{
    "nom": "Martin",
    "prenom": "Sophie",
    "email": "sophie@supplier.com",
    "societe": "ElectroSupply",
    "adresse": "456 Ave des Champs",
    "contact": "Sophie Martin",
    "telephone": "+33987654321",
    "ville": "Lyon"
}
# Réponse: { "id": 1, ... }

# 3. Créer des produits
POST http://localhost:8085/api/v1/products
{
    "name": "Clavier Mécanique",
    "unitPrice": 129.99,
    "description": "Clavier RGB",
    "quantity": 100
}
# Réponse: { "id": 1, ... }

POST http://localhost:8085/api/v1/products
{
    "name": "Écran 27 pouces",
    "unitPrice": 299.99,
    "description": "Full HD IPS",
    "quantity": 50
}
# Réponse: { "id": 2, ... }

# 4. Créer une commande
POST http://localhost:8083/api/v1/commandes/with-products
{
    "nomProduit": "Commande Bureau Complet",
    "idFournisseur": 1,
    "status": "EN_ATTENTE",
    "produits": [
        { "produitId": 1, "quantite": 3 },
        { "produitId": 2, "quantite": 2 }
    ]
}

# 5. Récupérer la commande avec tous les détails
GET http://localhost:8083/api/v1/commandes/1

# 6. Mettre à jour le statut
PATCH http://localhost:8083/api/v1/commandes/1
{
    "status": "VALIDEE"
}
```

---

## 🛠️ Technologies Utilisées

### Backend:
- **Java 17**
- **Spring Boot 3.x** (Services: Auth, Produits, Commandes)
- **Spring Framework Core** (Service: Fournisseurs)
- **Hibernate/JPA** - ORM
- **PostgreSQL** - Base de données
- **Maven** - Gestion des dépendances

### Déploiement:
- **Docker** - Conteneurisation
- **Docker Compose** - Orchestration
- **Tomcat 10.1** - Serveur d'application

### Patterns & Architectures:
- **Microservices Architecture**
- **RESTful API**
- **DTO Pattern**
- **Repository Pattern**
- **Service Layer Pattern**
- **Multi-stage Docker Build**

---

## 📁 Structure du Projet

```
tricol/
├── docker-compose.yml              # Orchestration des services
├── init-db.sql                     # Script d'initialisation PostgreSQL
├── README-COMPLETE.md              # Ce fichier
│
├── Authontification/               # Service d'authentification
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/ismail/Authontification/
│           │   ├── controller/     # AuthController
│           │   ├── service/        # AuthService
│           │   ├── repository/     # UserRepository
│           │   ├── model/          # User
│           │   └── dto/            # UserDTO, LoginDTO, AuthResponseDto
│           └── resources/
│               └── application.properties
│
├── Gestion-des-Fournisseurs-spring-core/  # Service fournisseurs (Spring Core)
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/org/ismail/Tricol/
│           │   ├── controller/     # FournisseurController
│           │   ├── service/        # FournisseurServiceImp
│           │   ├── dao/            # FournisseurDAO
│           │   ├── model/          # Fournisseur
│           │   └── dto/            # FournisseurDTO
│           └── resources/
│               └── application.properties
│
├── Gestion des produits-spring-boot/      # Service produits
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/org/ismail/gestiondesproduits/
│           │   ├── controller/     # ProduitController
│           │   ├── service/        # ProduitService
│           │   ├── repository/     # ProduitRepository
│           │   ├── model/          # Produit
│           │   ├── dto/            # ProduitDTO
│           │   └── mapper/         # ProduitMapper
│           └── resources/
│               └── application.properties
│
└── gestion des commmendsFournisseur spring boot/  # Service commandes
    ├── Dockerfile
    ├── pom.xml
    └── src/
        └── main/
            ├── java/org/ismail/gestiondescommmendsfournisseurspringboot/
            │   ├── controller/     # CommendeController
            │   ├── service/        # CommendeServiceImpl
            │   ├── repository/     # CommendeRepository, CommandeProduitRepository
            │   ├── model/          # Commande, CommandeProduit
            │   ├── dto/            # CreateCommandeRequest, CommandeResponseDTO, etc.
            │   └── Enum/           # CommendeStatus
            └── resources/
                └── application.properties
```

---

## 🔍 Points Techniques Importants

### 1. Partage de la Base de Données

Tous les microservices se connectent à la **même base de données PostgreSQL** (`tricol_db`). C'est un pattern de **base de données partagée**, plus simple à gérer mais moins découplé qu'une base par service.

### 2. Communication Inter-Services

Le service **Commandes** utilise `RestTemplate` pour communiquer avec les services **Fournisseurs** et **Produits** via HTTP:

```java
RestTemplate restTemplate = new RestTemplate();
FournisseurDTO fournisseur = restTemplate.getForObject(
    fournisseursServiceUrl + "/api/v0/fournisseurs/" + idFournisseur,
    FournisseurDTO.class
);
```

### 3. Gestion des Statuts de Commande

Les commandes peuvent avoir 4 statuts:
- `EN_ATTENTE` - Commande créée
- `VALIDEE` - Commande validée
- `LIVREE` - Commande livrée
- `ANNULEE` - Commande annulée

### 4. Health Checks

PostgreSQL utilise un health check pour s'assurer que la base est prête avant le démarrage des services:

```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U tricol_user -d tricol_db"]
  interval: 5s
  timeout: 5s
  retries: 5
```

### 5. Mapping des Ports

| Service | Port Container | Port Hôte |
|---------|---------------|-----------|
| PostgreSQL | 5432 | 5442 |
| Auth | 8080 | 8081 |
| Fournisseurs | 8080 | 8082 |
| Commandes | 8080 | 8083 |
| Produits | 8080 | 8085 |

---

## 🐛 Résolution de Problèmes

### Problème: "404 Not Found" sur les endpoints

**Solution**: Vérifiez que le service est bien démarré et que le contexte path est correct. Les services utilisent `/` comme contexte root dans Docker.

### Problème: "Connection refused" à PostgreSQL

**Solution**: Attendez que PostgreSQL soit complètement démarré (health check). Les services redémarrent automatiquement avec `restart: on-failure`.

### Problème: "target: no such file or directory" lors du build Docker

**Solution**: Buildez le projet Maven avant:
```bash
./mvnw clean package -DskipTests
```

### Problème: Le service Commandes ne trouve pas les Fournisseurs/Produits

**Solution**: Vérifiez que les URLs des services sont correctement configurées:
- En Docker: `http://fournisseurs:8080` et `http://produits:8080`
- En local: `http://localhost:8082` et `http://localhost:8085`

---

## 📈 Améliorations Futures Possibles

1. **Sécurité**:
   - Ajouter JWT pour l'authentification
   - Implémenter Spring Security
   - HTTPS/TLS

2. **Résilience**:
   - Circuit breaker (Resilience4j)
   - Retry mechanism
   - Service discovery (Eureka)

3. **Monitoring**:
   - Spring Boot Actuator
   - Prometheus + Grafana
   - ELK Stack pour les logs

4. **Base de Données**:
   - Une base par service (pattern recommandé)
   - Migration avec Flyway/Liquibase
   - Cache avec Redis

5. **API Gateway**:
   - Spring Cloud Gateway
   - Routage centralisé
   - Rate limiting

6. **Tests**:
   - Tests d'intégration
   - Tests de charge
   - Contract testing

---

## 👨‍💻 Pour un Autre Développeur/AI

### Points Clés à Retenir:

1. **4 microservices indépendants** mais communiquant entre eux
2. **Base de données PostgreSQL partagée** pour tous les services
3. **Docker Compose** orchestre tout l'écosystème
4. Le service **Commandes** est le plus complexe car il agrège données de **Fournisseurs** et **Produits**
5. **Deux builds Maven requis** avant docker-compose (Auth et Fournisseurs)
6. Les autres services utilisent **multi-stage build** (build dans Docker)

### Commandes Essentielles:

```bash
# Build complet
cd Authontification && ./mvnw clean package -DskipTests && cd ..
cd Gestion-des-Fournisseurs-spring-core && ./mvnw clean package -DskipTests && cd ..
docker-compose up --build

# Logs d'un service
docker-compose logs -f commandes

# Reconstruire un seul service
docker-compose up --build commandes

# Accès à la base de données
docker exec -it tricol-postgres psql -U tricol_user -d tricol_db
```

---

## 📞 Support

Pour toute question ou problème:
- Vérifiez les logs Docker: `docker-compose logs`
- Vérifiez la connexion DB: `docker exec -it tricol-postgres psql -U tricol_user -d tricol_db`
- Vérifiez que tous les ports sont disponibles: `netstat -tuln | grep -E '8081|8082|8083|8085|5442'`

---

**Date de création**: 2025-11-02  
**Version**: 1.0  
**Auteur**: Ismail - Projet TRICOL

