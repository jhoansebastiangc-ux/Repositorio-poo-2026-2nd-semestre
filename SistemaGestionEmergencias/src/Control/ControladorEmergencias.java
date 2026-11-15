package Control;

import Modelo.*;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControladorEmergencias implements ActionListener {
    // Controladores de todas las ventanas abiertas: cuando cambia el modelo
    // compartido se refrescan TODAS, para que la información sea la misma en todas.
    private static final List<ControladorEmergencias> ACTIVOS = new ArrayList<ControladorEmergencias>();

    private SistemaGestionEmergencias sistema;
    private JFEmergencias vista;

    public ControladorEmergencias(JFEmergencias vista, SistemaGestionEmergencias gestion) {
        this.sistema = gestion;
        this.vista = vista;
        vista.conectar(this);
        ACTIVOS.add(this);
        final ControladorEmergencias esteControlador = this;
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ACTIVOS.remove(esteControlador);
            }
        });
        // Al abrir la ventana se cargan los datos que YA existen en el modelo
        refrescarVista();
        vista.setVisible(true);
    }

    // Todos los eventos llegan aquí; los errores de validación se muestran en un solo lugar
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        try {
            if (accion.equals("registrar")) {
                registrarEmergencia();
            } else if (accion.equals("limpiar")) {
                vista.limpiarFormulario();
            } else if (accion.equals("tipo")) {
                vista.actualizarEtiquetasExtra();
            } else if (accion.equals("buscar")) {
                mostrarEmergencias();
            } else if (accion.equals("verTodos")) {
                vista.limpiarFiltros();
                mostrarEmergencias();
            } else if (accion.equals("cambiarEstado")) {
                cambiarEstado();
            } else if (accion.equals("actualizarPrioridad")) {
                actualizarPrioridad();
            } else if (accion.equals("emergenciaTriaje")) {
                mostrarAfectados();
            } else if (accion.equals("registrarAfectado")) {
                registrarAfectado();
            } else if (accion.equals("actualizarTriaje")) {
                actualizarTriaje();
            } else if (accion.equals("registrarEquipo")) {
                registrarEquipo();
            } else if (accion.equals("asignarEquipo")) {
                asignarEquipo();
            } else if (accion.equals("registrarRecurso")) {
                registrarRecurso();
            } else if (accion.equals("asignarRecurso")) {
                asignarRecurso();
            } else if (accion.equals("agregarIntegrante")) {
                agregarIntegrante();
            } else if (accion.equals("finalizarMision")) {
                finalizarMision();
            } else if (accion.equals("buscarEquipo")) {
                String codigo = vista.getCodigoBusquedaEquipo();
                if (codigo.isEmpty()) {
                    throw new IllegalArgumentException("Ingresa un código de equipo para buscar.");
                }

                Equipo equipo = sistema.buscarEquipo(codigo);
                if (equipo == null) {
                    throw new IllegalArgumentException("No se encontró ningún equipo con ese código.");
                }

    // Construimos el mensaje con el equipo y sus integrantes
            StringBuilder sb = new StringBuilder();
            sb.append("Equipo: ").append(equipo.getNombre())
            .append(" | Tipo: ").append(equipo.getTipo())
            .append(" | Estado: ").append(equipo.getEstado()).append("\n\nIntegrantes:\n");

            List<Integrante> integrantes = equipo.consultarIntegrantes();
            if (integrantes.isEmpty()) {
                sb.append("- Este equipo no tiene integrantes registrados.");
            } else {
                for (Integrante i : integrantes) {
                    sb.append("- ").append(i.consultarInformacion()).append("\n-------------------\n");
                }
            }

            vista.mostrarMensaje(sb.toString());
            }
       } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    // ---------- Emergencias (RF-01 a RF-04) ----------

    private void registrarEmergencia() {
        String codigo = vista.getCodigo();
        if (sistema.buscarEmergencia(codigo) != null) {
            throw new IllegalArgumentException("Ya existe una emergencia con ese ID.");
        }
        Date fecha = leerFecha(vista.getFecha());
        int afectados = leerEntero(vista.getAfectados(), "Los afectados estimados");
        boolean infra = vista.hayInfraestructuraColapsada();
        boolean capacidad = vista.capacidadLocalSuperada();
        String tipo = vista.getTipo();
        Emergencia emergencia;

        if (tipo.equals("NATURAL")) {
            if (vista.getExtra1().isEmpty()) {
                throw new IllegalArgumentException("Escribe el fenómeno natural.");
            }
            emergencia = new EmergenciaNatural(codigo, vista.getNombre(),
                vista.getUbicacion(), fecha, afectados, infra, capacidad,
                vista.getExtra1(), leerDecimal(vista.getExtra2(), "El área"));
        } else if (tipo.equals("MEDICA")) {
            emergencia = new EmergenciaMedica(codigo, vista.getNombre(),
                vista.getUbicacion(), fecha, afectados, infra, capacidad,
                leerEntero(vista.getExtra1(), "Los pacientes"),
                leerEntero(vista.getExtra2(), "Las ambulancias"));
        } else {
            emergencia = new EmergenciaHumanitaria(codigo, vista.getNombre(),
                vista.getUbicacion(), fecha, afectados, infra, capacidad,
                leerEntero(vista.getExtra1(), "Las personas que necesitan ayuda"),
                leerEntero(vista.getExtra2(), "Los kits entregados"));
        }

        sistema.registrarEmergencia(emergencia);
        vista.limpiarFormulario();
        actualizarTodo();
        vista.mostrarMensaje("Emergencia registrada.");
    }

    private void mostrarEmergencias() {
        vista.limpiarTablaEmergencias();
        String codigo = vista.getFiltroCodigo().toLowerCase();
        String tipo = vista.getFiltroTipo();
        String estado = vista.getFiltroEstado();
        String prioridad = vista.getFiltroPrioridad();

        for (Emergencia e : sistema.consultarEmergencias()) {
            boolean coincide = e.getCodigo().toLowerCase().contains(codigo)
                && (tipo.equals("Todos") || tipo.equals(tipoDe(e)))
                && (estado.equals("Todos") || estado.equals(e.getEstado().toString()))
                && (prioridad.equals("Todos") || prioridad.equals(e.getPrioridad().toString()));
            if (coincide) {
                vista.agregarFilaEmergencia(new Object[] {e.getCodigo(),
                    e.getDescripcion(), e.getZonaAfectada(), tipoDe(e),
                    e.getAfectadosEstimados(), e.getPrioridad(), e.getEstado()});
            }
        }
    }

    private void cambiarEstado() {
        Emergencia emergencia = emergenciaSeleccionada();
        emergencia.cambiarEstado(EstadoEmergencia.valueOf(vista.getEstadoNuevo()));
        actualizarTodo();
    }

    // Usa los datos del formulario: afectados y casillas de infraestructura/capacidad
    private void actualizarPrioridad() {
        Emergencia emergencia = emergenciaSeleccionada();
        int afectados = leerEntero(vista.getAfectados(), "Los afectados estimados");
        emergencia.actualizarDatosPrioridad(afectados,
            vista.hayInfraestructuraColapsada(), vista.capacidadLocalSuperada());
        actualizarTodo();
        vista.mostrarMensaje("Prioridad actualizada: " + emergencia.getPrioridad());
    }

    // ---------- Afectados (RF-05 y RF-06) ----------

    private void registrarAfectado() {
        Emergencia emergencia = emergenciaDeTriaje();
        int edad = -1; // -1 = edad desconocida
        if (!vista.getEdadAfectado().isEmpty()) {
            edad = leerEntero(vista.getEdadAfectado(), "La edad");
        }
        Afectado afectado = new Afectado(vista.getIdAfectado(),
            vista.getNombreAfectado(), edad,
            PrioridadAfectado.valueOf(vista.getPrioridadAfectado()));
        emergencia.agregarAfectado(afectado);
        vista.limpiarFormularioAfectado();
        actualizarTodo();
    }

    private void actualizarTriaje() {
        Emergencia emergencia = emergenciaDeTriaje();
        int fila = vista.getFilaAfectadoSeleccionada();
        if (fila < 0) {
            throw new IllegalArgumentException("Selecciona un afectado de la tabla.");
        }
        Afectado afectado = emergencia.consultarAfectados().get(fila);
        afectado.asignarPrioridad(PrioridadAfectado.valueOf(vista.getPrioridadAfectado()));
        afectado.actualizarEstado(EstadoAfectado.valueOf(vista.getEstadoAfectado()));
        actualizarTodo();
    }

    private void mostrarAfectados() {
        vista.limpiarTablaAfectados();
        String codigo = vista.getCodigoEmergenciaTriaje();
        Emergencia emergencia = null;
        if (codigo != null) {
            emergencia = sistema.buscarEmergencia(codigo);
        }
        if (emergencia == null) {
            vista.mostrarTotales(0, 0, 0, 0);
            return;
        }
        for (Afectado a : emergencia.consultarAfectados()) {
            Object edad = "Desconocida";
            if (a.getEdad() >= 0) {
                edad = a.getEdad();
            }
            vista.agregarFilaAfectado(new Object[] {a.getPrioridad(),
                a.getIdentificacion(), a.getNombre(), edad, a.getEstado()});
        }
        vista.mostrarTotales(
            emergencia.contarAfectados(PrioridadAfectado.ROJO),
            emergencia.contarAfectados(PrioridadAfectado.AMARILLO),
            emergencia.contarAfectados(PrioridadAfectado.VERDE),
            emergencia.contarAfectados(PrioridadAfectado.NEGRO));
    }
    
    private void registrarEquipo() {
        String codigo = vista.getCodigoEquipo();
        String nombre = vista.getNombreEquipo();
        TipoEquipo tipo = TipoEquipo.valueOf(vista.getTipoEquipo());
        
        if (codigo.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Complete los datos del equipo.");
        }
        
        if (sistema.buscarEquipo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un equipo con ese código.");
        }

        Equipo equipo = new Equipo(codigo, nombre, tipo);
        sistema.registrarEquipo(equipo);
        vista.limpiarFormularioEquipo();
        actualizarTodo();
        vista.mostrarMensaje("Equipo registrado exitosamente.");
    }

    private void asignarEquipo() {
        // Obtenemos la emergencia del ComboBox de la vista de equipos
        String codEmergencia = vista.getCodigoEmergenciaParaEquipo(); 
        
        // Obtenemos el equipo seleccionado haciendo clic en la tabla
        String codEquipo = vista.getCodigoEquipoSeleccionado();
        
        if (codEmergencia == null || codEmergencia.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una emergencia de la lista.");
        }
        
        if (codEquipo == null || codEquipo.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un equipo de la tabla para asignar.");
        }
        
        // Llamamos al método del modelo que ya maneja la lógica y las validaciones
        sistema.asignarEquipoAEmergencia(codEmergencia, codEquipo);
        
        actualizarTodo();
        vista.mostrarMensaje("✔ Equipo asignado a la emergencia correctamente.");
    }

    private void agregarIntegrante() {
        String codEquipo = vista.getCodigoEquipoSeleccionado();
        if (codEquipo == null) {
            throw new IllegalArgumentException("Selecciona un equipo de la tabla.");
        }
        if (vista.getIdIntegrante().isEmpty() || vista.getNombreIntegrante().isEmpty()
                || vista.getApellidoIntegrante().isEmpty()) {
            throw new IllegalArgumentException("ID, nombre y apellido del integrante son obligatorios.");
        }
        Integrante integrante = new Integrante(vista.getIdIntegrante(),
            vista.getNombreIntegrante(), vista.getApellidoIntegrante(),
            vista.getTelefonoIntegrante(), vista.getFuncionIntegrante());
        sistema.registrarIntegranteEnEquipo(codEquipo, integrante);
        vista.limpiarFormularioIntegrante();
        actualizarTodo();
        vista.mostrarMensaje("Integrante agregado al equipo.");
    }

    private void finalizarMision() {
        String codEquipo = vista.getCodigoEquipoSeleccionado();
        if (codEquipo == null) {
            throw new IllegalArgumentException("Selecciona un equipo de la tabla.");
        }
        sistema.finalizarMisionEquipo(codEquipo);
        actualizarTodo();
        vista.mostrarMensaje("Misión finalizada. El equipo está disponible.");
    }
    
    private void registrarRecurso() {
        String codigo = vista.getCodigoRecurso();
        String nombre = vista.getNombreRecurso();
        TipoRecurso tipo = TipoRecurso.valueOf(vista.getTipoRecurso());
        int cantidad = leerEntero(vista.getCantidadRecurso(), "La cantidad de recurso");

        if (codigo.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Complete el código y el nombre del recurso.");
        }
        if (sistema.buscarRecurso(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un recurso con ese código.");
        }

        Recurso recurso = new Recurso(codigo, nombre, tipo, cantidad);
        sistema.registrarRecurso(recurso);
        vista.limpiarFormularioRecurso();
        actualizarTodo();
        vista.mostrarMensaje("Recurso registrado exitosamente.");
    }

    private void asignarRecurso() {
        // La emergencia sale del ComboBox del panel de recursos y el recurso de la tabla
        String codEmergencia = vista.getCodigoEmergenciaParaRecurso();
        String codRecurso = vista.getCodigoRecursoSeleccionado();

        if (codEmergencia == null) {
            throw new IllegalArgumentException("Debe seleccionar una emergencia de la lista.");
        }
        if (codRecurso == null) {
            throw new IllegalArgumentException("Debe seleccionar un recurso de la tabla.");
        }
        int cantidad = leerEntero(vista.getCantidadAsignar(), "La cantidad a asignar");

        sistema.asignarRecursoAEmergencia(codEmergencia, codRecurso, cantidad);
        vista.limpiarCantidadAsignar();
        actualizarTodo();
        vista.mostrarMensaje("Recurso asignado correctamente.");
    }

    private void actualizarTablasEquiposRecursos() {
        vista.limpiarTablaEquipos();
        for (Equipo eq : sistema.consultarEquipos()) {
            vista.agregarFilaEquipo(new Object[]{eq.getCodigo(), eq.getNombre(), eq.getTipo(), eq.getEstado()});
        }

        vista.limpiarTablaRecursos();
        for (Recurso r : sistema.consultarRecursos()) {
            vista.agregarFilaRecurso(new Object[]{r.getCodigo(), r.getNombre(), r.getTipo(), r.consultarDisponibilidad(), r.getEstado()});
        }
    }

    // ---------- Ayudas ----------

    // Refresca TODAS las ventanas abiertas con los datos actuales del modelo compartido
    private void actualizarTodo() {
        for (ControladorEmergencias c : new ArrayList<ControladorEmergencias>(ACTIVOS)) {
            c.refrescarVista();
        }
    }

    // Carga en ESTA ventana todo lo que hay en el modelo: ComboBox y tablas
    private void refrescarVista() {
        List<Emergencia> lista = sistema.consultarEmergencias();
        String[] etiquetas = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            etiquetas[i] = lista.get(i).getCodigo() + " - "
                + lista.get(i).getDescripcion();
        }
        vista.setEmergenciasTriaje(etiquetas);
        vista.setEmergenciasParaEquipo(etiquetas);
        vista.setEmergenciasParaRecurso(etiquetas);
        mostrarEmergencias();
        mostrarAfectados();
        actualizarTablasEquiposRecursos();
    }

    private Emergencia emergenciaSeleccionada() {
        String codigo = vista.getCodigoSeleccionado();
        if (codigo == null) {
            throw new IllegalArgumentException("Selecciona una emergencia de la tabla.");
        }
        return sistema.buscarEmergencia(codigo);
    }

    private Emergencia emergenciaDeTriaje() {
        String codigo = vista.getCodigoEmergenciaTriaje();
        if (codigo == null) {
            throw new IllegalArgumentException("Registra una emergencia primero.");
        }
        return sistema.buscarEmergencia(codigo);
    }

    private String tipoDe(Emergencia e) {
        if (e instanceof EmergenciaNatural) {
            return "NATURAL";
        } else if (e instanceof EmergenciaMedica) {
            return "MEDICA";
        }
        return "HUMANITARIA";
    }

    private Date leerFecha(String texto) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setLenient(false);
        try {
            return formato.parse(texto);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Escribe la fecha como AAAA-MM-DD.");
        }
    }

    private int leerEntero(String texto, String campo) {
        try {
            int valor = Integer.parseInt(texto);
            if (valor < 0) {
                throw new IllegalArgumentException(campo + " no puede ser negativo.");
            }
            return valor;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(campo + " debe ser un número entero.");
        }
    }

    private double leerDecimal(String texto, String campo) {
        try {
            double valor = Double.parseDouble(texto.replace(',', '.'));
            if (valor < 0) {
                throw new IllegalArgumentException(campo + " no puede ser negativa.");
            }
            return valor;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(campo + " debe ser un número.");
        }
    }

}