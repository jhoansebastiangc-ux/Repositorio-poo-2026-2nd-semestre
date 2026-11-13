
package Modelo;

public class Integrante {

    // Atributos
    private String identificacion;
    private String nombre;
    private String apellido;
    private String telefono;
    private String funcion;

    // Constructor
    public Integrante(String identificacion, String nombre,
                      String apellido, String telefono, String funcion) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.funcion = funcion;
    }

    // Consultar información del integrante
    public String consultarInformacion() {
        return "Identificación: " + identificacion
            + "\nNombre: " + nombre
            + "\nApellido: " + apellido
            + "\nTeléfono: " + telefono
            + "\nFunción: " + funcion;
    }
}
