package org.ismail.gestiondescommmendsfournisseurspringboot.model;

import jakarta.persistence.*;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;

@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomProduit;

    private Double prixProduit;

    private Integer quantiteProduit;

    @Enumerated(EnumType.STRING)
    private CommendeStatus status;

    private Long idProduit;

    private Long idFournisseur;

    public Commande() {}

    public Commande(Long id, String nomProduit, Double prixProduit, Integer quantiteProduit, CommendeStatus status, Long idProduit, Long idFournisseur) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.quantiteProduit = quantiteProduit;
        this.status = status;
        this.idProduit = idProduit;
        this.idFournisseur = idFournisseur;
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

    public Double getPrixProduit() {
        return prixProduit;
    }

    public void setPrixProduit(Double prixProduit) {
        this.prixProduit = prixProduit;
    }

    public Integer getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(Integer quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public CommendeStatus getStatus() {
        return status;
    }

    public void setStatus(CommendeStatus status) {
        this.status = status;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public Long getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Long idFournisseur) {
        this.idFournisseur = idFournisseur;
    }
}
