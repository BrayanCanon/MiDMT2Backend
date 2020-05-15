/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Paso;
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
public class PasoJpaController implements Serializable {
public int cod;
    public PasoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paso paso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(paso);
             em.flush();
            cod=paso.getIdPaso();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paso paso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            paso = em.merge(paso);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paso.getIdPaso();
                if (findPaso(id) == null) {
                    throw new NonexistentEntityException("The paso with id " + id + " no longer exists.");
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
            Paso paso;
            try {
                paso = em.getReference(Paso.class, id);
                paso.getIdPaso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paso with id " + id + " no longer exists.", enfe);
            }
            em.remove(paso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paso> findPasoEntities() {
        return findPasoEntities(true, -1, -1);
    }

    public List<Paso> findPasoEntities(int maxResults, int firstResult) {
        return findPasoEntities(false, maxResults, firstResult);
    }

    private List<Paso> findPasoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paso.class));
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

    public Paso findPaso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPasoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paso> rt = cq.from(Paso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
 public List<Paso> consultapasocat(Integer cat){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Paso a WHERE a.categoria=:categ");
        con.setParameter("categ", cat);


        List<Paso> n=con.getResultList();
        return n;}
}
