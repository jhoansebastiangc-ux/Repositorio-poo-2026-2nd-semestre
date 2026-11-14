
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

    public boolean registrarIntegranteEnEquipo(String codigoEquipo, Integrante nuevoIntegrante) {
        Equipo equipo = buscarEquipo(codigoEquipo);
        if (equipo == null) {
            return false; // El equipo no existe
        }

        // 1. Validar que la persona NO esté registrada en otro equipo que esté Ocupado/Disponible/Asignado
        for (Equipo eq : listaEquipos) {
            if (eq.getEstado() != EstadoEquipo.FUERA_DE_SERVICIO) { // O la condición de activo
                for (Integrante i : eq.consultarIntegrantes()) {
                    if (i.getIdentificacion().equals(nuevoIntegrante.getIdentificacion())) {
                        return false; // La persona ya pertenece a otro equipo activo
                    }
                }
            }
        }

        // 2. Verificar si el integrante ya existe en la lista general del sistema
        Integrante integranteExistente = null;
        for (Integrante i : listaIntegrantes) {
            if (i.getIdentificacion().equals(nuevoIntegrante.getIdentificacion())) {
                integranteExistente = i;
                break;
            }
        }

        // Si no existe en el sistema, lo creamos y lo agregamos a la lista general
        if (integranteExistente == null) {
            listaIntegrantes.add(nuevoIntegrante);
            integranteExistente = nuevoIntegrante;
        }

        // 3. Agregarlo al equipo
        return equipo.agregarIntegrante(integranteExistente);
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

    public void asignarEquipoAEmergencia(String codigoEmergencia, String codigoEquipo) {
        Emergencia emergencia = buscarEmergencia(codigoEmergencia);
        Equipo equipo = buscarEquipo(codigoEquipo);

        if (emergencia != null && equipo != null) {
            // REQUISITO: Evitar que un equipo ocupado en una misión sea asignado simultáneamente a otra
            if (equipo.getEstado() == EstadoEquipo.EN_MISION || equipo.getEstado() == EstadoEquipo.ASIGNADO) {
                throw new IllegalArgumentException("El equipo ya se encuentra ocupado en una misión y no puede ser asignado.");
            }

            // Creamos la asignación registrando fecha/hora automáticamente (visto en tu clase AsignacionEquipo)
            AsignacionEquipo asignacion = new AsignacionEquipo();
            for(Integrante i : equipo.consultarIntegrantes()) {
                asignacion.agregarIntegrante(i); // Registra los integrantes participantes
            }
            
            listaAsignacionesEquipo.add(asignacion);
            
            // Cambiamos estados
            equipo.cambiarEstado(EstadoEquipo.EN_MISION);
            emergencia.cambiarEstado(EstadoEmergencia.EN_ATENCION);
        } else {
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
