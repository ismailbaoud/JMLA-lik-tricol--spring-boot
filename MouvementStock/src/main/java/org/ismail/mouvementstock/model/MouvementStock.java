package org.ismail.mouvementstock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * Entité représentant un mouvement de stock.
 * Un mouvement peut être une entrée, une sortie ou un ajustement de stock.
 * Chaque mouvement est lié à un produit (par son ID) qui est géré dans un autre microservice.
 */
@Entity
@Table(name = "mouvement_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeMouvement typeMvt;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_achat")
    private Double prixAchat;

    @Column(name = "date_mvt", nullable = false)
    private LocalDateTime dateMvt;

    @Column(name = "ref_commande")
    private Long refCommande;
}
