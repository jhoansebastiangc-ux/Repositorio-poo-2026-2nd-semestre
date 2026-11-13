
package Modelo;

import java.util.Date;

public class EmergenciaMedica extends Emergencia {
    private int cantidadPacientes;
    private int ambulanciasUtilizadas;

    public EmergenciaMedica(String codigo, String descripcion,
            String zonaAfectada, Date fechaOcurrencia,
            int afectadosEstimados, boolean infraestructuraColapsada,
            boolean capacidadLocalSuperada, int cantidadPacientes,
            int ambulanciasUtilizadas) {

        super(codigo, descripcion, zonaAfectada, fechaOcurrencia,
              afectadosEstimados, infraestructuraColapsada,
              capacidadLocalSuperada);

        this.cantidadPacientes = cantidadPacientes;
        this.ambulanciasUtilizadas = ambulanciasUtilizadas;
    }

    public void registrarPacientes(int cantidad) {
        if (cantidad >= 0) {
            cantidadPacientes = cantidad;
        }
    }

    public void registrarAmbulancias(int cantidad) {
        if (cantidad >= 0) {
            ambulanciasUtilizadas = cantidad;
        }
    }

    public int getCantidadPacientes() {
        return cantidadPacientes;
    }

    public int getAmbulanciasUtilizadas() {
        return ambulanciasUtilizadas;
    }

    @Override
    public String consultarInformacion() {
        return super.consultarInformacion()
            + "\nCantidad de pacientes: " + cantidadPacientes
            + "\nAmbulancias utilizadas: " + ambulanciasUtilizadas;
    }
}
