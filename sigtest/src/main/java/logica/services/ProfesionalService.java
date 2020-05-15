/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.ProfesionalJpaController;
import entidades.Asignacion;
import entidades.Paciente;
import entidades.Profesional;
import entidades.Usuario;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class ProfesionalService {
      ProfesionalJpaController  profesionallist;
    
    public  ProfesionalService(){
       profesionallist=new  ProfesionalJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List profesionallista(){
        return profesionallist.findProfesionalEntities();
        
    }
       
      public void profesionalAdd(Profesional a) throws Exception{
       profesionallist.create(a);
        
    }
     public void profesionalEdit(Profesional a) throws Exception{
        profesionallist.edit(a);
        
    }
     public void profesionalDelete(String a) throws Exception{
       profesionallist.destroy(a);
        
    }
     public Profesional profesionalListaId(String id){
         Profesional lo=profesionallist.findProfesional(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
      public List<Asignacion> busListUsuarios(String a){
       
        return  profesionallist.busListUsuarios(a);
}
}
