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
import entidades.Encuesta;
import entidades.Pregunta;
import entidades.TipoPregunta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class PreguntaJpaController implements Serializable {

    public PreguntaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pregunta pregunta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta idEncuesta = pregunta.getIdEncuesta();
            if (idEncuesta != null) {
                idEncuesta = em.getReference(idEncuesta.getClass(), idEncuesta.getIdEncuesta());
                pregunta.setIdEncuesta(idEncuesta);
            }
            TipoPregunta idTipoPregunta = pregunta.getIdTipoPregunta();
            if (idTipoPregunta != null) {
                idTipoPregunta = em.getReference(idTipoPregunta.getClass(), idTipoPregunta.getIdTipoPregunta());
                pregunta.setIdTipoPregunta(idTipoPregunta);
            }
            em.persist(pregunta);
            if (idEncuesta != null) {
                idEncuesta.getPreguntaList().add(pregunta);
                idEncuesta = em.merge(idEncuesta);
            }
            if (idTipoPregunta != null) {
                idTipoPregunta.getPreguntaList().add(pregunta);
                idTipoPregunta = em.merge(idTipoPregunta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pregunta pregunta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getIdPregunta());
            Encuesta idEncuestaOld = persistentPregunta.getIdEncuesta();
            Encuesta idEncuestaNew = pregunta.getIdEncuesta();
            TipoPregunta idTipoPreguntaOld = persistentPregunta.getIdTipoPregunta();
            TipoPregunta idTipoPreguntaNew = pregunta.getIdTipoPregunta();
            if (idEncuestaNew != null) {
                idEncuestaNew = em.getReference(idEncuestaNew.getClass(), idEncuestaNew.getIdEncuesta());
                pregunta.setIdEncuesta(idEncuestaNew);
            }
            if (idTipoPreguntaNew != null) {
                idTipoPreguntaNew = em.getReference(idTipoPreguntaNew.getClass(), idTipoPreguntaNew.getIdTipoPregunta());
                pregunta.setIdTipoPregunta(idTipoPreguntaNew);
            }
            pregunta = em.merge(pregunta);
            if (idEncuestaOld != null && !idEncuestaOld.equals(idEncuestaNew)) {
                idEncuestaOld.getPreguntaList().remove(pregunta);
                idEncuestaOld = em.merge(idEncuestaOld);
            }
            if (idEncuestaNew != null && !idEncuestaNew.equals(idEncuestaOld)) {
                idEncuestaNew.getPreguntaList().add(pregunta);
                idEncuestaNew = em.merge(idEncuestaNew);
            }
            if (idTipoPreguntaOld != null && !idTipoPreguntaOld.equals(idTipoPreguntaNew)) {
                idTipoPreguntaOld.getPreguntaList().remove(pregunta);
                idTipoPreguntaOld = em.merge(idTipoPreguntaOld);
            }
            if (idTipoPreguntaNew != null && !idTipoPreguntaNew.equals(idTipoPreguntaOld)) {
                idTipoPreguntaNew.getPreguntaList().add(pregunta);
                idTipoPreguntaNew = em.merge(idTipoPreguntaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pregunta.getIdPregunta();
                if (findPregunta(id) == null) {
                    throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.");
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
            Pregunta pregunta;
            try {
                pregunta = em.getReference(Pregunta.class, id);
                pregunta.getIdPregunta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.", enfe);
            }
            Encuesta idEncuesta = pregunta.getIdEncuesta();
            if (idEncuesta != null) {
                idEncuesta.getPreguntaList().remove(pregunta);
                idEncuesta = em.merge(idEncuesta);
            }
            TipoPregunta idTipoPregunta = pregunta.getIdTipoPregunta();
            if (idTipoPregunta != null) {
                idTipoPregunta.getPreguntaList().remove(pregunta);
                idTipoPregunta = em.merge(idTipoPregunta);
            }
            em.remove(pregunta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pregunta> findPreguntaEntities() {
        return findPreguntaEntities(true, -1, -1);
    }

    public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
        return findPreguntaEntities(false, maxResults, firstResult);
    }

    private List<Pregunta> findPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pregunta.class));
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

    public Pregunta findPregunta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pregunta> rt = cq.from(Pregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<Pregunta> busPreguntaId(Encuesta id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Pregunta a WHERE a.idEncuesta=:id ");
        con.setParameter("id",id);

   
        List<Pregunta> cat= con.getResultList();
        return cat;
    } 
      public List<Pregunta> busPreguntaId(Encuesta id,TipoPregunta id2) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Pregunta a WHERE a.idEncuesta=:id AND a.idTipoPregunta:id2 ");
        con.setParameter("id",id);
        con.setParameter("id2",id2);
   
        List<Pregunta> cat= con.getResultList();
        return cat;
    } 
}
