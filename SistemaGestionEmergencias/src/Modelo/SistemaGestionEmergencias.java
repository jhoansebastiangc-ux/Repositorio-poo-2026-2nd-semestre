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
        listaAsignacionesEquipo = new ArrayList<AsignacionEquipo>();
        listaAsignacionesRecurso = new ArrayList<AsignacionRecurso>();
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

    public void registrarIntegranteEnEquipo(String codigoEquipo, Integrante nuevo) {
        Equipo equipo = buscarEquipo(codigoEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("Equipo no encontrado.");
        }
        // Una persona solo puede estar en un equipo activo
        for (Equipo eq : listaEquipos) {
            if (eq.getEstado() != EstadoEquipo.FUERA_DE_SERVICIO) {
                for (Integrante i : eq.consultarIntegrantes()) {
                    if (i.getIdentificacion().equals(nuevo.getIdentificacion())) {
                        throw new IllegalArgumentException(
                            "Esa persona ya pertenece al equipo " + eq.getNombre() + ".");
                    }
                }
            }
        }
        listaIntegrantes.add(nuevo);
        equipo.agregarIntegrante(nuevo);
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
    
    public void abastecerRecurso(String codigo, int abastecer){
        if (abastecer < 0) {
            throw new IllegalArgumentException("La cantidad a abastecer no puede ser negativa.");
        }
        Recurso r = buscarRecurso(codigo);
        if (r == null) {
            throw new IllegalArgumentException("Recurso no encontrado.");
        }
        r.setCantidadDisponible(r.getCantidadDisponible() + abastecer);
        // Si estaba AGOTADO y ahora hay existencias, vuelve a DISPONIBLE
        if (r.getCantidadDisponible() > 0) {
            r.actualizarEstado(EstadoRecurso.DISPONIBLE);
        }
    }
    
    public void cambiarEstadoEmergencia(String codigo, EstadoEmergencia nuevo) {
        Emergencia emergencia = buscarEmergencia(codigo);
        if (emergencia == null) {
            throw new IllegalArgumentException("Emergencia no encontrada.");
        }
        if (emergencia.getEstado() == EstadoEmergencia.CERRADA && nuevo != EstadoEmergencia.CERRADA) {
            throw new IllegalArgumentException("La emergencia está cerrada y no puede reabrirse.");
        }
        // Al cerrar, se finalizan las misiones activas y los equipos quedan libres
        if (nuevo == EstadoEmergencia.CERRADA) {
            for (AsignacionEquipo a : listaAsignacionesEquipo) {
                if (a.getEmergencia() == emergencia && a.getEstado() == EstadoAsignacion.ACTIVA) {
                    a.finalizarAsignacion();
                    a.getEquipo().cambiarEstado(EstadoEquipo.DISPONIBLE);
                }
            }
        }
        emergencia.cambiarEstado(nuevo);
    }

    
    
    public void asignarEquipoAEmergencia(String codigoEmergencia, String codigoEquipo) {
        Emergencia emergencia = buscarEmergencia(codigoEmergencia);
        Equipo equipo = buscarEquipo(codigoEquipo);

        if (emergencia != null && equipo != null) {
            if (emergencia.getEstado() == EstadoEmergencia.CERRADA) {
                throw new IllegalArgumentException("La emergencia está cerrada y no admite nuevas asignaciones.");
            }
            // Evitar que un equipo ocupado en una misión sea asignado simultáneamente a otra
            if (equipo.getEstado() == EstadoEquipo.EN_MISION || equipo.getEstado() == EstadoEquipo.ASIGNADO) {
                throw new IllegalArgumentException("El equipo ya se encuentra ocupado en una misión y no puede ser asignado.");
            }
            
            if (equipo.getEstado() == EstadoEquipo.FUERA_DE_SERVICIO) {
                throw new IllegalArgumentException("El equipo está fuera de servicio.");
            }
            
            if (equipo.getNumIntegrantes() == 0) {
                throw new IllegalArgumentException("El equipo no tiene integrantes. Agrega al menos uno antes de asignarlo.");
            }
            
            // Creamos la asignación registrando fecha/hora automáticamente (visto en tu clase AsignacionEquipo)
            AsignacionEquipo asignacion = new AsignacionEquipo(emergencia, equipo);
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
            if (emergencia.getEstado() == EstadoEmergencia.CERRADA) {
                throw new IllegalArgumentException("La emergencia está cerrada y no admite nuevas asignaciones.");
            }
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad a asignar debe ser mayor a cero.");
            }
            if (recurso.consultarDisponibilidad() < cantidad) {
                throw new IllegalArgumentException("No hay suficiente cantidad disponible de este recurso.");
            }

            AsignacionRecurso asignacion = new AsignacionRecurso(emergencia, recurso, cantidad);

            listaAsignacionesRecurso.add(asignacion);

            int restante = recurso.consultarDisponibilidad() - cantidad;

            recurso.actualizarCantidad(restante);

            if (restante == 0) {
                recurso.actualizarEstado(EstadoRecurso.AGOTADO);
            } else {
                recurso.actualizarEstado(EstadoRecurso.DISPONIBLE);
            }
        } else {
            throw new IllegalArgumentException("Emergencia o recurso no encontrado.");
        }
    }
    
    public void finalizarMisionEquipo(String codigoEquipo) {
        Equipo equipo = buscarEquipo(codigoEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("Equipo no encontrado.");
        }
        for (AsignacionEquipo a : listaAsignacionesEquipo) {
            if (a.getEquipo() == equipo
                    && a.getEstado() == EstadoAsignacion.ACTIVA) {
                a.finalizarAsignacion();
                equipo.cambiarEstado(EstadoEquipo.DISPONIBLE);
                return;
            }
        }
        throw new IllegalArgumentException("El equipo no tiene una misión activa.");
    }
    
    public List<AsignacionEquipo> consultarAsignacionesEquipo(String codigoEmergencia) {
        List<AsignacionEquipo> resultado = new ArrayList<AsignacionEquipo>();
        for (AsignacionEquipo a : listaAsignacionesEquipo) {
            if (a.getEmergencia().getCodigo().equals(codigoEmergencia)) {
                resultado.add(a);
            }
        }
        return resultado;
    }
    
    public List<AsignacionRecurso> consultarAsignacionesRecurso(String codigoEmergencia) {
        List<AsignacionRecurso> resultado = new ArrayList<AsignacionRecurso>();
        for (AsignacionRecurso a : listaAsignacionesRecurso) {
            if (a.getEmergencia().getCodigo().equals(codigoEmergencia)) {
                resultado.add(a);
            }
        }
        return resultado;
    }
    
    public List<Equipo> consultarEquiposDisponibles() {
        List<Equipo> resultado = new ArrayList<Equipo>();
        for (Equipo eq : listaEquipos) {
            if (eq.getEstado() == EstadoEquipo.DISPONIBLE) {
                resultado.add(eq);
            }
        }
        return resultado;
    }

    public List<Recurso> consultarRecursosDisponibles() {
        List<Recurso> resultado = new ArrayList<Recurso>();
        for (Recurso r : listaRecursos) {
            if (r.getEstado() == EstadoRecurso.DISPONIBLE) {
                resultado.add(r);
            }
        }
        return resultado;
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