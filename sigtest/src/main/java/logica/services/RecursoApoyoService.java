/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.RecursoApoyoJpaController;
import entidades.RecursoApoyo;
import entidades.Usuario;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class RecursoApoyoService {
       RecursoApoyoJpaController  recursoApoyolist;
    Gson gson = new Gson();
    public  RecursoApoyoService(){
        recursoApoyolist=new  RecursoApoyoJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}


       public List<RecursoApoyo> recursoApoyolista(){

          List<RecursoApoyo> recursosApoyo=recursoApoyolist.findRecursoApoyoEntities();
           
           for(int i = 0;i>=recursosApoyo.size();i++){
               RecursoApoyo rec;
               rec=recursosApoyo.get(i);
               rec.setNomUsuario(null);
        
              recursosApoyo.set(i, rec);
           }
           
        return recursosApoyo;
        
    }
       
       public void recursoApoyoAdd2(RecursoApoyo a) throws Exception{
      recursoApoyolist.create(a);
        
    }
 
           public  RecursoApoyo  recursoApoyolistaId(int id){
           RecursoApoyo lo= recursoApoyolist.findRecursoApoyo(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
      public void  recursoApoyoAdd(String jsonString) throws Exception{
         RecursoApoyo categoria=gson.fromJson(jsonString,  RecursoApoyo.class);
        
         recursoApoyolist.create(categoria);
        
    }
     public void  recursoApoyoEdit( RecursoApoyo a) throws Exception{
         recursoApoyolist.edit(a);
        
    }
        public void recursoApoyoaEdit(String jsonString)throws Exception{
      
      RecursoApoyo  recursoApoyo;
       recursoApoyo = gson.fromJson(jsonString,  RecursoApoyo.class);
       recursoApoyoEdit( recursoApoyo);
      
      }
     public void  recursoApoyoDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idRecapoyo").getAsInt();
          RecursoApoyo x=recursoApoyolistaId(result);            
          x.setEstado("b");
          recursoApoyoEdit(x);   
        
    }
        public void  recursoApoyoDelete( RecursoApoyo a) throws Exception{
         a.setEstado("b");
          recursoApoyolist.edit(a); 
    }
        public int busRecursoId(String nom){
   
         return recursoApoyolist.busRecursoId(nom);
     }
         public RecursoApoyo busImagen(String nom){
   
         return recursoApoyolist.busImagen(nom);
     }
}
