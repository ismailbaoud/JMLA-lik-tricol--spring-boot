package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeProduitDetailDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CreateCommandeRequest;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.FournisseurDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitItemDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommendeRepository;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommandeProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommendeServiceImpl implements CommendeService {

    @Autowired
    public CommendeRepository commendeRepository;

    @Autowired
    public CommandeProduitRepository commandeProduitRepository;

    @Value("${produits.service.url}")
    private String produitsServiceUrl;

    @Override
    public List<CommandeResponseDTO> findAll() {
        List<Commande> commandes = commendeRepository.findAll();
        List<CommandeResponseDTO> responses = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        for (Commande commande : commandes) {
            CommandeResponseDTO response = buildCommandeResponse(commande, restTemplate);
            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional
    public CommandeResponseDTO creerCommandeAvecProduits(CreateCommandeRequest request, FournisseurDTO fournisseurDTO) {
        if (fournisseurDTO == null) {
            throw new RuntimeException("Fournisseur non trouvé");
        }

        if (request.getProduits() == null || request.getProduits().isEmpty()) {
            throw new RuntimeException("La liste des produits ne peut pas être vide");
        }

        Commande commande = new Commande();
        commande.setNomProduit(request.getNomProduit());
        commande.setStatus(request.getStatus());
        commande.setIdFournisseur(fournisseurDTO.getId());

        Commande savedCommande = commendeRepository.save(commande);

        RestTemplate restTemplate = new RestTemplate();
        List<CommandeProduitDetailDTO> commandeProduitsDetails = new ArrayList<>();

        for (ProduitItemDTO item : request.getProduits()) {
            try {
                ProduitDTO produitDTO = restTemplate.getForObject(
                    produitsServiceUrl + "/api/v1/products/" + item.getIdProduit(),
                    ProduitDTO.class
                );

                if (produitDTO == null) {
                    throw new RuntimeException("Produit avec ID " + item.getIdProduit() + " non trouvé");
                }

                CommandeProduit commandeProduit = new CommandeProduit();
                commandeProduit.setCommandeId(savedCommande.getId());
                commandeProduit.setProduitId(produitDTO.getId());
                commandeProduit.setQuantite(item.getQuantite());
                commandeProduit.setUnitPrice(produitDTO.getUnitPrice());

                CommandeProduit savedCommandeProduit = commandeProduitRepository.save(commandeProduit);

                CommandeProduitDetailDTO detailDTO = new CommandeProduitDetailDTO();
                detailDTO.setId(savedCommandeProduit.getId());
                detailDTO.setCommandeId(savedCommandeProduit.getCommandeId());
                detailDTO.setProduit(produitDTO);
                detailDTO.setQuantite(savedCommandeProduit.getQuantite());
                detailDTO.setUnitPrice(savedCommandeProduit.getUnitPrice());
                detailDTO.setLineTotal(savedCommandeProduit.getLineTotal());

                commandeProduitsDetails.add(detailDTO);

            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la récupération du produit " + item.getIdProduit() + ": " + e.getMessage());
            }
        }

        CommandeResponseDTO response = new CommandeResponseDTO();
        response.setId(savedCommande.getId());
        response.setNomProduit(savedCommande.getNomProduit());
        response.setStatus(savedCommande.getStatus());
        response.setFournisseur(fournisseurDTO);
        response.setProduits(commandeProduitsDetails);

        return response;
    }

    @Override
    public CommandeResponseDTO findById(Long id) {
        Commande commande = commendeRepository.findById(id).orElse(null);
        if (commande == null) {
            return null;
        }

        RestTemplate restTemplate = new RestTemplate();
        return buildCommandeResponse(commande, restTemplate);
    }

    @Override
    public void deleteById(Long id) {
        commendeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommandeResponseDTO updateCommande(Long id, CreateCommandeRequest commandeDetails, FournisseurDTO fournisseurDTO) {
        Commande commande = commendeRepository.findById(id).orElse(null);
        if (commande == null) {
            return null;
        }

        if (fournisseurDTO == null) {
            throw new RuntimeException("Fournisseur non trouvé");
        }

        commande.setNomProduit(commandeDetails.getNomProduit());
        commande.setStatus(commandeDetails.getStatus());
        commande.setIdFournisseur(fournisseurDTO.getId());

        Commande savedCommande = commendeRepository.save(commande);

        commandeProduitRepository.findByCommandeId(id).forEach(cp -> commandeProduitRepository.deleteById(cp.getId()));

        RestTemplate restTemplate = new RestTemplate();
        List<CommandeProduitDetailDTO> commandeProduitsDetails = new ArrayList<>();

        for (ProduitItemDTO item : commandeDetails.getProduits()) {
            try {
                ProduitDTO produitDTO = restTemplate.getForObject(
                    produitsServiceUrl + "/api/v1/products/" + item.getIdProduit(),
                    ProduitDTO.class
                );

                if (produitDTO == null) {
                    throw new RuntimeException("Produit avec ID " + item.getIdProduit() + " non trouvé");
                }

                CommandeProduit commandeProduit = new CommandeProduit();
                commandeProduit.setCommandeId(savedCommande.getId());
                commandeProduit.setProduitId(produitDTO.getId());
                commandeProduit.setQuantite(item.getQuantite());
                commandeProduit.setUnitPrice(produitDTO.getUnitPrice());

                CommandeProduit savedCommandeProduit = commandeProduitRepository.save(commandeProduit);

                CommandeProduitDetailDTO detailDTO = new CommandeProduitDetailDTO();
                detailDTO.setId(savedCommandeProduit.getId());
                detailDTO.setCommandeId(savedCommandeProduit.getCommandeId());
                detailDTO.setProduit(produitDTO);
                detailDTO.setQuantite(savedCommandeProduit.getQuantite());
                detailDTO.setUnitPrice(savedCommandeProduit.getUnitPrice());
                detailDTO.setLineTotal(savedCommandeProduit.getLineTotal());

                commandeProduitsDetails.add(detailDTO);

            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la récupération du produit " + item.getIdProduit() + ": " + e.getMessage());
            }
        }

        CommandeResponseDTO response = new CommandeResponseDTO();
        response.setId(savedCommande.getId());
        response.setNomProduit(savedCommande.getNomProduit());
        response.setStatus(savedCommande.getStatus());
        response.setFournisseur(fournisseurDTO);
        response.setProduits(commandeProduitsDetails);

        return response;
    }

    @Override
    public CommandeResponseDTO updatStatusCommende(Long id, CommendeStatus status) {
        return commendeRepository.findById(id).map(commande -> {
            commande.setStatus(status);
            Commande updatedCommande = commendeRepository.save(commande);
            RestTemplate restTemplate = new RestTemplate();
            return buildCommandeResponse(updatedCommande, restTemplate);
        }).orElse(null);
    }

    private CommandeResponseDTO buildCommandeResponse(Commande commande, RestTemplate restTemplate) {
        FournisseurDTO fournisseurDTO = null;
        try {
            fournisseurDTO = restTemplate.getForObject(
                "http://localhost:8082/api/v0/fournisseurs/" + commande.getIdFournisseur(),
                FournisseurDTO.class
            );
        } catch (Exception e) {
            System.out.println("Impossible de récupérer le fournisseur: " + e.getMessage());
        }

        List<CommandeProduit> commandeProduits = commandeProduitRepository.findByCommandeId(commande.getId());
        List<CommandeProduitDetailDTO> commandeProduitsDetails = new ArrayList<>();

        for (CommandeProduit cp : commandeProduits) {
            try {
                ProduitDTO produitDTO = restTemplate.getForObject(
                    produitsServiceUrl + "/api/v1/products/" + cp.getProduitId(),
                    ProduitDTO.class
                );

                CommandeProduitDetailDTO detailDTO = new CommandeProduitDetailDTO();
                detailDTO.setId(cp.getId());
                detailDTO.setCommandeId(cp.getCommandeId());
                detailDTO.setProduit(produitDTO);
                detailDTO.setQuantite(cp.getQuantite());
                detailDTO.setUnitPrice(cp.getUnitPrice());
                detailDTO.setLineTotal(cp.getLineTotal());

                commandeProduitsDetails.add(detailDTO);
            } catch (Exception e) {
                System.out.println("Impossible de récupérer le produit: " + e.getMessage());
            }
        }

        CommandeResponseDTO response = new CommandeResponseDTO();
        response.setId(commande.getId());
        response.setNomProduit(commande.getNomProduit());
        response.setStatus(commande.getStatus());
        response.setFournisseur(fournisseurDTO);
        response.setProduits(commandeProduitsDetails);

        return response;
    }
}
