package org.ismail.gestiondesproduits.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ismail.gestiondesproduits.dto.ProduitDTO;
import org.ismail.gestiondesproduits.mapper.ProduitMapper;
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

    @Autowired
    public ProduitMapper produitMapper;

//    @Operation
//    (summary = "Create a new product",
//     description = "This endpoint allows you to create a new product by providing the product details in the request body.")
    @PostMapping
    public Produit creatProduit(@RequestBody ProduitDTO p) {
        try {
            System.out.println(p);
            Produit pr = produitMapper.dtoToEntity(p);
            return produitService.save(pr);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du produit: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Tag( name = "Find Product by ID", description = "Retrieve a product using its unique ID")
    @GetMapping("/{id}")
    public Produit findById(@PathVariable("id") Long id) {
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
