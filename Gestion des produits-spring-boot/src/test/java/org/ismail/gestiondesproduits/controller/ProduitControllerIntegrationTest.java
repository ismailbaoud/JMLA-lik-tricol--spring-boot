package org.ismail.gestiondesproduits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ismail.gestiondesproduits.dto.AddQuantityRequestDTO;
import org.ismail.gestiondesproduits.dto.ProduitDTO;
import org.ismail.gestiondesproduits.dto.ReduceQuantityDTO;
import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
public class ProduitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProduitService produitService;

    @MockBean
    private org.ismail.gestiondesproduits.mapper.ProduitMapper produitMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Produit produit1;
    private Produit produit2;
    private ProduitDTO produitDTO1;

    @BeforeEach
    void setUp() {
        produit1 = new Produit();
        produit1.setId(1L);
        produit1.setName("Laptop");
        produit1.setDescription("Dell Laptop");
        produit1.setUnitPrice(1200.50);
        produit1.setQuantity(10);

        produit2 = new Produit();
        produit2.setId(2L);
        produit2.setName("Mouse");
        produit2.setDescription("Wireless Mouse");
        produit2.setUnitPrice(25.99);
        produit2.setQuantity(50);

        produitDTO1 = new ProduitDTO();
        produitDTO1.setName("Laptop");
        produitDTO1.setDescription("Dell Laptop");
        produitDTO1.setUnitPrice(1200.50);
        produitDTO1.setQuantity(10);
    }

    @Test
    void itShouldGetAllProducts() throws Exception {
        List<Produit> produits = Arrays.asList(produit1, produit2);
        when(produitService.findAllProduits()).thenReturn(produits);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].name", is("Mouse")));
    }

    @Test
    void itShouldGetProductById() throws Exception {
        when(produitService.findById(1L)).thenReturn(produit1);

        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.description", is("Dell Laptop")));
    }

    @Test
    void itShouldCreateProduct() throws Exception {
        when(produitMapper.dtoToEntity(any(ProduitDTO.class))).thenReturn(produit1);
        when(produitService.save(any(Produit.class))).thenReturn(produit1);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produitDTO1)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.name", is("Laptop")));
    }

    @Test
    void itShouldAddQuantity() throws Exception {
        AddQuantityRequestDTO dto = new AddQuantityRequestDTO();
        dto.setQuantityToAdd(10);
        dto.setPrixAchat(12000.0);

        Produit updated = new Produit();
        updated.setId(1L);
        updated.setName("Laptop");
        updated.setDescription("Dell Laptop");
        updated.setUnitPrice(1200.50);
        updated.setQuantity(20);

        when(produitService.addQuantity(eq(1L), eq(10), eq(12000.0))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/products/add-quantity/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(20)));
    }

    @Test
    void itShouldReduceQuantity() throws Exception {
        ReduceQuantityDTO dto = new ReduceQuantityDTO();
        dto.setQuantityToReduce(5);

        Produit updated = new Produit();
        updated.setId(1L);
        updated.setName("Laptop");
        updated.setDescription("Dell Laptop");
        updated.setUnitPrice(1200.50);
        updated.setQuantity(5);

        when(produitService.reduceQuantity(1L, 5)).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/products/reduce-quantity/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
