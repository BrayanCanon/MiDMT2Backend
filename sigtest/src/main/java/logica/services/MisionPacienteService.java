/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.MisionJpaController;
import controladores.MisionPacienteJpaController;
import entidades.Mision;
import entidades.MisionPaciente;
import entidades.Paciente;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class MisionPacienteService {

    MisionPacienteJpaController misionPacientelist;
    MisionJpaController misionController;

    public MisionPacienteService() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DT2PU");
        misionPacientelist = new MisionPacienteJpaController(factory);
        misionController = new MisionJpaController(factory);

    }

    public List misionPacientelista() {
        return misionPacientelist.findMisionPacienteEntities();

    }

    public MisionPaciente misionPacienteListaId(int id) {
        MisionPaciente lo = misionPacientelist.findsqlMision(id);
        if (lo.getEstado().equals("a")) {
            return lo;
        }
        return null;
    }

    public void misionPacienteAdd(MisionPaciente a) throws Exception {
        a.setIdMision(misionController.findMision(661));
        misionPacientelist.create(a);
    }

    public void misionPacienteEdit(MisionPaciente a) throws Exception {
        misionPacientelist.editsql(a);

    }

    public void misionPacienteDelete(int a) throws Exception {

        misionPacientelist.destroy(a);
    }

    public List<MisionPaciente> misionPacientelista(Paciente oo) {
        return misionPacientelist.misionPacientelista(oo);

    }

    public List<Mision> filtroMisionCompletad(Paciente oo) {
        return misionPacientelist.filtroMisionCompletada(oo);

    }

    public MisionPaciente MisionMedi(Paciente oo) {
        return misionPacientelist.MisionMedi(oo);

    }

}
