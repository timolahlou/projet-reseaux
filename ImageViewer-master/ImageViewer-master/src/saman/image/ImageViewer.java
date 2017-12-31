/* 
 * Copyright (C) 2016 Mátyás Gede
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package saman.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 * Simple image viewer class
 * @author Mátyás Gede
 */
public class ImageViewer extends JLabel implements MouseWheelListener,ComponentListener {
    /** the image */
    protected BufferedImage img;
    /** should aspect be kept? */
    protected boolean keepAspect=true;
    /** bounds of currently viewed area */
    protected int x0,y0,w,h;
    /** marker data */
    private List<Point> markerPos=new ArrayList<>();
    private List<String> markerId=new ArrayList<>();
    /** constructor */
    public ImageViewer() {
        super();
    }
    
    // implementing ComponentListener interface
    @Override
    public void componentHidden(ComponentEvent e) {}
    
    @Override
    public void componentMoved(ComponentEvent e) {}
    
    @Override
    public void componentShown(ComponentEvent e) {}
    
    @Override
    public void componentResized(ComponentEvent e) {
        // taking care of aspect ratio
        int hc=getHeight();
        int wc=getWidth();
        if (keepAspect)
            if ((double)wc/hc>(double)w/h) {
                x0-=((h*wc/hc)-w)/2;
                w=h*wc/hc;
            }
            else {
                y0-=((w*hc/wc)-h)/2;
                h=w*hc/wc;
            }        
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // get rotation direction
        int r=e.getWheelRotation();
        // compute relative coordinates of mouse pointer
        double mx=(double)e.getX()/getWidth();
        double my=(double)e.getY()/getHeight();
        // modify bounds according to rotation
        if (r>0) {
            x0-=(int)(w*mx);
            y0-=(int)(h*my);
            h*=2;w*=2;
        }
        else {
            x0+=(int)(w*mx/2);
            y0+=(int)(h*my/2);
            h/=2;w/=2;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (img!=null) {
            int hc=getHeight();
            int wc=getWidth();        
            g.drawImage(img,0,0,wc,hc,x0,y0,x0+w,y0+h,this);
            // drawing markers if they exist
            for(int i=0;i<markerId.size();i++) {
                g.setColor(Color.yellow);
                Point p=markerPos.get(i);
                int x=(p.x-x0)*wc/w;
                int y=(p.y-y0)*hc/h;
                if (x>=0&&x<wc&&y>=0&&y<hc)
                    g.fillRect(x-1,y-1,3,3);
            }
        }    
    }
    
    /**
     * Computes image pixelcoordinates from coordinates of the image viewer
     * @param x
     * @param y
     * @return Point object with the image coordinates
     */
    public Point getImageCoord(int x, int y) {
        // compute relative coordinates
        double mx=(double)x/getWidth();
        double my=(double)y/getHeight();
        // returns Point object
        return new Point(x0+(int)(w*mx),y0+(int)(h*my));
    }
    
    /**
     * Computes image pixelcoordinates from coordinates of the image viewer
     * @param p
     * @return Point object with the image coordinates
     */
    public Point getImageCoord(Point p) {
        return getImageCoord(p.x,p.y);
    }
    
    /**
     * Sets image
     * @param im the image to view
     */
    public void setImage(BufferedImage im) {
        img=im;
        // set initial bounds
        x0=y0=0;
        w=img.getWidth();
        h=img.getHeight();
        
        // maintaining aspect ratio if set
        int hc=getHeight();
        int wc=getWidth();
        if (keepAspect)
            if ((double)wc/hc>(double)w/h) {
                x0-=((h*wc/hc)-w)/2;
                w=h*wc/hc;
            }
            else {
                y0-=((w*hc/wc)-h)/2;
                h=w*hc/wc;
            }
        repaint();
        // setting listeners for mouse wheel and component resize
        removeMouseWheelListener(this);
        addMouseWheelListener(this);
        removeComponentListener(this);
        addComponentListener(this);
    }
    
    /**
     * Sets image
     * @param im image to view
     * @param keepAspect should it keep the aspect ratio?
     */
    public void setImage(BufferedImage im, boolean keepAspect) {
        this.keepAspect=keepAspect;
        setImage(im);
    }
    
    /**
     * retrieves the dimensions of the currently viewed image
     * @return 
     */
    public Dimension getImageSize() {
        return new Dimension(img.getWidth(),img.getHeight());
    }
    
    /**
     * add a marker to the image
     * @param id ID of the marker
     * @param pos position of the marker
     */
    public void addMarker(String id, Point pos) {
        int i=markerId.indexOf(id);
        if (i<0) {
            markerId.add(id);
            markerPos.add(pos);            
        }
        else {
            markerPos.set(i, pos);
        }
        repaint();
    }
    
    /**
     * removes marker with the given ID
     * @param id
     * @return true on success
     */
    public boolean removeMarker(String id) {
        int i=markerId.indexOf(id);
        if (i>=0) {
            markerId.remove(i);
            markerPos.remove(i);
            repaint();
            return true;
        }
        else 
            return false;
    }
}
