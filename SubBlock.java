/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;
import java.awt.*;

/**
 *
 * @author Kathryn
 */
public class SubBlock {
    int topX;
    int topY;
    Color color;
    
    public SubBlock(int x, int y, Color c) {
        topX = x;
        topY = y;
        color = c;
    }
}
