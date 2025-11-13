#!/bin/bash

# Navigate to project root
cd /home/happy/Bureau/tricol

# Create and checkout the test branch
git checkout -b test

echo "📦 Committing MouvementStock tests..."

# Commit MouvementStock controller integration test
git add "MouvementStock/src/test/java/org/ismail/mouvementstock/controller/MouvementStockControllerIntegrationTest.java"
git commit -m "test(MouvementStock): add controller integration tests

- Test create mouvement endpoint (POST /api/v1/mouvements)
- Test get all mouvements with pagination
- Test get mouvement by ID
- Test get mouvements filtered by product ID
- Test get mouvements filtered by type (ENTREE/SORTIE)
- Test get mouvements filtered by order reference
- Test delete mouvement endpoint
- Mock MouvementStockService with WebMvcTest
- Validate HTTP status codes and JSON responses"

echo "✅ MouvementStock tests committed"

echo "📦 Committing Gestion des produits tests..."

# Commit Produit controller integration test
git add "Gestion des produits-spring-boot/src/test/java/org/ismail/gestiondesproduits/controller/ProduitControllerIntegrationTest.java"
git commit -m "test(GestionProduits): add ProduitController integration tests

- Test get all products endpoint (GET /api/v1/products)
- Test get product by ID
- Test create product endpoint (POST /api/v1/products)
- Test add quantity to product (PUT /api/v1/products/add-quantity/{id})
- Test reduce quantity from product (PATCH /api/v1/products/reduce-quantity/{id})
- Mock ProduitService and ProduitMapper
- Setup test fixtures with Laptop and Mouse products
- Validate JSON responses and status codes"

echo "✅ Gestion des produits controller tests committed"

# Commit any service tests if they exist and have content
if [ -f "Gestion des produits-spring-boot/src/test/java/org/ismail/gestiondesproduits/service/ProduitServiceTest.java" ]; then
    # Check if file has content (more than just package and class declaration)
    if [ $(wc -l < "Gestion des produits-spring-boot/src/test/java/org/ismail/gestiondesproduits/service/ProduitServiceTest.java") -gt 5 ]; then
        git add "Gestion des produits-spring-boot/src/test/java/org/ismail/gestiondesproduits/service/ProduitServiceTest.java"
        git commit -m "test(GestionProduits): add ProduitService unit tests

- Unit tests for ProduitService business logic
- Mock repository layer
- Test CRUD operations
- Validate business rules"
    fi
fi

# Find and commit any other test files with content
echo "🔍 Looking for other test files..."
find . -path "*/src/test/java/**/*Test.java" -type f | while read -r testfile; do
    # Skip already committed files
    if git diff --cached --name-only | grep -q "$testfile"; then
        continue
    fi
    
    # Check if file has meaningful content (more than 5 lines)
    if [ $(wc -l < "$testfile") -gt 5 ]; then
        module=$(echo "$testfile" | cut -d'/' -f2)
        classname=$(basename "$testfile" .java)
        
        git add "$testfile"
        git commit -m "test($module): add $classname

- Add test coverage for $module module
- Verify business logic and API endpoints"
        
        echo "✅ Committed $classname"
    fi
done

echo ""
echo "🎉 All tests committed to 'test' branch!"
echo ""
echo "📋 Summary of commits:"
git log --oneline --graph
echo ""
echo "📤 To push to remote, run:"
echo "   git push origin test"
