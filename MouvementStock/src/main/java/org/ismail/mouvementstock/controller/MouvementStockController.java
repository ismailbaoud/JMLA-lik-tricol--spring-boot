package org.ismail.mouvementstock.controller;

import lombok.RequiredArgsConstructor;
import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.ismail.mouvementstock.service.MouvementStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mouvements")
@RequiredArgsConstructor
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;

    @PostMapping
    public ResponseEntity<MouvementStockResponseDTO> createMouvement(@RequestBody MouvementStockRequestDTO requestDTO) {
        MouvementStockResponseDTO responseDTO = mouvementStockService.createMouvement(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MouvementStockResponseDTO>> getAllMouvements() {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getAllMouvements();
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouvementStockResponseDTO> getMouvementById(@PathVariable Long id) {
        MouvementStockResponseDTO responseDTO = mouvementStockService.getMouvementById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<MouvementStockResponseDTO>> getMouvementsByProduit(@PathVariable Long produitId) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getMouvementsByProduitId(produitId);
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MouvementStockResponseDTO>> getMouvementsByType(@PathVariable TypeMouvement type) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getMouvementsByType(type);
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("/commande/{refCommande}")
    public ResponseEntity<List<MouvementStockResponseDTO>> getMouvementsByCommande(@PathVariable Long refCommande) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getMouvementsByCommande(refCommande);
        return ResponseEntity.ok(mouvements);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        mouvementStockService.deleteMouvement(id);
        return ResponseEntity.noContent().build();
    }
}

