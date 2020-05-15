/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.TipoRecursoJpaController;
import entidades.TipoRecurso;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TipoRecursoService {
     TipoRecursoJpaController  tipoRecursolist;
    
    public  TipoRecursoService(){
       tipoRecursolist=new  TipoRecursoJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List tipoRecursolista(){
        return tipoRecursolist.findTipoRecursoEntities();
        
    }
       
      public void tipoRecursoAdd(TipoRecurso a) throws Exception{
       tipoRecursolist.create(a);
        
    }
     public void tipoRecursoEdit(TipoRecurso a) throws Exception{
       tipoRecursolist.edit(a);
        
    }
     public void tipoRecursoDelete(int a) throws Exception{
      tipoRecursolist.destroy(a);
        
    }
    public List<TipoRecurso> sqlbuscRecurso (TipoRecurso tyy){
    return  tipoRecursolist.sqlbuscRecurso(tyy);
    }
}
