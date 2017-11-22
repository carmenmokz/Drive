/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Archivo;
import model.Directorio;
import model.Directorio;
import model.Usuario;

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

    public String getNombreRaiz() {
        return nombreRaiz;
    }

    public void setNombreRaiz(String nombreRaiz) {
        this.nombreRaiz = nombreRaiz;
    }

    public int getConsumido() {
        return consumido;
    }

    public void setConsumido(int consumido) {
        this.consumido = consumido;
    }

    public int getLimiteTamanio() {
        return limiteTamanio;
    }

    public void setLimiteTamanio(int limiteTamanio) {
        this.limiteTamanio = limiteTamanio;
    }

    public Directorio getDir() {
        return dir;
    }

    public void setDir(Directorio dir) {
        this.dir = dir;
    }

    public String getDirActual() {
        return dirActual;
    }

    public void setDirActual(String dirActual) {
        this.dirActual = dirActual;
    }
    
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
        String dirOriginal = dirActual; 
        cambiarDirActual(dirActual + "/" + dir.getNombre());
        ArrayList<Archivo> archivos = dir.getArchivos(); 
        for (int i = 0; i < archivos.size(); i++){
            System.out.println("!");
            eliminarArchivo(archivos.get(i).getNombre(), archivos.get(i).getExtension()); 
            System.out.println("?");
            archivos = dir.getArchivos();
        }
        cambiarDirActual(dirOriginal);
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
    
    public Archivo conseguirArchivo(String directorio, String nombreArchExt){
        Directorio dirBase = encontrarDirectorio(directorio); 
        String[] nombreArcStrings = nombreArchExt.split("\\.");
        String nombreArch = "";
        String extension = nombreArcStrings[nombreArcStrings.length-1]; 
        for (int i = 0; i < nombreArcStrings.length-1; i++) {
            nombreArch += nombreArcStrings[i]; 
        }
        ArrayList<Archivo> archivos = dirBase.getArchivos(); 
        for (Archivo archivo : archivos) {
            if(archivo.getNombre().equals(nombreArch) && archivo.getExtension().equals(extension)){
                return archivo;
            }
        }
        return null; 
    }
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
    public boolean copiarVR(String dirVirtual, String dirReal){
        String dirOficial = verificacionVirtual_a_Real(dirVirtual); 
        String[] dirOficialArray = dirOficial.split("\\/"); 
        String nombre = dirOficialArray[dirOficialArray.length-1]; 
        String dirA = "";
        for (int i = 0; i < dirOficialArray.length-1; i++) {
            dirA += dirOficialArray[i]; 
        }
        boolean existeArc = existeArchEnDir(dirA, nombre); 
        boolean existeDir = existeDirEnDir(dirA, nombre); 
        if(existeArc && existeDir){
            Archivo arc = conseguirArchivo(dirA, nombre); 
            return (copiarVRArchivo(arc, dirReal) & copiarVRDirectorio(dirVirtual, dirReal));
        }
        else if(existeArc){
            Archivo arc = conseguirArchivo(dirA, nombre); 
            return copiarVRArchivo(arc, dirReal); 
        }
        else if(existeDir){
            return copiarVRDirectorio(dirVirtual, dirReal); 
        }
        return false;
    }   
    public boolean copiarVR(String dirVirtual, String dirReal, int type){
        String dirOficial = verificacionVirtual_a_Real(dirVirtual); 
        String[] dirOficialArray = dirOficial.split("\\/"); 
        String nombre = dirOficialArray[dirOficialArray.length-1]; 
        String dirA = ""; // es el directorio donde va a estar lo nuevo
        for (int i = 0; i < dirOficialArray.length-1; i++) {
            dirA += dirOficialArray[i]; 
        }
       
        if(type == 0 && existeDirEnDir(dirA, nombre)){
            return copiarVRDirectorio(dirVirtual, dirReal); 
        }
        else if(type == 1 && existeArchEnDir(dirA, nombre)){
            Archivo arc = conseguirArchivo(dirA, nombre);
            return copiarVRArchivo(arc, dirReal); 
        }
        return false;
    }
    public boolean copiarVRArchivo(Archivo a, String dirReal) {
        Directorio dirBase = encontrarDirectorio(dirActual); 
        try {
            String dirNomArchivo = dirReal + "/" + a.getNombre() + "." + a.getExtension(); 
            PrintWriter escritor = new PrintWriter(dirNomArchivo, "UTF-8"); 
            escritor.println(a.getContenido());
            escritor.close();
            return true; 
        } catch (Exception e) {
            return false; 
        }
    }
    public boolean copiarVRArchivos(String dirReal) {
        boolean exito = false; 
        Directorio dirBase = encontrarDirectorio(dirActual); 
        ArrayList<Directorio> directorios = dirBase.getDirectorios(); 
        ArrayList<Archivo> archivos = dirBase.getArchivos(); 
        for (Directorio directorio : directorios) {
            copiarVRDirectorio(dirActual+"/"+directorio.getNombre(), dirReal); 
        }
        for (Archivo archivo : archivos) {
            copiarVRArchivo(archivo, dirReal); 
        }
        exito = true; 
        return exito; 
    }
    public boolean copiarVRDirectorio(String dirVirtual, String dirReal) {
        String dirOriginal = dirActual;
        cambiarDirActual(dirVirtual);
        Directorio dirNuevo = encontrarDirectorio(dirActual); 
        String dirRealNueva = dirReal+"\\"+dirNuevo.getNombre();
        File carpeta = new File(dirRealNueva);
        carpeta.mkdirs(); 
        boolean exito = copiarVRArchivos(dirRealNueva); 
        cambiarDirActual(dirOriginal);
        return exito;
    }
    public boolean copiarVV(String dirVirtualDe, String dirVirtualA){
        String dirOficial = verificacionVirtual_a_Real(dirVirtualDe);
        String dirFinal = verificacionVirtual_a_Real(dirVirtualA);
        String[] dirOficialArray = dirOficial.split("\\/"); 
        String nombre = dirOficialArray[dirOficialArray.length-1]; 
        String dirA = "";
        for (int i = 0; i < dirOficialArray.length-1; i++) {
            dirA += dirOficialArray[i]; 
        }
        boolean existeArc = existeArchEnDir(dirA, nombre); 
        boolean existeDir = existeDirEnDir(dirA, nombre); 
        if(existeArc){
            Archivo arc = conseguirArchivo(dirA, nombre); 
            return copiarVVArchivo(arc, dirFinal); 
        }
        else if(existeDir){
            return copiarVVDirectorio(dirOficial, dirFinal); 
        }
        return false;
    }
    
    public boolean copiarVV(String dirVirtualDe, String dirVirtualA, int tipo){
        String dirOficial = verificacionVirtual_a_Real(dirVirtualDe);
        String dirFinal = verificacionVirtual_a_Real(dirVirtualA);
        String[] dirOficialArray = dirOficial.split("\\/"); 
        String nombre = dirOficialArray[dirOficialArray.length-1]; 
        String dirA = "";
        for (int i = 0; i < dirOficialArray.length-1; i++) {
            dirA += dirOficialArray[i]; 
        }
        if(tipo == 0){
            Archivo arc = conseguirArchivo(dirA, nombre); 
            return copiarVVArchivo(arc, dirFinal); 
        }
        else if(tipo == 1){
            return copiarVVDirectorio(dirOficial, dirFinal); 
        }
        return false;
    }
    public boolean copiarVVArchivo(Archivo arc, String dirFinal){
        String dirOriginal = dirActual; 
        cambiarDirActual(dirFinal);
        boolean exito = crearArchivo(arc.getNombre(), arc.getExtension(), arc.getContenido()); 
        cambiarDirActual(dirOriginal);
        return exito; 
    }
    public boolean copiarVVArchivos(String dirInicio, String dirFinal){
        Directorio dirACopiar = encontrarDirectorio(dirInicio);
        ArrayList<Directorio> directorios = dirACopiar.getDirectorios(); 
        for (Directorio directorio : directorios) {
            boolean exito = copiarVVDirectorio(dirInicio + "/" +directorio.getNombre(), dirFinal);
            if(!exito){System.out.println("error copiando dir en copiarVV");return false;}
        }
        ArrayList<Archivo> archivos = dirACopiar.getArchivos();
        for (Archivo archivo : archivos) {
            boolean exito = copiarVVArchivo(archivo, dirFinal);
            if(!exito){System.out.println("error copiando archivo en copiarVV");return false;}
        }
        return true; 
    }
    public boolean copiarVVDirectorio(String dirInicio, String dirFinal){
        System.out.println("jejejee");
        Directorio dirInicioD = encontrarDirectorio(dirInicio); 
        Directorio dirFinalD = encontrarDirectorio(dirFinal); 
        if(dirInicioD != null && dirFinalD != null){
            String dirOriginal = dirActual; 
            cambiarDirActual(dirFinal);
            crearDirectorio(dirInicioD.getNombre());
            System.out.println("crear dir de:" + dirInicioD.getNombre());
            cambiarDirActual(dirOriginal);
            copiarVVArchivos(dirInicio,dirFinal+"/"+dirInicioD.getNombre()); 
            return true; 
        }
        return false;
    }
    public boolean eliminarArchivo(String nombre, String extension){
        Directorio dir = encontrarDirectorio(dirActual); 
        ArrayList<Archivo> archivos = dir.getArchivos();
        for (Archivo archivo : archivos) {
            if(archivo.getNombre().equals(nombre) && archivo.getExtension().equals(extension)){
                
                System.out.println(archivo.toString());
                System.out.println("consumido " + consumido);
                consumido = consumido - archivo.getTamanio(); 
                System.out.println("consumido 2" + consumido);
                dir.quitarArchivo(archivo);
                System.out.println("esto es lo que tira eeror");
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
        System.out.println("string solicitado: " + directorioSolicitado);
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
    
    public boolean existeDirEnDir(String directorio, String dirABuscar){
        Directorio dirBase = encontrarDirectorio(directorio); 
        ArrayList<Directorio> directorios = dirBase.getDirectorios(); 
        for (Directorio directorio1 : directorios) {
            if(directorio1.getNombre().equals(dirABuscar)){
                return true; 
            }
        }
        return false; 
    }
    
    public boolean existeArchEnDir(String directorio, String nombreArchExt){
        Directorio dirBase = encontrarDirectorio(directorio); 
        String[] nombreArcStrings = nombreArchExt.split("\\.");
        String nombreArch = "";
        String extension = nombreArcStrings[nombreArcStrings.length-1]; 
        for (int i = 0; i < nombreArcStrings.length-1; i++) {
            nombreArch += nombreArcStrings[i]; 
        }
        ArrayList<Archivo> archivos = dirBase.getArchivos(); 
        for (Archivo archivo : archivos) {
            if(archivo.getNombre().equals(nombreArch) && archivo.getExtension().equals(extension)){
                return true; 
            }
        }
        return false; 
    }
    
    public boolean moverArchivo(String dirOrigen, String dirFinal){
        String dirNuevo = conseguirPadre(verificacionVirtual_a_Real(dirOrigen));
        String nombreArch = conseguirUltimo(dirOrigen); 
        String vFinal = verificacionVirtual_a_Real(dirFinal); 
        Directorio dNuevo = encontrarDirectorio(dirNuevo); 
        Directorio dFinal = encontrarDirectorio(vFinal); 
        if(dNuevo == null && dFinal == null){
            return false;
        }
        Archivo arc = conseguirArchivo(dirNuevo, nombreArch); 
        copiarVVArchivo(arc, vFinal); 
        cambiarDirActual(dirNuevo);
        eliminarArchivo(arc.getNombre(), arc.getExtension()); 
        return true; 
    }
    public boolean moverCarpeta(String dirOrigen, String dirFinal){
        String vOrigen = verificacionVirtual_a_Real(dirOrigen); 
        String vFinal = verificacionVirtual_a_Real(dirFinal); 
        Directorio origen = encontrarDirectorio(vOrigen);
        Directorio finalD = encontrarDirectorio(vFinal); 
        if(origen == null || finalD == null){
            return false;
        }
        copiarVV(vOrigen, vFinal, 1);
        System.out.println("vOrigen: " + conseguirPadre(vOrigen) + "-" + conseguirUltimo(vOrigen));
        cambiarDirActual(conseguirPadre(vOrigen));
        eliminarDirectorio(conseguirUltimo(vOrigen)); 
        return true; 
    }
    
    public String conseguirPadre(String dir){
        String[] dirArray = dir.split("\\/"); 
        String papa = ""; 
        for (int i = 0; i < dirArray.length-1; i++) {
            if(papa.equals("")){
                 papa += dirArray[i];
            }
            else{
                 papa += "/" + dirArray[i];
            }
        }
        return papa;
    }
    public String conseguirUltimo(String dir){
        String[] dirArray = dir.split("\\/"); 
        return dirArray[dirArray.length-1]; 
    }
    public boolean modificarArchivo(String nombreCompleto, String contenido){
        Archivo arc = conseguirArchivo(dirActual, nombreCompleto); 
        if(arc == null){return false;}
        arc.setContenido(contenido);
        arc.setModificacion(new Date());
        return true; 
    }
    
    public String enlistar(String dir){
        Directorio dirOficial = encontrarDirectorio(verificacionVirtual_a_Real(dir)); 
        if(dirOficial==null){return "error";}
        //aqui armar como le sirva a xime 
        return "goli"; 
    }
    
    public boolean modificarArchivoNombre(String nombreCompleto, String nombre){
        Archivo arc = conseguirArchivo(dirActual, nombreCompleto); 
        if(arc == null){return false;}
        arc.setNombre(nombre);
        arc.setModificacion(new Date());
        return true; 
    }
    
    public boolean modificarArchivoExt(String nombreCompleto, String ext){
        Archivo arc = conseguirArchivo(dirActual, nombreCompleto); 
        if(arc == null){return false;}
        arc.setExtension(ext);
        arc.setModificacion(new Date());
        return true; 
    }
    
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
    
    public void verPropiedadesArchivo(Archivo arc){
        System.out.println(arc.toString());
    }
    
    
    
    @Override
    public String toString() {
        return "RaizFS{" + "nombreRaiz=" + nombreRaiz + ", consumido=" + consumido + ", limiteTamanio=" + limiteTamanio + ", dir=" + dir + '}';
    }

   
    
    
  
  
}
    
    
