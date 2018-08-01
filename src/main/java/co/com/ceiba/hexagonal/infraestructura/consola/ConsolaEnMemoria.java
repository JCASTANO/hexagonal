package co.com.ceiba.hexagonal.infraestructura.consola;

import com.google.inject.Guice;
import com.google.inject.Injector;

import co.com.ceiba.hexagonal.aplicacion.ManejadorAdministrarLoteria;
import co.com.ceiba.hexagonal.aplicacion.ManejadorEnviarBilletesAlteatorio;
import co.com.ceiba.hexagonal.infraestructura.modulo.ModuloEnMemoria;

public class ConsolaEnMemoria {

  public static void main(String[] args) {

    Injector injector = Guice.createInjector(new ModuloEnMemoria());

    // inicia una nueva ronda de loteria
    ManejadorAdministrarLoteria manejadorAdministrarLoteria = injector.getInstance(ManejadorAdministrarLoteria.class);
    manejadorAdministrarLoteria.resetearLoteria();
    
    // enviar los billetes
    ManejadorEnviarBilletesAlteatorio manejadorEnviarBilletesAlteatorio = injector.getInstance(ManejadorEnviarBilletesAlteatorio.class);
    manejadorEnviarBilletesAlteatorio.enviar(10);
    
    // inica la loteria
    manejadorAdministrarLoteria.jugarLoteria();
  }
}
