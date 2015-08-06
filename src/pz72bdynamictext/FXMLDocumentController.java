/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz72bdynamictext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 *
 * @author Peng
 */
public class FXMLDocumentController implements Initializable {
    

    @FXML
    private CheckBox swFilterOn;
    @FXML 
    private TextArea inputText;
    @FXML 
    private TextField freqThreshold;
    @FXML
    private RadioButton matrix;
    @FXML
    private RadioButton dance;
    @FXML
    private RadioButton bar;
    @FXML
    private AnchorPane anchorPane;
    
    private Visualizer currentVisualizer;
    
    private ToggleGroup visualizerGroup;

    private Stage stage;
    
    private Map<String, Integer> freqMap;
    @FXML
    private TableView<WordFreq> table;
    @FXML
    private TableColumn<WordFreq, String> wordColumn;
    @FXML
    private TableColumn<WordFreq, String> frequencyColumn;
    
    private ObservableList<WordFreq> data;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        inputText.clear();
        swFilterOn.setSelected(true);
        freqThreshold.setText("1");
        
        visualizerGroup = new ToggleGroup();
        matrix.setToggleGroup(visualizerGroup);
        dance.setToggleGroup(visualizerGroup);
        bar.setToggleGroup(visualizerGroup);

        matrix.setUserData(new MatrixVisualizer());
        matrix.setOnAction((ActionEvent event) -> {
                selectVisualizer(event);
        });
        
        dance.setUserData(new DanceVisualizer());
        dance.setOnAction((ActionEvent event) -> {
                selectVisualizer(event);
        });
        
        bar.setUserData(new ChartVisualizer());
        bar.setOnAction((ActionEvent event) -> {
                selectVisualizer(event);
        });
        
        matrix.setSelected(true);
        currentVisualizer = new MatrixVisualizer();
        
        inputText.getStyleClass().add("textarea");
        inputText.setWrapText(true);
    }
    
    
    private void selectVisualizer(ActionEvent event) {
        RadioButton radioButton = (RadioButton)event.getSource();
        Visualizer visualizer = (Visualizer)radioButton.getUserData();
        changeVisualizer(visualizer);
    }
    
    
    private void changeVisualizer(Visualizer visualizer) {
        if (currentVisualizer != null) {
            currentVisualizer.cleanUp();
        }
        currentVisualizer = visualizer;
    }
    
    // TableView: http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
    @FXML
    private void createButtonHandler(ActionEvent event){
        
        if (inputText.getText().trim().length() == 0) {
            return;
        }
         
        String input = inputText.getText();
        
        FreqCalculator freqCalc = new FreqCalculator(input);        
        
        if(swFilterOn.isSelected()){
            freqCalc.setFilterOn();
        } else {
            freqCalc.setFilterOff();  
        } 
        
        
        String threshold = freqThreshold.getText();
        
        freqMap = freqCalc.getFreqMap(Integer.parseInt(threshold));
        
        //System.out.println(freqMap);
        
        currentVisualizer.create(freqMap, anchorPane);
        
        table.setEditable(true);
        data = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : freqMap.entrySet()){
            data.add(new WordFreq(entry));    
        }
        
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("freq"));
        
        table.setItems(data);
    }
    
    @FXML
    private void resetButtonHandler(ActionEvent event){ 
        
        if (inputText.getText().trim().length() == 0) {
            return;
        }
      
        inputText.clear();
        swFilterOn.setSelected(true);
        freqThreshold.setText("1");
        matrix.setSelected(true);
        currentVisualizer = new MatrixVisualizer();
        data.removeAll(data);
        
        anchorPane.getChildren().clear();
        anchorPane.setStyle("-fx-background-color: transparent;");

    }  

    @FXML
    private void openHandler(ActionEvent event){
        
        if (inputText.getText().trim().length() != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Do you want to save the current text?");
            
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
            Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
            yesButton.setDefaultButton(true);
            Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            yesButton.setOnAction((ActionEvent event1) -> {
                saveFile();
            });
            noButton.setOnAction((ActionEvent event1) -> {
                openFile();
            });
            cancelButton.setOnAction((ActionEvent event1) -> {
                return;
            });
            alert.showAndWait();  
        } else {
            openFile();  
        }
    }
    
    private void openFile() {
        
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open Text File");
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        File inputFile = openFile.showOpenDialog(stage);
        if (inputFile != null) {
            try {
                InputStream in = new FileInputStream(inputFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputText.setText(stringBuilder.toString());
                reader.close();
                in.close();
            } catch(Exception e){
                System.out.println(e.getMessage());  
            }
        }  
    }
    
    @FXML
    private void saveHandler(ActionEvent event){
        
        if (inputText.getText().trim().length() == 0) {
            return;
        }
        saveFile();
        
    }
    
    private void saveFile() {
        
        FileChooser saveFile = new FileChooser();
        saveFile.setTitle("Save Text File");
        saveFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        File outputFile = saveFile.showSaveDialog(stage);
        if (outputFile != null){
            try{
                OutputStream out = new FileOutputStream(outputFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                String text = inputText.getText();
                writer.write(text); 
                writer.flush();
                writer.close();
                out.close();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        
    }
    
    @FXML
    private void exportHandler(ActionEvent event){
        
        if (freqMap == null) {
            return;
        }
        
        FileChooser saveFile = new FileChooser();
        saveFile.setTitle("Export Word Frequency");
        saveFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File outputFile = saveFile.showSaveDialog(stage);
        if (outputFile != null){
            try{
                OutputStream out = new FileOutputStream(outputFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                for (Map.Entry<String, Integer> entry : freqMap.entrySet()){
                    String text = entry.getKey();
                    String freq = Integer.toString(entry.getValue());
                    writer.append(text);
                    writer.append(",");
                    writer.append(freq);
                    writer.append("\n");
                } 
                writer.flush();
                writer.close();
                out.close();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    @FXML
    private void aboutHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Dynamic Text");
        alert.setContentText("This program was developed to count the frequency of each word in a text and visualize the words based on their frequencies in a dynamic way or in a bar chart.\n"  
                + "\n" + "Version: 1.0.0" + "\n" + "Author: Peng Zhao");
        alert.showAndWait();
    }
    


}
    
    
