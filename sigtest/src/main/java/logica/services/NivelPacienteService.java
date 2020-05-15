/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.NivelPacienteJpaController;
import entidades.NivelPaciente;
import entidades.Paciente;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class NivelPacienteService {
     NivelPacienteJpaController nivelPacientelist;
    
    public NivelPacienteService(){
        nivelPacientelist=new NivelPacienteJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List nivelPacientelista(){
        return nivelPacientelist.findNivelPacienteEntities();
        
    }
       
      public void nivelPacienteAdd(NivelPaciente a) throws Exception{
        nivelPacientelist.create(a);
        
    }
     public void nivelPacienteEdit(NivelPaciente a) throws Exception{
        nivelPacientelist.edit(a);
        
    }
     public void nivelPacienteDelete(int a) throws Exception{
       nivelPacientelist.destroy(a);
        
    }
     public NivelPaciente sqlbuscNombreNivel(String a){
        return  nivelPacientelist.sqlbuscNombreNivel(a);
    }
     public List<NivelPaciente> filtroNivelEjercicio(Paciente a){
        return  nivelPacientelist.filtroNivelEjercicio(a);
    }
      public List<NivelPaciente> filtroNivelAlimentacion(Paciente a){
        return  nivelPacientelist.filtroNivelAlimentacion(a);
    }
}
