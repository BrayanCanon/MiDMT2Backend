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
@Table(name = "nivel_paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelPaciente.findAll", query = "SELECT n FROM NivelPaciente n")
    , @NamedQuery(name = "NivelPaciente.findByIdNivel", query = "SELECT n FROM NivelPaciente n WHERE n.idNivel = :idNivel")
    , @NamedQuery(name = "NivelPaciente.findByNombreNivel", query = "SELECT n FROM NivelPaciente n WHERE n.nombreNivel = :nombreNivel")
    , @NamedQuery(name = "NivelPaciente.findByDescripcion", query = "SELECT n FROM NivelPaciente n WHERE n.descripcion = :descripcion")
    , @NamedQuery(name = "NivelPaciente.findByPuntajeReq", query = "SELECT n FROM NivelPaciente n WHERE n.puntajeReq = :puntajeReq")
    , @NamedQuery(name = "NivelPaciente.findByEstado", query = "SELECT n FROM NivelPaciente n WHERE n.estado = :estado")})
public class NivelPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nivel")
    @Expose
    private Integer idNivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nombre_nivel")
     @Expose
    private String nombreNivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descripcion")
     @Expose
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "puntaje_req")
     @Expose
    private int puntajeReq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;

    public NivelPaciente() {
    }

    public NivelPaciente(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public NivelPaciente(Integer idNivel, String nombreNivel, String descripcion, int puntajeReq, String estado) {
        this.idNivel = idNivel;
        this.nombreNivel = nombreNivel;
        this.descripcion = descripcion;
        this.puntajeReq = puntajeReq;
        this.estado = estado;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuntajeReq() {
        return puntajeReq;
    }

    public void setPuntajeReq(int puntajeReq) {
        this.puntajeReq = puntajeReq;
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
        hash += (idNivel != null ? idNivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelPaciente)) {
            return false;
        }
        NivelPaciente other = (NivelPaciente) object;
        if ((this.idNivel == null && other.idNivel != null) || (this.idNivel != null && !this.idNivel.equals(other.idNivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.NivelPaciente[ idNivel=" + idNivel + " ]";
    }
    
}
