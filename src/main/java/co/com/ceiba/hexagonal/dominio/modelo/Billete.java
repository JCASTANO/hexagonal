package co.com.ceiba.hexagonal.dominio.modelo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Billete {

	private static volatile int numAllocated;
	private final int id;
	private final Jugador jugador;
	private final NumerosLoteria numerosLoteria;
	private LinkedList<String> eventos;

	public Billete(Jugador jugador, NumerosLoteria numerosLoteria) {
		this.id = numAllocated + 1;
		numAllocated++;
		this.jugador = jugador;
		this.numerosLoteria = numerosLoteria;
		this.eventos = new LinkedList<>();
	}
	
	public Billete(Integer id,Jugador jugador, NumerosLoteria numerosLoteria) {
		this.id = id;
		this.jugador = jugador;
		this.numerosLoteria = numerosLoteria;
		this.eventos = new LinkedList<>();
	}

	public Jugador getJugador() {
		return jugador;
	}

	public NumerosLoteria getNumerosLoteria() {
		return numerosLoteria;
	}

	public int getId() {
		return id;
	}
	
	public List<String> getEventos() {
		return Collections.unmodifiableList(eventos);
	}
	
	public void clearEventos() {
		eventos.clear();
	}
	
	public void adicionarEvento(String evento) {
		this.eventos.add(evento);
	}
	
	@Override
	public String toString() {
		return "Billete [id=" + id + ", jugador=" + jugador + ", numerosLoteria=" + numerosLoteria + "]";
	}
}
