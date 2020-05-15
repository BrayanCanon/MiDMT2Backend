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
import entidades.Indicador;
import entidades.Paciente;
import entidades.PruebaDiagnostica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class PruebaDiagnosticaJpaController implements Serializable {

    public PruebaDiagnosticaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PruebaDiagnostica pruebaDiagnostica) {
        if (pruebaDiagnostica.getIndicadorList() == null) {
            pruebaDiagnostica.setIndicadorList(new ArrayList<Indicador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Indicador> attachedIndicadorList = new ArrayList<Indicador>();
            for (Indicador indicadorListIndicadorToAttach : pruebaDiagnostica.getIndicadorList()) {
                indicadorListIndicadorToAttach = em.getReference(indicadorListIndicadorToAttach.getClass(), indicadorListIndicadorToAttach.getIdIndicador());
                attachedIndicadorList.add(indicadorListIndicadorToAttach);
            }
            pruebaDiagnostica.setIndicadorList(attachedIndicadorList);
            em.persist(pruebaDiagnostica);
            for (Indicador indicadorListIndicador : pruebaDiagnostica.getIndicadorList()) {
                PruebaDiagnostica oldIdPruebaOfIndicadorListIndicador = indicadorListIndicador.getIdPrueba();
                indicadorListIndicador.setIdPrueba(pruebaDiagnostica);
                indicadorListIndicador = em.merge(indicadorListIndicador);
                if (oldIdPruebaOfIndicadorListIndicador != null) {
                    oldIdPruebaOfIndicadorListIndicador.getIndicadorList().remove(indicadorListIndicador);
                    oldIdPruebaOfIndicadorListIndicador = em.merge(oldIdPruebaOfIndicadorListIndicador);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PruebaDiagnostica pruebaDiagnostica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PruebaDiagnostica persistentPruebaDiagnostica = em.find(PruebaDiagnostica.class, pruebaDiagnostica.getIdPrueba());
            List<Indicador> indicadorListOld = persistentPruebaDiagnostica.getIndicadorList();
            List<Indicador> indicadorListNew = pruebaDiagnostica.getIndicadorList();
            List<String> illegalOrphanMessages = null;
            for (Indicador indicadorListOldIndicador : indicadorListOld) {
                if (!indicadorListNew.contains(indicadorListOldIndicador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Indicador " + indicadorListOldIndicador + " since its idPrueba field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Indicador> attachedIndicadorListNew = new ArrayList<Indicador>();
            for (Indicador indicadorListNewIndicadorToAttach : indicadorListNew) {
                indicadorListNewIndicadorToAttach = em.getReference(indicadorListNewIndicadorToAttach.getClass(), indicadorListNewIndicadorToAttach.getIdIndicador());
                attachedIndicadorListNew.add(indicadorListNewIndicadorToAttach);
            }
            indicadorListNew = attachedIndicadorListNew;
            pruebaDiagnostica.setIndicadorList(indicadorListNew);
            pruebaDiagnostica = em.merge(pruebaDiagnostica);
            for (Indicador indicadorListNewIndicador : indicadorListNew) {
                if (!indicadorListOld.contains(indicadorListNewIndicador)) {
                    PruebaDiagnostica oldIdPruebaOfIndicadorListNewIndicador = indicadorListNewIndicador.getIdPrueba();
                    indicadorListNewIndicador.setIdPrueba(pruebaDiagnostica);
                    indicadorListNewIndicador = em.merge(indicadorListNewIndicador);
                    if (oldIdPruebaOfIndicadorListNewIndicador != null && !oldIdPruebaOfIndicadorListNewIndicador.equals(pruebaDiagnostica)) {
                        oldIdPruebaOfIndicadorListNewIndicador.getIndicadorList().remove(indicadorListNewIndicador);
                        oldIdPruebaOfIndicadorListNewIndicador = em.merge(oldIdPruebaOfIndicadorListNewIndicador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pruebaDiagnostica.getIdPrueba();
                if (findPruebaDiagnostica(id) == null) {
                    throw new NonexistentEntityException("The pruebaDiagnostica with id " + id + " no longer exists.");
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
            PruebaDiagnostica pruebaDiagnostica;
            try {
                pruebaDiagnostica = em.getReference(PruebaDiagnostica.class, id);
                pruebaDiagnostica.getIdPrueba();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pruebaDiagnostica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Indicador> indicadorListOrphanCheck = pruebaDiagnostica.getIndicadorList();
            for (Indicador indicadorListOrphanCheckIndicador : indicadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PruebaDiagnostica (" + pruebaDiagnostica + ") cannot be destroyed since the Indicador " + indicadorListOrphanCheckIndicador + " in its indicadorList field has a non-nullable idPrueba field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pruebaDiagnostica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PruebaDiagnostica> findPruebaDiagnosticaEntities() {
        return findPruebaDiagnosticaEntities(true, -1, -1);
    }

    public List<PruebaDiagnostica> findPruebaDiagnosticaEntities(int maxResults, int firstResult) {
        return findPruebaDiagnosticaEntities(false, maxResults, firstResult);
    }

    private List<PruebaDiagnostica> findPruebaDiagnosticaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PruebaDiagnostica.class));
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

    public PruebaDiagnostica findPruebaDiagnostica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PruebaDiagnostica.class, id);
        } finally {
            em.close();
        }
    }

    public int getPruebaDiagnosticaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PruebaDiagnostica> rt = cq.from(PruebaDiagnostica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<PruebaDiagnostica> busUsuarioId(Paciente id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM PruebaDiagnostica a WHERE a.codPaciente=:id");
        con.setParameter("id",id);
   
        List<PruebaDiagnostica> cat= con.getResultList();
        return cat;
    } 

    public List<PruebaDiagnostica> busUsuarioId2(Paciente id, String id0) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM PruebaDiagnostica a WHERE a.codPaciente=:id and a.nombrePrueba=:id2");
        con.setParameter("id",id);
        con.setParameter("id2",id0);
   
        List<PruebaDiagnostica> cat= con.getResultList();
        return cat;
    }
}
