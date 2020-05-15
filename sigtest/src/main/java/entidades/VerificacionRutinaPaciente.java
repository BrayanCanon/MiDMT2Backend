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

/**
 *
 * @author carlos.escobar
 */
@Entity
@Table(name = "verificacion_rutina_paciente")
@NamedQueries({
    @NamedQuery(name = "VerificacionRutinaPaciente.findAll", query = "SELECT v FROM VerificacionRutinaPaciente v")
    , @NamedQuery(name = "VerificacionRutinaPaciente.findById", query = "SELECT v FROM VerificacionRutinaPaciente v WHERE v.id = :id")
    , @NamedQuery(name = "VerificacionRutinaPaciente.findByValor", query = "SELECT v FROM VerificacionRutinaPaciente v WHERE v.valor = :valor")
    , @NamedQuery(name = "VerificacionRutinaPaciente.findByFecha", query = "SELECT v FROM VerificacionRutinaPaciente v WHERE v.fecha = :fecha")})
public class VerificacionRutinaPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "valor")
    private String valor;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "id_rutina_paciente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RutinaPaciente idRutinaPaciente;

    public VerificacionRutinaPaciente() {
    }

    public VerificacionRutinaPaciente(Integer id) {
        this.id = id;
    }

    public VerificacionRutinaPaciente(Integer id, String valor) {
        this.id = id;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public RutinaPaciente getIdRutinaPaciente() {
        return idRutinaPaciente;
    }

    public void setIdRutinaPaciente(RutinaPaciente idRutinaPaciente) {
        this.idRutinaPaciente = idRutinaPaciente;
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
        if (!(object instanceof VerificacionRutinaPaciente)) {
            return false;
        }
        VerificacionRutinaPaciente other = (VerificacionRutinaPaciente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VerificacionRutinaPaciente[ id=" + id + " ]";
    }
    
}
