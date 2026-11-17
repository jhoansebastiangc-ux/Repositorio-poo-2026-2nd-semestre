
package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private String codigo;
    private String nombre;
    private TipoEquipo tipo;
    private EstadoEquipo estado;
    private List<Integrante> integrantes;

    public Equipo(String codigo, String nombre, TipoEquipo tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = EstadoEquipo.DISPONIBLE;
        this.integrantes = new ArrayList<Integrante>();
    }

    public void cambiarEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public boolean agregarIntegrante(Integrante integrante) {
        for (Integrante i : integrantes) {
            if (i.getIdentificacion().equals(integrante.getIdentificacion())) {
                return false; // Ya está en este equipo
            }
        }
        integrantes.add(integrante);
        return true;
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

    public String getNombre() {
        return nombre;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }
    
    public int getNumIntegrantes() {
        return integrantes.size();
    }
//    
}
