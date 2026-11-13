
package Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsignacionEquipo {
    private Date fechaHoraAsignacion;
    private EstadoAsignacion estado;
    private List<Integrante> integrantes;

    public AsignacionEquipo() {
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
}
