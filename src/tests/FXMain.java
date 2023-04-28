/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.awt.BorderLayout;
import java.awt.Component;
import services.VideoPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 *
 * @author HP
 */
public class FXMain extends Application {
    
    public Player player;

public class VideoPlayer extends JFrame {
     
    
    public VideoPlayer(URL mediaURL) {
        
        super("BANEKTI");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel videoPanel = new JPanel();
        add(videoPanel, BorderLayout.CENTER);
        
        try {
//            URL url = new URL(mediaURL);
//            MediaLocator locator = new MediaLocator(url);
//            player = Manager.createRealizedPlayer(locator);
            Component video = player.getVisualComponent();
            videoPanel.add(video);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void startVideoPlayer() {
            player.start();
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
//       Path path = Paths.get("C:\\xampp\\htdocs\\Gestion_Pack\\src\\Media\\ayoub.mp4");
//       URL url = path.toUri().toURL();
//       VideoPlayer player = new VideoPlayer(url); 
//       player.setVisible(true);
//       player.startVideoPlayer();
//       player.setVisible(false);

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/BackHome.fxml"));
        
                  Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Back Office");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     */
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        
        String clipPath;
        clipPath = "C:\\Users\\HP\\Desktop\\ss.wav";
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File(clipPath));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
             clip.start();
             
        launch(args);
    }
    
}
