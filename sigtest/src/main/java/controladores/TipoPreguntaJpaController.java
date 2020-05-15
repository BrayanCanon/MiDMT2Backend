/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Pregunta;
import entidades.TipoPregunta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class TipoPreguntaJpaController implements Serializable {

    public TipoPreguntaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPregunta tipoPregunta) {
        if (tipoPregunta.getPreguntaList() == null) {
            tipoPregunta.setPreguntaList(new ArrayList<Pregunta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pregunta> attachedPreguntaList = new ArrayList<Pregunta>();
            for (Pregunta preguntaListPreguntaToAttach : tipoPregunta.getPreguntaList()) {
                preguntaListPreguntaToAttach = em.getReference(preguntaListPreguntaToAttach.getClass(), preguntaListPreguntaToAttach.getIdPregunta());
                attachedPreguntaList.add(preguntaListPreguntaToAttach);
            }
            tipoPregunta.setPreguntaList(attachedPreguntaList);
            em.persist(tipoPregunta);
            for (Pregunta preguntaListPregunta : tipoPregunta.getPreguntaList()) {
                TipoPregunta oldIdTipoPreguntaOfPreguntaListPregunta = preguntaListPregunta.getIdTipoPregunta();
                preguntaListPregunta.setIdTipoPregunta(tipoPregunta);
                preguntaListPregunta = em.merge(preguntaListPregunta);
                if (oldIdTipoPreguntaOfPreguntaListPregunta != null) {
                    oldIdTipoPreguntaOfPreguntaListPregunta.getPreguntaList().remove(preguntaListPregunta);
                    oldIdTipoPreguntaOfPreguntaListPregunta = em.merge(oldIdTipoPreguntaOfPreguntaListPregunta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPregunta tipoPregunta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPregunta persistentTipoPregunta = em.find(TipoPregunta.class, tipoPregunta.getIdTipoPregunta());
            List<Pregunta> preguntaListOld = persistentTipoPregunta.getPreguntaList();
            List<Pregunta> preguntaListNew = tipoPregunta.getPreguntaList();
            List<String> illegalOrphanMessages = null;
            for (Pregunta preguntaListOldPregunta : preguntaListOld) {
                if (!preguntaListNew.contains(preguntaListOldPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pregunta " + preguntaListOldPregunta + " since its idTipoPregunta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pregunta> attachedPreguntaListNew = new ArrayList<Pregunta>();
            for (Pregunta preguntaListNewPreguntaToAttach : preguntaListNew) {
                preguntaListNewPreguntaToAttach = em.getReference(preguntaListNewPreguntaToAttach.getClass(), preguntaListNewPreguntaToAttach.getIdPregunta());
                attachedPreguntaListNew.add(preguntaListNewPreguntaToAttach);
            }
            preguntaListNew = attachedPreguntaListNew;
            tipoPregunta.setPreguntaList(preguntaListNew);
            tipoPregunta = em.merge(tipoPregunta);
            for (Pregunta preguntaListNewPregunta : preguntaListNew) {
                if (!preguntaListOld.contains(preguntaListNewPregunta)) {
                    TipoPregunta oldIdTipoPreguntaOfPreguntaListNewPregunta = preguntaListNewPregunta.getIdTipoPregunta();
                    preguntaListNewPregunta.setIdTipoPregunta(tipoPregunta);
                    preguntaListNewPregunta = em.merge(preguntaListNewPregunta);
                    if (oldIdTipoPreguntaOfPreguntaListNewPregunta != null && !oldIdTipoPreguntaOfPreguntaListNewPregunta.equals(tipoPregunta)) {
                        oldIdTipoPreguntaOfPreguntaListNewPregunta.getPreguntaList().remove(preguntaListNewPregunta);
                        oldIdTipoPreguntaOfPreguntaListNewPregunta = em.merge(oldIdTipoPreguntaOfPreguntaListNewPregunta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPregunta.getIdTipoPregunta();
                if (findTipoPregunta(id) == null) {
                    throw new NonexistentEntityException("The tipoPregunta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPregunta tipoPregunta;
            try {
                tipoPregunta = em.getReference(TipoPregunta.class, id);
                tipoPregunta.getIdTipoPregunta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPregunta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pregunta> preguntaListOrphanCheck = tipoPregunta.getPreguntaList();
            for (Pregunta preguntaListOrphanCheckPregunta : preguntaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoPregunta (" + tipoPregunta + ") cannot be destroyed since the Pregunta " + preguntaListOrphanCheckPregunta + " in its preguntaList field has a non-nullable idTipoPregunta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoPregunta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPregunta> findTipoPreguntaEntities() {
        return findTipoPreguntaEntities(true, -1, -1);
    }

    public List<TipoPregunta> findTipoPreguntaEntities(int maxResults, int firstResult) {
        return findTipoPreguntaEntities(false, maxResults, firstResult);
    }

    private List<TipoPregunta> findTipoPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPregunta.class));
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

    public TipoPregunta findTipoPregunta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPregunta> rt = cq.from(TipoPregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public TipoPregunta busTipoPreguntaId(String id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM TipoPregunta a WHERE a.nomPregunta=:id ");
        con.setParameter("id",id);
   
        List<TipoPregunta> cat= con.getResultList();
        return cat.get(0);
    } 
}
