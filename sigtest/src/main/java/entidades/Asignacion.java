/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

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
@Table(name = "asignacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asignacion.findAll", query = "SELECT a FROM Asignacion a")
    , @NamedQuery(name = "Asignacion.findByCodAsginacion", query = "SELECT a FROM Asignacion a WHERE a.codAsginacion = :codAsginacion")
    , @NamedQuery(name = "Asignacion.findByEstado", query = "SELECT a FROM Asignacion a WHERE a.estado = :estado")})
public class Asignacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod_asginacion")
    private Integer codAsginacion;
    @Size(max = 30)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "cod_apoyosocial", referencedColumnName = "cod_apoyosocial")
    @ManyToOne
    private ApoyoSocial codApoyosocial;
    @JoinColumn(name = "cod_paciente", referencedColumnName = "cod_paciente")
    @ManyToOne
    private Paciente codPaciente;
    @JoinColumn(name = "cod_profesional", referencedColumnName = "cod_profesional")
    @ManyToOne
    private Profesional codProfesional;

    public Asignacion() {
    }

    public Asignacion(Integer codAsginacion) {
        this.codAsginacion = codAsginacion;
    }

    public Integer getCodAsginacion() {
        return codAsginacion;
    }

    public void setCodAsginacion(Integer codAsginacion) {
        this.codAsginacion = codAsginacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ApoyoSocial getCodApoyosocial() {
        return codApoyosocial;
    }

    public void setCodApoyosocial(ApoyoSocial codApoyosocial) {
        this.codApoyosocial = codApoyosocial;
    }

    public Paciente getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(Paciente codPaciente) {
        this.codPaciente = codPaciente;
    }

    public Profesional getCodProfesional() {
        return codProfesional;
    }

    public void setCodProfesional(Profesional codProfesional) {
        this.codProfesional = codProfesional;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAsginacion != null ? codAsginacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignacion)) {
            return false;
        }
        Asignacion other = (Asignacion) object;
        if ((this.codAsginacion == null && other.codAsginacion != null) || (this.codAsginacion != null && !this.codAsginacion.equals(other.codAsginacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Asignacion[ codAsginacion=" + codAsginacion + " ]";
    }
    
}
