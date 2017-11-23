/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.dfd
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

            else if ("remove".equals(jsonMessage.getString("action"))) {
                String username = jsonMessage.getString("username");
                sessionHandler.removeUsuario(username);
            }
            else if ("getUsers".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getUsers();
                System.out.println("Salio");
            }
            else if ("getUsers".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getUsers();
                System.out.println("Salio");
            }
            else if ("getMainFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getMainFolder(jsonMessage.getString("username"),session);
                System.out.println("Salio");
            }
            else if ("getShareFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.getShareFolder(jsonMessage.getString("username"),session);
                System.out.println("Salio");
            }
            else if ("changeFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.changeFolder(jsonMessage.getString("username"),jsonMessage.getString("folder"),session);
                System.out.println("Salio");
            }
            else if ("addFile".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.addFile(jsonMessage.getString("username"),jsonMessage.getString("dir"),jsonMessage.getString("name"),jsonMessage.getString("ext"),jsonMessage.getString("cont"),session);
                System.out.println("Salio");
            }
            else if ("addFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("Entro");
                sessionHandler.addFolder(jsonMessage.getString("username"),jsonMessage.getString("dir"),jsonMessage.getString("name"),session);
                System.out.println("Salio");
            }
            else if("shareFile".equals(jsonMessage.getString("action"))) {
                System.out.println("Share");
                String username = jsonMessage.getString("username"); 
                String nameFile = jsonMessage.getString("file"); 
                String currentPath = jsonMessage.getString("currentPath"); 
                String toUser = jsonMessage.getString("toUser"); 
                 sessionHandler.shareFile(username, currentPath+"/"+nameFile, currentPath, toUser, "", session);
            }
            else if("shareDir".equals(jsonMessage.getString("action"))) {
                System.out.println("Share");
                String username = jsonMessage.getString("username"); 
                String nameFile = jsonMessage.getString("path"); 
                String toUser = jsonMessage.getString("toUser"); 
           
                sessionHandler.shareDirectory(username, nameFile, toUser,"",session);
            }
            else if("copy".equals(jsonMessage.getString("action"))) {
                System.out.println("LLegue aqui");
                sessionHandler.copy(jsonMessage.getString("username"),jsonMessage.getInt("type"),jsonMessage.getString("origin"),jsonMessage.getString("destiny"),session);
            }
             else if("deleteFile".equals(jsonMessage.getString("action"))) {
                System.out.println("LLegue aqui");
                sessionHandler.deleteFile(jsonMessage.getString("username"), jsonMessage.getString("dir"),jsonMessage.getString("file"), jsonMessage.getString("ext"),session);
            }
            else if("deleteFolder".equals(jsonMessage.getString("action"))) {
                System.out.println("LLegue aqui");
                sessionHandler.deleteFolder(jsonMessage.getString("username"), jsonMessage.getString("dir"),jsonMessage.getString("file"),session);
            }
             else if("move".equals(jsonMessage.getString("action"))) {
                System.out.println("LLegue aqui");
                sessionHandler.move(jsonMessage.getString("username"),jsonMessage.getInt("type"),jsonMessage.getString("dir")+"/"+jsonMessage.getString("file"),jsonMessage.getString("destiny"),session);
            }
            else if("edit".equals(jsonMessage.getString("action"))) {
                System.out.println("LLegue aqui");
                sessionHandler.edit(jsonMessage.getString("username"), jsonMessage.getString("dir"), jsonMessage.getString("oldfile"), jsonMessage.getString("file"), jsonMessage.getString("ext"), jsonMessage.getString("content"), session);
            }
            else if("view".equals(jsonMessage.getString("action"))) {
                System.out.println("LLegue aqui");
                sessionHandler.view(jsonMessage.getString("username"), jsonMessage.getString("dir"), jsonMessage.getString("file"), session, jsonMessage.getInt("type"));
            }

        }
    }
    
    @Override
    public String toString() {
        return "DeviceWebSocketServer{" + "sessionHandler=" + sessionHandler + '}';
    }
    
}
