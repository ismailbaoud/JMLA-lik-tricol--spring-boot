package org.ismail.gestiondescommmendsfournisseurspringboot.model;

import jakarta.persistence.*;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;

@Entity
public class Commandes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String nomProduit;

    private Double prixProduit;

    private Integer quantiteProduit;

    @Enumerated(EnumType.STRING)
    private CommendeStatus Status;

    private String IdProduit;

    private String IdFournisseur;

    public Commandes() {}

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
        return Status;
    }

    public void setStatus(CommendeStatus status) {
        Status = status;
    }

    public String getIdProduit() {
        return IdProduit;
    }

    public void setIdProduit(String idProduit) {
        IdProduit = idProduit;
    }

    public String getIdFournisseur() {
        return IdFournisseur;
    }

    public void setIdFournisseur(String idFournisseur) {
        IdFournisseur = idFournisseur;
    }
}
