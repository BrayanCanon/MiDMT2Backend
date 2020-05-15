/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Encuesta;
import entidades.Paciente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.TipoEncuesta;
import entidades.Pregunta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class EncuestaJpaController implements Serializable {

    public EncuestaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encuesta encuesta) {
        if (encuesta.getPreguntaList() == null) {
            encuesta.setPreguntaList(new ArrayList<Pregunta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEncuesta idTipoEncuesta = encuesta.getIdTipoEncuesta();
            if (idTipoEncuesta != null) {
                idTipoEncuesta = em.getReference(idTipoEncuesta.getClass(), idTipoEncuesta.getIdTipoEncuesta());
                encuesta.setIdTipoEncuesta(idTipoEncuesta);
            }
            List<Pregunta> attachedPreguntaList = new ArrayList<Pregunta>();
            for (Pregunta preguntaListPreguntaToAttach : encuesta.getPreguntaList()) {
                preguntaListPreguntaToAttach = em.getReference(preguntaListPreguntaToAttach.getClass(), preguntaListPreguntaToAttach.getIdPregunta());
                attachedPreguntaList.add(preguntaListPreguntaToAttach);
            }
            encuesta.setPreguntaList(attachedPreguntaList);
            em.persist(encuesta);
            if (idTipoEncuesta != null) {
                idTipoEncuesta.getEncuestaList().add(encuesta);
                idTipoEncuesta = em.merge(idTipoEncuesta);
            }
            for (Pregunta preguntaListPregunta : encuesta.getPreguntaList()) {
                Encuesta oldIdEncuestaOfPreguntaListPregunta = preguntaListPregunta.getIdEncuesta();
                preguntaListPregunta.setIdEncuesta(encuesta);
                preguntaListPregunta = em.merge(preguntaListPregunta);
                if (oldIdEncuestaOfPreguntaListPregunta != null) {
                    oldIdEncuestaOfPreguntaListPregunta.getPreguntaList().remove(preguntaListPregunta);
                    oldIdEncuestaOfPreguntaListPregunta = em.merge(oldIdEncuestaOfPreguntaListPregunta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encuesta encuesta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta persistentEncuesta = em.find(Encuesta.class, encuesta.getIdEncuesta());
            TipoEncuesta idTipoEncuestaOld = persistentEncuesta.getIdTipoEncuesta();
            TipoEncuesta idTipoEncuestaNew = encuesta.getIdTipoEncuesta();
            List<Pregunta> preguntaListOld = persistentEncuesta.getPreguntaList();
            List<Pregunta> preguntaListNew = encuesta.getPreguntaList();
            List<String> illegalOrphanMessages = null;
            for (Pregunta preguntaListOldPregunta : preguntaListOld) {
                if (!preguntaListNew.contains(preguntaListOldPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pregunta " + preguntaListOldPregunta + " since its idEncuesta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoEncuestaNew != null) {
                idTipoEncuestaNew = em.getReference(idTipoEncuestaNew.getClass(), idTipoEncuestaNew.getIdTipoEncuesta());
                encuesta.setIdTipoEncuesta(idTipoEncuestaNew);
            }
            List<Pregunta> attachedPreguntaListNew = new ArrayList<Pregunta>();
            for (Pregunta preguntaListNewPreguntaToAttach : preguntaListNew) {
                preguntaListNewPreguntaToAttach = em.getReference(preguntaListNewPreguntaToAttach.getClass(), preguntaListNewPreguntaToAttach.getIdPregunta());
                attachedPreguntaListNew.add(preguntaListNewPreguntaToAttach);
            }
            preguntaListNew = attachedPreguntaListNew;
            encuesta.setPreguntaList(preguntaListNew);
            encuesta = em.merge(encuesta);
            if (idTipoEncuestaOld != null && !idTipoEncuestaOld.equals(idTipoEncuestaNew)) {
                idTipoEncuestaOld.getEncuestaList().remove(encuesta);
                idTipoEncuestaOld = em.merge(idTipoEncuestaOld);
            }
            if (idTipoEncuestaNew != null && !idTipoEncuestaNew.equals(idTipoEncuestaOld)) {
                idTipoEncuestaNew.getEncuestaList().add(encuesta);
                idTipoEncuestaNew = em.merge(idTipoEncuestaNew);
            }
            for (Pregunta preguntaListNewPregunta : preguntaListNew) {
                if (!preguntaListOld.contains(preguntaListNewPregunta)) {
                    Encuesta oldIdEncuestaOfPreguntaListNewPregunta = preguntaListNewPregunta.getIdEncuesta();
                    preguntaListNewPregunta.setIdEncuesta(encuesta);
                    preguntaListNewPregunta = em.merge(preguntaListNewPregunta);
                    if (oldIdEncuestaOfPreguntaListNewPregunta != null && !oldIdEncuestaOfPreguntaListNewPregunta.equals(encuesta)) {
                        oldIdEncuestaOfPreguntaListNewPregunta.getPreguntaList().remove(preguntaListNewPregunta);
                        oldIdEncuestaOfPreguntaListNewPregunta = em.merge(oldIdEncuestaOfPreguntaListNewPregunta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encuesta.getIdEncuesta();
                if (findEncuesta(id) == null) {
                    throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.");
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
            Encuesta encuesta;
            try {
                encuesta = em.getReference(Encuesta.class, id);
                encuesta.getIdEncuesta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pregunta> preguntaListOrphanCheck = encuesta.getPreguntaList();
            for (Pregunta preguntaListOrphanCheckPregunta : preguntaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encuesta (" + encuesta + ") cannot be destroyed since the Pregunta " + preguntaListOrphanCheckPregunta + " in its preguntaList field has a non-nullable idEncuesta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoEncuesta idTipoEncuesta = encuesta.getIdTipoEncuesta();
            if (idTipoEncuesta != null) {
                idTipoEncuesta.getEncuestaList().remove(encuesta);
                idTipoEncuesta = em.merge(idTipoEncuesta);
            }
            em.remove(encuesta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encuesta> findEncuestaEntities() {
        return findEncuestaEntities(true, -1, -1);
    }

    public List<Encuesta> findEncuestaEntities(int maxResults, int firstResult) {
        return findEncuestaEntities(false, maxResults, firstResult);
    }

    private List<Encuesta> findEncuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encuesta.class));
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

    public Encuesta findEncuesta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encuesta> rt = cq.from(Encuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<Encuesta> busEncuestaPa(Paciente id,TipoEncuesta id2) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Encuesta a WHERE a.codPaciente=:id AND  a.idTipoEncuesta=:id2");
        con.setParameter("id",id);
        con.setParameter("id2",id2);
        
  
        List<Encuesta> cat= con.getResultList();
        return cat;
    } 
     public List<Encuesta> busEncuestaSoloPa(Paciente id) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Encuesta a WHERE a.codPaciente=:id");
        con.setParameter("id",id);
      
        
  
        List<Encuesta> cat= con.getResultList();
        return cat;
    } 

}
