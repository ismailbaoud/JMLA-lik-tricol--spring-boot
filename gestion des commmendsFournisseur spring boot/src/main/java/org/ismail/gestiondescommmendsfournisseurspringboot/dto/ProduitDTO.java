package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.Data;

@Data
public class ProduitDTO {
    public Long id;
    public String nom;
    public Double prix;
    public Integer quantite;
}
