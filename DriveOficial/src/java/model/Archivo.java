/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author bermu
 */
public class Archivo {
    private String nombre; 
    private String extension; 
    private String contenido; 
    private int tamanio; 
    private Date creacion; 
    private Date modificacion; 

    public Archivo(String nombre, String extension, String contenido) {
        this.nombre = nombre;
        this.extension = extension;
        this.contenido = contenido;
        tamanio = contenido.length(); 
        Date dia = new Date();          //lo hago asi para que tengan el mismo milisegundo
        creacion = dia; 
        modificacion = dia; 
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

    public Date getCreacion() {
        return creacion;
    }


    public Date getModificacion() {
        return modificacion;
    }

    public void setModificacion(Date modificacion) {
        this.modificacion = modificacion;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    
    
    
    @Override
    public String toString() {
        return "Archivo{" + "nombre=" + nombre + ", extension=" + extension + ", contenido=" + contenido + ", tamanio=" + tamanio + '}';
    }
 
    
}
