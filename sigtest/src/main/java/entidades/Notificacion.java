/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "notificacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notificacion.findAll", query = "SELECT n FROM Notificacion n")
    , @NamedQuery(name = "Notificacion.findByIdnotificacion", query = "SELECT n FROM Notificacion n WHERE n.idnotificacion = :idnotificacion")
    , @NamedQuery(name = "Notificacion.findByFecha", query = "SELECT n FROM Notificacion n WHERE n.fecha = :fecha")
    , @NamedQuery(name = "Notificacion.findByCodRecurso", query = "SELECT n FROM Notificacion n WHERE n.codRecurso = :codRecurso")
    , @NamedQuery(name = "Notificacion.findByEstado", query = "SELECT n FROM Notificacion n WHERE n.estado = :estado")})
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_notificacion")
    @Expose
    private Integer idnotificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
        @Expose
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cod_recurso")
        @Expose
    private String codRecurso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
        @Expose
    private String estado;
    @JoinColumn(name = "id_tipo_recurso", referencedColumnName = "id_tipo_recurso")
    @ManyToOne(optional = false)
        @Expose
    private TipoRecurso idTipoRecurso;
    @JoinColumn(name = "nom_usuario", referencedColumnName = "nom_usuario")
    @Expose
    @ManyToOne(optional = false)
    
    private Usuario nomUsuario;
        

    public Notificacion() {
    }

    public Notificacion(Integer idnotificacion) {
        this.idnotificacion = idnotificacion;
    }

    public Notificacion(Integer idnotificacion, Date fecha, String codRecurso, String estado) {
        this.idnotificacion = idnotificacion;
        this.fecha = fecha;
        this.codRecurso = codRecurso;
        this.estado = estado;
    }

    public Integer getIdnotificacion() {
        return idnotificacion;
    }

    public void setIdnotificacion(Integer idnotificacion) {
        this.idnotificacion = idnotificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodRecurso() {
        return codRecurso;
    }

    public void setCodRecurso(String codRecurso) {
        this.codRecurso = codRecurso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoRecurso getIdTipoRecurso() {
        return idTipoRecurso;
    }

    public void setIdTipoRecurso(TipoRecurso idTipoRecurso) {
        this.idTipoRecurso = idTipoRecurso;
    }

    public Usuario getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(Usuario nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idnotificacion != null ? idnotificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.idnotificacion == null && other.idnotificacion != null) || (this.idnotificacion != null && !this.idnotificacion.equals(other.idnotificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Notificacion[ idnotificacion=" + idnotificacion + " ]";
    }
    
}
