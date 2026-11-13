
package Modelo;

import java.util.Date;

public class EmergenciaNatural extends Emergencia {
    private String fenomeno;
    private double areaAfectadaKm2;

    public EmergenciaNatural(String codigo, String descripcion,
            String zonaAfectada, Date fechaOcurrencia,
            int afectadosEstimados, boolean infraestructuraColapsada,
            boolean capacidadLocalSuperada, String fenomeno,
            double areaAfectadaKm2) {

        super(codigo, descripcion, zonaAfectada, fechaOcurrencia,
              afectadosEstimados, infraestructuraColapsada,
              capacidadLocalSuperada);

        this.fenomeno = fenomeno;
        this.areaAfectadaKm2 = areaAfectadaKm2;
    }

    public void registrarFenomeno(String fenomeno) {
        this.fenomeno = fenomeno;
    }

    public String getFenomeno() {
        return fenomeno;
    }

    public double getAreaAfectadaKm2() {
        return areaAfectadaKm2;
    }

    @Override
    public String consultarInformacion() {
        return super.consultarInformacion()
            + "\nFenómeno: " + fenomeno
            + "\nÁrea afectada (km2): " + areaAfectadaKm2;
    }
}
