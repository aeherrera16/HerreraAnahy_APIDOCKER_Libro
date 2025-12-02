package com.espe.test.test.services;

import com.espe.test.test.models.entities.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {
    List<Libro> buscarTodos();
    Optional<Libro> buscarPorId(Long id);
    Libro guardar(Libro libro);
    void eliminar(Long id);

}
