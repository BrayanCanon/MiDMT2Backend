/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.PerfilJpaController;
import entidades.Perfil;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class PerfilService {
    PerfilJpaController perfilList;
     public PerfilService(){
        perfilList=new PerfilJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List perfillista(){
        return perfilList.findPerfilEntities();
        
    }
       
      public void perfilAdd(Perfil a) throws Exception{
       perfilList.create(a);
        
    }
     public void perfilEdit(Perfil a) throws Exception{
        perfilList.edit(a);
        
    }
     public void perfilDelete(int a) throws Exception{
        perfilList.destroy(a);
        
    }
      public Perfil perfilistaId(int id){
          Perfil lo=perfilList.findPerfil(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
}
