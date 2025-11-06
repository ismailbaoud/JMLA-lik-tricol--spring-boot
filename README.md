# 🏭 Système Tricol - Gestion des Approvisionnements

Système de microservices pour la gestion complète des approvisionnements : produits, fournisseurs, commandes, stocks et authentification.

## 🎯 Microservices

| Service | Port | Description |
|---------|------|-------------|
| **PostgreSQL** | 5442 | Base de données partagée |
| **Authentification** | 8081 | Gestion des utilisateurs et JWT |
| **Produits** | 8080 | Gestion du catalogue produits |
| **Fournisseurs** | 8082 | Gestion des fournisseurs |
| **Commandes** | 8083 | Gestion des commandes fournisseurs avec **calcul CUMP** |
| **Mouvement Stock** | 8084 | Suivi des entrées/sorties de stock |

## 🚀 Démarrage Rapide

### Prérequis
- Docker 20.10+
- Docker Compose 2.0+

### Lancer l'application

```bash
# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Vérifier l'état
docker-compose ps
```

### Accès aux services
- Auth: http://localhost:8081
- Produits: http://localhost:8080
- Fournisseurs: http://localhost:8082
- Commandes: http://localhost:8083
- MouvementStock: http://localhost:8084

## 📊 Base de Données

```yaml
Database: tricol_db
User: tricol_user
Password: tricol_pass123
Port: 5442
```

## 🔧 API Endpoints Principaux

### Authentification (8081)
```http
POST /auth/register    # Créer un compte
POST /auth/login       # Se connecter (JWT)
GET  /auth/users       # Liste utilisateurs
```

### Produits (8080)
```http
GET    /produits           # Liste
POST   /produits           # Créer
GET    /produits/{id}      # Détails
PUT    /produits/{id}      # Modifier
DELETE /produits/{id}      # Supprimer
PATCH  /produits/{id}/reduce-stock  # Réduire stock
```

### Fournisseurs (8082)
```http
GET    /fournisseurs       # Liste
POST   /fournisseurs       # Créer
GET    /fournisseurs/{id}  # Détails
PUT    /fournisseurs/{id}  # Modifier
DELETE /fournisseurs/{id}  # Supprimer
```

### Commandes Fournisseurs (8083) ⭐
```http
GET    /api/v1/commandes              # Liste toutes les commandes
POST   /api/v1/commandes              # Créer une commande (multi-produits)
GET    /api/v1/commandes/{id}         # Détails d'une commande
PUT    /api/v1/commandes/{id}         # Modifier une commande
DELETE /api/v1/commandes/{id}         # Supprimer une commande
PATCH  /api/v1/commandes/{id}/status  # Changer le statut
```

**Statuts de commande** : `PENDING`, `CONFIRMED`, `DELIVERED`, `CANCELLED`

**Calcul du prix** : Utilise la méthode **CUMP** (Coût Unitaire Moyen Pondéré)

### Mouvement Stock (8084)
```http
GET  /api/mouvements              # Liste des mouvements
POST /api/mouvements              # Enregistrer un mouvement
GET  /api/mouvements/{id}         # Détails
GET  /api/mouvements/produit/{id} # Mouvements par produit
```

**Types de mouvement** : `ENTREE`, `SORTIE`, `AJUSTEMENT`

## 💡 Fonctionnalités Clés

### 📦 Gestion des Commandes Multi-Produits
- Créer une commande avec plusieurs produits
- Calcul automatique du montant total avec **méthode CUMP**
- Validation automatique des fournisseurs et produits

### 🔄 Intégration Automatique Stock
- Statut `DELIVERED` → Création automatique de mouvements d'entrée
- Mise à jour automatique des quantités en stock
- Traçabilité complète des mouvements

### 🛡️ Gestion des Erreurs
- Exceptions personnalisées par service
- Messages d'erreur clairs et cohérents
- Validation des données en entrée

## 📝 Exemple de Création de Commande

```json
POST http://localhost:8083/api/v1/commandes
Content-Type: application/json

{
  "fournisseurId": 1,
  "produits": [
    {
      "produitId": 1,
      "quantite": 100,
      "prixUnitaire": 15.50
    },
    {
      "produitId": 2,
      "quantite": 50,
      "prixUnitaire": 25.00
    }
  ],
  "status": "PENDING"
}
```

## 🛠️ Commandes Utiles

```bash
# Redémarrer un service
docker-compose restart commandes

# Reconstruire un service
docker-compose up -d --build commandes

# Voir les logs d'un service
docker-compose logs -f commandes

# Arrêter tout
docker-compose down

# Arrêter et supprimer les données
docker-compose down -v
```

## 🏗️ Architecture Technique

- **Framework** : Spring Boot 3.x / Spring Core 6.x
- **Base de données** : PostgreSQL 15
- **ORM** : Spring Data JPA + Hibernate
- **API** : REST avec WebClient pour communication inter-services
- **Sécurité** : JWT (service Auth)
- **Déploiement** : Docker + Docker Compose

## 📚 Documentation Complète

- [API Commandes](./gestion%20des%20commmendsFournisseur%20spring%20boot/API_DOCUMENTATION.md)
- [Guide Docker](./README-DOCKER.md)
- [Documentation Complète](./README-COMPLETE.md)

## 🐛 Dépannage

### Services ne démarrent pas
```bash
docker-compose logs <service-name>
docker-compose restart <service-name>
```

### Erreur de connexion DB
```bash
docker-compose down -v
docker-compose up -d
```

### Port déjà utilisé
Modifiez les ports dans `docker-compose.yml`

## 📄 Licence

Propriétaire - Tricol © 2025
