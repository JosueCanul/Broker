package app.server.controllerServer;

import app.mvc.RegistrosRuta;
import app.server.model.AdministradorProductoServer;
import app.server.model.Producto;
import app.server.model.dao.VotosDAO;
import app.server.resources.RegistrosRutaServer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControllerServer {
    private File filePrimerProducto = new File(RegistrosRutaServer.primerRegistro);
    private File fileSegundoProducto = new File(RegistrosRutaServer.segudoRegistro);
    private File fileTercerProducto = new File(RegistrosRutaServer.tercerRegistro);
    
    private AdministradorProductoServer administradorProductoServer = new AdministradorProductoServer(
            new File(RegistrosRutaServer.primerRegistro),
            new File(RegistrosRutaServer.segudoRegistro),
            new File(RegistrosRutaServer.tercerRegistro)
    );

    private VotosDAO votosDAO = new VotosDAO();

    public Producto[] countAll(){
        Producto[] productos = new Producto[3];

        productos[0] = administradorProductoServer.getProducto1();
        productos[1] = administradorProductoServer.getProducto2();
        productos[2] = administradorProductoServer.getProducto3();
        return productos;
    }
    public Integer contarVotosProducto(String producto){
        switch(producto){
            case "primero":
                return administradorProductoServer.getProducto1().contarVotos();
            case "segundo":
                return administradorProductoServer.getProducto2().contarVotos();
            case "tercero":
                return administradorProductoServer.getProducto3().contarVotos();
            default:
                System.out.println(("El producto no se encuentra"));
                return 0;
        }

    }

    public void agregarVotoProducto(String producto){
        switch(producto){
            case "primero":
                this.votosDAO.escribirVoto(this.filePrimerProducto);
                break;
            case "segundo":
                this.votosDAO.escribirVoto(this.fileSegundoProducto);
                break;
            case "tercero":
                this.votosDAO.escribirVoto(this.fileTercerProducto);
                break;
            default:
                System.out.println(("El producto no se encuentra"));
        }

    }

    public void findAllVotos(){
        
    }
}
