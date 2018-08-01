package co.com.ceiba.hexagonal.aplicacion;

import java.util.Optional;

import javax.inject.Inject;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.servicio.ServicioBillete;

public class ManejadorEnviarBillete {

	private final ServicioBillete servicioBillete;
	private final EventoLog eventoLog;
	
	@Inject
	public ManejadorEnviarBillete(ServicioBillete servicioBillete,EventoLog eventoLog) {
		this.servicioBillete = servicioBillete;
		this.eventoLog = eventoLog;
	}
	
	public Optional<Billete> ejecutar(Billete billete) {
		Optional<Billete> billeteEnviado = this.servicioBillete.enviarBillete(billete);
		if(billeteEnviado.isPresent()) {
			this.eventoLog.printAll(billeteEnviado.get().getEventos());
			billeteEnviado.get().clearEventos();
		}
		return billeteEnviado;
	}
}
