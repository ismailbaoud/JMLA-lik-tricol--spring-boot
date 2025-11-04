package org.ismail.gestiondesproduits.service;

import org.ismail.gestiondesproduits.exception.InvalidQuantityException;
import org.ismail.gestiondesproduits.exception.MouvementStockException;
import org.ismail.gestiondesproduits.exception.ProduitNotFoundException;
import org.ismail.gestiondesproduits.exception.ProduitSaveException;
import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProduitService {

    @Autowired
    public ProduitRepository produitRepository;
    private final RestClient restClient;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8086/api/v1/mouvements")
                .build();
    }

    @Transactional
    public Produit save(Produit p) {
        if (p == null) {
            throw new ProduitSaveException("Le produit ne peut pas être null");
        }

        if (p.getQuantity() != null && p.getQuantity() < 0) {
            throw new InvalidQuantityException(p.getQuantity());
        }

        Produit savedProduit;
        try {
            savedProduit = produitRepository.save(p);
        } catch (Exception e) {
            throw new ProduitSaveException("Erreur lors de la sauvegarde du produit", e);
        }

        Map<String, Object> mouvementData = new HashMap<>();
        mouvementData.put("produitId", savedProduit.getId());
        mouvementData.put("typeMvt", "ENTREE");
        mouvementData.put("quantite", savedProduit.getQuantity());
        mouvementData.put("prixAchat", savedProduit.getUnitPrice());
        mouvementData.put("refCommande", null);

        try {
            restClient.post()
                    .uri("")
                    .body(mouvementData)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("✅ Mouvement de stock créé pour le produit ID: " + savedProduit.getId());
        } catch (Exception e) {
            System.err.println("⚠️ Erreur lors de l'envoi du mouvement: " + e.getMessage());
            // On lance l'exception pour informer que le mouvement n'a pas été créé
            throw new MouvementStockException(
                "Le service de mouvement de stock n'est pas accessible. Le produit a été créé mais le mouvement n'a pas été enregistré.",
                e
            );
        }

        return savedProduit;
    }

    public void delete(Produit p) {
        if (p == null || p.getId() == null) {
            throw new ProduitNotFoundException("Le produit à supprimer n'existe pas");
        }

        try {
            produitRepository.delete(p);
        } catch (Exception e) {
            throw new ProduitSaveException("Erreur lors de la suppression du produit", e);
        }
    }

    public Produit update(Produit p) {
        if (p == null || p.getId() == null) {
            throw new ProduitNotFoundException("Le produit à mettre à jour n'existe pas");
        }

        if (!produitRepository.existsById(p.getId())) {
            throw new ProduitNotFoundException(p.getId());
        }

        if (p.getQuantity() != null && p.getQuantity() < 0) {
            throw new InvalidQuantityException(p.getQuantity());
        }

        try {
            return produitRepository.save(p);
        } catch (Exception e) {
            throw new ProduitSaveException("Erreur lors de la mise à jour du produit", e);
        }
    }

    public List<Produit> findAllProduits() {
        try {
            return produitRepository.findAll();
        } catch (Exception e) {
            throw new ProduitSaveException("Erreur lors de la récupération des produits", e);
        }
    }

    public Produit findById(Long id) {
        if (id == null) {
            throw new ProduitNotFoundException("L'ID du produit ne peut pas être null");
        }

        return produitRepository.findById(id)
                .orElseThrow(() -> new ProduitNotFoundException(id));
    }

    @Transactional
    public Produit addQuantity(Long productId, Integer quantityToAdd, Double prixAchat) {
        if (productId == null) {
            throw new ProduitNotFoundException("L'ID du produit ne peut pas être null");
        }

        if (quantityToAdd == null || quantityToAdd <= 0) {
            throw new InvalidQuantityException("La quantité à ajouter doit être positive et non null");
        }

        Produit produit = findById(productId);

        Integer currentQuantity = produit.getQuantity() != null ? produit.getQuantity() : 0;
        produit.setQuantity(currentQuantity + quantityToAdd);

        Produit updatedProduit;
        try {
            updatedProduit = produitRepository.save(produit);
        } catch (Exception e) {
            throw new ProduitSaveException("Erreur lors de la mise à jour de la quantité", e);
        }

        Map<String, Object> mouvementData = new HashMap<>();
        mouvementData.put("produitId", produit.getId());
        mouvementData.put("typeMvt", "ENTREE");
        mouvementData.put("quantite", quantityToAdd);
        mouvementData.put("prixAchat", prixAchat != null ? prixAchat : produit.getUnitPrice());
        mouvementData.put("refCommande", null);

        try {
            restClient.post()
                    .uri("")
                    .body(mouvementData)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("✅ Mouvement de stock créé pour le produit ID: " + produit.getId());
        } catch (Exception e) {
            System.err.println("⚠️ Erreur lors de l'envoi du mouvement: " + e.getMessage());
            throw new MouvementStockException(
                "Le service de mouvement de stock n'est pas accessible. La quantité a été mise à jour mais le mouvement n'a pas été enregistré.",
                e
            );
        }

        return updatedProduit;
    }
}

