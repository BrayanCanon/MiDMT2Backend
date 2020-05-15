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
@Table(name = "tipo_pregunta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPregunta.findAll", query = "SELECT t FROM TipoPregunta t")
    , @NamedQuery(name = "TipoPregunta.findByIdTipoPregunta", query = "SELECT t FROM TipoPregunta t WHERE t.idTipoPregunta = :idTipoPregunta")
    , @NamedQuery(name = "TipoPregunta.findByNomPregunta", query = "SELECT t FROM TipoPregunta t WHERE t.nomPregunta = :nomPregunta")
    , @NamedQuery(name = "TipoPregunta.findByEstado", query = "SELECT t FROM TipoPregunta t WHERE t.estado = :estado")})
public class TipoPregunta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_pregunta")
    private Integer idTipoPregunta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_pregunta")
    private String nomPregunta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoPregunta")
    private List<Pregunta> preguntaList;

    public TipoPregunta() {
    }

    public TipoPregunta(Integer idTipoPregunta) {
        this.idTipoPregunta = idTipoPregunta;
    }

    public TipoPregunta(Integer idTipoPregunta, String nomPregunta, String estado) {
        this.idTipoPregunta = idTipoPregunta;
        this.nomPregunta = nomPregunta;
        this.estado = estado;
    }

    public Integer getIdTipoPregunta() {
        return idTipoPregunta;
    }

    public void setIdTipoPregunta(Integer idTipoPregunta) {
        this.idTipoPregunta = idTipoPregunta;
    }

    public String getNomPregunta() {
        return nomPregunta;
    }

    public void setNomPregunta(String nomPregunta) {
        this.nomPregunta = nomPregunta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Pregunta> getPreguntaList() {
        return preguntaList;
    }

    public void setPreguntaList(List<Pregunta> preguntaList) {
        this.preguntaList = preguntaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoPregunta != null ? idTipoPregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPregunta)) {
            return false;
        }
        TipoPregunta other = (TipoPregunta) object;
        if ((this.idTipoPregunta == null && other.idTipoPregunta != null) || (this.idTipoPregunta != null && !this.idTipoPregunta.equals(other.idTipoPregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoPregunta[ idTipoPregunta=" + idTipoPregunta + " ]";
    }
    
}
