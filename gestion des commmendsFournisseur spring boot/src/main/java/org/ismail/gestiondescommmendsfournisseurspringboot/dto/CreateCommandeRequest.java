package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import java.util.List;

public class CreateCommandeRequest {
    
    private String nomProduit;
    private Long idFournisseur;
    private CommendeStatus status;
    private List<ProduitItemDTO> produits;

    public CreateCommandeRequest() {}

    public CreateCommandeRequest(String nomProduit, Long idFournisseur, CommendeStatus status, List<ProduitItemDTO> produits) {
        this.nomProduit = nomProduit;
        this.idFournisseur = idFournisseur;
        this.status = status;
        this.produits = produits;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Long idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public CommendeStatus getStatus() {
        return status;
    }

    public void setStatus(CommendeStatus status) {
        this.status = status;
    }

    public List<ProduitItemDTO> getProduits() {
        return produits;
    }

    public void setProduits(List<ProduitItemDTO> produits) {
        this.produits = produits;
    }
}

