package org.ismail.mouvementstock.repository;

import org.ismail.mouvementstock.model.MouvementStock;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    /**
     * Trouve tous les mouvements pour un produit spécifique
     */
    List<MouvementStock> findByProduitId(Long produitId);

    /**
     * Trouve tous les mouvements d'un type spécifique
     */
    List<MouvementStock> findByTypeMvt(TypeMouvement typeMvt);

    /**
     * Trouve tous les mouvements liés à une commande
     */
    List<MouvementStock> findByRefCommande(Long refCommande);

    /**
     * Trouve tous les mouvements pour un produit et un type spécifiques
     */
    List<MouvementStock> findByProduitIdAndTypeMvt(Long produitId, TypeMouvement typeMvt);
}

