/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterClient.UI;

import EasyFilterClient.Communication.ClientCommunicationInterface;
import EasyFilterClient.EasyFilterClient;
import common.EasyImageWriter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author BOGDAN
 */
public class EasyFilterClientInter extends JPanel {
    
    private ClientCommunicationInterface ci;
    
    /**
     * Radio buttons for choosing type of request
     */
    private JButton Dwl;
    private JButton Upl;
    
     /**
     * Once an option is chosen, this button
     * does the action
     */
    private JButton goDownload;
    private JButton goUpload;
    
    
    /**
     * Button for choosing a file for browsing
     */
    private JButton choose;
    /**
     * Panel for common buttons
     */
    
    private JPanel northPanel;
    
    /**
     * panel for each option
     */
    private JPanel pUpl;
    private JPanel pDwl;
    
    /**
     * Jtext fields for the two options
     * filename - for downloading a file from the server
     * filepath - for uploading a file to the server
     */
    
    private JTextField  filename;
    private JTextField filepath;
    
    public EasyFilterClientInter(ClientCommunicationInterface ci){
        
        this.setCi(ci);
        
        this.setLayout(new GridLayout(2, 4));
        
        createCommon();    
        
        this.setpUpl(new JPanel());
        this.setpDwl(new JPanel());
        
        //this.add(getpUpl());
        
        /**
         * listener on the Dwl radio button
         */
        this.getDwl().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createDwl();
                DwlAction();
                
            }
        });
        /**
         * listener on the Upl radio button
         */
        this.getUpl().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createUpl();
                UplAction();
                
            }
        });
        
        
    }
    
    
    private void createCommon(){
        
        this.setUpl(new JButton("Upload"));
        this.setDwl(new JButton("Download"));
        this.setNorthPanel(new JPanel());
        
        
        this.getUpl().setVisible(true);
        this.getDwl().setVisible(true);
        
        this.getNorthPanel().add(this.getUpl());
        this.getNorthPanel().add(this.getDwl());
        
        this.add(this.getNorthPanel());

        
    }

    private void DwlAction(){
        
        this.remove(this.getpDwl());
        this.validate();
        
        this.remove(this.getpUpl());
        this.validate();
        this.add(this.getpDwl());
        this.validate();
        
        
    }
    
    private void UplAction(){
        this.remove(this.getpUpl());
        this.validate();       
        this.remove(this.getpDwl());
        this.validate();
        this.add(this.getpUpl());
        this.validate();
        
    }
    
    
    /**
     * creates the Upl interface
     */
    private void createUpl(){
        
        this.getpUpl().removeAll();
        
        this.setFilepath(new JTextField("",30));
        this.setChoose(new JButton("Choose File"));
        this.setGoUpload(new JButton("Do it"));
        
        
        this.getChoose().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                        
                chooseFile();
                
            }
        });
           
        
        this.getGoUpload().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createUploadRequest();
            }
        });
        
        this.getpUpl().add(this.getFilepath());
        this.getpUpl().add(this.getChoose());
        this.getpUpl().add(this.getGoUpload());
        
    }
    //file 0
    private void createUploadRequest(){
        this.getCi().openConnection();

        common.Package pkg;
        String filePathUI =  this.getFilepath().getText();
        pkg = EasyFilterClient.preparePackage(
            filePathUI, 
            0
        );
        ci.sendRequest(pkg);
        
        System.out.println("request sent");
        // receive file from server
        pkg = ci.receiveRequest();
        System.out.println("request received");
        
        // write the image locally
        EasyImageWriter eiw = new EasyImageWriter(pkg);
        eiw.write();
    }
    
    private void chooseFile(){
        
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int rVal = fileChooser.showOpenDialog(this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
        this.getFilepath().setText(fileChooser.getCurrentDirectory().toString()+"/"+fileChooser.getSelectedFile().getName());
//            this.getFilepath().setText(fileChooser.getCurrentDirectory().toString());
//            System.out.println("chooseer: "+fileChooser.getCurrentDirectory().toString());
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
        filename.setText("You pressed cancel");
        //directory.setText("");
        }
        
    }
    
    /**
     * creates the Dwl interface
     */
    private void createDwl(){
        this.getpDwl().removeAll();
        
        this.setFilename(new JTextField("",30));
        this.setGoDownload(new JButton("Do it"));
        
        this.getpDwl().add(this.getFilename());
        this.getpDwl().add(this.getGoDownload());
        
        this.getGoDownload().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createDownloadRequest();
            }
        });
        
    }
    
    private void createDownloadRequest(){
        common.Package pkg;
        this.getCi().openConnection();
        
        ci.sendRequest(EasyFilterClient.preparePackage(
            this.getFilename().getText(), 
            1
        ));
        
        System.out.println("request sent");
        // receive file from server
        pkg = ci.receiveRequest();
        System.out.println("request received");        
        
        // check if the package is an actual image
        if (pkg.getRequestType() == -1) {
            this.getFilename().setText("File Not Found");
        } else {
            // write the image locally
            EasyImageWriter eiw = new EasyImageWriter(pkg);
            eiw.write();
        }
    }
    
    private void initButtons() {
        //this.remove();
        //this.validate();
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public JButton getDwl() {
        return Dwl;
    }

    private void setDwl(JButton Dwl) {
        this.Dwl = Dwl;
    }

    public JPanel getpDwl() {
        return pDwl;
    }

    public void setpDwl(JPanel pDwl) {
        this.pDwl = pDwl;
    }

    public JPanel getpUpl() {
        return pUpl;
    }

    public void setpUpl(JPanel pUpl) {
        this.pUpl = pUpl;
    }

    public JButton getUpl() {
        return Upl;
    }

    private void setUpl(JButton Upl) {
        this.Upl = Upl;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public void setNorthPanel(JPanel northPanel) {
        this.northPanel = northPanel;
    }

    public JTextField getFilename() {
        return filename;
    }

    public void setFilename(JTextField filename) {
        this.filename = filename;
    }

    public JTextField getFilepath() {
        return filepath;
    }

    public void setFilepath(JTextField filepath) {
        this.filepath = filepath;
    }

    public JButton getGoDownload() {
        return goDownload;
    }

    public void setGoDownload(JButton goDownload) {
        this.goDownload = goDownload;
    }

    public JButton getGoUpload() {
        return goUpload;
    }

    public void setGoUpload(JButton goUpload) {
        this.goUpload = goUpload;
    }

    public JButton getChoose() {
        return choose;
    }

    public void setChoose(JButton choose) {
        this.choose = choose;
    }

    public ClientCommunicationInterface getCi() {
        return ci;
    }

    public void setCi(ClientCommunicationInterface ci) {
        this.ci = ci;
    }
    
    
    
}
