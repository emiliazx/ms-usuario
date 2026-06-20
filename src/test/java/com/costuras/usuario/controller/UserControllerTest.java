package com.costuras.usuario.controller;

import com.costuras.usuario.dto.AddDireccionesRequest;
import com.costuras.usuario.dto.DireccionesResponse;
import com.costuras.usuario.dto.HistorialVentaResponse;
import com.costuras.usuario.model.User; // Usamos tu clase User nativa del módulo
import com.costuras.usuario.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SuppressWarnings("null")
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UsernamePasswordAuthenticationToken auth;
    private DireccionesResponse direccionResponse;

    @BeforeEach
    void setUp() {
        // CORRECCIÓN: Construimos la entidad User que tu controlador espera recibir en el Auth
        User currentUser = User.builder()
                .id(1)
                .username("juan123")
                .email("juan@mail.com")
                .build();

        // Envolvemos al usuario real en el token de autenticación
        auth = new UsernamePasswordAuthenticationToken(currentUser, null, List.of());

        direccionResponse = new DireccionesResponse();
        direccionResponse.setId(1);
        direccionResponse.setCalle("Av. Principal");
        direccionResponse.setNumero("123");
        direccionResponse.setComuna("Santiago");
    }

    @Test
    void getDirecciones_autenticado_retornaLista() throws Exception {
        when(userService.getDirecciones(1)).thenReturn(List.of(direccionResponse));

        mockMvc.perform(get("/user/direcciones")
                .principal(auth)) // Se pasa de forma nativa a MockMvc
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calle").value("Av. Principal"))
                .andExpect(jsonPath("$[0].comuna").value("Santiago"));
    }

    @Test
    void getDirecciones_listaVacia_retornaArregloVacio() throws Exception {
        when(userService.getDirecciones(1)).thenReturn(List.of());

        mockMvc.perform(get("/user/direcciones")
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void addDireccion_datosValidos_retorna201() throws Exception {
        AddDireccionesRequest request = new AddDireccionesRequest();
        request.setCalle("Calle Nueva");
        request.setNumero("456");
        request.setComuna("Providencia");

        when(userService.addDireccion(any(AddDireccionesRequest.class), eq(1))).thenReturn(direccionResponse);

        mockMvc.perform(post("/user/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(auth))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Dirección agregada correctamente"))
                .andExpect(jsonPath("$.direccion.calle").value("Av. Principal"));
    }

    @Test
    void updateDireccion_propia_retorna200() throws Exception {
        AddDireccionesRequest request = new AddDireccionesRequest();
        request.setCalle("Calle Modificada");
        request.setNumero("789");
        request.setComuna("Las Condes");

        when(userService.updateDireccion(eq(1), any(AddDireccionesRequest.class), eq(1))).thenReturn(direccionResponse);

        mockMvc.perform(put("/user/direcciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Dirección actualizada correctamente"));
    }

    @Test
    void deleteDireccion_existente_retorna200() throws Exception {
        doNothing().when(userService).deleteDireccion(1);

        mockMvc.perform(delete("/user/direcciones/1")
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Dirección eliminada correctamente"));
    }

    @Test
    void getHistorial_autenticado_retornaLista() throws Exception {
        HistorialVentaResponse historial = new HistorialVentaResponse();
        historial.setIdVenta("venta_001");

        when(userService.getHistorial("Bearer token_jwt")).thenReturn(List.of(historial));

        mockMvc.perform(get("/user/historial")
                .header("Authorization", "Bearer token_jwt")
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idVenta").value("venta_001"));
    }
}