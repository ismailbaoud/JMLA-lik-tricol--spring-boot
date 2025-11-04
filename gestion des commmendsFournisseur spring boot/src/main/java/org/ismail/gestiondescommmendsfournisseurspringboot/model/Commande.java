package org.ismail.gestiondescommmendsfournisseurspringboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
