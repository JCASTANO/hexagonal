package co.com.ceiba.hexagonal.dominio.servicio;

import java.util.Optional;

import com.google.inject.Inject;

import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.ConstantesLoteria;
import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;

public class ServicioBillete {

	private final RepositorioBillete repositorioBillete;
	private final BancoElectronico bancoElectronico;

	@Inject
	public ServicioBillete(RepositorioBillete repositorioBillete,
			BancoElectronico bancoElectronico) {
		this.repositorioBillete = repositorioBillete;
		this.bancoElectronico = bancoElectronico;
	}

	public Optional<Billete> enviarBillete(Billete billete) {
		boolean transferenciaExitosa = bancoElectronico.transferirFondos(ConstantesLoteria.PRECIO_DE_TIQUETE,billete.getJugador().getNumeroCuentaBancaria(), ConstantesLoteria.CUENTA_BANCARIA_DE_SERVICIO);
		if (!transferenciaExitosa) {
			billete.adicionarEvento(String.format("El billete de %s no pudo jugar debido a que no tiene fondos.", billete.getJugador().getCorreoElectronico()));
			return Optional.empty();
		}
		Optional<Billete> optional = repositorioBillete.guardar(billete);
		if (optional.isPresent()) {
			billete.adicionarEvento(String.format("El billete de %s fue enviado. Numero de cuenta bancaria %s fue cargada para 3 creditos.",billete.getJugador().getCorreoElectronico(),billete.getJugador().getNumeroCuentaBancaria()));
		}
		return optional;
	}
}
