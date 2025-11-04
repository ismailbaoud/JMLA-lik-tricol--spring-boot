# Liquibase - Guide d'utilisation

## 📋 Vue d'ensemble

Liquibase est intégré dans ce projet pour gérer les migrations de base de données de manière versionnée et contrôlée.

## 🏗️ Structure des fichiers

```
src/main/resources/db/changelog/
├── db.changelog-master.xml              # Fichier principal qui inclut tous les changesets
└── changes/
    ├── 001-create-mouvement-stock-table.xml  # Création de la table principale
    ├── 002-add-indexes.xml                    # Ajout des index de performance
    └── 003-insert-sample-data.xml             # Données de test (contexte dev uniquement)
```

## ⚙️ Configuration

### Application Properties

```properties
# Liquibase activé
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.default-schema=public

# Important : ddl-auto doit être sur "none" pour laisser Liquibase gérer le schéma
spring.jpa.hibernate.ddl-auto=none
```

## 🚀 Utilisation

### Au démarrage de l'application

Liquibase s'exécute automatiquement au démarrage et applique tous les changesets non encore exécutés.

### Charger les données de test

Pour charger les données de test (changeset 003), démarrez l'application avec le profil `dev` :

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```

Ou dans `application.properties` :
```properties
spring.profiles.active=dev
```

### Commandes Maven Liquibase

```bash
# Voir le statut des migrations
mvn liquibase:status

# Mettre à jour la base de données
mvn liquibase:update

# Générer le SQL sans l'exécuter
mvn liquibase:updateSQL

# Rollback du dernier changeset
mvn liquibase:rollback -Dliquibase.rollbackCount=1

# Rollback jusqu'à une date
mvn liquibase:rollback -Dliquibase.rollbackDate=2025-01-01

# Rollback jusqu'à un tag
mvn liquibase:rollback -Dliquibase.rollbackTag=version-1.0

# Marquer la base avec un tag
mvn liquibase:tag -Dliquibase.tag=version-1.0

# Générer un diff entre la base et les entités JPA
mvn liquibase:diff
```

## 📝 Créer un nouveau changeset

### 1. Créer un fichier XML

Créez un nouveau fichier dans `src/main/resources/db/changelog/changes/` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <changeSet id="004-add-column-exemple" author="votre-nom">
        <comment>Description de votre changement</comment>
        
        <addColumn tableName="mouvement_stock">
            <column name="nouvelle_colonne" type="VARCHAR(255)">
                <constraints nullable="true"/>
            </column>
        </addColumn>
        
        <rollback>
            <dropColumn tableName="mouvement_stock" columnName="nouvelle_colonne"/>
        </rollback>
    </changeSet>

</databaseChangeLog>
```

### 2. Inclure dans le master

Ajoutez la référence dans `db.changelog-master.xml` :

```xml
<include file="db/changelog/changes/004-add-column-exemple.xml"/>
```

## 🔄 Tables de tracking Liquibase

Liquibase crée deux tables pour suivre les migrations :

- **`databasechangelog`** : Historique de tous les changesets exécutés
- **`databasechangeloglock`** : Verrou pour éviter les exécutions concurrentes

## 🎯 Bonnes pratiques

1. **Ne jamais modifier un changeset déjà appliqué en production**
2. **Toujours créer un nouveau changeset pour les modifications**
3. **Utiliser des IDs uniques et séquentiels** (001, 002, 003...)
4. **Ajouter des commentaires descriptifs**
5. **Toujours fournir un rollback** quand c'est possible
6. **Tester les migrations en local** avant de les déployer
7. **Utiliser les contextes** (dev, prod) pour les données de test

## 🔧 Exemples d'opérations courantes

### Ajouter une colonne
```xml
<addColumn tableName="mouvement_stock">
    <column name="commentaire" type="TEXT"/>
</addColumn>
```

### Modifier une colonne
```xml
<modifyDataType tableName="mouvement_stock" columnName="quantite" newDataType="BIGINT"/>
```

### Créer un index
```xml
<createIndex indexName="idx_example" tableName="mouvement_stock">
    <column name="colonne_name"/>
</createIndex>
```

### Ajouter une contrainte
```xml
<addNotNullConstraint tableName="mouvement_stock" columnName="produit_id"/>
```

### Insérer des données
```xml
<insert tableName="mouvement_stock">
    <column name="produit_id" valueNumeric="1"/>
    <column name="type_mvt" value="ENTREE"/>
</insert>
```

## 🐛 Dépannage

### Erreur : "Waiting for changelog lock"

Si Liquibase reste bloqué, libérez le verrou manuellement :

```sql
DELETE FROM databasechangeloglock;
```

### Forcer le marquage d'un changeset comme exécuté

```bash
mvn liquibase:changelogSync
```

### Nettoyer les checksums (après modification accidentelle)

```bash
mvn liquibase:clearCheckSums
```

## 📚 Documentation officielle

- [Liquibase Documentation](https://docs.liquibase.com/)
- [Liquibase Spring Boot](https://docs.liquibase.com/tools-integrations/springboot/springboot.html)

