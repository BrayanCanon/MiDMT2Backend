/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.GformsJpaController;
import entidades.Gforms;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author gulmi
 */
public class GformsService {
       GformsJpaController  gformslist;
      Gson gson = new Gson();
    public  GformsService(){
        gformslist=new  GformsJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List  gformslista(){
        return  gformslist.findGformsEntities();
        
    }
       
   public Gforms gformslistaId(int id){
       
          Gforms  lo=gformslist.findGforms(id);
          if(lo.getEstado().equals("a")){
                return lo;
           }
          return null;
    }
   
      public void  gformsAdd(String jsonString) throws Exception{
       Gforms gforms=gson.fromJson(jsonString, Gforms.class);
        
        gformslist.create(gforms);
        
    }
     public void  gformsEdit(Gforms a) throws Exception{
       gformslist.edit(a);
        
    }
      public void gformsEdit(String jsonString)throws Exception{
      
      Gforms gforms;
      gforms = gson.fromJson(jsonString, Gforms.class);
      gformsEdit(gforms);
      
      }
     public void  gformsDelete(String jsonString ) throws Exception{
       JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idGform").getAsInt();
          Gforms x=gformslistaId(result);            
          x.setEstado("b");
          gformsEdit(x);  
        
    }  
}
