
package Modelo;

public class Recurso {

    // Atributos
    private String codigo;
    private String nombre;
    private TipoRecurso tipo;
    private int cantidadDisponible;
    private EstadoRecurso estado;

    // Constructor
    public Recurso(String codigo, String nombre,
                   TipoRecurso tipo, int cantidadDisponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidadDisponible = cantidadDisponible;

        if (cantidadDisponible > 0) {
            this.estado = EstadoRecurso.DISPONIBLE;
        } else {
            this.estado = EstadoRecurso.AGOTADO;
        }
    }

    // Actualizar la cantidad disponible
    public void actualizarCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidadDisponible = cantidad;
        }
    }

    // Actualizar el estado del recurso
    public void actualizarEstado(EstadoRecurso estado) {
        this.estado = estado;
    }

    // Consultar la cantidad disponible
    public int consultarDisponibilidad() {
        return cantidadDisponible;
    }
    
    public String getCodigo() {
    return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public EstadoRecurso getEstado() {
        return estado;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    
    
}
