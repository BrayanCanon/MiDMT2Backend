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
import entidades.Mision;
import entidades.TipoMision;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class TipoMisionJpaController implements Serializable {

    public TipoMisionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoMision tipoMision) {
        if (tipoMision.getMisionList() == null) {
            tipoMision.setMisionList(new ArrayList<Mision>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mision> attachedMisionList = new ArrayList<Mision>();
            for (Mision misionListMisionToAttach : tipoMision.getMisionList()) {
                misionListMisionToAttach = em.getReference(misionListMisionToAttach.getClass(), misionListMisionToAttach.getIdMision());
                attachedMisionList.add(misionListMisionToAttach);
            }
            tipoMision.setMisionList(attachedMisionList);
            em.persist(tipoMision);
            for (Mision misionListMision : tipoMision.getMisionList()) {
                TipoMision oldIdTipoMisionOfMisionListMision = misionListMision.getIdTipoMision();
                misionListMision.setIdTipoMision(tipoMision);
                misionListMision = em.merge(misionListMision);
                if (oldIdTipoMisionOfMisionListMision != null) {
                    oldIdTipoMisionOfMisionListMision.getMisionList().remove(misionListMision);
                    oldIdTipoMisionOfMisionListMision = em.merge(oldIdTipoMisionOfMisionListMision);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoMision tipoMision) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoMision persistentTipoMision = em.find(TipoMision.class, tipoMision.getIdTipoMision());
            List<Mision> misionListOld = persistentTipoMision.getMisionList();
            List<Mision> misionListNew = tipoMision.getMisionList();
            List<Mision> attachedMisionListNew = new ArrayList<Mision>();
            for (Mision misionListNewMisionToAttach : misionListNew) {
                misionListNewMisionToAttach = em.getReference(misionListNewMisionToAttach.getClass(), misionListNewMisionToAttach.getIdMision());
                attachedMisionListNew.add(misionListNewMisionToAttach);
            }
            misionListNew = attachedMisionListNew;
            tipoMision.setMisionList(misionListNew);
            tipoMision = em.merge(tipoMision);
            for (Mision misionListOldMision : misionListOld) {
                if (!misionListNew.contains(misionListOldMision)) {
                    misionListOldMision.setIdTipoMision(null);
                    misionListOldMision = em.merge(misionListOldMision);
                }
            }
            for (Mision misionListNewMision : misionListNew) {
                if (!misionListOld.contains(misionListNewMision)) {
                    TipoMision oldIdTipoMisionOfMisionListNewMision = misionListNewMision.getIdTipoMision();
                    misionListNewMision.setIdTipoMision(tipoMision);
                    misionListNewMision = em.merge(misionListNewMision);
                    if (oldIdTipoMisionOfMisionListNewMision != null && !oldIdTipoMisionOfMisionListNewMision.equals(tipoMision)) {
                        oldIdTipoMisionOfMisionListNewMision.getMisionList().remove(misionListNewMision);
                        oldIdTipoMisionOfMisionListNewMision = em.merge(oldIdTipoMisionOfMisionListNewMision);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoMision.getIdTipoMision();
                if (findTipoMision(id) == null) {
                    throw new NonexistentEntityException("The tipoMision with id " + id + " no longer exists.");
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
            TipoMision tipoMision;
            try {
                tipoMision = em.getReference(TipoMision.class, id);
                tipoMision.getIdTipoMision();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoMision with id " + id + " no longer exists.", enfe);
            }
            List<Mision> misionList = tipoMision.getMisionList();
            for (Mision misionListMision : misionList) {
                misionListMision.setIdTipoMision(null);
                misionListMision = em.merge(misionListMision);
            }
            em.remove(tipoMision);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoMision> findTipoMisionEntities() {
        return findTipoMisionEntities(true, -1, -1);
    }

    public List<TipoMision> findTipoMisionEntities(int maxResults, int firstResult) {
        return findTipoMisionEntities(false, maxResults, firstResult);
    }

    private List<TipoMision> findTipoMisionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoMision.class));
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

    public TipoMision findTipoMision(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoMision.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoMisionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoMision> rt = cq.from(TipoMision.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
