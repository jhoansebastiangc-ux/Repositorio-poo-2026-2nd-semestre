package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class DashboardEmergencias extends JFrame {
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
    private JComboBox<String> cmbTipo = new JComboBox<String>(
        new String[] {"NATURAL", "MEDICA", "HUMANITARIA"});
    private JCheckBox chkInfra = new JCheckBox("Infraestructura colapsada");
    private JCheckBox chkCapacidad = new JCheckBox("Capacidad local superada");
    private JButton btnRegistrar = new JButton("Registrar emergencia");
    private JButton btnLimpiar = new JButton("Limpiar");

    private JTextField txtFiltroCodigo = new JTextField(6);
    private JComboBox<String> cmbFiltroTipo = new JComboBox<String>(
        new String[] {"Todos", "NATURAL", "MEDICA", "HUMANITARIA"});
    private JComboBox<String> cmbFiltroEstado = new JComboBox<String>(
        new String[] {"Todos", "REGISTRADA", "EN_ATENCION", "CONTROLADA", "CERRADA"});
    private JComboBox<String> cmbFiltroPrioridad = new JComboBox<String>(
        new String[] {"Todos", "BAJA", "MEDIA", "ALTA"});
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnVerTodos = new JButton("Ver todos");

    private DefaultTableModel modeloEmergencias = new DefaultTableModel(
        new String[] {"ID", "Emergencia", "Ubicación", "Tipo", "Afectados",
                      "Prioridad", "Estado"}, 0);
    private JTable tablaEmergencias = new JTable(modeloEmergencias);
    private JComboBox<String> cmbEstadoNuevo = new JComboBox<String>(
        new String[] {"REGISTRADA", "EN_ATENCION", "CONTROLADA", "CERRADA"});
    private JButton btnCambiarEstado = new JButton("Cambiar estado");
    private JButton btnActualizarPrioridad = new JButton("Actualizar prioridad");

    // Panel Triaje
    private JComboBox<String> cmbEmergenciaTriaje = new JComboBox<String>();
    private DefaultTableModel modeloAfectados = new DefaultTableModel(
        new String[] {"Prioridad", "Identificación", "Nombre", "Edad", "Estado"}, 0);
    private JTable tablaAfectados = new JTable(modeloAfectados);
    private JTextField txtIdAfectado = new JTextField(8);
    private JTextField txtNombreAfectado = new JTextField(8);
    private JTextField txtEdadAfectado = new JTextField(4);
    private JComboBox<String> cmbPrioridadAfectado = new JComboBox<String>(
        new String[] {"ROJO", "AMARILLO", "VERDE", "NEGRO"});
    private JComboBox<String> cmbEstadoAfectado = new JComboBox<String>(
        new String[] {"PENDIENTE", "ATENDIDO", "EVACUADO"});
    private JButton btnRegistrarAfectado = new JButton("Registrar afectado");
    private JButton btnActualizarTriaje = new JButton("Actualizar triaje");
    private JLabel lblRojos = etiqueta("Rojos: 0");
    private JLabel lblAmarillos = etiqueta("Amarillos: 0");
    private JLabel lblVerdes = etiqueta("Verdes: 0");
    private JLabel lblNegros = etiqueta("Negros: 0");

    public DashboardEmergencias() {
        super("Sistema de Gestión de Emergencias y Ayuda Humanitaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(FONDO);

        JPanel contenido = new JPanel(new GridLayout(2, 2, 10, 10));
        contenido.setBackground(FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(crearPanelEmergencias());
        contenido.add(crearPanelTriaje());
        contenido.add(crearPanelEquipos());
        contenido.add(crearPanelRecursos());
        add(contenido);
    }

    // ---------- Construcción de paneles ----------

    private JPanel crearPanelEmergencias() {
        JPanel panel = tarjeta("Registro de Emergencias");

        JPanel form = new JPanel(new GridLayout(0, 4, 6, 6));
        form.setOpaque(false);
        form.add(etiqueta("ID:"));            form.add(txtCodigo);
        form.add(etiqueta("Fecha (AAAA-MM-DD):")); form.add(txtFecha);
        form.add(etiqueta("Emergencia:"));    form.add(txtNombre);
        form.add(etiqueta("Ubicación:"));     form.add(txtUbicacion);
        form.add(etiqueta("Tipo:"));          form.add(cmbTipo);
        form.add(etiqueta("Afectados:"));     form.add(txtAfectados);
        form.add(lblExtra1);                  form.add(txtExtra1);
        form.add(lblExtra2);                  form.add(txtExtra2);
        chkInfra.setOpaque(false);
        chkInfra.setForeground(TEXTO);
        chkCapacidad.setOpaque(false);
        chkCapacidad.setForeground(TEXTO);
        form.add(chkInfra);
        form.add(chkCapacidad);
        form.add(btnRegistrar);
        form.add(btnLimpiar);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        filtros.setOpaque(false);
        filtros.add(etiqueta("Código:"));
        filtros.add(txtFiltroCodigo);
        filtros.add(cmbFiltroTipo);
        filtros.add(cmbFiltroEstado);
        filtros.add(cmbFiltroPrioridad);
        filtros.add(btnBuscar);
        filtros.add(btnVerTodos);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(form, BorderLayout.NORTH);
        norte.add(filtros, BorderLayout.SOUTH);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        acciones.setOpaque(false);
        acciones.add(etiqueta("Nuevo estado:"));
        acciones.add(cmbEstadoNuevo);
        acciones.add(btnCambiarEstado);
        acciones.add(btnActualizarPrioridad);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(estilizar(tablaEmergencias), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTriaje() {
        JPanel panel = tarjeta("Triaje de Afectados");

        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        selector.setOpaque(false);
        selector.add(etiqueta("Emergencia:"));
        selector.add(cmbEmergenciaTriaje);

        JPanel totales = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 2));
        totales.setOpaque(false);
        totales.add(lblRojos);
        totales.add(lblAmarillos);
        totales.add(lblVerdes);
        totales.add(lblNegros);

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        datos.setOpaque(false);
        datos.add(etiqueta("ID:"));
        datos.add(txtIdAfectado);
        datos.add(etiqueta("Nombre:"));
        datos.add(txtNombreAfectado);
        datos.add(etiqueta("Edad:"));
        datos.add(txtEdadAfectado);
        datos.add(cmbPrioridadAfectado);
        datos.add(cmbEstadoAfectado);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        botones.setOpaque(false);
        botones.add(btnRegistrarAfectado);
        botones.add(btnActualizarTriaje);

        JPanel sur = new JPanel(new GridLayout(3, 1));
        sur.setOpaque(false);
        sur.add(totales);
        sur.add(datos);
        sur.add(botones);

        panel.add(selector, BorderLayout.NORTH);
        panel.add(estilizar(tablaAfectados), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    // Panel solo visual (fuera del alcance de esta entrega)
    private JPanel crearPanelEquipos() {
        JPanel panel = tarjeta("Equipos de Respuesta (ejemplo)");
        String[] columnas = {"Código", "Nombre", "Tipo", "Integrantes", "Estado"};
        String[][] filas = {
            {"ID1", "Equipo Médico", "MEDICO", "13", "DISPONIBLE"},
            {"ID2", "Equipo Rescate", "RESCATE", "20", "EN_MISION"},
            {"ID4", "Equipo Logística", "LOGISTICO", "14", "EN_MISION"}};
        panel.add(estilizar(new JTable(filas, columnas)), BorderLayout.CENTER);
        return panel;
    }

    // Panel solo visual (fuera del alcance de esta entrega)
    private JPanel crearPanelRecursos() {
        JPanel panel = tarjeta("Gestión de Recursos (ejemplo)");
        String[] columnas = {"Código", "Nombre", "Tipo", "Cantidad", "Estado"};
        String[][] filas = {
            {"101", "Suministro Médico A", "SUMINISTRO_MEDICO", "90", "DISPONIBLE"},
            {"102", "Alimento B", "ALIMENTO", "10", "DISPONIBLE"},
            {"103", "Kit de rescate", "ELEMENTO_RESCATE", "0", "AGOTADO"}};
        panel.add(estilizar(new JTable(filas, columnas)), BorderLayout.CENTER);
        return panel;
    }

    // ---------- Ayudas de estilo ----------

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(TEXTO);
        return l;
    }

    private JPanel tarjeta(String titulo) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(PANEL);
        TitledBorder borde = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), titulo);
        borde.setTitleColor(TEXTO);
        p.setBorder(BorderFactory.createCompoundBorder(
            borde, BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        return p;
    }

    private JScrollPane estilizar(JTable tabla) {
        tabla.setDefaultEditor(Object.class, null); // celdas no editables
        tabla.setBackground(PANEL);
        tabla.setForeground(TEXTO);
        tabla.setGridColor(Color.GRAY);
        tabla.getTableHeader().setBackground(new Color(48, 49, 49));
        tabla.getTableHeader().setForeground(TEXTO);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(PANEL);
        return scroll;
    }

    // ---------- Conexión con el controlador ----------

    public void conectar(ActionListener oyente) {
        btnRegistrar.setActionCommand("registrar");
        btnLimpiar.setActionCommand("limpiar");
        btnBuscar.setActionCommand("buscar");
        btnVerTodos.setActionCommand("verTodos");
        btnCambiarEstado.setActionCommand("cambiarEstado");
        btnActualizarPrioridad.setActionCommand("actualizarPrioridad");
        btnRegistrarAfectado.setActionCommand("registrarAfectado");
        btnActualizarTriaje.setActionCommand("actualizarTriaje");
        cmbTipo.setActionCommand("tipo");
        cmbEmergenciaTriaje.setActionCommand("emergenciaTriaje");

        btnRegistrar.addActionListener(oyente);
        btnLimpiar.addActionListener(oyente);
        btnBuscar.addActionListener(oyente);
        btnVerTodos.addActionListener(oyente);
        btnCambiarEstado.addActionListener(oyente);
        btnActualizarPrioridad.addActionListener(oyente);
        btnRegistrarAfectado.addActionListener(oyente);
        btnActualizarTriaje.addActionListener(oyente);
        cmbTipo.addActionListener(oyente);
        cmbEmergenciaTriaje.addActionListener(oyente);
    }

    // ---------- Emergencias ----------

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
        if (fila < 0) {
            return null;
        }
        return modeloEmergencias.getValueAt(fila, 0).toString();
    }

    public void limpiarTablaEmergencias() { modeloEmergencias.setRowCount(0); }

    public void agregarFilaEmergencia(Object[] fila) { modeloEmergencias.addRow(fila); }

    public void actualizarEtiquetasExtra() {
        String tipo = getTipo();
        if (tipo.equals("NATURAL")) {
            lblExtra1.setText("Fenómeno:");
            lblExtra2.setText("Área km²:");
        } else if (tipo.equals("MEDICA")) {
            lblExtra1.setText("Pacientes:");
            lblExtra2.setText("Ambulancias:");
        } else {
            lblExtra1.setText("Necesitan ayuda:");
            lblExtra2.setText("Kits entregados:");
        }
        txtExtra1.setText("");
        txtExtra2.setText("");
    }

    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtFecha.setText("");
        txtNombre.setText("");
        txtUbicacion.setText("");
        txtAfectados.setText("");
        txtExtra1.setText("");
        txtExtra2.setText("");
        chkInfra.setSelected(false);
        chkCapacidad.setSelected(false);
    }

    public void limpiarFiltros() {
        txtFiltroCodigo.setText("");
        cmbFiltroTipo.setSelectedIndex(0);
        cmbFiltroEstado.setSelectedIndex(0);
        cmbFiltroPrioridad.setSelectedIndex(0);
    }

    // ---------- Triaje ----------

    // Cada etiqueta tiene la forma "CODIGO - descripción"
    public String getCodigoEmergenciaTriaje() {
        String texto = (String) cmbEmergenciaTriaje.getSelectedItem();
        if (texto == null) {
            return null;
        }
        return texto.substring(0, texto.indexOf(" - "));
    }

    public void setEmergenciasTriaje(String[] etiquetas) {
        String codigo = getCodigoEmergenciaTriaje();
        cmbEmergenciaTriaje.removeAllItems();
        for (int i = 0; i < etiquetas.length; i++) {
            cmbEmergenciaTriaje.addItem(etiquetas[i]);
        }
        for (int i = 0; i < etiquetas.length; i++) {
            if (codigo != null && etiquetas[i].startsWith(codigo + " - ")) {
                cmbEmergenciaTriaje.setSelectedIndex(i);
            }
        }
    }

    public String getIdAfectado() { return txtIdAfectado.getText().trim(); }
    public String getNombreAfectado() { return txtNombreAfectado.getText().trim(); }
    public String getEdadAfectado() { return txtEdadAfectado.getText().trim(); }
    public String getPrioridadAfectado() { return (String) cmbPrioridadAfectado.getSelectedItem(); }
    public String getEstadoAfectado() { return (String) cmbEstadoAfectado.getSelectedItem(); }
    public int getFilaAfectadoSeleccionada() { return tablaAfectados.getSelectedRow(); }

    public void limpiarTablaAfectados() { modeloAfectados.setRowCount(0); }

    public void agregarFilaAfectado(Object[] fila) { modeloAfectados.addRow(fila); }

    public void limpiarFormularioAfectado() {
        txtIdAfectado.setText("");
        txtNombreAfectado.setText("");
        txtEdadAfectado.setText("");
    }

    public void mostrarTotales(int rojos, int amarillos, int verdes, int negros) {
        lblRojos.setText("Rojos: " + rojos);
        lblAmarillos.setText("Amarillos: " + amarillos);
        lblVerdes.setText("Verdes: " + verdes);
        lblNegros.setText("Negros: " + negros);
    }

    // ---------- Mensajes ----------

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revisa los datos",
            JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Listo",
            JOptionPane.INFORMATION_MESSAGE);
    }
}