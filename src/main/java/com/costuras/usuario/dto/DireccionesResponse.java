package com.costuras.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DireccionesResponse {
    private Integer id;
    private String calle;
    private String numero;
    private String comuna;
}
