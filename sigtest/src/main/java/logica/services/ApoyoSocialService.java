/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.ApoyoSocialJpaController;
import entidades.ApoyoSocial;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class ApoyoSocialService {
     ApoyoSocialJpaController  apoyoSociallList;
    public ApoyoSocialService(){
        apoyoSociallList=new ApoyoSocialJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }
    public List apoyoSociallista(){
        return apoyoSociallList.findApoyoSocialEntities();
        
    }
       
      public void apoyoSocialAdd(ApoyoSocial a) throws Exception{
       apoyoSociallList.create(a);
        
    }
     public void apoyoSocialEdit(ApoyoSocial a) throws Exception{
        apoyoSociallList.edit(a);
        
    }
     public void apoyoSocialDelete(String a) throws Exception{
        apoyoSociallList.destroy(a);
        
    }
      public ApoyoSocial apoyoSocialListaId(String id){
          ApoyoSocial lo=apoyoSociallList.findApoyoSocial(id);
          if(lo.getEstado().equals("a")){
          return lo;
           }
          return null;
    }
       public ApoyoSocial apoyoSocialListaIdC(String id){
          ApoyoSocial lo=apoyoSociallList.findApoyoSocial(id);
          if(lo.getEstado().equals("c")){
          return lo;
           }
          return null;
    }
}
