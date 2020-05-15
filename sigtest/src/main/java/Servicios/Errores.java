/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import javax.xml.ws.WebFault;

/**
 *
 * @author daryo
 */

public class Errores extends Exception  {
 private String message;

    public String getMessage() {
        return super.getMessage();
    }

    public Errores(String message) {
        super(message);
    }

}
