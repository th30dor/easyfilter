/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Handles the main panel that will be used in the user interface
 */
public class MainPanel extends JPanel
{
    private JTextField filename;
    private JTextField directory;
    private JButton upload;
    private JFileChooser fileChooser;
    private JTextField downloadFile;
    private JButton download;
    private JPanel northPanel;
    private JPanel southPanel;

    /**
     * Constructor
     */
    public MainPanel(){
        setLayout(new GridLayout(2, 1));
        northPanel = new JPanel();

        upload = new JButton("Upload");
        filename = new JTextField("",20);
        directory = new JTextField();
        upload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser(System.getProperty("user.dir"));
                int rVal = fileChooser.showOpenDialog(MainPanel.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename.setText(System.getProperty("user.dir")+"\\"+fileChooser.getSelectedFile().getName());
                    directory.setText(fileChooser.getCurrentDirectory().toString());
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    filename.setText("You pressed cancel");
                    directory.setText("");
                }
            }
        });
        northPanel.add(upload);
        northPanel.add(filename);
        add(northPanel);

        southPanel = new JPanel(new GridBagLayout());
        downloadFile = new JTextField("",20);
        download = new JButton("Download");
        southPanel.add(download);
        southPanel.add(downloadFile);
        add(southPanel);

    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public JTextField getDirectory() {
        return directory;
    }

    public void setDirectory(JTextField directory) {
        this.directory = directory;
    }

    public JButton getDownload() {
        return download;
    }

    public void setDownload(JButton download) {
        this.download = download;
    }

    public JTextField getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(JTextField downloadFile) {
        this.downloadFile = downloadFile;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public JTextField getFilename() {
        return filename;
    }

    public void setFilename(JTextField filename) {
        this.filename = filename;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public void setNorthPanel(JPanel northPanel) {
        this.northPanel = northPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public void setSouthPanel(JPanel southPanel) {
        this.southPanel = southPanel;
    }

    public JButton getUpload() {
        return upload;
    }

    public void setUpload(JButton upload) {
        this.upload = upload;
    }
}