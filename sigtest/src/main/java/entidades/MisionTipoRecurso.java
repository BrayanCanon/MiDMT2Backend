/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "mision_tipo_recurso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MisionTipoRecurso.findAll", query = "SELECT m FROM MisionTipoRecurso m")
    , @NamedQuery(name = "MisionTipoRecurso.findByIdMisionTipor", query = "SELECT m FROM MisionTipoRecurso m WHERE m.idMisionTipor = :idMisionTipor")
    , @NamedQuery(name = "MisionTipoRecurso.findByIdTipoRecurso", query = "SELECT m FROM MisionTipoRecurso m WHERE m.idTipoRecurso = :idTipoRecurso")
    , @NamedQuery(name = "MisionTipoRecurso.findByIdRecurso", query = "SELECT m FROM MisionTipoRecurso m WHERE m.idRecurso = :idRecurso")
    , @NamedQuery(name = "MisionTipoRecurso.findByEstado", query = "SELECT m FROM MisionTipoRecurso m WHERE m.estado = :estado")})
public class MisionTipoRecurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mision_tipor")
    private Integer idMisionTipor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_recurso")
    private int idTipoRecurso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_recurso")
    private int idRecurso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "id_mision", referencedColumnName = "id_mision")
    @ManyToOne(optional = false)
    private Mision idMision;

    public MisionTipoRecurso() {
    }

    public MisionTipoRecurso(Integer idMisionTipor) {
        this.idMisionTipor = idMisionTipor;
    }

    public MisionTipoRecurso(Integer idMisionTipor, int idTipoRecurso, int idRecurso, String estado) {
        this.idMisionTipor = idMisionTipor;
        this.idTipoRecurso = idTipoRecurso;
        this.idRecurso = idRecurso;
        this.estado = estado;
    }

    public Integer getIdMisionTipor() {
        return idMisionTipor;
    }

    public void setIdMisionTipor(Integer idMisionTipor) {
        this.idMisionTipor = idMisionTipor;
    }

    public int getIdTipoRecurso() {
        return idTipoRecurso;
    }

    public void setIdTipoRecurso(int idTipoRecurso) {
        this.idTipoRecurso = idTipoRecurso;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Mision getIdMision() {
        return idMision;
    }

    public void setIdMision(Mision idMision) {
        this.idMision = idMision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMisionTipor != null ? idMisionTipor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MisionTipoRecurso)) {
            return false;
        }
        MisionTipoRecurso other = (MisionTipoRecurso) object;
        if ((this.idMisionTipor == null && other.idMisionTipor != null) || (this.idMisionTipor != null && !this.idMisionTipor.equals(other.idMisionTipor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.MisionTipoRecurso[ idMisionTipor=" + idMisionTipor + " ]";
    }
    
}
