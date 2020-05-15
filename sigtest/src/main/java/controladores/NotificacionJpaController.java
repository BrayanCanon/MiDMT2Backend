/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Notificacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.TipoRecurso;
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class NotificacionJpaController implements Serializable {

    public NotificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Notificacion notificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoRecurso idTipoRecurso = notificacion.getIdTipoRecurso();
            if (idTipoRecurso != null) {
                idTipoRecurso = em.getReference(idTipoRecurso.getClass(), idTipoRecurso.getIdTipoRecurso());
                notificacion.setIdTipoRecurso(idTipoRecurso);
            }
            Usuario nomUsuario = notificacion.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario = em.getReference(nomUsuario.getClass(), nomUsuario.getNomUsuario());
                notificacion.setNomUsuario(nomUsuario);
            }
            em.persist(notificacion);
            if (idTipoRecurso != null) {
                idTipoRecurso.getNotificacionList().add(notificacion);
                idTipoRecurso = em.merge(idTipoRecurso);
            }
            if (nomUsuario != null) {
                nomUsuario.getNotificacionList().add(notificacion);
                nomUsuario = em.merge(nomUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Notificacion notificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notificacion persistentNotificacion = em.find(Notificacion.class, notificacion.getIdnotificacion());
            TipoRecurso idTipoRecursoOld = persistentNotificacion.getIdTipoRecurso();
            TipoRecurso idTipoRecursoNew = notificacion.getIdTipoRecurso();
            Usuario nomUsuarioOld = persistentNotificacion.getNomUsuario();
            Usuario nomUsuarioNew = notificacion.getNomUsuario();
            if (idTipoRecursoNew != null) {
                idTipoRecursoNew = em.getReference(idTipoRecursoNew.getClass(), idTipoRecursoNew.getIdTipoRecurso());
                notificacion.setIdTipoRecurso(idTipoRecursoNew);
            }
            if (nomUsuarioNew != null) {
                nomUsuarioNew = em.getReference(nomUsuarioNew.getClass(), nomUsuarioNew.getNomUsuario());
                notificacion.setNomUsuario(nomUsuarioNew);
            }
            notificacion = em.merge(notificacion);
            if (idTipoRecursoOld != null && !idTipoRecursoOld.equals(idTipoRecursoNew)) {
                idTipoRecursoOld.getNotificacionList().remove(notificacion);
                idTipoRecursoOld = em.merge(idTipoRecursoOld);
            }
            if (idTipoRecursoNew != null && !idTipoRecursoNew.equals(idTipoRecursoOld)) {
                idTipoRecursoNew.getNotificacionList().add(notificacion);
                idTipoRecursoNew = em.merge(idTipoRecursoNew);
            }
            if (nomUsuarioOld != null && !nomUsuarioOld.equals(nomUsuarioNew)) {
                nomUsuarioOld.getNotificacionList().remove(notificacion);
                nomUsuarioOld = em.merge(nomUsuarioOld);
            }
            if (nomUsuarioNew != null && !nomUsuarioNew.equals(nomUsuarioOld)) {
                nomUsuarioNew.getNotificacionList().add(notificacion);
                nomUsuarioNew = em.merge(nomUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = notificacion.getIdnotificacion();
                if (findNotificacion(id) == null) {
                    throw new NonexistentEntityException("The notificacion with id " + id + " no longer exists.");
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
            Notificacion notificacion;
            try {
                notificacion = em.getReference(Notificacion.class, id);
                notificacion.getIdnotificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notificacion with id " + id + " no longer exists.", enfe);
            }
            TipoRecurso idTipoRecurso = notificacion.getIdTipoRecurso();
            if (idTipoRecurso != null) {
                idTipoRecurso.getNotificacionList().remove(notificacion);
                idTipoRecurso = em.merge(idTipoRecurso);
            }
            Usuario nomUsuario = notificacion.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario.getNotificacionList().remove(notificacion);
                nomUsuario = em.merge(nomUsuario);
            }
            em.remove(notificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Notificacion> findNotificacionEntities() {
        return findNotificacionEntities(true, -1, -1);
    }

    public List<Notificacion> findNotificacionEntities(int maxResults, int firstResult) {
        return findNotificacionEntities(false, maxResults, firstResult);
    }

    private List<Notificacion> findNotificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Notificacion.class));
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

    public Notificacion findNotificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notificacion> rt = cq.from(Notificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<Notificacion> busNotificacionUusuario(Usuario id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Notificacion a WHERE a.nomUsuario=:id ");
        con.setParameter("id",id);
        
        
  
        List<Notificacion> cat= con.getResultList();
        return cat;
    } 
    
}
