/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.TipoPreguntaJpaController;
import entidades.TipoPregunta;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TipoPreguntaService {
      TipoPreguntaJpaController  tipoPreguntalist;
    
    public  TipoPreguntaService(){
       tipoPreguntalist=new  TipoPreguntaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List tipoPreguntalista(){
        return tipoPreguntalist.findTipoPreguntaEntities();
        
    }
       
      public void tipoPreguntaAdd(TipoPregunta a) throws Exception{
       tipoPreguntalist.create(a);
        
    }
     public void tipoPreguntaEdit(TipoPregunta a) throws Exception{
       tipoPreguntalist.edit(a);
        
    }
     public void tipoPreguntaDelete(int a) throws Exception{
      tipoPreguntalist.destroy(a);
        
    }
       public TipoPregunta tipopreguntalistaId(int id){
          TipoPregunta lo=tipoPreguntalist.findTipoPregunta(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       public TipoPregunta busTipoPreguntaId(String id){
    
         return  tipoPreguntalist.busTipoPreguntaId(id);
     }
}
