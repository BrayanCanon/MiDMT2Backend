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
import entidades.Logro;
import entidades.Mision;
import entidades.MisionPasoLogro;
import entidades.Paso;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class MisionPasoLogroJpaController implements Serializable {

    public MisionPasoLogroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MisionPasoLogro misionPasoLogro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Logro idLogro = misionPasoLogro.getIdLogro();
            if (idLogro != null) {
                idLogro = em.getReference(idLogro.getClass(), idLogro.getIdLogro());
                misionPasoLogro.setIdLogro(idLogro);
            }
            Mision idMision = misionPasoLogro.getIdMision();
            if (idMision != null) {
                idMision = em.getReference(idMision.getClass(), idMision.getIdMision());
                misionPasoLogro.setIdMision(idMision);
            }
            Paso idPaso = misionPasoLogro.getIdPaso();
            if (idPaso != null) {
                idPaso = em.getReference(idPaso.getClass(), idPaso.getIdPaso());
                misionPasoLogro.setIdPaso(idPaso);
            }
            em.persist(misionPasoLogro);
            if (idLogro != null) {
                idLogro.getMisionPasoLogroList().add(misionPasoLogro);
                idLogro = em.merge(idLogro);
            }
            if (idMision != null) {
                idMision.getMisionPasoLogroList().add(misionPasoLogro);
                idMision = em.merge(idMision);
            }
            if (idPaso != null) {
                idPaso.getMisionPasoLogroList().add(misionPasoLogro);
                idPaso = em.merge(idPaso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MisionPasoLogro misionPasoLogro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MisionPasoLogro persistentMisionPasoLogro = em.find(MisionPasoLogro.class, misionPasoLogro.getIdMisionPasoLogro());
            Logro idLogroOld = persistentMisionPasoLogro.getIdLogro();
            Logro idLogroNew = misionPasoLogro.getIdLogro();
            Mision idMisionOld = persistentMisionPasoLogro.getIdMision();
            Mision idMisionNew = misionPasoLogro.getIdMision();
            Paso idPasoOld = persistentMisionPasoLogro.getIdPaso();
            Paso idPasoNew = misionPasoLogro.getIdPaso();
            if (idLogroNew != null) {
                idLogroNew = em.getReference(idLogroNew.getClass(), idLogroNew.getIdLogro());
                misionPasoLogro.setIdLogro(idLogroNew);
            }
            if (idMisionNew != null) {
                idMisionNew = em.getReference(idMisionNew.getClass(), idMisionNew.getIdMision());
                misionPasoLogro.setIdMision(idMisionNew);
            }
            if (idPasoNew != null) {
                idPasoNew = em.getReference(idPasoNew.getClass(), idPasoNew.getIdPaso());
                misionPasoLogro.setIdPaso(idPasoNew);
            }
            misionPasoLogro = em.merge(misionPasoLogro);
            if (idLogroOld != null && !idLogroOld.equals(idLogroNew)) {
                idLogroOld.getMisionPasoLogroList().remove(misionPasoLogro);
                idLogroOld = em.merge(idLogroOld);
            }
            if (idLogroNew != null && !idLogroNew.equals(idLogroOld)) {
                idLogroNew.getMisionPasoLogroList().add(misionPasoLogro);
                idLogroNew = em.merge(idLogroNew);
            }
            if (idMisionOld != null && !idMisionOld.equals(idMisionNew)) {
                idMisionOld.getMisionPasoLogroList().remove(misionPasoLogro);
                idMisionOld = em.merge(idMisionOld);
            }
            if (idMisionNew != null && !idMisionNew.equals(idMisionOld)) {
                idMisionNew.getMisionPasoLogroList().add(misionPasoLogro);
                idMisionNew = em.merge(idMisionNew);
            }
            if (idPasoOld != null && !idPasoOld.equals(idPasoNew)) {
                idPasoOld.getMisionPasoLogroList().remove(misionPasoLogro);
                idPasoOld = em.merge(idPasoOld);
            }
            if (idPasoNew != null && !idPasoNew.equals(idPasoOld)) {
                idPasoNew.getMisionPasoLogroList().add(misionPasoLogro);
                idPasoNew = em.merge(idPasoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = misionPasoLogro.getIdMisionPasoLogro();
                if (findMisionPasoLogro(id) == null) {
                    throw new NonexistentEntityException("The misionPasoLogro with id " + id + " no longer exists.");
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
            MisionPasoLogro misionPasoLogro;
            try {
                misionPasoLogro = em.getReference(MisionPasoLogro.class, id);
                misionPasoLogro.getIdMisionPasoLogro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The misionPasoLogro with id " + id + " no longer exists.", enfe);
            }
            Logro idLogro = misionPasoLogro.getIdLogro();
            if (idLogro != null) {
                idLogro.getMisionPasoLogroList().remove(misionPasoLogro);
                idLogro = em.merge(idLogro);
            }
            Mision idMision = misionPasoLogro.getIdMision();
            if (idMision != null) {
                idMision.getMisionPasoLogroList().remove(misionPasoLogro);
                idMision = em.merge(idMision);
            }
            Paso idPaso = misionPasoLogro.getIdPaso();
            if (idPaso != null) {
                idPaso.getMisionPasoLogroList().remove(misionPasoLogro);
                idPaso = em.merge(idPaso);
            }
            em.remove(misionPasoLogro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MisionPasoLogro> findMisionPasoLogroEntities() {
        return findMisionPasoLogroEntities(true, -1, -1);
    }

    public List<MisionPasoLogro> findMisionPasoLogroEntities(int maxResults, int firstResult) {
        return findMisionPasoLogroEntities(false, maxResults, firstResult);
    }

    private List<MisionPasoLogro> findMisionPasoLogroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MisionPasoLogro.class));
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

    public MisionPasoLogro findMisionPasoLogro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MisionPasoLogro.class, id);
        } finally {
            em.close();
        }
    }

    public int getMisionPasoLogroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MisionPasoLogro> rt = cq.from(MisionPasoLogro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public Logro mplbusc(Mision mis,Paso paso){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a.logro FROM MisionPasoLogro a WHERE a.mision=:mision AND a.paso=:paso AND a.estado=:estado");
        con.setParameter("mision",mis);
        con.setParameter("paso", paso);
        con.setParameter("estado", "a");
        con.setMaxResults(1);
        return (Logro)con.getResultList().get(0);
    }
      public List<Paso> mplbuscList(Mision mis){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a.paso FROM MisionPasoLogro a WHERE a.mision=:mision AND a.estado=:estado");
        con.setParameter("mision",mis);

        con.setParameter("estado", "a");
        
        
        return con.getResultList();
    }
       public List<MisionPasoLogro> mplbuscMis(Mision mis){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionPasoLogro a WHERE a.idMision=:mision AND a.estado=:estado");
        con.setParameter("mision",mis);

        con.setParameter("estado", "a");
        
        
        return con.getResultList();
    }
       public MisionPasoLogro pasoNumeroList(Mision mis,Paso paso){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionPasoLogro a WHERE a.mision=:mision AND a.paso=:paso AND a.estado=:estado");
        con.setParameter("mision",mis);
        con.setParameter("paso",paso);
        con.setParameter("estado", "a");
        
        
        return (MisionPasoLogro) con.getResultList().get(0) ;
    }
        public Paso pasoidm(Mision m) {
         EntityManager em = emf.createEntityManager();
         Query con= em.createQuery("SELECT a.idPaso FROM MisionPasoLogro a WHERE a.idMision=:mision");
         con.setParameter("mision", m);
         return(Paso)con.getResultList().get(0);
       
    }
         public MisionPasoLogro pasoVeri(Mision m,int a) {
         EntityManager em = emf.createEntityManager();
         Query con= em.createQuery("SELECT a FROM MisionPasoLogro a WHERE a.idMision=:m AND a.pasoNumero=:a");
         con.setParameter("m", m);
         con.setParameter("a", a);
         return (MisionPasoLogro) con.getResultList().get(0);
       
    }
}
