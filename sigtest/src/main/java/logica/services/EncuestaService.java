/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.EncuestaJpaController;
import entidades.Encuesta;
import entidades.Paciente;
import entidades.Pregunta;
import entidades.TipoEncuesta;
import entidades.TipoPregunta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class EncuestaService {
  EncuestaJpaController encuestalist;
    
    public EncuestaService(){
        encuestalist=new EncuestaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List encuestalista(){
        return encuestalist.findEncuestaEntities();
        
    }
       
      public void encuestaAdd(Encuesta a) throws Exception{
       encuestalist.create(a);
        
    }
     public void encuestaEdit(Encuesta a) throws Exception{
        encuestalist.edit(a);
        
    }
     public void encuestaDelete(int a) throws Exception{
        encuestalist.destroy(a);
        
    }  
     
      public List<Encuesta> busEncuestaPorTipoPac(Paciente id, TipoEncuesta id2){
    
         return encuestalist.busEncuestaPa(id,id2);
     }
      
       public List<Encuesta> busEncuestaSoloPa(Paciente id){
    
         return encuestalist.busEncuestaSoloPa(id);
     }
      
      public List<String> busEncuestaPa2(Paciente id,String tipoEncuesta){
            List<String> mostrar = new ArrayList();
            PreguntaService in= new PreguntaService();
      
            TipoEncuestaService tis= new TipoEncuestaService();
      
            TipoEncuesta uuu=tis.busTipoEncuestaId(tipoEncuesta);
            List<Encuesta> rtt= busEncuestaPorTipoPac(id,uuu);
            Encuesta yuu= rtt.get(rtt.size()-1);
            mostrar.add(String.valueOf(yuu.getFecha()));
            List<Pregunta> rtt2 = in.busPreguntaId(rtt.get(rtt.size()-1));
            Pregunta yuu2= rtt2.get(rtt2.size()-1);
            mostrar.add(String.valueOf(yuu2.getRespuesta()));
         return mostrar;
     }
       public List<String> busEncuestaPa3(Paciente id,String tipoEncuesta){
            List<String> mostrar = new ArrayList();
            PreguntaService in= new PreguntaService();
      
            TipoEncuestaService tis= new TipoEncuestaService();
          
            TipoEncuesta uuu=tis.busTipoEncuestaId(tipoEncuesta);
            List<Encuesta> rtt= busEncuestaPorTipoPac(id,uuu);
           for (int j = 0; j < rtt.size(); j++) {
               
           
            Encuesta yuu= rtt.get(j);
            mostrar.add(String.valueOf(yuu.getFecha()));
             List<Pregunta> rtt2 = in.busPreguntaId(rtt.get(j));
            for (int i = 0; i < rtt2.size(); i++) {
             
            Pregunta yuu2= rtt2.get(i);
            mostrar.add(String.valueOf(yuu2.getRespuesta()));
           }
           }
            
         return mostrar;
     }
}
