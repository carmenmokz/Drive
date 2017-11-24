/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools |sd Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import static java.lang.Integer.parseInt;
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
import org.apache.commons.io.FileUtils;

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
     public void addFolder(String username,String dir, String nombre, Session session) {
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        if(usuario.getFileSystem().existeDirEnDir(dir, nombre)==false){
            usuario.getFileSystem().crearDirectorio(nombre);
            changeFolder(username,dir, session);
        }else{
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                        .add("action", "verFolder")
                        .add("username", username)
                        .add("dir", dir)
                        .add("nombre",nombre)
                        .add("sol",0)
                        .build();
            System.out.println(removeMessage);
            sendToSession(session, removeMessage);
        }
       
       
         
    }
    public void addFile(String username,String dir, String nombre, String ext, String cont,Session session) {
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        int nuevoTotal = usuario.getFileSystem().getConsumido() + cont.length(); 
        if (nuevoTotal <= usuario.getFileSystem().getLimiteTamanio()){
            if(usuario.getFileSystem().existeArchEnDir(dir, nombre+"."+ext)==false){
                usuario.getFileSystem().crearArchivo(nombre,ext,cont);
                changeFolder(username,dir,session);
            }else{
                JsonProvider provider = JsonProvider.provider();
                JsonObject removeMessage = provider.createObjectBuilder()
                            .add("action", "verFile")
                            .add("username", username)
                            .add("dir", dir)
                            .add("nombre",nombre)
                            .add("ext",ext)
                            .add("cont",cont)
                            .build();
                System.out.println(removeMessage);
                sendToSession(session, removeMessage);
            }
        }
        else{
            System.out.println("al chilee es mas grande");
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "NoDisponible")
                    .build();
            sendToSession(session, removeMessage);
        }

         
    }
    public void copy(String username, int type,String origin,String destiny,Session session) throws IOException {
        System.out.println("en copy" + " origin: "+ origin + " destiny: "+ destiny);
        boolean exito = true; 
        Usuario usuario=getUsuarioByUsername(username);
        String[] bits = origin.split("/");
        String lastOne = bits[bits.length-1];
        switch(type){
                case 0:
                   
                    usuario.getFileSystem().copiarVR(origin, destiny);
                    destiny=origin; 
                    break;
                case 1:
                    bits = origin.split("\\\\");
                    System.out.println(bits);
                    lastOne = bits[bits.length-1];
                    System.out.println(lastOne);
                    System.out.println("caso  0");
                    Path path = Paths.get(origin); 
                    File file = path.toFile(); 
                    int tamanoarchivo = 0; 
                    if(file.isDirectory())
                            tamanoarchivo = Math.toIntExact(FileUtils.sizeOfDirectory(new File(origin))); 
                    else
                            tamanoarchivo =  Files.readAllLines(path).size();
                    System.out.println("tamano arch"+ tamanoarchivo);
                    int verificar = tamanoarchivo + usuario.getFileSystem().getConsumido(); 
                    System.out.println("li:" +verificar);
                    if(verificar <= usuario.getFileSystem().getLimiteTamanio()){
                        if(usuario.getFileSystem().existeDirEnDir(destiny, lastOne)==false&& usuario.getFileSystem().existeArchEnDir(destiny, lastOne)==false){
                            usuario.getFileSystem().copiarRV(origin, destiny);
                        }else{
                             JsonProvider provider = JsonProvider.provider();
                             JsonObject removeMessage = provider.createObjectBuilder()
                                            .add("action", "verTodos")
                                            .add("username", username)
                                            .add("typeCopy", type)
                                            .add("origin",origin)
                                            .add("destiny",destiny)
                                            .add("c",0)
                                            .build();
                                System.out.println(removeMessage);
                                sendToSession(session, removeMessage);
                        }
                    }
                    else{
                        System.out.println("al chilee es mas grande");
                        JsonProvider provider = JsonProvider.provider();
                        JsonObject removeMessage = provider.createObjectBuilder()
                                .add("action", "NoDisponible")
                                .build();
                        exito = false; 
                        sendToSession(session, removeMessage);
                    }
                    break;
                case 2:
                   bits = origin.split("/");
                    System.out.println(bits);
                   lastOne = bits[bits.length-1];
                   int tam = 0;
                   if(usuario.getFileSystem().existeDirEnDir(origin, lastOne)){
                       tam = usuario.getFileSystem().conseguirArchivo(origin, lastOne).getTamanio(); 
                   }
                   else{
                       tam = usuario.getFileSystem().pesoDir(origin);
                   }
                   int total = tam + usuario.getFileSystem().getConsumido(); 
                   if(total  <= usuario.getFileSystem().getLimiteTamanio()){
                        if(usuario.getFileSystem().existeDirEnDir(destiny, lastOne)==false&&usuario.getFileSystem().existeArchEnDir(destiny, lastOne)==false){
                            System.out.println("Vine a copiar");
                            usuario.getFileSystem().copiarVV(origin, destiny);
                        }else{
                             JsonProvider provider = JsonProvider.provider();
                             JsonObject removeMessage = provider.createObjectBuilder()
                                            .add("action", "verTodos")
                                            .add("username", username)
                                            .add("typeCopy", type)
                                            .add("origin",origin)
                                            .add("destiny",destiny)
                                            .add("c",1)
                                            .build();
                                System.out.println(removeMessage);
                                sendToSession(session, removeMessage);
                        }
                   }
                    else{
                        System.out.println("al chile es mas grande");
                        JsonProvider provider = JsonProvider.provider();
                        JsonObject removeMessage = provider.createObjectBuilder()
                                .add("action", "NoDisponible")
                                .build();
                        exito = false; 
                        sendToSession(session, removeMessage);
                    }
                    break;
        }  
        System.out.println(destiny);
        changeFolder(username,destiny, session);
       
         
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
    public void getMainFolder(String username, Session session){
       Usuario usuario=getUsuarioByUsername(username);
       JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder files = Json.createArrayBuilder();
        usuario.getFileSystem().cambiarDirActual("D/Personal");
   
    
        for(Archivo a: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getArchivos()){
            files.add(Json.createObjectBuilder()
                    .add("name", a.getNombre())
                    .add("ext", a.getExtension())
                    .add("size",a.getTamanio())
                    .add("cont",a.getContenido())
                    .add("date",a.getCreacion().toString())
                    .build());
        }

        JsonArray arr = files.build();
        JsonArrayBuilder folders = Json.createArrayBuilder();
        System.out.println(usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios());
        for(Directorio d: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios()){
            System.out.println(d.getNombre());
            folders.add(Json.createObjectBuilder()
                    .add("name",d.getNombre())
                    .add("date","")
                    .build());
        }

        JsonArray arr2 = folders.build();
    
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "getMainFolder")
                .add("folders", arr2)
                .add("archivos",arr)
                .build();
        System.out.println(addMessage);
        sendToSession(session, addMessage);
       
       
   }
   public void getShareFolder(String username,Session session){
        Usuario usuario=getUsuarioByUsername(username);
       JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder files = Json.createArrayBuilder();
        usuario.getFileSystem().cambiarDirActual("D/Compartido");

        for(Archivo a: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getArchivos()){
            files.add(Json.createObjectBuilder()
                    .add("name", a.getNombre())
                    .add("ext", a.getExtension())
                    .add("size",a.getTamanio())
                    .add("cont",a.getContenido())
                    .add("date",a.getCreacion().toString())
                    .build());
        }

        JsonArray arr = files.build();
        JsonArrayBuilder folders = Json.createArrayBuilder();
        System.out.println(usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios());
        for(Directorio d: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios()){
            System.out.println(d.getNombre());
            folders.add(Json.createObjectBuilder()
                    .add("name",d.getNombre())
                    .add("date","")
                    .build());
        }

        JsonArray arr2 = folders.build();
    
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "getMainFolder")
                .add("folders", arr2)
                .add("archivos",arr)
                .build();
        sendToSession(session, addMessage);
       
   }
     public void changeFolder(String username,String folder, Session session){
        Usuario usuario=getUsuarioByUsername(username);
       JsonProvider provider = JsonProvider.provider();
        // create Json array with only values
        JsonArrayBuilder files = Json.createArrayBuilder();
        usuario.getFileSystem().cambiarDirActual(folder);

        for(Archivo a: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getArchivos()){
            files.add(Json.createObjectBuilder()
                    .add("name", a.getNombre())
                    .add("ext", a.getExtension())
                    .add("size",a.getTamanio())
                    .add("cont",a.getContenido())
                    .add("date",a.getCreacion().toString())
                    .build());
        }

        JsonArray arr = files.build();
        JsonArrayBuilder folders = Json.createArrayBuilder();
        System.out.println(usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios());
        for(Directorio d: usuario.getFileSystem().encontrarDirectorio(usuario.getFileSystem().getDirActual()).getDirectorios()){
            System.out.println(d.getNombre());
            folders.add(Json.createObjectBuilder()
                    .add("name",d.getNombre())
                    .add("date","")
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
        sendToSession(session, addMessage);
       
   }
    
    public boolean guardarUsuarios(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonEjemplo = gson.toJson(usuarios);
        String basePath = new File("").getAbsolutePath() + "\\users.json" ;
      
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
    
    
    
     public void sharedAllFiles(Directorio dirShared, RaizFS userWhoRecivies,Session session) {
         System.out.println("eentra a dir shared");
         System.out.println(dirShared.getNombre());
         ArrayList<Directorio> directorios = dirShared.getDirectorios(); 
        ArrayList<Archivo> archivos = dirShared.getArchivos(); 
        Directorio dirActual = userWhoRecivies.encontrarDirectorio(userWhoRecivies.getDirActual());
        userWhoRecivies.cambiarDirActual(userWhoRecivies.getDirActual()+"/"+dirShared.getNombre());
        for (Archivo archivo : archivos) {
            shareFileSimplified(archivo, userWhoRecivies);
        }
        for (Directorio directorio : directorios) {
            shareDirectory(directorio, userWhoRecivies, session);
        }
        
        
        
    }
    public void shareFileSimplified(Archivo arc, RaizFS filesystem){
        System.out.println("file systen shared" + filesystem.getDirActual());
       filesystem.crearArchivo(arc.getNombre(), arc.getExtension(), arc.getContenido()); 
        
    }
    
    public void shareFile(String user, String file, String path, String toUser, String toCopyPath,Session session){
        Usuario usuarioManda = getUsuarioByUsername(user); 
        Usuario usuarioRecibe = getUsuarioByUsername(toUser);
        RaizFS FileSystem = usuarioRecibe.getFileSystem(); 
        RaizFS FileSystemShared = usuarioManda.getFileSystem(); 
        String altfile =FileSystem.conseguirUltimo(file); 
        Archivo archivo = FileSystemShared.conseguirArchivo(path, altfile); 
        if(toCopyPath.equals("")){
            FileSystem.encontrarDirectorio("D/Compartido");
            FileSystem.cambiarDirActual("D/Compartido");
            FileSystem.copiarVVArchivo(archivo, FileSystem.getDirActual()); 
        }
        System.out.println(usuarios.toString());
    }
    
    public void shareDirectory(String user, String directory, String toUser, String toCopyPath,Session session){
        
        System.out.println(user + directory+toUser);
        Usuario usuarioManda = getUsuarioByUsername(user); 
        Usuario usuarioRecibe = getUsuarioByUsername(toUser);
        RaizFS FileSystemOf = usuarioManda.getFileSystem(); 
        RaizFS FileSystemS = usuarioRecibe.getFileSystem();
        Directorio dirShared = FileSystemOf.encontrarDirectorio(FileSystemOf.verificacionVirtual_a_Real(directory)); 
        System.out.println("dirShAAARED:" +dirShared.toString());
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
        sharedAllFiles(dirShared, FileSystemS, session);
        FileSystemS.cambiarDirActual(dirOriginalS);
        
    }
    
    public void shareDirectory(Directorio directory, RaizFS filesystem, Session session){
        String dirOriginalS = filesystem.getDirActual();
        filesystem.crearDirectorio(directory.getNombre());
        sharedAllFiles(directory, filesystem, session);
        filesystem.cambiarDirActual(dirOriginalS);
    }
    public void deleteFile(String username,String dir,String file, String ext,Session session){
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        usuario.getFileSystem().eliminarArchivo(file, ext);
        changeFolder(username, dir,session);
        
    }
    public void deleteFolder(String username,String dir,String file, Session session){
        System.out.println(dir + " file: " + file);
        
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        usuario.getFileSystem().eliminarDirectorio(file);
        changeFolder(username, dir,session);
        
    }
    public void move(String username,int type, String file,String destiny,Session session){
        Usuario usuario=getUsuarioByUsername(username);
        String[] bits ;
        String lastOne ;
        switch(type){
            case 0:
                bits = file.split("/");
                lastOne = bits[bits.length-1];
               
                System.out.println(usuario.getFileSystem().existeDirEnDir(destiny,lastOne));
                if(usuario.getFileSystem().existeArchEnDir(destiny,lastOne)==false){
                    System.out.println("No existe");
                    usuario.getFileSystem().moverArchivo(file, destiny);
                    changeFolder(username, destiny,session);
                }else{
                    System.out.println("Exist");
                    JsonProvider provider = JsonProvider.provider();
                    JsonObject removeMessage = provider.createObjectBuilder()
                                        .add("action", "verFile")
                                        .add("username", username)
                                        .add("typeMove", type)
                                        .add("file",file)
                                        .add("destiny",destiny)
                                        .add("sol",2)
                                        .build();
                            
                    sendToSession(session, removeMessage);
                }
                break;
            case 1:
                bits = file.split("/");
                lastOne = bits[bits.length-1];
               
                System.out.println(usuario.getFileSystem().existeDirEnDir(destiny,lastOne));
                if(usuario.getFileSystem().existeDirEnDir(destiny,lastOne)==false){
                    System.out.println("Si entro");
                    System.out.println(file);
                    System.out.println(destiny);
                    usuario.getFileSystem().moverCarpeta(file, destiny);
                    changeFolder(username, destiny,session);
                 }else{
                    System.out.println("Exist");
                    JsonProvider provider = JsonProvider.provider();
                    JsonObject removeMessage = provider.createObjectBuilder()
                                        .add("action", "verFolder")
                                        .add("username", username)
                                        .add("typeMove", type)
                                        .add("file",file)
                                        .add("destiny",destiny)
                                        .add("sol",2)
                                        .build();
                            
                    sendToSession(session, removeMessage);
                }
                break;
        }
        
        
    }
    
     public void edit(String username,String dir, String oldfile,String file,String ext,String cont, Session session){
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        usuario.getFileSystem().modificarArchivo(oldfile, cont);
        usuario.getFileSystem().modificarArchivoExt(oldfile, ext);
        usuario.getFileSystem().modificarArchivoNombre(oldfile, file);
        
        changeFolder(username, dir,session);
    }
     public void view(String username,String dir, String file, Session session, int type){
        Usuario usuario=getUsuarioByUsername(username);
        usuario.getFileSystem().cambiarDirActual(dir);
        String fileStr=usuario.getFileSystem().verArchivo(dir, file);
        
        JsonObject body = Json.createReader(new StringReader(fileStr)).readObject();
        
        JsonProvider provider = JsonProvider.provider();
        JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "view")
                    .add("type", type)
                    .add("file", body)
                    .build();
        System.out.println(removeMessage);
        sendToSession(session, removeMessage);
    }
    @Override
    public String toString() {
        return "DeviceSessionHandler{" + "usuarios=" + usuarios + '}';
    }
}