/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author bermu
 */
public class Directorio {
    
    private String nombre;                      //el nombre de la carpetilla
    private String ubicacionDireccion;          //para saber donde estoy
    private ArrayList<Archivo> archivos;       //los archivos que va a contener 
    private ArrayList<Directorio> directorios;  //las carpetas que puede tener
    
    
    // cuando se hace desde cero el nuevo directorio
    public Directorio(String nombre){
        this.nombre = nombre; 
        archivos = new ArrayList<>(); 
        directorios = new ArrayList<>(); 
    }
    
    //en caso de que este copiando la carpeta
    public Directorio(String nombre, ArrayList<Archivo> archivos, ArrayList<Directorio> carpetas){
        this.nombre = nombre;
        this.archivos = archivos; 
        this.directorios = carpetas; 
    }
    
    public boolean agregarArchivo(Archivo archivo){
       //si devuelve true esq hubo exito 
       //hay que chequear si esta 
       boolean listo = false; 
       Archivo archivoExiste = chequearPorMismoArchivo(archivo); 
       if (archivoExiste != null){
           archivos.remove(archivoExiste); 
       }
       archivos.add(archivo); 
       listo = true; 
       return listo; 
    }
    
    public boolean agregarDirectorio(Directorio dir){
       //si devuelve true esq hubo exito 
       //hay que chequear si esta 
       boolean listo = false; 
       Directorio dirExiste = chequearPorMismaCarpeta(dir); 
       if (dirExiste != null){
           directorios.remove(dirExiste); 
       }
       directorios.add(dir); 
       listo = true; 
       return listo; 
    }
    
    
    public Archivo chequearPorMismoArchivo(Archivo archivo){
        //cuando en la interfaz quiere agregar algo llamar a esta funcion para saber si 
        //quiere reemplazarlo o no, si no quiere preguntarle por nuevo nombre
        for (Archivo archivo1 : archivos) {
            if (archivo.nombre.equals(archivo1.getNombre()) && archivo.extension.equals(archivo1.getExtension())){
                return archivo1; 
            }
        }
        return null;
    }
    
    public Directorio chequearPorMismaCarpeta(Directorio dir){
        //cuando en la interfaz quiere agregar algo llamar a esta funcion para saber si 
        //quiere reemplazarlo o no, si no quiere preguntarle por nuevo nombre
        String nombre = dir.getNombre(); 
        for (Directorio directorio : directorios) {
            if(nombre.equals(directorio.getNombre())){
                return directorio; 
            }
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacionDireccion() {
        return ubicacionDireccion;
    }

    public void setUbicacionDireccion(String ubicacionDireccion) {
        this.ubicacionDireccion = ubicacionDireccion;
    }
    
    
    public void quitarArchivo(Archivo arc){
        archivos.remove(arc); 
    }
    
    public void quitarCarpeta(Directorio dir){
        directorios.remove(dir); 
    }

    @Override
    public String toString() {
        return "Directorio{" + "nombre=" + nombre + ", ubicacionDireccion=" + ubicacionDireccion + ", archivos=" + archivos + ", directorios=" + directorios + '}';
    }
    
}
