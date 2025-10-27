package org.ismail.Tricol.repository;

import org.ismail.Tricol.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    @Query("select f from Fournisseur f where f.nom = ?1")
    Fournisseur findByNom(String nom);


    @Query("select f from Fournisseur f order by f.nom asc")
    java.util.List<Fournisseur> findAllOrderByNomAsc();
}
