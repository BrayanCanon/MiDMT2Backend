/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "mision_paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MisionPaciente.findAll", query = "SELECT m FROM MisionPaciente m")
    , @NamedQuery(name = "MisionPaciente.findByIdMisionPaciente", query = "SELECT m FROM MisionPaciente m WHERE m.idMisionPaciente = :idMisionPaciente")
    , @NamedQuery(name = "MisionPaciente.findByCompletada", query = "SELECT m FROM MisionPaciente m WHERE m.completada = :completada")
    , @NamedQuery(name = "MisionPaciente.findByFechaInicio", query = "SELECT m FROM MisionPaciente m WHERE m.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "MisionPaciente.findByFechaFinal", query = "SELECT m FROM MisionPaciente m WHERE m.fechaFinal = :fechaFinal")
    , @NamedQuery(name = "MisionPaciente.findByEstado", query = "SELECT m FROM MisionPaciente m WHERE m.estado = :estado")})
public class MisionPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_mision_paciente")
    @Expose
    private Integer idMisionPaciente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "completada")
    @Expose
    private String completada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    @Expose
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    @Expose
    private Date fechaFinal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    
    
    @OneToMany(mappedBy = "idMisionPaciente")
    private List<MedicamentoMision> medicamentoMisionList;
    @JoinColumn(name = "id_mision", referencedColumnName = "id_mision")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Expose
    private Mision idMision;
    @JoinColumn(name = "cod_paciente", referencedColumnName = "cod_paciente")
    @ManyToOne(optional = false)
    private Paciente codPaciente;
    @OneToMany(mappedBy = "idMisionPaciente")
    private List<Verificacion> verificacionList;

    public MisionPaciente() {
    }

    public MisionPaciente(Integer idMisionPaciente) {
        this.idMisionPaciente = idMisionPaciente;
    }

    public MisionPaciente(Integer idMisionPaciente, String completada, Date fechaInicio, Date fechaFinal, String estado) {
        this.idMisionPaciente = idMisionPaciente;
        this.completada = completada;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.estado = estado;
    }

    public Integer getIdMisionPaciente() {
        return idMisionPaciente;
    }

    public void setIdMisionPaciente(Integer idMisionPaciente) {
        this.idMisionPaciente = idMisionPaciente;
    }

    public String getCompletada() {
        return completada;
    }

    public void setCompletada(String completada) {
        this.completada = completada;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
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

    public Mision getIdMision() {
        return idMision;
    }

    public void setIdMision(Mision idMision) {
        this.idMision = idMision;
    }

    public Paciente getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(Paciente codPaciente) {
        this.codPaciente = codPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMisionPaciente != null ? idMisionPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MisionPaciente)) {
            return false;
        }
        MisionPaciente other = (MisionPaciente) object;
        if ((this.idMisionPaciente == null && other.idMisionPaciente != null) || (this.idMisionPaciente != null && !this.idMisionPaciente.equals(other.idMisionPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.MisionPaciente[ idMisionPaciente=" + idMisionPaciente + " ]";
    }

    @XmlTransient
    public List<Verificacion> getVerificacionList() {
        return verificacionList;
    }

    public void setVerificacionList(List<Verificacion> verificacionList) {
        this.verificacionList = verificacionList;
    }

}
