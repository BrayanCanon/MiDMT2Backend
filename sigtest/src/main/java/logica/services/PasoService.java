/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.PasoJpaController;


import entidades.Paso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class PasoService {
    PasoJpaController  pasoList;
    Gson gson = new Gson();
    List <Paso> lista; 
    List salida=new ArrayList();
    public PasoService(){
        pasoList=new PasoJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List pasolista(){
        lista=pasoList.findPasoEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
        
    }
        public Paso pasolistaId(int id){
          Paso pa=pasoList.findPaso(id);
          if(pa.getEstado().equals("a")){
          return pa;
           }
          return null;
    }
      public int pasoAdd(String jsonString) throws Exception{
       Paso paso=gson.fromJson(jsonString, Paso.class);
        
        pasoList.create(paso);
         return pasoList.cod;
    }
     public void pasoEdit(String jsonString) throws Exception{
         Paso paso=gson.fromJson(jsonString, Paso.class);
         pasoEdit(paso);
        
    }
     public void pasoEdit(Paso a) throws Exception{
      pasoList.edit(a);
    }
     public void pasoDelete(String jsonString) throws Exception{
        JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idPaso").getAsInt();
          Paso x=pasolistaId(result);            
          x.setEstado("b");
          pasoEdit(x);   
    }
     public List<Paso> consultapasocat(Integer cat){
         
         
         return pasoList.consultapasocat(cat);
     }
     public void pasobjAdd(Paso paso) throws Exception{
           try{
           pasoList.create(paso);
           }
           catch(Exception e){}
       }
}
