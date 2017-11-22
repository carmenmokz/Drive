/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.RaizFS;

/**
 *
 * @author bermu
 */
public class Usuario {
    private RaizFS FileSystem; 
    private String username; 
    private String password; 

    public Usuario(String username, String password, int bytes) {
        this.FileSystem = new RaizFS(bytes); 
        this.username = username;
        this.password = password;
    }

    public RaizFS getFileSystem() {
        return FileSystem;
    }
        
    @Override
    public String toString() {
        return "Usuario{" + "FileSystem=" + FileSystem + ", username=" + username + ", password=" + password + '}';
    }
    
    
   
    
}
