package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.FournisseurDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;

import java.util.List;
import java.util.Optional;

public interface CommendeService {

     List<Commande> findAll();
     Optional<Commande> creerCommende(Commande commande, FournisseurDTO fournisseurDTO, ProduitDTO produitDTO);
     Commande findById(Long id);
     void deleteById(Long id);
     Commande updateCommande(Long id, Commande commandeDetails);
     Commande updateCommendeStatus(Long id, CommendeStatus status);
}
