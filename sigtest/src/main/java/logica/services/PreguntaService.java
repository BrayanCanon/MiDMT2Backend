/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.PreguntaJpaController;
import entidades.Encuesta;
import entidades.Pregunta;

import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class PreguntaService {
      PreguntaJpaController preguntalist;
    
    public PreguntaService(){
        preguntalist=new PreguntaJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}

       public List preguntalista(){
        return preguntalist.findPreguntaEntities();
        
    }
       
      public void preguntaAdd(Pregunta a) throws Exception{
        preguntalist.create(a);
        
    }
     public void preguntaEdit(Pregunta a) throws Exception{
        preguntalist.edit(a);
        
    }
     public void preguntaDelete(int a) throws Exception{
        preguntalist.destroy(a);
        
    }
     public List<Pregunta> busPreguntaId(Encuesta id){
    
         return preguntalist.busPreguntaId(id);
     }
}
