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
import entidades.Nivel;
import entidades.NivelPaciente;
import entidades.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class NivelJpaController implements Serializable {

    public NivelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nivel nivel) {
        if (nivel.getMisionList() == null) {
            nivel.setMisionList(new ArrayList<Mision>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mision> attachedMisionList = new ArrayList<Mision>();
            for (Mision misionListMisionToAttach : nivel.getMisionList()) {
                misionListMisionToAttach = em.getReference(misionListMisionToAttach.getClass(), misionListMisionToAttach.getIdMision());
                attachedMisionList.add(misionListMisionToAttach);
            }
            nivel.setMisionList(attachedMisionList);
            em.persist(nivel);
            for (Mision misionListMision : nivel.getMisionList()) {
                Nivel oldIdNivelOfMisionListMision = misionListMision.getIdNivel();
                misionListMision.setIdNivel(nivel);
                misionListMision = em.merge(misionListMision);
                if (oldIdNivelOfMisionListMision != null) {
                    oldIdNivelOfMisionListMision.getMisionList().remove(misionListMision);
                    oldIdNivelOfMisionListMision = em.merge(oldIdNivelOfMisionListMision);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nivel nivel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel persistentNivel = em.find(Nivel.class, nivel.getIdNivel());
            List<Mision> misionListOld = persistentNivel.getMisionList();
            List<Mision> misionListNew = nivel.getMisionList();
            List<Mision> attachedMisionListNew = new ArrayList<Mision>();
            for (Mision misionListNewMisionToAttach : misionListNew) {
                misionListNewMisionToAttach = em.getReference(misionListNewMisionToAttach.getClass(), misionListNewMisionToAttach.getIdMision());
                attachedMisionListNew.add(misionListNewMisionToAttach);
            }
            misionListNew = attachedMisionListNew;
            nivel.setMisionList(misionListNew);
            nivel = em.merge(nivel);
            for (Mision misionListOldMision : misionListOld) {
                if (!misionListNew.contains(misionListOldMision)) {
                    misionListOldMision.setIdNivel(null);
                    misionListOldMision = em.merge(misionListOldMision);
                }
            }
            for (Mision misionListNewMision : misionListNew) {
                if (!misionListOld.contains(misionListNewMision)) {
                    Nivel oldIdNivelOfMisionListNewMision = misionListNewMision.getIdNivel();
                    misionListNewMision.setIdNivel(nivel);
                    misionListNewMision = em.merge(misionListNewMision);
                    if (oldIdNivelOfMisionListNewMision != null && !oldIdNivelOfMisionListNewMision.equals(nivel)) {
                        oldIdNivelOfMisionListNewMision.getMisionList().remove(misionListNewMision);
                        oldIdNivelOfMisionListNewMision = em.merge(oldIdNivelOfMisionListNewMision);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivel.getIdNivel();
                if (findNivel(id) == null) {
                    throw new NonexistentEntityException("The nivel with id " + id + " no longer exists.");
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
            Nivel nivel;
            try {
                nivel = em.getReference(Nivel.class, id);
                nivel.getIdNivel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivel with id " + id + " no longer exists.", enfe);
            }
            List<Mision> misionList = nivel.getMisionList();
            for (Mision misionListMision : misionList) {
                misionListMision.setIdNivel(null);
                misionListMision = em.merge(misionListMision);
            }
            em.remove(nivel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nivel> findNivelEntities() {
        return findNivelEntities(true, -1, -1);
    }

    public List<Nivel> findNivelEntities(int maxResults, int firstResult) {
        return findNivelEntities(false, maxResults, firstResult);
    }

    private List<Nivel> findNivelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nivel.class));
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

    public Nivel findNivel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nivel.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nivel> rt = cq.from(Nivel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Nivel sqlbuscNivel(NivelPaciente us) {
        EntityManager em = emf.createEntityManager();

        Query con = em.createQuery("SELECT a FROM Nivel a WHERE a.nombre=:cod ");
        con.setParameter("cod", us.getNombreNivel());

        return (Nivel) con.getResultList().get(0);
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar - 24/04/2019</p>
     *
     * @param paciente
     * @return
     */ 
    public List<Nivel> consultarListadoMisionesPorPaciente(Paciente paciente) {
        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT "
                + "niv "
                + "FROM Nivel niv "
                + "INNER JOIN niv.misionList mis "
                + "INNER JOIN mis.misionPacienteList misP "
                + "INNER JOIN misP.codPaciente pac "
                + "WHERE pac.codPaciente=:CodP"
        );
        con.setParameter("CodP", paciente.getCodPaciente());
        List<Nivel> a = con.getResultList();
        return a;
    }
}
