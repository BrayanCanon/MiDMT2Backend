/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Mision;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) {
        if (categoria.getMisionList() == null) {
            categoria.setMisionList(new ArrayList<Mision>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mision> attachedMisionList = new ArrayList<Mision>();
            for (Mision misionListMisionToAttach : categoria.getMisionList()) {
                misionListMisionToAttach = em.getReference(misionListMisionToAttach.getClass(), misionListMisionToAttach.getIdMision());
                attachedMisionList.add(misionListMisionToAttach);
            }
            categoria.setMisionList(attachedMisionList);
            em.persist(categoria);
            for (Mision misionListMision : categoria.getMisionList()) {
                Categoria oldIdCategoriaOfMisionListMision = misionListMision.getIdCategoria();
                misionListMision.setIdCategoria(categoria);
                misionListMision = em.merge(misionListMision);
                if (oldIdCategoriaOfMisionListMision != null) {
                    oldIdCategoriaOfMisionListMision.getMisionList().remove(misionListMision);
                    oldIdCategoriaOfMisionListMision = em.merge(oldIdCategoriaOfMisionListMision);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdCategoria());
            List<Mision> misionListOld = persistentCategoria.getMisionList();
            List<Mision> misionListNew = categoria.getMisionList();
            List<Mision> attachedMisionListNew = new ArrayList<Mision>();
            for (Mision misionListNewMisionToAttach : misionListNew) {
                misionListNewMisionToAttach = em.getReference(misionListNewMisionToAttach.getClass(), misionListNewMisionToAttach.getIdMision());
                attachedMisionListNew.add(misionListNewMisionToAttach);
            }
            misionListNew = attachedMisionListNew;
            categoria.setMisionList(misionListNew);
            categoria = em.merge(categoria);
            for (Mision misionListOldMision : misionListOld) {
                if (!misionListNew.contains(misionListOldMision)) {
                    misionListOldMision.setIdCategoria(null);
                    misionListOldMision = em.merge(misionListOldMision);
                }
            }
            for (Mision misionListNewMision : misionListNew) {
                if (!misionListOld.contains(misionListNewMision)) {
                    Categoria oldIdCategoriaOfMisionListNewMision = misionListNewMision.getIdCategoria();
                    misionListNewMision.setIdCategoria(categoria);
                    misionListNewMision = em.merge(misionListNewMision);
                    if (oldIdCategoriaOfMisionListNewMision != null && !oldIdCategoriaOfMisionListNewMision.equals(categoria)) {
                        oldIdCategoriaOfMisionListNewMision.getMisionList().remove(misionListNewMision);
                        oldIdCategoriaOfMisionListNewMision = em.merge(oldIdCategoriaOfMisionListNewMision);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getIdCategoria();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<Mision> misionList = categoria.getMisionList();
            for (Mision misionListMision : misionList) {
                misionListMision.setIdCategoria(null);
                misionListMision = em.merge(misionListMision);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public int busCategoriaNom(String a){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Categoria a WHERE a.nombreCategoria=:nomcateg");
        con.setParameter("nomcateg", a);
        List<Categoria> cat= con.getResultList();
        return cat.get(0).getIdCategoria();
}
}
