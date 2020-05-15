/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;
 
import controladores.MedicamentoJpaController;
import controladores.MedicamentoMisionJpaController;
import entidades.MedicamentoMision;
import entidades.MisionPaciente;
import java.util.List;
import javax.persistence.Persistence;
 
/**
 *
 * @author root
 */
public class MedicamentoMisionService {
   
     MedicamentoMisionJpaController  MedicamentoMisionlist;
     
    public MedicamentoMisionService(){
        MedicamentoMisionlist=new MedicamentoMisionJpaController(Persistence.createEntityManagerFactory("DT2PU"));
       
}
 
       public List MedicamentoMisionlista(){
        return MedicamentoMisionlist.findMedicamentoMisionEntities();
       
    }
       
   public MedicamentoMision MedicamentoMisionalistaId(Integer id){
       
          MedicamentoMision lo=MedicamentoMisionlist.findMedicamentoMision(id);
          if(lo.getEstado().equals("a")){
                return lo;
           }
          return null;
    }
   
   public void medicamentoMisionDelete(int hh) throws Exception{
   
          MedicamentoMision x=MedicamentoMisionalistaId(hh);            
          x.setEstado("b");
          MedicamentoMisionEdit(x);   
        
    }
    
   
   
      public void MedicamentoMisionAdd(MedicamentoMision a) throws Exception{
       MedicamentoMisionlist.create(a);
       
    }
     public void MedicamentoMisionEdit(MedicamentoMision a) throws Exception{
       MedicamentoMisionlist.edit(a);
       
    }
     public void MedicamentoMisionDelete(Integer a) throws Exception{
       MedicamentoMisionlist.destroy(a);
       
    }  
     public MedicamentoMision busMedicamentoMision(MisionPaciente a) throws Exception{
       return MedicamentoMisionlist.busMedicamentoMision(a);
       
    }  
   
}