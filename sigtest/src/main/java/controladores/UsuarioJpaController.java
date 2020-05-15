/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Perfil;
import entidades.RecursoApoyo;
import java.util.ArrayList;
import java.util.List;
import entidades.Comentario;
import entidades.Notificacion;
import entidades.Mensaje;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getRecursoApoyoList() == null) {
            usuario.setRecursoApoyoList(new ArrayList<RecursoApoyo>());
        }
        if (usuario.getComentarioList() == null) {
            usuario.setComentarioList(new ArrayList<Comentario>());
        }
        if (usuario.getNotificacionList() == null) {
            usuario.setNotificacionList(new ArrayList<Notificacion>());
        }
        if (usuario.getMensajeList() == null) {
            usuario.setMensajeList(new ArrayList<Mensaje>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil idPerfil = usuario.getIdPerfil();
            if (idPerfil != null) {
                idPerfil = em.getReference(idPerfil.getClass(), idPerfil.getIdPerfil());
                usuario.setIdPerfil(idPerfil);
            }
            List<RecursoApoyo> attachedRecursoApoyoList = new ArrayList<RecursoApoyo>();
            for (RecursoApoyo recursoApoyoListRecursoApoyoToAttach : usuario.getRecursoApoyoList()) {
                recursoApoyoListRecursoApoyoToAttach = em.getReference(recursoApoyoListRecursoApoyoToAttach.getClass(), recursoApoyoListRecursoApoyoToAttach.getIdRecapoyo());
                attachedRecursoApoyoList.add(recursoApoyoListRecursoApoyoToAttach);
            }
            usuario.setRecursoApoyoList(attachedRecursoApoyoList);
            List<Comentario> attachedComentarioList = new ArrayList<Comentario>();
            for (Comentario comentarioListComentarioToAttach : usuario.getComentarioList()) {
                comentarioListComentarioToAttach = em.getReference(comentarioListComentarioToAttach.getClass(), comentarioListComentarioToAttach.getIdComentario());
                attachedComentarioList.add(comentarioListComentarioToAttach);
            }
            usuario.setComentarioList(attachedComentarioList);
            List<Notificacion> attachedNotificacionList = new ArrayList<Notificacion>();
            for (Notificacion notificacionListNotificacionToAttach : usuario.getNotificacionList()) {
                notificacionListNotificacionToAttach = em.getReference(notificacionListNotificacionToAttach.getClass(), notificacionListNotificacionToAttach.getIdnotificacion());
                attachedNotificacionList.add(notificacionListNotificacionToAttach);
            }
            usuario.setNotificacionList(attachedNotificacionList);
            List<Mensaje> attachedMensajeList = new ArrayList<Mensaje>();
            for (Mensaje mensajeListMensajeToAttach : usuario.getMensajeList()) {
                mensajeListMensajeToAttach = em.getReference(mensajeListMensajeToAttach.getClass(), mensajeListMensajeToAttach.getIdMensaje());
                attachedMensajeList.add(mensajeListMensajeToAttach);
            }
            usuario.setMensajeList(attachedMensajeList);
            em.persist(usuario);
            if (idPerfil != null) {
                idPerfil.getUsuarioList().add(usuario);
                idPerfil = em.merge(idPerfil);
            }
            for (RecursoApoyo recursoApoyoListRecursoApoyo : usuario.getRecursoApoyoList()) {
                Usuario oldNomUsuarioOfRecursoApoyoListRecursoApoyo = recursoApoyoListRecursoApoyo.getNomUsuario();
                recursoApoyoListRecursoApoyo.setNomUsuario(usuario);
                recursoApoyoListRecursoApoyo = em.merge(recursoApoyoListRecursoApoyo);
                if (oldNomUsuarioOfRecursoApoyoListRecursoApoyo != null) {
                    oldNomUsuarioOfRecursoApoyoListRecursoApoyo.getRecursoApoyoList().remove(recursoApoyoListRecursoApoyo);
                    oldNomUsuarioOfRecursoApoyoListRecursoApoyo = em.merge(oldNomUsuarioOfRecursoApoyoListRecursoApoyo);
                }
            }
            for (Comentario comentarioListComentario : usuario.getComentarioList()) {
                Usuario oldNomUsuarioOfComentarioListComentario = comentarioListComentario.getNomUsuario();
                comentarioListComentario.setNomUsuario(usuario);
                comentarioListComentario = em.merge(comentarioListComentario);
                if (oldNomUsuarioOfComentarioListComentario != null) {
                    oldNomUsuarioOfComentarioListComentario.getComentarioList().remove(comentarioListComentario);
                    oldNomUsuarioOfComentarioListComentario = em.merge(oldNomUsuarioOfComentarioListComentario);
                }
            }
            for (Notificacion notificacionListNotificacion : usuario.getNotificacionList()) {
                Usuario oldNomUsuarioOfNotificacionListNotificacion = notificacionListNotificacion.getNomUsuario();
                notificacionListNotificacion.setNomUsuario(usuario);
                notificacionListNotificacion = em.merge(notificacionListNotificacion);
                if (oldNomUsuarioOfNotificacionListNotificacion != null) {
                    oldNomUsuarioOfNotificacionListNotificacion.getNotificacionList().remove(notificacionListNotificacion);
                    oldNomUsuarioOfNotificacionListNotificacion = em.merge(oldNomUsuarioOfNotificacionListNotificacion);
                }
            }
            for (Mensaje mensajeListMensaje : usuario.getMensajeList()) {
                Usuario oldNomUsuarioOfMensajeListMensaje = mensajeListMensaje.getNomUsuario();
                mensajeListMensaje.setNomUsuario(usuario);
                mensajeListMensaje = em.merge(mensajeListMensaje);
                if (oldNomUsuarioOfMensajeListMensaje != null) {
                    oldNomUsuarioOfMensajeListMensaje.getMensajeList().remove(mensajeListMensaje);
                    oldNomUsuarioOfMensajeListMensaje = em.merge(oldNomUsuarioOfMensajeListMensaje);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getNomUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getNomUsuario());
            Perfil idPerfilOld = persistentUsuario.getIdPerfil();
            Perfil idPerfilNew = usuario.getIdPerfil();
            List<RecursoApoyo> recursoApoyoListOld = persistentUsuario.getRecursoApoyoList();
            List<RecursoApoyo> recursoApoyoListNew = usuario.getRecursoApoyoList();
            List<Comentario> comentarioListOld = persistentUsuario.getComentarioList();
            List<Comentario> comentarioListNew = usuario.getComentarioList();
            List<Notificacion> notificacionListOld = persistentUsuario.getNotificacionList();
            List<Notificacion> notificacionListNew = usuario.getNotificacionList();
            List<Mensaje> mensajeListOld = persistentUsuario.getMensajeList();
            List<Mensaje> mensajeListNew = usuario.getMensajeList();
            List<String> illegalOrphanMessages = null;
            for (Comentario comentarioListOldComentario : comentarioListOld) {
                if (!comentarioListNew.contains(comentarioListOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioListOldComentario + " since its nomUsuario field is not nullable.");
                }
            }
            for (Notificacion notificacionListOldNotificacion : notificacionListOld) {
                if (!notificacionListNew.contains(notificacionListOldNotificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notificacion " + notificacionListOldNotificacion + " since its nomUsuario field is not nullable.");
                }
            }
            for (Mensaje mensajeListOldMensaje : mensajeListOld) {
                if (!mensajeListNew.contains(mensajeListOldMensaje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mensaje " + mensajeListOldMensaje + " since its nomUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPerfilNew != null) {
                idPerfilNew = em.getReference(idPerfilNew.getClass(), idPerfilNew.getIdPerfil());
                usuario.setIdPerfil(idPerfilNew);
            }
            List<RecursoApoyo> attachedRecursoApoyoListNew = new ArrayList<RecursoApoyo>();
            for (RecursoApoyo recursoApoyoListNewRecursoApoyoToAttach : recursoApoyoListNew) {
                recursoApoyoListNewRecursoApoyoToAttach = em.getReference(recursoApoyoListNewRecursoApoyoToAttach.getClass(), recursoApoyoListNewRecursoApoyoToAttach.getIdRecapoyo());
                attachedRecursoApoyoListNew.add(recursoApoyoListNewRecursoApoyoToAttach);
            }
            recursoApoyoListNew = attachedRecursoApoyoListNew;
            usuario.setRecursoApoyoList(recursoApoyoListNew);
            List<Comentario> attachedComentarioListNew = new ArrayList<Comentario>();
            for (Comentario comentarioListNewComentarioToAttach : comentarioListNew) {
                comentarioListNewComentarioToAttach = em.getReference(comentarioListNewComentarioToAttach.getClass(), comentarioListNewComentarioToAttach.getIdComentario());
                attachedComentarioListNew.add(comentarioListNewComentarioToAttach);
            }
            comentarioListNew = attachedComentarioListNew;
            usuario.setComentarioList(comentarioListNew);
            List<Notificacion> attachedNotificacionListNew = new ArrayList<Notificacion>();
            for (Notificacion notificacionListNewNotificacionToAttach : notificacionListNew) {
                notificacionListNewNotificacionToAttach = em.getReference(notificacionListNewNotificacionToAttach.getClass(), notificacionListNewNotificacionToAttach.getIdnotificacion());
                attachedNotificacionListNew.add(notificacionListNewNotificacionToAttach);
            }
            notificacionListNew = attachedNotificacionListNew;
            usuario.setNotificacionList(notificacionListNew);
            List<Mensaje> attachedMensajeListNew = new ArrayList<Mensaje>();
            for (Mensaje mensajeListNewMensajeToAttach : mensajeListNew) {
                mensajeListNewMensajeToAttach = em.getReference(mensajeListNewMensajeToAttach.getClass(), mensajeListNewMensajeToAttach.getIdMensaje());
                attachedMensajeListNew.add(mensajeListNewMensajeToAttach);
            }
            mensajeListNew = attachedMensajeListNew;
            usuario.setMensajeList(mensajeListNew);
            usuario = em.merge(usuario);
            if (idPerfilOld != null && !idPerfilOld.equals(idPerfilNew)) {
                idPerfilOld.getUsuarioList().remove(usuario);
                idPerfilOld = em.merge(idPerfilOld);
            }
            if (idPerfilNew != null && !idPerfilNew.equals(idPerfilOld)) {
                idPerfilNew.getUsuarioList().add(usuario);
                idPerfilNew = em.merge(idPerfilNew);
            }
            for (RecursoApoyo recursoApoyoListOldRecursoApoyo : recursoApoyoListOld) {
                if (!recursoApoyoListNew.contains(recursoApoyoListOldRecursoApoyo)) {
                    recursoApoyoListOldRecursoApoyo.setNomUsuario(null);
                    recursoApoyoListOldRecursoApoyo = em.merge(recursoApoyoListOldRecursoApoyo);
                }
            }
            for (RecursoApoyo recursoApoyoListNewRecursoApoyo : recursoApoyoListNew) {
                if (!recursoApoyoListOld.contains(recursoApoyoListNewRecursoApoyo)) {
                    Usuario oldNomUsuarioOfRecursoApoyoListNewRecursoApoyo = recursoApoyoListNewRecursoApoyo.getNomUsuario();
                    recursoApoyoListNewRecursoApoyo.setNomUsuario(usuario);
                    recursoApoyoListNewRecursoApoyo = em.merge(recursoApoyoListNewRecursoApoyo);
                    if (oldNomUsuarioOfRecursoApoyoListNewRecursoApoyo != null && !oldNomUsuarioOfRecursoApoyoListNewRecursoApoyo.equals(usuario)) {
                        oldNomUsuarioOfRecursoApoyoListNewRecursoApoyo.getRecursoApoyoList().remove(recursoApoyoListNewRecursoApoyo);
                        oldNomUsuarioOfRecursoApoyoListNewRecursoApoyo = em.merge(oldNomUsuarioOfRecursoApoyoListNewRecursoApoyo);
                    }
                }
            }
            for (Comentario comentarioListNewComentario : comentarioListNew) {
                if (!comentarioListOld.contains(comentarioListNewComentario)) {
                    Usuario oldNomUsuarioOfComentarioListNewComentario = comentarioListNewComentario.getNomUsuario();
                    comentarioListNewComentario.setNomUsuario(usuario);
                    comentarioListNewComentario = em.merge(comentarioListNewComentario);
                    if (oldNomUsuarioOfComentarioListNewComentario != null && !oldNomUsuarioOfComentarioListNewComentario.equals(usuario)) {
                        oldNomUsuarioOfComentarioListNewComentario.getComentarioList().remove(comentarioListNewComentario);
                        oldNomUsuarioOfComentarioListNewComentario = em.merge(oldNomUsuarioOfComentarioListNewComentario);
                    }
                }
            }
            for (Notificacion notificacionListNewNotificacion : notificacionListNew) {
                if (!notificacionListOld.contains(notificacionListNewNotificacion)) {
                    Usuario oldNomUsuarioOfNotificacionListNewNotificacion = notificacionListNewNotificacion.getNomUsuario();
                    notificacionListNewNotificacion.setNomUsuario(usuario);
                    notificacionListNewNotificacion = em.merge(notificacionListNewNotificacion);
                    if (oldNomUsuarioOfNotificacionListNewNotificacion != null && !oldNomUsuarioOfNotificacionListNewNotificacion.equals(usuario)) {
                        oldNomUsuarioOfNotificacionListNewNotificacion.getNotificacionList().remove(notificacionListNewNotificacion);
                        oldNomUsuarioOfNotificacionListNewNotificacion = em.merge(oldNomUsuarioOfNotificacionListNewNotificacion);
                    }
                }
            }
            for (Mensaje mensajeListNewMensaje : mensajeListNew) {
                if (!mensajeListOld.contains(mensajeListNewMensaje)) {
                    Usuario oldNomUsuarioOfMensajeListNewMensaje = mensajeListNewMensaje.getNomUsuario();
                    mensajeListNewMensaje.setNomUsuario(usuario);
                    mensajeListNewMensaje = em.merge(mensajeListNewMensaje);
                    if (oldNomUsuarioOfMensajeListNewMensaje != null && !oldNomUsuarioOfMensajeListNewMensaje.equals(usuario)) {
                        oldNomUsuarioOfMensajeListNewMensaje.getMensajeList().remove(mensajeListNewMensaje);
                        oldNomUsuarioOfMensajeListNewMensaje = em.merge(oldNomUsuarioOfMensajeListNewMensaje);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getNomUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getNomUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Comentario> comentarioListOrphanCheck = usuario.getComentarioList();
            for (Comentario comentarioListOrphanCheckComentario : comentarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Comentario " + comentarioListOrphanCheckComentario + " in its comentarioList field has a non-nullable nomUsuario field.");
            }
            List<Notificacion> notificacionListOrphanCheck = usuario.getNotificacionList();
            for (Notificacion notificacionListOrphanCheckNotificacion : notificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Notificacion " + notificacionListOrphanCheckNotificacion + " in its notificacionList field has a non-nullable nomUsuario field.");
            }
            List<Mensaje> mensajeListOrphanCheck = usuario.getMensajeList();
            for (Mensaje mensajeListOrphanCheckMensaje : mensajeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Mensaje " + mensajeListOrphanCheckMensaje + " in its mensajeList field has a non-nullable nomUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Perfil idPerfil = usuario.getIdPerfil();
            if (idPerfil != null) {
                idPerfil.getUsuarioList().remove(usuario);
                idPerfil = em.merge(idPerfil);
            }
            List<RecursoApoyo> recursoApoyoList = usuario.getRecursoApoyoList();
            for (RecursoApoyo recursoApoyoListRecursoApoyo : recursoApoyoList) {
                recursoApoyoListRecursoApoyo.setNomUsuario(null);
                recursoApoyoListRecursoApoyo = em.merge(recursoApoyoListRecursoApoyo);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
 public Usuario sqlbuscCod(Usuario us){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Usuario a WHERE a.codUsuario=:cod ");
        con.setParameter("cod",us.getCodUsuario());
        return (Usuario) con.getResultList().get(0);
    }
 
  public int repetidas(Usuario us){
        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM Usuario a WHERE a.codUsuario=:cod ");
        con.setParameter("cod",us.getCodUsuario());
        return con.getResultList().size();
    }


}
