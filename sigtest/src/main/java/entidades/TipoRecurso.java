/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "tipo_recurso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRecurso.findAll", query = "SELECT t FROM TipoRecurso t")
    , @NamedQuery(name = "TipoRecurso.findByIdTipoRecurso", query = "SELECT t FROM TipoRecurso t WHERE t.idTipoRecurso = :idTipoRecurso")
    , @NamedQuery(name = "TipoRecurso.findByNombreRecurso", query = "SELECT t FROM TipoRecurso t WHERE t.nombreRecurso = :nombreRecurso")
    , @NamedQuery(name = "TipoRecurso.findByMensajeNotificacion", query = "SELECT t FROM TipoRecurso t WHERE t.mensajeNotificacion = :mensajeNotificacion")
    , @NamedQuery(name = "TipoRecurso.findByEstado", query = "SELECT t FROM TipoRecurso t WHERE t.estado = :estado")})
public class TipoRecurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_recurso")
    @Expose
    private Integer idTipoRecurso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_recurso")
      @Expose
    private String nombreRecurso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "mensaje_notificacion")
      @Expose
    private String mensajeNotificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
      @Expose
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoRecurso")
    private List<Notificacion> notificacionList;

    public TipoRecurso() {
    }

    public TipoRecurso(Integer idTipoRecurso) {
        this.idTipoRecurso = idTipoRecurso;
    }

    public TipoRecurso(Integer idTipoRecurso, String nombreRecurso, String mensajeNotificacion, String estado) {
        this.idTipoRecurso = idTipoRecurso;
        this.nombreRecurso = nombreRecurso;
        this.mensajeNotificacion = mensajeNotificacion;
        this.estado = estado;
    }

    public Integer getIdTipoRecurso() {
        return idTipoRecurso;
    }

    public void setIdTipoRecurso(Integer idTipoRecurso) {
        this.idTipoRecurso = idTipoRecurso;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    public String getMensajeNotificacion() {
        return mensajeNotificacion;
    }

    public void setMensajeNotificacion(String mensajeNotificacion) {
        this.mensajeNotificacion = mensajeNotificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Notificacion> getNotificacionList() {
        return notificacionList;
    }

    public void setNotificacionList(List<Notificacion> notificacionList) {
        this.notificacionList = notificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoRecurso != null ? idTipoRecurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoRecurso)) {
            return false;
        }
        TipoRecurso other = (TipoRecurso) object;
        if ((this.idTipoRecurso == null && other.idTipoRecurso != null) || (this.idTipoRecurso != null && !this.idTipoRecurso.equals(other.idTipoRecurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoRecurso[ idTipoRecurso=" + idTipoRecurso + " ]";
    }
    
}
