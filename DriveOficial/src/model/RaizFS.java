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
public class RaizFS {
    private String nombreRaiz; 
    private int consumido; 
    private int limiteTamanio; 
    private Directorio dir;         //a quien apunta ya para trabajar normal 
    
    public RaizFS(int maxBytes){
        nombreRaiz = "D:"; // D de DRIVE ajajjaja entienden xD kill me
        dir = new Directorio(nombreRaiz); 
        dir.agregarDirectorio(new Directorio("Personal")); 
        dir.agregarDirectorio(new Directorio("Compartido"));
        consumido = 0; 
        limiteTamanio = maxBytes; 
    }
    
    public Directorio encontrarDirectorio (String directorioSolicitado){
    /*
        @sumary: es navegar al directorio que me piden, y devuelvo el 
        directorio para que ya con eso haga lo que ocupa, copiar, mover, etc
    */
        
    String[] carpetasDeDirSolicitado = directorioSolicitado.split("\\");            // son las carpetas de la direccion separadas
    Directorio temp = dir;                                                          // se situa en la pura raiz, con ese temp vamos a navegar
    if(carpetasDeDirSolicitado.length == 1 && carpetasDeDirSolicitado[0].equals(dir.getNombre())){
        return dir; 
    }
    else{
        for (String string : carpetasDeDirSolicitado) {
            ArrayList<Directorio> directorios = temp.getDirectorios(); 
            for (Directorio directorio : directorios) {
                if(directorio.getNombre().equals(string)){
                    int indice = directorios.indexOf(directorio); 
                    if(indice == (carpetasDeDirSolicitado.length - 1))
                    {
                        return temp.getDirectorios().get(indice); 
                    }
                    else{
                        temp = temp.getDirectorios().get(indice); 
                    }
                }
            }
        }
        return null;
    }
    
    
    }
    
    
}
