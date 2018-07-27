
package co.com.ceiba.hexagonal.infraestructura.consola;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import co.com.ceiba.hexagonal.aplicacion.ManejadorAdministradorConsola;
import co.com.ceiba.hexagonal.aplicacion.ManejadorEnviarBilletesAlteatorio;
import co.com.ceiba.hexagonal.infraestructura.modulo.ModuloMondoDB;
import co.com.ceiba.hexagonal.infraestructura.mongodb.MongoConnectionPropertiesLoader;

public class ConsolaAdministrador {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsolaAdministrador.class);

	public static void main(String[] args) {
		MongoConnectionPropertiesLoader.load();
		Injector injector = Guice.createInjector(new ModuloMondoDB());
		
		ManejadorAdministradorConsola manejadorAdministradorConsola = injector.getInstance(ManejadorAdministradorConsola.class);
		ManejadorEnviarBilletesAlteatorio manejadorEnviarBilletesAlteatorio = injector.getInstance(ManejadorEnviarBilletesAlteatorio.class);
		manejadorEnviarBilletesAlteatorio.enviar(20);
		
		try (Scanner scanner = new Scanner(System.in)) {
			boolean exit = false;
			while (!exit) {
				printMainMenu();
				String cmd = readString(scanner);
				if ("1".equals(cmd)) {
					manejadorAdministradorConsola.obtenerBilletesEnviados()
							.forEach((k, v) -> LOGGER.info("Llave: {}, ValorS: {}", k, v));
				} else if ("2".equals(cmd)) {
					String numeros = manejadorAdministradorConsola.iniciarLoteria();
					LOGGER.info("Los numeros ganadores: {}", numeros);
					LOGGER.info("Tiempo de resetear la base de datos?");
				} else if ("3".equals(cmd)) {
					manejadorAdministradorConsola.resetearLoteria();
					LOGGER.info("La base de datos fue reseteada.");
				} else if ("4".equals(cmd)) {
					exit = true;
				} else {
					LOGGER.info("Comando desconocido: {}", cmd);
				}
			}
		}
	}

	private static void printMainMenu() {
		LOGGER.info("");
		LOGGER.info("### Loteria administracion Console ###");
		LOGGER.info("(1) Mostrar todos los billetes enviados");
		LOGGER.info("(2) Realizar sorteo");
		LOGGER.info("(3) Resetear base de datos");
		LOGGER.info("(4) Exit");
	}

	private static String readString(Scanner scanner) {
		System.out.print("> ");
		return scanner.next();
	}
}
