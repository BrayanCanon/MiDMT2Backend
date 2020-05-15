/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.NivelJpaController;
import entidades.DTO.NivelDTO;
import entidades.Nivel;
import entidades.NivelPaciente;
import entidades.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class NivelService {
     NivelJpaController nivelList;
    public NivelService(){
        nivelList=new NivelJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List nivelista(){
        return nivelList.findNivelEntities();
        
    }
 
       
      public void nivelAdd(Nivel a) throws Exception{
       nivelList.create(a);
        
    }
     public void nivelEdit(Nivel a) throws Exception{
         nivelList.edit(a);
        
    }
     public void nivelDelete(int a) throws Exception{
         nivelList.destroy(a);
        
    }
     public Nivel sqlbuscNivel(NivelPaciente a) throws Exception{
        return nivelList.sqlbuscNivel(a);
        
    }
     
    public List<NivelDTO> consultarListadoMisionesPorPaciente(String codPaciente){
         ArrayList<Nivel> ab = new ArrayList<>(nivelList.consultarListadoMisionesPorPaciente(new Paciente(codPaciente)));
        List<NivelDTO> listadoMisionDTO = new ArrayList<>();
        for (Nivel nivel : ab) {
            listadoMisionDTO.add(new NivelDTO(nivel));
        }
        return listadoMisionDTO;
    }
}
