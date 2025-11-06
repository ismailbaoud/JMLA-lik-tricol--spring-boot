package org.ismail.gestiondescommmendsfournisseurspringboot.controller;

import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeRequestDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.UpdateStatusRequest;
import org.ismail.gestiondescommmendsfournisseurspringboot.service.CommendeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/commandes")
public class CommendeController {

    @Autowired
    public CommendeServiceImpl commendeServiceImpl;

    @PostMapping
    public CommandeResponseDTO creerCommende(@RequestBody CommandeRequestDTO commandeRequest) {
        return commendeServiceImpl.creerCommende(commandeRequest);
    }

    @GetMapping("/{id}")
    public CommandeResponseDTO findById(@PathVariable Long id) {
        return commendeServiceImpl.findById(id);
    }

    @GetMapping
    public Page<CommandeResponseDTO> findAll(Pageable pageable) {
        return commendeServiceImpl.findAll(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        commendeServiceImpl.deleteById(id);
    }

    @PatchMapping("/{id}/status")
    public CommandeResponseDTO updateCommendeStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return commendeServiceImpl.updateCommendeStatus(id, request.getStatus());
    }

}