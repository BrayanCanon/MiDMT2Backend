/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.DTO;

import entidades.Mision;
import entidades.Nivel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chaly
 */
public class NivelDTO {

    private Integer idNivel;
  
    private String nombre;
   
    private String estado;
   
    private List<MisionDTO> misionList;

    public NivelDTO() {
    }   
    

    public NivelDTO(Nivel nivel) {
        this.idNivel = nivel.getIdNivel();
        this.nombre = nivel.getNombre();
        this.estado = nivel.getEstado();
        List<MisionDTO> misionDTOlist = new ArrayList<>();
        for (Mision mision : nivel.getMisionList()) {
           misionDTOlist.add(new MisionDTO(mision));
        }
        this.misionList = misionDTOlist;
    }
   
    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<MisionDTO> getMisionList() {
        return misionList;
    }

    public void setMisionList(List<MisionDTO> misionList) {
        this.misionList = misionList;
    }
    
    

}
