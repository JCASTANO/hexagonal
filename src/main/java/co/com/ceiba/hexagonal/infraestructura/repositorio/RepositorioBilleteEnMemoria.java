package co.com.ceiba.hexagonal.infraestructura.repositorio;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;

public class RepositorioBilleteEnMemoria implements RepositorioBillete {

	private static Map<Integer, Billete> billetes = new HashMap<>();

	@Override
	public Optional<Billete> buscarPorId(int id) {
		Billete billete = billetes.get(id);
		if (billete == null) {
			return Optional.empty();
		} else {
			return Optional.of(billete);
		}
	}

	@Override
	public Optional<Billete> guardar(Billete billete) {
		billetes.put(billete.getId(), billete);
		return Optional.of(billete);
	}

	@Override
	public Map<Integer, Billete> buscarTodos() {
		return billetes;
	}

	@Override
	public void borrarTodos() {
		billetes.clear();
	}
}
