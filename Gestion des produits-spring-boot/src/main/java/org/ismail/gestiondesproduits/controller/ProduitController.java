package org.ismail.gestiondesproduits.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProduitController {

    @Autowired
    public ProduitService produitService;

    @Operation
    (summary = "Create a new product",
     description = "This endpoint allows you to create a new product by providing the product details in the request body.")
    @PostMapping
    public Produit creat(@RequestBody Produit p) {
        return produitService.save(p);
    }

    @Tag( name = "Find Product by ID", description = "Retrieve a product using its unique ID")
    @GetMapping("/{id}")
    public Produit findById(Long id) {
        return produitService.findById(id);
    }

    @Tag( name = "Get All Products", description = "Retrieve a list of all products")
    @GetMapping
    public List<Produit> findAll() {
        return produitService.findAllProduits();
    }

    @Tag( name = "Delete Product", description = "Delete a product by providing its details")
    @DeleteMapping
    public void delete(Produit p) {
        produitService.delete(p);
    }

    @Tag( name = "Update Product", description = "Update an existing product by providing the updated product details")
    @PutMapping
    public Produit update(Produit p) {
        return produitService.update(p);
    }

}
