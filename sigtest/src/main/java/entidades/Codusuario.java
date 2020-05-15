/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "codusuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Codusuario.findAll", query = "SELECT c FROM Codusuario c")
    , @NamedQuery(name = "Codusuario.findByIdUsuario", query = "SELECT c FROM Codusuario c WHERE c.idUsuario = :idUsuario")
    , @NamedQuery(name = "Codusuario.findByTerminos", query = "SELECT c FROM Codusuario c WHERE c.terminos = :terminos")
    , @NamedQuery(name = "Codusuario.findByAvances", query = "SELECT c FROM Codusuario c WHERE c.avances = :avances")
    , @NamedQuery(name = "Codusuario.findByFechaAvance", query = "SELECT c FROM Codusuario c WHERE c.fechaAvance = :fechaAvance")
    , @NamedQuery(name = "Codusuario.findByObservacion", query = "SELECT c FROM Codusuario c WHERE c.observacion = :observacion")
    , @NamedQuery(name = "Codusuario.findByFechaObservacion", query = "SELECT c FROM Codusuario c WHERE c.fechaObservacion = :fechaObservacion")})
public class Codusuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Integer idUsuario;
    @Lob
    @Size(max = 65535)
    @Column(name = "nom_usuario")
    private String nomUsuario;
    @Column(name = "terminos")
    private Boolean terminos;
    @Column(name = "avances")
    private Integer avances;
    @Column(name = "fecha_avance")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAvance;
    @Size(max = 100)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "fecha_observacion")
    @Temporal(TemporalType.DATE)
    private Date fechaObservacion;

    public Codusuario() {
    }

    public Codusuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public Boolean getTerminos() {
        return terminos;
    }

    public void setTerminos(Boolean terminos) {
        this.terminos = terminos;
    }

    public Integer getAvances() {
        return avances;
    }

    public void setAvances(Integer avances) {
        this.avances = avances;
    }

    public Date getFechaAvance() {
        return fechaAvance;
    }

    public void setFechaAvance(Date fechaAvance) {
        this.fechaAvance = fechaAvance;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaObservacion() {
        return fechaObservacion;
    }

    public void setFechaObservacion(Date fechaObservacion) {
        this.fechaObservacion = fechaObservacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Codusuario)) {
            return false;
        }
        Codusuario other = (Codusuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Codusuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
