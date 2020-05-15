/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Chaly
 */
@Entity
@Table(name = "rutina_paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RutinaPaciente.findAll", query = "SELECT r FROM RutinaPaciente r")
    , @NamedQuery(name = "RutinaPaciente.findById", query = "SELECT r FROM RutinaPaciente r WHERE r.id = :id")})
public class RutinaPaciente implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "cod_paciente", referencedColumnName = "cod_paciente")
    @ManyToOne(optional = false)
    private Paciente codPaciente;
    @JoinColumn(name = "id_rutina", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Rutina idRutina;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idRutinaPaciente")
    private Collection<VerificacionRutinaPaciente> verificacionRutinaPacienteCollection;

    public RutinaPaciente() {
    }

    public RutinaPaciente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Paciente getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(Paciente codPaciente) {
        this.codPaciente = codPaciente;
    }

    public Rutina getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(Rutina idRutina) {
        this.idRutina = idRutina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RutinaPaciente)) {
            return false;
        }
        RutinaPaciente other = (RutinaPaciente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.RutinaPaciente[ id=" + id + " ]";
    }

    public Collection<VerificacionRutinaPaciente> getVerificacionRutinaPacienteCollection() {
        return verificacionRutinaPacienteCollection;
    }

    public void setVerificacionRutinaPacienteCollection(Collection<VerificacionRutinaPaciente> verificacionRutinaPacienteCollection) {
        this.verificacionRutinaPacienteCollection = verificacionRutinaPacienteCollection;
    }
    
}
