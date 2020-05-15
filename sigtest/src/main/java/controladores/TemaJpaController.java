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
import entidades.Comentario;
import entidades.Tema;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class TemaJpaController implements Serializable {

    public TemaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

  public int create(Tema tema) {
        if (tema.getComentarioList() == null) {
            tema.setComentarioList(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Comentario> attachedComentarioList = new ArrayList<Comentario>();
            for (Comentario comentarioListComentarioToAttach : tema.getComentarioList()) {
                comentarioListComentarioToAttach = em.getReference(comentarioListComentarioToAttach.getClass(), comentarioListComentarioToAttach.getIdComentario());
                attachedComentarioList.add(comentarioListComentarioToAttach);
            }
            tema.setComentarioList(attachedComentarioList);
            em.persist(tema);
            em.flush();
            int codTema=tema.getIdTema();
            for (Comentario comentarioListComentario : tema.getComentarioList()) {
                Tema oldIdTemaOfComentarioListComentario = comentarioListComentario.getIdTema();
                comentarioListComentario.setIdTema(tema);
                comentarioListComentario = em.merge(comentarioListComentario);
                if (oldIdTemaOfComentarioListComentario != null) {
                    oldIdTemaOfComentarioListComentario.getComentarioList().remove(comentarioListComentario);
                    oldIdTemaOfComentarioListComentario = em.merge(oldIdTemaOfComentarioListComentario);
                }
            }
            em.getTransaction().commit();
            return codTema;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tema tema) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tema persistentTema = em.find(Tema.class, tema.getIdTema());
            List<Comentario> comentarioListOld = persistentTema.getComentarioList();
            List<Comentario> comentarioListNew = tema.getComentarioList();
            List<String> illegalOrphanMessages = null;
            for (Comentario comentarioListOldComentario : comentarioListOld) {
                if (!comentarioListNew.contains(comentarioListOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioListOldComentario + " since its idTema field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Comentario> attachedComentarioListNew = new ArrayList<Comentario>();
            for (Comentario comentarioListNewComentarioToAttach : comentarioListNew) {
                comentarioListNewComentarioToAttach = em.getReference(comentarioListNewComentarioToAttach.getClass(), comentarioListNewComentarioToAttach.getIdComentario());
                attachedComentarioListNew.add(comentarioListNewComentarioToAttach);
            }
            comentarioListNew = attachedComentarioListNew;
            tema.setComentarioList(comentarioListNew);
            tema = em.merge(tema);
            for (Comentario comentarioListNewComentario : comentarioListNew) {
                if (!comentarioListOld.contains(comentarioListNewComentario)) {
                    Tema oldIdTemaOfComentarioListNewComentario = comentarioListNewComentario.getIdTema();
                    comentarioListNewComentario.setIdTema(tema);
                    comentarioListNewComentario = em.merge(comentarioListNewComentario);
                    if (oldIdTemaOfComentarioListNewComentario != null && !oldIdTemaOfComentarioListNewComentario.equals(tema)) {
                        oldIdTemaOfComentarioListNewComentario.getComentarioList().remove(comentarioListNewComentario);
                        oldIdTemaOfComentarioListNewComentario = em.merge(oldIdTemaOfComentarioListNewComentario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tema.getIdTema();
                if (findTema(id) == null) {
                    throw new NonexistentEntityException("The tema with id " + id + " no longer exists.");
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
            Tema tema;
            try {
                tema = em.getReference(Tema.class, id);
                tema.getIdTema();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tema with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Comentario> comentarioListOrphanCheck = tema.getComentarioList();
            for (Comentario comentarioListOrphanCheckComentario : comentarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tema (" + tema + ") cannot be destroyed since the Comentario " + comentarioListOrphanCheckComentario + " in its comentarioList field has a non-nullable idTema field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tema);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tema> findTemaEntities() {
        return findTemaEntities(true, -1, -1);
    }

    public List<Tema> findTemaEntities(int maxResults, int firstResult) {
        return findTemaEntities(false, maxResults, firstResult);
    }

    private List<Tema> findTemaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tema.class));
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

    public Tema findTema(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tema.class, id);
        } finally {
            em.close();
        }
    }

    public int getTemaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tema> rt = cq.from(Tema.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Tema busNombreTema(String nom) {
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Tema a WHERE a.tituloTema=:nom");
        con.setParameter("nom", nom);
        List<Tema> cat= con.getResultList();
        return cat.get(0);
    }

}
