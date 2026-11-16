import Control.ControladorEmergencias;
import Modelo.SistemaGestionEmergencias;
import Vista.JFEmergencias;
import Vista.JFMenuPrincipal;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // Ventanas abiertas por módulo: evita abrir dos veces el mismo panel
    private static final Map<String, JFEmergencias> ventanas = new HashMap<String, JFEmergencias>();

    public static void main(String[] args) {
        // 1. UN SOLO modelo para todo el programa: todas las ventanas comparten estos datos
        final SistemaGestionEmergencias sistema = new SistemaGestionEmergencias();

        // 2. Menú principal con los 4 botones
        JFMenuPrincipal menu = new JFMenuPrincipal();
        menu.setVisible(true);

        // 3. Cada botón abre su módulo conectado al mismo modelo
        menu.getBtnEmergencias().addActionListener(e -> abrir("EMERGENCIAS", sistema));
        menu.getBtnTriaje().addActionListener(e -> abrir("TRIAJE", sistema));
        menu.getBtnEquipos().addActionListener(e -> abrir("EQUIPOS", sistema));
        menu.getBtnRecursos().addActionListener(e -> abrir("RECURSOS", sistema));
    }

    private static void abrir(String modulo, SistemaGestionEmergencias sistema) {
        JFEmergencias existente = ventanas.get(modulo);
        if (existente != null && existente.isDisplayable()) {
            existente.setState(java.awt.Frame.NORMAL);
            existente.toFront();
            return;
        }
        JFEmergencias vista = new JFEmergencias(modulo);
        ventanas.put(modulo, vista);
        // El controlador carga en la ventana los datos que ya existen en el modelo
        new ControladorEmergencias(vista, sistema);
    }
}