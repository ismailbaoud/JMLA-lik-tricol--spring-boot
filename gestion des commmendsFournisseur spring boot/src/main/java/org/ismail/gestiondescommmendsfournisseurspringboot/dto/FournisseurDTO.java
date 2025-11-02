package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

public class FournisseurDTO {
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String societe;
    public String adresse;
    public String contact;
    public String telephone;
    public String ville;
    public String ice;

    public FournisseurDTO() {}

    public FournisseurDTO(Long id, String nom, String prenom, String email, String societe, String adresse, String contact, String telephone, String ville, String ice) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.societe = societe;
        this.adresse = adresse;
        this.contact = contact;
        this.telephone = telephone;
        this.ville = ville;
        this.ice = ice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }


    @Override
    public String toString() {
        return "FournisseurDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", societe='" + societe + '\'' +
                ", adresse='" + adresse + '\'' +
                ", contact='" + contact + '\'' +
                ", telephone='" + telephone + '\'' +
                ", ville='" + ville + '\'' +
                ", ice='" + ice + '\'' +
                '}';
    }
}
