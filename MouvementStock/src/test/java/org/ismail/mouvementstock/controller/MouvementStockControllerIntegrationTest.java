package org.ismail.mouvementstock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.ismail.mouvementstock.service.MouvementStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MouvementStockController.class)
class MouvementStockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MouvementStockService mouvementStockService;

    @Autowired
    private ObjectMapper objectMapper;

    private MouvementStockResponseDTO mouvement1;
    private MouvementStockResponseDTO mouvement2;
    private MouvementStockRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mouvement1 = new MouvementStockResponseDTO();
        mouvement1.setId(1L);
        mouvement1.setProduitId(100L);
        mouvement1.setQuantite(10);
        mouvement1.setTypeMvt(TypeMouvement.ENTREE);
        mouvement1.setDateMvt(LocalDateTime.now());
        mouvement1.setRefCommande(999L);

        mouvement2 = new MouvementStockResponseDTO();
        mouvement2.setId(2L);
        mouvement2.setProduitId(200L);
        mouvement2.setQuantite(5);
        mouvement2.setTypeMvt(TypeMouvement.SORTIE);
        mouvement2.setDateMvt(LocalDateTime.now());

        requestDTO = new MouvementStockRequestDTO();
        requestDTO.setProduitId(100L);
        requestDTO.setQuantite(10);
        requestDTO.setTypeMvt(TypeMouvement.ENTREE);
        requestDTO.setRefCommande(999L);
    }

    @Test
    void itShouldCreateMouvement() throws Exception {
        when(mouvementStockService.createMouvement(ArgumentMatchers.any(MouvementStockRequestDTO.class)))
                .thenReturn(mouvement1);

        mockMvc.perform(post("/api/v1/mouvements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.typeMvt", is("ENTREE")));
    }

    @Test
    void itShouldGetAllMouvements() throws Exception {
        List<MouvementStockResponseDTO> mouvements = Arrays.asList(mouvement1, mouvement2);
        when(mouvementStockService.getAllMouvements(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(mouvements));

        mockMvc.perform(get("/api/v1/mouvements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].typeMvt", is("ENTREE")))
                .andExpect(jsonPath("$[1].typeMvt", is("SORTIE")));
    }

    @Test
    void itShouldGetMouvementById() throws Exception {
        when(mouvementStockService.getMouvementById(1L)).thenReturn(mouvement1);

        mockMvc.perform(get("/api/v1/mouvements/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produitId", is(100)))
                .andExpect(jsonPath("$.typeMvt", is("ENTREE")));
    }

    @Test
    void itShouldGetMouvementsByProduit() throws Exception {
        when(mouvementStockService.getMouvementsByProduitId(100L))
                .thenReturn(Arrays.asList(mouvement1));

        mockMvc.perform(get("/api/v1/mouvements/produit/{produitId}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].produitId", is(100)));
    }

    @Test
    void itShouldGetMouvementsByType() throws Exception {
        when(mouvementStockService.getMouvementsByType(TypeMouvement.ENTREE))
                .thenReturn(Arrays.asList(mouvement1));

        mockMvc.perform(get("/api/v1/mouvements/type/{type}", "ENTREE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typeMvt", is("ENTREE")));
    }

    @Test
    void itShouldGetMouvementsByCommande() throws Exception {
        when(mouvementStockService.getMouvementsByCommande(999L))
                .thenReturn(Arrays.asList(mouvement1));

        mockMvc.perform(get("/api/v1/mouvements/commande/{refCommande}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].refCommande", is(999)));
    }

    @Test
    void itShouldDeleteMouvement() throws Exception {
        Mockito.doNothing().when(mouvementStockService).deleteMouvement(1L);

        mockMvc.perform(delete("/api/v1/mouvements/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
