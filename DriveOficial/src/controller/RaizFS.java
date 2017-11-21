/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Archivo;
import model.Directorio;
import model.Directorio;

/**
 *
 * @author bermu
 */
public class RaizFS {
    private String nombreRaiz; 
    private int consumido; 
    private int limiteTamanio; 
    private Directorio dir;         //a quien apunta ya para trabajar normal 
    private String dirActual;       //como pasare navegando en el FileSystem si quiero agregar una carpeta o archivo sera donde estoy
    
    public RaizFS(int maxBytes){
        nombreRaiz = "D"; // D de DRIVE ajajjaja entienden xD kill me
        dirActual = nombreRaiz; 
        dir = new Directorio(nombreRaiz); 
        dir.agregarDirectorio(new Directorio("Personal")); 
        dir.agregarDirectorio(new Directorio("Compartido"));
        consumido = 0; 
        limiteTamanio = maxBytes; 
    }
    
    public boolean borrarEnHijos(Directorio dir){ // funcion que navega hasta lo profundo O:
        ArrayList<Directorio> dirs = dir.getDirectorios(); 
        for (Directorio dir1 : dirs) {
            borrarEnHijos(dir1); 
        }
        ArrayList<Archivo> archivos = dir.getArchivos(); 
        for (Archivo archivo : archivos) {
            eliminarArchivo(archivo.getNombre(), archivo.getExtension()); 
        }
        return false;
    }
    
    public void cambiarDirActual(String dir){
        dirActual = verificacionVirtual_a_Real(dir);
    }
    public boolean crearArchivo(String nombreArchivo, String extension, String contenido){
        Archivo archivo = new Archivo(nombreArchivo, extension, contenido); 
        consumido += contenido.length(); 
        if (limiteTamanio < consumido){
            System.out.println("Excede la cantidad de consumo de su drive");
            consumido -= contenido.length(); 
            return false; 
        }
        else{
            return encontrarDirectorio(dirActual).agregarArchivo(archivo); 
        }
        
    }
    public boolean crearDirectorio(String nombreCarpeta){
        Directorio dir = new Directorio(nombreCarpeta);
        return encontrarDirectorio(dirActual).agregarDirectorio(dir); 
    }
    public boolean compartirArchivo(){return false;}
    public boolean compartirDir(){return false;}
    public boolean copiarRV(String dirReal, String dirVirtual){
      
        String dirVirtualProcesada = verificacionVirtual_a_Real(dirVirtual); 
        if(verificarDirectorioExiste(dirVirtualProcesada)){
            System.out.println("dir real" + dirReal);
            Path path = Paths.get(dirReal); 
            File file = path.toFile(); 
            if(file.isDirectory()){
                /*
                    En vez de enviar la carpeta de un solo, decidi
                mejor enviar otra vez el string porque si es una
                carpeta muy grande dura mucho
                */
                copiarRVDirectorio(dirReal, dirVirtualProcesada); 
            }
            else{
                copiarRVArchivo(dirReal, dirVirtualProcesada); 
            }
            return true; 
        }
        else{
            System.out.println("El directorio Virtual no existe");
            return false;
        }
    }
    public boolean copiarRVArchivo(String dirReal, String dirVirtual) {
        String dirOriginal = dirActual;
        System.out.println("dirvirtual" + dirVirtual);
        Path path = Paths.get(dirReal); 
        File file = path.toFile(); 
        String[] nombreArchivo = file.getName().split("\\.");
        String nombreDrive = ""; 
        String contenidoArchivo = ""; 
        for (int i = 0; i < (nombreArchivo.length-1); i++) {
            // que tome todo lo que esta antes del punto
            nombreDrive += nombreArchivo[i];
        }
        List<String> contenido_a_copiar; 
        try {
            contenido_a_copiar = Files.readAllLines(path);
            for (int i = 0; i < contenido_a_copiar.size(); i++) {
                contenidoArchivo += contenido_a_copiar.get(i); 
            }
        } catch (IOException ex) {
            System.out.println("Error copiando contenido, revisar direccion");
            return false;
        }
        cambiarDirActual(dirVirtual);
        System.out.println("dirVirtual: "+ dirVirtual + " \n Archivo: nombre: " + nombreDrive + " ext: " + nombreArchivo[nombreArchivo.length-1] + " contenido: "+ contenidoArchivo );
        boolean exito = crearArchivo(nombreDrive, nombreArchivo[nombreArchivo.length-1], contenidoArchivo); 
        if(exito){
            cambiarDirActual(dirOriginal);
            return true; 
        }
        return false; 
    }
    
    public boolean copiarRVArchivos(String dirReal) {
        /*
            Diferencia entre funcion archivo y archivos es que
        archivos copia todos los archivos que haya en el dir
        */
        Path path = Paths.get(dirReal); 
        File dirNuevo = path.toFile(); 
        File[] archivos = dirNuevo.listFiles(); 
        for (File archivo : archivos) {
            if(archivo.isDirectory()){
                System.out.println("arhcivo abs" + dirActual);
                boolean exito = copiarRVDirectorio(archivo.getAbsolutePath(), dirActual); 
                if(!exito){return false;}               
            }
            else if(archivo.isFile()){
                System.out.println("arhcivo abs" + dirActual);
                boolean exito = copiarRVArchivo(archivo.getAbsolutePath(), dirActual); 
                if(!exito){return false;}  
            }
        }
        return true; 
    }

    public boolean copiarRVDirectorio(String dirReal, String dirVirtual) {
        String dirOriginal = dirActual;
        System.out.println("dir Actual 1:" + dirActual);
        cambiarDirActual(dirVirtual);
        
        Path path = Paths.get(dirReal); 
        File dirNuevo = path.toFile(); 
        crearDirectorio(dirNuevo.getName()); 
        cambiarDirActual(dirNuevo.getName());
        if(copiarRVArchivos(dirReal)){
            //significa que si se copiaron bien
            cambiarDirActual(dirOriginal);
            return true; 
        }
        else{
            return false; 
        }
    }
    
    public boolean copiarVR(){return false;}
    public boolean copiarVV(){return false;}
    
    public boolean eliminarArchivo(String nombre, String extension){
        Directorio dir = encontrarDirectorio(dirActual); 
        ArrayList<Archivo> archivos = dir.getArchivos();
        for (Archivo archivo : archivos) {
            if(archivo.getNombre().equals(nombre) && archivo.getExtension().equals(extension)){
                consumido -= archivo.getContenido().length(); 
                dir.quitarArchivo(archivo);
                return true;
            }
        }
        return false; 
    }
    
    
    
    public boolean eliminarDirectorio(String nombre){
        Directorio dir = encontrarDirectorio(dirActual); 
        if(dir == null){
            return false;
        }
        else{
            ArrayList<Directorio> dirs = dir.getDirectorios(); 
            for (Directorio dir1 : dirs) {
                if (dir1.getNombre().equals(nombre)){
                    borrarEnHijos(dir1); 
                    dir.quitarCarpeta(dir1);
                    return true;
                }
            }
        }
        return false; 
    }
    
    
    public Directorio encontrarDirectorio (String directorioSolicitado){ 
    /* 
        @sumary: es navegar al directorio que me piden, y devuelvo el  
        directorio para que ya con eso haga lo que ocupa, copiar, mover, etc 
    */ 
        String[] carpetasDeDirSolicitado = directorioSolicitado.split("\\/"); // son las carpetas de la direccion separadas 
        Directorio temp = dir;                                                          // se situa en la pura raiz, con ese temp vamos a navegar 
        if(carpetasDeDirSolicitado.length == 1 && carpetasDeDirSolicitado[0].equals(dir.getNombre())){ 
            return dir;  
        } 
        else{ 
            for(int i = 0; i < carpetasDeDirSolicitado.length; i++){
                String string = carpetasDeDirSolicitado[i];
                ArrayList<Directorio> directorios = temp.getDirectorios();  
                int indice = i; 
                for (Directorio directorio : directorios) { 
                    if(directorio.getNombre().equals(string)){ 
                        //int indice = directorios.indexOf(directorio);  
                        if(indice == (carpetasDeDirSolicitado.length - 1)) 
                        { 
                            return directorio; 
                        } 
                        else{ 
                            temp = directorio;
                        }
                    } 
                } 
            } 
            System.out.println("no lo encontro el FS");
            return null; 
        }
    }
    public boolean moverArchivo(){return false;}
    public boolean moverCarpeta(){return false;}
    public boolean modificarArchivo(){return false;}
    public void verArchivo(){}
   
    public boolean verificarDirectorioExiste(String direccion){
        Directorio dir = encontrarDirectorio(direccion); 
        if(dir == null){
            return false;
        }
        else{
            return true; 
        }
    }   
    
    public String virtual_a_real(String dirBase, String direccionVirtual){
        String[] virtualArray = direccionVirtual.split("\\/"); 
        for (String virtualSegmento : virtualArray) {
            if(virtualSegmento.equals("..")){
                /*
                    En este caso habria que buscar al papa con
                ayuda de dirBase, y cuando ya lo encuentra se
                sigue pusheando la dirVirtual normalmente
                Se valida que la dirBase no sea el mismo nombre
                del directorio base (dir) porque sino no tiene
                a quien buscarle papa y seria un error 
                */
                
                String[] dirBaseArray = dirBase.split("\\/"); 
                dirBase = ""; // se formatea para que se realice una nueva dir sobre ella 
                if(!dirBase.equals(dir.getNombre())){
                    for (int i = 0; i < (dirBaseArray.length-1); i++){
                        if(dirBase.equals("")){
                            /*
                                Se mete a dirBase como el inicio 
                            de verdad sin / o cosas raras invalidas
                            */
                            dirBase += dirBaseArray[i]; 
                        }
                        else{
                            /*
                                Aqui ya se mete con los separadores
                            correspondientes (: 
                            */
                            dirBase += "/" + dirBaseArray[i]; 
                        }
                    }
                } 
            }
            else if(!virtualSegmento.equals(".")){
                /*
                    Basicamente si sale un punto(".") se ignora
                y se le empieza a meter a dirBase la dirVirtual
                entonces ya queda como Dios manda
                */
                dirBase += "/" + virtualSegmento; 
            }
        }
        return dirBase; //va a tener la nueva dir 
    }
    public String verificacionVirtual_a_Real(String direccion){
        String[] direccionSplit = direccion.split("\\/");
        if (direccionSplit[0].equals(dir.getNombre())){
            return direccion;
        }
        else{
            return virtual_a_real(dirActual, direccion); 
        }
    }
    
    @Override
    public String toString() {
        return "RaizFS{" + "nombreRaiz=" + nombreRaiz + ", consumido=" + consumido + ", limiteTamanio=" + limiteTamanio + ", dir=" + dir + '}';
    }
    
    
  
  
}
    
    
