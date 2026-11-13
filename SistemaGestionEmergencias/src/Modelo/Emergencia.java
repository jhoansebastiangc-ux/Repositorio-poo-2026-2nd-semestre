package Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Emergencia {
    private String codigo;
    private String descripcion;
    private String zonaAfectada;
    private Date fechaOcurrencia;
    private int afectadosEstimados;
    private boolean infraestructuraColapsada;
    private boolean capacidadLocalSuperada;
    private PrioridadEmergencia prioridad;
    private EstadoEmergencia estado;
    private List<Afectado> afectados;

    public Emergencia(String codigo, String descripcion,
                      String zonaAfectada, Date fechaOcurrencia,
                      int afectadosEstimados,
                      boolean infraestructuraColapsada,
                      boolean capacidadLocalSuperada) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (zonaAfectada == null || zonaAfectada.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación es obligatoria.");
        }
        if (fechaOcurrencia == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (afectadosEstimados < 0) {
            throw new IllegalArgumentException(
                "Los afectados estimados no pueden ser negativos.");
        }
        this.codigo = codigo.trim();
        this.descripcion = descripcion.trim();
        this.zonaAfectada = zonaAfectada.trim();
        this.fechaOcurrencia = fechaOcurrencia;
        this.afectadosEstimados = afectadosEstimados;
        this.infraestructuraColapsada = infraestructuraColapsada;
        this.capacidadLocalSuperada = capacidadLocalSuperada;
        this.estado = EstadoEmergencia.REGISTRADA;
        this.afectados = new ArrayList<Afectado>();
        calcularPrioridad();
    }

    public void calcularPrioridad() {
        if (infraestructuraColapsada || afectadosEstimados > 100) {
            prioridad = PrioridadEmergencia.ALTA;
        } else if (afectadosEstimados >= 20 || capacidadLocalSuperada) {
            prioridad = PrioridadEmergencia.MEDIA;
        } else {
            prioridad = PrioridadEmergencia.BAJA;
        }
    }

    public void cambiarEstado(EstadoEmergencia nuevoEstado) {
        estado = nuevoEstado;
    }

    public void actualizarDatosPrioridad(int nuevosAfectados,
            boolean nuevaInfraestructura, boolean nuevaCapacidad) {
        if (nuevosAfectados < 0) {
            throw new IllegalArgumentException(
                "Los afectados estimados no pueden ser negativos.");
        }
        afectadosEstimados = nuevosAfectados;
        infraestructuraColapsada = nuevaInfraestructura;
        capacidadLocalSuperada = nuevaCapacidad;
        calcularPrioridad();
    }

    public void agregarAfectado(Afectado afectado) {
        if (afectado != null) {
            afectados.add(afectado);
        }
    }

    public List<Afectado> consultarAfectados() {
        return new ArrayList<Afectado>(afectados);
    }

    // RF-06: número de afectados por color de triaje
    public int contarAfectados(PrioridadAfectado color) {
        int total = 0;
        for (Afectado a : afectados) {
            if (a.getPrioridad() == color) {
                total++;
            }
        }
        return total;
    }

    public String consultarInformacion() {
        return "Código: " + codigo
            + "\nDescripción: " + descripcion
            + "\nZona afectada: " + zonaAfectada
            + "\nFecha: " + fechaOcurrencia
            + "\nAfectados estimados: " + afectadosEstimados
            + "\nInfraestructura colapsada: " + infraestructuraColapsada
            + "\nCapacidad local superada: " + capacidadLocalSuperada
            + "\nPrioridad: " + prioridad
            + "\nEstado: " + estado;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public String getZonaAfectada() { return zonaAfectada; }
    public Date getFechaOcurrencia() { return fechaOcurrencia; }
    public EstadoEmergencia getEstado() { return estado; }
    public PrioridadEmergencia getPrioridad() { return prioridad; }
    public int getAfectadosEstimados() { return afectadosEstimados; }
    public boolean isInfraestructuraColapsada() { return infraestructuraColapsada; }
    public boolean isCapacidadLocalSuperada() { return capacidadLocalSuperada; }
}