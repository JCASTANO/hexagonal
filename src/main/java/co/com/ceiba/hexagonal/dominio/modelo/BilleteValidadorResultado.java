package co.com.ceiba.hexagonal.dominio.modelo;

public class BilleteValidadorResultado {

  public enum CheckResult { GANO, NO_GANO, BILLETE_NO_ENVIADO }

  private final CheckResult checkResult;
  private final int monto;


  public BilleteValidadorResultado(CheckResult result) {
    checkResult = result;
    monto = 0;
  }

  public BilleteValidadorResultado(CheckResult result, int monto) {
    checkResult = result;
    this.monto = monto;
  }

  public CheckResult getResult() {
    return checkResult;
  }

  public int getPrizeAmount() {
    return monto;
  }
}
