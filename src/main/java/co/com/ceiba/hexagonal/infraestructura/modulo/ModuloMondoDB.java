package co.com.ceiba.hexagonal.infraestructura.modulo;

import com.google.inject.AbstractModule;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;
import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;
import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;
import co.com.ceiba.hexagonal.infraestructura.banco.BancoElectronicoMongoDB;
import co.com.ceiba.hexagonal.infraestructura.eventolog.EventoLogMongoDB;
import co.com.ceiba.hexagonal.infraestructura.mongodb.MongoConnectionPropertiesLoader;
import co.com.ceiba.hexagonal.infraestructura.repositorio.RepositorioBilleteMongoDB;

public class ModuloMondoDB extends AbstractModule {
	@Override
	protected void configure() {
		MongoConnectionPropertiesLoader.load();
		bind(RepositorioBillete.class).to(RepositorioBilleteMongoDB.class);
		bind(EventoLog.class).to(EventoLogMongoDB.class);
		bind(BancoElectronico.class).to(BancoElectronicoMongoDB.class);
	}
}
