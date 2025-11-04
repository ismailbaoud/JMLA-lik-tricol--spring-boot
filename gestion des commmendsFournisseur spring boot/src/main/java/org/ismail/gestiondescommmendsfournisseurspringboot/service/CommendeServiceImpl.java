package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.FournisseurDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommandeProduitRepository;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommendeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommendeServiceImpl implements CommendeService {

    @Autowired
    public CommendeRepository commendeRepository;

    @Autowired
    public CommandeProduitRepository commandeProduitRepository;

    @Value("${mouvements.service.url}")
    private String mouvementsServiceUrl;

    @Override
    public List<Commande> findAll() {
        return commendeRepository.findAll();
    }

    @Override
    public Optional<Commande> creerCommende(Commande commande , FournisseurDTO fournisseurDTO, ProduitDTO produitDTO) {
        if(produitDTO != null && fournisseurDTO != null) {
            Double prixTotal = produitDTO.getPrix() * commande.getQuantiteProduit();
            commande.setIdFournisseur(fournisseurDTO.getId());
            commande.setIdProduit(produitDTO.getId());
            commande.setNomProduit(produitDTO.getNom());
            commande.setPrixProduit(prixTotal);
        } else if (produitDTO == null) {
            throw new RuntimeException("Produit non trouvé");
        } else {
            throw new RuntimeException("Fournisseur non trouvé");
        }

        return Optional.of(commendeRepository.save(commande));
    }

    @Override
    public Commande findById(Long id) {
        return commendeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        commendeRepository.deleteById(id);
    }

    @Override
    public Commande updateCommande(Long id, Commande commandeDetails) {
        Commande commande = commendeRepository.findById(id).orElse(null);
        if (commande != null) {
            commande.setNomProduit(commandeDetails.getNomProduit());
            commande.setPrixProduit(commandeDetails.getPrixProduit());
            commande.setQuantiteProduit(commandeDetails.getQuantiteProduit());
            commande.setStatus(commandeDetails.getStatus());
            commande.setIdProduit(commandeDetails.getIdProduit());
            commande.setIdFournisseur(commandeDetails.getIdFournisseur());
            return commendeRepository.save(commande);
        }
        return null;
    }

    @Override
    public Commande updateCommendeStatus(Long id, CommendeStatus status) {
        Commande commande = commendeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'id: " + id));

        commande.setStatus(status);
        Commande savedCommande = commendeRepository.save(commande);
        if(status == CommendeStatus.LIVREE) {
            List<CommandeProduit> commandeProduits = commandeProduitRepository.findByCommandeId(id);

            if (commandeProduits.isEmpty()) {
                System.out.println("Aucun produit trouvé pour la commande ID: " + id);
                return savedCommande;
            }

            RestTemplate restTemplate = new RestTemplate();

            for (CommandeProduit commandeProduit : commandeProduits) {
                System.out.println("Produit dans la commande: ID=" + commandeProduit.getProduitId()
                        + ", Quantité=" + commandeProduit.getQuantite());

                Map<String, Object> mouvementData = new HashMap<>();
                mouvementData.put("produitId", commandeProduit.getProduitId());
                mouvementData.put("typeMvt", "SORTIE");
                mouvementData.put("quantite", commandeProduit.getQuantite());
                mouvementData.put("prixAchat", commandeProduit.getUnitPrice());
                mouvementData.put("refCommande", id);

                try {
                    restTemplate.postForObject(
                            mouvementsServiceUrl + "/api/v1/mouvements",
                            mouvementData,
                            Object.class
                    );

                    System.out.println("Mouvement de stock créé pour le produit ID: " + commandeProduit.getProduitId());
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'envoi du mouvement pour le produit ID "
                            + commandeProduit.getProduitId() + ": " + e.getMessage());
                    System.err.println("Le service de mouvement de stock n'est pas accessible. "
                            + "Le statut de la commande a été mis à jour mais le mouvement n'a pas été enregistré.");
                }
            }
        }

        return savedCommande;
    }

}
