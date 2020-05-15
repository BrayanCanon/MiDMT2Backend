/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.PruebaDiagnosticaJpaController;
import entidades.Indicador;
import entidades.Paciente;
import entidades.PruebaDiagnostica;
import entidades.TipoIndicador;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class PruebaDiagnosticaService {
     PruebaDiagnosticaJpaController pruebaDiagnosticalist;
     Gson gson = new Gson();
     public PruebaDiagnosticaService(){
        pruebaDiagnosticalist=new PruebaDiagnosticaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
     public List pruebaDiagnosticaLista(){
        List<PruebaDiagnostica> salida=new ArrayList();
          List <PruebaDiagnostica> lista=pruebaDiagnosticalist.findPruebaDiagnosticaEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
      public PruebaDiagnostica pruebaDiagnosticalistaId(int id){
          PruebaDiagnostica lo=pruebaDiagnosticalist.findPruebaDiagnostica(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       
       public void pruebaDiagnosticaAdd(PruebaDiagnostica a) throws Exception{
       pruebaDiagnosticalist.create(a);
        
    }
      
      public void pruebaDiagnosticaAdd(String jsonString) throws Exception{
        PruebaDiagnostica pruebaDiagnostica=gson.fromJson(jsonString, PruebaDiagnostica.class);
        
        pruebaDiagnosticalist.create(pruebaDiagnostica);
        
    }
     public void pruebaDiagnosticaEdit(PruebaDiagnostica a) throws Exception{
        pruebaDiagnosticalist.edit(a);
        
    }
        public void pruebaDiagnosticaEdit(String jsonString)throws Exception{
      
   
      PruebaDiagnostica pruebaDiagnostica = gson.fromJson(jsonString, PruebaDiagnostica.class);
      pruebaDiagnosticaEdit(pruebaDiagnostica);
      
      }
     public void pruebaDiagnosticaDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idPrueba").getAsInt();
          PruebaDiagnostica x= pruebaDiagnosticalistaId(result);            
          x.setEstado("b");
          pruebaDiagnosticaEdit(x);   
        
    }
      public List<PruebaDiagnostica> busUsuarioId(Paciente id){
    
         return pruebaDiagnosticalist.busUsuarioId(id);
     }
      
        public List<String> buscarTipoPrueba(Paciente id,String nomIndi,String nomPrueba){
            List<String> mostrar = new ArrayList();
            IndicadorService in= new IndicadorService();
            TipoIndicadorService tis= new TipoIndicadorService();
            
            List<PruebaDiagnostica> rtt= busUsuarioId2(id,nomPrueba);
            
            PruebaDiagnostica yuu= rtt.get(rtt.size()-1);
            mostrar.add(String.valueOf(yuu.getFechaPrueba()));
            
            TipoIndicador iii= tis.busTipoIndId(nomIndi);
            
           
               
            List<Indicador> rtt2 = in.busIndicadorId(rtt.get(rtt.size()-1),iii);
            
        
         
            Indicador yuu2= rtt2.get(rtt2.size()-1);
            
            mostrar.add(String.valueOf(yuu2.getValorIndicador()));
        
         return mostrar;
     }
        
        public List<PruebaDiagnostica> busUsuarioId2(Paciente id,String id2){
    
         return pruebaDiagnosticalist.busUsuarioId2(id,id2);
     }
    
     
}
