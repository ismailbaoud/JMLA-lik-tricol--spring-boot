package org.ismail.gestiondesproduits.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProduitController {


    @Tag( name = "Produit", description = "API pour la gestion des produits")
    @GetMapping
    public String getAllProducts() {
        return "List of products";
    }

}
