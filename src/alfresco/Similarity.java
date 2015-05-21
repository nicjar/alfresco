/*
* $Revision: 1.1 $
* $Id: Similarity.java,v 1.1 2003/04/04 10:15:11 niclas Exp $
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
import java.awt.*;
/**
 * Glyph that represents a similarity e.g. Blast
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Similarity extends SeqFeature {
/**
 * match description
 */
	String match;
	
	/**
	 * Creates Similarity of specified Entry with specified range
	 * @param ent Entry the Similarity belongs to
	 * @param r the range
	 */
	public Similarity (Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.yellow;
		darker(1);
		canvHeight = 8;
		if (tier > 0) {
			tier = 9;
		} else {
			tier = -9;
		}
	}
	
	/**
	 * Sets the match description
	 * @param m match description
	 */
	public void setMatch(String m) {
		match = m;
	}
	
/**
 * Draws Similarity to Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.black);
		g.drawRect(canvX, canvY, canvWidth, canvHeight);
	}
	
	
/**
 * String representation of SeqFeature
 * @return string representation
 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Similarity" + " " + entry + " "+ range);
		sb.append(", " + predMethod);
		if (match != null) {
			sb.append(" " + match);
		}
		sb.append(" Score: " + score);
		return new String(sb);
	}

/**
 * Gets a gff line description of the feature
 * @return gff line
 */
	public String gffString() {
		return super.gffString() + " " + match;
	}

}
