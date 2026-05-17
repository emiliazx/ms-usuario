package com.costuras.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddDireccionesRequest {

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    private String numero;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;
}
