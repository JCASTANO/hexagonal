package co.com.ceiba.hexagonal.infraestructura.modulo;

import com.google.inject.AbstractModule;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;
import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;
import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;
import co.com.ceiba.hexagonal.infraestructura.banco.BancoElectronicoEnMemoria;
import co.com.ceiba.hexagonal.infraestructura.eventolog.StdOutEventLog;
import co.com.ceiba.hexagonal.infraestructura.repositorio.RepositorioBilleteEnMemoria;

public class ModuloEnMemoria extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(RepositorioBillete.class).to(RepositorioBilleteEnMemoria.class);
		bind(EventoLog.class).to(StdOutEventLog.class);
		bind(BancoElectronico.class).to(BancoElectronicoEnMemoria.class);
	}
}
