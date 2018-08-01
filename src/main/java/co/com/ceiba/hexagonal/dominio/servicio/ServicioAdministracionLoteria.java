package co.com.ceiba.hexagonal.dominio.servicio;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.inject.Inject;

import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.ConstantesLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.EstadoBillete;
import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;

public class ServicioAdministracionLoteria {

	private final RepositorioBillete repositorioBillete;
	private final BancoElectronico bancoElectronico;

	@Inject
	public ServicioAdministracionLoteria(RepositorioBillete repositorioBillete,
			BancoElectronico bancoElectronico) {
		this.repositorioBillete = repositorioBillete;
		this.bancoElectronico = bancoElectronico;
	}

	public Map<Integer, Billete> obtenerBilletesEnviados() {
		return repositorioBillete.buscarTodos();
	}

	public List<String> iniciarLoteria(NumerosLoteria numeros) {
		LinkedList<String> eventos = new LinkedList<>();
		Map<Integer, Billete> billetes = obtenerBilletesEnviados();
		for (Integer idBillete : billetes.keySet()) {
			
			EstadoBillete resultado = validarBilleteGanador(idBillete, numeros);
			Billete billete = billetes.get(idBillete);
			
			if (resultado.getEstado().equals(EstadoBillete.Estado.GANO)) {
				boolean fueTransferido = bancoElectronico.transferirFondos(ConstantesLoteria.CANTIDAD_PREMIO,
						ConstantesLoteria.CUENTA_BANCARIA_DE_SERVICIO,
						billete.getJugador().getNumeroCuentaBancaria());
				
				if (fueTransferido) {
					billete.adicionarEvento(String.format("El billete de %s ganó! Numero de cuenta bancaria %s fue depositada con %s creditos.",
							billete.getJugador().getCorreoElectronico(), billete.getJugador().getNumeroCuentaBancaria(), ConstantesLoteria.CANTIDAD_PREMIO));
				} else {
					billete.adicionarEvento(String.format("El billete de %s ganó! Desafortunadamente la transferencia de %s falló.",billete.getJugador().getCorreoElectronico(), ConstantesLoteria.CANTIDAD_PREMIO));
				}
				
			} else if (resultado.getEstado().equals(EstadoBillete.Estado.NO_GANO)) {
				billete.adicionarEvento(String.format("El billete de %s fue revisado y no gano esta vez", billete.getJugador().getCorreoElectronico()));
			}
			eventos.addAll(billete.getEventos());
			billete.clearEventos();
		}
		return eventos;
	}

	public void resetearLoteria() {
		repositorioBillete.borrarTodos();
	}
	
	public EstadoBillete validarBilleteGanador(int idBillete,NumerosLoteria numerosGanadores) {
		Optional<Billete> optional = repositorioBillete.buscarPorId(idBillete);
		if (optional.isPresent()) {
			if (optional.get().getNumerosLoteria().equals(numerosGanadores)) {
				return new EstadoBillete(EstadoBillete.Estado.GANO, 1000);
			} else {
				return new EstadoBillete(EstadoBillete.Estado.NO_GANO);
			}
		} else {
			return new EstadoBillete(EstadoBillete.Estado.BILLETE_NO_ENVIADO);
		}
	}
}
