/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.TipoIndicadorJpaController;
import entidades.TipoIndicador;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TipoIndicadorService {
   TipoIndicadorJpaController tipoIndicadorList;
     public TipoIndicadorService(){
        tipoIndicadorList=new TipoIndicadorJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
     
    public List tipoIndicadorlista(){
        return tipoIndicadorList.findTipoIndicadorEntities();
        
    }
       
      public void tipoIndicadorAdd(TipoIndicador a) throws Exception{
       tipoIndicadorList.create(a);
        
    }
     public void tipoIndicadorEdit(TipoIndicador a) throws Exception{
       tipoIndicadorList.edit(a);
        
    }
     public void tipoIndicadorDelete(int a) throws Exception{
        tipoIndicadorList.destroy(a);
        
    } 
     public TipoIndicador tipoIndicadorListaId(int id){
          TipoIndicador lo=tipoIndicadorList.findTipoIndicador(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
     public TipoIndicador busTipoIndId(String id){
    
         return  tipoIndicadorList.busTIpoIndicadorId(id);
     }
}
