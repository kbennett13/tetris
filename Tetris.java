/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.lang.*;

/**
 *
 * @author Kathryn
 */
public class Tetris extends JPanel {
    static boolean gameOver = false;
    static int score;
    static int level;
    static int blockWidth = 25;
    static int gridWidth = 10;
    static int gridHeight = 20;
    static int left = 100;
    static int top = 150;
    static Tetromino t;
    static HashMap tops = new HashMap(10);
    static SubBlock[] currentBlocks = new SubBlock[4];
    static SubBlock[][] allBlocks = new SubBlock[gridWidth][gridHeight];
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 550, 750);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(100, 100, 100, 650);
        g2d.drawLine(100, 650, 350, 650);
        g2d.drawLine(350, 100, 350, 650);
        g2d.drawString("TETRIS", 200, 50);
        if (gameOver) {
            g2d.drawString("GAME OVER", 200, 100);
        }
        g2d.drawString("SCORE", 450, 150);
        g2d.drawString(String.valueOf(score), 450, 175);
        g2d.drawString("LEVEL", 450, 225);
        g2d.drawString(String.valueOf(level), 450, 250);
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                SubBlock b = allBlocks[i][j];
                if (b != null) {
                    g2d.setColor(b.color);
                    g2d.fill3DRect(left+blockWidth*b.topX, top+blockWidth*b.topY,
                            blockWidth, blockWidth, true);
                }
            }
        }
        for (int i = 0; i < currentBlocks.length; i++) {
            SubBlock b = currentBlocks[i];
            if (b != null) {
                g2d.setColor(b.color);
                g2d.fill3DRect(left+blockWidth*b.topX, top+blockWidth*b.topY,
                        blockWidth, blockWidth, true);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        Tetris game = new Tetris();
        t = new Tetromino();
        frame.add(game);
        frame.setSize(550, 750);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Tetris keyboardExample = new Tetris();
	frame.add(keyboardExample);
        score = 0;
        level = 0;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                allBlocks[i][j] = null;
            }
        }
        for (int i = 0; i < currentBlocks.length; i++) {
            currentBlocks[i] = null;
        }
        for (int i = 0; i < gridWidth; i++) {
            tops.put(100+25*i, 650);
        }
        long lastDrop = 0;
        
        while (true) {
            if (t.stopped) {
              t.addTetromino(newType());
              t.stopped = false;
              lastDrop = new Date().getTime();
            } else {
              long currentTime = new Date().getTime();
              if (currentTime - lastDrop >= 500) {
                moveTetromino();
                lastDrop = currentTime;
              }
            }
            game.repaint();
        }
    }
    
    public static void moveTetromino() {
        if (passCollisionCheck()) {
            for (int i = 0; i < currentBlocks.length; i++) {
                currentBlocks[i].topY += 1;
            }
        }
    }
    
    public static boolean passCollisionCheck() {
        try {
            // check for bottom boundary
            for (int i = 0; i < currentBlocks.length; i++) {
                if (currentBlocks[i].topY + 1 == gridHeight) {
                    for (int j = 0; j < currentBlocks.length; j++) {
                        SubBlock temp = currentBlocks[j];
                        allBlocks[temp.topX][temp.topY] = currentBlocks[j];
                        currentBlocks[j] = null;
                    }
                    t.stopped = true;
                    score();
                    return false;
                }
            }

            // check for other blocks
            for (int i = 0; i < currentBlocks.length; i++) {
                SubBlock temp = currentBlocks[i];
                if (temp.topY >= 0) {
                    if (allBlocks[temp.topX][temp.topY + 1] != null) {
                        for (int j = 0; j < currentBlocks.length; j++) {
                            SubBlock temp2 = currentBlocks[j];
                            allBlocks[temp2.topX][temp2.topY] = currentBlocks[j];
                            currentBlocks[j] = null;
                        }
                        t.stopped = true;
                        score();
                        return false;
                    }
                } else {
                    continue;
                }
            }

            return true;
        } catch (Exception e) {
            gameOver = true;
            return false;
        }
    }
    
    public static int getLeftmost() {
        int min = gridWidth;

        for (int i = 0; i < currentBlocks.length; i++) {
            if (currentBlocks[i].topX < min) {
                min = currentBlocks[i].topX;
            }
        }

        return min;
    }
    
    public static int getRightmost() {
        int max = 0;

        for (int i = 0; i < currentBlocks.length; i++) {
            if (currentBlocks[i].topX + 1 > max) {
                max = currentBlocks[i].topX + 1;
            }
        }

        return max;
    }
    
    static int newType() {
        int type = 0;
        
        double rand = Math.round(Math.random()*1000);
        
        if (rand <= 137) {
            type = 1;
        } else if (rand <= 274) {
            type = 2;
        } else if (rand <= 380) {
            type = 3;
        } else if (rand <= 541) {
            type = 4;
        } else if (rand <= 702) {
            type = 5;
        } else if (rand <= 863) {
            type = 6;
        } else {
            type = 7;
        }
        
        return type;
    }
    
    public static void score() {
        int lines = 0;
        
        for (int i = gridHeight - 1; i >= 0; i--) {
            boolean all_full = true;
            
            for (int j = 0; j < gridWidth; j++) {
                if (allBlocks[j][i] == null) {
                    all_full = false;
                    break;
                }
            }
            
            if (all_full) {
                for (int j = i; j >= 0; j--) {
                    for (int k = 0; k < gridWidth; k++) {
                        if (j > 0) {
                            if (allBlocks[k][j - 1] != null) {
                                allBlocks[k][j - 1].topY += 1;
                                allBlocks[k][j] = allBlocks[k][j - 1];
                                allBlocks[k][j - 1] = null;
                            } else {
                                allBlocks[k][j] = null;
                            }
                        } else {
                            if (allBlocks[k][j] != null) {
                                allBlocks[k][j].topY += 1;
                                allBlocks[k][j] = null;
                            }
                        }
                    }
                }
                i++;
                lines++;
            }
        }
        
        switch (lines) {
            case 1:
                score += 40 * (level+1);
                break;
            case 2:
                score += 100 * (level+1);
                break;
            case 3:
                score += 300 * (level+1);
                break;
            case 4:
                score += 1200 * (level+1);
                break;
        }
    }
    
    public class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveRight();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moveTetromino();
            } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                changeRotation();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
        
        public void moveLeft() {
            if (getLeftmost() > 0) {
                for (int i = 0; i < currentBlocks.length; i++) {
                    currentBlocks[i].topX -= 1;
                }
            }
        }
        
        public void moveRight() {
            if (getRightmost() < gridWidth) {
                for (int i = 0; i < currentBlocks.length; i++) {
                    currentBlocks[i].topX += 1;
                }
            }
        }
        
        public void changeRotation() {
            switch (t.type) {
                case 1:
                    switch (t.rotation) {
                        case 1:
                            currentBlocks[0].topX += 2;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[1].topX += 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY += 2;
                            t.rotation = 2;
                            break;
                        case 2:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY += 2;
                            currentBlocks[1].topY += 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[3].topX -= 2;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 3;
                            break;
                        case 3:
                            currentBlocks[0].topX -= 2;
                            currentBlocks[0].topY += 1;
                            currentBlocks[1].topX -= 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY -= 2;
                            t.rotation = 4;
                            break;
                        case 4:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY -= 2;
                            currentBlocks[1].topY -= 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[3].topX += 2;
                            currentBlocks[3].topY += 1;
                            t.rotation = 1;
                            break;
                    }
                    break;
                case 2:
                    switch (t.rotation) {
                        case 1:
                            currentBlocks[0].topX += 2;
                            currentBlocks[1].topX += 1;
                            currentBlocks[1].topY -= 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY += 1;
                            t.rotation = 2;
                            break;
                        case 2:
                            currentBlocks[0].topY += 2;
                            currentBlocks[1].topX += 1;
                            currentBlocks[1].topY += 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 3;
                            break;
                        case 3:
                            currentBlocks[0].topX -= 2;
                            currentBlocks[1].topX -= 1;
                            currentBlocks[1].topY += 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 4;
                            break;
                        case 4:
                            currentBlocks[0].topY -= 2;
                            currentBlocks[1].topX -= 1;
                            currentBlocks[1].topY -= 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY += 1;
                            t.rotation = 1;
                            break;
                    }
                    break;
                case 3: // L
                    switch (t.rotation) {
                        case 1:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topY += 2;
                            t.rotation = 2;
                            break;
                        case 2:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY += 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topX -= 2;
                            t.rotation = 3;
                            break;
                        case 3:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY += 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topY -= 2;
                            t.rotation = 4;
                            break;
                        case 4:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topX += 2;
                            t.rotation = 1;
                            break;
                    }
                    break;
                case 5:
                    switch (t.rotation) {
                        case 1:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topY += 2;
                            t.rotation = 2;
                            break;
                        case 2:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY += 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topX -= 2;
                            t.rotation = 3;
                            break;
                        case 3:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY += 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topY -= 2;
                            t.rotation = 4;
                            break;
                        case 4:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topX += 2;
                            t.rotation = 1;
                            break;
                    }
                    break;
                case 6:
                    switch (t.rotation) {
                        case 1:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY += 1;
                            t.rotation = 2;
                            break;
                        case 2:
                            currentBlocks[0].topX += 1;
                            currentBlocks[0].topY += 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[2].topY += 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 3;
                            break;
                        case 3:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY += 1;
                            currentBlocks[2].topX -= 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 4;
                            break;
                        case 4:
                            currentBlocks[0].topX -= 1;
                            currentBlocks[0].topY -= 1;
                            currentBlocks[2].topX += 1;
                            currentBlocks[2].topY -= 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY += 1;
                            t.rotation = 1;
                            break;
                    }
                    break;
                case 7:
                    switch (t.rotation) {
                        case 1:
                            currentBlocks[0].topX += 2;
                            currentBlocks[1].topX += 1;
                            currentBlocks[1].topY += 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY += 1;
                            t.rotation = 2;
                            break;
                        case 2:
                            currentBlocks[0].topY += 2;
                            currentBlocks[1].topX -= 1;
                            currentBlocks[1].topY += 1;
                            currentBlocks[3].topX -= 1;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 3;
                            break;
                        case 3:
                            currentBlocks[0].topX -= 2;
                            currentBlocks[1].topX -= 1;
                            currentBlocks[1].topY -= 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY -= 1;
                            t.rotation = 4;
                            break;
                        case 4:
                            currentBlocks[0].topY -= 2;
                            currentBlocks[1].topX += 1;
                            currentBlocks[1].topY -= 1;
                            currentBlocks[3].topX += 1;
                            currentBlocks[3].topY += 1;
                            t.rotation = 1;
                            break;
                    }
                    break;
            }
        }
    }
    
    
    
    public Tetris() {
	addKeyListener(new MyKeyListener());
	setFocusable(true);
    }
}
