/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterClientUI;

import javax.swing.JFrame;

/**
 *
 * @author BOGDAN
 */
public class EasyFilterClientUI extends JFrame{
    
    private MainPanel mainPanel;
    
    public EasyFilterClientUI(){
        mainPanel = new MainPanel();
        setSize(400, 400);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public static void main(String args[]){
      EasyFilterClientUI ui = new EasyFilterClientUI();   
    }
    
}
