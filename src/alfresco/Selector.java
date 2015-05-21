/*
* $Revision: 1.1 $
* $Id: Selector.java,v 1.1 2003/04/04 10:15:03 niclas Exp $
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
 * A marquee Glyph used to select a range.
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Selector extends SeqFeature {
	
/**
 * Creates a new selector for the specified Entry with the specified x and y position, and width and height
 * @param ent the Entry that the selector should cover
 * @param x selector x position in pixels
 * @param y selector y position in pixels (not used)
 * @param width selector width in pixels
 * @param height selector height in pixels (not used)
 */
	Selector(Entry ent, int x, int y, int width, int height) {
		super(ent);
		canvHeight = 25;
		int start = (int) Math.round((x-ent.realX)/ent.coord.zoom);
		if (start < 1 ) {
			start = 1;
		} else if (start > entry.getLength()) {
			start = entry.getLength();
		}
		int stop = (int) Math.round((x+width-ent.realX)/ent.coord.zoom);
		if (stop < 1 ) {
			stop = 1;
		} else if (stop > entry.getLength()) {
			stop = entry.getLength();
		}
		range = new SeqRange(start, stop);
		fillColor = Color.blue;
		highlightColor = fillColor;
	}
		
/**
 * Determines if the Selector overlaps another selector
 * @return true if the Selectors overlap, otherwise false
 * @param sel the other Selector
 */
	public boolean overlap(Selector sel){
		if(this.entry.equals(sel.entry)) {
			if(this.range.overlap(sel.range)) {
				return true;
			}
		}
		return false;
	}
	
/**
 * Draws Selector to Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
// 		Color oc = g.getColor();
		g.setColor(fillColor);
		zoom = coord.zoom;
		canvWidth = (int) Math.round(range.length() * zoom);
		canvY = coord.originY - canvHeight/2;
		if (coord.originX >= 0) {
			canvX =  coord.originX + (int) Math.round((range.begin - 1)*zoom); // To get selection of first base right??
		} else {
			canvX =  coord.originX + (int) Math.round((range.begin)*zoom);
		}
		boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
// 		System.out.println("selector width:" + canvWidth);
		g.drawRect(canvX, canvY, canvWidth, canvHeight);
// 		g.setColor(oc);
	}
	
/**
 * String representation of Selector
 * @return string representation
 */
	public String toString() {
		return entry + " "+ range;
	}
	
}
