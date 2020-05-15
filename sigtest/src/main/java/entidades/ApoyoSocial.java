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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "apoyo_social")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApoyoSocial.findAll", query = "SELECT a FROM ApoyoSocial a")
    , @NamedQuery(name = "ApoyoSocial.findByCodApoyosocial", query = "SELECT a FROM ApoyoSocial a WHERE a.codApoyosocial = :codApoyosocial")
    , @NamedQuery(name = "ApoyoSocial.findByNombre", query = "SELECT a FROM ApoyoSocial a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "ApoyoSocial.findByApellido", query = "SELECT a FROM ApoyoSocial a WHERE a.apellido = :apellido")
    , @NamedQuery(name = "ApoyoSocial.findByCorreo", query = "SELECT a FROM ApoyoSocial a WHERE a.correo = :correo")
    , @NamedQuery(name = "ApoyoSocial.findByDirecci\u00f3n", query = "SELECT a FROM ApoyoSocial a WHERE a.direcci\u00f3n = :direcci\u00f3n")
    , @NamedQuery(name = "ApoyoSocial.findByEstado", query = "SELECT a FROM ApoyoSocial a WHERE a.estado = :estado")
    , @NamedQuery(name = "ApoyoSocial.findBySexo", query = "SELECT a FROM ApoyoSocial a WHERE a.sexo = :sexo")
    , @NamedQuery(name = "ApoyoSocial.findByTelefono", query = "SELECT a FROM ApoyoSocial a WHERE a.telefono = :telefono")
    , @NamedQuery(name = "ApoyoSocial.findByFechaNacimiento", query = "SELECT a FROM ApoyoSocial a WHERE a.fechaNacimiento = :fechaNacimiento")})
public class ApoyoSocial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cod_apoyosocial")
    private String codApoyosocial;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 30)
    @Column(name = "apellido")
    private String apellido;
    @Size(max = 50)
    @Column(name = "correo")
    private String correo;
    @Size(max = 100)
    @Column(name = "direcci\u00f3n")
    private String dirección;
    @Size(max = 30)
    @Column(name = "estado")
    private String estado;
    @Size(max = 100)
    @Column(name = "sexo")
    private String sexo;
    @Size(max = 100)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @OneToMany(mappedBy = "codApoyosocial")
    private List<Asignacion> asignacionList;

    public ApoyoSocial() {
    }

    public ApoyoSocial(String codApoyosocial) {
        this.codApoyosocial = codApoyosocial;
    }

    public String getCodApoyosocial() {
        return codApoyosocial;
    }

    public void setCodApoyosocial(String codApoyosocial) {
        this.codApoyosocial = codApoyosocial;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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
        hash += (codApoyosocial != null ? codApoyosocial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApoyoSocial)) {
            return false;
        }
        ApoyoSocial other = (ApoyoSocial) object;
        if ((this.codApoyosocial == null && other.codApoyosocial != null) || (this.codApoyosocial != null && !this.codApoyosocial.equals(other.codApoyosocial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ApoyoSocial[ codApoyosocial=" + codApoyosocial + " ]";
    }
    
}
