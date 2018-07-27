package co.com.ceiba.hexagonal.aplicacion;

import java.util.Optional;

import javax.inject.Inject;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.BilleteValidadorResultado;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.servicio.ServicioLoteria;

public class ManejadorEnviarBilletes {

	private final ServicioLoteria servicioLoteria;
	private final EventoLog eventoLog;
	
	@Inject
	public ManejadorEnviarBilletes(ServicioLoteria servicioLoteria,EventoLog eventoLog) {
		this.servicioLoteria = servicioLoteria;
		this.eventoLog = eventoLog;
	}
	
	public Optional<Billete> enviarBillete(Billete billete) {
		Optional<Billete> billeteEnviado = this.servicioLoteria.enviarBillete(billete);
		if(billeteEnviado.isPresent()) {
			this.eventoLog.printAll(billeteEnviado.get().getEventos());
			billeteEnviado.get().clearEventos();
		}
		return billeteEnviado;
	}
	
	public BilleteValidadorResultado validarSiElBilleteGano(int id, NumerosLoteria numerosGanadores) {
		return this.servicioLoteria.validarSiElBilleteGano(id, numerosGanadores);
	}
}
