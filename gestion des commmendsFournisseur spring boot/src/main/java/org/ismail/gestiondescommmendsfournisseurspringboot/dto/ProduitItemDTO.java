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

