package co.com.ceiba.hexagonal.infraestructura.banco;

import java.util.HashMap;
import java.util.Map;

import co.com.ceiba.hexagonal.dominio.banco.BancoElectronico;
import co.com.ceiba.hexagonal.dominio.modelo.ConstantesLoteria;

public class BancoElectronicoEnMemoria implements BancoElectronico {

	private static Map<String, Integer> cuentasBancarias = new HashMap<>();

	static {
		cuentasBancarias.put(ConstantesLoteria.CUENTA_BANCARIA_DE_SERVICIO, ConstantesLoteria.CUENTA_BANCARIA_DE_SERVICIO_SALDO);
	}

	@Override
	public void realizarPago(String numeroCuentaBancaria, int monto) {
		cuentasBancarias.put(numeroCuentaBancaria, monto);
	}

	@Override
	public int obtenerTotalEnCuenta(String numeroCuentaBancaria) {
		return cuentasBancarias.getOrDefault(numeroCuentaBancaria, 0);
	}

	@Override
	public boolean transferirFondos(int monto, String numeroCuentaBancariaOrigen, String numeroCuentaBancariaDestino) {
		if (cuentasBancarias.getOrDefault(numeroCuentaBancariaOrigen, 0) >= monto) {
			cuentasBancarias.put(numeroCuentaBancariaOrigen, cuentasBancarias.get(numeroCuentaBancariaOrigen) - monto);
			cuentasBancarias.put(numeroCuentaBancariaDestino, cuentasBancarias.get(numeroCuentaBancariaDestino) + monto);
			return true;
		} else {
			return false;
		}
	}
}
