/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.DTO;

import entidades.VerificacionRutinaPaciente;
import java.text.SimpleDateFormat;

/**
 *
 * @author Charly
 */
public class VerificacionRutinaDTO {

    private String nombreRutina;
    private String valorVerificacion;
    private String fecha;

    public VerificacionRutinaDTO() {
    }

    public VerificacionRutinaDTO(VerificacionRutinaPaciente verificacion) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
        if (verificacion.getIdRutinaPaciente() != null) {
            this.nombreRutina = verificacion.getIdRutinaPaciente().getIdRutina().getNombre();
        }
        this.valorVerificacion = verificacion.getValor();
        this.fecha = formato.format(verificacion.getFecha());
    }

    public String getNombreRutina() {
        return nombreRutina;
    }

    public void setNombreRutina(String nombreRutina) {
        this.nombreRutina = nombreRutina;
    }

    public String getValorVerificacion() {
        return valorVerificacion;
    }

    public void setValorVerificacion(String valorVerificacion) {
        this.valorVerificacion = valorVerificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
