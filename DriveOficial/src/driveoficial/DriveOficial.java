/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driveoficial;

import model.Usuario;

/**
 *
 * @author bermu
 */
public class DriveOficial {

    /**
     * @param args the command line arguments
     */
    public static void prueba1(){
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        user.getFileSystem().cambiarDirActual("D");
        user.getFileSystem().eliminarDirectorio("Compartido");
        System.out.println(user.toString());
    }
    
    public static void prueba2(){
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        user.getFileSystem().crearDirectorio("Me miau"); 
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().verificacionVirtual_a_Real("./CUAL"); 
        user.getFileSystem().verificacionVirtual_a_Real("../"); 
        user.getFileSystem().verificacionVirtual_a_Real("D/Compartido/CUAL"); 
        System.out.println(user.toString());
    }
    public static void main(String[] args) {
        // TODO code application logic here
        prueba2(); 
        
    }
    
    
    
}
