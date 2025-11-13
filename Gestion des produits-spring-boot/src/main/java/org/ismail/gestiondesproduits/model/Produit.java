package org.ismail.gestiondesproduits.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Generated;

import javax.naming.Name;

@Entity
@Data
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;
    private double unitPrice;
    private String description;
    private Integer quantity;
    
    
    public Produit() {}

    public Produit(Long id, String name, double unitPrice, String description, Integer quantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.description = description;
        this.quantity = quantity;
    }


}
