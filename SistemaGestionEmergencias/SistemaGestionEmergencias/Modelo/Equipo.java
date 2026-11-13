
package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private String codigo;
    private String nombre;
    private TipoEquipo tipo;
    private int numIntegrantes;
    private EstadoEquipo estado;
    private List<Integrante> integrantes;

    public Equipo(String codigo, String nombre, TipoEquipo tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.numIntegrantes = 0;
        this.estado = EstadoEquipo.DISPONIBLE;
        this.integrantes = new ArrayList<Integrante>();
    }

    public void cambiarEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public void agregarIntegrante(Integrante integrante) {
        if (integrante != null) {
            integrantes.add(integrante);
            numIntegrantes = integrantes.size();
        }
    }

    public List<Integrante> consultarIntegrantes() {
        return new ArrayList<Integrante>(integrantes);
    }
    
    public String getCodigo() {
    return codigo;
    }

    public EstadoEquipo getEstado() {
    return estado;
    }
}
