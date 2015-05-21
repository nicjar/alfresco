/*
* $Revision: 1.1 $
* $Id: DotterRegion.java,v 1.1 2003/04/04 10:14:10 niclas Exp $
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
 * A glyph to represent a range similar to some other range.<P>
 * The ranges are defined by a result file from dotter.
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class DotterRegion extends SeqFeature {

/**
 * Creates DotterRegion of the specified Entry with the specified range
 * @parm ent The entry the region belongs to.
 * @param r A SeqRange object describing the region.
 */
	DotterRegion(Entry ent, SeqRange r) {
		super(ent, r, false);
		fillColor = Color.darkGray;
		canvHeight = 7;
// 		coord = entry.coord;
		
	}

/**
 * Evaluates if the DotterRegion equals another object
 * @return true if DotterRegion equals object, otherwise false
 * @param obj The object to evaluate	
 */
	public boolean equals(Object obj) {
		if (obj instanceof DotterRegion) {
			DotterRegion reg = (DotterRegion) obj;
			if (this.entry.equals(reg.entry) && this.range.equals(reg.range) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Draws the DotterRegion to the Graphics specified
	 * @param g Graphics to draw to
	 */
	public void draw(Graphics g) {
		if (visible) {
	// 		System.out.println("drawing SeqFeature " + this);
	// 		System.out.println(coord + " zoom: " + zoom);
			if (tier == coord.entryTier) {
// 				if (!complement) {
// 					canvY = coord.originY - canvHeight;
// 				} else {
// 					canvY = coord.originY;
// 				}
				canvY = coord.originY - canvHeight/2;
			} else {
				int diff = coord.tierDiff(tier);
				if (tier > 0) { canvY = coord.originY - canvHeight/2 - diff; }
				else if (tier < 0) { canvY = coord.originY - canvHeight/2 + diff; }
				else { System.out.println("Kablooeeyh! Tier is 0!"); }
			}
			zoom = coord.zoom;

			realX =  coord.originX + (int) Math.round(range.begin*zoom);
			realWidth = (int) (range.length() * zoom);
			if (realWidth == 0) { realWidth = 1; }
			realXend = realX + realWidth;
			if (realX > ABSXMAX || realXend < -ABSXMAX) { //outside drawing area
				if (realX > ABSXMAX) {
					canvX = ABSXMAX;
				} else {
					canvX = -(ABSXMAX + 2);
				}
				canvWidth = 1;
				boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
				return; 
			}
			if (realX < -ABSXMAX) {
				canvX = -ABSXMAX;
			} else {
				canvX = realX;
			}
			if (realXend > ABSXMAX) {
				canvWidth = ABSXMAX - canvX;
			} else {
				canvWidth = realXend - canvX;
			}
			
			boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
			g.setColor(fillColor);
			g.fillRect(canvX, canvY, canvWidth, canvHeight);
			g.setColor(Color.black);
			g.drawRect(canvX, canvY, canvWidth, canvHeight);
		}


	}
	
// 	public String toString() {
// 		return " DotterRegion " + range + " " + entry + " " + fillColor;
// 	}
}
