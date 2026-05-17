package com.costuras.usuario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costuras.usuario.dto.AddDireccionesRequest;
import com.costuras.usuario.dto.DireccionesResponse;
import com.costuras.usuario.dto.HistorialVentaResponse;
import com.costuras.usuario.model.User;
import com.costuras.usuario.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/direcciones")
    public ResponseEntity<List<DireccionesResponse>> getDirecciones(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getDirecciones(currentUser.getId()));
    }

    @PostMapping("/direcciones")
    public ResponseEntity<Map<String, Object>> addDireccion(
            @Valid @RequestBody AddDireccionesRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        DireccionesResponse direccion = userService.addDireccion(request, currentUser.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                    "mensaje", "Dirección agregada correctamente",
                    "direccion", direccion
                ));
    }

    @PutMapping("/direcciones/{id}")
    public ResponseEntity<Map<String, Object>> updateDireccion(
            @PathVariable Integer id,
            @Valid @RequestBody AddDireccionesRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        DireccionesResponse direccion = userService.updateDireccion(id, request, currentUser.getId());
        return ResponseEntity.ok(Map.of(
            "mensaje", "Dirección actualizada correctamente",
            "direccion", direccion
        ));
    }

    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<Map<String, String>> deleteDireccion(
            @PathVariable Integer id,
            Authentication authentication) {
        userService.deleteDireccion(id);
        return ResponseEntity.ok(Map.of("mensaje", "Dirección eliminada correctamente"));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<HistorialVentaResponse>> getHistorial(
            @RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(userService.getHistorial(bearerToken));
    }
}