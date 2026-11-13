import Vista.*;
import Modelo.*;
import Control.*;
public class Main {
    public static void main(String[] args) {
        SistemaGestionEmergencias modeloGestor = new SistemaGestionEmergencias();
        JFEmergencias vistaGestion = new JFEmergencias();
        ControladorEmergencias control = new ControladorEmergencias(vistaGestion,modeloGestor);
        vistaGestion.setVisible(true);
    }
}
