/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.MisionPaciente;
import entidades.Verificacion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author gulmi
 */
public class VerificacionJpaController implements Serializable {

    public VerificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Verificacion verificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(verificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Verificacion verificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            verificacion = em.merge(verificacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = verificacion.getIdVerificacion();
                if (findVerificacion(id) == null) {
                    throw new NonexistentEntityException("The verificacion with id " + id + " no longer exists.");
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
            Verificacion verificacion;
            try {
                verificacion = em.getReference(Verificacion.class, id);
                verificacion.getIdVerificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The verificacion with id " + id + " no longer exists.", enfe);
            }
            em.remove(verificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Verificacion> findVerificacionEntities() {
        return findVerificacionEntities(true, -1, -1);
    }

    public List<Verificacion> findVerificacionEntities(int maxResults, int firstResult) {
        return findVerificacionEntities(false, maxResults, firstResult);
    }

    private List<Verificacion> findVerificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Verificacion.class));
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

    public Verificacion findVerificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Verificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getVerificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Verificacion> rt = cq.from(Verificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Verificacion> sqlbuscVerificacionPorMP(MisionPaciente us) {
        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT a FROM Verificacion a WHERE a.idMisionPaciente=:cod ");
        con.setParameter("cod", us);
        return con.getResultList();
    }

    public List<Verificacion> sqlbuscVerificacionPorMPN(MisionPaciente us, int num) {
        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT a FROM Verificacion a WHERE a.idMisionPaciente=:cod AND a.numeroDia=:numdias");
        con.setParameter("cod", us);
        con.setParameter("numdias", num);
        return con.getResultList();
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar - 12/05/2019</p>
     *
     * @param codApoyoSocial
     * @return
     */
    public List<Object[]> consultarVerificacionesApoyoSocial(String codApoyoSocial) {
        EntityManager em = emf.createEntityManager();
        Query cons = em.createNativeQuery("select v.id_verificacion, m.nombre,p2.nombre, v.fecha,asocial.cod_apoyosocial from verificacion v "
                + "inner join mision_paciente mp on v.id_mision_paciente = mp.id_mision_paciente "
                + "inner join mision m on mp.id_mision = m.id_mision "
                + "inner join paciente p on mp.cod_paciente = p.cod_paciente "
                + "inner join asignacion a on p.cod_paciente = a.cod_paciente "
                + "inner join apoyo_social asocial on a.cod_apoyosocial = asocial.cod_apoyosocial "
                + "inner join mision_paso_logro mpl on m.id_mision = mpl.id_mision "
                + "inner join paso p2 on mpl.id_paso = p2.id_paso "
                + "where asocial.cod_apoyosocial =" + codApoyoSocial);
        return cons.getResultList();
    }
}
