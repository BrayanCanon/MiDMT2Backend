/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import controladores.MisionPasoLogroJpaController;
import entidades.Logro;
import entidades.Mision;
import entidades.MisionPasoLogro;
import entidades.Paso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class MisionPasoLogroService {
     MisionPasoLogroJpaController misionPasoLogrolist;
    
    public MisionPasoLogroService(){
        misionPasoLogrolist=new MisionPasoLogroJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        
}
 public List mpasologroLista(){
        List<MisionPasoLogro> salida=new ArrayList();
          List <MisionPasoLogro> lista= misionPasoLogrolist.findMisionPasoLogroEntities();
            for(int ab=0;ab<lista.size();ab++){
                if(lista.get(ab).getEstado().equals("a")){
                salida.add(lista.get(ab));
                }
            }
        return salida;
    } 
      public void misionPasoLogroAdd(MisionPasoLogro a) throws Exception{
        misionPasoLogrolist.create(a);
        
    }
     public void misionPasoLogroEdit(MisionPasoLogro a) throws Exception{
        misionPasoLogrolist.edit(a);
        
    }
     public void misionPasoLogroDelete(int a) throws Exception{
         misionPasoLogrolist.destroy(a);

    }
     public int mplbusc(Mision mis,Paso paso)
      {
        Logro a= misionPasoLogrolist.mplbusc(mis, paso);
        return a.getIdLogro();
      }
     public List<Paso> mplbuscListPaso(Mision mis)
      {
        return misionPasoLogrolist.mplbuscList(mis);
       
      }
     public List<MisionPasoLogro> mplbuscMis(Mision mis)
      {
        return misionPasoLogrolist.mplbuscMis(mis);
       
      }
      public MisionPasoLogro mplbuscListPaso(Mision mis,Paso paso)
      {
        return misionPasoLogrolist.pasoNumeroList(mis,paso);
       
      }
     public String consultarPasoIdm(String jsonString){
           //misionPasoLogrolist
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject entrada=gson.fromJson(jsonString, JsonObject.class);
        Mision busc=new Mision();
        busc.setIdMision(entrada.get("idMision").getAsInt());
        return gson.toJson(this.pasoidm(busc));
         
       }
 
public Paso pasoidm(Mision m){
         
          return misionPasoLogrolist.pasoidm(m);
         
      }
public MisionPasoLogro pasoVeri(Mision m, int a){
         
          return misionPasoLogrolist.pasoVeri(m,a);
         
      }
}

