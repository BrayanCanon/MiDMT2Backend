/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import entidades.Medicamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.MedicamentoMision;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gulmi
 */
public class MedicamentoJpaController implements Serializable {

    public MedicamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicamento medicamento) throws PreexistingEntityException, Exception {
        if (medicamento.getMedicamentoMisionList() == null) {
            medicamento.setMedicamentoMisionList(new ArrayList<MedicamentoMision>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MedicamentoMision> attachedMedicamentoMisionList = new ArrayList<MedicamentoMision>();
            for (MedicamentoMision medicamentoMisionListMedicamentoMisionToAttach : medicamento.getMedicamentoMisionList()) {
                medicamentoMisionListMedicamentoMisionToAttach = em.getReference(medicamentoMisionListMedicamentoMisionToAttach.getClass(), medicamentoMisionListMedicamentoMisionToAttach.getIdMedicamentoMision());
                attachedMedicamentoMisionList.add(medicamentoMisionListMedicamentoMisionToAttach);
            }
            medicamento.setMedicamentoMisionList(attachedMedicamentoMisionList);
            em.persist(medicamento);
            for (MedicamentoMision medicamentoMisionListMedicamentoMision : medicamento.getMedicamentoMisionList()) {
                Medicamento oldCodMedicamentoOfMedicamentoMisionListMedicamentoMision = medicamentoMisionListMedicamentoMision.getCodMedicamento();
                medicamentoMisionListMedicamentoMision.setCodMedicamento(medicamento);
                medicamentoMisionListMedicamentoMision = em.merge(medicamentoMisionListMedicamentoMision);
                if (oldCodMedicamentoOfMedicamentoMisionListMedicamentoMision != null) {
                    oldCodMedicamentoOfMedicamentoMisionListMedicamentoMision.getMedicamentoMisionList().remove(medicamentoMisionListMedicamentoMision);
                    oldCodMedicamentoOfMedicamentoMisionListMedicamentoMision = em.merge(oldCodMedicamentoOfMedicamentoMisionListMedicamentoMision);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedicamento(medicamento.getCodMedicamento()) != null) {
                throw new PreexistingEntityException("Medicamento " + medicamento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicamento medicamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamento persistentMedicamento = em.find(Medicamento.class, medicamento.getCodMedicamento());
            List<MedicamentoMision> medicamentoMisionListOld = persistentMedicamento.getMedicamentoMisionList();
            List<MedicamentoMision> medicamentoMisionListNew = medicamento.getMedicamentoMisionList();
            List<String> illegalOrphanMessages = null;
            for (MedicamentoMision medicamentoMisionListOldMedicamentoMision : medicamentoMisionListOld) {
                if (medicamentoMisionListNew != null && !medicamentoMisionListNew.contains(medicamentoMisionListOldMedicamentoMision)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MedicamentoMision " + medicamentoMisionListOldMedicamentoMision + " since its codMedicamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MedicamentoMision> attachedMedicamentoMisionListNew = new ArrayList<MedicamentoMision>();
            if (medicamentoMisionListNew != null) {
                for (MedicamentoMision medicamentoMisionListNewMedicamentoMisionToAttach : medicamentoMisionListNew) {
                    medicamentoMisionListNewMedicamentoMisionToAttach = em.getReference(medicamentoMisionListNewMedicamentoMisionToAttach.getClass(), medicamentoMisionListNewMedicamentoMisionToAttach.getIdMedicamentoMision());
                    attachedMedicamentoMisionListNew.add(medicamentoMisionListNewMedicamentoMisionToAttach);
                }
            }

            medicamentoMisionListNew = attachedMedicamentoMisionListNew;
            medicamento.setMedicamentoMisionList(medicamentoMisionListNew);
            medicamento = em.merge(medicamento);
            for (MedicamentoMision medicamentoMisionListNewMedicamentoMision : medicamentoMisionListNew) {
                if (!medicamentoMisionListOld.contains(medicamentoMisionListNewMedicamentoMision)) {
                    Medicamento oldCodMedicamentoOfMedicamentoMisionListNewMedicamentoMision = medicamentoMisionListNewMedicamentoMision.getCodMedicamento();
                    medicamentoMisionListNewMedicamentoMision.setCodMedicamento(medicamento);
                    medicamentoMisionListNewMedicamentoMision = em.merge(medicamentoMisionListNewMedicamentoMision);
                    if (oldCodMedicamentoOfMedicamentoMisionListNewMedicamentoMision != null && !oldCodMedicamentoOfMedicamentoMisionListNewMedicamentoMision.equals(medicamento)) {
                        oldCodMedicamentoOfMedicamentoMisionListNewMedicamentoMision.getMedicamentoMisionList().remove(medicamentoMisionListNewMedicamentoMision);
                        oldCodMedicamentoOfMedicamentoMisionListNewMedicamentoMision = em.merge(oldCodMedicamentoOfMedicamentoMisionListNewMedicamentoMision);
                    }
                }
            }//estado
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = medicamento.getCodMedicamento();
                if (findMedicamento(id) == null) {
                    throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.");
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
            Medicamento medicamento;
            try {
                medicamento = em.getReference(Medicamento.class, id);
                medicamento.getCodMedicamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MedicamentoMision> medicamentoMisionListOrphanCheck = medicamento.getMedicamentoMisionList();
            for (MedicamentoMision medicamentoMisionListOrphanCheckMedicamentoMision : medicamentoMisionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicamento (" + medicamento + ") cannot be destroyed since the MedicamentoMision " + medicamentoMisionListOrphanCheckMedicamentoMision + " in its medicamentoMisionList field has a non-nullable codMedicamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicamento> findMedicamentoEntities() {
        return findMedicamentoEntities(true, -1, -1);
    }

    public List<Medicamento> findMedicamentoEntities(int maxResults, int firstResult) {
        return findMedicamentoEntities(false, maxResults, firstResult);
    }

    private List<Medicamento> findMedicamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicamento.class));
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

    public Medicamento findMedicamento(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicamento> rt = cq.from(Medicamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Medicamento busMedicamento(Medicamento mi) {

        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT a FROM Medicamento a WHERE a.codMedicamento=:pa AND a.estado=:estado");
        con.setParameter("pa", mi.getCodMedicamento());
        con.setParameter("estado", "a");
        return (Medicamento) con.getResultList().get(0);

    }

    /**
     * TO DO 13/05
     *
     * @param codApoyoSocial
     * @return
     */
    public List<Object[]> consultarVerificacionesApoyoSocial(String codApoyoSocial) {
        EntityManager em = emf.createEntityManager();
        Query cons = em.createNativeQuery("");
        return cons.getResultList();
    }

}
