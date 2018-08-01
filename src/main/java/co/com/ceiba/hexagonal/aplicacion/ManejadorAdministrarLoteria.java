package co.com.ceiba.hexagonal.aplicacion;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.EstadoBillete;
import co.com.ceiba.hexagonal.dominio.servicio.ServicioAdministracionLoteria;

public class ManejadorAdministrarLoteria {

	private final ServicioAdministracionLoteria servicioAdministracionLoteria;
	private final EventoLog eventoLog;
	
	@Inject
	public ManejadorAdministrarLoteria(ServicioAdministracionLoteria servicioAdministracionLoteria,EventoLog eventoLog) {
		this.servicioAdministracionLoteria = servicioAdministracionLoteria;
		this.eventoLog = eventoLog;
	}

	public void resetearLoteria() {
		this.servicioAdministracionLoteria.resetearLoteria();
	}
	
	public String jugarLoteria() {
		NumerosLoteria numeros = NumerosLoteria.crearAleatorio();
		List<String> eventos = this.servicioAdministracionLoteria.jugarLoteria(numeros);
		this.eventoLog.printAll(eventos);
		return numeros.obtenerNumerosComoString();
	}
	
	public Map<Integer, Billete> obtenerBilletesEnviados() {
		return this.servicioAdministracionLoteria.obtenerBilletesEnviados();
	}
	
	public EstadoBillete validarBilleteGanador(int idBillete, NumerosLoteria numerosGanadores) {
		return this.servicioAdministracionLoteria.validarBilleteGanador(idBillete, numerosGanadores);
	}
}
