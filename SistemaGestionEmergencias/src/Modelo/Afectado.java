package Modelo;

public class Afectado {
    private String identificacion;
    private String nombre;
    private int edad; // -1 = edad desconocida
    private EstadoAfectado estado;
    private PrioridadAfectado prioridad;

    public Afectado(String identificacion, String nombre, int edad,
                    PrioridadAfectado prioridad) {
        if (edad < -1) {
            throw new IllegalArgumentException("La edad no es válida.");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("Indica la prioridad de triaje.");
        }
        if (identificacion == null || identificacion.trim().isEmpty()) {
            this.identificacion = "No registrada";
        } else {
            this.identificacion = identificacion.trim();
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            this.nombre = "Sin identificar";
        } else {
            this.nombre = nombre.trim();
        }
        this.edad = edad;
        this.prioridad = prioridad;
        this.estado = EstadoAfectado.PENDIENTE;
    }

    public void actualizarEstado(EstadoAfectado estado) {
        this.estado = estado;
    }

    public void asignarPrioridad(PrioridadAfectado prioridad) {
        if (prioridad != null) {
            this.prioridad = prioridad;
        }
    }

    // Un valor vacío borra el dato y queda el valor por defecto, igual que en el constructor
    public void actualizarIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            this.identificacion = "No registrada";
        } else {
            this.identificacion = identificacion.trim();
        }
    }

    public void actualizarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            this.nombre = "Sin identificar";
        } else {
            this.nombre = nombre.trim();
        }
    }

    // -1 = edad desconocida
    public void actualizarEdad(int edad) {
        if (edad < -1) {
            throw new IllegalArgumentException("La edad no es válida.");
        }
        this.edad = edad;
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public EstadoAfectado getEstado() { return estado; }
    public PrioridadAfectado getPrioridad() { return prioridad; }

    public String consultarInformacion() {
        return "Identificación: " + identificacion
            + "\nNombre: " + nombre
            + "\nEdad: " + edad
            + "\nEstado: " + estado
            + "\nPrioridad: " + prioridad;
    }
}