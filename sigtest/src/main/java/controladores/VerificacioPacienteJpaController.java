/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import entidades.RutinaPaciente;
import entidades.VerificacionRutinaPaciente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author carlos.escobar
 */
public class VerificacioPacienteJpaController {

    public VerificacioPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void create(VerificacionRutinaPaciente rutinaPaciente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rutinaPaciente);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * TO-DO
     * <p>Carlos D. Escobar - 26/04/2019</p>
     * @param codPaciente
     * @param idRutina
     * @return 
     */
    public List<VerificacionRutinaPaciente> consultaRutinaPaciente(String codPaciente, Integer idRutina){
        
       EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT "
                + "verificacionPaciente "
                + "FROM VerificacionRutinaPaciente verificacionPaciente "
                + "WHERE verificacionPaciente.idRutinaPaciente.idRutina.id =:idRutina "
                + "AND verificacionPaciente.idRutinaPaciente.codPaciente.codPaciente=:codP "
                + "ORDER BY verificacionPaciente.fecha"
        );
        con.setParameter("codP", codPaciente);
        con.setParameter("idRutina", idRutina);
        List<VerificacionRutinaPaciente> a = con.getResultList();
        return a;
    }
}
