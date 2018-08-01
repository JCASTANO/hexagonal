package co.com.ceiba.hexagonal.infraestructura.consola;

import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import co.com.ceiba.hexagonal.aplicacion.ManejadorAdministrarLoteria;
import co.com.ceiba.hexagonal.aplicacion.ManejadorEnviarBillete;
import co.com.ceiba.hexagonal.aplicacion.ManejadorGestionarBancoElectronico;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.Jugador;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.EstadoBillete;
import co.com.ceiba.hexagonal.infraestructura.modulo.ModuloMondoDB;

public class ConsolaJugador {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsolaJugador.class);

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ModuloMondoDB());
		
		ManejadorEnviarBillete manejadorEnviarBillete = injector.getInstance(ManejadorEnviarBillete.class);
		ManejadorGestionarBancoElectronico manejadorGestionarBancoElectronico = injector.getInstance(ManejadorGestionarBancoElectronico.class);
		ManejadorAdministrarLoteria manejadorAdministrarLoteria = injector.getInstance(ManejadorAdministrarLoteria.class);
		
		try (final Scanner scanner = new Scanner(System.in)) {
			boolean exit = false;
			while (!exit) {
				printMainMenu();
				String cmd = readString(scanner);
				if ("1".equals(cmd)) {
					consultarMontoCuentaBancaria(manejadorGestionarBancoElectronico, scanner);
				} else if ("2".equals(cmd)) {
					adicionarFondosCuentaLoteria(manejadorGestionarBancoElectronico, scanner);
				} else if ("3".equals(cmd)) {
					enviarBillete(manejadorEnviarBillete,scanner);
				} else if ("4".equals(cmd)) {
					validarBillete(manejadorAdministrarLoteria, scanner);
				} else if ("5".equals(cmd)) {
					exit = true;
				} else {
					LOGGER.info("Comando desconocido");
				}
			}
		}
	}

	private static void validarBillete(ManejadorAdministrarLoteria manejadorAdministrarLoteria, Scanner scanner) {
		LOGGER.info("Cuál es el ID del billete de lotería?");
		String idBillete = readString(scanner);
		LOGGER.info("Dar los 4 números ganadores separados por coma");
		String numerosApostados = readString(scanner);
		try {
			String[] parts = numerosApostados.split(",");
			Set<Integer> numerosGanadores = new HashSet<>();
			for (int i = 0; i < 4; i++) {
				numerosGanadores.add(Integer.parseInt(parts[i]));
			}
			EstadoBillete estadoBillete = manejadorAdministrarLoteria.validarBilleteGanador(Integer.valueOf(idBillete),NumerosLoteria.crear(numerosGanadores));
			if (estadoBillete.getEstado().equals(EstadoBillete.Estado.GANO)) {
				LOGGER.info("¡Felicidades! ¡El billete de lotería ha ganado!");
			} else if (estadoBillete.getEstado().equals(EstadoBillete.Estado.NO_GANO)) {
				LOGGER.info("Lamentablemente, el billete de lotería no ganó.");
			} else {
				LOGGER.info("Tal billete de lotería no ha sido enviado.");
			}
		} catch (Exception e) {
			LOGGER.info("Error al verificar el billete de lotería. Inténtalo de nuevo.");
		}
	}

	private static void enviarBillete(ManejadorEnviarBillete manejadorEnviarBillete, Scanner scanner) {
		LOGGER.info("Cual es tu correo electronico?");
		String correoElectronico = readString(scanner);
		LOGGER.info("Cual es tu numero de cuenta bancaria?");
		String numeroCuentaBancaria = readString(scanner);
		LOGGER.info("Cual es tu numero de celular?");
		String numeroCelular = readString(scanner);
		Jugador jugador = new Jugador(correoElectronico, numeroCuentaBancaria, numeroCelular);
		LOGGER.info("Dame 4 número separados por coma?");
		String numbers = readString(scanner);
		try {
			String[] parts = numbers.split(",");
			Set<Integer> chosen = new HashSet<>();
			for (int i = 0; i < 4; i++) {
				chosen.add(Integer.parseInt(parts[i]));
			}
			NumerosLoteria numerosLoteria = NumerosLoteria.crear(chosen);
			Billete billete = new Billete(jugador, numerosLoteria);
			Optional<Billete> billeteGuardado = manejadorEnviarBillete.ejecutar(billete);
			if (billeteGuardado.isPresent()) {
				LOGGER.info("Billete de lotería con ID: {}", billeteGuardado.get().getId());
			} else {
				LOGGER.info("Error enviando el billete de lotería. Inténtalo de nuevo.");
			}
		} catch (Exception e) {
			LOGGER.info("Error enviando el billete de lotería. Inténtalo de nuevo.");
		}
	}

	private static void adicionarFondosCuentaLoteria(ManejadorGestionarBancoElectronico manejadorGestionarBancoElectronico, Scanner scanner) {
		LOGGER.info("Cual es el numero de cuenta bancaria?");
		String numeroCuentaBancaria = readString(scanner);
		LOGGER.info("Cual es el monto que deseas depositar?");
		String monto = readString(scanner);
		manejadorGestionarBancoElectronico.realizarPago(numeroCuentaBancaria, Integer.parseInt(monto));
		LOGGER.info("La cuenta {} ahora tiene {} creditos.", numeroCuentaBancaria, manejadorGestionarBancoElectronico.obtenerTotalEnCuenta(numeroCuentaBancaria));
	}

	private static void consultarMontoCuentaBancaria(ManejadorGestionarBancoElectronico manejadorGestionarBancoElectronico, Scanner scanner) {
		LOGGER.info("Cual es el numero de cuenta bancaria?");
		String numeroCuentaBancaria = readString(scanner);
		LOGGER.info("La cuenta {} tiene {} creditos.", numeroCuentaBancaria, manejadorGestionarBancoElectronico.obtenerTotalEnCuenta(numeroCuentaBancaria));
	}

	private static void printMainMenu() {
		LOGGER.info("");
		LOGGER.info("### Loteria Service Console ###");
		LOGGER.info("(1) Consulta fondos de cuenta bancaria");
		LOGGER.info("(2) Adicionar fondos a cuenta bancaria");
		LOGGER.info("(3) Enviar billete");
		LOGGER.info("(4) Validar billete");
		LOGGER.info("(5) Salir");
	}

	private static String readString(Scanner scanner) {
		System.out.print("> ");
		return scanner.next();
	}
}
