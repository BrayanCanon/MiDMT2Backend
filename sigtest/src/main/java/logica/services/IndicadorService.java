/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.IndicadorJpaController;
import entidades.Indicador;
import entidades.PruebaDiagnostica;
import entidades.TipoIndicador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class IndicadorService {
    IndicadorJpaController indicadorlist;
      Gson gson = new Gson();
    public IndicadorService(){
        indicadorlist=new IndicadorJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}
 public List indicadorLista(){
        List<Indicador> salida=new ArrayList();
          List <Indicador> lista=indicadorlist.findIndicadorEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
  public void indicadorAdd(Indicador a) throws Exception{
       indicadorlist.create(a);
        
    }
 
 
      public Indicador indicadorlistaId(int id){
          Indicador lo=indicadorlist.findIndicador(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
      public void indicadorAdd(String jsonString) throws Exception{
        Indicador indicador=gson.fromJson(jsonString, Indicador.class);
        
        indicadorlist.create(indicador);
        
    }
     public void indicadorEdit(Indicador a) throws Exception{
        indicadorlist.edit(a);
        
    }
        public void indicadorEdit(String jsonString)throws Exception{
      Indicador indicador = gson.fromJson(jsonString, Indicador.class);
      indicadorEdit(indicador);
      
      }
     public void indicadorDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idIndicador").getAsInt();
          Indicador x=indicadorlistaId(result);            
          x.setEstado("b");
          indicadorEdit(x);   
        
    }
  public List<Indicador> busIndicadorId(PruebaDiagnostica id, TipoIndicador id2){
    
         return indicadorlist.busIndicadorId(id,id2);
     }
}
