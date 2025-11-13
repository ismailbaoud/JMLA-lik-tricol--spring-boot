package org.ismail.Tricol.repository;

import org.ismail.Tricol.config.TestConfig;
import org.ismail.Tricol.model.Fournisseur;
import org.junit.jupiter.api.Assertions;
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
class FournisseurRepositoryTest {

    @Autowired
    private FournisseurRepository underTest;

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
        underTest.save(f);
        underTest.save(f1);
    }
    @Test
    void shouldReturnNameWhenFournisseurExists() {
        // given
        String nom = "ismailTestNom";

        mockFournisseurs();

        // when
        Fournisseur f = underTest.findByNom(nom);

        //then
        Assertions.assertNotNull(f);
        Assertions.assertEquals(nom, f.getNom());
    }

    @Test
    void shouldReturFournisseursListOrderedByNameAscWhenFournisseursExists() {
        // given

         mockFournisseurs();

        //when

        List<Fournisseur> fs = underTest.findAllOrderByNomAsc();

        //then
        assertNotNull(fs);
        assertEquals("ismailTestNom", fs.get(0).getNom());
        assertEquals("ismailTestNom1", fs.get(1).getNom());
    }
}
