/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

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
@Table(name = "tipo_indicador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoIndicador.findAll", query = "SELECT t FROM TipoIndicador t")
    , @NamedQuery(name = "TipoIndicador.findByIdTipoIndicador", query = "SELECT t FROM TipoIndicador t WHERE t.idTipoIndicador = :idTipoIndicador")
    , @NamedQuery(name = "TipoIndicador.findByNomTipoIndicador", query = "SELECT t FROM TipoIndicador t WHERE t.nomTipoIndicador = :nomTipoIndicador")
    , @NamedQuery(name = "TipoIndicador.findByEstado", query = "SELECT t FROM TipoIndicador t WHERE t.estado = :estado")})
public class TipoIndicador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_indicador")
    private Integer idTipoIndicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_tipo_indicador")
    private String nomTipoIndicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoIndicador")
    private List<Indicador> indicadorList;

    public TipoIndicador() {
    }

    public TipoIndicador(Integer idTipoIndicador) {
        this.idTipoIndicador = idTipoIndicador;
    }

    public TipoIndicador(Integer idTipoIndicador, String nomTipoIndicador, String estado) {
        this.idTipoIndicador = idTipoIndicador;
        this.nomTipoIndicador = nomTipoIndicador;
        this.estado = estado;
    }

    public Integer getIdTipoIndicador() {
        return idTipoIndicador;
    }

    public void setIdTipoIndicador(Integer idTipoIndicador) {
        this.idTipoIndicador = idTipoIndicador;
    }

    public String getNomTipoIndicador() {
        return nomTipoIndicador;
    }

    public void setNomTipoIndicador(String nomTipoIndicador) {
        this.nomTipoIndicador = nomTipoIndicador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Indicador> getIndicadorList() {
        return indicadorList;
    }

    public void setIndicadorList(List<Indicador> indicadorList) {
        this.indicadorList = indicadorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoIndicador != null ? idTipoIndicador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoIndicador)) {
            return false;
        }
        TipoIndicador other = (TipoIndicador) object;
        if ((this.idTipoIndicador == null && other.idTipoIndicador != null) || (this.idTipoIndicador != null && !this.idTipoIndicador.equals(other.idTipoIndicador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoIndicador[ idTipoIndicador=" + idTipoIndicador + " ]";
    }
    
}
