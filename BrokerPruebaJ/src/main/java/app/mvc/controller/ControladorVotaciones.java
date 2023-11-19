package app.mvc.controller;

import app.mvc.Cliente;

import app.mvc.model.Bitacora;
import app.mvc.model.GraficaModelo;

import app.mvc.view.GraficaBarrasVista;
import app.mvc.view.GraficaPastelVista;
import app.mvc.view.VotacionesVista;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;

public class ControladorVotaciones implements ActionListener{
    private VotacionesVista votacionesVista;

    private JsonObject jsonProductos;
    private Cliente cliente;

    private ArrayList<ProductoCliente> productosArray;

    public ControladorVotaciones(VotacionesVista votacionesVista, Cliente cliente) {
        Gson gson = new Gson();
        this.cliente = cliente;
        this.votacionesVista = votacionesVista;
        this.jsonProductos = gson.fromJson(cliente.contarProductos(), JsonObject.class);
        iniciarComponentesGraficos();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Bitacora bitacora = new Bitacora();
        Gson gson = new Gson();
        JsonObject productos = null;
        if(e.getSource() == votacionesVista.votarBtnProducto1){
            JsonObject respuestaServer = cliente.sendMessageVotar("primero");
            Integer votaciones = respuestaServer.get("valor2").getAsInt();
            actualizarContadorEnPantalla(8, this.votacionesVista.producto1ContadorLabel);
        }
        if(e.getSource() == votacionesVista.votarBtnProducto2){
            JsonObject respuestaServer = cliente.sendMessageVotar("segundo");
            Integer votaciones = respuestaServer.get("valor2").getAsInt();
            actualizarContadorEnPantalla(8, this.votacionesVista.producto2ContadorLabel);
        }
        if(e.getSource() == votacionesVista.votarBtnProducto3){
            JsonObject respuestaServer = cliente.sendMessageVotar("tercero");
            Integer votaciones = respuestaServer.get("valor2").getAsInt();
            actualizarContadorEnPantalla(8, this.votacionesVista.producto3ContadorLabel);
        }
        if(e.getSource() == votacionesVista.verGraficasBtn){
            productos = gson.fromJson(cliente.contarProductos(), JsonObject.class);
            System.out.println(productos);
            this.productosArray = crearArregloProductos(productos);
            GraficaModelo graficaModelo = new GraficaModelo(this.productosArray);
            GraficaBarrasVista graficaBarrasVista = new GraficaBarrasVista();
            this.controladorGraficaBarras = new BarrasControl(graficaModelo, graficaBarrasVista);
            GraficaPastelVista graficaPastelVista = new GraficaPastelVista();
            controladorGraficaBarras.refresh(productos);
            this.controladorGraficaPastel = new PastelControl(graficaModelo, graficaPastelVista);
            System.out.println("1");
            controladorGraficaPastel.refresh(productos);
        }
        
        refresh(productos);
    }
    private PastelControl controladorGraficaPastel;
    private BarrasControl controladorGraficaBarras;
    private void refresh(JsonObject jsonProductos){
        this.controladorGraficaPastel.refresh(jsonProductos);
        this.controladorGraficaBarras.refresh(jsonProductos);
    }
    
    private ArrayList crearArregloProductos(JsonObject productosActulizados){
        ArrayList<ProductoCliente> productoClientes = new ArrayList<ProductoCliente>();
        productoClientes.add(new ProductoCliente(
                productosActulizados.get("respuesta2").getAsString(),
                productosActulizados.get("valor2").getAsInt()
                ));
        productoClientes.add(new ProductoCliente(
                productosActulizados.get("respuesta3").getAsString(),
                productosActulizados.get("valor3").getAsInt()
        ));
        productoClientes.add(new ProductoCliente(
                productosActulizados.get("respuesta4").getAsString(),
                productosActulizados.get("valor4").getAsInt()
        ));
        return productoClientes;
    }
    
    private void iniciarComponentesGraficos(){
        this.votacionesVista.verGraficasBtn.addActionListener(this);
        this.votacionesVista.votarBtnProducto1.addActionListener(this);
        this.votacionesVista.votarBtnProducto2.addActionListener(this);
        this.votacionesVista.votarBtnProducto3.addActionListener(this); 
        //Obtener los nombres de los archivos sin extensión y mostrar en pantalla
        String nombreProducto1 = this.jsonProductos.get("respuesta2").getAsString();
        this.votacionesVista.producto1Label.setText(nombreProducto1);
        String nombreProducto2 = this.jsonProductos.get("respuesta3").getAsString();;
        this.votacionesVista.producto2Label.setText(nombreProducto2);
        String nombreProducto3 = this.jsonProductos.get("respuesta4").getAsString();;
        this.votacionesVista.producto3Label.setText(nombreProducto3);  
        //Contar cantidad de votos y mostrar en pantalla
        int votosPrimero = this.jsonProductos.get("valor2").getAsInt();
        int votosSegundo = this.jsonProductos.get("valor3").getAsInt();
        int votosTercero = this.jsonProductos.get("valor4").getAsInt();
        actualizarContadorEnPantalla(votosPrimero, this.votacionesVista.producto1ContadorLabel);
        actualizarContadorEnPantalla(votosSegundo, this.votacionesVista.producto2ContadorLabel);
        actualizarContadorEnPantalla(votosTercero, this.votacionesVista.producto3ContadorLabel);



    }
    
    private void actualizarContadorEnPantalla(Integer votos, JLabel contadorPorActualizar){
        contadorPorActualizar.setText(votos.toString());
    }


    public VotacionesVista getVotacionesVista() {
        return votacionesVista;
    }

    public void setVotacionesVista(VotacionesVista votacionesVista) {
        this.votacionesVista = votacionesVista;
    }

    

    
    
}
