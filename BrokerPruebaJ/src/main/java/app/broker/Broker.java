package app.broker;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Broker {
    /*
    *
    * El siguiente código tiene la finalidad de registrar servidores, cuando se
    * invoque al servidor.
    *
    *  */
    private PrintWriter out;
    private BufferedReader in;
    private ServerSocket serverSocket;

    private final int portBroker;
    private List<JsonObject> servers;

    private JsonObject server;
    public Broker(int portBroker){
        this.portBroker = portBroker;
        this.servers = new ArrayList<>();
    }

    public void upBrokerForPetitions() {
        try {
            this.serverSocket = new ServerSocket(this.portBroker);

            System.out.println("se a levantado el broker para peticiones del cliente");

            while (true) {
                if(!this.servers.isEmpty()) this.server = this.servers.get(0);
                Socket petitionSocket = serverSocket.accept();

                ThreadEchoHandlerBroker threadEchoHandlerServer =
                        new ThreadEchoHandlerBroker(petitionSocket, servers, server);

                Thread threadRequestBroker = new Thread(threadEchoHandlerServer);

                threadRequestBroker.start();
                try {
                    threadRequestBroker.join();
                } catch (InterruptedException e) {
                    
                    e.printStackTrace();
                }
            }

    } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                    this.serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Broker broker = new Broker(7777);
        broker.upBrokerForPetitions();
    }
}
