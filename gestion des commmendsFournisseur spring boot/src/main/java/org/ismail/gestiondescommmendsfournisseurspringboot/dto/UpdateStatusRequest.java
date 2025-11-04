package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    private CommendeStatus status;
}

