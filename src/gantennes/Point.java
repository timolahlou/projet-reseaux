/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gantennes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author Nabil
 */
public class Point extends JPanel{

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public void changerX(){
        this.x +=10;
       /* Graphics2D gr = null;
        gr.setColor(Color.BLUE);
        Image img = Toolkit.getDefaultToolkit().getImage(JFrame.class.getResource("/images/Triangle.png"));
        gr.drawImage(img, x, y, this);*/
    }
   
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gr=(Graphics2D) g;
        gr.setColor(Color.BLUE);
       // gr.fillOval(10, 40, 10, 10);
       // gr.fillRect(this.x, this.y, 10, 10);
       Image img = Toolkit.getDefaultToolkit().getImage(JFrame.class.getResource("/images/Triangle.png"));
        gr.drawImage(img, x, y, this);
        
    }
    
}
