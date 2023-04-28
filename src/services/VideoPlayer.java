/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author HP
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.net.URL;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VideoPlayer extends JFrame {
     static Player player;
    
    public VideoPlayer(String mediaURL) {
        
        super("BANEKTI");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel videoPanel = new JPanel();
        add(videoPanel, BorderLayout.CENTER);
        
        try {
            URL url = new URL(mediaURL);
            MediaLocator locator = new MediaLocator(url);
            player = Manager.createRealizedPlayer(locator);
            Component video = player.getVisualComponent();
            videoPanel.add(video);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}