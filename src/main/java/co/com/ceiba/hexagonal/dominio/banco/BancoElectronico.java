package co.com.ceiba.hexagonal.dominio.banco;

public interface BancoElectronico {

	void realizarPago(String numeroCuentaBancaria, int monto);

	int obtenerTotalEnCuenta(String numeroCuentaBancaria);

	boolean transferirFondos(int monto, String numeroCuentaBancariaOrigen, String numeroCuentaBancariaDestino);

}
