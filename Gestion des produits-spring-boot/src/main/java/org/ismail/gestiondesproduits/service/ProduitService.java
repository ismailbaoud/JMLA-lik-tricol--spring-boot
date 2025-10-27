package org.ismail.gestiondesproduits.service;

import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    @Autowired
    public ProduitRepository produitRepository;

    public Produit save(Produit p) {
        return produitRepository.save(p);
    }

    public void delete(Produit p) {
        produitRepository.delete(p);
    }

    public Produit update(Produit p) {
        return produitRepository.save(p);
    }

    public List<Produit> findAllProduits() {
        return produitRepository.findAll();
    }

    public Produit findById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

}
