/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz72bdynamictext;

import java.util.Map;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Peng
 */
public class WordFreq {
    
    private final SimpleStringProperty WORD;
    private final SimpleStringProperty FREQ;
    
    public WordFreq(Map.Entry<String, Integer> entry){
        this.WORD = new SimpleStringProperty(entry.getKey());
        this.FREQ = new SimpleStringProperty(entry.getValue().toString());
    }
    
    public String getWord() {
        return WORD.get();
    }
    
    public void setWord(Map.Entry<String, Integer> entry) {
        WORD.set(entry.getKey());
    }
    
    public String getFreq() {
        return FREQ.get();
    }
    
    public void setFreq(Map.Entry<String, Integer> entry) {
        WORD.set(entry.getValue().toString());
    }
    
    
}
