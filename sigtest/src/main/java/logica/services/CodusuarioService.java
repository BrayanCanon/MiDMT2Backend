/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import controladores.CodusuarioJpaController;
import entidades.Codusuario;
import javax.persistence.Persistence;

/**
 *
 * @author gulmi
 */
public class CodusuarioService {
        CodusuarioJpaController codusuariolist;
     Gson gson = new Gson();

    public CodusuarioService (){
        codusuariolist=new CodusuarioJpaController(Persistence.createEntityManagerFactory("DT2PU"));

} 
    public Codusuario busNombre(Codusuario cu){

    return codusuariolist.sqlbuscCod(cu);
    }
     public void codusuarioEdit(Codusuario a) throws Exception{
        codusuariolist.edit(a);

    }
       public Codusuario codusuariolistaId(int id){
          Codusuario lo=codusuariolist.findCodusuario(id);
          
          return lo;
   
    }
    
}
