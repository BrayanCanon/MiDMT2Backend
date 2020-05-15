/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "medicamento_mision")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedicamentoMision.findAll", query = "SELECT m FROM MedicamentoMision m")
    , @NamedQuery(name = "MedicamentoMision.findByIdMedicamentoMision", query = "SELECT m FROM MedicamentoMision m WHERE m.idMedicamentoMision = :idMedicamentoMision")
    , @NamedQuery(name = "MedicamentoMision.findByFecha", query = "SELECT m FROM MedicamentoMision m WHERE m.fecha = :fecha")
    , @NamedQuery(name = "MedicamentoMision.findByDosificaci\u00f3n", query = "SELECT m FROM MedicamentoMision m WHERE m.dosificaci\u00f3n = :dosificaci\u00f3n")
    , @NamedQuery(name = "MedicamentoMision.findByPosologia", query = "SELECT m FROM MedicamentoMision m WHERE m.posologia = :posologia")
    , @NamedQuery(name = "MedicamentoMision.findByRecordar", query = "SELECT m FROM MedicamentoMision m WHERE m.recordar = :recordar")
    , @NamedQuery(name = "MedicamentoMision.findByUltimaToma", query = "SELECT m FROM MedicamentoMision m WHERE m.ultimaToma = :ultimaToma")
    , @NamedQuery(name = "MedicamentoMision.findByObservaciones", query = "SELECT m FROM MedicamentoMision m WHERE m.observaciones = :observaciones")
    , @NamedQuery(name = "MedicamentoMision.findByEstado", query = "SELECT m FROM MedicamentoMision m WHERE m.estado = :estado")})
public class MedicamentoMision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medicamento_mision")
    private Integer idMedicamentoMision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dosificaci\u00f3n")
    private int dosificación;
    @Basic(optional = false)
    @NotNull
    @Column(name = "posologia")
    private int posologia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recordar")
    private short recordar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ultima_toma")
    @Temporal(TemporalType.TIME)
    private Date ultimaToma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "cod_medicamento", referencedColumnName = "cod_medicamento")
    @ManyToOne(optional = false)
    private Medicamento codMedicamento;
    @JoinColumn(name = "id_mision_paciente", referencedColumnName = "id_mision_paciente")
    @ManyToOne
    private MisionPaciente idMisionPaciente;

    public MedicamentoMision() {
    }

    public MedicamentoMision(Integer idMedicamentoMision) {
        this.idMedicamentoMision = idMedicamentoMision;
    }

    public MedicamentoMision(Integer idMedicamentoMision, Date fecha, int dosificación, int posologia, short recordar, Date ultimaToma, String observaciones, String estado) {
        this.idMedicamentoMision = idMedicamentoMision;
        this.fecha = fecha;
        this.dosificación = dosificación;
        this.posologia = posologia;
        this.recordar = recordar;
        this.ultimaToma = ultimaToma;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public Integer getIdMedicamentoMision() {
        return idMedicamentoMision;
    }

    public void setIdMedicamentoMision(Integer idMedicamentoMision) {
        this.idMedicamentoMision = idMedicamentoMision;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDosificación() {
        return dosificación;
    }

    public void setDosificación(int dosificación) {
        this.dosificación = dosificación;
    }

    public int getPosologia() {
        return posologia;
    }

    public void setPosologia(int posologia) {
        this.posologia = posologia;
    }

    public short getRecordar() {
        return recordar;
    }

    public void setRecordar(short recordar) {
        this.recordar = recordar;
    }

    public Date getUltimaToma() {
        return ultimaToma;
    }

    public void setUltimaToma(Date ultimaToma) {
        this.ultimaToma = ultimaToma;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Medicamento getCodMedicamento() {
        return codMedicamento;
    }

    public void setCodMedicamento(Medicamento codMedicamento) {
        this.codMedicamento = codMedicamento;
    }

    public MisionPaciente getIdMisionPaciente() {
        return idMisionPaciente;
    }

    public void setIdMisionPaciente(MisionPaciente idMisionPaciente) {
        this.idMisionPaciente = idMisionPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedicamentoMision != null ? idMedicamentoMision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicamentoMision)) {
            return false;
        }
        MedicamentoMision other = (MedicamentoMision) object;
        if ((this.idMedicamentoMision == null && other.idMedicamentoMision != null) || (this.idMedicamentoMision != null && !this.idMedicamentoMision.equals(other.idMedicamentoMision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.MedicamentoMision[ idMedicamentoMision=" + idMedicamentoMision + " ]";
    }
    
}
