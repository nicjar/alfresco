/*
 * $Revision: 1.1 $
 * $Id: Promoter.java,v 1.1 2003/04/04 10:14:47 niclas Exp $
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
 * Promoter represents the promoter elements of a gene. Children are TrxStart,
 * TATA, and TFBS objects.<p>
 * @see alfresco.CompositeSeqFeature
 * @see alfresco.Exon
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Promoter extends CompositeSeqFeature {
/**
 * Transcription start glyph
 */
	TrxStart start;

/**
 * Constructs Promoter without specifying an Entry	
 */
	Promoter() {
		super();
		fillColor = Color.magenta;
	}

/**
 * Construct Promoter
 * @param ent parent Entry
 */
	Promoter(Entry ent) {
		super(ent);
		fillColor = Color.magenta;
	}
	
/**
 * Construct Promoter 
 * @param ent parent Entry
 * @param children Vector of child objects (Exon)
 */
	Promoter(Entry ent, Vector children) {
		super(ent, children);
		fillColor = Color.magenta;
	}
	
	/**
	 * Gets the TrxStart Glyph
	 * @return TrxStart object
	 */
	public TrxStart getTrxStart() {
		if (start == null) {
			Enumeration kids = getChildEnumeration();
			while (kids.hasMoreElements()) {
				Object sf = kids.nextElement();
				if (sf instanceof TrxStart) {
					return start = (TrxStart) sf;
				}
			}
		}
		return start;
	}

/**
 * Draws Promoter and child Glyphs to Graphics specified.<p>
 * Drawing behavior might depend on zoomlevel
 * for now just call default drawing and draw an outline around boundingRect
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		if (coord.zoom > 0.8) {
			super.draw(g);
			g.setColor(fillColor);
			boundingRect = getBounds();
			if (boundingRect == null) return; 
			g.drawRect(boundingRect.x, boundingRect.y, boundingRect.width, boundingRect.height);
		} else {
			TrxStart st = getTrxStart();
			if (st != null)
				st.draw(g);
		}
	}
	
// /**
//  * String representation of Promoter
//  * @return string representation
//  */
// 	public String toString() {
// 		return "Promoter: " + name;
// 	}
	
// /**
//  * If x, y is within boundingRect returns the SeqFeature clicked or parent Entry.
//  * @return The Glyph clicked
//  * @param x mouse x position
//  * @param y mouse y position
//  */	
// 	public Glyph clicked(int x, int y) {
// 		Glyph g = super.clicked(x, y);
// 		if (g != null && g.equals(this)) { return this.parent; }
// 		return g;
// 	}
}

