/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "prueba_diagnostica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PruebaDiagnostica.findAll", query = "SELECT p FROM PruebaDiagnostica p")
    , @NamedQuery(name = "PruebaDiagnostica.findByIdPrueba", query = "SELECT p FROM PruebaDiagnostica p WHERE p.idPrueba = :idPrueba")
    , @NamedQuery(name = "PruebaDiagnostica.findByFechaPrueba", query = "SELECT p FROM PruebaDiagnostica p WHERE p.fechaPrueba = :fechaPrueba")
    , @NamedQuery(name = "PruebaDiagnostica.findByNombrePrueba", query = "SELECT p FROM PruebaDiagnostica p WHERE p.nombrePrueba = :nombrePrueba")
    , @NamedQuery(name = "PruebaDiagnostica.findByEstado", query = "SELECT p FROM PruebaDiagnostica p WHERE p.estado = :estado")})
public class PruebaDiagnostica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_prueba")
    private Integer idPrueba;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_prueba")
    @Temporal(TemporalType.DATE)
    private Date fechaPrueba;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_prueba")
    private String nombrePrueba;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "cod_paciente", referencedColumnName = "cod_paciente")
    @ManyToOne(optional = false)
    private Paciente codPaciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPrueba")
    private List<Indicador> indicadorList;

    public PruebaDiagnostica() {
    }

    public PruebaDiagnostica(Integer idPrueba) {
        this.idPrueba = idPrueba;
    }

    public PruebaDiagnostica(Integer idPrueba, Date fechaPrueba, String nombrePrueba, String estado) {
        this.idPrueba = idPrueba;
        this.fechaPrueba = fechaPrueba;
        this.nombrePrueba = nombrePrueba;
        this.estado = estado;
    }

    public Integer getIdPrueba() {
        return idPrueba;
    }

    public void setIdPrueba(Integer idPrueba) {
        this.idPrueba = idPrueba;
    }

    public Date getFechaPrueba() {
        return fechaPrueba;
    }

    public void setFechaPrueba(Date fechaPrueba) {
        this.fechaPrueba = fechaPrueba;
    }

    public String getNombrePrueba() {
        return nombrePrueba;
    }

    public void setNombrePrueba(String nombrePrueba) {
        this.nombrePrueba = nombrePrueba;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Paciente getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(Paciente codPaciente) {
        this.codPaciente = codPaciente;
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
        hash += (idPrueba != null ? idPrueba.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PruebaDiagnostica)) {
            return false;
        }
        PruebaDiagnostica other = (PruebaDiagnostica) object;
        if ((this.idPrueba == null && other.idPrueba != null) || (this.idPrueba != null && !this.idPrueba.equals(other.idPrueba))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PruebaDiagnostica[ idPrueba=" + idPrueba + " ]";
    }
    
}
