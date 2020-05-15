/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.TemaJpaController;
import entidades.Tema;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TemaService {
     TemaJpaController temalist;
     Gson gson = new Gson();
     List <Tema> lista; 
     List salida=new ArrayList();
    public TemaService(){
        temalist=new TemaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

      public List temaLista(){
        lista=temalist.findTemaEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
        
    }
        public Tema temalistaId(int id){
          Tema pa=temalist.findTema(id);
          if(pa.getEstado().equals("a")){
          return pa;
           }
          return null;
    }
      public void temaAdd(String jsonString) throws Exception{
       Tema tema=gson.fromJson(jsonString, Tema.class);
        
        temalist.create(tema);
        
    }
    
     public void temaEdit(Tema a) throws Exception{
      temalist.edit(a);
    }
      public void temaEdit(String jsonString) throws Exception{
         Tema tema=gson.fromJson(jsonString, Tema.class);
         temaEdit(tema);
        
    }
     public void temaDelete(String jsonString) throws Exception{
        JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idPaso").getAsInt();
          Tema x=temalistaId(result);            
          x.setEstado("b");
          temaEdit(x);   
    }
      public Tema busNombreTema(String nom){
   
         return temalist.busNombreTema(nom);
     }

   public int temaAddobj(Tema tema) throws Exception{
       return temalist.create(tema);


    }
}
