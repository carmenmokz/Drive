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
import model.Archivo;
import model.Directorio;
import model.Usuario;

@ApplicationScoped
public class DeviceSessionHandler {
    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private Set<Usuario> usuarios = new HashSet<>();

    public DeviceSessionHandler() {
        verificarExisteJson();
        
    }
    
    public void verificarExisteJson(){
        String basePath = new File("").getAbsolutePath();
        boolean check = new File(basePath, "users.json").exists();
        System.out.println("Existe  Json: "+ check);
        if(!check){
            try (FileWriter file = new FileWriter(basePath+"//users.json")) {
                file.write("[]");
                System.out.println("Successfully Copied JSON Object to File...");		
            } catch (IOException ex) {
                 Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);

             }
        }
        else{
            cargarUsuarios(); 
        }
    }
    
    
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
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
     public void addFolder(String username,String dir, String nombre) {
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        usuario.getFileSystem().crearDirectorio(nombre);
        changeFolder(username,dir);
       
         
    }
      public void addFile(String username,String dir, String nombre, String ext, String cont) {
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        usuario.getFileSystem().crearArchivo(nombre,ext,cont);
        changeFolder(username,dir);
       
         
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
    public void getUsers() {
        
        JsonObject addMessage = createAddMessageAllUsers();
        System.out.println(addMessage);
        sendToAllConnectedSessions(addMessage);
      
    }
    public void getMainFolder(String username){
       Usuario usuario=getUsuarioByUsername(username);
       JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder files = Json.createArrayBuilder();
        usuario.getFileSystem().cambiarDirActual("D/Personal");
        usuario.getFileSystem().crearDirectorio("CUAL"); 
        usuario.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(usuario.toString());
        for(Archivo a: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getArchivos()){
            files.add(Json.createObjectBuilder()
                    .add("name", a.getNombre())
                    .add("ext", a.getExtension())
                    .add("size",a.getTamanio())
                    .add("cont",a.getContenido())
                    .build());
        }

        JsonArray arr = files.build();
        JsonArrayBuilder folders = Json.createArrayBuilder();
        System.out.println(usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios());
        for(Directorio d: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios()){
            System.out.println(d.getNombre());
            folders.add(Json.createObjectBuilder()
                    .add("name",d.getNombre())
                    .build());
        }

        JsonArray arr2 = folders.build();
    
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "getMainFolder")
                .add("folders", arr2)
                .add("archivos",arr)
                .build();
        System.out.println(addMessage);
        sendToAllConnectedSessions(addMessage);
       
   }
   public void getShareFolder(String username){
        Usuario usuario=getUsuarioByUsername(username);
       JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder files = Json.createArrayBuilder();
        usuario.getFileSystem().cambiarDirActual("D/Compartido");
         usuario.getFileSystem().crearDirectorio("CUAL"); 
        usuario.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        for(Archivo a: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getArchivos()){
            files.add(Json.createObjectBuilder()
                    .add("name", a.getNombre())
                    .add("ext", a.getExtension())
                    .add("size",a.getTamanio())
                    .add("cont",a.getContenido())
                    .build());
        }

        JsonArray arr = files.build();
        JsonArrayBuilder folders = Json.createArrayBuilder();
        System.out.println(usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios());
        for(Directorio d: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios()){
            System.out.println(d.getNombre());
            folders.add(Json.createObjectBuilder()
                    .add("name",d.getNombre())
                    .build());
        }

        JsonArray arr2 = folders.build();
    
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "getMainFolder")
                .add("folders", arr2)
                .add("archivos",arr)
                .build();
        sendToAllConnectedSessions(addMessage);
       
   }
     public void changeFolder(String username,String folder){
        Usuario usuario=getUsuarioByUsername(username);
       JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder files = Json.createArrayBuilder();
        usuario.getFileSystem().cambiarDirActual(folder);
         usuario.getFileSystem().crearDirectorio("blub"); 
        usuario.getFileSystem().crearArchivo("xime", "pdf", "salsa a la 2 am");
        for(Archivo a: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getArchivos()){
            files.add(Json.createObjectBuilder()
                    .add("name", a.getNombre())
                    .add("ext", a.getExtension())
                    .add("size",a.getTamanio())
                    .add("cont",a.getContenido())
                    .build());
        }

        JsonArray arr = files.build();
        JsonArrayBuilder folders = Json.createArrayBuilder();
        System.out.println(usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios());
        for(Directorio d: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios()){
            System.out.println(d.getNombre());
            folders.add(Json.createObjectBuilder()
                    .add("name",d.getNombre())
                    .build());
        }

        JsonArray arr2 = folders.build();
    
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "changeFolder")
                .add("dir", folder)
                .add("folders", arr2)
                .add("archivos",arr)
                .build();
        System.out.println("Me paso a:" + folder);
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
    
    
    
     public void sharedAllFiles(Directorio dirShared, RaizFS userWhoRecivies) {
         System.out.println("entra a dir shared");
         System.out.println(dirShared.getNombre());
         ArrayList<Directorio> directorios = dirShared.getDirectorios(); 
        ArrayList<Archivo> archivos = dirShared.getArchivos(); 
        Directorio dirActual = userWhoRecivies.encontrarDirectorio(userWhoRecivies.getDirActual()); 
        for (Archivo archivo : archivos) {
            userWhoRecivies.cambiarDirActual(userWhoRecivies.getDirActual()+"/"+dirShared.getNombre());
            shareFileSimplified(archivo, userWhoRecivies);
        }
        for (Directorio directorio : directorios) {
            shareDirectory(directorio, userWhoRecivies);
        }
        
        
        
    }
    public void shareFileSimplified(Archivo arc, RaizFS filesystem){
        System.out.println("file systen shared" + filesystem.getDirActual());
       filesystem.crearArchivo(arc.getNombre(), arc.getExtension(), arc.getContenido()); 
        
    }
    
    public void shareFile(String user, String file, String path, String toUser, String toCopyPath){
        Usuario usuarioManda = getUsuarioByUsername(user); 
        Usuario usuarioRecibe = getUsuarioByUsername(toUser);
        RaizFS FileSystem = usuarioRecibe.getFileSystem(); 
        RaizFS FileSystemShared = usuarioManda.getFileSystem(); 
        System.out.println(usuarioManda);
        System.out.println(path);
        System.out.println(FileSystemShared.getDirActual());
        Archivo archivo = FileSystemShared.conseguirArchivo(path, file); 
        if(toCopyPath.equals("")){
            FileSystem.encontrarDirectorio("D/Compartido");
            FileSystem.cambiarDirActual("D/Compartido");
            FileSystem.copiarVVArchivo(archivo, FileSystem.getDirActual()); 
        }
    }
    
    public void shareDirectory(String user, String directory, String toUser, String toCopyPath){
        Usuario usuarioManda = getUsuarioByUsername(user); 
        Usuario usuarioRecibe = getUsuarioByUsername(toUser);
        RaizFS FileSystemOf = usuarioManda.getFileSystem(); 
        RaizFS FileSystemS = usuarioRecibe.getFileSystem();
        Directorio dirShared = FileSystemOf.encontrarDirectorio(FileSystemOf.verificacionVirtual_a_Real(directory)); 
        Directorio dirRecibe; 
        String dirOriginalS = FileSystemS.getDirActual(); 
        if(toCopyPath.equals("")){
            dirRecibe = FileSystemS.encontrarDirectorio("D/Compartido"); 
            FileSystemS.cambiarDirActual("D/Compartido");
        }
        else{
            dirRecibe = FileSystemS.encontrarDirectorio(toCopyPath); 
            FileSystemS.cambiarDirActual(toCopyPath);
        }
        FileSystemS.crearDirectorio(dirShared.getNombre());
        sharedAllFiles(dirShared, FileSystemS);
        FileSystemS.cambiarDirActual(dirOriginalS);
        
    }
    
    public void shareDirectory(Directorio directory, RaizFS filesystem){
        String dirOriginalS = filesystem.getDirActual();
        filesystem.crearDirectorio(directory.getNombre());
        sharedAllFiles(directory, filesystem);
        filesystem.cambiarDirActual(dirOriginalS);
    }

    @Override
    public String toString() {
        return "DeviceSessionHandler{" + "usuarios=" + usuarios + '}';
    }
}