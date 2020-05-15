/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Indicador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.PruebaDiagnostica;
import entidades.TipoIndicador;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class IndicadorJpaController implements Serializable {

    public IndicadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Indicador indicador) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PruebaDiagnostica idPrueba = indicador.getIdPrueba();
            if (idPrueba != null) {
                idPrueba = em.getReference(idPrueba.getClass(), idPrueba.getIdPrueba());
                indicador.setIdPrueba(idPrueba);
            }
            TipoIndicador idTipoIndicador = indicador.getIdTipoIndicador();
            if (idTipoIndicador != null) {
                idTipoIndicador = em.getReference(idTipoIndicador.getClass(), idTipoIndicador.getIdTipoIndicador());
                indicador.setIdTipoIndicador(idTipoIndicador);
            }
            em.persist(indicador);
            if (idPrueba != null) {
                idPrueba.getIndicadorList().add(indicador);
                idPrueba = em.merge(idPrueba);
            }
            if (idTipoIndicador != null) {
                idTipoIndicador.getIndicadorList().add(indicador);
                idTipoIndicador = em.merge(idTipoIndicador);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Indicador indicador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Indicador persistentIndicador = em.find(Indicador.class, indicador.getIdIndicador());
            PruebaDiagnostica idPruebaOld = persistentIndicador.getIdPrueba();
            PruebaDiagnostica idPruebaNew = indicador.getIdPrueba();
            TipoIndicador idTipoIndicadorOld = persistentIndicador.getIdTipoIndicador();
            TipoIndicador idTipoIndicadorNew = indicador.getIdTipoIndicador();
            if (idPruebaNew != null) {
                idPruebaNew = em.getReference(idPruebaNew.getClass(), idPruebaNew.getIdPrueba());
                indicador.setIdPrueba(idPruebaNew);
            }
            if (idTipoIndicadorNew != null) {
                idTipoIndicadorNew = em.getReference(idTipoIndicadorNew.getClass(), idTipoIndicadorNew.getIdTipoIndicador());
                indicador.setIdTipoIndicador(idTipoIndicadorNew);
            }
            indicador = em.merge(indicador);
            if (idPruebaOld != null && !idPruebaOld.equals(idPruebaNew)) {
                idPruebaOld.getIndicadorList().remove(indicador);
                idPruebaOld = em.merge(idPruebaOld);
            }
            if (idPruebaNew != null && !idPruebaNew.equals(idPruebaOld)) {
                idPruebaNew.getIndicadorList().add(indicador);
                idPruebaNew = em.merge(idPruebaNew);
            }
            if (idTipoIndicadorOld != null && !idTipoIndicadorOld.equals(idTipoIndicadorNew)) {
                idTipoIndicadorOld.getIndicadorList().remove(indicador);
                idTipoIndicadorOld = em.merge(idTipoIndicadorOld);
            }
            if (idTipoIndicadorNew != null && !idTipoIndicadorNew.equals(idTipoIndicadorOld)) {
                idTipoIndicadorNew.getIndicadorList().add(indicador);
                idTipoIndicadorNew = em.merge(idTipoIndicadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = indicador.getIdIndicador();
                if (findIndicador(id) == null) {
                    throw new NonexistentEntityException("The indicador with id " + id + " no longer exists.");
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
            Indicador indicador;
            try {
                indicador = em.getReference(Indicador.class, id);
                indicador.getIdIndicador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The indicador with id " + id + " no longer exists.", enfe);
            }
            PruebaDiagnostica idPrueba = indicador.getIdPrueba();
            if (idPrueba != null) {
                idPrueba.getIndicadorList().remove(indicador);
                idPrueba = em.merge(idPrueba);
            }
            TipoIndicador idTipoIndicador = indicador.getIdTipoIndicador();
            if (idTipoIndicador != null) {
                idTipoIndicador.getIndicadorList().remove(indicador);
                idTipoIndicador = em.merge(idTipoIndicador);
            }
            em.remove(indicador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Indicador> findIndicadorEntities() {
        return findIndicadorEntities(true, -1, -1);
    }

    public List<Indicador> findIndicadorEntities(int maxResults, int firstResult) {
        return findIndicadorEntities(false, maxResults, firstResult);
    }

    private List<Indicador> findIndicadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Indicador.class));
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

    public Indicador findIndicador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Indicador.class, id);
        } finally {
            em.close();
        }
    }

    public int getIndicadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Indicador> rt = cq.from(Indicador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<Indicador> busIndicadorId(PruebaDiagnostica id,TipoIndicador id2) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Indicador a WHERE a.idPrueba=:id AND a.idTipoIndicador=:id2 ");
        con.setParameter("id",id);
        con.setParameter("id2",id2);
   
        List<Indicador> cat= con.getResultList();
        return cat;
    } 
}
