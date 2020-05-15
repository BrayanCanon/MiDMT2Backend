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
@Table(name = "tipo_mision")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoMision.findAll", query = "SELECT t FROM TipoMision t")
    , @NamedQuery(name = "TipoMision.findByIdTipoMision", query = "SELECT t FROM TipoMision t WHERE t.idTipoMision = :idTipoMision")
    , @NamedQuery(name = "TipoMision.findByNombreTipoMision", query = "SELECT t FROM TipoMision t WHERE t.nombreTipoMision = :nombreTipoMision")
    , @NamedQuery(name = "TipoMision.findByEstado", query = "SELECT t FROM TipoMision t WHERE t.estado = :estado")})
public class TipoMision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_mision")
    @Expose
    private Integer idTipoMision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_tipo_mision")
    @Expose
    private String nombreTipoMision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @OneToMany(mappedBy = "idTipoMision")
    private List<Mision> misionList;

    public TipoMision() {
    }

    public TipoMision(Integer idTipoMision) {
        this.idTipoMision = idTipoMision;
    }

    public TipoMision(Integer idTipoMision, String nombreTipoMision, String estado) {
        this.idTipoMision = idTipoMision;
        this.nombreTipoMision = nombreTipoMision;
        this.estado = estado;
    }

    public Integer getIdTipoMision() {
        return idTipoMision;
    }

    public void setIdTipoMision(Integer idTipoMision) {
        this.idTipoMision = idTipoMision;
    }

    public String getNombreTipoMision() {
        return nombreTipoMision;
    }

    public void setNombreTipoMision(String nombreTipoMision) {
        this.nombreTipoMision = nombreTipoMision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Mision> getMisionList() {
        return misionList;
    }

    public void setMisionList(List<Mision> misionList) {
        this.misionList = misionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoMision != null ? idTipoMision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoMision)) {
            return false;
        }
        TipoMision other = (TipoMision) object;
        if ((this.idTipoMision == null && other.idTipoMision != null) || (this.idTipoMision != null && !this.idTipoMision.equals(other.idTipoMision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoMision[ idTipoMision=" + idTipoMision + " ]";
    }
    
}
