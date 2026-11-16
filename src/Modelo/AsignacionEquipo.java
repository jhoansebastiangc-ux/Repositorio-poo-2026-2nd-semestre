
package Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsignacionEquipo {
    private Date fechaHoraAsignacion;
    private EstadoAsignacion estado;
    private List<Integrante> integrantes;
    private Emergencia emergencia;
    private Equipo equipo;

    public AsignacionEquipo(Emergencia emergencia, Equipo equipo) {
        this.emergencia = emergencia;
        this.equipo = equipo;
        fechaHoraAsignacion = new Date();
        estado = EstadoAsignacion.ACTIVA;
        integrantes = new ArrayList<Integrante>();
    }

    public void finalizarAsignacion() {
        estado = EstadoAsignacion.FINALIZADA;
    }

    public void agregarIntegrante(Integrante integrante) {
        if (integrante != null) {
            integrantes.add(integrante);
        }
    }

    public List<Integrante> consultarIntegrantes() {
        return new ArrayList<Integrante>(integrantes);
    }
    
    public Emergencia getEmergencia() { return emergencia; }
    public Equipo getEquipo() { return equipo; }
    public EstadoAsignacion getEstado() { return estado; }
    public Date getFechaHoraAsignacion() { return fechaHoraAsignacion; }
    
}
