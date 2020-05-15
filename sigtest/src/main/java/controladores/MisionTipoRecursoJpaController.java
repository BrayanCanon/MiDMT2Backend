/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Mision;
import entidades.MisionTipoRecurso;
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
public class MisionTipoRecursoJpaController implements Serializable {

    public MisionTipoRecursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MisionTipoRecurso misionTipoRecurso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(misionTipoRecurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MisionTipoRecurso misionTipoRecurso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            misionTipoRecurso = em.merge(misionTipoRecurso);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = misionTipoRecurso.getIdMisionTipor();
                if (findMisionTipoRecurso(id) == null) {
                    throw new NonexistentEntityException("The misionTipoRecurso with id " + id + " no longer exists.");
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
            MisionTipoRecurso misionTipoRecurso;
            try {
                misionTipoRecurso = em.getReference(MisionTipoRecurso.class, id);
                misionTipoRecurso.getIdMisionTipor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The misionTipoRecurso with id " + id + " no longer exists.", enfe);
            }
            em.remove(misionTipoRecurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MisionTipoRecurso> findMisionTipoRecursoEntities() {
        return findMisionTipoRecursoEntities(true, -1, -1);
    }

    public List<MisionTipoRecurso> findMisionTipoRecursoEntities(int maxResults, int firstResult) {
        return findMisionTipoRecursoEntities(false, maxResults, firstResult);
    }

    private List<MisionTipoRecurso> findMisionTipoRecursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MisionTipoRecurso.class));
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

    public MisionTipoRecurso findMisionTipoRecurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MisionTipoRecurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getMisionTipoRecursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MisionTipoRecurso> rt = cq.from(MisionTipoRecurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<MisionTipoRecurso> sqlbuscRecursoMision(Mision us){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionTipoRecurso a WHERE a.idMision=:cod AND a.idTipoRecurso=:cod2");
        con.setParameter("cod",us);
        con.setParameter("cod2",1);
        return  con.getResultList();
    }
      public List<MisionTipoRecurso> sqlbuscForoMision(Mision us){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionTipoRecurso a WHERE a.idMision=:cod AND a.idTipoRecurso=:cod2");
        con.setParameter("cod",us);
        con.setParameter("cod2",2);
        return  con.getResultList();
    }
      
        public MisionTipoRecurso sqlbuscPorRecurso(int a){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionTipoRecurso a WHERE a.idTipoRecurso=:cod");
        con.setParameter("cod",a);
        
        return  (MisionTipoRecurso) con.getResultList().get(0);
    }
        public MisionTipoRecurso sqlbuscPorRecursoYMision(int c, Mision b){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionTipoRecurso a WHERE a.idRecurso=:cod and a.idMision=:cod2 and a.estado=:a");
        con.setParameter("cod",c);
        con.setParameter("cod2",b);
        String este="a";
        con.setParameter("a",este);
        return  (MisionTipoRecurso) con.getResultList().get(0);
    }
}
