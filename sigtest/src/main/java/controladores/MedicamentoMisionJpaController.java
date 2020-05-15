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
import entidades.Medicamento;
import entidades.MedicamentoMision;
import entidades.MisionPaciente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class MedicamentoMisionJpaController implements Serializable {

    public MedicamentoMisionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MedicamentoMision medicamentoMision) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamento codMedicamento = medicamentoMision.getCodMedicamento();
            if (codMedicamento != null) {
                codMedicamento = em.getReference(codMedicamento.getClass(), codMedicamento.getCodMedicamento());
                medicamentoMision.setCodMedicamento(codMedicamento);
            }
            MisionPaciente idMisionPaciente = medicamentoMision.getIdMisionPaciente();
            if (idMisionPaciente != null) {
                idMisionPaciente = em.getReference(idMisionPaciente.getClass(), idMisionPaciente.getIdMisionPaciente());
                medicamentoMision.setIdMisionPaciente(idMisionPaciente);
            }
            em.persist(medicamentoMision);
            if (codMedicamento != null) {
                codMedicamento.getMedicamentoMisionList().add(medicamentoMision);
                codMedicamento = em.merge(codMedicamento);
            }
            if (idMisionPaciente != null) {
                idMisionPaciente.getMedicamentoMisionList().add(medicamentoMision);
                idMisionPaciente = em.merge(idMisionPaciente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MedicamentoMision medicamentoMision) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MedicamentoMision persistentMedicamentoMision = em.find(MedicamentoMision.class, medicamentoMision.getIdMedicamentoMision());
            Medicamento codMedicamentoOld = persistentMedicamentoMision.getCodMedicamento();
            Medicamento codMedicamentoNew = medicamentoMision.getCodMedicamento();
            MisionPaciente idMisionPacienteOld = persistentMedicamentoMision.getIdMisionPaciente();
            MisionPaciente idMisionPacienteNew = medicamentoMision.getIdMisionPaciente();
            if (codMedicamentoNew != null) {
                codMedicamentoNew = em.getReference(codMedicamentoNew.getClass(), codMedicamentoNew.getCodMedicamento());
                medicamentoMision.setCodMedicamento(codMedicamentoNew);
            }
            if (idMisionPacienteNew != null) {
                idMisionPacienteNew = em.getReference(idMisionPacienteNew.getClass(), idMisionPacienteNew.getIdMisionPaciente());
                medicamentoMision.setIdMisionPaciente(idMisionPacienteNew);
            }
            medicamentoMision = em.merge(medicamentoMision);
            if (codMedicamentoOld != null && !codMedicamentoOld.equals(codMedicamentoNew)) {
                codMedicamentoOld.getMedicamentoMisionList().remove(medicamentoMision);
                codMedicamentoOld = em.merge(codMedicamentoOld);
            }
            if (codMedicamentoNew != null && !codMedicamentoNew.equals(codMedicamentoOld)) {
                codMedicamentoNew.getMedicamentoMisionList().add(medicamentoMision);
                codMedicamentoNew = em.merge(codMedicamentoNew);
            }
            if (idMisionPacienteOld != null && !idMisionPacienteOld.equals(idMisionPacienteNew)) {
                idMisionPacienteOld.getMedicamentoMisionList().remove(medicamentoMision);
                idMisionPacienteOld = em.merge(idMisionPacienteOld);
            }
            if (idMisionPacienteNew != null && !idMisionPacienteNew.equals(idMisionPacienteOld)) {
                idMisionPacienteNew.getMedicamentoMisionList().add(medicamentoMision);
                idMisionPacienteNew = em.merge(idMisionPacienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicamentoMision.getIdMedicamentoMision();
                if (findMedicamentoMision(id) == null) {
                    throw new NonexistentEntityException("The medicamentoMision with id " + id + " no longer exists.");
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
            MedicamentoMision medicamentoMision;
            try {
                medicamentoMision = em.getReference(MedicamentoMision.class, id);
                medicamentoMision.getIdMedicamentoMision();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamentoMision with id " + id + " no longer exists.", enfe);
            }
            Medicamento codMedicamento = medicamentoMision.getCodMedicamento();
            if (codMedicamento != null) {
                codMedicamento.getMedicamentoMisionList().remove(medicamentoMision);
                codMedicamento = em.merge(codMedicamento);
            }
            MisionPaciente idMisionPaciente = medicamentoMision.getIdMisionPaciente();
            if (idMisionPaciente != null) {
                idMisionPaciente.getMedicamentoMisionList().remove(medicamentoMision);
                idMisionPaciente = em.merge(idMisionPaciente);
            }
            em.remove(medicamentoMision);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MedicamentoMision> findMedicamentoMisionEntities() {
        return findMedicamentoMisionEntities(true, -1, -1);
    }

    public List<MedicamentoMision> findMedicamentoMisionEntities(int maxResults, int firstResult) {
        return findMedicamentoMisionEntities(false, maxResults, firstResult);
    }

    private List<MedicamentoMision> findMedicamentoMisionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MedicamentoMision.class));
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

    public MedicamentoMision findMedicamentoMision(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MedicamentoMision.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentoMisionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MedicamentoMision> rt = cq.from(MedicamentoMision.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
      public MedicamentoMision busMedicamentoMision(MisionPaciente mi){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MedicamentoMision a WHERE a.idMisionPaciente=:pa AND a.estado=:estado");
        con.setParameter("pa",mi);
        con.setParameter("estado", "a");
        return (MedicamentoMision) con.getResultList().get(0);
        
    }
}
