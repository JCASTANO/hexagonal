package co.com.ceiba.hexagonal.dominio.modelo;

public class EstadoBillete {

  public enum Estado { GANO, NO_GANO, BILLETE_NO_ENVIADO }

  private final Estado estado;
  private final int monto;


  public EstadoBillete(Estado estado) {
    this.estado = estado;
    this.monto = 0;
  }

  public EstadoBillete(Estado estado, int monto) {
    this.estado = estado;
    this.monto = monto;
  }

  public Estado getEstado() {
    return estado;
  }

  public int getMonto() {
    return monto;
  }
}
