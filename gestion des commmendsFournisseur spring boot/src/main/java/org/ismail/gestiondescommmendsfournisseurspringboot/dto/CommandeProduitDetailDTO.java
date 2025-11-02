package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

public class CommandeProduitDetailDTO {

    private Long id;
    private Long commandeId;
    private ProduitDTO produit;
    private Integer quantite;
    private Double unitPrice;
    private Double lineTotal;

    public CommandeProduitDetailDTO() {}

    public CommandeProduitDetailDTO(Long id, Long commandeId, ProduitDTO produit, Integer quantite, Double unitPrice) {
        this.id = id;
        this.commandeId = commandeId;
        this.produit = produit;
        this.quantite = quantite;
        this.unitPrice = unitPrice;
        this.lineTotal = quantite * unitPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
        if (this.unitPrice != null) {
            this.lineTotal = quantite * unitPrice;
        }
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        if (this.quantite != null) {
            this.lineTotal = quantite * unitPrice;
        }
    }

    public Double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(Double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
