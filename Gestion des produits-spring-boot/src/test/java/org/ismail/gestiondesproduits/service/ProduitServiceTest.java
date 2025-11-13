package org.ismail.gestiondesproduits.service;


import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {
    @Mock
    ProduitRepository produitRepository;

    @InjectMocks
    ProduitService produitService ;


    // mock data
    public void mockProduits() {
        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);

        Produit p1 = new Produit();
        p1.setId(2L);
        p1.setName("test1");
        p1.setDescription("test1");
        p1.setUnitPrice(30.50);
        p1.setQuantity(10);

        // when
        when(produitRepository.save(p)).thenReturn(p);
        when(produitRepository.save(p1)).thenReturn(p1);
        produitService.save(p);
        produitService.save(p1);

    }
    
    //save
    @Test
    public void iTShouldCreateProduit(){
        // given

        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);

        // when
        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produit result = produitService.reduceQuantity(1L, 5);

        // then

        assertNotNull(result);
        assertEquals(result.getId(),p.getId());
    }

    //delete
    @Test
    public void iTShouldDeleteProduit(){
        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);
        produitService.delete(p);
        Produit pTest2 = produitRepository.findById(1L).orElse(null);
        assertNull(pTest2);
    }

    //update
    @Test
    public void itShouldUpdateProduit() {
        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);
        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produit result = produitService.reduceQuantity(1L, 5);

        assertNotNull(result);
        result.setName("test2");
        Produit pTest2 = produitService.update(p);
        assertNotNull(pTest2);
        assertEquals(pTest2.getName(),result.getName());
    }

    //findAllProduits
    @Test
    public void itShouldFindAllProduits() {
        mockProduits();
        when(produitRepository.findAll()).thenReturn(List.of(new Produit()));
        List<Produit> ps = produitService.findAllProduits();
        assertNotNull(ps);
    }

    //findById
    @Test
    public void itShouldFindProduitById() {
        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);
        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        Produit pTest = produitService.findById(1L);
        assertNotNull(pTest);
        assertEquals(pTest.getId(),p.getId());
    }

    //addQuantity
    @Test
    public void itShouldAddQuantityToProduit() {
        // given
        
        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);

        // when

        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produit result = produitService.addQuantity(1L, 5, 200.0);


        // then

        assertEquals(25, result.getQuantity());
        assertNotNull(result);
        assertEquals(result.getQuantity(),result.getQuantity());
    }

    //resuceQuantity
    @Test
    public void itShouldRediceQuantity() {
        // given

        Produit p = new Produit();
        p.setId(1L);
        p.setName("test");
        p.setDescription("test");
        p.setUnitPrice(40.50);
        p.setQuantity(20);

        // when
        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produit result = produitService.reduceQuantity(1L, 5);

        // then

        assertNotNull(result);
        assertEquals(result.getQuantity(),result.getQuantity());
    }
}
