package org.ismail.Tricol.service;

import org.ismail.Tricol.config.TestConfig;
import org.ismail.Tricol.model.Fournisseur;
import org.ismail.Tricol.repository.FournisseurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
class FournisseurServiceTest {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    private FournisseurServiceImp fournisseurServiceImp;

    @BeforeEach
    public void setUp() {
        FournisseurServiceImp fournisseurServiceImp = new FournisseurServiceImp();
        fournisseurServiceImp.setFournisseurRepository(fournisseurRepository);
        this.fournisseurServiceImp = fournisseurServiceImp;
    }

    private void mockFournisseurs() {
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
    void saveFournisseur() {
        // given
        Fournisseur f1 = new Fournisseur();
        f1.setNom("ismailTestNomSave");
        f1.setPrenom("ismailTestPrenomSave");
        f1.setEmail("ismailTestEmailSave@gmail.com");
        f1.setSociete("ismailTestSocieteSave");
        f1.setContact("ismailTestContactSave");
        f1.setAdresse("ismailTestAdresseSave");
        f1.setTelephone("ismailTestTelephoneSave");
        f1.setIce("ismailTestIceSave");

        // when
        fournisseurServiceImp.saveFournisseur(f1);

        // then
        assertTrue(fournisseurRepository.existsById(f1.getId()));
    }

    @Test
    void findAllFournisseurs() {
        // given
        mockFournisseurs();

        // when
        List<Fournisseur> fs = fournisseurServiceImp.findAllFournisseurs();

        // then
        assertFalse(fs.isEmpty());
        assertTrue(fs.size() >= 2);
    }

    @Test
    void deleteFournisseurById() {
        // given
        Fournisseur f = new Fournisseur();
        f.setNom("DeleteNom");
        f.setPrenom("DeletePrenom");
        f.setEmail("delete@gmail.com");
        f.setSociete("DeleteSociete");
        f.setContact("DeleteContact");
        f.setAdresse("DeleteAdresse");
        f.setTelephone("DeleteTelephone");
        f.setIce("DeleteIce");

        fournisseurRepository.save(f);
        Long id = f.getId();
        assertTrue(fournisseurRepository.existsById(id));

        // when
        fournisseurServiceImp.deleteFournisseurById(id);

        // then
        assertFalse(fournisseurRepository.existsById(id));
    }

    @Test
    void updateFournisseur() {
        // given
        Fournisseur f = new Fournisseur();
        f.setNom("BeforeUpdate");
        f.setPrenom("PrenomBefore");
        f.setEmail("before@gmail.com");
        f.setSociete("SocieteBefore");
        f.setContact("ContactBefore");
        f.setAdresse("AdresseBefore");
        f.setTelephone("TelephoneBefore");
        f.setIce("IceBefore");

        fournisseurRepository.save(f);

        Fournisseur update = new Fournisseur();
        update.setNom("AfterUpdate");
        update.setPrenom("PrenomAfter");
        update.setEmail("after@gmail.com");
        update.setSociete("SocieteAfter");
        update.setContact("ContactAfter");
        update.setAdresse("AdresseAfter");
        update.setTelephone("TelephoneAfter");
        update.setIce("IceAfter");

        // when
        Fournisseur updated = fournisseurServiceImp.updateFournisseur(f.getId(), update);

        // then
        assertEquals("AfterUpdate", updated.getNom());
        assertEquals("PrenomAfter", updated.getPrenom());
        assertEquals("after@gmail.com", updated.getEmail());
        assertEquals("SocieteAfter", updated.getSociete());
    }

    @Test
    void findFournisseurById() {
        // given
        mockFournisseurs();
        Fournisseur f = fournisseurRepository.findAll().get(0);

        // when
        Optional<Fournisseur> result = fournisseurServiceImp.findFournisseurById(f.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(f.getNom(), result.get().getNom());
    }

    @Test
    void findFournisseurByNom() {
        // given
        Fournisseur f = new Fournisseur();
        f.setNom("UniqueNom");
        f.setPrenom("PrenomX");
        f.setEmail("unique@gmail.com");
        f.setSociete("UniqueSociete");
        f.setContact("UniqueContact");
        f.setAdresse("UniqueAdresse");
        f.setTelephone("UniqueTelephone");
        f.setIce("UniqueIce");
        fournisseurRepository.save(f);

        // when
        Fournisseur result = fournisseurServiceImp.findFournisseurByNom("UniqueNom");

        // then
        assertNotNull(result);
        assertEquals("UniqueNom", result.getNom());
    }

    @Test
    void findAllFournisseursOrderByNomAsc() {
        // given
        mockFournisseurs();

        // when
        List<Fournisseur> results = fournisseurServiceImp.findAllFournisseursOrderByNomAsc();

        // then
        assertFalse(results.isEmpty());
        assertTrue(results.size() >= 2);

        // Check alphabetical order
        for (int i = 1; i < results.size(); i++) {
            String prev = results.get(i - 1).getNom();
            String curr = results.get(i).getNom();
            assertTrue(prev.compareToIgnoreCase(curr) <= 0,
                    "List not sorted alphabetically: " + prev + " > " + curr);
        }
    }
}
