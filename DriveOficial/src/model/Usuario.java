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
public class Usuario {
    private RaizFS FileSystem; 
    private String username; 
    private String password; 

    public Usuario(String username, String password, int bytes) {
        this.FileSystem = new RaizFS(bytes); 
        this.username = username;
        this.password = password;
    }
   
    
}
