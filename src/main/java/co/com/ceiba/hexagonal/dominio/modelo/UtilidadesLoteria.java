package co.com.ceiba.hexagonal.dominio.modelo;

import java.util.Optional;

import co.com.ceiba.hexagonal.dominio.repositorio.RepositorioBillete;

public class UtilidadesLoteria {

	private UtilidadesLoteria() {
	}

	public static BilleteValidadorResultado validarBilleteGanador(RepositorioBillete repository, int id,NumerosLoteria numerosGanadores) {
		Optional<Billete> optional = repository.buscarPorId(id);
		if (optional.isPresent()) {
			if (optional.get().getNumerosLoteria().equals(numerosGanadores)) {
				return new BilleteValidadorResultado(BilleteValidadorResultado.CheckResult.GANO, 1000);
			} else {
				return new BilleteValidadorResultado(BilleteValidadorResultado.CheckResult.NO_GANO);
			}
		} else {
			return new BilleteValidadorResultado(BilleteValidadorResultado.CheckResult.BILLETE_NO_ENVIADO);
		}
	}
}
