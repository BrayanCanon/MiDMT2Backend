/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.MisionTipoRecursoJpaController;
import entidades.Mision;
import entidades.MisionTipoRecurso;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class MisionTipoRecursoService {
     MisionTipoRecursoJpaController misionTipoRecursolist;
    
    public MisionTipoRecursoService(){
        misionTipoRecursolist=new MisionTipoRecursoJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List misionTipoRecursolista(){
        return misionTipoRecursolist.findMisionTipoRecursoEntities();
        
    }
       public MisionTipoRecurso mtiporecursolistaId(int id){
          MisionTipoRecurso lo= misionTipoRecursolist.findMisionTipoRecurso(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
      public void misionTipoRecursoAdd(MisionTipoRecurso a) throws Exception{
        misionTipoRecursolist.create(a);
        
    }
     public void misionTipoRecursoEdit(MisionTipoRecurso a) throws Exception{
        misionTipoRecursolist.edit(a);
        
    }
     public void misionTipoRecursoDelete(int a) throws Exception{
       misionTipoRecursolist.destroy(a);
        
    }
     public List<MisionTipoRecurso> sqlbuscRecursoMision(Mision a) throws Exception{
      return  misionTipoRecursolist.sqlbuscRecursoMision(a);
        
    }
      public List<MisionTipoRecurso> sqlbuscForoMision(Mision a) throws Exception{
      return  misionTipoRecursolist.sqlbuscForoMision(a);
        
    }
       public MisionTipoRecurso sqlbuscPorRecurso(int a) throws Exception{
      return  misionTipoRecursolist.sqlbuscPorRecurso(a);
        
    }
       public MisionTipoRecurso sqlbuscPorRecursoYMision(int a,Mision b) throws Exception{
      return  misionTipoRecursolist.sqlbuscPorRecursoYMision(a,b);
        
    }
}
