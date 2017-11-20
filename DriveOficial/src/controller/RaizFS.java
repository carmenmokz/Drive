/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
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
        dirActual = dir; 
    }
    public boolean crearArchivo(String nombreArchivo, String extension, String contenido){
        Archivo archivo = new Archivo(nombreArchivo, extension, contenido); 
        consumido += contenido.length(); 
        if (limiteTamanio < consumido){
            System.out.println("excede la cantidad de consumo de su drive");
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
    public boolean copiarRV(){return false;}
    public boolean copiarVR(){return false;}
    public boolean copiarVV(){return false;}
    public String relativo_a_absoluto(){
        return "uqka"; 
    }
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
                    System.out.println("aqui?");
                    dir.quitarCarpeta(dir1);
                    System.out.println("wat 2");
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
        String[] carpetasDeDirSolicitado = directorioSolicitado.split("\\/");
        System.out.println("holi ");// son las carpetas de la direccion separadas 
        for (String string : carpetasDeDirSolicitado) {
            System.out.println(string);
        }
        System.out.println("holi ");
        Directorio temp = dir;                                                          // se situa en la pura raiz, con ese temp vamos a navegar 
        if(carpetasDeDirSolicitado.length == 1 && carpetasDeDirSolicitado[0].equals(dir.getNombre())){ 
            return dir;  
        } 
        else{ 
            for(int i = 0; i < carpetasDeDirSolicitado.length; i++){
                String string = carpetasDeDirSolicitado[i];
                System.out.println("iteracion de string " + i);
                ArrayList<Directorio> directorios = temp.getDirectorios();  
                int indice = i; 
                for (Directorio directorio : directorios) { 
                    if(directorio.getNombre().equals(string)){ 
                        System.out.println("victory " + string );
                        //int indice = directorios.indexOf(directorio);  
                        System.out.println("indice " + indice + " y el carp " + (carpetasDeDirSolicitado.length - 1));
                        if(indice == (carpetasDeDirSolicitado.length - 1)) 
                        { 
                            return directorio; 
                        } 
                        else{ 
                            temp = directorio;
                            System.out.println(directorio);
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

    @Override
    public String toString() {
        return "RaizFS{" + "nombreRaiz=" + nombreRaiz + ", consumido=" + consumido + ", limiteTamanio=" + limiteTamanio + ", dir=" + dir + '}';
    }
    
  
}
    
    
