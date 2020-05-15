/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
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
@Table(name = "verificacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Verificacion.findAll", query = "SELECT v FROM Verificacion v")
    , @NamedQuery(name = "Verificacion.findByIdVerificacion", query = "SELECT v FROM Verificacion v WHERE v.idVerificacion = :idVerificacion")
    , @NamedQuery(name = "Verificacion.findByVerifPaciente", query = "SELECT v FROM Verificacion v WHERE v.verifPaciente = :verifPaciente")
    , @NamedQuery(name = "Verificacion.findByVerifApoyoSocial", query = "SELECT v FROM Verificacion v WHERE v.verifApoyoSocial = :verifApoyoSocial")
    , @NamedQuery(name = "Verificacion.findByEstado", query = "SELECT v FROM Verificacion v WHERE v.estado = :estado")
    , @NamedQuery(name = "Verificacion.findByNumeroDia", query = "SELECT v FROM Verificacion v WHERE v.numeroDia = :numeroDia")})
public class Verificacion implements Serializable {



    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_verificacion")
    @Expose
    private Integer idVerificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verif_paciente")
    @Expose
    private boolean verifPaciente;
        @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    @Expose
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verif_apoyo_social")
    @Expose
    private boolean verifApoyoSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @Column(name = "numero_dia")
    @Expose
    private Integer numeroDia;
    @JoinColumn(name = "id_mision_paciente", referencedColumnName = "id_mision_paciente")
    @ManyToOne
    @Expose
    private MisionPaciente idMisionPaciente;

    public Verificacion() {
    }

    public Verificacion(Integer idVerificacion) {
        this.idVerificacion = idVerificacion;
    }

    public Verificacion(Integer idVerificacion, boolean verifPaciente, boolean verifApoyoSocial, String estado) {
        this.idVerificacion = idVerificacion;
        this.verifPaciente = verifPaciente;
        this.verifApoyoSocial = verifApoyoSocial;
        this.estado = estado;
    }

    public Integer getIdVerificacion() {
        return idVerificacion;
    }

    public void setIdVerificacion(Integer idVerificacion) {
        this.idVerificacion = idVerificacion;
    }

    public boolean getVerifPaciente() {
        return verifPaciente;
    }

    public void setVerifPaciente(boolean verifPaciente) {
        this.verifPaciente = verifPaciente;
    }

    public boolean getVerifApoyoSocial() {
        return verifApoyoSocial;
    }

    public void setVerifApoyoSocial(boolean verifApoyoSocial) {
        this.verifApoyoSocial = verifApoyoSocial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNumeroDia() {
        return numeroDia;
    }

    public void setNumeroDia(Integer numeroDia) {
        this.numeroDia = numeroDia;
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
        hash += (idVerificacion != null ? idVerificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Verificacion)) {
            return false;
        }
        Verificacion other = (Verificacion) object;
        if ((this.idVerificacion == null && other.idVerificacion != null) || (this.idVerificacion != null && !this.idVerificacion.equals(other.idVerificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Verificacion[ idVerificacion=" + idVerificacion + " ]";
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
