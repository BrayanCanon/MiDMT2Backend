/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.UsuarioJpaController;
import entidades.Usuario;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class UsuarioService {
     UsuarioJpaController usuarioList;
          Gson gson = new Gson();
     public UsuarioService(){
        usuarioList=new UsuarioJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List usuariolista(){
        return usuarioList.findUsuarioEntities();
        
    }
    
       
      public void usuarioAdd(Usuario a) throws Exception{
       usuarioList.create(a);
        
    }
      public void usuarioEdit(Usuario a) throws Exception{
        usuarioList.edit(a);
        
    }
       public void usuarioEditB2(Usuario a) throws Exception{
           a.setEstado("b");
        usuarioList.edit(a);
        
    }
  public void usuarioEdit(String jsonString)throws Exception{
      
      Usuario usuario;
      usuario = gson.fromJson(jsonString,Usuario.class);
      usuarioEdit(usuario);
      
      }
     public void usuarioDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          String result = jobj.get("codUsuario").getAsString();
         Usuario x=usuariolistaId(result);            
          x.setEstado("b");
          usuarioEdit(x);   
        
    }
    public Usuario usuariolistaId(String id){
        try{
          Usuario lo=usuarioList.findUsuario(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
        }
        catch(Exception e){
            return null;
        }
    }
    public Usuario sqlbuscCod(Usuario a){
        return usuarioList.sqlbuscCod(a);
    }

    public int test(Usuario a){
        return usuarioList.repetidas(a);
    }
  

}
