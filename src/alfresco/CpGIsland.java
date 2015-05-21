/*
 * $Revision: 1.1 $
 * $Id: CpGIsland.java,v 1.1 2003/04/04 10:14:01 niclas Exp $
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
 * A Glyph to represent CpG islands.<p>
 * CpGIslands are by default drawn one tier away from the Entry.
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class CpGIsland extends SeqFeature {
/**
 * Contains info about GC content and o/e ratio
 */
	String info;
	
/**
 * Creates new CpGIsland of the specified Entry, and with the specified start and stop
 * @param ent parent Entry
 * @param start start of CpG island
 * @param stop stop of CpG island
 */
 	CpGIsland(Entry ent, int start, int stop) {
		super(ent, start, stop, false);
		fillColor = Color.yellow;
		canvHeight = 8;
		if (ent.getTier() > 0) {
			tier = 2;
		} else {
			tier = -2;
		}
	}
	
/**
 * Creates new CpGIsland of the specified Entry, and with the specified SeqRange
 * @param ent parent Entry
 * @param r range of CpG island
 */
	CpGIsland(Entry ent, SeqRange r) {
		super(ent, r, false);
		fillColor = Color.yellow;
		canvHeight = 8;
		if (ent.getTier() > 0) {
			tier = 2;
		} else {
			tier = -2;
		}
	}
	
/**
 * Sets the info String. (Called from CpG.parse())
 * @param inf info string
 */
 	public void setInfo(String inf) {
		this.info = inf;
	}
	
/**
 * Draws the Glyph to the Graphics object specified
 * @param g Graphics obj to draw to
 */
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.black);
		g.drawRect(canvX, canvY, canvWidth, canvHeight);
	}
	
/**
 * Converts an object to a String
 * @return the object description
 */
	public String toString() {
		if (info != null) {
			return info + " " + range;
		}
		return super.toString();
	}
}

