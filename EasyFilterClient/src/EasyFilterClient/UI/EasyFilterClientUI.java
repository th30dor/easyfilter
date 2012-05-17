/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient.UI;

import javax.swing.JFrame;

/**
 * Main user interface for EasyClient
 */
public class EasyFilterClientUI extends JFrame{

    //private MainPanel mainPanel;
    private EasyFilterClientInter mainPanel;

    /**
     * Constructor
     */
    public EasyFilterClientUI ()
    {
        //mainPanel = new MainPanel();
        mainPanel = new EasyFilterClientInter(null);
        setSize(400, 400);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method
     *
     * @param args arguments to be run with
     */
    public static void main(String args[])
    {
      EasyFilterClientUI ui = new EasyFilterClientUI();
    }
}