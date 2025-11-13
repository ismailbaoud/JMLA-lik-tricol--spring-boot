package org.ismail.mouvementstock.service;

import org.ismail.mouvementstock.client.ProductClient;
import org.ismail.mouvementstock.dto.*;
import org.ismail.mouvementstock.exception.ResourceNotFoundException;
import org.ismail.mouvementstock.model.*;
import org.ismail.mouvementstock.repository.MouvementStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MouvementStockServiceTest {

    @Mock
    private MouvementStockRepository repository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private MouvementStockServiceImpl service;



    @Test
    void shouldCreateMouvementEntreeSuccessfully() {
        // GIVEN
        MouvementStockRequestDTO request = new MouvementStockRequestDTO();
        request.setProduitId(1L);
        request.setQuantite(10);
        request.setTypeMvt(TypeMouvement.ENTREE);
        request.setPrixAchat(100.0);
        request.setRefCommande(123L);

        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setQuantiteStock(50);

        MouvementStock savedEntity = new MouvementStock();
        savedEntity.setId(99L);
        savedEntity.setProduitId(1L);
        savedEntity.setTypeMvt(TypeMouvement.ENTREE);
        savedEntity.setQuantite(10);

        given(productClient.getProductById(1L)).willReturn(product);
        given(repository.save(any(MouvementStock.class))).willReturn(savedEntity);

        // WHEN
        MouvementStockResponseDTO response = service.createMouvement(request);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(99L);
        then(productClient).should().updateProductQuantity(1L, 60);
        then(repository).should().save(any(MouvementStock.class));
    }


    @Test
    void shouldThrowWhenProductNotFound() {
        // GIVEN
        MouvementStockRequestDTO request = new MouvementStockRequestDTO();
        request.setProduitId(1L);
        given(productClient.getProductById(1L)).willReturn(null);

        // WHEN + THEN
        assertThatThrownBy(() -> service.createMouvement(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produit non trouvé");
    }


    @Test
    void shouldThrowWhenStockInsufficientOnSortie() {
        // GIVEN
        MouvementStockRequestDTO request = new MouvementStockRequestDTO();
        request.setProduitId(1L);
        request.setQuantite(100);
        request.setTypeMvt(TypeMouvement.SORTIE);

        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setQuantiteStock(20);

        given(productClient.getProductById(1L)).willReturn(product);

        // WHEN + THEN
        assertThatThrownBy(() -> service.createMouvement(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock insuffisant");
    }


    @Test
    void shouldDeleteMouvementAndRestoreStock() {
        // GIVEN
        MouvementStock mouvement = new MouvementStock();
        mouvement.setId(1L);
        mouvement.setProduitId(10L);
        mouvement.setTypeMvt(TypeMouvement.ENTREE);
        mouvement.setQuantite(5);

        ProductDTO product = new ProductDTO();
        product.setId(10L);
        product.setQuantiteStock(100);

        given(repository.findById(1L)).willReturn(Optional.of(mouvement));
        given(productClient.getProductById(10L)).willReturn(product);

        // WHEN
        service.deleteMouvement(1L);

        // THEN
        then(productClient).should().updateProductQuantity(10L, 95);
        then(repository).should().deleteById(1L);
    }


    @Test
    void shouldReturnPagedMouvements() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 5);
        MouvementStock m1 = new MouvementStock(); m1.setId(1L);
        given(repository.findAll(pageable)).willReturn(new PageImpl<>(List.of(m1)));

        // WHEN
        var page = service.getAllMouvements(pageable);

        // THEN
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
    }


    @Test
    void shouldReturnMouvementsByProduitId() {
        // GIVEN
        MouvementStock m1 = new MouvementStock(); m1.setId(1L);
        given(repository.findByProduitId(10L)).willReturn(List.of(m1));

        // WHEN
        var result = service.getMouvementsByProduitId(10L);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }
}
