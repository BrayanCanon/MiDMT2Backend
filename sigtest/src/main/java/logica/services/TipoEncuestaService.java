/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.LogroJpaController;
import controladores.TipoEncuestaJpaController;
import entidades.Logro;
import entidades.TipoEncuesta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class TipoEncuestaService {
    TipoEncuestaJpaController  tiponcuestaList;
    Gson gson = new Gson();
   
    
    
    public TipoEncuestaService (){
    
   tiponcuestaList=new TipoEncuestaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
      public List tipoencuestalista(){
        List<TipoEncuesta> salida=new ArrayList();
          List <TipoEncuesta> lista=tiponcuestaList.findTipoEncuestaEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
        public TipoEncuesta tipoencuestalistaId(int id){
          TipoEncuesta lo=tiponcuestaList.findTipoEncuesta(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
      public void tipoencuestaAdd(String jsonString) throws Exception{
      
        TipoEncuesta  tipoencuesta =gson.fromJson(jsonString, TipoEncuesta.class);
       tiponcuestaList.create(tipoencuesta);
    }
      public void tipoencuestEdit(String jsonString)throws Exception{
      
       TipoEncuesta tipoencuesta=gson.fromJson(jsonString, TipoEncuesta.class);
      tipoencuestaEdit(tipoencuesta);
      
      }
      
     public void tipoencuestaEdit( TipoEncuesta a) throws Exception{
        tiponcuestaList.edit(a);
        
    }
     public void tipoencuestaDelete(String jsonString) throws Exception{
      
          JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idTipoEncuesta").getAsInt();
          TipoEncuesta x=tipoencuestalistaId(result);            
          x.setEstado("b");
          tipoencuestaEdit(x);   
    }
     public TipoEncuesta busTipoEncuestaId(String id){
    
         return  tiponcuestaList.busTipoEncuestaId(id);
     }
}
