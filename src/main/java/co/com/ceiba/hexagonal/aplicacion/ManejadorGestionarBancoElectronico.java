package co.com.ceiba.hexagonal.aplicacion;

import javax.inject.Inject;

import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;

public class ManejadorGestionarBancoElectronico {
	
	private final BancoElectronico bancoElectronico;
	
	@Inject
	public ManejadorGestionarBancoElectronico(BancoElectronico bancoElectronico) {
		this.bancoElectronico = bancoElectronico;
	}

	public void realizarPago(String numeroCuentaBancaria, int monto) {
		this.bancoElectronico.realizarPago(numeroCuentaBancaria, monto);
	}

	public int obtenerTotalEnCuenta(String numeroCuentaBancaria) {
		return this.bancoElectronico.obtenerTotalEnCuenta(numeroCuentaBancaria);
	}
}
