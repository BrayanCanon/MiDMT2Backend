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
@Table(name = "tratamiento_paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TratamientoPaciente.findAll", query = "SELECT t FROM TratamientoPaciente t")
    , @NamedQuery(name = "TratamientoPaciente.findByIdPerfilTratamiento", query = "SELECT t FROM TratamientoPaciente t WHERE t.idPerfilTratamiento = :idPerfilTratamiento")
    , @NamedQuery(name = "TratamientoPaciente.findByNomPerfilTratamiento", query = "SELECT t FROM TratamientoPaciente t WHERE t.nomPerfilTratamiento = :nomPerfilTratamiento")
    , @NamedQuery(name = "TratamientoPaciente.findByEstado", query = "SELECT t FROM TratamientoPaciente t WHERE t.estado = :estado")})
public class TratamientoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_perfil_tratamiento")
    private Integer idPerfilTratamiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nom_perfil_tratamiento")
    private int nomPerfilTratamiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    private String estado;

    public TratamientoPaciente() {
    }

    public TratamientoPaciente(Integer idPerfilTratamiento) {
        this.idPerfilTratamiento = idPerfilTratamiento;
    }

    public TratamientoPaciente(Integer idPerfilTratamiento, int nomPerfilTratamiento, String estado) {
        this.idPerfilTratamiento = idPerfilTratamiento;
        this.nomPerfilTratamiento = nomPerfilTratamiento;
        this.estado = estado;
    }

    public Integer getIdPerfilTratamiento() {
        return idPerfilTratamiento;
    }

    public void setIdPerfilTratamiento(Integer idPerfilTratamiento) {
        this.idPerfilTratamiento = idPerfilTratamiento;
    }

    public int getNomPerfilTratamiento() {
        return nomPerfilTratamiento;
    }

    public void setNomPerfilTratamiento(int nomPerfilTratamiento) {
        this.nomPerfilTratamiento = nomPerfilTratamiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfilTratamiento != null ? idPerfilTratamiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TratamientoPaciente)) {
            return false;
        }
        TratamientoPaciente other = (TratamientoPaciente) object;
        if ((this.idPerfilTratamiento == null && other.idPerfilTratamiento != null) || (this.idPerfilTratamiento != null && !this.idPerfilTratamiento.equals(other.idPerfilTratamiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TratamientoPaciente[ idPerfilTratamiento=" + idPerfilTratamiento + " ]";
    }
    
}
