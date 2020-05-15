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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mision_paso_logro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MisionPasoLogro.findAll", query = "SELECT m FROM MisionPasoLogro m")
    , @NamedQuery(name = "MisionPasoLogro.findByRepeticion", query = "SELECT m FROM MisionPasoLogro m WHERE m.repeticion = :repeticion")
    , @NamedQuery(name = "MisionPasoLogro.findByPasoNumero", query = "SELECT m FROM MisionPasoLogro m WHERE m.pasoNumero = :pasoNumero")
    , @NamedQuery(name = "MisionPasoLogro.findByEstado", query = "SELECT m FROM MisionPasoLogro m WHERE m.estado = :estado")
    , @NamedQuery(name = "MisionPasoLogro.findByIdMisionPasoLogro", query = "SELECT m FROM MisionPasoLogro m WHERE m.idMisionPasoLogro = :idMisionPasoLogro")})
public class MisionPasoLogro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "repeticion")
    private Integer repeticion;
    @Column(name = "paso_numero")
    @Expose
    private Integer pasoNumero;
    @Size(max = 30)
    @Column(name = "estado")
    private String estado;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mision_paso_logro")
    @Expose
    private Integer idMisionPasoLogro;
    @JoinColumn(name = "id_logro", referencedColumnName = "id_logro")
    @ManyToOne
    @Expose
    private Logro idLogro;
    @JoinColumn(name = "id_mision", referencedColumnName = "id_mision")
    @ManyToOne
    @Expose
    private Mision idMision;
    @JoinColumn(name = "id_paso", referencedColumnName = "id_paso")
    @ManyToOne
    @Expose
    private Paso idPaso;

    public MisionPasoLogro() {
    }

    public MisionPasoLogro(Integer idMisionPasoLogro) {
        this.idMisionPasoLogro = idMisionPasoLogro;
    }

    public Integer getRepeticion() {
        return repeticion;
    }

    public void setRepeticion(Integer repeticion) {
        this.repeticion = repeticion;
    }

    public Integer getPasoNumero() {
        return pasoNumero;
    }

    public void setPasoNumero(Integer pasoNumero) {
        this.pasoNumero = pasoNumero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdMisionPasoLogro() {
        return idMisionPasoLogro;
    }

    public void setIdMisionPasoLogro(Integer idMisionPasoLogro) {
        this.idMisionPasoLogro = idMisionPasoLogro;
    }

    public Logro getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Logro idLogro) {
        this.idLogro = idLogro;
    }

    public Mision getIdMision() {
        return idMision;
    }

    public void setIdMision(Mision idMision) {
        this.idMision = idMision;
    }

    public Paso getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(Paso idPaso) {
        this.idPaso = idPaso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMisionPasoLogro != null ? idMisionPasoLogro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MisionPasoLogro)) {
            return false;
        }
        MisionPasoLogro other = (MisionPasoLogro) object;
        if ((this.idMisionPasoLogro == null && other.idMisionPasoLogro != null) || (this.idMisionPasoLogro != null && !this.idMisionPasoLogro.equals(other.idMisionPasoLogro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.MisionPasoLogro[ idMisionPasoLogro=" + idMisionPasoLogro + " ]";
    }
    
}
