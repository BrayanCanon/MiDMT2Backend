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
import entidades.TipoEncuesta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class TipoEncuestaJpaController implements Serializable {

    public TipoEncuestaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoEncuesta tipoEncuesta) {
        if (tipoEncuesta.getEncuestaList() == null) {
            tipoEncuesta.setEncuestaList(new ArrayList<Encuesta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Encuesta> attachedEncuestaList = new ArrayList<Encuesta>();
            for (Encuesta encuestaListEncuestaToAttach : tipoEncuesta.getEncuestaList()) {
                encuestaListEncuestaToAttach = em.getReference(encuestaListEncuestaToAttach.getClass(), encuestaListEncuestaToAttach.getIdEncuesta());
                attachedEncuestaList.add(encuestaListEncuestaToAttach);
            }
            tipoEncuesta.setEncuestaList(attachedEncuestaList);
            em.persist(tipoEncuesta);
            for (Encuesta encuestaListEncuesta : tipoEncuesta.getEncuestaList()) {
                TipoEncuesta oldIdTipoEncuestaOfEncuestaListEncuesta = encuestaListEncuesta.getIdTipoEncuesta();
                encuestaListEncuesta.setIdTipoEncuesta(tipoEncuesta);
                encuestaListEncuesta = em.merge(encuestaListEncuesta);
                if (oldIdTipoEncuestaOfEncuestaListEncuesta != null) {
                    oldIdTipoEncuestaOfEncuestaListEncuesta.getEncuestaList().remove(encuestaListEncuesta);
                    oldIdTipoEncuestaOfEncuestaListEncuesta = em.merge(oldIdTipoEncuestaOfEncuestaListEncuesta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoEncuesta tipoEncuesta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEncuesta persistentTipoEncuesta = em.find(TipoEncuesta.class, tipoEncuesta.getIdTipoEncuesta());
            List<Encuesta> encuestaListOld = persistentTipoEncuesta.getEncuestaList();
            List<Encuesta> encuestaListNew = tipoEncuesta.getEncuestaList();
            List<Encuesta> attachedEncuestaListNew = new ArrayList<Encuesta>();
            for (Encuesta encuestaListNewEncuestaToAttach : encuestaListNew) {
                encuestaListNewEncuestaToAttach = em.getReference(encuestaListNewEncuestaToAttach.getClass(), encuestaListNewEncuestaToAttach.getIdEncuesta());
                attachedEncuestaListNew.add(encuestaListNewEncuestaToAttach);
            }
            encuestaListNew = attachedEncuestaListNew;
            tipoEncuesta.setEncuestaList(encuestaListNew);
            tipoEncuesta = em.merge(tipoEncuesta);
            for (Encuesta encuestaListOldEncuesta : encuestaListOld) {
                if (!encuestaListNew.contains(encuestaListOldEncuesta)) {
                    encuestaListOldEncuesta.setIdTipoEncuesta(null);
                    encuestaListOldEncuesta = em.merge(encuestaListOldEncuesta);
                }
            }
            for (Encuesta encuestaListNewEncuesta : encuestaListNew) {
                if (!encuestaListOld.contains(encuestaListNewEncuesta)) {
                    TipoEncuesta oldIdTipoEncuestaOfEncuestaListNewEncuesta = encuestaListNewEncuesta.getIdTipoEncuesta();
                    encuestaListNewEncuesta.setIdTipoEncuesta(tipoEncuesta);
                    encuestaListNewEncuesta = em.merge(encuestaListNewEncuesta);
                    if (oldIdTipoEncuestaOfEncuestaListNewEncuesta != null && !oldIdTipoEncuestaOfEncuestaListNewEncuesta.equals(tipoEncuesta)) {
                        oldIdTipoEncuestaOfEncuestaListNewEncuesta.getEncuestaList().remove(encuestaListNewEncuesta);
                        oldIdTipoEncuestaOfEncuestaListNewEncuesta = em.merge(oldIdTipoEncuestaOfEncuestaListNewEncuesta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoEncuesta.getIdTipoEncuesta();
                if (findTipoEncuesta(id) == null) {
                    throw new NonexistentEntityException("The tipoEncuesta with id " + id + " no longer exists.");
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
            TipoEncuesta tipoEncuesta;
            try {
                tipoEncuesta = em.getReference(TipoEncuesta.class, id);
                tipoEncuesta.getIdTipoEncuesta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoEncuesta with id " + id + " no longer exists.", enfe);
            }
            List<Encuesta> encuestaList = tipoEncuesta.getEncuestaList();
            for (Encuesta encuestaListEncuesta : encuestaList) {
                encuestaListEncuesta.setIdTipoEncuesta(null);
                encuestaListEncuesta = em.merge(encuestaListEncuesta);
            }
            em.remove(tipoEncuesta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoEncuesta> findTipoEncuestaEntities() {
        return findTipoEncuestaEntities(true, -1, -1);
    }

    public List<TipoEncuesta> findTipoEncuestaEntities(int maxResults, int firstResult) {
        return findTipoEncuestaEntities(false, maxResults, firstResult);
    }

    private List<TipoEncuesta> findTipoEncuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoEncuesta.class));
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

    public TipoEncuesta findTipoEncuesta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoEncuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoEncuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoEncuesta> rt = cq.from(TipoEncuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public TipoEncuesta busTipoEncuestaId(String id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM TipoEncuesta a WHERE a.nomTipoEncuesta=:id ");
        con.setParameter("id",id);
   
        List<TipoEncuesta> cat= con.getResultList();
        return cat.get(0);
    } 
}
