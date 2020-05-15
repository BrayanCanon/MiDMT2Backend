/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Asignacion;
import entidades.Paciente;
import entidades.Profesional;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class ProfesionalJpaController implements Serializable {

    public ProfesionalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesional profesional) throws PreexistingEntityException, Exception {
        if (profesional.getAsignacionList() == null) {
            profesional.setAsignacionList(new ArrayList<Asignacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Asignacion> attachedAsignacionList = new ArrayList<Asignacion>();
            for (Asignacion asignacionListAsignacionToAttach : profesional.getAsignacionList()) {
                asignacionListAsignacionToAttach = em.getReference(asignacionListAsignacionToAttach.getClass(), asignacionListAsignacionToAttach.getCodAsginacion());
                attachedAsignacionList.add(asignacionListAsignacionToAttach);
            }
            profesional.setAsignacionList(attachedAsignacionList);
            em.persist(profesional);
            for (Asignacion asignacionListAsignacion : profesional.getAsignacionList()) {
                Profesional oldCodProfesionalOfAsignacionListAsignacion = asignacionListAsignacion.getCodProfesional();
                asignacionListAsignacion.setCodProfesional(profesional);
                asignacionListAsignacion = em.merge(asignacionListAsignacion);
                if (oldCodProfesionalOfAsignacionListAsignacion != null) {
                    oldCodProfesionalOfAsignacionListAsignacion.getAsignacionList().remove(asignacionListAsignacion);
                    oldCodProfesionalOfAsignacionListAsignacion = em.merge(oldCodProfesionalOfAsignacionListAsignacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfesional(profesional.getCodProfesional()) != null) {
                throw new PreexistingEntityException("Profesional " + profesional + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesional profesional) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesional persistentProfesional = em.find(Profesional.class, profesional.getCodProfesional());
            List<Asignacion> asignacionListOld = persistentProfesional.getAsignacionList();
            List<Asignacion> asignacionListNew = profesional.getAsignacionList();
            List<Asignacion> attachedAsignacionListNew = new ArrayList<Asignacion>();
            for (Asignacion asignacionListNewAsignacionToAttach : asignacionListNew) {
                asignacionListNewAsignacionToAttach = em.getReference(asignacionListNewAsignacionToAttach.getClass(), asignacionListNewAsignacionToAttach.getCodAsginacion());
                attachedAsignacionListNew.add(asignacionListNewAsignacionToAttach);
            }
            asignacionListNew = attachedAsignacionListNew;
            profesional.setAsignacionList(asignacionListNew);
            profesional = em.merge(profesional);
            for (Asignacion asignacionListOldAsignacion : asignacionListOld) {
                if (!asignacionListNew.contains(asignacionListOldAsignacion)) {
                    asignacionListOldAsignacion.setCodProfesional(null);
                    asignacionListOldAsignacion = em.merge(asignacionListOldAsignacion);
                }
            }
            for (Asignacion asignacionListNewAsignacion : asignacionListNew) {
                if (!asignacionListOld.contains(asignacionListNewAsignacion)) {
                    Profesional oldCodProfesionalOfAsignacionListNewAsignacion = asignacionListNewAsignacion.getCodProfesional();
                    asignacionListNewAsignacion.setCodProfesional(profesional);
                    asignacionListNewAsignacion = em.merge(asignacionListNewAsignacion);
                    if (oldCodProfesionalOfAsignacionListNewAsignacion != null && !oldCodProfesionalOfAsignacionListNewAsignacion.equals(profesional)) {
                        oldCodProfesionalOfAsignacionListNewAsignacion.getAsignacionList().remove(asignacionListNewAsignacion);
                        oldCodProfesionalOfAsignacionListNewAsignacion = em.merge(oldCodProfesionalOfAsignacionListNewAsignacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = profesional.getCodProfesional();
                if (findProfesional(id) == null) {
                    throw new NonexistentEntityException("The profesional with id " + id + " no longer exists.");
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
            Profesional profesional;
            try {
                profesional = em.getReference(Profesional.class, id);
                profesional.getCodProfesional();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesional with id " + id + " no longer exists.", enfe);
            }
            List<Asignacion> asignacionList = profesional.getAsignacionList();
            for (Asignacion asignacionListAsignacion : asignacionList) {
                asignacionListAsignacion.setCodProfesional(null);
                asignacionListAsignacion = em.merge(asignacionListAsignacion);
            }
            em.remove(profesional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesional> findProfesionalEntities() {
        return findProfesionalEntities(true, -1, -1);
    }

    public List<Profesional> findProfesionalEntities(int maxResults, int firstResult) {
        return findProfesionalEntities(false, maxResults, firstResult);
    }

    private List<Profesional> findProfesionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesional.class));
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

    public Profesional findProfesional(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesional.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesionalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesional> rt = cq.from(Profesional.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
      public List<Asignacion> busListUsuarios(String a){
        EntityManager em = emf.createEntityManager();
        Profesional p =new Profesional();
        p.setCodProfesional(a);
        Query con= em.createQuery("SELECT a FROM Asignacion a WHERE a.codProfesional=:nom");
        con.setParameter("nom", p);
        List<Asignacion> cat= con.getResultList();
        return cat;
}

}
