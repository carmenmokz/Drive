/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author bermu
 */
public class Archivo {
    String nombre; 
    String extension; 
    String contenido; 
    int tamanio; 

    public Archivo(String nombre, String extension, String contenido) {
        this.nombre = nombre;
        this.extension = extension;
        this.contenido = contenido;
        tamanio = contenido.length(); 
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "Archivo{" + "nombre=" + nombre + ", extension=" + extension + ", contenido=" + contenido + ", tamanio=" + tamanio + '}';
    }
 
    
}
