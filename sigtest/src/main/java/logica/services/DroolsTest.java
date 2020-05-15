/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;
import javax.inject.Inject;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
 

 
public class DroolsTest {
 
  
  @Inject
 @KSession()
 private KieSession kSession;
 
 public void bootstrapDrools() {
   // The KieSession was injected so we can use it now
   kSession.insert("Hi There!");
   int rulesFired = kSession.fireAllRules();
   System.out.println("Rules Fired: "+rulesFired);
 }
 
 public static void main(String[] args) {
   //Boostrap the CDI container, in this case WELD
   Weld w = new Weld();
 
   WeldContainer wc = w.initialize();
  DroolsTest app = wc.instance().select(DroolsTest.class).get();
   app.bootstrapDrools();
 
   w.shutdown();
 }
    }


