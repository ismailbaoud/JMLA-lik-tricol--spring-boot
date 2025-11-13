package org.ismail.gestiondescommmendsfournisseurspringboot.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CommendeControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void itShouldCreateCommande() throws Exception {
        mockMvc.perform(post("/api/v1/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "description": "Test order" }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/api/v1/commandes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Test order"));
    }

    @Test
    public void itShouldGetCommandeById() throws Exception {
        String response = mockMvc.perform(post("/api/v1/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "description": "Order by ID" }
                """))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        String id = response.substring(response.indexOf("\"id\":") + 5, response.indexOf(","));

        mockMvc.perform(get("/api/v1/commandes/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Order by ID"));
    }

    @Test
    public void itShouldUpdateCommande() throws Exception {
        String response = mockMvc.perform(post("/api/v1/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "description": "Original description" }
                """))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        String id = response.substring(response.indexOf("\"id\":") + 5, response.indexOf(","));

        mockMvc.perform(put("/api/v1/commandes/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "description": "Updated description" }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    public void itShouldDeleteCommande() throws Exception {
        String response = mockMvc.perform(post("/api/v1/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "description": "To be deleted" }
                """))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        String id = response.substring(response.indexOf("\"id\":") + 5, response.indexOf(","));

        mockMvc.perform(delete("/api/v1/commandes/" + id))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/commandes/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    public void itShouldReturnNotFoundForNonExistentCommande() throws Exception {
        mockMvc.perform(get("/api/v1/commandes/999999"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void itShouldReturnBadRequestForInvalidCommande() throws Exception {
        mockMvc.perform(post("/api/v1/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "invalid": "field" }
                """))
            .andExpect(status().isBadRequest());
    }
}