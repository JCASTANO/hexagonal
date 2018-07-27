package co.com.ceiba.hexagonal.dominio.servicio;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.BilleteValidadorResultado;
import co.com.ceiba.hexagonal.dominio.modelo.ConstantesLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.UtilidadesLoteria;
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
		for (Integer id : billetes.keySet()) {
			
			BilleteValidadorResultado resultado = UtilidadesLoteria.validarBilleteGanador(repositorioBillete, id, numeros);
			Billete billete = billetes.get(id);
			
			if (resultado.getResult().equals(BilleteValidadorResultado.CheckResult.GANO)) {
				boolean fueTransferido = bancoElectronico.transferirFondos(ConstantesLoteria.CANTIDAD_PREMIO,
						ConstantesLoteria.CUENTA_BANCARIA_DE_SERVICIO,
						billete.getJugador().getNumeroCuentaBancaria());
				
				if (fueTransferido) {
					billete.adicionarEvento(String.format("El billete de %s ganó! Numero de cuenta bancaria %s fue depositada con %s creditos.",
							billete.getJugador().getCorreoElectronico(), billete.getJugador().getNumeroCuentaBancaria(), ConstantesLoteria.CANTIDAD_PREMIO));
				} else {
					billete.adicionarEvento(String.format("El billete de %s ganó! Desafortunadamente la transferencia de %s falló.",billete.getJugador().getCorreoElectronico(), ConstantesLoteria.CANTIDAD_PREMIO));
				}
				
			} else if (resultado.getResult().equals(BilleteValidadorResultado.CheckResult.NO_GANO)) {
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
}
