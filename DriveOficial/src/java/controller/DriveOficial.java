/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
    
    public static void prueba3(){
        //copia RV
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Personal");
        user.getFileSystem().copiarRV("C:\\Users\\bermu\\Desktop\\pruebita", "D/Personal"); 
        System.out.println(user.toString());
    }
    
    public static void prueba4(){
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Personal");
        user.getFileSystem().copiarRV("C:\\Users\\bermu\\Desktop\\pruebita\\nani.txt", "D/Personal"); 
        System.out.println(user.toString());
    }
    
    public static void prueba5(){
        //existe en DIR o arch en dir
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(user.getFileSystem().existeArchEnDir("D/Compartido/CUAL", "ari.pdf"));
        System.out.println(user.getFileSystem().existeArchEnDir("D/Compartido/CUAL", "arip.pdf"));
        user.getFileSystem().cambiarDirActual("D/Compartido");
        System.out.println(user.getFileSystem().existeDirEnDir("D/Compartido", "CUAL"));
        System.out.println(user.getFileSystem().existeDirEnDir("D/Compartido", "CUALP"));
        /*
            RESPUESTA ESPERADA
            true 
            false
            true
            false
        */
    }
    
    public static void prueba6(){
        //copia VR
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(user.getFileSystem().copiarVR("D/Compartido", "C:\\Users\\bermu\\Desktop"));
     
        
        /*
            RESPUESTA ESPERADA
            true 
            false
            true
            false
        */
    }
    
    public static void prueba7(){
        //copia VV 
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(user.getFileSystem().copiarVV("D/Compartido", "D/Personal"));
        System.out.println(user.toString());
    }
    
    public static void prueba8(){
        //mover carpeta 
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(user.getFileSystem().moverCarpeta("D/Compartido/CUAL", "D/Personal"));
        System.out.println(user.toString());
    }
    
    public static void prueba9(){
        //mover archivo
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(user.getFileSystem().moverArchivo("D/Compartido/CUAL/ari.pdf", "D/Personal"));
        System.out.println(user.toString());
    }


    public static void prueba10(){
        //mod archivo
        Usuario user = new Usuario("bermudezari", "1234", 50000);
        user.getFileSystem().cambiarDirActual("D/Compartido");
        user.getFileSystem().crearDirectorio("CUAL"); 
        user.getFileSystem().cambiarDirActual("D/Compartido/CUAL");
        user.getFileSystem().crearArchivo("ari", "pdf", "salsa a la 1 am");
        System.out.println(user.getFileSystem().modificarArchivo("ari.pdf", "bueno no se que poner"));
        System.out.println(user.toString());
        System.out.println(user.getFileSystem().modificarArchivoNombre("ari.pdf", "fois"));
        System.out.println(user.toString());
        System.out.println(user.getFileSystem().modificarArchivoExt("fois.pdf", "txt"));
        System.out.println(user.toString());
    }
    public static void main(String[] args) {
        // TODO code application logic here
        prueba2(); 
        
    }
    
    
    
}
