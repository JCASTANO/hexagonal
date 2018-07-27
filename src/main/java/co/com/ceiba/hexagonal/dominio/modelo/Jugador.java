package co.com.ceiba.hexagonal.dominio.modelo;

public class Jugador {

	private final String correoElectronico;
	private final String numeroCuentaBancaria;
	private final String numeroCelular;

	public Jugador(String correoElectronico, String numeroCuentaBancaria, String numeroCelular) {
		this.correoElectronico = correoElectronico;
		this.numeroCuentaBancaria = numeroCuentaBancaria;
		this.numeroCelular = numeroCelular;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public String getNumeroCuentaBancaria() {
		return numeroCuentaBancaria;
	}

	public String getNumeroCelular() {
		return numeroCelular;
	}

	@Override
	public String toString() {
		return "Jugador [correoElectronico=" + correoElectronico + ", numeroCuentaBancaria=" + numeroCuentaBancaria
				+ ", numeroCelular=" + numeroCelular + "]";
	}
}
