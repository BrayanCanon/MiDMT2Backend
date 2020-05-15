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
import entidades.Notificacion;
import entidades.TipoRecurso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class TipoRecursoJpaController implements Serializable {

    public TipoRecursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoRecurso tipoRecurso) {
        if (tipoRecurso.getNotificacionList() == null) {
            tipoRecurso.setNotificacionList(new ArrayList<Notificacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Notificacion> attachedNotificacionList = new ArrayList<Notificacion>();
            for (Notificacion notificacionListNotificacionToAttach : tipoRecurso.getNotificacionList()) {
                notificacionListNotificacionToAttach = em.getReference(notificacionListNotificacionToAttach.getClass(), notificacionListNotificacionToAttach.getIdnotificacion());
                attachedNotificacionList.add(notificacionListNotificacionToAttach);
            }
            tipoRecurso.setNotificacionList(attachedNotificacionList);
            em.persist(tipoRecurso);
            for (Notificacion notificacionListNotificacion : tipoRecurso.getNotificacionList()) {
                TipoRecurso oldIdTipoRecursoOfNotificacionListNotificacion = notificacionListNotificacion.getIdTipoRecurso();
                notificacionListNotificacion.setIdTipoRecurso(tipoRecurso);
                notificacionListNotificacion = em.merge(notificacionListNotificacion);
                if (oldIdTipoRecursoOfNotificacionListNotificacion != null) {
                    oldIdTipoRecursoOfNotificacionListNotificacion.getNotificacionList().remove(notificacionListNotificacion);
                    oldIdTipoRecursoOfNotificacionListNotificacion = em.merge(oldIdTipoRecursoOfNotificacionListNotificacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoRecurso tipoRecurso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoRecurso persistentTipoRecurso = em.find(TipoRecurso.class, tipoRecurso.getIdTipoRecurso());
            List<Notificacion> notificacionListOld = persistentTipoRecurso.getNotificacionList();
            List<Notificacion> notificacionListNew = tipoRecurso.getNotificacionList();
            List<String> illegalOrphanMessages = null;
            for (Notificacion notificacionListOldNotificacion : notificacionListOld) {
                if (!notificacionListNew.contains(notificacionListOldNotificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notificacion " + notificacionListOldNotificacion + " since its idTipoRecurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Notificacion> attachedNotificacionListNew = new ArrayList<Notificacion>();
            for (Notificacion notificacionListNewNotificacionToAttach : notificacionListNew) {
                notificacionListNewNotificacionToAttach = em.getReference(notificacionListNewNotificacionToAttach.getClass(), notificacionListNewNotificacionToAttach.getIdnotificacion());
                attachedNotificacionListNew.add(notificacionListNewNotificacionToAttach);
            }
            notificacionListNew = attachedNotificacionListNew;
            tipoRecurso.setNotificacionList(notificacionListNew);
            tipoRecurso = em.merge(tipoRecurso);
            for (Notificacion notificacionListNewNotificacion : notificacionListNew) {
                if (!notificacionListOld.contains(notificacionListNewNotificacion)) {
                    TipoRecurso oldIdTipoRecursoOfNotificacionListNewNotificacion = notificacionListNewNotificacion.getIdTipoRecurso();
                    notificacionListNewNotificacion.setIdTipoRecurso(tipoRecurso);
                    notificacionListNewNotificacion = em.merge(notificacionListNewNotificacion);
                    if (oldIdTipoRecursoOfNotificacionListNewNotificacion != null && !oldIdTipoRecursoOfNotificacionListNewNotificacion.equals(tipoRecurso)) {
                        oldIdTipoRecursoOfNotificacionListNewNotificacion.getNotificacionList().remove(notificacionListNewNotificacion);
                        oldIdTipoRecursoOfNotificacionListNewNotificacion = em.merge(oldIdTipoRecursoOfNotificacionListNewNotificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoRecurso.getIdTipoRecurso();
                if (findTipoRecurso(id) == null) {
                    throw new NonexistentEntityException("The tipoRecurso with id " + id + " no longer exists.");
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
            TipoRecurso tipoRecurso;
            try {
                tipoRecurso = em.getReference(TipoRecurso.class, id);
                tipoRecurso.getIdTipoRecurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoRecurso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Notificacion> notificacionListOrphanCheck = tipoRecurso.getNotificacionList();
            for (Notificacion notificacionListOrphanCheckNotificacion : notificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoRecurso (" + tipoRecurso + ") cannot be destroyed since the Notificacion " + notificacionListOrphanCheckNotificacion + " in its notificacionList field has a non-nullable idTipoRecurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoRecurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoRecurso> findTipoRecursoEntities() {
        return findTipoRecursoEntities(true, -1, -1);
    }

    public List<TipoRecurso> findTipoRecursoEntities(int maxResults, int firstResult) {
        return findTipoRecursoEntities(false, maxResults, firstResult);
    }

    private List<TipoRecurso> findTipoRecursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoRecurso.class));
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

    public TipoRecurso findTipoRecurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoRecurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoRecursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoRecurso> rt = cq.from(TipoRecurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<TipoRecurso> sqlbuscRecurso(TipoRecurso us){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM TipoRecurso a WHERE a.nombreRecurso=:cod ");
        con.setParameter("cod",us.getNombreRecurso());
        return  con.getResultList();
    }
    
}
