package org.ismail.mouvementstock.service;

import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.model.TypeMouvement;

import java.util.List;

public interface MouvementStockService {

    /**
     * Crée un nouveau mouvement de stock
     */
    MouvementStockResponseDTO createMouvement(MouvementStockRequestDTO requestDTO);

    /**
     * Récupère tous les mouvements
     */
    List<MouvementStockResponseDTO> getAllMouvements();

    /**
     * Récupère un mouvement par son ID
     */
    MouvementStockResponseDTO getMouvementById(Long id);

    /**
     * Récupère tous les mouvements d'un produit
     */
    List<MouvementStockResponseDTO> getMouvementsByProduitId(Long produitId);

    /**
     * Récupère tous les mouvements d'un type spécifique
     */
    List<MouvementStockResponseDTO> getMouvementsByType(TypeMouvement typeMvt);

    /**
     * Récupère tous les mouvements liés à une commande
     */
    List<MouvementStockResponseDTO> getMouvementsByCommande(Long refCommande);

    /**
     * Supprime un mouvement de stock
     */
    void deleteMouvement(Long id);
}

