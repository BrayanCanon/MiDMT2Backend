/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.AsignacionJpaController;
import entidades.Asignacion;
import entidades.Paciente;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class AsignacionService {
     AsignacionJpaController asignacionlist;
    
    public AsignacionService(){
        asignacionlist=new AsignacionJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List asignacionlista(){
        return asignacionlist.findAsignacionEntities();
        
    }
       
      public void asignacionAdd(Asignacion a) throws Exception{
        asignacionlist.create(a);
        
    }
     public void asignacionEdit(Asignacion a) throws Exception{
        asignacionlist.edit(a);
        
    }
     public void asignacionDelete(int  a) throws Exception{
        
        
    }
       public Asignacion asignacionlistaId(int id){
         Asignacion lo=asignacionlist.findAsignacion(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       public List<Asignacion> asignacionPacientelista(Paciente oo){
          return asignacionlist.asignacionPacientelista(oo);

        
    }
       public Asignacion sqlbusc(String id){
       return asignacionlist.sqlbusc(id);
       }
       
       public Asignacion sqlbuscFamiliar(String id){
       return asignacionlist.sqlbuscFamiliar(id);
       }

}
