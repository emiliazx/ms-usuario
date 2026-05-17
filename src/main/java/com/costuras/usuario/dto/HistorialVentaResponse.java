package com.costuras.usuario.dto;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialVentaResponse {
    private String idventa;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private String idPagoExterno;
    private List<ItemResponse> items;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemResponse{
        private String idProducto;
        private Integer cantidad;
        private BigDecimal precio;
    }
}
