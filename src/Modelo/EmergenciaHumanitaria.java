
package Modelo;

import java.util.Date;

public class EmergenciaHumanitaria extends Emergencia {
    private int personasQueRequierenAyuda;
    private int kitsAyudaEntregados;

    public EmergenciaHumanitaria(String codigo, String descripcion,
            String zonaAfectada, Date fechaOcurrencia,
            int afectadosEstimados, boolean infraestructuraColapsada,
            boolean capacidadLocalSuperada, int personasQueRequierenAyuda,
            int kitsAyudaEntregados) {

        super(codigo, descripcion, zonaAfectada, fechaOcurrencia,
              afectadosEstimados, infraestructuraColapsada,
              capacidadLocalSuperada);

        this.personasQueRequierenAyuda = personasQueRequierenAyuda;
        this.kitsAyudaEntregados = kitsAyudaEntregados;
    }

    public void registrarPersonasAyuda(int cantidad) {
        if (cantidad >= 0) {
            personasQueRequierenAyuda = cantidad;
        }
    }

    public void registrarKits(int cantidad) {
        if (cantidad >= 0) {
            kitsAyudaEntregados = cantidad;
        }
    }

    public int getPersonasQueRequierenAyuda() {
        return personasQueRequierenAyuda;
    }

    public int getKitsAyudaEntregados() {
        return kitsAyudaEntregados;
    }

    @Override
    public String consultarInformacion() {
        return super.consultarInformacion()
            + "\nPersonas que requieren ayuda: "
            + personasQueRequierenAyuda
            + "\nKits de ayuda entregados: " + kitsAyudaEntregados;
    }
}
