/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.LogroJpaController;
import entidades.Logro;
import java.util.ArrayList;

import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class LogroService {
    LogroJpaController logrolist;
    Gson gson = new Gson();
   
    
    
    public LogroService (){
    
    logrolist=new LogroJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
      public List logrolista(){
        List<Logro> salida=new ArrayList();
          List <Logro> lista=logrolist.findLogroEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
        public Logro logrolistaId(int id){
          Logro lo=logrolist.findLogro(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
      public void logroAdd(String jsonString) throws Exception{
      
        Logro logro=gson.fromJson(jsonString, Logro.class);
        logrolist.create(logro);
    }
      public void logroEdit(String jsonString)throws Exception{
      
      Logro logro=gson.fromJson(jsonString, Logro.class);
      logroEdit(logro);
      
      }
      
     public void logroEdit(Logro a) throws Exception{
        logrolist.edit(a);
        
    }
     public void logroDelete(String jsonString) throws Exception{
      
          JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idLogro").getAsInt();
          Logro x=logrolistaId(result);            
          x.setEstado("b");
          logroEdit(x);   
    }
}
