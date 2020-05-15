/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import entidades.ApoyoSocial;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Asignacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class ApoyoSocialJpaController implements Serializable {

    public ApoyoSocialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ApoyoSocial apoyoSocial) throws PreexistingEntityException, Exception {
        if (apoyoSocial.getAsignacionList() == null) {
            apoyoSocial.setAsignacionList(new ArrayList<Asignacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Asignacion> attachedAsignacionList = new ArrayList<Asignacion>();
            for (Asignacion asignacionListAsignacionToAttach : apoyoSocial.getAsignacionList()) {
                asignacionListAsignacionToAttach = em.getReference(asignacionListAsignacionToAttach.getClass(), asignacionListAsignacionToAttach.getCodAsginacion());
                attachedAsignacionList.add(asignacionListAsignacionToAttach);
            }
            apoyoSocial.setAsignacionList(attachedAsignacionList);
            em.persist(apoyoSocial);
            for (Asignacion asignacionListAsignacion : apoyoSocial.getAsignacionList()) {
                ApoyoSocial oldCodApoyosocialOfAsignacionListAsignacion = asignacionListAsignacion.getCodApoyosocial();
                asignacionListAsignacion.setCodApoyosocial(apoyoSocial);
                asignacionListAsignacion = em.merge(asignacionListAsignacion);
                if (oldCodApoyosocialOfAsignacionListAsignacion != null) {
                    oldCodApoyosocialOfAsignacionListAsignacion.getAsignacionList().remove(asignacionListAsignacion);
                    oldCodApoyosocialOfAsignacionListAsignacion = em.merge(oldCodApoyosocialOfAsignacionListAsignacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findApoyoSocial(apoyoSocial.getCodApoyosocial()) != null) {
                throw new PreexistingEntityException("ApoyoSocial " + apoyoSocial + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ApoyoSocial apoyoSocial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApoyoSocial persistentApoyoSocial = em.find(ApoyoSocial.class, apoyoSocial.getCodApoyosocial());
            List<Asignacion> asignacionListOld = persistentApoyoSocial.getAsignacionList();
            List<Asignacion> asignacionListNew = apoyoSocial.getAsignacionList();
            List<Asignacion> attachedAsignacionListNew = new ArrayList<Asignacion>();
            for (Asignacion asignacionListNewAsignacionToAttach : asignacionListNew) {
                asignacionListNewAsignacionToAttach = em.getReference(asignacionListNewAsignacionToAttach.getClass(), asignacionListNewAsignacionToAttach.getCodAsginacion());
                attachedAsignacionListNew.add(asignacionListNewAsignacionToAttach);
            }
            asignacionListNew = attachedAsignacionListNew;
            apoyoSocial.setAsignacionList(asignacionListNew);
            apoyoSocial = em.merge(apoyoSocial);
            for (Asignacion asignacionListOldAsignacion : asignacionListOld) {
                if (!asignacionListNew.contains(asignacionListOldAsignacion)) {
                    asignacionListOldAsignacion.setCodApoyosocial(null);
                    asignacionListOldAsignacion = em.merge(asignacionListOldAsignacion);
                }
            }
            for (Asignacion asignacionListNewAsignacion : asignacionListNew) {
                if (!asignacionListOld.contains(asignacionListNewAsignacion)) {
                    ApoyoSocial oldCodApoyosocialOfAsignacionListNewAsignacion = asignacionListNewAsignacion.getCodApoyosocial();
                    asignacionListNewAsignacion.setCodApoyosocial(apoyoSocial);
                    asignacionListNewAsignacion = em.merge(asignacionListNewAsignacion);
                    if (oldCodApoyosocialOfAsignacionListNewAsignacion != null && !oldCodApoyosocialOfAsignacionListNewAsignacion.equals(apoyoSocial)) {
                        oldCodApoyosocialOfAsignacionListNewAsignacion.getAsignacionList().remove(asignacionListNewAsignacion);
                        oldCodApoyosocialOfAsignacionListNewAsignacion = em.merge(oldCodApoyosocialOfAsignacionListNewAsignacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = apoyoSocial.getCodApoyosocial();
                if (findApoyoSocial(id) == null) {
                    throw new NonexistentEntityException("The apoyoSocial with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApoyoSocial apoyoSocial;
            try {
                apoyoSocial = em.getReference(ApoyoSocial.class, id);
                apoyoSocial.getCodApoyosocial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apoyoSocial with id " + id + " no longer exists.", enfe);
            }
            List<Asignacion> asignacionList = apoyoSocial.getAsignacionList();
            for (Asignacion asignacionListAsignacion : asignacionList) {
                asignacionListAsignacion.setCodApoyosocial(null);
                asignacionListAsignacion = em.merge(asignacionListAsignacion);
            }
            em.remove(apoyoSocial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ApoyoSocial> findApoyoSocialEntities() {
        return findApoyoSocialEntities(true, -1, -1);
    }

    public List<ApoyoSocial> findApoyoSocialEntities(int maxResults, int firstResult) {
        return findApoyoSocialEntities(false, maxResults, firstResult);
    }

    private List<ApoyoSocial> findApoyoSocialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ApoyoSocial.class));
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

    public ApoyoSocial findApoyoSocial(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ApoyoSocial.class, id);
        } finally {
            em.close();
        }
    }

    public int getApoyoSocialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ApoyoSocial> rt = cq.from(ApoyoSocial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
