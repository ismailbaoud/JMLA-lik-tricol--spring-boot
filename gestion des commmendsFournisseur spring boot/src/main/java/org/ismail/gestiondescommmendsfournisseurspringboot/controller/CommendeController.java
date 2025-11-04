package org.ismail.gestiondescommmendsfournisseurspringboot.controller;

import org.ismail.gestiondescommmendsfournisseurspringboot.dto.FournisseurDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.UpdateStatusRequest;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.ismail.gestiondescommmendsfournisseurspringboot.service.CommendeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/commandes")
public class CommendeController {

    @Autowired
    public CommendeServiceImpl commendeServiceImpl;

    @Value("${fournisseurs.service.url}")
    private String fournisseursServiceUrl;

    @Value("${produits.service.url}")
    private String produitsServiceUrl;

    @PostMapping
    public Commande creerCommende(@RequestBody Commande c) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            FournisseurDTO fournisseurDTO = restTemplate.getForObject(
                fournisseursServiceUrl + "/api/v0/fournisseurs/1", 
                FournisseurDTO.class
            );
            ProduitDTO produitDTO = restTemplate.getForObject(
                produitsServiceUrl + "/api/v1/products/1", 
                ProduitDTO.class
            );
            System.out.println(produitDTO);
            return commendeServiceImpl.creerCommende(c , fournisseurDTO , produitDTO ).orElseThrow();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{id}")
    public Commande findById(@RequestParam("id") Long id) {
        return commendeServiceImpl.findById(id);
    }

    @GetMapping
    public List<Commande> findAll() {
        return commendeServiceImpl.findAll();
    }

    @DeleteMapping
    public void deleteById(@RequestParam("id") Long id) {
        commendeServiceImpl.deleteById(id);
    }

    @PutMapping
    public Commande updateCommande(@RequestParam("id") Long id, @RequestBody Commande commandeDetails) {
        return commendeServiceImpl.updateCommande(id, commandeDetails);
    }

    @PatchMapping("/{id}")
    public Commande updateCommendeStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return commendeServiceImpl.updateCommendeStatus(id, request.getStatus());
    }

}