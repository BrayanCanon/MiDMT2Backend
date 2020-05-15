/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.ComentarioJpaController;
import entidades.Comentario;
import entidades.Tema;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class ComentarioService {
    ComentarioJpaController comentariolist;
      Gson gson = new Gson();
    
    public ComentarioService(){
        comentariolist=new  ComentarioJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       
    public List comentarioLista(){
        List<Comentario> salida=new ArrayList();
          List <Comentario> lista=comentariolist.findComentarioEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
      public Comentario comentariolistaId(int id){
          Comentario lo=comentariolist.findComentario(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
       public void comentarioAdd(Comentario a) throws Exception{
        comentariolist.create(a);
        
    }
      public void comentarioAdd(String jsonString) throws Exception{
        Comentario comentario=gson.fromJson(jsonString, Comentario.class);
        
        comentariolist.create(comentario);
        
    }
     public void comentarioEdit(Comentario a) throws Exception{
        comentariolist.edit(a);
        
    }
        public void comentarioEdit(String jsonString)throws Exception{
      
      Comentario comentario;
      comentario = gson.fromJson(jsonString, Comentario.class);
      comentarioEdit(comentario);
      
      }
     public void comentarioDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idComentario").getAsInt();
          Comentario x=comentariolistaId(result);            
          x.setEstado("b");
         comentarioEdit(x);   
        
    }
     public Comentario busTema(Tema nom){
  
         return comentariolist.busTema(nom);
     }
      public List<Comentario> busTemaList(Tema nom){
  
         return comentariolist.busTemaList(nom);
     }
}
