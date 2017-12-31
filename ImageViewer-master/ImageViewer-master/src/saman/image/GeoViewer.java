/* 
 * Copyright (C) 2016 Mátyás Gede <saman at map.elte.hu>
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
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Extension of ImageViewer class
 * capable of displaying geographic graticule lines for a given metapole
 * 
 * @author saman
 */
public class GeoViewer extends ImageViewer {
    
    private static final double rad=PI/180;
    /** Should grid be displayed? */
    public boolean showGrid=false;
    /** Array of lines; each line is array of Points */
    List<List<Point>> lines=new ArrayList();

    /**
     * creates graticule for a given metapole and meta prime meridian
     * 
     * @param fi0 latitude of the metapole in radians
     * @param la0 longitude of the metapole in radians
     * @param dlc rotation for the meta-PM
     */
    public void createGrid(double fi0, double la0, double dlc) {
        lines.clear();
        Dimension s=getImageSize();
        double fi=0,la=0,fic,lac,pfi,pla;
        boolean firstPoint;
        System.out.println("metapole:"+fi0/rad+"/"+la0/rad);
        for(lac=-180;lac<=180;lac+=10) {
            List<Point> line=new ArrayList();
            firstPoint=true;
            for(fic=-90;fic<=90;fic+=2) {
                pfi=fi;pla=la;
                if (fi0!=90*rad||la0!=0) {
                    double f=asin(sin(fi0)*sin(fic*rad)+cos(fi0)*cos(fic*rad)*cos(lac*rad-dlc));
                    double cosdl=(sin(fic*rad)-sin(fi0)*sin(f))/cos(fi0)/cos(f);
                    if (cosdl>1) cosdl=1;
                    if (cosdl<-1) cosdl=-1;
                    double l=la0-acos(cosdl)*(sin(lac*rad-dlc)>0?1:-1);
                    fi=(f/rad);la=(l/rad);
                }
                else {
                    fi=fic;la=lac;
                }
                if (la<-180) la+=360;
                if (la>180) la-=360;
                if (!firstPoint) 
                    if (abs(pla-la)>180) {
                        lines.add(line);
                        line.clear();
                        //firstPoint=true;
                    }
                line.add(new Point((int)((la+180)*s.width/360),(int)((90-fi)*s.height/180)));
                firstPoint=false;
            }
            lines.add(line);
        }
        // refresh view
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img!=null&&showGrid) {
            int hc=getHeight();
            int wc=getWidth();
            g.setColor(Color.green);
            for (List<Point> line : lines) {
                for(int i=0;i<line.size()-1;i++) {
                    Point p1=line.get(i), p2=line.get(i+1);
                    int x1=(p1.x-x0)*wc/w;
                    int y1=(p1.y-y0)*hc/h;
                    int x2=(p2.x-x0)*wc/w;
                    int y2=(p2.y-y0)*hc/h;
                    //if ((x1>=0&&x1<wc&&y1>=0&&y1<hc)||(x2>=0&&x2<wc&&y2>=0&&y2<hc))
                        g.drawLine(x1,y1,x2,y2);
                }
            }
        }
    }
}
