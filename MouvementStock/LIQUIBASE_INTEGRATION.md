# 🔄 Migration vers Liquibase

## ✅ Ce qui a été fait

Liquibase a été intégré avec succès dans le projet MouvementStock. Voici les changements apportés :

### 1. Dépendances ajoutées (pom.xml)
- ✅ `liquibase-core` - Bibliothèque principale
- ✅ `liquibase-maven-plugin` - Plugin pour exécuter des commandes Maven

### 2. Configuration (application.properties)
- ✅ Liquibase activé
- ✅ `spring.jpa.hibernate.ddl-auto` changé de `update` à `none`
- ✅ Chemin du changelog configuré

### 3. Structure des changelogs créée
```
src/main/resources/db/changelog/
├── db.changelog-master.xml                    # Fichier principal
└── changes/
    ├── 001-create-mouvement-stock-table.xml   # Création de la table
    ├── 002-add-indexes.xml                    # Index de performance
    └── 003-insert-sample-data.xml             # Données de test (dev)
```

### 4. Tables créées par les migrations
- **mouvement_stock** : Table principale avec tous les champs
- **Index de performance** :
  - `idx_mouvement_stock_produit_id`
  - `idx_mouvement_stock_type_mvt`
  - `idx_mouvement_stock_date_mvt`
  - `idx_mouvement_stock_ref_commande`
  - `idx_mouvement_stock_produit_date` (composé pour FIFO/CUMP)

### 5. Documentation
- ✅ `database/LIQUIBASE_README.md` - Guide complet d'utilisation
- ✅ `src/main/resources/liquibase.properties` - Configuration Maven

## 🚀 Démarrage rapide

### Première utilisation

1. **Vérifier la configuration de la base de données** dans `application.properties`
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/tricol_db
   spring.datasource.username=tricol_user
   spring.datasource.password=tricol_pass123
   ```

2. **Démarrer l'application**
   ```bash
   mvn spring-boot:run
   ```
   
   Liquibase va automatiquement :
   - Créer les tables `databasechangelog` et `databasechangeloglock`
   - Exécuter les changesets 001 et 002
   - Créer la table `mouvement_stock` avec tous les index

3. **Pour charger les données de test** (optionnel)
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
   ```

## 📋 Commandes utiles

### Via Maven
```bash
# Voir le statut des migrations
mvn liquibase:status

# Mettre à jour la base de données
mvn liquibase:update

# Générer le SQL sans l'exécuter
mvn liquibase:updateSQL

# Rollback
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

### Via SQL direct
```sql
-- Voir l'historique des migrations
SELECT * FROM databasechangelog ORDER BY dateexecuted DESC;

-- Voir le statut du verrou
SELECT * FROM databasechangeloglock;
```

## ⚠️ Points importants

1. **Ne jamais modifier `spring.jpa.hibernate.ddl-auto`** - Il doit rester sur `none`
2. **Les migrations s'exécutent au démarrage** - Pas besoin de commandes manuelles
3. **Les données de test (003)** ne se chargent qu'avec le profil `dev`
4. **Ne jamais modifier un changeset déjà appliqué** - Créer un nouveau changeset à la place

## 🔧 Ajouter une nouvelle migration

1. Créer un fichier `004-votre-changement.xml` dans `db/changelog/changes/`
2. Ajouter la référence dans `db.changelog-master.xml`
3. Redémarrer l'application (ou exécuter `mvn liquibase:update`)

Exemple :
```xml
<changeSet id="004-add-comment-column" author="votre-nom">
    <addColumn tableName="mouvement_stock">
        <column name="commentaire" type="TEXT"/>
    </addColumn>
</changeSet>
```

## 📚 Documentation complète

Voir `database/LIQUIBASE_README.md` pour :
- Guide détaillé d'utilisation
- Exemples de changesets
- Bonnes pratiques
- Dépannage

## 🎯 Prochaines étapes recommandées

1. ✅ Tester le démarrage de l'application
2. ✅ Vérifier que les tables sont créées
3. ✅ Tester les endpoints de valorisation FIFO/CUMP
4. 📝 Créer des migrations pour les évolutions futures du schéma

