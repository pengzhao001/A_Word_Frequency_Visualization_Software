/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz72bdynamictext;

import java.util.Map;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Peng
 */
public class ChartVisualizer extends Visualizer {
    
    private Double height;
    private Double width;
    private AnchorPane anchorPane;
    private int shapeCount = 0;
    
    @Override
    public void create(Map<String, Integer> freqMap, AnchorPane displayPane){
    
        cleanUp();
        
        height = displayPane.getHeight();
        width = displayPane.getWidth(); 
        
        anchorPane = displayPane;
        anchorPane.setStyle("-fx-background-color: #F3F781;");
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();  
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle("Word Frequency");
        bc.setLegendVisible(false);
        bc.setHorizontalGridLinesVisible(false);
        bc.setVerticalGridLinesVisible(false);
        
        xAxis.setLabel("Word");
        xAxis.setTickLabelRotation(90.0);
        yAxis.setLabel("Frequency");
 
        XYChart.Series series = new XYChart.Series();
        
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()){
            
            String text = entry.getKey();
            int freq = entry.getValue();
            
            series.getData().add(new XYChart.Data(text, freq));
            
            
            shapeCount++;   
        }  
        bc.getData().add(series);
        anchorPane.getChildren().add(bc);
    }


    @Override
    public void cleanUp() {
        if (shapeCount != 0) {
            anchorPane.getChildren().clear();
            anchorPane.setStyle("-fx-background-color: transparent;");
            shapeCount = 0;
        }
    }

    
    
}
