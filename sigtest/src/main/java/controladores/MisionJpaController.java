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
import entidades.Categoria;
import entidades.Mision;
import entidades.Nivel;
import entidades.NivelPaciente;
import entidades.TipoMision;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.services.CategoriaService;

/**
 *
 * @author gulmi
 */
public class MisionJpaController implements Serializable {
    public MisionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public int create(Mision mision) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = mision.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                mision.setIdCategoria(idCategoria);
            }
            Nivel idNivel = mision.getIdNivel();
            if (idNivel != null) {
                idNivel = em.getReference(idNivel.getClass(), idNivel.getIdNivel());
                mision.setIdNivel(idNivel);
            }
            TipoMision idTipoMision = mision.getIdTipoMision();
            if (idTipoMision != null) {
                idTipoMision = em.getReference(idTipoMision.getClass(), idTipoMision.getIdTipoMision());
                mision.setIdTipoMision(idTipoMision);
            }
            em.persist(mision);
           
            em.flush();
            int cod=mision.getIdMision();
            if (idCategoria != null) {
                idCategoria.getMisionList().add(mision);
                idCategoria = em.merge(idCategoria);
            }
            if (idNivel != null) {
                idNivel.getMisionList().add(mision);
                idNivel = em.merge(idNivel);
            }
            if (idTipoMision != null) {
                idTipoMision.getMisionList().add(mision);
                idTipoMision = em.merge(idTipoMision);
            }
            em.getTransaction().commit();
            return cod;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void edit(Mision mision) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mision persistentMision = em.find(Mision.class, mision.getIdMision());
            Categoria idCategoriaOld = persistentMision.getIdCategoria();
            Categoria idCategoriaNew = mision.getIdCategoria();
            Nivel idNivelOld = persistentMision.getIdNivel();
            Nivel idNivelNew = mision.getIdNivel();
            TipoMision idTipoMisionOld = persistentMision.getIdTipoMision();
            TipoMision idTipoMisionNew = mision.getIdTipoMision();
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                mision.setIdCategoria(idCategoriaNew);
            }
            if (idNivelNew != null) {
                idNivelNew = em.getReference(idNivelNew.getClass(), idNivelNew.getIdNivel());
                mision.setIdNivel(idNivelNew);
            }
            if (idTipoMisionNew != null) {
                idTipoMisionNew = em.getReference(idTipoMisionNew.getClass(), idTipoMisionNew.getIdTipoMision());
                mision.setIdTipoMision(idTipoMisionNew);
            }
            mision = em.merge(mision);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getMisionList().remove(mision);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getMisionList().add(mision);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            if (idNivelOld != null && !idNivelOld.equals(idNivelNew)) {
                idNivelOld.getMisionList().remove(mision);
                idNivelOld = em.merge(idNivelOld);
            }
            if (idNivelNew != null && !idNivelNew.equals(idNivelOld)) {
                idNivelNew.getMisionList().add(mision);
                idNivelNew = em.merge(idNivelNew);
            }
            if (idTipoMisionOld != null && !idTipoMisionOld.equals(idTipoMisionNew)) {
                idTipoMisionOld.getMisionList().remove(mision);
                idTipoMisionOld = em.merge(idTipoMisionOld);
            }
            if (idTipoMisionNew != null && !idTipoMisionNew.equals(idTipoMisionOld)) {
                idTipoMisionNew.getMisionList().add(mision);
                idTipoMisionNew = em.merge(idTipoMisionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mision.getIdMision();
                if (findMision(id) == null) {
                    throw new NonexistentEntityException("The mision with id " + id + " no longer exists.");
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
            Mision mision;
            try {
                mision = em.getReference(Mision.class, id);
                mision.getIdMision();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mision with id " + id + " no longer exists.", enfe);
            }
            Categoria idCategoria = mision.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getMisionList().remove(mision);
                idCategoria = em.merge(idCategoria);
            }
            Nivel idNivel = mision.getIdNivel();
            if (idNivel != null) {
                idNivel.getMisionList().remove(mision);
                idNivel = em.merge(idNivel);
            }
            TipoMision idTipoMision = mision.getIdTipoMision();
            if (idTipoMision != null) {
                idTipoMision.getMisionList().remove(mision);
                idTipoMision = em.merge(idTipoMision);
            }
            em.remove(mision);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mision> findMisionEntities() {
        return findMisionEntities(true, -1, -1);
    }

    public List<Mision> findMisionEntities(int maxResults, int firstResult) {
        return findMisionEntities(false, maxResults, firstResult);
    }

    private List<Mision> findMisionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mision.class));
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

    public Mision findMision(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mision.class, id);
        } finally {
            em.close();
        }
    }

    public int getMisionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mision> rt = cq.from(Mision.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Mision sqlbuscMisionNivel(NivelPaciente us){
        EntityManager em = emf.createEntityManager();
      
        Query con= em.createQuery("SELECT a FROM Mision a WHERE a.idNivel.nombre=:cod ");
        con.setParameter("cod",us.getNombreNivel());
        return (Mision) con.getResultList().get(0);
    }
    
    
       public List<Mision> sqlbuscNombreEjercicio(Nivel us){
        EntityManager em = emf.createEntityManager();
      
        Query con= em.createQuery("SELECT a FROM Mision a WHERE a.idNivel=:cod AND a.idCategoria=:cod2 ");
        con.setParameter("cod",us);
        CategoriaService css= new CategoriaService();
        Categoria cc=css.categorialistaId(2);
         con.setParameter("cod2",cc);
        return  con.getResultList();
    }
         public List<Mision> sqlbuscNombreAlimentacion(Nivel us){
        EntityManager em = emf.createEntityManager();
      
        Query con= em.createQuery("SELECT a FROM Mision a WHERE a.idNivel=:cod AND a.idCategoria=:cod2 ");
        con.setParameter("cod",us);
        CategoriaService css= new CategoriaService();
       
        Categoria cc=css.categorialistaId(3);
       con.setParameter("cod2",cc);
        return  con.getResultList();
    }
}
