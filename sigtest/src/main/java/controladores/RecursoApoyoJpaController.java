/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Paciente;
import entidades.PruebaDiagnostica;
import entidades.RecursoApoyo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class RecursoApoyoJpaController implements Serializable {

    public RecursoApoyoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecursoApoyo recursoApoyo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nomUsuario = recursoApoyo.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario = em.getReference(nomUsuario.getClass(), nomUsuario.getNomUsuario());
                recursoApoyo.setNomUsuario(nomUsuario);
            }
            em.persist(recursoApoyo);
            if (nomUsuario != null) {
                nomUsuario.getRecursoApoyoList().add(recursoApoyo);
                nomUsuario = em.merge(nomUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecursoApoyo recursoApoyo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecursoApoyo persistentRecursoApoyo = em.find(RecursoApoyo.class, recursoApoyo.getIdRecapoyo());
            Usuario nomUsuarioOld = persistentRecursoApoyo.getNomUsuario();
            Usuario nomUsuarioNew = recursoApoyo.getNomUsuario();
            if (nomUsuarioNew != null) {
                nomUsuarioNew = em.getReference(nomUsuarioNew.getClass(), nomUsuarioNew.getNomUsuario());
                recursoApoyo.setNomUsuario(nomUsuarioNew);
            }
            recursoApoyo = em.merge(recursoApoyo);
            if (nomUsuarioOld != null && !nomUsuarioOld.equals(nomUsuarioNew)) {
                nomUsuarioOld.getRecursoApoyoList().remove(recursoApoyo);
                nomUsuarioOld = em.merge(nomUsuarioOld);
            }
            if (nomUsuarioNew != null && !nomUsuarioNew.equals(nomUsuarioOld)) {
                nomUsuarioNew.getRecursoApoyoList().add(recursoApoyo);
                nomUsuarioNew = em.merge(nomUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recursoApoyo.getIdRecapoyo();
                if (findRecursoApoyo(id) == null) {
                    throw new NonexistentEntityException("The recursoApoyo with id " + id + " no longer exists.");
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
            RecursoApoyo recursoApoyo;
            try {
                recursoApoyo = em.getReference(RecursoApoyo.class, id);
                recursoApoyo.getIdRecapoyo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recursoApoyo with id " + id + " no longer exists.", enfe);
            }
            Usuario nomUsuario = recursoApoyo.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario.getRecursoApoyoList().remove(recursoApoyo);
                nomUsuario = em.merge(nomUsuario);
            }
            em.remove(recursoApoyo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecursoApoyo> findRecursoApoyoEntities() {
        return findRecursoApoyoEntities(true, -1, -1);
    }

    public List<RecursoApoyo> findRecursoApoyoEntities(int maxResults, int firstResult) {
        return findRecursoApoyoEntities(false, maxResults, firstResult);
    }

    private List<RecursoApoyo> findRecursoApoyoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecursoApoyo.class));
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

    public RecursoApoyo findRecursoApoyo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecursoApoyo.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecursoApoyoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecursoApoyo> rt = cq.from(RecursoApoyo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
  /* public int busRecursoId(String a){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Categoria a WHERE a.nombreCategoria=:nomcateg");
        con.setParameter("nomcateg", a);
        List<RecursoApoyo> cat= con.getResultList();
        return cat.get(0).getIdCategoria();
}*/

    public int busRecursoId(String nom) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM RecursoApoyo a WHERE a.tituloRec=:nom");
        con.setParameter("nom", nom);
        List<RecursoApoyo> cat= con.getResultList();
        return cat.get(0).getIdRecapoyo();
    }
     public RecursoApoyo busImagen(String nom) {
         RecursoApoyo aa= new RecursoApoyo();
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM RecursoApoyo a WHERE a.imagen=:nom");
        aa.setImagen(nom);
        con.setParameter("nom",aa.getImagen());
        List<RecursoApoyo> cat= con.getResultList();
        return cat.get(0);
    }

    
}
