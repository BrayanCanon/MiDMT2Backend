/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import entidades.RutinaPaciente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Charly
 */
public class RutinaPacienteJpaController {

    public RutinaPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * TO-DO
     * <p>Carlos D. Escobar - 03/05/2019</p>
     *
     * @param rutinaPaciente
     */
    public void create(RutinaPaciente rutinaPaciente) {
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
     * <p>Carlos D. Escobar - 03/05/2019</p>
     *
     * @param idrutina
     * @param codPaciente
     * @return
     */
    public RutinaPaciente consultarRutinaPacientePorId(Integer idrutina,String codPaciente) {

        EntityManager em = emf.createEntityManager();
        Query con = em.createQuery("SELECT "
                + "rutinaP "
                + "FROM RutinaPaciente rutinaP "
                + "WHERE rutinaP.idRutina.id=:idRutina "
                + "AND rutinaP.codPaciente.codPaciente=:codPaciente");
        con.setParameter("idRutina", idrutina);
        con.setParameter("codPaciente", codPaciente);
        RutinaPaciente a = (RutinaPaciente) con.getResultList().get(0);
        return a;
    }
}
