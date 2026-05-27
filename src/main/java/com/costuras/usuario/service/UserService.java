package com.costuras.usuario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.costuras.usuario.client.VentaClient;
import com.costuras.usuario.dto.AddDireccionesRequest;
import com.costuras.usuario.dto.DireccionesResponse;
import com.costuras.usuario.dto.HistorialVentaResponse;
import com.costuras.usuario.excepciones.AccesoDenegadoException;
import com.costuras.usuario.excepciones.DireccionNotFoundException;
import com.costuras.usuario.model.Direccion;
import com.costuras.usuario.repository.DireccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DireccionRepository direccionRepository;
    private final VentaClient ventaClient;

    public List<HistorialVentaResponse>getHistorial(String bearerToken){
        return ventaClient.getHistorialByToken(bearerToken);
    }



   
    public List<DireccionesResponse> getDirecciones(Integer userId) {
        return direccionRepository.findByUserId(userId)
                .stream()
                .map(d -> DireccionesResponse.builder()
                        .id(d.getId())
                        .calle(d.getCalle())
                        .numero(d.getNumero())
                        .comuna(d.getComuna())
                        .build())
                .toList();
    }

    
    public DireccionesResponse addDireccion(AddDireccionesRequest request, Integer userId) {
        Direccion direccion = Direccion.builder()
                .userId(userId)
                .calle(request.getCalle())
                .numero(request.getNumero())
                .comuna(request.getComuna())
                .build();

        direccionRepository.save(direccion);

        return DireccionesResponse.builder()
                .id(direccion.getId())
                .calle(direccion.getCalle())
                .numero(direccion.getNumero())
                .comuna(direccion.getComuna())
                .build();
    }

  
  public DireccionesResponse updateDireccion(Integer id, AddDireccionesRequest request, Integer userId) {
    Direccion direccion = direccionRepository.findById(id)
            .orElseThrow(() -> new DireccionNotFoundException(id));

    if (!direccion.getUserId().equals(userId)) {
        throw new AccesoDenegadoException();
    }

    direccion.setCalle(request.getCalle());
    direccion.setNumero(request.getNumero());
    direccion.setComuna(request.getComuna());
    direccionRepository.save(direccion);

    return DireccionesResponse.builder()
            .id(direccion.getId())
            .calle(direccion.getCalle())
            .numero(direccion.getNumero())
            .comuna(direccion.getComuna())
            .build();
}

public void deleteDireccion(Integer id) {
    if (!direccionRepository.existsById(id)) {
        throw new DireccionNotFoundException(id);
    }
    direccionRepository.deleteById(id);
}

}
