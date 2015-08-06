/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz72bdynamictext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 *
 * @author Peng
 */
public class FreqCalculator {
    
    private String[] words;
    private boolean filterIsOn = true;
    
    
    public void setFilterOn(){
        filterIsOn = true;
    }
    
    public void setFilterOff(){
        filterIsOn = false; 
    }
    
    
    public FreqCalculator(String inputText) {
        
        // split the string by any non-word delimiters and white spaces
        // http://stackoverflow.com/questions/11874234/difference-between-w-and-b-regular-expression-meta-characters
        
        String tempText = inputText.replaceAll("\\W", " ");
        words = tempText.split("\\s+");
    }
    
    public FreqCalculator(String inputText, boolean filterIsOn) {
        this(inputText);
        this.filterIsOn = filterIsOn;
    }
     
    public Map<String, Integer> getFreqMap(int freqThreshold){
      
        // first get a map with words as keys and freq as values
        // http://stackoverflow.com/questions/21771566/calculating-frequency-of-each-word-in-a-sentence-in-java
        
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            Integer n = map.get(word);
            n = (n == null) ? 1 : ++n;
            map.put(word, n);
        }  
        
        // only interested with words having freq > threshold
        // http://stackoverflow.com/questions/1335935/whats-the-quickest-way-to-remove-an-element-from-a-map-by-value-in-java
        
        while(freqThreshold > 0){
            while(map.values().remove(freqThreshold));
            freqThreshold--;
        }
        
        // sort the map 
        // http://stackoverflow.com/questions/1894081/what-is-the-easiest-way-to-sort-maps-according-to-values-in-java
        
        List<Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, (Entry<String, Integer> e1, Entry<String, Integer> e2) 
                -> e2.getValue().compareTo(e1.getValue()));
        Map<String, Integer> orderedMap = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : entries) {
            orderedMap.put(entry.getKey(), entry.getValue());
        }
        
        // remove the stop words if filter is on
        // stop word list credit:
        // http://ir.dcs.gla.ac.uk/resources/linguistic_utils/stop_words
         
        if (filterIsOn){
            orderedMap.keySet().removeAll(Arrays.asList(StopWord.LIST));
        }
        return orderedMap;    
    }    
}
