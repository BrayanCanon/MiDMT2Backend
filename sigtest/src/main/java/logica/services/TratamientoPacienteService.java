/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.TratamientoPacienteJpaController;
import entidades.TratamientoPaciente;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TratamientoPacienteService {
       TratamientoPacienteJpaController  tratamientoPacientelist;
    
    public  TratamientoPacienteService(){
       tratamientoPacientelist=new  TratamientoPacienteJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List tratamientoPacientelista(){
        return tratamientoPacientelist.findTratamientoPacienteEntities();
        
    }
       
      public void tratamientoPacienteAdd(TratamientoPaciente a) throws Exception{
       tratamientoPacientelist.create(a);
        
    }
     public void tratamientoPacienteEdit(TratamientoPaciente a) throws Exception{
       tratamientoPacientelist.edit(a);
        
    }
     public void tratamientoPacienteDelete(int a) throws Exception{
      tratamientoPacientelist.destroy(a);
        
    }
}
