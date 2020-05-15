/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ApoyoSocial;
import entidades.Asignacion;
import entidades.Paciente;
import entidades.Profesional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class AsignacionJpaController implements Serializable {

    public AsignacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignacion asignacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApoyoSocial codApoyosocial = asignacion.getCodApoyosocial();
            if (codApoyosocial != null) {
                codApoyosocial = em.getReference(codApoyosocial.getClass(), codApoyosocial.getCodApoyosocial());
                asignacion.setCodApoyosocial(codApoyosocial);
            }
            Profesional codProfesional = asignacion.getCodProfesional();
            if (codProfesional != null) {
                codProfesional = em.getReference(codProfesional.getClass(), codProfesional.getCodProfesional());
                asignacion.setCodProfesional(codProfesional);
            }
            em.persist(asignacion);
            if (codApoyosocial != null) {
                codApoyosocial.getAsignacionList().add(asignacion);
                codApoyosocial = em.merge(codApoyosocial);
            }
            if (codProfesional != null) {
                codProfesional.getAsignacionList().add(asignacion);
                codProfesional = em.merge(codProfesional);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignacion asignacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignacion persistentAsignacion = em.find(Asignacion.class, asignacion.getCodAsginacion());
            ApoyoSocial codApoyosocialOld = persistentAsignacion.getCodApoyosocial();
            ApoyoSocial codApoyosocialNew = asignacion.getCodApoyosocial();
            Profesional codProfesionalOld = persistentAsignacion.getCodProfesional();
            Profesional codProfesionalNew = asignacion.getCodProfesional();
            if (codApoyosocialNew != null) {
                codApoyosocialNew = em.getReference(codApoyosocialNew.getClass(), codApoyosocialNew.getCodApoyosocial());
                asignacion.setCodApoyosocial(codApoyosocialNew);
            }
            if (codProfesionalNew != null) {
                codProfesionalNew = em.getReference(codProfesionalNew.getClass(), codProfesionalNew.getCodProfesional());
                asignacion.setCodProfesional(codProfesionalNew);
            }
            asignacion = em.merge(asignacion);
            if (codApoyosocialOld != null && !codApoyosocialOld.equals(codApoyosocialNew)) {
                codApoyosocialOld.getAsignacionList().remove(asignacion);
                codApoyosocialOld = em.merge(codApoyosocialOld);
            }
            if (codApoyosocialNew != null && !codApoyosocialNew.equals(codApoyosocialOld)) {
                codApoyosocialNew.getAsignacionList().add(asignacion);
                codApoyosocialNew = em.merge(codApoyosocialNew);
            }
            if (codProfesionalOld != null && !codProfesionalOld.equals(codProfesionalNew)) {
                codProfesionalOld.getAsignacionList().remove(asignacion);
                codProfesionalOld = em.merge(codProfesionalOld);
            }
            if (codProfesionalNew != null && !codProfesionalNew.equals(codProfesionalOld)) {
                codProfesionalNew.getAsignacionList().add(asignacion);
                codProfesionalNew = em.merge(codProfesionalNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asignacion.getCodAsginacion();
                if (findAsignacion(id) == null) {
                    throw new NonexistentEntityException("The asignacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignacion asignacion;
            try {
                asignacion = em.getReference(Asignacion.class, id);
                asignacion.getCodAsginacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignacion with id " + id + " no longer exists.", enfe);
            }
            ApoyoSocial codApoyosocial = asignacion.getCodApoyosocial();
            if (codApoyosocial != null) {
                codApoyosocial.getAsignacionList().remove(asignacion);
                codApoyosocial = em.merge(codApoyosocial);
            }
            Profesional codProfesional = asignacion.getCodProfesional();
            if (codProfesional != null) {
                codProfesional.getAsignacionList().remove(asignacion);
                codProfesional = em.merge(codProfesional);
            }
            em.remove(asignacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asignacion> findAsignacionEntities() {
        return findAsignacionEntities(true, -1, -1);
    }

    public List<Asignacion> findAsignacionEntities(int maxResults, int firstResult) {
        return findAsignacionEntities(false, maxResults, firstResult);
    }

    private List<Asignacion> findAsignacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Asignacion findAsignacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignacion> rt = cq.from(Asignacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Asignacion sqlbusc(String id){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Asignacion a WHERE a.codPaciente.codPaciente = :id" );
        con.setParameter("id",id);
        return (Asignacion) con.getResultList().get(0);
    }
   
    public Asignacion sqlbuscFamiliar(String id){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Asignacion a WHERE a.codApoyosocial.codApoyosocial = :id" );
        con.setParameter("id",id);
        return (Asignacion) con.getResultList().get(0);
    }
    public List<Asignacion> asignacionPacientelista(Paciente an){
         EntityManager em = emf.createEntityManager();
 
        Query con= em.createQuery("SELECT a FROM Asignacion a WHERE a.codPaciente=:codPaciente AND a.estado=:estado AND a.codApoyosocial IS NULL");
        con.setParameter("codPaciente", an);
        con.setParameter("estado", "a");
        List<Asignacion> sd=con.getResultList();
        if(sd.size()>0){
        return sd;
}
        return null;
 
    }
}
