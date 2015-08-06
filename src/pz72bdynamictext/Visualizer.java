/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz72bdynamictext;

import java.util.Map;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Peng
 */

public abstract class Visualizer{
    
    abstract void create(Map<String, Integer> freqMap, AnchorPane displayPane);
    abstract void cleanUp();
    
}
