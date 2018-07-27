package co.com.ceiba.hexagonal.infraestructura.eventolog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;

public class StdOutEventLog implements EventoLog {

	private static final Logger LOGGER = LoggerFactory.getLogger(StdOutEventLog.class);

	@Override
	public void printAll(List<String> eventos) {
		eventos.forEach(LOGGER::info);
	}
}
