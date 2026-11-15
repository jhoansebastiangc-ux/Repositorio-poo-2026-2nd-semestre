package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class JFEmergencias extends JFrame {
    private static final Color FONDO = new Color(30, 31, 31);
    private static final Color PANEL = new Color(38, 39, 39);
    private static final Color TEXTO = new Color(238, 238, 238);

    // Panel Emergencias
    private JTextField txtCodigo = new JTextField();
    private JTextField txtFecha = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTextField txtUbicacion = new JTextField();
    private JTextField txtAfectados = new JTextField();
    private JTextField txtExtra1 = new JTextField();
    private JTextField txtExtra2 = new JTextField();
    private JLabel lblExtra1 = etiqueta("Fenómeno:");
    private JLabel lblExtra2 = etiqueta("Área km²:");
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[] {"NATURAL", "MEDICA", "HUMANITARIA"});
    private JCheckBox chkInfra = new JCheckBox("Infraestructura colapsada");
    private JCheckBox chkCapacidad = new JCheckBox("Capacidad local superada");
    private JButton btnRegistrar = new JButton("Registrar emergencia");
    private JButton btnLimpiar = new JButton("Limpiar");

    private JTextField txtFiltroCodigo = new JTextField(6);
    private JComboBox<String> cmbFiltroTipo = new JComboBox<>(new String[] {"Todos", "NATURAL", "MEDICA", "HUMANITARIA"});
    private JComboBox<String> cmbFiltroEstado = new JComboBox<>(new String[] {"Todos", "REGISTRADA", "EN_ATENCION", "CONTROLADA", "CERRADA"});
    private JComboBox<String> cmbFiltroPrioridad = new JComboBox<>(new String[] {"Todos", "BAJA", "MEDIA", "ALTA"});
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnVerTodos = new JButton("Ver todos");

    private DefaultTableModel modeloEmergencias = new DefaultTableModel(
        new String[] {"ID", "Emergencia", "Ubicación", "Tipo", "Afectados", "Prioridad", "Estado"}, 0);
    private JTable tablaEmergencias = new JTable(modeloEmergencias);
    private JComboBox<String> cmbEstadoNuevo = new JComboBox<>(new String[] {"REGISTRADA", "EN_ATENCION", "CONTROLADA", "CERRADA"});
    private JButton btnCambiarEstado = new JButton("Cambiar estado");
    private JButton btnActualizarPrioridad = new JButton("Actualizar prioridad");

    // Panel Triaje
    private JComboBox<String> cmbEmergenciaTriaje = new JComboBox<>();
    private DefaultTableModel modeloAfectados = new DefaultTableModel(
        new String[] {"Prioridad", "Identificación", "Nombre", "Edad", "Estado"}, 0);
    private JTable tablaAfectados = new JTable(modeloAfectados);
    private JTextField txtIdAfectado = new JTextField(8);
    private JTextField txtNombreAfectado = new JTextField(8);
    private JTextField txtEdadAfectado = new JTextField(4);
    private JComboBox<String> cmbPrioridadAfectado = new JComboBox<>(new String[] {"ROJO", "AMARILLO", "VERDE", "NEGRO"});
    private JComboBox<String> cmbEstadoAfectado = new JComboBox<>(new String[] {"PENDIENTE", "ATENDIDO", "EVACUADO"});
    private JButton btnRegistrarAfectado = new JButton("Registrar afectado");
    private JButton btnActualizarTriaje = new JButton("Actualizar triaje");
    private JLabel lblRojos = etiqueta("Rojos: 0");
    private JLabel lblAmarillos = etiqueta("Amarillos: 0");
    private JLabel lblVerdes = etiqueta("Verdes: 0");
    private JLabel lblNegros = etiqueta("Negros: 0");

    // Panel Equipos
    private JTextField txtCodigoEquipo = new JTextField(6);
    private JTextField txtNombreEquipo = new JTextField(8);
    private JComboBox<String> cmbTipoEquipo = new JComboBox<>(new String[] {"MEDICO", "RESCATE", "LOGISTICO"});
    private JButton btnRegistrarEquipo = new JButton("Registrar");
    private JButton btnAsignarEquipo = new JButton("Asignar a Emergencia");
    private JTextField txtBuscarEquipo = new JTextField(6);
    private JButton btnBuscarEquipo = new JButton("Buscar Equipo");
    private JComboBox<String> cmbEmergenciaParaEquipo = new JComboBox<>(); // Nuevo ComboBox para asignar
    private DefaultTableModel modeloEquipos = new DefaultTableModel(
        new String[] {"Código", "Nombre", "Tipo", "Estado"}, 0);
    private JTable tablaEquipos = new JTable(modeloEquipos);
    private JTextField txtIdIntegrante = new JTextField(7);
    private JTextField txtNombreIntegrante = new JTextField(7);
    private JTextField txtApellidoIntegrante = new JTextField(7);
    private JTextField txtTelefonoIntegrante = new JTextField(7);
    private JTextField txtFuncionIntegrante = new JTextField(7);
    private JButton btnAgregarIntegrante = new JButton("Agregar integrante");
    private JButton btnFinalizarMision = new JButton("Finalizar misión");
    
    // Panel Recursos
    private JTextField txtCodigoRecurso = new JTextField(6);
    private JTextField txtNombreRecurso = new JTextField(8);
    private JComboBox<String> cmbTipoRecurso = new JComboBox<>(new String[] {"SUMINISTRO_MEDICO", "ALIMENTO", "AGUA", "KIT_AYUDA", "ELEMENTO_RESCATE", "OTRO"});
    private JTextField txtCantidadRecurso = new JTextField(4);
    private JButton btnRegistrarRecurso = new JButton("Registrar Recurso");
    private JButton btnAsignarRecurso = new JButton("Asignar a Emergencia");
    private JComboBox<String> cmbEmergenciaParaRecurso = new JComboBox<>();
    private JTextField txtCantidadAsignar = new JTextField(4);
    private DefaultTableModel modeloRecursos = new DefaultTableModel(
        new String[] {"Código", "Nombre", "Tipo", "Disponible", "Estado"}, 0);
    private JTable tablaRecursos = new JTable(modeloRecursos);

    // Constructor dinámico por módulo
    public JFEmergencias(String moduloSeleccionado) {
        super("Sistema de Gestión de Emergencias - " + moduloSeleccionado);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(FONDO);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        switch (moduloSeleccionado) {
            case "EMERGENCIAS":
                contenido.add(crearPanelEmergencias(), BorderLayout.CENTER);
                setSize(1000, 650);
                break;
            case "TRIAJE":
                contenido.add(crearPanelTriaje(), BorderLayout.CENTER);
                setSize(900, 600);
                break;
            case "EQUIPOS":
                contenido.add(crearPanelEquipos(), BorderLayout.CENTER);
                setSize(950, 600);
                break;
            case "RECURSOS":
                contenido.add(crearPanelRecursos(), BorderLayout.CENTER);
                setSize(800, 500);
                break;
            default:
                setSize(800, 600);
                break;
        }

        add(contenido);
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelEmergencias() {
        JPanel panel = tarjeta("Registro de Emergencias");
        JPanel form = new JPanel(new GridLayout(0, 4, 6, 6));
        form.setOpaque(false);
        form.add(etiqueta("ID:")); form.add(txtCodigo);
        form.add(etiqueta("Fecha (AAAA-MM-DD):")); form.add(txtFecha);
        form.add(etiqueta("Emergencia:")); form.add(txtNombre);
        form.add(etiqueta("Ubicación:")); form.add(txtUbicacion);
        form.add(etiqueta("Tipo:")); form.add(cmbTipo);
        form.add(etiqueta("Afectados:")); form.add(txtAfectados);
        form.add(lblExtra1); form.add(txtExtra1);
        form.add(lblExtra2); form.add(txtExtra2);
        chkInfra.setOpaque(false); chkInfra.setForeground(TEXTO);
        chkCapacidad.setOpaque(false); chkCapacidad.setForeground(TEXTO);
        form.add(chkInfra); form.add(chkCapacidad);
        form.add(btnRegistrar); form.add(btnLimpiar);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        filtros.setOpaque(false);
        filtros.add(etiqueta("Código:")); filtros.add(txtFiltroCodigo);
        filtros.add(cmbFiltroTipo); filtros.add(cmbFiltroEstado);
        filtros.add(cmbFiltroPrioridad); filtros.add(btnBuscar); filtros.add(btnVerTodos);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(form, BorderLayout.NORTH);
        norte.add(filtros, BorderLayout.SOUTH);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        acciones.setOpaque(false);
        acciones.add(etiqueta("Nuevo estado:")); acciones.add(cmbEstadoNuevo);
        acciones.add(btnCambiarEstado); acciones.add(btnActualizarPrioridad);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(estilizar(tablaEmergencias), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTriaje() {
        JPanel panel = tarjeta("Triaje de Afectados");
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        selector.setOpaque(false);
        selector.add(etiqueta("Emergencia:")); selector.add(cmbEmergenciaTriaje);

        JPanel totales = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 2));
        totales.setOpaque(false);
        totales.add(lblRojos); totales.add(lblAmarillos); totales.add(lblVerdes); totales.add(lblNegros);

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        datos.setOpaque(false);
        datos.add(etiqueta("ID:")); datos.add(txtIdAfectado);
        datos.add(etiqueta("Nombre:")); datos.add(txtNombreAfectado);
        datos.add(etiqueta("Edad:")); datos.add(txtEdadAfectado);
        datos.add(cmbPrioridadAfectado); datos.add(cmbEstadoAfectado);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        botones.setOpaque(false);
        botones.add(btnRegistrarAfectado); botones.add(btnActualizarTriaje);

        JPanel sur = new JPanel(new GridLayout(3, 1));
        sur.setOpaque(false);
        sur.add(totales); sur.add(datos); sur.add(botones);

        panel.add(selector, BorderLayout.NORTH);
        panel.add(estilizar(tablaAfectados), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelEquipos() {
        JPanel panel = tarjeta("Equipos de Respuesta y Asignación");

        // Fila 1: Asignación por ComboBox y Botón Asignar
        JPanel formAsignacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        formAsignacion.setOpaque(false);
        formAsignacion.add(etiqueta("Emergencia:"));
        formAsignacion.add(cmbEmergenciaParaEquipo);
        formAsignacion.add(btnAsignarEquipo);
        formAsignacion.add(btnFinalizarMision);
        
        // Fila 2: Formulario para registrar equipo
        JPanel formRegistro = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        formRegistro.setOpaque(false);
        formRegistro.add(etiqueta("Cód:")); formRegistro.add(txtCodigoEquipo);
        formRegistro.add(etiqueta("Nom:")); formRegistro.add(txtNombreEquipo);
        formRegistro.add(etiqueta("Tipo:")); formRegistro.add(cmbTipoEquipo);
        formRegistro.add(btnRegistrarEquipo);

        // Fila 3: Barra para buscar equipos por código
        JPanel formBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        formBusqueda.setOpaque(false);
        formBusqueda.add(etiqueta("Buscar por código:"));
        formBusqueda.add(txtBuscarEquipo);
        formBusqueda.add(btnBuscarEquipo);

        // Fila 4: integrante para el equipo seleccionado en la tabla
        JPanel formIntegrante = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        formIntegrante.setOpaque(false);
        formIntegrante.add(etiqueta("Integrante  ID:")); formIntegrante.add(txtIdIntegrante);
        formIntegrante.add(etiqueta("Nom:")); formIntegrante.add(txtNombreIntegrante);
        formIntegrante.add(etiqueta("Ape:")); formIntegrante.add(txtApellidoIntegrante);
        formIntegrante.add(etiqueta("Tel:")); formIntegrante.add(txtTelefonoIntegrante);
        formIntegrante.add(etiqueta("Función:")); formIntegrante.add(txtFuncionIntegrante);
        formIntegrante.add(btnAgregarIntegrante);
        
        JPanel norte = new JPanel(new GridLayout(4, 1, 0, 4));
        norte.setOpaque(false);
        norte.add(formAsignacion);
        norte.add(formRegistro);
        norte.add(formBusqueda);
        norte.add(formIntegrante);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(estilizar(tablaEquipos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelRecursos() {
        JPanel panel = tarjeta("Gestión de Recursos y Asignación");
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        form.setOpaque(false);
        form.add(etiqueta("Cód:")); form.add(txtCodigoRecurso);
        form.add(etiqueta("Nom:")); form.add(txtNombreRecurso);
        form.add(etiqueta("Tipo:")); form.add(cmbTipoRecurso);
        form.add(etiqueta("Cant:")); form.add(txtCantidadRecurso);
        form.add(btnRegistrarRecurso);

        // Fila 2: asignación a una emergencia (ComboBox + recurso elegido en la tabla)
        JPanel formAsignacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        formAsignacion.setOpaque(false);
        formAsignacion.add(etiqueta("Emergencia:"));
        formAsignacion.add(cmbEmergenciaParaRecurso);
        formAsignacion.add(etiqueta("Cantidad a asignar:"));
        formAsignacion.add(txtCantidadAsignar);
        formAsignacion.add(btnAsignarRecurso);

        JPanel norte = new JPanel(new GridLayout(2, 1, 0, 4));
        norte.setOpaque(false);
        norte.add(form);
        norte.add(formAsignacion);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(estilizar(tablaRecursos), BorderLayout.CENTER);
        return panel;
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(TEXTO);
        return l;
    }

    private JPanel tarjeta(String titulo) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(PANEL);
        TitledBorder borde = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), titulo);
        borde.setTitleColor(TEXTO);
        p.setBorder(BorderFactory.createCompoundBorder(borde, BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        return p;
    }

    private JScrollPane estilizar(JTable tabla) {
        tabla.setDefaultEditor(Object.class, null);
        tabla.setBackground(PANEL);
        tabla.setForeground(TEXTO);
        tabla.setGridColor(Color.GRAY);
        tabla.getTableHeader().setBackground(new Color(48, 49, 49));
        tabla.getTableHeader().setForeground(TEXTO);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(PANEL);
        return scroll;
    }

    public void conectar(ActionListener oyente) {
        btnRegistrar.setActionCommand("registrar");
        btnLimpiar.setActionCommand("limpiar");
        btnBuscar.setActionCommand("buscar");
        btnVerTodos.setActionCommand("verTodos");
        btnCambiarEstado.setActionCommand("cambiarEstado");
        btnActualizarPrioridad.setActionCommand("actualizarPrioridad");
        btnRegistrarAfectado.setActionCommand("registrarAfectado");
        btnActualizarTriaje.setActionCommand("actualizarTriaje");
        btnRegistrarEquipo.setActionCommand("registrarEquipo");
        btnAsignarEquipo.setActionCommand("asignarEquipo");
        btnRegistrarRecurso.setActionCommand("registrarRecurso");
        btnAsignarRecurso.setActionCommand("asignarRecurso");
        cmbTipo.setActionCommand("tipo");
        cmbEmergenciaTriaje.setActionCommand("emergenciaTriaje");
        cmbEmergenciaParaEquipo.setActionCommand("emergenciaParaEquipo"); // Conectado
        btnBuscarEquipo.setActionCommand("buscarEquipo");
        btnAgregarIntegrante.setActionCommand("agregarIntegrante");
        btnFinalizarMision.setActionCommand("finalizarMision");

        btnRegistrar.addActionListener(oyente);
        btnLimpiar.addActionListener(oyente);
        btnBuscar.addActionListener(oyente);
        btnVerTodos.addActionListener(oyente);
        btnCambiarEstado.addActionListener(oyente);
        btnActualizarPrioridad.addActionListener(oyente);
        btnRegistrarAfectado.addActionListener(oyente);
        btnActualizarTriaje.addActionListener(oyente);
        btnRegistrarEquipo.addActionListener(oyente);
        btnAsignarEquipo.addActionListener(oyente);
        btnRegistrarRecurso.addActionListener(oyente);
        btnAsignarRecurso.addActionListener(oyente);
        cmbTipo.addActionListener(oyente);
        cmbEmergenciaTriaje.addActionListener(oyente);
        cmbEmergenciaParaEquipo.addActionListener(oyente); // Conectado
        btnBuscarEquipo.addActionListener(oyente);
        btnAgregarIntegrante.addActionListener(oyente);
        btnFinalizarMision.addActionListener(oyente);
    }

    // Getters
    public String getCodigo() { return txtCodigo.getText().trim(); }
    public String getFecha() { return txtFecha.getText().trim(); }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getUbicacion() { return txtUbicacion.getText().trim(); }
    public String getAfectados() { return txtAfectados.getText().trim(); }
    public String getExtra1() { return txtExtra1.getText().trim(); }
    public String getExtra2() { return txtExtra2.getText().trim(); }
    public String getTipo() { return (String) cmbTipo.getSelectedItem(); }
    public boolean hayInfraestructuraColapsada() { return chkInfra.isSelected(); }
    public boolean capacidadLocalSuperada() { return chkCapacidad.isSelected(); }

    public String getFiltroCodigo() { return txtFiltroCodigo.getText().trim(); }
    public String getFiltroTipo() { return (String) cmbFiltroTipo.getSelectedItem(); }
    public String getFiltroEstado() { return (String) cmbFiltroEstado.getSelectedItem(); }
    public String getFiltroPrioridad() { return (String) cmbFiltroPrioridad.getSelectedItem(); }
    public String getEstadoNuevo() { return (String) cmbEstadoNuevo.getSelectedItem(); }

    public String getCodigoSeleccionado() {
        int fila = tablaEmergencias.getSelectedRow();
        return (fila < 0) ? null : modeloEmergencias.getValueAt(fila, 0).toString();
    }

    public void limpiarTablaEmergencias() { modeloEmergencias.setRowCount(0); }
    public void agregarFilaEmergencia(Object[] fila) { modeloEmergencias.addRow(fila); }

    public void actualizarEtiquetasExtra() {
        String tipo = getTipo();
        if (tipo.equals("NATURAL")) {
            lblExtra1.setText("Fenómeno:"); lblExtra2.setText("Área km²:");
        } else if (tipo.equals("MEDICA")) {
            lblExtra1.setText("Pacientes:"); lblExtra2.setText("Ambulancias:");
        } else {
            lblExtra1.setText("Necesitan ayuda:"); lblExtra2.setText("Kits entregados:");
        }
        txtExtra1.setText(""); txtExtra2.setText("");
    }

    public void limpiarFormulario() {
        txtCodigo.setText(""); txtFecha.setText(""); txtNombre.setText("");
        txtUbicacion.setText(""); txtAfectados.setText(""); txtExtra1.setText("");
        txtExtra2.setText(""); chkInfra.setSelected(false); chkCapacidad.setSelected(false);
    }

    public void limpiarFiltros() {
        txtFiltroCodigo.setText(""); cmbFiltroTipo.setSelectedIndex(0);
        cmbFiltroEstado.setSelectedIndex(0); cmbFiltroPrioridad.setSelectedIndex(0);
    }

    public String getCodigoEmergenciaTriaje() {
        String texto = (String) cmbEmergenciaTriaje.getSelectedItem();
        return (texto == null || !texto.contains(" - ")) ? null : texto.substring(0, texto.indexOf(" - "));
    }

    public void setEmergenciasTriaje(String[] etiquetas) {
        String codigo = getCodigoEmergenciaTriaje();
        cmbEmergenciaTriaje.removeAllItems();
        for (String etiqueta : etiquetas) { cmbEmergenciaTriaje.addItem(etiqueta); }
        for (int i = 0; i < etiquetas.length; i++) {
            if (codigo != null && etiquetas[i].startsWith(codigo + " - ")) {
                cmbEmergenciaTriaje.setSelectedIndex(i);
            }
        }
    }

    // Métodos para el ComboBox de emergencias en el panel de equipos
    public String getCodigoEmergenciaParaEquipo() {
        String texto = (String) cmbEmergenciaParaEquipo.getSelectedItem();
        if (texto == null || texto.isEmpty() || !texto.contains(" - ")) {
            return null;
        }
        return texto.substring(0, texto.indexOf(" - ")).trim();
    }

    public void setEmergenciasParaEquipo(String[] etiquetas) {
        String codigoActual = getCodigoEmergenciaParaEquipo();
        cmbEmergenciaParaEquipo.removeAllItems();
        for (String etiqueta : etiquetas) {
            cmbEmergenciaParaEquipo.addItem(etiqueta);
        }
        for (int i = 0; i < etiquetas.length; i++) {
            if (codigoActual != null && etiquetas[i].startsWith(codigoActual + " - ")) {
                cmbEmergenciaParaEquipo.setSelectedIndex(i);
            }
        }
    }

    // Métodos para el ComboBox de emergencias en el panel de recursos
    public String getCodigoEmergenciaParaRecurso() {
        String texto = (String) cmbEmergenciaParaRecurso.getSelectedItem();
        if (texto == null || !texto.contains(" - ")) {
            return null;
        }
        return texto.substring(0, texto.indexOf(" - ")).trim();
    }

    public void setEmergenciasParaRecurso(String[] etiquetas) {
        String codigoActual = getCodigoEmergenciaParaRecurso();
        cmbEmergenciaParaRecurso.removeAllItems();
        for (String etiqueta : etiquetas) {
            cmbEmergenciaParaRecurso.addItem(etiqueta);
        }
        for (int i = 0; i < etiquetas.length; i++) {
            if (codigoActual != null && etiquetas[i].startsWith(codigoActual + " - ")) {
                cmbEmergenciaParaRecurso.setSelectedIndex(i);
            }
        }
    }

    public String getCantidadAsignar() { return txtCantidadAsignar.getText().trim(); }
    public void limpiarCantidadAsignar() { txtCantidadAsignar.setText(""); }

    public String getIdAfectado() { return txtIdAfectado.getText().trim(); }
    public String getNombreAfectado() { return txtNombreAfectado.getText().trim(); }
    public String getEdadAfectado() { return txtEdadAfectado.getText().trim(); }
    public String getPrioridadAfectado() { return (String) cmbPrioridadAfectado.getSelectedItem(); }
    public String getEstadoAfectado() { return (String) cmbEstadoAfectado.getSelectedItem(); }
    public int getFilaAfectadoSeleccionada() { return tablaAfectados.getSelectedRow(); }

    public void limpiarTablaAfectados() { modeloAfectados.setRowCount(0); }
    public void agregarFilaAfectado(Object[] fila) { modeloAfectados.addRow(fila); }
    public void limpiarFormularioAfectado() { txtIdAfectado.setText(""); txtNombreAfectado.setText(""); txtEdadAfectado.setText(""); }

    public void mostrarTotales(int rojos, int amarillos, int verdes, int negros) {
        lblRojos.setText("Rojos: " + rojos);
        lblAmarillos.setText("Amarillos: " + amarillos);
        lblVerdes.setText("Verdes: " + verdes);
        lblNegros.setText("Negros: " + negros);
    }

    public String getCodigoEquipo() { return txtCodigoEquipo.getText().trim(); }
    public String getNombreEquipo() { return txtNombreEquipo.getText().trim(); }
    public String getTipoEquipo() { return (String) cmbTipoEquipo.getSelectedItem(); }
    public String getCodigoEquipoSeleccionado() {
        int fila = tablaEquipos.getSelectedRow();
        return (fila >= 0) ? modeloEquipos.getValueAt(fila, 0).toString() : null;
    }
    public void limpiarTablaEquipos() { modeloEquipos.setRowCount(0); }
    public void agregarFilaEquipo(Object[] fila) { modeloEquipos.addRow(fila); }
    public void limpiarFormularioEquipo() { txtCodigoEquipo.setText(""); txtNombreEquipo.setText(""); }
    public String getCodigoBusquedaEquipo() { return txtBuscarEquipo.getText().trim(); }
    public String getIdIntegrante() { return txtIdIntegrante.getText().trim(); }
    public String getNombreIntegrante() { return txtNombreIntegrante.getText().trim(); }
    public String getApellidoIntegrante() { return txtApellidoIntegrante.getText().trim(); }
    public String getTelefonoIntegrante() { return txtTelefonoIntegrante.getText().trim(); }
    public String getFuncionIntegrante() { return txtFuncionIntegrante.getText().trim(); }
    public void limpiarFormularioIntegrante() {
        txtIdIntegrante.setText(""); txtNombreIntegrante.setText("");
        txtApellidoIntegrante.setText(""); txtTelefonoIntegrante.setText("");
        txtFuncionIntegrante.setText("");
    }
    
    public String getCodigoRecurso() { return txtCodigoRecurso.getText().trim(); }
    public String getNombreRecurso() { return txtNombreRecurso.getText().trim(); }
    public String getTipoRecurso() { return (String) cmbTipoRecurso.getSelectedItem(); }
    public String getCantidadRecurso() { return txtCantidadRecurso.getText().trim(); }
    public String getCodigoRecursoSeleccionado() {
        int fila = tablaRecursos.getSelectedRow();
        return (fila >= 0) ? modeloRecursos.getValueAt(fila, 0).toString() : null;
    }
    public void limpiarTablaRecursos() { modeloRecursos.setRowCount(0); }
    public void agregarFilaRecurso(Object[] fila) { modeloRecursos.addRow(fila); }
    public void limpiarFormularioRecurso() { txtCodigoRecurso.setText(""); txtNombreRecurso.setText(""); txtCantidadRecurso.setText(""); }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revisa los datos", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Listo", JOptionPane.INFORMATION_MESSAGE);
    }
}