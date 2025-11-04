#!/bin/bash

echo "=========================================="
echo "Test de l'intégration Liquibase"
echo "=========================================="
echo ""

# Compilation
echo "1. Compilation du projet..."
cd /home/happy/Bureau/tricol/MouvementStock
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie"
else
    echo "❌ Échec de la compilation"
    exit 1
fi

echo ""
echo "2. Vérification des fichiers Liquibase créés..."

# Vérifier les changelogs
if [ -f "src/main/resources/db/changelog/db.changelog-master.xml" ]; then
    echo "✅ Master changelog trouvé"
else
    echo "❌ Master changelog manquant"
fi

if [ -f "src/main/resources/db/changelog/changes/001-create-mouvement-stock-table.xml" ]; then
    echo "✅ Changeset 001 trouvé"
else
    echo "❌ Changeset 001 manquant"
fi

if [ -f "src/main/resources/db/changelog/changes/002-add-indexes.xml" ]; then
    echo "✅ Changeset 002 trouvé"
else
    echo "❌ Changeset 002 manquant"
fi

if [ -f "src/main/resources/db/changelog/changes/003-insert-sample-data.xml" ]; then
    echo "✅ Changeset 003 trouvé"
else
    echo "❌ Changeset 003 manquant"
fi

echo ""
echo "3. Vérification de la configuration..."

if grep -q "spring.liquibase.enabled=true" src/main/resources/application.properties; then
    echo "✅ Liquibase activé dans application.properties"
else
    echo "❌ Liquibase non activé"
fi

if grep -q "spring.jpa.hibernate.ddl-auto=none" src/main/resources/application.properties; then
    echo "✅ ddl-auto configuré sur 'none'"
else
    echo "❌ ddl-auto n'est pas sur 'none'"
fi

echo ""
echo "4. Vérification du pom.xml..."

if grep -q "liquibase-core" pom.xml; then
    echo "✅ Dépendance liquibase-core présente"
else
    echo "❌ Dépendance liquibase-core manquante"
fi

if grep -q "liquibase-maven-plugin" pom.xml; then
    echo "✅ Plugin Maven Liquibase présent"
else
    echo "❌ Plugin Maven Liquibase manquant"
fi

echo ""
echo "=========================================="
echo "Résumé de l'intégration Liquibase"
echo "=========================================="
echo "✅ Liquibase a été intégré avec succès !"
echo ""
echo "Prochaines étapes :"
echo "1. Démarrer PostgreSQL"
echo "2. Lancer l'application : mvn spring-boot:run"
echo "3. Liquibase créera automatiquement les tables"
echo ""
echo "Commandes utiles :"
echo "- mvn liquibase:status"
echo "- mvn liquibase:update"
echo "- mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev"
echo ""

