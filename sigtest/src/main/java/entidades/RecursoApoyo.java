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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "recurso_apoyo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecursoApoyo.findAll", query = "SELECT r FROM RecursoApoyo r")
    , @NamedQuery(name = "RecursoApoyo.findByIdRecapoyo", query = "SELECT r FROM RecursoApoyo r WHERE r.idRecapoyo = :idRecapoyo")
    , @NamedQuery(name = "RecursoApoyo.findByTituloRec", query = "SELECT r FROM RecursoApoyo r WHERE r.tituloRec = :tituloRec")
    , @NamedQuery(name = "RecursoApoyo.findByContenidoApoyo", query = "SELECT r FROM RecursoApoyo r WHERE r.contenidoApoyo = :contenidoApoyo")
    , @NamedQuery(name = "RecursoApoyo.findByEstado", query = "SELECT r FROM RecursoApoyo r WHERE r.estado = :estado")
    , @NamedQuery(name = "RecursoApoyo.findByFecha", query = "SELECT r FROM RecursoApoyo r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "RecursoApoyo.findByImagen", query = "SELECT r FROM RecursoApoyo r WHERE r.imagen = :imagen")
    , @NamedQuery(name = "RecursoApoyo.findByVideo", query = "SELECT r FROM RecursoApoyo r WHERE r.video = :video")})
public class RecursoApoyo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_recapoyo")
    @Expose
    private Integer idRecapoyo;
    @Size(max = 30)
    @Column(name = "titulo_rec")
    @Expose
    private String tituloRec;
    @Size(max = 500)
    @Column(name = "contenido_apoyo")
    @Expose
    private String contenidoApoyo;
    @Size(max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    @Expose
    private Date fecha;
    @Size(max = 100)
    @Column(name = "imagen")
    @Expose
    private String imagen;
    @Size(max = 100)
    @Column(name = "video")
    @Expose
    private String video;
    @JoinColumn(name = "nom_usuario", referencedColumnName = "nom_usuario")
    @ManyToOne
    @Expose
    private Usuario nomUsuario;

    public RecursoApoyo() {
    }

    public RecursoApoyo(Integer idRecapoyo) {
        this.idRecapoyo = idRecapoyo;
    }

    public Integer getIdRecapoyo() {
        return idRecapoyo;
    }

    public void setIdRecapoyo(Integer idRecapoyo) {
        this.idRecapoyo = idRecapoyo;
    }

    public String getTituloRec() {
        return tituloRec;
    }

    public void setTituloRec(String tituloRec) {
        this.tituloRec = tituloRec;
    }

    public String getContenidoApoyo() {
        return contenidoApoyo;
    }

    public void setContenidoApoyo(String contenidoApoyo) {
        this.contenidoApoyo = contenidoApoyo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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
        hash += (idRecapoyo != null ? idRecapoyo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecursoApoyo)) {
            return false;
        }
        RecursoApoyo other = (RecursoApoyo) object;
        if ((this.idRecapoyo == null && other.idRecapoyo != null) || (this.idRecapoyo != null && !this.idRecapoyo.equals(other.idRecapoyo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.RecursoApoyo[ idRecapoyo=" + idRecapoyo + " ]";
    }
    
}
