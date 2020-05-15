/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.PacienteJpaController;

import entidades.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class PacienteService {
      PacienteJpaController pacientelist;
    Gson gson = new Gson();
    public PacienteService(){
        pacientelist=new PacienteJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}
    
    
    public void PacienteAdd2(Paciente a) throws Exception{
        pacientelist.create(a);
        
    }
   
    public List PacienteLista(){
        List<Paciente> salida=new ArrayList();
          List <Paciente> lista=pacientelist.findPacienteEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    }
    
       
      public void PacienteAdd(String jsonString) throws Exception{
        Paciente paciente=gson.fromJson(jsonString, Paciente.class);
        
       pacientelist.create(paciente);
        
    }
     public void PacienteEdit(Paciente a) throws Exception{
        pacientelist.edit(a);
        
    }
        public void PacienteEdit(String jsonString)throws Exception{
      
      
      Paciente paciente = gson.fromJson(jsonString, Paciente.class);
      PacienteEdit(paciente);
      
      }
     public void PacienteDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          String result = jobj.get("idPaciente").getAsString();
          Paciente x=pacienteListaId(result);            
          x.setEstado("b");
          PacienteEdit(x);   
        
    }
     
         
      public Paciente pacienteListaId(String id){
          Paciente lo=pacientelist.findPaciente(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       public Paciente pacienteListaC(String id){
          Paciente lo=pacientelist.findPaciente(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       public Paciente busPaciente(Paciente nom){
   
         return pacientelist.busPaciente(nom);
     }
}
