package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;

import java.util.List;

public class CommandeResponseDTO {
    
    private Long id;
    private String nomProduit;
    private CommendeStatus status;
    private Long idFournisseur;
    private List<CommandeProduit> produits;
    private Double totalPrice;

    public CommandeResponseDTO() {}

    public CommandeResponseDTO(Long id, String nomProduit, CommendeStatus status, Long idFournisseur, List<CommandeProduit> produits) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.status = status;
        this.idFournisseur = idFournisseur;
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

    public Long getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Long idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public List<CommandeProduit> getProduits() {
        return produits;
    }

    public void setProduits(List<CommandeProduit> produits) {
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

