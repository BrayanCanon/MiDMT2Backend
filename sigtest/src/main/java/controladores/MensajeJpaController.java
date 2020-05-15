/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Mensaje;
import entidades.Mensajesend;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class MensajeJpaController implements Serializable {

    public MensajeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mensaje mensaje) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nomUsuario = mensaje.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario = em.getReference(nomUsuario.getClass(), nomUsuario.getNomUsuario());
                mensaje.setNomUsuario(nomUsuario);
            }
            em.persist(mensaje);
            if (nomUsuario != null) {
                nomUsuario.getMensajeList().add(mensaje);
                nomUsuario = em.merge(nomUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mensaje mensaje) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mensaje persistentMensaje = em.find(Mensaje.class, mensaje.getIdMensaje());
            Usuario nomUsuarioOld = persistentMensaje.getNomUsuario();
            Usuario nomUsuarioNew = mensaje.getNomUsuario();
            if (nomUsuarioNew != null) {
                nomUsuarioNew = em.getReference(nomUsuarioNew.getClass(), nomUsuarioNew.getNomUsuario());
                mensaje.setNomUsuario(nomUsuarioNew);
            }
            mensaje = em.merge(mensaje);
            if (nomUsuarioOld != null && !nomUsuarioOld.equals(nomUsuarioNew)) {
                nomUsuarioOld.getMensajeList().remove(mensaje);
                nomUsuarioOld = em.merge(nomUsuarioOld);
            }
            if (nomUsuarioNew != null && !nomUsuarioNew.equals(nomUsuarioOld)) {
                nomUsuarioNew.getMensajeList().add(mensaje);
                nomUsuarioNew = em.merge(nomUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mensaje.getIdMensaje();
                if (findMensaje(id) == null) {
                    throw new NonexistentEntityException("The mensaje with id " + id + " no longer exists.");
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
            Mensaje mensaje;
            try {
                mensaje = em.getReference(Mensaje.class, id);
                mensaje.getIdMensaje();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mensaje with id " + id + " no longer exists.", enfe);
            }
            Usuario nomUsuario = mensaje.getNomUsuario();
            if (nomUsuario != null) {
                nomUsuario.getMensajeList().remove(mensaje);
                nomUsuario = em.merge(nomUsuario);
            }
            em.remove(mensaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mensaje> findMensajeEntities() {
        return findMensajeEntities(true, -1, -1);
    }

    public List<Mensaje> findMensajeEntities(int maxResults, int firstResult) {
        return findMensajeEntities(false, maxResults, firstResult);
    }

    private List<Mensaje> findMensajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mensaje.class));
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

    public Mensaje findMensaje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mensaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getMensajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mensaje> rt = cq.from(Mensaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Mensaje> BuscarMensajeUsu(Usuario us) {
        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT a FROM Mensaje a WHERE a.nomUsuario:cod ");
        con.setParameter("cod", us);
        List<Mensaje> cat = con.getResultList();
        return cat;
    }

    public List<Mensaje> sqlbuscMensaje(Mensaje us) {
        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT a FROM Mensaje a WHERE a.contenidoMensaje=:cod ");
        con.setParameter("cod", us.getContenidoMensaje());
        return con.getResultList();
    }

    public List<Mensaje> BuscarMensajedest(String cod) {
        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT a FROM Mensaje a WHERE a.nomDestinatario=:cod ");
        con.setParameter("cod", cod);
        return con.getResultList();
    }

    public ArrayList consultachat(String us1, String us2) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("(SELECT * FROM usuario NATURAL JOIN mensaje where (nom_usuario='" + us1 + "' or nom_usuario='" + us2 + "') and (nom_destinatario='" + us1 + "' or nom_destinatario='" + us2 + "') order by id_mensaje DESC LIMIT 10) ORDER by id_mensaje");
        List<Object[]> mensajes = q.getResultList();
        ArrayList<Mensajesend> mensajesarr = new ArrayList();

        for (int a = 0; a < mensajes.size(); a++) {
            Object[] ax = mensajes.get(a);
            Mensajesend mensaje = new Mensajesend();
            mensaje.remitente = String.valueOf(ax[0]);
            mensaje.destinatario = String.valueOf(ax[10]);
            mensaje.mensaje = String.valueOf(ax[9]);
            mensaje.fecha = String.valueOf(ax[11]);
            mensajesarr.add(mensaje);
        }
        return mensajesarr;
    }

    /**
     * TO-DO
     * <p>Carlos D. Escobar - 12/05/2019</p>
     *
     * @param desti
     * @param us
     * @return
     */
    public String cantidadNotificaciones(String desti) {
        EntityManager em = emf.createEntityManager();
        Date fechaActual = new Date();
        Query con = em.createQuery("SELECT count(a.idMensaje) FROM Mensaje a WHERE a.nomDestinatario=:desti AND a.fecha = :fechaAc");
        con.setParameter("desti", desti);
        con.setParameter("fechaAc", fechaActual);
        String conteo = con.getSingleResult().toString();
        return conteo;
    }
}
