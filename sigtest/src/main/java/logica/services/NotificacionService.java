/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.NotificacionJpaController;
import entidades.Notificacion;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class NotificacionService {
     NotificacionJpaController notificacionlist;
       Gson gson = new Gson();
    public NotificacionService(){
        notificacionlist=new NotificacionJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

   public List notificacionLista(){
        List<Notificacion> salida=new ArrayList();
          List <Notificacion> lista=notificacionlist.findNotificacionEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
      public Notificacion notificacionlistaId(int id){
          Notificacion lo=notificacionlist.findNotificacion(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
      public void notificacionAdd(String jsonString) throws Exception{
        Notificacion categoria=gson.fromJson(jsonString, Notificacion.class);
        
        notificacionlist.create(categoria);
        
    }
       public void notificacionAdd2(Notificacion nt) throws Exception{
       
        
        notificacionlist.create(nt);
        
    }
     public void notificacionEdit(Notificacion a) throws Exception{
        notificacionlist.edit(a);
        
    }
        public void notificacionEdit(String jsonString)throws Exception{
      
      Notificacion notificacion;
      notificacion= gson.fromJson(jsonString, Notificacion.class);
      notificacionEdit(notificacion);
      
      }
     public void notificacionDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idnotificacion").getAsInt();
          Notificacion x=notificacionlistaId(result);            
          x.setEstado("b");
          notificacionEdit(x);   
        
    }
       public List<Notificacion> busNotificacionUusuario(Usuario nom){
           
         return notificacionlist.busNotificacionUusuario(nom);
     }
     
     
}
