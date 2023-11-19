
package app.mvc;

import app.mvc.controller.ControladorVotaciones;
import app.mvc.view.VotacionesVista;

public class MVC {

    public static void main(String[] args) {
        Cliente cliente = new Cliente("192.168.10.101", 7777);
        VotacionesVista votacionesVista = new VotacionesVista();
        ControladorVotaciones controladorVotaciones = new ControladorVotaciones(votacionesVista, cliente);
        votacionesVista.setVisible(true);
    }
}
