
package Modelo;

import java.util.ArrayList;
import java.util.List;

public class SistemaGestionEmergencias {
    private List<Emergencia> listaEmergencias;
    private List<Equipo> listaEquipos;
    private List<Integrante> listaIntegrantes;
    private List<Recurso> listaRecursos;
    private List<AsignacionEquipo> listaAsignacionesEquipo;
    private List<AsignacionRecurso> listaAsignacionesRecurso;

    public SistemaGestionEmergencias() {
        listaEmergencias = new ArrayList<Emergencia>();
        listaEquipos = new ArrayList<Equipo>();
        listaIntegrantes = new ArrayList<Integrante>();
        listaRecursos = new ArrayList<Recurso>();
        listaAsignacionesEquipo =
            new ArrayList<AsignacionEquipo>();
        listaAsignacionesRecurso =
            new ArrayList<AsignacionRecurso>();
    }

    public void registrarEmergencia(Emergencia e) {
        if (e != null && buscarEmergencia(e.getCodigo()) == null) {
            listaEmergencias.add(e);
        }
    }

    public List<Emergencia> consultarEmergencias() {
        return new ArrayList<Emergencia>(listaEmergencias);
    }

    public Emergencia buscarEmergencia(String codigo) {
        for (Emergencia e : listaEmergencias) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    public void registrarEquipo(Equipo eq) {
        if (eq != null && buscarEquipo(eq) == null) {
            listaEquipos.add(eq);
        }
    }

    private Equipo buscarEquipo(Equipo equipo) {
        for (Equipo eq : listaEquipos) {
            if (eq == equipo) {
                return eq;
            }
        }
        return null;
    }

    public List<Equipo> consultarEquipos() {
        return new ArrayList<Equipo>(listaEquipos);
    }

    public Equipo buscarEquipo(String codigo) {
        for (Equipo eq : listaEquipos) {
            if (eq.getCodigo().equals(codigo)) {
                return eq;
            }
        }
        return null;
    }

    public void registrarIntegrante(Integrante integrante) {
        if (integrante != null) {
            listaIntegrantes.add(integrante);
        }
    }

    public void registrarRecurso(Recurso r) {
        if (r != null && buscarRecurso(r.getCodigo()) == null) {
            listaRecursos.add(r);
        }
    }

    public List<Recurso> consultarRecursos() {
        return new ArrayList<Recurso>(listaRecursos);
    }

    public Recurso buscarRecurso(String codigo) {
        for (Recurso r : listaRecursos) {
            if (r.getCodigo().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    public void asignarEquipoAEmergencia(
            String codigoEmergencia, String codigoEquipo) {

        Emergencia emergencia = buscarEmergencia(codigoEmergencia);
        Equipo equipo = buscarEquipo(codigoEquipo);

        if (emergencia != null && equipo != null
                && equipo.getEstado() == EstadoEquipo.DISPONIBLE) {
            if (equipo.getEstado() == EstadoEquipo.EN_MISION || equipo.getEstado() == EstadoEquipo.ASIGNADO) {
                throw new IllegalArgumentException("El equipo ya se encuentra ocupado en una misión.");
            }

            AsignacionEquipo asignacion = new AsignacionEquipo();
            listaAsignacionesEquipo.add(asignacion);
            equipo.cambiarEstado(EstadoEquipo.ASIGNADO);
            emergencia.cambiarEstado(EstadoEmergencia.EN_ATENCION);
        }else {
            throw new IllegalArgumentException("Emergencia o equipo no encontrado.");
        }
    }

    public void asignarRecursoAEmergencia(
            String codigoEmergencia, String codigoRecurso, int cantidad) {

        Emergencia emergencia = buscarEmergencia(codigoEmergencia);
        Recurso recurso = buscarRecurso(codigoRecurso);

        if (emergencia != null && recurso != null){
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad a asignar debe ser mayor a cero.");
            }
            if (recurso.consultarDisponibilidad() < cantidad) {
                throw new IllegalArgumentException("No hay suficiente cantidad disponible de este recurso.");
            }

            AsignacionRecurso asignacion =
                new AsignacionRecurso(cantidad);

            listaAsignacionesRecurso.add(asignacion);

            int restante =
                recurso.consultarDisponibilidad() - cantidad;

            recurso.actualizarCantidad(restante);

            if (restante == 0) {
                recurso.actualizarEstado(EstadoRecurso.AGOTADO);
            } else {
                recurso.actualizarEstado(EstadoRecurso.DISPONIBLE);
            }
        }
    }

    public List<Emergencia> consultarEmergenciasEnAtencion() {
        List<Emergencia> enAtencion = new ArrayList<Emergencia>();

        for (Emergencia e : listaEmergencias) {
            if (e.getEstado() == EstadoEmergencia.EN_ATENCION) {
                enAtencion.add(e);
            }
        }

        return enAtencion;
    }

    public int calcularTotalAfectadosEstimados() {
        int total = 0;

        for (Emergencia e : listaEmergencias) {
            total += e.getAfectadosEstimados();
        }

        return total;
    }
    
    public int contarEmergenciasPorEstado(EstadoEmergencia estado) {
        int contador = 0;
        for (Emergencia e : listaEmergencias) {
            if (e.getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }
    
    public int contarEmergenciasPorPrioridad(PrioridadEmergencia prioridad) {
        int contador = 0;
        for (Emergencia e : listaEmergencias) {
            if (e.getPrioridad() == prioridad) {
                contador++;
            }
        }
        return contador;
    }
}
