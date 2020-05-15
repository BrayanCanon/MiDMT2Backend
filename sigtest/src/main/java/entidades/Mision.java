/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gulmi
 */
@Entity
@Table(name = "mision")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mision.findAll", query = "SELECT m FROM Mision m")
    , @NamedQuery(name = "Mision.findByIdMision", query = "SELECT m FROM Mision m WHERE m.idMision = :idMision")
    , @NamedQuery(name = "Mision.findByNombre", query = "SELECT m FROM Mision m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "Mision.findByEstado", query = "SELECT m FROM Mision m WHERE m.estado = :estado")})
public class Mision implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMision")
    private List<MisionTipoRecurso> misionTipoRecursoList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mision")
    @Expose
    private Integer idMision;
    @Size(max = 30)
    @Column(name = "nombre")
    @Expose
    private String nombre;
    @Size(max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    @Expose
    private String descripcion;
    @OneToMany(mappedBy = "idMision")
    private List<MisionPasoLogro> misionPasoLogroList;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    @Expose
    private Categoria idCategoria;
    @JoinColumn(name = "id_nivel", referencedColumnName = "id_nivel")
    @ManyToOne
    @Expose
    private Nivel idNivel;
    @JoinColumn(name = "id_tipo_mision", referencedColumnName = "id_tipo_mision")
    @ManyToOne
    @Expose
    private TipoMision idTipoMision;
    @OneToMany(mappedBy = "idMision",fetch = FetchType.EAGER, cascade ={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<MisionPaciente> misionPacienteList;

    public Mision() {
    }

    public Mision(Integer idMision) {
        this.idMision = idMision;
    }

    public Integer getIdMision() {
        return idMision;
    }

    public void setIdMision(Integer idMision) {
        this.idMision = idMision;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<MisionPasoLogro> getMisionPasoLogroList() {
        return misionPasoLogroList;
    }

    public void setMisionPasoLogroList(List<MisionPasoLogro> misionPasoLogroList) {
        this.misionPasoLogroList = misionPasoLogroList;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Nivel getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Nivel idNivel) {
        this.idNivel = idNivel;
    }

    public TipoMision getIdTipoMision() {
        return idTipoMision;
    }

    public void setIdTipoMision(TipoMision idTipoMision) {
        this.idTipoMision = idTipoMision;
    }

    @XmlTransient
    public List<MisionPaciente> getMisionPacienteList() {
        return misionPacienteList;
    }

    public void setMisionPacienteList(List<MisionPaciente> misionPacienteList) {
        this.misionPacienteList = misionPacienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMision != null ? idMision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mision)) {
            return false;
        }
        Mision other = (Mision) object;
        if ((this.idMision == null && other.idMision != null) || (this.idMision != null && !this.idMision.equals(other.idMision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Mision[ idMision=" + idMision + " ]";
    }

    @XmlTransient
    public List<MisionTipoRecurso> getMisionTipoRecursoList() {
        return misionTipoRecursoList;
    }

    public void setMisionTipoRecursoList(List<MisionTipoRecurso> misionTipoRecursoList) {
        this.misionTipoRecursoList = misionTipoRecursoList;
    }
    
}
