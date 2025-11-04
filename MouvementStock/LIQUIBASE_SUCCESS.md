# ✅ LIQUIBASE - INTÉGRATION TERMINÉE

## 📋 Ce qui a été fait

### 1. **Dépendances ajoutées** (pom.xml)
```xml
<!-- Liquibase Core -->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>

<!-- Liquibase Maven Plugin -->
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <version>4.20.0</version>
</plugin>
```

### 2. **Configuration** (application.properties)
```properties
# Liquibase activé
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.default-schema=public
spring.liquibase.liquibase-schema=public

# IMPORTANT : ddl-auto mis sur "none" (Liquibase gère le schéma)
spring.jpa.hibernate.ddl-auto=none
```

### 3. **Structure des changelogs créée**
```
src/main/resources/
└── db/
    └── changelog/
        ├── db.changelog-master.xml           # Fichier principal
        └── changes/
            ├── 001-create-mouvement-stock-table.xml  # Création table
            ├── 002-add-indexes.xml                    # Index performance
            └── 003-insert-sample-data.xml             # Données test (dev)
```

### 4. **Fichiers de configuration créés**
- ✅ `src/main/resources/liquibase.properties` - Configuration Maven
- ✅ `database/LIQUIBASE_README.md` - Guide complet
- ✅ `LIQUIBASE_INTEGRATION.md` - Documentation intégration

### 5. **Fonctions FIFO et CUMP ajoutées**
- ✅ `StockValuationDTO.java` - DTO pour résultats
- ✅ Méthodes dans `MouvementStockService` et `MouvementStockServiceImpl`
- ✅ Endpoints REST dans `MouvementStockController`

## 🚀 DÉMARRAGE RAPIDE

### Option 1 : Démarrage normal (sans données de test)
```bash
mvn spring-boot:run
```

### Option 2 : Avec données de test
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```

## 📊 Ce qui se passe au démarrage

1. **Liquibase s'exécute automatiquement** au démarrage de l'application
2. **Crée les tables de tracking** :
   - `databasechangelog` - Historique des migrations
   - `databasechangeloglock` - Verrou d'exécution

3. **Exécute les changesets** :
   - 001 : Crée la table `mouvement_stock`
   - 002 : Ajoute 5 index de performance
   - 003 : Insère données de test (uniquement avec profil `dev`)

## 🎯 ENDPOINTS DISPONIBLES

### Mouvements de stock (existants)
```
POST   /api/v1/mouvements                    # Créer mouvement
GET    /api/v1/mouvements                    # Liste tous
GET    /api/v1/mouvements/{id}               # Par ID
GET    /api/v1/mouvements/produit/{id}       # Par produit
GET    /api/v1/mouvements/type/{type}        # Par type
GET    /api/v1/mouvements/commande/{ref}     # Par commande
DELETE /api/v1/mouvements/{id}               # Supprimer
```

### Valorisation FIFO/CUMP (NOUVEAUX) ⭐
```
GET /api/v1/mouvements/valuation/fifo                    # FIFO tous produits
GET /api/v1/mouvements/valuation/fifo/produit/{id}       # FIFO un produit
GET /api/v1/mouvements/valuation/cump                    # CUMP tous produits
GET /api/v1/mouvements/valuation/cump/produit/{id}       # CUMP un produit
GET /api/v1/mouvements/valuation/both                    # FIFO+CUMP tous
GET /api/v1/mouvements/valuation/both/produit/{id}       # FIFO+CUMP un produit
```

## 🔧 COMMANDES LIQUIBASE UTILES

### Via Maven
```bash
# Voir le statut des migrations
mvn liquibase:status

# Mettre à jour la base (si nécessaire)
mvn liquibase:update

# Voir le SQL qui sera exécuté
mvn liquibase:updateSQL

# Rollback du dernier changeset
mvn liquibase:rollback -Dliquibase.rollbackCount=1

# Marquer la base avec un tag
mvn liquibase:tag -Dliquibase.tag=version-1.0
```

### Via SQL
```sql
-- Voir l'historique des migrations
SELECT * FROM databasechangelog ORDER BY dateexecuted DESC;

-- Vérifier le verrou
SELECT * FROM databasechangeloglock;

-- Libérer le verrou (si bloqué)
DELETE FROM databasechangeloglock;
```

## 📝 AJOUTER UNE NOUVELLE MIGRATION

### Étape 1 : Créer le fichier XML
Créer `src/main/resources/db/changelog/changes/004-votre-changement.xml` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <changeSet id="004-add-comment-column" author="votre-nom">
        <comment>Ajout d'une colonne commentaire</comment>
        
        <addColumn tableName="mouvement_stock">
            <column name="commentaire" type="TEXT">
                <constraints nullable="true"/>
            </column>
        </addColumn>
        
        <rollback>
            <dropColumn tableName="mouvement_stock" columnName="commentaire"/>
        </rollback>
    </changeSet>

</databaseChangeLog>
```

### Étape 2 : Inclure dans le master
Modifier `db.changelog-master.xml` :

```xml
<include file="db/changelog/changes/001-create-mouvement-stock-table.xml"/>
<include file="db/changelog/changes/002-add-indexes.xml"/>
<include file="db/changelog/changes/003-insert-sample-data.xml"/>
<include file="db/changelog/changes/004-votre-changement.xml"/>  <!-- NOUVEAU -->
```

### Étape 3 : Redémarrer l'application
```bash
mvn spring-boot:run
```

Liquibase détectera et appliquera automatiquement le nouveau changeset !

## ⚠️ RÈGLES IMPORTANTES

1. ❌ **Ne JAMAIS modifier un changeset déjà appliqué en production**
2. ✅ **Toujours créer un nouveau changeset pour les modifications**
3. ✅ **Utiliser des IDs séquentiels** (001, 002, 003...)
4. ✅ **Toujours ajouter un commentaire descriptif**
5. ✅ **Fournir un rollback** quand c'est possible
6. ✅ **Tester en local** avant de déployer
7. ✅ **Utiliser les contextes** (dev, prod) pour les données de test

## 🧪 TESTER L'INTÉGRATION

```bash
# 1. S'assurer que PostgreSQL est démarré
sudo systemctl status postgresql

# 2. Compiler le projet
mvn clean compile

# 3. Démarrer l'application
mvn spring-boot:run

# 4. Vérifier les logs Liquibase
# Vous devriez voir :
# "Running Changeset: db/changelog/changes/001-create-mouvement-stock-table.xml..."
# "Running Changeset: db/changelog/changes/002-add-indexes.xml..."

# 5. Tester un endpoint de valorisation
curl http://localhost:8086/api/v1/mouvements/valuation/both
```

## 📚 DOCUMENTATION COMPLÈTE

- **`database/LIQUIBASE_README.md`** - Guide détaillé avec exemples
- **`LIQUIBASE_INTEGRATION.md`** - Résumé de l'intégration
- **`src/main/resources/liquibase.properties`** - Configuration Maven

## 🎉 RÉSUMÉ

✅ Liquibase est **100% opérationnel** dans votre projet !

✅ Les fonctions **FIFO et CUMP** sont implémentées et testables via API REST !

✅ Les migrations se feront **automatiquement** au démarrage de l'application !

**Prochaine étape** : Démarrez l'application et testez les endpoints de valorisation ! 🚀

