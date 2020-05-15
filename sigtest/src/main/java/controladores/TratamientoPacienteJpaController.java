/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.TratamientoPaciente;
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
public class TratamientoPacienteJpaController implements Serializable {

    public TratamientoPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TratamientoPaciente tratamientoPaciente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tratamientoPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TratamientoPaciente tratamientoPaciente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tratamientoPaciente = em.merge(tratamientoPaciente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tratamientoPaciente.getIdPerfilTratamiento();
                if (findTratamientoPaciente(id) == null) {
                    throw new NonexistentEntityException("The tratamientoPaciente with id " + id + " no longer exists.");
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
            TratamientoPaciente tratamientoPaciente;
            try {
                tratamientoPaciente = em.getReference(TratamientoPaciente.class, id);
                tratamientoPaciente.getIdPerfilTratamiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tratamientoPaciente with id " + id + " no longer exists.", enfe);
            }
            em.remove(tratamientoPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TratamientoPaciente> findTratamientoPacienteEntities() {
        return findTratamientoPacienteEntities(true, -1, -1);
    }

    public List<TratamientoPaciente> findTratamientoPacienteEntities(int maxResults, int firstResult) {
        return findTratamientoPacienteEntities(false, maxResults, firstResult);
    }

    private List<TratamientoPaciente> findTratamientoPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TratamientoPaciente.class));
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

    public TratamientoPaciente findTratamientoPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TratamientoPaciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getTratamientoPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TratamientoPaciente> rt = cq.from(TratamientoPaciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
