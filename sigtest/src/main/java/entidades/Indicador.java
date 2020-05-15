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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "indicador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicador.findAll", query = "SELECT i FROM Indicador i")
    , @NamedQuery(name = "Indicador.findByIdIndicador", query = "SELECT i FROM Indicador i WHERE i.idIndicador = :idIndicador")
    , @NamedQuery(name = "Indicador.findByValorIndicador", query = "SELECT i FROM Indicador i WHERE i.valorIndicador = :valorIndicador")
    , @NamedQuery(name = "Indicador.findByEstado", query = "SELECT i FROM Indicador i WHERE i.estado = :estado")})
public class Indicador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_indicador")
    @Expose
    private Integer idIndicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_indicador")
    @Expose
    private float valorIndicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @JoinColumn(name = "id_prueba", referencedColumnName = "id_prueba")
    @ManyToOne(optional = false)
    @Expose
    private PruebaDiagnostica idPrueba;
    @JoinColumn(name = "id_tipo_indicador", referencedColumnName = "id_tipo_indicador")
    @ManyToOne(optional = false)
    @Expose
    private TipoIndicador idTipoIndicador;

    public Indicador() {
    }

    public Indicador(Integer idIndicador) {
        this.idIndicador = idIndicador;
    }

    public Indicador(Integer idIndicador, float valorIndicador, String estado) {
        this.idIndicador = idIndicador;
        this.valorIndicador = valorIndicador;
        this.estado = estado;
    }

    public Integer getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(Integer idIndicador) {
        this.idIndicador = idIndicador;
    }

    public float getValorIndicador() {
        return valorIndicador;
    }

    public void setValorIndicador(float valorIndicador) {
        this.valorIndicador = valorIndicador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public PruebaDiagnostica getIdPrueba() {
        return idPrueba;
    }

    public void setIdPrueba(PruebaDiagnostica idPrueba) {
        this.idPrueba = idPrueba;
    }

    public TipoIndicador getIdTipoIndicador() {
        return idTipoIndicador;
    }

    public void setIdTipoIndicador(TipoIndicador idTipoIndicador) {
        this.idTipoIndicador = idTipoIndicador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIndicador != null ? idIndicador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indicador)) {
            return false;
        }
        Indicador other = (Indicador) object;
        if ((this.idIndicador == null && other.idIndicador != null) || (this.idIndicador != null && !this.idIndicador.equals(other.idIndicador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Indicador[ idIndicador=" + idIndicador + " ]";
    }
    
}
