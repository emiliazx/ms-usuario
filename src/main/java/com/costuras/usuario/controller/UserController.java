package com.costuras.usuario.controller;

import com.costuras.usuario.dto.AddDireccionesRequest;
import com.costuras.usuario.dto.DireccionesResponse;
import com.costuras.usuario.dto.HistorialVentaResponse;
import com.costuras.usuario.model.User;
import com.costuras.usuario.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Gestión de direcciones e historial de compras del usuario")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Ver mis direcciones", description = "Obtiene todas las direcciones registradas del usuario autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Direcciones obtenidas correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/direcciones")
    public ResponseEntity<List<DireccionesResponse>> getDirecciones(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getDirecciones(currentUser.getId()));
    }

    @Operation(summary = "Agregar dirección", description = "Registra una nueva dirección de despacho para el usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dirección agregada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/direcciones")
    public ResponseEntity<Map<String, Object>> addDireccion(
            @Valid @RequestBody AddDireccionesRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        DireccionesResponse direccion = userService.addDireccion(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Dirección agregada correctamente", "direccion", direccion));
    }

    @Operation(summary = "Actualizar dirección", description = "Modifica los datos de una dirección existente del usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección actualizada correctamente"),
        @ApiResponse(responseCode = "403", description = "No tienes permiso para modificar esta dirección"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @PutMapping("/direcciones/{id}")
    public ResponseEntity<Map<String, Object>> updateDireccion(
            @PathVariable Integer id,
            @Valid @RequestBody AddDireccionesRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        DireccionesResponse direccion = userService.updateDireccion(id, request, currentUser.getId());
        return ResponseEntity.ok(Map.of("mensaje", "Dirección actualizada correctamente", "direccion", direccion));
    }

    @Operation(summary = "Eliminar dirección", description = "Elimina una dirección del usuario por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<Map<String, String>> deleteDireccion(
            @PathVariable Integer id, Authentication authentication) {
        userService.deleteDireccion(id);
        return ResponseEntity.ok(Map.of("mensaje", "Dirección eliminada correctamente"));
    }

    @Operation(summary = "Ver historial de compras",
               description = "Obtiene el historial de ventas del usuario consultando el microservicio de Ventas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/historial")
    public ResponseEntity<List<HistorialVentaResponse>> getHistorial(
            @RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(userService.getHistorial(bearerToken));
    }
}