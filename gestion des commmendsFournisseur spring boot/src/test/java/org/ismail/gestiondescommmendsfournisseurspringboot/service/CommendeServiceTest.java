package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.*;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommandeProduitRepository;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommendeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;


class CommendeServiceTest {

    @Mock
    private CommendeRepository commendeRepository;

    @Mock
    private CommandeProduitRepository commandeProduitRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CommendeServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service.mouvementsServiceUrl = "http://mouvements-service";
        service.produitsServiceUrl = "http://produits-service";}

    @Test
    void shouldCreateCommandeAndSaveProducts() {
        // Given
        ProduitCommandeDTO produitCmd = new ProduitCommandeDTO();
        produitCmd.setProduitId(10L);
        produitCmd.setQuantite(3);

        CommandeRequestDTO request = new CommandeRequestDTO();
        request.setIdFournisseur(5L);
        request.setProduits(List.of(produitCmd));

        ProduitDTO produit = new ProduitDTO();
        produit.setId(10L);
        produit.setNom("Test Product");
        produit.setPrix(100.0);

        Commande savedCommande = new Commande();
        savedCommande.setId(99L);
        savedCommande.setIdFournisseur(5L);
        savedCommande.setStatus(CommendeStatus.EN_ATTENTE);

        given(commendeRepository.save(any(Commande.class))).willReturn(savedCommande);
        given(restTemplate.getForObject(anyString(), eq(ProduitDTO.class))).willReturn(produit);

        // When
        CommandeResponseDTO response = service.creerCommende(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(CommendeStatus.EN_ATTENTE);
        then(commandeProduitRepository).should().save(any(CommandeProduit.class));
        then(commendeRepository).should().save(any(Commande.class));
    }

    @Test
    void shouldFindCommandeById() {
        // Given
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setStatus(CommendeStatus.EN_ATTENTE);

        given(commendeRepository.findById(1L)).willReturn(Optional.of(commande));
        given(commandeProduitRepository.findByCommandeId(1L)).willReturn(List.of());
        
        // When
        CommandeResponseDTO result = service.findById(1L);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(CommendeStatus.EN_ATTENTE);
    }
    
    @Test
    void shouldDeleteCommandeAndItsProducts() {

        // Given
        CommandeProduit cp = new CommandeProduit();
        cp.setId(1L);
        given(commandeProduitRepository.findByCommandeId(100L)).willReturn(List.of(cp));

        // When
        service.deleteById(100L);

        // Then
        then(commandeProduitRepository).should().deleteAll(anyList());
        then(commendeRepository).should().deleteById(100L);
    }

    @Test
    void shouldUpdateStatusToLivreeAndReduceProductQuantities() {
        // Given
        Commande commande = new Commande();
        commande.setId(10L);
        commande.setStatus(CommendeStatus.EN_ATTENTE);

        CommandeProduit cp = new CommandeProduit();
        cp.setProduitId(50L);
        cp.setQuantite(5);
        cp.setUnitPrice(10.0);

        given(commendeRepository.findById(10L)).willReturn(Optional.of(commande));
        given(commandeProduitRepository.findByCommandeId(10L)).willReturn(List.of(cp));
        given(commendeRepository.save(any(Commande.class))).willAnswer(i -> i.getArgument(0));

        given(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
                .willReturn(ResponseEntity.ok().build());
        given(restTemplate.postForObject(anyString(), any(), eq(Object.class))).willReturn(null);

        // When
        CommandeResponseDTO response = service.updateCommendeStatus(10L, CommendeStatus.LIVREE);

        // Then
        assertThat(response.getStatus()).isEqualTo(CommendeStatus.LIVREE);
        then(restTemplate).should().exchange(
                contains("/reduce-quantity/50"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        );
        then(restTemplate).should().postForObject(
                contains("/mouvements"),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void shouldCalculateSumOfProductPrices() {
        // Given
        Commande commande = new Commande();
        commande.setId(20L);

        CommandeProduit cp1 = new CommandeProduit(); cp1.setProduitId(1L);
        CommandeProduit cp2 = new CommandeProduit(); cp2.setProduitId(2L);

        ProduitDTO p1 = new ProduitDTO(); p1.setPrix(50.0);
        ProduitDTO p2 = new ProduitDTO(); p2.setPrix(70.0);

        given(commendeRepository.findById(20L)).willReturn(Optional.of(commande));
        given(commandeProduitRepository.findByCommandeId(20L)).willReturn(List.of(cp1, cp2));
        given(restTemplate.getForObject(contains("/1"), eq(ProduitDTO.class))).willReturn(p1);
        given(restTemplate.getForObject(contains("/2"), eq(ProduitDTO.class))).willReturn(p2);

        // When
        Double sum = service.calculate();

        // Then
        assertThat(sum).isEqualTo(120.0);
    }

    @Test
    void shouldReturnPagedCommandes() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        Commande c1 = new Commande(); c1.setId(1L);
        Page<Commande> page = new PageImpl<>(List.of(c1));
        given(commendeRepository.findAll(pageable)).willReturn(page);
        given(commandeProduitRepository.findByCommandeId(anyLong())).willReturn(List.of());

        // When
        Page<CommandeResponseDTO> result = service.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }
    
}
