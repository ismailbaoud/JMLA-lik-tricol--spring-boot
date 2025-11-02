package org.ismail.Tricol.controller;

import org.ismail.Tricol.model.Fournisseur;
import org.ismail.Tricol.service.FournisseurServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/fournisseurs")
public class FournisseurController {

    private FournisseurServiceImp fournisseurServiceImp;

    public void setFournisseurService(FournisseurServiceImp fournisseurServiceImp) {
        this.fournisseurServiceImp = fournisseurServiceImp;
    }

    @PostMapping
    public Fournisseur save(@RequestBody Fournisseur fournisseur) {
        fournisseurServiceImp.saveFournisseur(fournisseur);
        return fournisseur;
    }

    @GetMapping
    public List<Fournisseur> findAll() {
        return fournisseurServiceImp.findAllFournisseurs();
    }

    @GetMapping("/sorted")
    public List<Fournisseur> findAllOrderByNomAsc() {
        return fournisseurServiceImp.findAllFournisseursOrderByNomAsc();
    }


    @GetMapping("/nom/{nom}")
    public Fournisseur findByNom(@PathVariable("nom") String nom) {
        return fournisseurServiceImp.findFournisseurByNom(nom);
    }

    @DeleteMapping("/{id}")
    public void deleteFournisseur(@PathVariable("id") Long id) {
        fournisseurServiceImp.deleteFournisseurById(id);
    }

    @PutMapping("/{id}")
    public Fournisseur updateFournisseur(@PathVariable("id") Long id, @RequestBody Fournisseur fournisseur) {
        return fournisseurServiceImp.updateFournisseur(id, fournisseur);
    }

    @GetMapping("/{id}")
    public Fournisseur findById(@PathVariable("id") Long id) {
        return fournisseurServiceImp.findFournisseurById(id).orElseThrow(()-> new RuntimeException("Fournisseur not found with id " + id));
    }


}
