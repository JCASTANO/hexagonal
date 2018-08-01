package co.com.ceiba.hexagonal.aplicacion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.inject.Inject;

import co.com.ceiba.hexagonal.aplicacion.evento.EventoLog;
import co.com.ceiba.hexagonal.dominio.modelo.Billete;
import co.com.ceiba.hexagonal.dominio.modelo.ConstantesLoteria;
import co.com.ceiba.hexagonal.dominio.modelo.Jugador;
import co.com.ceiba.hexagonal.dominio.modelo.NumerosLoteria;
import co.com.ceiba.hexagonal.dominio.servicio.ServicioBillete;
import co.com.ceiba.hexagonal.infraestructura.banco.BancoElectronicoEnMemoria;

public class ManejadorEnviarBilletesAlteatorio {
	
	private static final List<Jugador> JUGADORES;

	static {
		JUGADORES = new ArrayList<>();
		JUGADORES.add(new Jugador("john@google.com", "312-342", "+3242434242"));
		JUGADORES.add(new Jugador("mary@google.com", "234-987", "+23452346"));
		JUGADORES.add(new Jugador("steve@google.com", "833-836", "+63457543"));
		JUGADORES.add(new Jugador("wayne@google.com", "319-826", "+24626"));
		JUGADORES.add(new Jugador("johnie@google.com", "983-322", "+3635635"));
		JUGADORES.add(new Jugador("andy@google.com", "934-734", "+0898245"));
		JUGADORES.add(new Jugador("richard@google.com", "536-738", "+09845325"));
		JUGADORES.add(new Jugador("kevin@google.com", "453-936", "+2423532"));
		JUGADORES.add(new Jugador("arnold@google.com", "114-988", "+5646346524"));
		JUGADORES.add(new Jugador("ian@google.com", "663-765", "+928394235"));
		JUGADORES.add(new Jugador("robin@google.com", "334-763", "+35448"));
		JUGADORES.add(new Jugador("ted@google.com", "735-964", "+98752345"));
		JUGADORES.add(new Jugador("larry@google.com", "734-853", "+043842423"));
		JUGADORES.add(new Jugador("calvin@google.com", "334-746", "+73294135"));
		JUGADORES.add(new Jugador("jacob@google.com", "444-766", "+358042354"));
		JUGADORES.add(new Jugador("edwin@google.com", "895-345", "+9752435"));
		JUGADORES.add(new Jugador("mary@google.com", "760-009", "+34203542"));
		JUGADORES.add(new Jugador("lolita@google.com", "425-907", "+9872342"));
		JUGADORES.add(new Jugador("bruno@google.com", "023-638", "+673824122"));
		JUGADORES.add(new Jugador("peter@google.com", "335-886", "+5432503945"));
		JUGADORES.add(new Jugador("warren@google.com", "225-946", "+9872341324"));
		JUGADORES.add(new Jugador("monica@google.com", "265-748", "+134124"));
		JUGADORES.add(new Jugador("ollie@google.com", "190-045", "+34453452"));
		JUGADORES.add(new Jugador("yngwie@google.com", "241-465", "+9897641231"));
		JUGADORES.add(new Jugador("lars@google.com", "746-936", "+42345298345"));
		JUGADORES.add(new Jugador("bobbie@google.com", "946-384", "+79831742"));
		JUGADORES.add(new Jugador("tyron@google.com", "310-992", "+0498837412"));
		JUGADORES.add(new Jugador("tyrell@google.com", "032-045", "+67834134"));
		JUGADORES.add(new Jugador("nadja@google.com", "000-346", "+498723"));
		JUGADORES.add(new Jugador("wendy@google.com", "994-989", "+987324454"));
		JUGADORES.add(new Jugador("luke@google.com", "546-634", "+987642435"));
		JUGADORES.add(new Jugador("bjorn@google.com", "342-874", "+7834325"));
		JUGADORES.add(new Jugador("lisa@google.com", "024-653", "+980742154"));
		JUGADORES.add(new Jugador("anton@google.com", "834-935", "+876423145"));
		JUGADORES.add(new Jugador("bruce@google.com", "284-936", "+09843212345"));
		JUGADORES.add(new Jugador("ray@google.com", "843-073", "+678324123"));
		JUGADORES.add(new Jugador("ron@google.com", "637-738", "+09842354"));
		JUGADORES.add(new Jugador("xavier@google.com", "143-947", "+375245"));
		JUGADORES.add(new Jugador("harriet@google.com", "842-404", "+131243252"));
		
		BancoElectronicoEnMemoria bancoloElectronico = new BancoElectronicoEnMemoria();
		Random random = new Random();
		for (int i = 0; i < JUGADORES.size(); i++) {
			bancoloElectronico.realizarPago(JUGADORES.get(i).getNumeroCuentaBancaria(),
					random.nextInt(ConstantesLoteria.MAXIMO_SALDO_JUGADOR));
		}
	}
	
	private final ServicioBillete servicioBillete;
	private final EventoLog eventoLog;
	
	@Inject
	public ManejadorEnviarBilletesAlteatorio(ServicioBillete servicioBillete,EventoLog eventoLog) {
		this.servicioBillete = servicioBillete;
		this.eventoLog = eventoLog;
	}

	public void enviar(int numeroDeBilletes) {
		LinkedList<String> eventos = new LinkedList<>();
		for (int i = 0; i < numeroDeBilletes; i++) {
			Billete billete = new Billete(obtenerJugadorRandomico(),NumerosLoteria.crearAleatorio());
			Optional<Billete> billeteEnviado = servicioBillete.enviarBillete(billete);
			if(billeteEnviado.isPresent()) {
				eventos.addAll(billeteEnviado.get().getEventos());
				billeteEnviado.get().clearEventos();
			}
		}
		this.eventoLog.printAll(eventos);
	}
	
	private static Jugador obtenerJugadorRandomico() {
		Random random = new Random();
		int idx = random.nextInt(JUGADORES.size());
		return JUGADORES.get(idx);
	}
}
