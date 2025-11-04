package org.ismail.mouvementstock.service;

import lombok.RequiredArgsConstructor;
import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.exception.ResourceNotFoundException;
import org.ismail.mouvementstock.model.MouvementStock;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.ismail.mouvementstock.repository.MouvementStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository mouvementStockRepository;

    @Override
    @Transactional
    public MouvementStockResponseDTO createMouvement(MouvementStockRequestDTO requestDTO) {
        validateMouvementRequest(requestDTO);

        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduitId(requestDTO.getProduitId());
        mouvement.setTypeMvt(requestDTO.getTypeMvt());
        mouvement.setQuantite(requestDTO.getQuantite());
        mouvement.setPrixAchat(requestDTO.getPrixAchat());
        mouvement.setDateMvt(LocalDateTime.now());
        mouvement.setRefCommande(requestDTO.getRefCommande());

        MouvementStock savedMouvement = mouvementStockRepository.save(mouvement);

        return convertToResponseDTO(savedMouvement);
    }

    @Override
    public List<MouvementStockResponseDTO> getAllMouvements() {
        return mouvementStockRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MouvementStockResponseDTO getMouvementById(Long id) {
        MouvementStock mouvement = mouvementStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mouvement de stock non trouvé avec l'ID: " + id));
        return convertToResponseDTO(mouvement);
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByProduitId(Long produitId) {
        return mouvementStockRepository.findByProduitId(produitId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByType(TypeMouvement typeMvt) {
        return mouvementStockRepository.findByTypeMvt(typeMvt)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByCommande(Long refCommande) {
        return mouvementStockRepository.findByRefCommande(refCommande)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMouvement(Long id) {
        if (!mouvementStockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mouvement de stock non trouvé avec l'ID: " + id);
        }
        mouvementStockRepository.deleteById(id);
    }

    private void validateMouvementRequest(MouvementStockRequestDTO requestDTO) {
        if (requestDTO.getProduitId() == null) {
            throw new IllegalArgumentException("L'ID du produit est obligatoire");
        }

        if (requestDTO.getTypeMvt() == null) {
            throw new IllegalArgumentException("Le type de mouvement est obligatoire");
        }

        if (requestDTO.getQuantite() == null || requestDTO.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à zéro");
        }
    }


    private MouvementStockResponseDTO convertToResponseDTO(MouvementStock mouvement) {
        MouvementStockResponseDTO responseDTO = new MouvementStockResponseDTO();
        responseDTO.setId(mouvement.getId());
        responseDTO.setProduitId(mouvement.getProduitId());
        responseDTO.setTypeMvt(mouvement.getTypeMvt());
        responseDTO.setQuantite(mouvement.getQuantite());
        responseDTO.setPrixAchat(mouvement.getPrixAchat());
        responseDTO.setDateMvt(mouvement.getDateMvt());
        responseDTO.setRefCommande(mouvement.getRefCommande());
        return responseDTO;
    }
}

