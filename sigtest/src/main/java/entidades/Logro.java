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
import javax.persistence.Lob;
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
@Table(name = "logro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logro.findAll", query = "SELECT l FROM Logro l")
    , @NamedQuery(name = "Logro.findByIdLogro", query = "SELECT l FROM Logro l WHERE l.idLogro = :idLogro")
    , @NamedQuery(name = "Logro.findByPuntos", query = "SELECT l FROM Logro l WHERE l.puntos = :puntos")
    , @NamedQuery(name = "Logro.findByEstado", query = "SELECT l FROM Logro l WHERE l.estado = :estado")})
public class Logro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_logro")
    @Expose
    private Integer idLogro;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "nom_logro")
    @Expose
    private String nomLogro;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "descripcion")
    @Expose
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "puntos")
    @Expose
    private int puntos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    @Expose
    private String estado;
    @OneToMany(mappedBy = "idLogro")
    private List<MisionPasoLogro> misionPasoLogroList;

    public Logro() {
    }

    public Logro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Logro(Integer idLogro, String nomLogro, String descripcion, int puntos, String estado) {
        this.idLogro = idLogro;
        this.nomLogro = nomLogro;
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.estado = estado;
    }

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public String getNomLogro() {
        return nomLogro;
    }

    public void setNomLogro(String nomLogro) {
        this.nomLogro = nomLogro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        hash += (idLogro != null ? idLogro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logro)) {
            return false;
        }
        Logro other = (Logro) object;
        if ((this.idLogro == null && other.idLogro != null) || (this.idLogro != null && !this.idLogro.equals(other.idLogro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Logro[ idLogro=" + idLogro + " ]";
    }
    
}
