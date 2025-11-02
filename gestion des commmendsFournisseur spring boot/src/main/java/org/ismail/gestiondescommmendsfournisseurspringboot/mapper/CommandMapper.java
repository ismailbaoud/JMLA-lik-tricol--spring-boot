package org.ismail.gestiondescommmendsfournisseurspringboot.mapper;

import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommendeDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;

@org.mapstruct.Mapper
public interface Mapper {
    Commande dtoToEntity(CommendeDTO source);
    CommendeDTO EntityToDto(Commande destination);
}
