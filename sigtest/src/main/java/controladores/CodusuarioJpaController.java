/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Codusuario;
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
public class CodusuarioJpaController implements Serializable {

    public CodusuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Codusuario codusuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(codusuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Codusuario codusuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            codusuario = em.merge(codusuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = codusuario.getIdUsuario();
                if (findCodusuario(id) == null) {
                    throw new NonexistentEntityException("The codusuario with id " + id + " no longer exists.");
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
            Codusuario codusuario;
            try {
                codusuario = em.getReference(Codusuario.class, id);
                codusuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The codusuario with id " + id + " no longer exists.", enfe);
            }
            em.remove(codusuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Codusuario> findCodusuarioEntities() {
        return findCodusuarioEntities(true, -1, -1);
    }

    public List<Codusuario> findCodusuarioEntities(int maxResults, int firstResult) {
        return findCodusuarioEntities(false, maxResults, firstResult);
    }

    private List<Codusuario> findCodusuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Codusuario.class));
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

    public Codusuario findCodusuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Codusuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getCodusuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Codusuario> rt = cq.from(Codusuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Codusuario sqlbuscCod(Codusuario cu) {
         EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Codusuario a WHERE a.nomUsuario=:cod ");
        con.setParameter("cod",cu.getNomUsuario());
        return (Codusuario) con.getResultList().get(0);
    }
    
}