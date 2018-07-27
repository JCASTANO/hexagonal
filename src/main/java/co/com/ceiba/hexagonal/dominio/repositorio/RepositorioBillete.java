package co.com.ceiba.hexagonal.dominio.repositorio;

import java.util.Map;
import java.util.Optional;

import co.com.ceiba.hexagonal.dominio.modelo.Billete;

public interface RepositorioBillete {

  Optional<Billete> buscarPorId(int id);
  Optional<Billete> guardar(Billete billete);
  Map<Integer, Billete> buscarTodos();
  void borrarTodos();
  
}
