/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.TipoMisionJpaController;
import entidades.TipoMision;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TipoMisionService {
     TipoMisionJpaController tipoMisionList;
    public TipoMisionService(){
        tipoMisionList=new TipoMisionJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List tipoMisionlista(){
        return tipoMisionList.findTipoMisionEntities();
        
    }
       
      public void tipoMisionAdd(TipoMision a) throws Exception{
       tipoMisionList.create(a);
        
    }
     public void tipoMisionEdit(TipoMision a) throws Exception{
         tipoMisionList.edit(a);
        
    }
     public void tipoMisionDelete(int a) throws Exception{
         tipoMisionList.destroy(a);
        
    }
}
