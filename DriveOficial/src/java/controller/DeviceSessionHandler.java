/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import model.Usuario;

@ApplicationScoped
public class DeviceSessionHandler {
    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private Set<Usuario> usuarios = new HashSet<>();

    public DeviceSessionHandler() {
        cargarUsuarios(); 
    }
    
    
    
    
      public void addSession(Session session) {
        sessions.add(session);
        for (Usuario usuario : usuarios) {
            JsonObject addMessage = createAddMessage(usuario);
            sendToSession(session, addMessage);
        }

    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
     public List<Usuario> getDevices() {
        return new ArrayList<>(usuarios);
    }

    public void addUsuario(Usuario usuario) {
       
        
        cargarUsuarios(); 
        usuarios.add(usuario);
        guardarUsuarios();
        cargarUsuarios(); 
        System.out.println(usuarios.toString());
        JsonObject addMessage = createAddMessage(usuario);
        sendToAllConnectedSessions(addMessage);
         
    }

    public void removeUsuario(String username) {
        Usuario usuario = getUsuarioByUsername(username);
        if (usuario != null) {
            usuarios.remove(usuario);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("username", username)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }



   private Usuario getUsuarioByUsername(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equalsIgnoreCase(username)) {
                return usuario;
            }
        }
        return null;
    }

    private JsonObject createAddMessage(Usuario usuario) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("username", usuario.getUsername())
                .add("password", usuario.getPassword())
             
                .build();
        return addMessage;
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private JsonObject createAddMessageAllUsers() {
        JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder array = Json.createArrayBuilder();
        for(Usuario u: usuarios){
            array.add(Json.createObjectBuilder()
                    .add("username", u.getUsername())
                    .add("pass", u.getPassword())
                    .build());
        }

        JsonArray arr = array.build();
    
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "getUsers")
                .add("users", arr)
                .build();
        return addMessage;
    }
    void getUsers() {
        
        JsonObject addMessage = createAddMessageAllUsers();
        System.out.println(addMessage);
        sendToAllConnectedSessions(addMessage);
      
    }
    
    public boolean guardarUsuarios(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonEjemplo = gson.toJson(usuarios);
        String basePath = new File("").getAbsolutePath() + "\\users.json" ;
        System.out.println("this is? " + basePath);
        System.out.println(jsonEjemplo); 
        try (FileWriter file = new FileWriter(basePath)) {
			file.write(jsonEjemplo);
			System.out.println("Successfully Copied JSON Object to File...");		
	
        } catch (IOException ex) {
             Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
             return false; 
         }
        return true; 
    }
    
    public boolean cargarUsuarios(){
        Gson gson1 = new Gson();  
        String basePath = new File("").getAbsolutePath() + "\\users.json" ;
        Path path = Paths.get(basePath);
        File file = path.toFile(); 
        List<String> contenido_a_copiar; 
        String contenidoArchivo = ""; 
        try {
            contenido_a_copiar = Files.readAllLines(path);
            for (int i = 0; i < contenido_a_copiar.size(); i++) {
                contenidoArchivo += contenido_a_copiar.get(i); 
            }
        } catch (IOException ex) {
             Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
             return false; 
        }
        usuarios = gson1.fromJson(contenidoArchivo, new TypeToken<Set<Usuario>>(){}.getType());     
        if( usuarios!= null ){
            for(Usuario object : usuarios){
                System.out.println(object.toString());    
            }    
        }else{
            return false; 
        }
        return true; 
    }
    
}