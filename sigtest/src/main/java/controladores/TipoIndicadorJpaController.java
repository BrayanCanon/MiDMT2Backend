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
import entidades.TipoIndicador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class TipoIndicadorJpaController implements Serializable {

    public TipoIndicadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoIndicador tipoIndicador) {
        if (tipoIndicador.getIndicadorList() == null) {
            tipoIndicador.setIndicadorList(new ArrayList<Indicador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Indicador> attachedIndicadorList = new ArrayList<Indicador>();
            for (Indicador indicadorListIndicadorToAttach : tipoIndicador.getIndicadorList()) {
                indicadorListIndicadorToAttach = em.getReference(indicadorListIndicadorToAttach.getClass(), indicadorListIndicadorToAttach.getIdIndicador());
                attachedIndicadorList.add(indicadorListIndicadorToAttach);
            }
            tipoIndicador.setIndicadorList(attachedIndicadorList);
            em.persist(tipoIndicador);
            for (Indicador indicadorListIndicador : tipoIndicador.getIndicadorList()) {
                TipoIndicador oldIdTipoIndicadorOfIndicadorListIndicador = indicadorListIndicador.getIdTipoIndicador();
                indicadorListIndicador.setIdTipoIndicador(tipoIndicador);
                indicadorListIndicador = em.merge(indicadorListIndicador);
                if (oldIdTipoIndicadorOfIndicadorListIndicador != null) {
                    oldIdTipoIndicadorOfIndicadorListIndicador.getIndicadorList().remove(indicadorListIndicador);
                    oldIdTipoIndicadorOfIndicadorListIndicador = em.merge(oldIdTipoIndicadorOfIndicadorListIndicador);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoIndicador tipoIndicador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoIndicador persistentTipoIndicador = em.find(TipoIndicador.class, tipoIndicador.getIdTipoIndicador());
            List<Indicador> indicadorListOld = persistentTipoIndicador.getIndicadorList();
            List<Indicador> indicadorListNew = tipoIndicador.getIndicadorList();
            List<String> illegalOrphanMessages = null;
            for (Indicador indicadorListOldIndicador : indicadorListOld) {
                if (!indicadorListNew.contains(indicadorListOldIndicador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Indicador " + indicadorListOldIndicador + " since its idTipoIndicador field is not nullable.");
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
            tipoIndicador.setIndicadorList(indicadorListNew);
            tipoIndicador = em.merge(tipoIndicador);
            for (Indicador indicadorListNewIndicador : indicadorListNew) {
                if (!indicadorListOld.contains(indicadorListNewIndicador)) {
                    TipoIndicador oldIdTipoIndicadorOfIndicadorListNewIndicador = indicadorListNewIndicador.getIdTipoIndicador();
                    indicadorListNewIndicador.setIdTipoIndicador(tipoIndicador);
                    indicadorListNewIndicador = em.merge(indicadorListNewIndicador);
                    if (oldIdTipoIndicadorOfIndicadorListNewIndicador != null && !oldIdTipoIndicadorOfIndicadorListNewIndicador.equals(tipoIndicador)) {
                        oldIdTipoIndicadorOfIndicadorListNewIndicador.getIndicadorList().remove(indicadorListNewIndicador);
                        oldIdTipoIndicadorOfIndicadorListNewIndicador = em.merge(oldIdTipoIndicadorOfIndicadorListNewIndicador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoIndicador.getIdTipoIndicador();
                if (findTipoIndicador(id) == null) {
                    throw new NonexistentEntityException("The tipoIndicador with id " + id + " no longer exists.");
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
            TipoIndicador tipoIndicador;
            try {
                tipoIndicador = em.getReference(TipoIndicador.class, id);
                tipoIndicador.getIdTipoIndicador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoIndicador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Indicador> indicadorListOrphanCheck = tipoIndicador.getIndicadorList();
            for (Indicador indicadorListOrphanCheckIndicador : indicadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoIndicador (" + tipoIndicador + ") cannot be destroyed since the Indicador " + indicadorListOrphanCheckIndicador + " in its indicadorList field has a non-nullable idTipoIndicador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoIndicador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoIndicador> findTipoIndicadorEntities() {
        return findTipoIndicadorEntities(true, -1, -1);
    }

    public List<TipoIndicador> findTipoIndicadorEntities(int maxResults, int firstResult) {
        return findTipoIndicadorEntities(false, maxResults, firstResult);
    }

    private List<TipoIndicador> findTipoIndicadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoIndicador.class));
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

    public TipoIndicador findTipoIndicador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoIndicador.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoIndicadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoIndicador> rt = cq.from(TipoIndicador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public TipoIndicador busTIpoIndicadorId(String id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM TipoIndicador a WHERE a.nomTipoIndicador=:id ");
        con.setParameter("id",id);
   
        List<TipoIndicador> cat= con.getResultList();
        return cat.get(0);
    } 
    
}
