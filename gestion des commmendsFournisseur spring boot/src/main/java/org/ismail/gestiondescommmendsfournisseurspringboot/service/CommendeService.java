package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CreateCommandeRequest;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.FournisseurDTO;

import java.util.List;

public interface CommendeService {

     List<CommandeResponseDTO> findAll();
     CommandeResponseDTO creerCommandeAvecProduits(CreateCommandeRequest request, FournisseurDTO fournisseurDTO);
     CommandeResponseDTO findById(Long id);
     void deleteById(Long id);
     CommandeResponseDTO updateCommande(Long id, CreateCommandeRequest commandeDetails, FournisseurDTO fournisseurDTO);

    CommandeResponseDTO updatStatusCommende(Long id, CommendeStatus status);
}
