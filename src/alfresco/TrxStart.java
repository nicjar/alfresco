/*
 * $Revision: 1.1 $
 * $Id: TrxStart.java,v 1.1 2003/04/04 10:15:21 niclas Exp $
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
 * Glyph that represents a transcription start site
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class TrxStart extends SeqFeature {

/**
 * Creates a TrxStart of the specified Entry, with the specified start and stop
 * on the upper strand
 * @param entry parent Entry
 * @param start TrxStart start
 * @param stop TrxStart stop
 */
	TrxStart(Entry ent, int start, int stop) {
		super(ent, start, stop);
		fillColor = Color.black;
		canvHeight = 6;
		darker(2);
	}
	
/**
 * Creates a TrxStart of the specified Entry, with the specified range
 * on the upper strand
 * @param entry parent Entry
 * @param r range of TrxStart
 */
	TrxStart(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.black;
		canvHeight = 6;
		darker(2);
	}
	
/**
 * Creates a TrxStart of the specified Entry, with the specified start and stop,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param start TrxStart start
 * @param stop TrxStart stop
 */
	TrxStart(Entry ent, int start, int stop, boolean compl) {
		super(ent, start, stop, compl);
		fillColor = Color.black;
		canvHeight = 6;
		darker(2);
	}
	
/**
 * Creates a TrxStart of the specified Entry, with the specified range,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param r range of TrxStart
 */
	TrxStart(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.black;
		canvHeight = 6;
		darker(2);
	}
	
/**
	 * Gets the gene that the TFBS belongs to
	 * @return gene
	 */
	public Gene getGene() {
		Glyph parent = getParent();
		if(parent != null && parent instanceof Promoter) {
			Glyph grandparent = parent.getParent();
			if (grandparent != null && grandparent instanceof Gene) {
				return (Gene) grandparent;
			}
		}
		return null;
	}
	
/**
 * Draws the Glyph to the Graphics object specified
 * @param g Graphics obj to draw to
 */
	public void draw(Graphics g) {
// 		super.draw(g);
// 		g.setColor(Color.blue);
// 		g.drawRect(canvX, canvY, canvWidth, canvHeight);
		if (visible) {
	// 		System.out.println("drawing SeqFeature " + this);
	// 		System.out.println(coord + " zoom: " + zoom);
			if (tier == coord.entryTier) {
// 				if (!complement) {
// 					canvY = coord.originY - canvHeight;
// 				} else {
// 					canvY = coord.originY;
// 				}
				canvY = coord.originY;
			} else {
				int diff = coord.tierDiff(tier);
// 				if (tier > 0) { canvY = coord.originY - canvHeight/2 - diff; }
// 				else if (tier < 0) { canvY = coord.originY - canvHeight/2 + diff; }
				if (tier > 0) { canvY = coord.originY  - diff; }
				else if (tier < 0) { canvY = coord.originY  + diff; }
				else { System.out.println("Kablooeeyh! Tier is 0!"); }
			}
			zoom = coord.zoom;
// 			canvX =  coord.originX + (int) Math.round(range.begin*zoom);
// 			canvWidth = (int) (range.length() * zoom);
// 			if (canvWidth == 0) { canvWidth = 1; }

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
// 				canvX = realX;	// this will be caught by Reciprocal and not drawn, I hope...
// 				canvWidth = realWidth;
// 				boundingRect = null;
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
// 				canvWidth = realWidth;
				canvWidth = realXend - canvX;
			}
			if (complement) {
				boundingRect = new Rectangle(canvX, canvY-10, 5, 5);
			} else {
				boundingRect = new Rectangle(canvX, canvY+10, 5, 5);
			}
			g.setColor(fillColor);
			g.drawLine(canvX, canvY, canvX, canvY - 15);	// |
			g.drawLine(canvX, canvY - 15, canvX + 10, canvY - 15);	// -
			g.drawLine(canvX + 10, canvY - 15, canvX + 10 - 5, canvY - 15 - 5);	// \
			g.drawLine(canvX + 10, canvY - 15, canvX + 10 - 5, canvY - 15 + 5);	// /
			
// 			g.fillRect(canvX, canvY, canvWidth, canvHeight);
		}
		
	}
	
/**
 * Gets a gff line description of the feature
 * @return gff line
 */
	public String gffString() {
		Gene gene = getGene();
		if (gene != null) {
			return super.gffString() + "\t" + gene.getName();
		}
		return super.gffString();
	}
		
	
}

