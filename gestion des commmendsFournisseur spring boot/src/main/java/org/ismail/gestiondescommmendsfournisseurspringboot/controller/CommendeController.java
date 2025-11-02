package org.ismail.gestiondescommmendsfournisseurspringboot.controller;

import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CreateCommandeRequest;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.FournisseurDTO;
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

    @PostMapping("/with-products")
    public CommandeResponseDTO creerCommandeAvecProduits(@RequestBody CreateCommandeRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            FournisseurDTO fournisseurDTO = restTemplate.getForObject(
                fournisseursServiceUrl + "/api/v0/fournisseurs/" + request.getIdFournisseur(),
                FournisseurDTO.class
            );

            return commendeServiceImpl.creerCommandeAvecProduits(request, fournisseurDTO);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la commande avec produits: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la commande: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CommandeResponseDTO findById(@PathVariable("id") Long id) {
        return commendeServiceImpl.findById(id);
    }

    @GetMapping
    public List<CommandeResponseDTO> findAll() {
        return commendeServiceImpl.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        commendeServiceImpl.deleteById(id);
    }

    @PatchMapping("/{id}")
    public CommandeResponseDTO updateCansel(@PathVariable("id") Long id,@RequestBody CreateCommandeRequest request) {
        try {
            return commendeServiceImpl.updatStatusCommende(id, request.getStatus());
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour de la commande: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la commande: " + e.getMessage());
        }
    }
}
