/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.NivelPaciente;
import entidades.Paciente;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author gulmi
 */
public class NivelPacienteJpaController implements Serializable {

    public NivelPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NivelPaciente nivelPaciente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(nivelPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NivelPaciente nivelPaciente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            nivelPaciente = em.merge(nivelPaciente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivelPaciente.getIdNivel();
                if (findNivelPaciente(id) == null) {
                    throw new NonexistentEntityException("The nivelPaciente with id " + id + " no longer exists.");
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
            NivelPaciente nivelPaciente;
            try {
                nivelPaciente = em.getReference(NivelPaciente.class, id);
                nivelPaciente.getIdNivel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivelPaciente with id " + id + " no longer exists.", enfe);
            }
            em.remove(nivelPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NivelPaciente> findNivelPacienteEntities() {
        return findNivelPacienteEntities(true, -1, -1);
    }

    public List<NivelPaciente> findNivelPacienteEntities(int maxResults, int firstResult) {
        return findNivelPacienteEntities(false, maxResults, firstResult);
    }

    private List<NivelPaciente> findNivelPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NivelPaciente.class));
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

    public NivelPaciente findNivelPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NivelPaciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NivelPaciente> rt = cq.from(NivelPaciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public NivelPaciente sqlbuscNombreNivel(String us){
        EntityManager em = emf.createEntityManager();
      
        Query con= em.createQuery("SELECT a FROM NivelPaciente a WHERE a.nombreNivel=:cod ");
        con.setParameter("cod",us);
        return (NivelPaciente) con.getResultList().get(0);
    }
      public List<NivelPaciente> filtroNivelEjercicio(Paciente mi){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM NivelPaciente a WHERE a.puntajeReq<=:pa  AND a.estado=:estado");
        con.setParameter("pa",mi.getPuntajeEjer());
        con.setParameter("estado", "e");
        return con.getResultList();
        
    }
        public List<NivelPaciente> filtroNivelAlimentacion(Paciente mi){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM NivelPaciente a WHERE a.puntajeReq<=:pa  AND a.estado=:estado");
        con.setParameter("pa",mi.getPuntajeAlim());
        con.setParameter("estado", "a");
        return con.getResultList();
        
    }
     
}
