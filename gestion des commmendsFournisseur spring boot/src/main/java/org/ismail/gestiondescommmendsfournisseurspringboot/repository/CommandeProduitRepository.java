package org.ismail.gestiondescommmendsfournisseurspringboot.repository;

import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Long> {
    List<CommandeProduit> findByCommandeId(Long commandeId);
}
package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

public class ProduitItemDTO {
    
    private Long idProduit;
    private Integer quantite;

    public ProduitItemDTO() {}

    public ProduitItemDTO(Long idProduit, Integer quantite) {
        this.idProduit = idProduit;
        this.quantite = quantite;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}

