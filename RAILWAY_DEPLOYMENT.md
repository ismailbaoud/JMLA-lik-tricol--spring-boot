# 🚂 Guide de Déploiement sur Railway

## ⚠️ Important : Architecture Multi-Services

Ce projet contient **6 services indépendants** :
- PostgreSQL (Base de données)
- Auth (Authentification)
- Produits
- Fournisseurs
- Commandes
- MouvementStock

Railway nécessite de **déployer chaque service séparément**.

## 🎯 Option 1 : Déploiement Multi-Services sur Railway (Recommandé)

### Étape 1 : Créer un nouveau projet Railway
```bash
railway login
railway init
```

### Étape 2 : Ajouter PostgreSQL
1. Dans le dashboard Railway, cliquez sur **"New"**
2. Sélectionnez **"Database" > "PostgreSQL"**
3. Notez les variables d'environnement générées

### Étape 3 : Déployer chaque microservice

Pour chaque service, créez un nouveau service Railway :

#### Service Auth
```bash
railway service create auth
railway up --service auth
```

Variables d'environnement à définir :
```env
SERVICE_NAME=auth
DATABASE_URL=<votre_postgresql_url>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
JWT_SECRET=your-secret-key
PORT=8080
```

#### Service Produits
```bash
railway service create produits
railway up --service produits
```

Variables d'environnement :
```env
SERVICE_NAME=produits
DATABASE_URL=<votre_postgresql_url>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
MOUVEMENTSTOCK_SERVICE_URL=<url_service_mouvementstock>
PORT=8080
```

#### Service Fournisseurs
```bash
railway service create fournisseurs
railway up --service fournisseurs
```

Variables d'environnement :
```env
SERVICE_NAME=fournisseurs
DATABASE_URL=<votre_postgresql_url>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
PORT=8080
```

#### Service Commandes
```bash
railway service create commandes
railway up --service commandes
```

Variables d'environnement :
```env
SERVICE_NAME=commandes
DATABASE_URL=<votre_postgresql_url>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
FOURNISSEURS_SERVICE_URL=<url_service_fournisseurs>
PRODUITS_SERVICE_URL=<url_service_produits>
PORT=8080
```

#### Service MouvementStock
```bash
railway service create mouvementstock
railway up --service mouvementstock
```

Variables d'environnement :
```env
SERVICE_NAME=mouvementstock
DATABASE_URL=<votre_postgresql_url>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
PRODUITS_SERVICE_URL=<url_service_produits>
PORT=8080
```

## 🎯 Option 2 : Déploiement via le Dashboard Railway

1. **Créer un projet** sur https://railway.app
2. **Ajouter PostgreSQL** comme base de données
3. **Créer 5 services** (un pour chaque microservice)
4. Pour chaque service :
   - Connecter votre repo GitHub
   - Définir `Root Directory` (optionnel)
   - Ajouter les variables d'environnement
   - Définir `SERVICE_NAME` selon le service

## 🎯 Option 3 : Alternative - Déployer sur d'autres plateformes

### Render.com (Mieux adapté pour Docker Compose)
Render supporte nativement Docker Compose :
```bash
# Créer un render.yaml
```

### Heroku
Heroku nécessite un Procfile pour chaque service.

### VPS (DigitalOcean, Linode, AWS EC2)
La meilleure option pour Docker Compose :
```bash
# Sur le serveur
git clone <votre-repo>
cd tricol
docker-compose up -d
```

## 📋 Fichiers Créés

J'ai créé les fichiers suivants pour faciliter le déploiement :

1. **`start.sh`** - Script de démarrage qui gère tous les services
2. **`Dockerfile`** - Dockerfile générique pour tous les services
3. **`railway.json`** - Configuration Railway
4. **`RAILWAY_DEPLOYMENT.md`** - Ce guide

## 🔧 Configuration des Variables d'Environnement

Pour chaque service Railway, ajoutez ces variables :

### Variables communes
```env
SERVICE_NAME=<nom_du_service>
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_DATASOURCE_USERNAME=${PGUSER}
SPRING_DATASOURCE_PASSWORD=${PGPASSWORD}
PORT=8080
```

### Variables spécifiques par service

**Auth :**
```env
JWT_SECRET=your-super-secret-key-min-256-bits
JWT_EXPIRATION=3600000
```

**Commandes :**
```env
FOURNISSEURS_SERVICE_URL=https://<fournisseurs-service>.railway.app
PRODUITS_SERVICE_URL=https://<produits-service>.railway.app
```

**Produits :**
```env
MOUVEMENTSTOCK_SERVICE_URL=https://<mouvementstock-service>.railway.app
```

**MouvementStock :**
```env
PRODUITS_SERVICE_URL=https://<produits-service>.railway.app
```

## ⚠️ Limitations Railway

- **Plan gratuit** : Limite de 500h/mois pour tous les services
- **6 services** = consommation rapide du quota gratuit
- **Recommandation** : Utilisez un VPS pour production

## 💰 Coûts Estimés

### Railway
- Gratuit : 500h/mois (≈ 20 jours pour 1 service)
- Pro : $5/service/mois → ~$30/mois pour 6 services

### Alternative VPS (Recommandé pour production)
- DigitalOcean Droplet 2GB : $12/mois
- Peut héberger tous les services avec Docker Compose

## 🚀 Déploiement Rapide sur VPS (Recommandé)

```bash
# Sur votre VPS
sudo apt update
sudo apt install docker.io docker-compose git -y

# Cloner le projet
git clone <votre-repo> tricol
cd tricol

# Démarrer tous les services
docker-compose up -d

# Vérifier
docker-compose ps
docker-compose logs -f
```

## 📞 Support

Si vous rencontrez des problèmes :
1. Vérifiez les logs Railway de chaque service
2. Assurez-vous que DATABASE_URL est correctement configurée
3. Vérifiez que les URLs inter-services sont correctes
4. Consultez la documentation Railway : https://docs.railway.app

## ✅ Checklist de Déploiement

- [ ] PostgreSQL créé et accessible
- [ ] Service Auth déployé et fonctionne
- [ ] Service Fournisseurs déployé
- [ ] Service Produits déployé  
- [ ] Service MouvementStock déployé
- [ ] Service Commandes déployé
- [ ] Toutes les URLs inter-services configurées
- [ ] Variables d'environnement validées
- [ ] Tests des endpoints effectués

## 🎯 Ordre de Déploiement Recommandé

1. **PostgreSQL** (base de données)
2. **Auth** (pas de dépendances externes)
3. **Fournisseurs** (pas de dépendances externes)
4. **MouvementStock** (dépend de Produits mais démarrage indépendant)
5. **Produits** (dépend de MouvementStock)
6. **Commandes** (dépend de Fournisseurs et Produits)

Une fois tous les services déployés, mettez à jour les URLs inter-services dans les variables d'environnement.

