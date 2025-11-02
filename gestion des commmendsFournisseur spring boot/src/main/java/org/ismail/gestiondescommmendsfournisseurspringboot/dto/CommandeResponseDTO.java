package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;

import java.util.List;

public class CommandeResponseDTO {
    
    private Long id;
    private String nomProduit;
    private CommendeStatus status;
    private FournisseurDTO fournisseur;
    private List<CommandeProduitDetailDTO> produits;
    private Double totalPrice;

    public CommandeResponseDTO() {}

    public CommandeResponseDTO(Long id, String nomProduit, CommendeStatus status, FournisseurDTO fournisseur, List<CommandeProduitDetailDTO> produits) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.status = status;
        this.fournisseur = fournisseur;
        this.produits = produits;
        this.totalPrice = calculateTotalPrice();
    }

    private Double calculateTotalPrice() {
        if (produits == null || produits.isEmpty()) {
            return 0.0;
        }
        return produits.stream()
                .mapToDouble(p -> p.getLineTotal())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public CommendeStatus getStatus() {
        return status;
    }

    public void setStatus(CommendeStatus status) {
        this.status = status;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<CommandeProduitDetailDTO> getProduits() {
        return produits;
    }

    public void setProduits(List<CommandeProduitDetailDTO> produits) {
        this.produits = produits;
        this.totalPrice = calculateTotalPrice();
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
