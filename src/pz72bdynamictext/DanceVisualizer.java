/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz72bdynamictext;

import java.util.Map;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Peng
 */
public class DanceVisualizer extends Visualizer {
    
    private Double height;
    private Double width;
    private AnchorPane anchorPane;
    private int shapeCount = 0;
    private TranslateTransition transition;
    private int maxFreq = 0;
    
    @Override
    public void create(Map<String, Integer> freqMap, AnchorPane displayPane){
    
        cleanUp();
        
        height = displayPane.getHeight();
        width = displayPane.getWidth(); 
        
        anchorPane = displayPane;
        anchorPane.setStyle("-fx-background-color: #000000;");
        
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()){
            
            String text = entry.getKey();
            int freq = entry.getValue();
            
            if (freq > maxFreq) {
                maxFreq = freq;
            }
            
            Text t = createTextShape(text, freq);
            
            anchorPane.getChildren().add(t);
            shapeCount++;   
        }   
    }


    @Override
    public void cleanUp() {
        if (shapeCount != 0) {
            anchorPane.getChildren().clear();
            anchorPane.setStyle("-fx-background-color: transparent;");
            shapeCount = 0;
            maxFreq = 0;
        }
    }

    
    public Text createTextShape(String text, int freq) {
        Text t = new Text();

        int fontSize = (int) (Math.pow(4.0, 1.5*freq/maxFreq)*10);
        int hue = 350 / maxFreq * freq;
        t.setFont(new Font(fontSize));
        t.setText(text);
        t.setFill(Color.hsb(hue, 1.0, 1.0, 1.0));

        setAnimation(freq, t);
        
        return t;    
    }
    
    public void setAnimation(int freq, Text t){
        
        double fromX = Math.random() * width;
        double fromY = Math.random() * height;
        double toX = Math.random() * width;
        double toY = Math.random() * height;
        double delayTime = 0;
        int duration = 5;
        
        transition = new TranslateTransition();
        transition.setFromX(fromX);
        transition.setFromY(fromY); 
        transition.setToX(toX);
        transition.setToY(toY);
        transition.setCycleCount(100);
        transition.setAutoReverse(true);
        transition.setDelay(Duration.seconds(delayTime));
        transition.setDuration(Duration.seconds(duration));
        transition.setNode(t);
        transition.play();
       
           
    }
}
