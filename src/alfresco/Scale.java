/*
* $Revision: 1.1 $
* $Id: Scale.java,v 1.1 2003/04/04 10:15:01 niclas Exp $
 *
 * This file is part of Alfresco
 * Copyright (C) 1998  Niclas Jareborg
 * 
 * The Alfresco source code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA  02111-1307, USA. 
 *
 * For more information contact Niclas at nic@sanger.ac.uk
*/
package alfresco;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
/**
 * A glyph to represent a bp scale.<P>
 * Displayed on the highest/lowest tier. Ticks are displayed depending on
 * the zoom level
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Scale extends SeqFeature {

/**
 * Creates a new Scale
 * @param ent The entry the scale is for.
 */
	Scale(Entry ent) {
		super(ent, new SeqRange(1, ent.getLength()));
		if (ent.tier > 0) { tier = CoordVal.MAXTIERS; }
		else { tier = - CoordVal.MAXTIERS; }
		fillColor = Color.black;
		canvHeight = 8;
	}

/**
 * Draws the Scale to the specified Graphics
 * @param g A graphics object to draw to.	
 */
	public void draw(Graphics g) {
		zoom = coord.zoom;
		int diff = coord.tierDiff(tier);
		if (tier > 0) { canvY = coord.originY - diff; }
		else if (tier < 0) { canvY = coord.originY + diff; }
		else { System.out.println("Kablooeeyh! Tier is 0!"); }
// 		canvWidth = (int) (range.length() * zoom);
// 		canvX =  coord.originX;
		realX =  coord.originX;
		realWidth = (int) (range.length() * zoom);
		realXend = realX + realWidth;
		if (realX > ABSXMAX || realXend < -ABSXMAX) { return; }
		if (realX < -ABSXMAX) {
			canvX = -ABSXMAX;
		} else {
			canvX = realX;
		}
		if (realXend > ABSXMAX) {
			canvWidth = ABSXMAX - canvX;
		} else {
// 			canvWidth = realWidth;
			canvWidth = realXend - canvX;
		}
		
		boundingRect = new Rectangle(canvX, canvY-canvHeight/2, canvWidth, canvHeight);
		g.setColor(fillColor);
		Font origFont = g.getFont();
		g.setFont(new Font("SansSerif", Font.PLAIN, 11));
		g.drawLine(canvX, canvY, canvX+canvWidth, canvY);
// 		System.out.println("Drawing Scale, x = " + canvX + ", y = " + canvY + ", w = " + canvWidth);
		if (realX > -ABSXMAX) {
			drawPos(g, "1", realX);
		}
		if (realXend < ABSXMAX) {
			drawPos(g, String.valueOf(range.getStop()), realXend);
		}
		double zoomf = 1.0;
		if ( zoom < 0.025 ) { zoomf = 10; }
		else if ( zoom > 0.25 ) { zoomf = 0.1; }
		for (int i = (int)(1000*zoomf); i < range.length(); i += (int)(1000*zoomf)) {
			int x = (int) (i * zoom);
			int pos = realX+x;
			if (pos > -ABSXMAX && pos < ABSXMAX) {
				drawPos(g, String.valueOf(i), pos);
			}
		}
		
		g.setFont(origFont);
	}

/**
 * Draws the number of the bp position of a scale tick
 * @param g A graphics object to draw to.
 * @param posString A number as a String to be displayed at the tick.
 * @param xPos The x pixel position for the tick.
 */
	private void drawPos(Graphics g, String posString, int xPos) {
		g.drawLine(xPos, canvY-canvHeight/2, xPos, canvY+canvHeight/2);
		if (tier < 0 ) { 
			g.drawString(posString, xPos, canvY+canvHeight/2+11);
		} else {
			g.drawString(posString, xPos, canvY-canvHeight/2-3);
		}
		
	}
}
