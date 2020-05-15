/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "gforms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gforms.findAll", query = "SELECT g FROM Gforms g")
    , @NamedQuery(name = "Gforms.findByIdGform", query = "SELECT g FROM Gforms g WHERE g.idGform = :idGform")
    , @NamedQuery(name = "Gforms.findByEstado", query = "SELECT g FROM Gforms g WHERE g.estado = :estado")})
public class Gforms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_gform")
    @Expose
    private Integer idGform;
    @Lob
    @Size(max = 65535)
    @Column(name = "titulo")
     @Expose
    private String titulo;
    @Lob
    @Size(max = 65535)
    @Column(name = "enlace")
     @Expose
    private String enlace;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
     @Expose
    private String descripcion;
    @Size(max = 1)
    @Column(name = "estado")
     @Expose
    private String estado;

    public Gforms() {
    }

    public Gforms(Integer idGform) {
        this.idGform = idGform;
    }

    public Integer getIdGform() {
        return idGform;
    }

    public void setIdGform(Integer idGform) {
        this.idGform = idGform;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGform != null ? idGform.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gforms)) {
            return false;
        }
        Gforms other = (Gforms) object;
        if ((this.idGform == null && other.idGform != null) || (this.idGform != null && !this.idGform.equals(other.idGform))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Gforms[ idGform=" + idGform + " ]";
    }
    
}
