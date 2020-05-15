/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chaly
 */
public class VerficacionApoyoSocialDTO {

    private int idVerificacion;
    private String nombreMision;
    private String nombrePaso;
    private String fecha;

    public VerficacionApoyoSocialDTO() {
    }

    public List<VerficacionApoyoSocialDTO> modeloADTO(List<Object[]> listado) {
        List<VerficacionApoyoSocialDTO> listadoDTO = new ArrayList<>();
        for (Object[] verificacion : listado) {
            VerficacionApoyoSocialDTO veri = new VerficacionApoyoSocialDTO();
            veri.idVerificacion = Integer.parseInt(verificacion[0].toString());
            veri.nombreMision = verificacion[1].toString();
            veri.nombrePaso = verificacion[2].toString();
            veri.fecha = verificacion[3].toString();
            listadoDTO.add(veri);
        }
        return listadoDTO;
    }

    public int getIdVerificacion() {
        return idVerificacion;
    }

    public void setIdVerificacion(int idVerificacion) {
        this.idVerificacion = idVerificacion;
    }

    public String getNombreMision() {
        return nombreMision;
    }

    public void setNombreMision(String nombreMision) {
        this.nombreMision = nombreMision;
    }

    public String getNombrePaso() {
        return nombrePaso;
    }

    public void setNombrePaso(String nombrePaso) {
        this.nombrePaso = nombrePaso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
