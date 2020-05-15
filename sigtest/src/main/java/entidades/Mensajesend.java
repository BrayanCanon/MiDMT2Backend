/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author estebn
 */
public class Mensajesend {
    public String mensaje;
    public String fecha;
    public String destinatario;
    public String usuario;
    public String remitente;

    public Mensajesend(String mensaje, String fecha, String destinatario, String usuario,String remitente) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.destinatario = destinatario;
        this.usuario = usuario;
        this.remitente=remitente;
    }
    public Mensajesend() {
       
    }

    
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void setRemitente(String remitente){
        this.remitente=remitente;
    }
    public String getRemitente(){
      return remitente;
    }
}
