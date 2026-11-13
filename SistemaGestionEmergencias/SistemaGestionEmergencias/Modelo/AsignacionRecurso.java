
package Modelo;

import java.util.Date;

public class AsignacionRecurso {
    private int cantidadAsignada;
    private Date fechaAsignacion;

    public AsignacionRecurso(int cantidadAsignada) {
        if (cantidadAsignada < 0) {
            throw new IllegalArgumentException(
                "La cantidad no puede ser negativa.");
        }

        this.cantidadAsignada = cantidadAsignada;
        this.fechaAsignacion = new Date();
    }

    public void actualizarCantidad(int cantidad) {
        if (cantidad >= 0) {
            cantidadAsignada = cantidad;
        }
    }

    public void finalizarAsignacion() {
    }
}
