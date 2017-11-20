/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driveoficial;

import model.Directorio;
import model.Archivo; 
import model.Usuario;

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
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        user.getFileSystem().cambiarDirActual("D");
        user.getFileSystem().eliminarDirectorio("Compartido");
        System.out.println(user.toString());
        
    }
    
}
