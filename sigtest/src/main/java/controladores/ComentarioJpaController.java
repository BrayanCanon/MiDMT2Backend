/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Comentario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Tema;
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class ComentarioJpaController implements Serializable {

    public ComentarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comentario comentario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tema idTema = comentario.getIdTema();
            if (idTema != null) {
                idTema = em.getReference(idTema.getClass(), idTema.getIdTema());
                comentario.setIdTema(idTema);
            }
            Usuario nomUsuario = comentario.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario = em.getReference(nomUsuario.getClass(), nomUsuario.getNomUsuario());
                comentario.setNomUsuario(nomUsuario);
            }
            em.persist(comentario);
            if (idTema != null) {
                idTema.getComentarioList().add(comentario);
                idTema = em.merge(idTema);
            }
            if (nomUsuario != null) {
                nomUsuario.getComentarioList().add(comentario);
                nomUsuario = em.merge(nomUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comentario comentario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comentario persistentComentario = em.find(Comentario.class, comentario.getIdComentario());
            Tema idTemaOld = persistentComentario.getIdTema();
            Tema idTemaNew = comentario.getIdTema();
            Usuario nomUsuarioOld = persistentComentario.getNomUsuario();
            Usuario nomUsuarioNew = comentario.getNomUsuario();
            if (idTemaNew != null) {
                idTemaNew = em.getReference(idTemaNew.getClass(), idTemaNew.getIdTema());
                comentario.setIdTema(idTemaNew);
            }
            if (nomUsuarioNew != null) {
                nomUsuarioNew = em.getReference(nomUsuarioNew.getClass(), nomUsuarioNew.getNomUsuario());
                comentario.setNomUsuario(nomUsuarioNew);
            }
            comentario = em.merge(comentario);
            if (idTemaOld != null && !idTemaOld.equals(idTemaNew)) {
                idTemaOld.getComentarioList().remove(comentario);
                idTemaOld = em.merge(idTemaOld);
            }
            if (idTemaNew != null && !idTemaNew.equals(idTemaOld)) {
                idTemaNew.getComentarioList().add(comentario);
                idTemaNew = em.merge(idTemaNew);
            }
            if (nomUsuarioOld != null && !nomUsuarioOld.equals(nomUsuarioNew)) {
                nomUsuarioOld.getComentarioList().remove(comentario);
                nomUsuarioOld = em.merge(nomUsuarioOld);
            }
            if (nomUsuarioNew != null && !nomUsuarioNew.equals(nomUsuarioOld)) {
                nomUsuarioNew.getComentarioList().add(comentario);
                nomUsuarioNew = em.merge(nomUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comentario.getIdComentario();
                if (findComentario(id) == null) {
                    throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.");
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
            Comentario comentario;
            try {
                comentario = em.getReference(Comentario.class, id);
                comentario.getIdComentario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.", enfe);
            }
            Tema idTema = comentario.getIdTema();
            if (idTema != null) {
                idTema.getComentarioList().remove(comentario);
                idTema = em.merge(idTema);
            }
            Usuario nomUsuario = comentario.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario.getComentarioList().remove(comentario);
                nomUsuario = em.merge(nomUsuario);
            }
            em.remove(comentario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comentario> findComentarioEntities() {
        return findComentarioEntities(true, -1, -1);
    }

    public List<Comentario> findComentarioEntities(int maxResults, int firstResult) {
        return findComentarioEntities(false, maxResults, firstResult);
    }

    private List<Comentario> findComentarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comentario.class));
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

    public Comentario findComentario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comentario.class, id);
        } finally {
            em.close();
        }
    }

    public int getComentarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comentario> rt = cq.from(Comentario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public Comentario busTema(Tema a){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Comentario a WHERE a.idTema=:holi");
         con.setParameter("holi", a);
         con.setMaxResults(1);
        List<Comentario> cat= con.getResultList();
        return cat.get(0);
}
     public List<Comentario> busTemaList(Tema a){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Comentario a WHERE a.idTema=:holi");
         con.setParameter("holi", a);
         con.setMaxResults(1);
        List<Comentario> cat= con.getResultList();
        return cat;
}

}
