/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

import common.EasyPropertiesReader;


/**
 *
 * @author TEO
 */
public class OneThreadPerSocket {
    
    CommunicationInterface ci;
   

    public OneThreadPerSocket(CommunicationInterface ci) {
        this.ci = ci;
    }
    
    public void acceptConnections(){
        
        ci.openConnection();
        
        while(true){
            
            CommunicationInterface localCi = instanceFactory();
            
            if(localCi.connectionAccepted()){
               WorkerThread wt = new WorkerThread(localCi);
               wt.start();
                
            }
            
        }
        
    }
    
    
    CommunicationInterface instanceFactory(){
        
         EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");
         String requestType = epr.readProperty("Settings", "RequestType");
         
         if(requestType.equals("tcp"))
             return new BlockingTcpConnection();
         else //TODO alte cazuri
             return new BlockingTcpConnection();
    }
    
}
