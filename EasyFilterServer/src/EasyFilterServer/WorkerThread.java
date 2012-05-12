/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

import common.EasyImageWriter;
import common.Package;

/**
 *
 * @author TEO
 */
class WorkerThread extends Thread{

    CommunicationInterface ci;

    public WorkerThread(CommunicationInterface ci) {
        this.ci = ci;
    }
    
            
            
    @Override
    public void run() {
        
        Package p = (Package)ci.receiveFile();
        EasyImageWriter eiw = new EasyImageWriter(p,"MegaTest.pgm");
        eiw.write();
        
    }
    
    
}
