/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "profesional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profesional.findAll", query = "SELECT p FROM Profesional p")
    , @NamedQuery(name = "Profesional.findByCodProfesional", query = "SELECT p FROM Profesional p WHERE p.codProfesional = :codProfesional")
    , @NamedQuery(name = "Profesional.findByNombre", query = "SELECT p FROM Profesional p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Profesional.findByApellido", query = "SELECT p FROM Profesional p WHERE p.apellido = :apellido")
    , @NamedQuery(name = "Profesional.findByEspecialidad", query = "SELECT p FROM Profesional p WHERE p.especialidad = :especialidad")
    , @NamedQuery(name = "Profesional.findByCorreo", query = "SELECT p FROM Profesional p WHERE p.correo = :correo")
    , @NamedQuery(name = "Profesional.findByTelefono", query = "SELECT p FROM Profesional p WHERE p.telefono = :telefono")
    , @NamedQuery(name = "Profesional.findByEstado", query = "SELECT p FROM Profesional p WHERE p.estado = :estado")})
public class Profesional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cod_profesional")
    private String codProfesional;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "especialidad")
    private String especialidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @OneToMany(mappedBy = "codProfesional")
    private List<Asignacion> asignacionList;

    public Profesional() {
    }

    public Profesional(String codProfesional) {
        this.codProfesional = codProfesional;
    }

    public Profesional(String codProfesional, String nombre, String apellido, String especialidad, String correo, String telefono, String estado) {
        this.codProfesional = codProfesional;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }

    public String getCodProfesional() {
        return codProfesional;
    }

    public void setCodProfesional(String codProfesional) {
        this.codProfesional = codProfesional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Asignacion> getAsignacionList() {
        return asignacionList;
    }

    public void setAsignacionList(List<Asignacion> asignacionList) {
        this.asignacionList = asignacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codProfesional != null ? codProfesional.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profesional)) {
            return false;
        }
        Profesional other = (Profesional) object;
        if ((this.codProfesional == null && other.codProfesional != null) || (this.codProfesional != null && !this.codProfesional.equals(other.codProfesional))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Profesional[ codProfesional=" + codProfesional + " ]";
    }
    
}
