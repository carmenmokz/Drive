/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driveoficial;

import model.Directorio;
import model.Archivo; 

/**
 *
 * @author bermu
 */
public class DriveOficial {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Directorio dir = new Directorio("prueba"); 
        Archivo arc = new Archivo("holi", "quak", "el patito lleva caquita express"); 
        Directorio dir2 = new Directorio("pruebaw"); 
        Archivo arc2 = new Archivo("adios", "buuu", "el patito no lleva caquita express"); 
        dir.agregarArchivo(arc); 
        dir2.agregarArchivo(arc2); 
        dir.agregarDirectorio(dir2); 
        System.out.println(dir.toString());
        
    }
    
}
