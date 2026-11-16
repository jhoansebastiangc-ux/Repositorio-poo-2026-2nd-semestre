package Vista;

import java.awt.*;
import javax.swing.*;

public class JFMenuPrincipal extends JFrame {
    private static final Color FONDO = new Color(30, 31, 31);
    private static final Color PANEL = new Color(38, 39, 39);
    private static final Color TEXTO = new Color(238, 238, 238);
    private static final Color ACENTO = new Color(70, 130, 180);

    private JButton btnEmergencias = crearBotonEstilizado(" Registro de Emergencias");
    private JButton btnTriaje = crearBotonEstilizado(" Triaje de Afectados");
    private JButton btnEquipos = crearBotonEstilizado(" Equipos Respuesta y Asignación");
    private JButton btnRecursos = crearBotonEstilizado(" Gestión de Recursos y Asignación");

    public JFMenuPrincipal() {
        super("Sistema de Gestión de Emergencias - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(FONDO);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBackground(PANEL);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("PANEL DE CONTROL PRINCIPAL", JLabel.CENTER);
        lblTitulo.setForeground(TEXTO);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 12));
        panelBotones.setOpaque(false);
        panelBotones.add(btnEmergencias);
        panelBotones.add(btnTriaje);
        panelBotones.add(btnEquipos);
        panelBotones.add(btnRecursos);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return boton;
    }

    public JButton getBtnEmergencias() { return btnEmergencias; }
    public JButton getBtnTriaje() { return btnTriaje; }
    public JButton getBtnEquipos() { return btnEquipos; }
    public JButton getBtnRecursos() { return btnRecursos; }
}