/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;
import java.util.*;
import java.awt.*;

/**
 *
 * @author Kathryn
 */
public class Tetromino {
    int type;
    int rotation;
    boolean stopped = true;
    
    public void addTetromino(int t) {
        type = t;
        rotation = 1;
        switch (type) {
            case 1: // I
                Tetris.currentBlocks[0] = new SubBlock(3, -3, Color.CYAN);
                Tetris.currentBlocks[1] = new SubBlock(4, -3, Color.CYAN);
                Tetris.currentBlocks[2] = new SubBlock(5, -3, Color.CYAN);
                Tetris.currentBlocks[3] = new SubBlock(6, -3, Color.CYAN);
                break;
            case 2: // J
                Tetris.currentBlocks[0] = new SubBlock(1, -3, Color.BLUE);
                Tetris.currentBlocks[1] = new SubBlock(1, -2, Color.BLUE);
                Tetris.currentBlocks[2] = new SubBlock(2, -2, Color.BLUE);
                Tetris.currentBlocks[3] = new SubBlock(3, -2, Color.BLUE);
                break;
            case 3: // L
                Tetris.currentBlocks[0] = new SubBlock(6, -2, Color.ORANGE);
                Tetris.currentBlocks[1] = new SubBlock(7, -2, Color.ORANGE);
                Tetris.currentBlocks[2] = new SubBlock(8, -2, Color.ORANGE);
                Tetris.currentBlocks[3] = new SubBlock(8, -3, Color.ORANGE);
                break;
            case 4: // O
                Tetris.currentBlocks[0] = new SubBlock(4, -3, Color.YELLOW);
                Tetris.currentBlocks[1] = new SubBlock(5, -3, Color.YELLOW);
                Tetris.currentBlocks[2] = new SubBlock(4, -2, Color.YELLOW);
                Tetris.currentBlocks[3] = new SubBlock(5, -2, Color.YELLOW);
                break;
            case 5: // S
                Tetris.currentBlocks[0] = new SubBlock(1, -2, Color.GREEN);
                Tetris.currentBlocks[1] = new SubBlock(2, -2, Color.GREEN);
                Tetris.currentBlocks[2] = new SubBlock(2, -3, Color.GREEN);
                Tetris.currentBlocks[3] = new SubBlock(3, -3, Color.GREEN);
                break;
            case 6: // T
                Tetris.currentBlocks[0] = new SubBlock(4, -2, Color.MAGENTA);
                Tetris.currentBlocks[1] = new SubBlock(5, -2, Color.MAGENTA);
                Tetris.currentBlocks[2] = new SubBlock(5, -3, Color.MAGENTA);
                Tetris.currentBlocks[3] = new SubBlock(6, -2, Color.MAGENTA);
                break;
            case 7: // Z
                Tetris.currentBlocks[0] = new SubBlock(6, -3, Color.RED);
                Tetris.currentBlocks[1] = new SubBlock(7, -3, Color.RED);
                Tetris.currentBlocks[2] = new SubBlock(7, -2, Color.RED);
                Tetris.currentBlocks[3] = new SubBlock(8, -2, Color.RED);
                break;
        }
    }
}
