/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.MedicamentoJpaController;
import entidades.Categoria;
import entidades.Medicamento;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class MedicamentoService {
    MedicamentoJpaController medicamentolist;
    
    public MedicamentoService(){
        medicamentolist=new MedicamentoJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List medicamentolista(){
        return medicamentolist.findMedicamentoEntities();
        
    }
       
   public Medicamento medicamentoalistaId(String id){
       
          Medicamento lo=medicamentolist.findMedicamento(id);
          if(lo.getEstado().equals("a")){
                return lo;
           }
          return null;
    }
   
      public void medicamentoAdd(Medicamento a) throws Exception{
       medicamentolist.create(a);
        
    }
     public void medicamentoEdit(Medicamento a) throws Exception{
       medicamentolist.edit(a);
        
    }
     public void medicamentoDelete(String a) throws Exception{
       medicamentolist.destroy(a);
        
    }  
     
      
     
     
     public Medicamento busMedicamento(Medicamento a) throws Exception{
     return  medicamentolist.busMedicamento(a);
        
    }  
}
