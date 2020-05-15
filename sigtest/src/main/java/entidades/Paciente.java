/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p")
    , @NamedQuery(name = "Paciente.findByCodPaciente", query = "SELECT p FROM Paciente p WHERE p.codPaciente = :codPaciente")
    , @NamedQuery(name = "Paciente.findByFechaNacimiento", query = "SELECT p FROM Paciente p WHERE p.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Paciente.findByEstrato", query = "SELECT p FROM Paciente p WHERE p.estrato = :estrato")
    , @NamedQuery(name = "Paciente.findByNumHijo", query = "SELECT p FROM Paciente p WHERE p.numHijo = :numHijo")
    , @NamedQuery(name = "Paciente.findByEstatura", query = "SELECT p FROM Paciente p WHERE p.estatura = :estatura")
    , @NamedQuery(name = "Paciente.findByEstado", query = "SELECT p FROM Paciente p WHERE p.estado = :estado")
    , @NamedQuery(name = "Paciente.findByCiudad", query = "SELECT p FROM Paciente p WHERE p.ciudad = :ciudad")
    , @NamedQuery(name = "Paciente.findByPuntajeAlim", query = "SELECT p FROM Paciente p WHERE p.puntajeAlim = :puntajeAlim")
    , @NamedQuery(name = "Paciente.findByPuntajeEjer", query = "SELECT p FROM Paciente p WHERE p.puntajeEjer = :puntajeEjer")})
public class Paciente implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPaciente")
    private List<RutinaPaciente> rutinaPacienteCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPaciente")
    private List<Asignacion> asignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPaciente")
    private List<Encuesta> encuestaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPaciente")
    private List<PruebaDiagnostica> pruebaDiagnosticaList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPaciente")
    private List<MisionPaciente> misionPacienteList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cod_paciente")
    @Expose
    private String codPaciente;
    @Lob
    @Size(max = 65535)
    @Column(name = "nombre")
    @Expose
    private String nombre;
    @Lob
    @Size(max = 65535)
    @Column(name = "apellido")
    @Expose
    private String apellido;
    @Lob
    @Size(max = 65535)
    @Column(name = "correo")
    @Expose
    private String correo;
    @Lob
    @Size(max = 65535)
    @Column(name = "sexo")
    @Expose
    private String sexo;
    @Lob
    @Size(max = 65535)
    @Column(name = "telefono")
    @Expose
    private String telefono;
    @Lob
    @Size(max = 65535)
    @Column(name = "direccion")
    @Expose
    private String direccion;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    @Expose
    private Date fechaNacimiento;
    @Lob
    @Size(max = 65535)
    @Column(name = "ocupacion")
    @Expose
    private String ocupacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "eps")
    @Expose
    private String eps;
    @Column(name = "estrato")
    @Expose
    private Integer estrato;
    @Lob
    @Size(max = 65535)
    @Column(name = "estado_civil")
    @Expose
    private String estadoCivil;
    @Column(name = "num_hijo")
    @Expose
    private Integer numHijo;
    @Column(name = "estatura")
    @Expose
    private Integer estatura;
    @Size(max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @Size(max = 100)
    @Column(name = "ciudad")
    @Expose
    private String ciudad;
    @Column(name = "puntaje_alim")
    @Expose
    private Integer puntajeAlim;
    @Column(name = "puntaje_ejer")
    @Expose
    private Integer puntajeEjer;

    public Paciente() {
    }

    public Paciente(String codPaciente) {
        this.codPaciente = codPaciente;
    }

    public String getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(String codPaciente) {
        this.codPaciente = codPaciente;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public Integer getEstrato() {
        return estrato;
    }

    public void setEstrato(Integer estrato) {
        this.estrato = estrato;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Integer getNumHijo() {
        return numHijo;
    }

    public void setNumHijo(Integer numHijo) {
        this.numHijo = numHijo;
    }

    public Integer getEstatura() {
        return estatura;
    }

    public void setEstatura(Integer estatura) {
        this.estatura = estatura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getPuntajeAlim() {
        return puntajeAlim;
    }

    public void setPuntajeAlim(Integer puntajeAlim) {
        this.puntajeAlim = puntajeAlim;
    }

    public Integer getPuntajeEjer() {
        return puntajeEjer;
    }

    public void setPuntajeEjer(Integer puntajeEjer) {
        this.puntajeEjer = puntajeEjer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPaciente != null ? codPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.codPaciente == null && other.codPaciente != null) || (this.codPaciente != null && !this.codPaciente.equals(other.codPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Paciente[ codPaciente=" + codPaciente + " ]";
    }

    @XmlTransient
    public List<MisionPaciente> getMisionPacienteList() {
        return misionPacienteList;
    }

    public void setMisionPacienteList(List<MisionPaciente> misionPacienteList) {
        this.misionPacienteList = misionPacienteList;
    }

    @XmlTransient
    public List<Asignacion> getAsignacionList() {
        return asignacionList;
    }

    public void setAsignacionList(List<Asignacion> asignacionList) {
        this.asignacionList = asignacionList;
    }

    @XmlTransient
    public List<Encuesta> getEncuestaList() {
        return encuestaList;
    }

    public void setEncuestaList(List<Encuesta> encuestaList) {
        this.encuestaList = encuestaList;
    }

    @XmlTransient
    public List<PruebaDiagnostica> getPruebaDiagnosticaList() {
        return pruebaDiagnosticaList;
    }

    public void setPruebaDiagnosticaList(List<PruebaDiagnostica> pruebaDiagnosticaList) {
        this.pruebaDiagnosticaList = pruebaDiagnosticaList;
    }

    @XmlTransient
    public List<RutinaPaciente> getRutinaPacienteCollection() {
        return rutinaPacienteCollection;
    }

    public void setRutinaPacienteCollection(List<RutinaPaciente> rutinaPacienteCollection) {
        this.rutinaPacienteCollection = rutinaPacienteCollection;
    }
    
}
