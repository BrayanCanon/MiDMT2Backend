/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import controladores.MisionJpaController;
import entidades.Mision;
import entidades.Nivel;
import entidades.NivelPaciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class MisionService {
        Gson gson = new Gson();

     MisionJpaController  misionlList;
    public MisionService(){
        misionlList=new MisionJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List misionLista(){
        List<Mision> salida=new ArrayList();
          List <Mision> lista= misionlList.findMisionEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                    salida.add(lista.get(ab));
                }
            }
        return salida;
    }

      public Mision  misionlistaId(int id){
          Mision lo=misionlList.findMision(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
   public int misionAdd2(String jsonString) throws Exception{
   
        Mision mision=gson.fromJson(jsonString, Mision.class);

        return  misionlList.create(mision);

    }
  
  public void misionAdd(String jsonString) throws Exception{
        Mision mision=gson.fromJson(jsonString, Mision.class);
       
        misionlList.create(mision);
       
    }
     public void misionEdit(Mision a) throws Exception{
       misionlList.edit(a);
       
    }
        public void misionEdit(String jsonString)throws Exception{
     Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
      Mision mision;
      mision = gson.fromJson(jsonString, Mision.class);
      misionEdit(mision);
     
      }
     public void misionDelete(String jsonString) throws Exception{
         JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
          int result = jobj.get("idMision").getAsInt();
          Mision x=misionlistaId(result);            
          x.setEstado("b");
          misionEdit(x);  
       
    }
     
     
     public Mision misionget(String jsonString){
    JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
    int result = jobj.get("idMision").getAsInt();
    return misionlistaId(result);
    }
     
     public Mision sqlbuscNivel(NivelPaciente uu){
     return misionlList.sqlbuscMisionNivel(uu);
     }
     public List<Mision> sqlbuscNombreAlimentacion(Nivel uu){
     return misionlList.sqlbuscNombreAlimentacion(uu);
     }
      public List<Mision> sqlbuscNombreEjercicio(Nivel uu){
     return misionlList.sqlbuscNombreEjercicio(uu);
     }
}
