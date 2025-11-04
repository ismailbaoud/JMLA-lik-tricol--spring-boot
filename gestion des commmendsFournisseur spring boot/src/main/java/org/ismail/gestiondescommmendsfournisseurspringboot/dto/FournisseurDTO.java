package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
