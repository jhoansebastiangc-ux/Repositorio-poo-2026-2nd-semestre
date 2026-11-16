
package Modelo;

import java.util.Date;

public class AsignacionRecurso {
    private int cantidadAsignada;
    private Date fechaAsignacion;
    private Emergencia emergencia;
    private Recurso recurso;

    public AsignacionRecurso(Emergencia emergencia, Recurso recurso, int cantidadAsignada) {
        if (cantidadAsignada < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.emergencia = emergencia;
        this.recurso = recurso;
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
    
    public Emergencia getEmergencia() { return emergencia; }
    public Recurso getRecurso() { return recurso; }
    public int getCantidadAsignada() { return cantidadAsignada; }
    public Date getFechaAsignacion() { return fechaAsignacion; }
    
}
