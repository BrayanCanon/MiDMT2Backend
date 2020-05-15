/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.DTO;

import entidades.Categoria;
import entidades.Mision;
import entidades.TipoMision;

/**
 *
 * @author Chaly
 */
public class MisionDTO {
 
    private Integer idMision;
   
    private String nombre;
    
    private String estado;
    
    private String descripcion;
    
    //private Categoria idCategoria;
  
    //private TipoMision idTipoMision;

    public MisionDTO() {
    }   
    
    public MisionDTO(Mision mis) {
        this.idMision = mis.getIdMision();
        this.nombre = mis.getNombre();
        this.estado = mis.getEstado();
        this.descripcion = mis.getDescripcion();
        //this.idCategoria = mis.getIdCategoria();
        //this.idTipoMision = mis.getIdTipoMision();
    }
    
    
    public Integer getIdMision() {
        return idMision;
    }

    public void setIdMision(Integer idMision) {
        this.idMision = idMision;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

//    public Categoria getIdCategoria() {
//        return idCategoria;
//    }
//
//    public void setIdCategoria(Categoria idCategoria) {
//        this.idCategoria = idCategoria;
//    }
//
//    public TipoMision getIdTipoMision() {
//        return idTipoMision;
//    }
//
//    public void setIdTipoMision(TipoMision idTipoMision) {
//        this.idTipoMision = idTipoMision;
//    }
//   
    
}
