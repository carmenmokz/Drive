/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.StringReader;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import model.Usuario;

@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
   @Inject
    private DeviceSessionHandler sessionHandler;
    
    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }
    

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
        sessionHandler.guardarUsuarios(); 
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                String basePath = new File("").getAbsolutePath();
                System.out.println("base path: " + basePath);
                System.out.println("Entro a handleMessage");
                System.out.println(jsonMessage.getString("username"));
                System.out.println(jsonMessage.getString("pass"));
                System.out.println(jsonMessage.getString("bytes"));
                Usuario usuario = new Usuario(jsonMessage.getString("username"),jsonMessage.getString("pass"),parseInt(jsonMessage.getString("bytes")));
               
                sessionHandler.addUsuario(usuario);
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                String username = jsonMessage.getString("username");
                sessionHandler.removeUsuario(username);
            }
            if ("getUsers".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getUsers();
                System.out.println("Salio");
            }
            if ("getUsers".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getUsers();
                System.out.println("Salio");
            }
            if ("getMainFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getMainFolder(jsonMessage.getString("username"));
                System.out.println("Salio");
            }
            if ("getShareFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getShareFolder(jsonMessage.getString("username"));
                System.out.println("Salio");
            }
             if ("changeFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.changeFolder(jsonMessage.getString("username"),jsonMessage.getString("folder"));
                System.out.println("Salio");
            }
            
          

            
        }
    }
}
