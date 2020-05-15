/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.MedicamentoMision;
import entidades.Mision;
import entidades.MisionPaciente;
import entidades.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import logica.services.PacienteService;

/**
 *
 * @author gulmi
 */
public class MisionPacienteJpaController implements Serializable {

    public MisionPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MisionPaciente misionPaciente) throws PreexistingEntityException, Exception {
        if (misionPaciente.getMedicamentoMisionList() == null) {
            misionPaciente.setMedicamentoMisionList(new ArrayList<MedicamentoMision>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MedicamentoMision> attachedMedicamentoMisionList = new ArrayList<MedicamentoMision>();
            for (MedicamentoMision medicamentoMisionListMedicamentoMisionToAttach : misionPaciente.getMedicamentoMisionList()) {
                medicamentoMisionListMedicamentoMisionToAttach = em.getReference(medicamentoMisionListMedicamentoMisionToAttach.getClass(), medicamentoMisionListMedicamentoMisionToAttach.getIdMedicamentoMision());
                attachedMedicamentoMisionList.add(medicamentoMisionListMedicamentoMisionToAttach);
            }
            misionPaciente.setMedicamentoMisionList(attachedMedicamentoMisionList);
            em.persist(misionPaciente);
            for (MedicamentoMision medicamentoMisionListMedicamentoMision : misionPaciente.getMedicamentoMisionList()) {
                MisionPaciente oldIdMisionPacienteOfMedicamentoMisionListMedicamentoMision = medicamentoMisionListMedicamentoMision.getIdMisionPaciente();
                medicamentoMisionListMedicamentoMision.setIdMisionPaciente(misionPaciente);
                medicamentoMisionListMedicamentoMision = em.merge(medicamentoMisionListMedicamentoMision);
                if (oldIdMisionPacienteOfMedicamentoMisionListMedicamentoMision != null) {
                    oldIdMisionPacienteOfMedicamentoMisionListMedicamentoMision.getMedicamentoMisionList().remove(medicamentoMisionListMedicamentoMision);
                    oldIdMisionPacienteOfMedicamentoMisionListMedicamentoMision = em.merge(oldIdMisionPacienteOfMedicamentoMisionListMedicamentoMision);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMisionPaciente(misionPaciente.getIdMisionPaciente()) != null) {
                throw new PreexistingEntityException("MisionPaciente " + misionPaciente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void editsql(MisionPaciente m){
          EntityManager em = emf.createEntityManager();
             EntityTransaction et = em.getTransaction();
             et.begin();
             em.createNativeQuery("update mision_paciente set mision_paciente.completada="+m.getCompletada()+" where id_mision_paciente="+m.getIdMisionPaciente());
             et.commit();
    }

    public void edit(MisionPaciente misionPaciente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MisionPaciente persistentMisionPaciente = em.find(MisionPaciente.class, misionPaciente.getIdMisionPaciente());
            List<MedicamentoMision> medicamentoMisionListOld = persistentMisionPaciente.getMedicamentoMisionList();
            List<MedicamentoMision> medicamentoMisionListNew = misionPaciente.getMedicamentoMisionList();
            List<MedicamentoMision> attachedMedicamentoMisionListNew = new ArrayList<MedicamentoMision>();
            for (MedicamentoMision medicamentoMisionListNewMedicamentoMisionToAttach : medicamentoMisionListNew) {
                medicamentoMisionListNewMedicamentoMisionToAttach = em.getReference(medicamentoMisionListNewMedicamentoMisionToAttach.getClass(), medicamentoMisionListNewMedicamentoMisionToAttach.getIdMedicamentoMision());
                attachedMedicamentoMisionListNew.add(medicamentoMisionListNewMedicamentoMisionToAttach);
            }
            medicamentoMisionListNew = attachedMedicamentoMisionListNew;
            misionPaciente.setMedicamentoMisionList(medicamentoMisionListNew);
            misionPaciente = em.merge(misionPaciente);
            for (MedicamentoMision medicamentoMisionListOldMedicamentoMision : medicamentoMisionListOld) {
                if (!medicamentoMisionListNew.contains(medicamentoMisionListOldMedicamentoMision)) {
                    medicamentoMisionListOldMedicamentoMision.setIdMisionPaciente(null);
                    medicamentoMisionListOldMedicamentoMision = em.merge(medicamentoMisionListOldMedicamentoMision);
                }
            }
            for (MedicamentoMision medicamentoMisionListNewMedicamentoMision : medicamentoMisionListNew) {
                if (!medicamentoMisionListOld.contains(medicamentoMisionListNewMedicamentoMision)) {
                    MisionPaciente oldIdMisionPacienteOfMedicamentoMisionListNewMedicamentoMision = medicamentoMisionListNewMedicamentoMision.getIdMisionPaciente();
                    medicamentoMisionListNewMedicamentoMision.setIdMisionPaciente(misionPaciente);
                    medicamentoMisionListNewMedicamentoMision = em.merge(medicamentoMisionListNewMedicamentoMision);
                    if (oldIdMisionPacienteOfMedicamentoMisionListNewMedicamentoMision != null && !oldIdMisionPacienteOfMedicamentoMisionListNewMedicamentoMision.equals(misionPaciente)) {
                        oldIdMisionPacienteOfMedicamentoMisionListNewMedicamentoMision.getMedicamentoMisionList().remove(medicamentoMisionListNewMedicamentoMision);
                        oldIdMisionPacienteOfMedicamentoMisionListNewMedicamentoMision = em.merge(oldIdMisionPacienteOfMedicamentoMisionListNewMedicamentoMision);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = misionPaciente.getIdMisionPaciente();
                if (findMisionPaciente(id) == null) {
                    throw new NonexistentEntityException("The misionPaciente with id " + id + " no longer exists.");
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
            MisionPaciente misionPaciente;
            try {
                misionPaciente = em.getReference(MisionPaciente.class, id);
                misionPaciente.getIdMisionPaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The misionPaciente with id " + id + " no longer exists.", enfe);
            }
            List<MedicamentoMision> medicamentoMisionList = misionPaciente.getMedicamentoMisionList();
            for (MedicamentoMision medicamentoMisionListMedicamentoMision : medicamentoMisionList) {
                medicamentoMisionListMedicamentoMision.setIdMisionPaciente(null);
                medicamentoMisionListMedicamentoMision = em.merge(medicamentoMisionListMedicamentoMision);
            }
            em.remove(misionPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MisionPaciente> findMisionPacienteEntities() {
        return findMisionPacienteEntities(true, -1, -1);
    }

    public List<MisionPaciente> findMisionPacienteEntities(int maxResults, int firstResult) {
        return findMisionPacienteEntities(false, maxResults, firstResult);
    }

    private List<MisionPaciente> findMisionPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MisionPaciente.class));
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

    public MisionPaciente findMisionPaciente(Integer id) {
        EntityManager em = getEntityManager();
     
            try {
            return em.find(MisionPaciente.class, id);
       
            }finally {
            em.close();
        }
    }

        public MisionPaciente findsqlMision(Integer id){
         EntityManager em = getEntityManager();
            try{
                Query con= em.createNativeQuery("select * from mision_paciente where id_mision_paciente="+id);
                Object[] a=(Object[])con.getSingleResult();
                MisionPaciente salida=new MisionPaciente();
                PacienteService ps= new  PacienteService();
                        
                Paciente sal=new Paciente();
                sal.setCodPaciente((String)a[1]);
              Paciente oo=  ps.pacienteListaId((String)a[1]);
              sal.setPuntajeAlim(oo.getPuntajeAlim());
              sal.setPuntajeEjer(oo.getPuntajeEjer());
                salida.setCodPaciente(sal);
                
                salida.setEstado((String)a[6]);
                Mision n=new Mision();
                n.setIdMision((Integer)a[2]);
                salida.setIdMisionPaciente(id);
                salida.setIdMision(n);
                return salida;
               }
            catch(Exception e){
                return null;
            }
                
        }
    public int getMisionPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MisionPaciente> rt = cq.from(MisionPaciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<MisionPaciente> misionPacientelista(Paciente an){
         EntityManager em = emf.createEntityManager();

        Query con= em.createQuery("SELECT a FROM MisionPaciente a WHERE a.codPaciente=:codPaciente AND a.estado=:estado ");
        con.setParameter("codPaciente", an);
        con.setParameter("estado", "a");
        List<MisionPaciente> sd=con.getResultList();
        if(sd.size()>0){
        return sd;
}
        return null;

    }   
       public List<Mision> filtroMisionCompletada(Paciente mi){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a.idMision FROM MisionPaciente a WHERE a.codPaciente=:pa AND (a.completada=:com OR a.completada=:com2) AND a.estado=:estado");
        con.setParameter("pa",mi);
        con.setParameter("com", "1");
        con.setParameter("com2", "2");
        con.setParameter("estado", "a");
        return con.getResultList();
        
    }
        public MisionPaciente MisionMedi(Paciente mi){

        EntityManager em = emf.createEntityManager();
        Query con= em.createQuery("SELECT a FROM MisionPaciente a WHERE a.codPaciente=:pa AND a.estado=:estado");
        con.setParameter("pa",mi);
        con.setParameter("estado", "m");
        return (MisionPaciente) con.getResultList().get(0);
        
    }
     
}
