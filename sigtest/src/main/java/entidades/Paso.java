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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "paso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paso.findAll", query = "SELECT p FROM Paso p")
    , @NamedQuery(name = "Paso.findByIdPaso", query = "SELECT p FROM Paso p WHERE p.idPaso = :idPaso")
    , @NamedQuery(name = "Paso.findByDescripcion", query = "SELECT p FROM Paso p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "Paso.findByNombre", query = "SELECT p FROM Paso p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Paso.findByDiasDuracion", query = "SELECT p FROM Paso p WHERE p.diasDuracion = :diasDuracion")
    , @NamedQuery(name = "Paso.findByEstado", query = "SELECT p FROM Paso p WHERE p.estado = :estado")
    , @NamedQuery(name = "Paso.findByCategoria", query = "SELECT p FROM Paso p WHERE p.categoria = :categoria")})
public class Paso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_paso")
    @Expose
    private Integer idPaso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    @Expose
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    @Expose
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dias_duracion")
    @Expose
    private int diasDuracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @Column(name = "categoria")
    @Expose
    private Integer categoria;
    @OneToMany(mappedBy = "idPaso")
    private List<MisionPasoLogro> misionPasoLogroList;

    public Paso() {
    }

    public Paso(Integer idPaso) {
        this.idPaso = idPaso;
    }

    public Paso(Integer idPaso, String descripcion, String nombre, int diasDuracion, String estado) {
        this.idPaso = idPaso;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.diasDuracion = diasDuracion;
        this.estado = estado;
    }

    public Integer getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(Integer idPaso) {
        this.idPaso = idPaso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDiasDuracion() {
        return diasDuracion;
    }

    public void setDiasDuracion(int diasDuracion) {
        this.diasDuracion = diasDuracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public List<MisionPasoLogro> getMisionPasoLogroList() {
        return misionPasoLogroList;
    }

    public void setMisionPasoLogroList(List<MisionPasoLogro> misionPasoLogroList) {
        this.misionPasoLogroList = misionPasoLogroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaso != null ? idPaso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paso)) {
            return false;
        }
        Paso other = (Paso) object;
        if ((this.idPaso == null && other.idPaso != null) || (this.idPaso != null && !this.idPaso.equals(other.idPaso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Paso[ idPaso=" + idPaso + " ]";
    }
    
}
