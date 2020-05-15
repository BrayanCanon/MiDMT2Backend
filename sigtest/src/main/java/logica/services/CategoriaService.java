/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;
import controladores.CategoriaJpaController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import entidades.Categoria;
import entidades.Logro;
import java.util.ArrayList;


import java.util.List;
import javax.persistence.Persistence;
/**
 *
 * @author znico
 */
public class CategoriaService {
    CategoriaJpaController categoriaslist;
     Gson gson = new Gson();
    
    public CategoriaService(){
        categoriaslist=new CategoriaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

      public List categroiaLista(){
        List<Categoria> salida=new ArrayList();
          List <Categoria> lista=categoriaslist.findCategoriaEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
      public Categoria categorialistaId(int id){
          Categoria lo=categoriaslist.findCategoria(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
      public void categoriaAdd(String jsonString) throws Exception{
        Categoria categoria=gson.fromJson(jsonString, Categoria.class);
        
        categoriaslist.create(categoria);
        
    }
     public void categoriaEdit(Categoria a) throws Exception{
        categoriaslist.edit(a);
        
    }
        public void categoriaEdit(String jsonString)throws Exception{
      
      Categoria categoria;
      categoria = gson.fromJson(jsonString, Categoria.class);
      categoriaEdit(categoria);
      
      }
     public void categoriaDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idCategoria").getAsInt();
          Categoria x=categorialistaId(result);            
          x.setEstado("b");
          categoriaEdit(x);   
        
    }
     
     public int busCategoriaNom(String nom){
         JsonObject jobj = gson.fromJson(nom, JsonObject.class);
         String result = jobj.get("nomCategoria").getAsString();   
         return categoriaslist.busCategoriaNom(result);
     }
     
     
}
