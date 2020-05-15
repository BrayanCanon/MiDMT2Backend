/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "tipo_encuesta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEncuesta.findAll", query = "SELECT t FROM TipoEncuesta t")
    , @NamedQuery(name = "TipoEncuesta.findByIdTipoEncuesta", query = "SELECT t FROM TipoEncuesta t WHERE t.idTipoEncuesta = :idTipoEncuesta")
    , @NamedQuery(name = "TipoEncuesta.findByNomTipoEncuesta", query = "SELECT t FROM TipoEncuesta t WHERE t.nomTipoEncuesta = :nomTipoEncuesta")
    , @NamedQuery(name = "TipoEncuesta.findByEscala", query = "SELECT t FROM TipoEncuesta t WHERE t.escala = :escala")
    , @NamedQuery(name = "TipoEncuesta.findByEstado", query = "SELECT t FROM TipoEncuesta t WHERE t.estado = :estado")})
public class TipoEncuesta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_encuesta")
    private Integer idTipoEncuesta;
    @Size(max = 30)
    @Column(name = "nom_tipo_encuesta")
    private String nomTipoEncuesta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "escala")
    private Double escala;
    @Size(max = 10)
    @Column(name = "estado")
    private String estado;
    @OneToMany(mappedBy = "idTipoEncuesta")
    private List<Encuesta> encuestaList;

    public TipoEncuesta() {
    }

    public TipoEncuesta(Integer idTipoEncuesta) {
        this.idTipoEncuesta = idTipoEncuesta;
    }

    public Integer getIdTipoEncuesta() {
        return idTipoEncuesta;
    }

    public void setIdTipoEncuesta(Integer idTipoEncuesta) {
        this.idTipoEncuesta = idTipoEncuesta;
    }

    public String getNomTipoEncuesta() {
        return nomTipoEncuesta;
    }

    public void setNomTipoEncuesta(String nomTipoEncuesta) {
        this.nomTipoEncuesta = nomTipoEncuesta;
    }

    public Double getEscala() {
        return escala;
    }

    public void setEscala(Double escala) {
        this.escala = escala;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Encuesta> getEncuestaList() {
        return encuestaList;
    }

    public void setEncuestaList(List<Encuesta> encuestaList) {
        this.encuestaList = encuestaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEncuesta != null ? idTipoEncuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEncuesta)) {
            return false;
        }
        TipoEncuesta other = (TipoEncuesta) object;
        if ((this.idTipoEncuesta == null && other.idTipoEncuesta != null) || (this.idTipoEncuesta != null && !this.idTipoEncuesta.equals(other.idTipoEncuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoEncuesta[ idTipoEncuesta=" + idTipoEncuesta + " ]";
    }
    
}
