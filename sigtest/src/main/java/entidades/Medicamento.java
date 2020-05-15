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
@Table(name = "medicamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m")
    , @NamedQuery(name = "Medicamento.findByCodMedicamento", query = "SELECT m FROM Medicamento m WHERE m.codMedicamento = :codMedicamento")
    , @NamedQuery(name = "Medicamento.findByNombre", query = "SELECT m FROM Medicamento m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "Medicamento.findByTipo", query = "SELECT m FROM Medicamento m WHERE m.tipo = :tipo")
    , @NamedQuery(name = "Medicamento.findByEstado", query = "SELECT m FROM Medicamento m WHERE m.estado = :estado")})
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cod_medicamento")
    @Expose
    private String codMedicamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    @Expose
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipo")
    @Expose
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "estado")
    @Expose
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMedicamento")
    private List<MedicamentoMision> medicamentoMisionList;

    public Medicamento() {
    }

    public Medicamento(String codMedicamento) {
        this.codMedicamento = codMedicamento;
    }

    public Medicamento(String codMedicamento, String nombre, String tipo, String estado) {
        this.codMedicamento = codMedicamento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getCodMedicamento() {
        return codMedicamento;
    }

    public void setCodMedicamento(String codMedicamento) {
        this.codMedicamento = codMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<MedicamentoMision> getMedicamentoMisionList() {
        return medicamentoMisionList;
    }

    public void setMedicamentoMisionList(List<MedicamentoMision> medicamentoMisionList) {
        this.medicamentoMisionList = medicamentoMisionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMedicamento != null ? codMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.codMedicamento == null && other.codMedicamento != null) || (this.codMedicamento != null && !this.codMedicamento.equals(other.codMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Medicamento[ codMedicamento=" + codMedicamento + " ]";
    }
    
}
