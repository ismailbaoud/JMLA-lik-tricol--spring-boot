package org.ismail.gestiondescommmendsfournisseurspringboot.exeption;

import org.springframework.http.ResponseEntity;

public class ItemNotFound {


    public ResponseEntity<Object> itemNotFoundException(ApiRequestExeption exeption) {
        return ResponseEntity.status(404).body(exeption.getMessage());
    }
}
