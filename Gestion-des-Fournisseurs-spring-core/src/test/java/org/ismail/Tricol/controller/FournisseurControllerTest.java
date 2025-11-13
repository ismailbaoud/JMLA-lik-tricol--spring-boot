package org.ismail.Tricol.controller;

import org.ismail.Tricol.config.TestConfig;
import org.ismail.Tricol.model.Fournisseur;
import org.ismail.Tricol.repository.FournisseurRepository;
import org.ismail.Tricol.service.FournisseurServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
class FournisseurControllerTest {

    @Autowired
    private FournisseurRepository fournisseurRepository;
    private FournisseurController fournisseurController;

    @BeforeEach
    public void fournisseurController() {
        FournisseurServiceImp fournisseurServiceImp = new FournisseurServiceImp();
        fournisseurServiceImp.setFournisseurRepository(fournisseurRepository);
        FournisseurController fournisseurController = new FournisseurController();
        fournisseurController.setFournisseurService(fournisseurServiceImp);
        this.fournisseurController = fournisseurController;
    }

    public void mockFournisseurs() {
        Fournisseur f = new Fournisseur();
        f.setNom("ismailTestNom");
        f.setPrenom("ismailTestPrenom");
        f.setEmail("ismailTestEmil@gmail.com");
        f.setSociete("ismailTestSociete");
        f.setContact("ismailTestContact");
        f.setAdresse("ismailTestAdresse");
        f.setTelephone("ismailTestTelephone");
        f.setIce("ismailTestIce");

        Fournisseur f1 = new Fournisseur();
        f1.setNom("ismailTestNom1");
        f1.setPrenom("ismailTestPrenom1");
        f1.setEmail("ismailTestEmil1@gmail.com");
        f1.setSociete("ismailTestSociete1");
        f1.setContact("ismailTestContact1");
        f1.setAdresse("ismailTestAdresse1");
        f1.setTelephone("ismailTestTelephone1");
        f1.setIce("ismailTestIce1");
        fournisseurRepository.save(f);
        fournisseurRepository.save(f1);
    }

    @Test
    void save() {
        // given
        Fournisseur f = new Fournisseur();
        f.setNom("ismailTestNom");
        f.setPrenom("ismailTestPrenom");
        f.setEmail("ismailTestEmil@gmail.com");
        f.setSociete("ismailTestSociete");
        f.setContact("ismailTestContact");
        f.setAdresse("ismailTestAdresse");
        f.setTelephone("ismailTestTelephone");
        f.setIce("ismailTestIce");

        // when

        Fournisseur fournisseur = fournisseurController.save(f);

        // then

        assertNotNull(fournisseur);
        assertEquals(f.getNom(), fournisseur.getNom());
        assertEquals(f.getEmail(), fournisseur.getEmail());
    }

    @Test
    void findAll() {
        // given

        mockFournisseurs();

        // when

        List<Fournisseur> fournisseurs = fournisseurController.findAll();

        // then

        assertEquals(2, fournisseurs.size());
    }

    @Test
    void findAllOrderByNomAsc() {
        // given

        mockFournisseurs();

        //when

        List<Fournisseur> fs = fournisseurController.findAllOrderByNomAsc();

        // then

        assertEquals("ismailTestNom", fs.get(0).getNom());
        assertEquals("ismailTestNom1", fs.get(1).getNom());
    }

    @Test
    void findByNom() {
        // given

        mockFournisseurs();

        String nom =  "ismailTestNom";

        // when

        Fournisseur fournisseur = fournisseurController.findByNom(nom);

        //then

        assertEquals(nom, fournisseur.getNom());
    }

    @Test
    void deleteFournisseur() {
        // given

        Fournisseur f1 = new Fournisseur();
        f1.setNom("ismailTestNom1");
        f1.setPrenom("ismailTestPrenom1");
        f1.setEmail("ismailTestEmil1@gmail.com");
        f1.setSociete("ismailTestSociete1");
        f1.setContact("ismailTestContact1");
        f1.setAdresse("ismailTestAdresse1");
        f1.setTelephone("ismailTestTelephone1");
        f1.setIce("ismailTestIce1");
        Fournisseur fUp = fournisseurController.save(f1);

        Long id =  fUp.getId() ;

        // when

        fournisseurController.deleteFournisseur(id);

        //then

        assertFalse(fournisseurRepository.existsById(id));
    }


    @Test
    void updateFournisseur() {
        // given

        String nom = "ismailTestNomUpdated";
        Fournisseur f1 = new Fournisseur();
        f1.setNom("ismailTestNom1");
        f1.setPrenom("ismailTestPrenom1");
        f1.setEmail("ismailTestEmil1@gmail.com");
        f1.setSociete("ismailTestSociete1");
        f1.setContact("ismailTestContact1");
        f1.setAdresse("ismailTestAdresse1");
        f1.setTelephone("ismailTestTelephone1");
        f1.setIce("ismailTestIce1");
        Fournisseur fUp = fournisseurController.save(f1);
        fUp.setNom(nom);


        // When

        Fournisseur fUpdated = fournisseurController.updateFournisseur(f1.getId(), fUp);

        // then

        assertEquals(nom, fUpdated.getNom());
    }
}