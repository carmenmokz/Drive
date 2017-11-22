/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import model.Usuario;

@ApplicationScoped
public class DeviceSessionHandler {
     private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Usuario> usuarios = new HashSet<>();
    
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
       
        usuarios.add(usuario);
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
}