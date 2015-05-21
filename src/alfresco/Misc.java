/*
* $Revision: 1.1 $
* $Id: Misc.java,v 1.1 2003/04/04 10:14:41 niclas Exp $
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
 * Glyph that represents a miscellanious SeqFeature
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Misc extends SeqFeature {
/**
 * type name	
 */
	String type;
	
	/**
	 * Creates Misc of specified Entry with specified range
	 * @param ent Entry the Misc belongs to
	 * @param r the range
	 */
	public Misc(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.magenta;
		darker(1);
		canvHeight = 6;
		if (tier > 0) {
			tier = 8;
		} else {
			tier = -8;
		}
	}
	
	/**
	 * Sets type string
	 * @param type type name
	 */
	public void setType(String type) {
		this.type = type;
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
 * String representation of Misc
 * @return string representation
 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.isPredicted()) sb.append("Predicted ");
		sb.append("Misc. feature: " + type + " " + entry + " "+ range);
		if (score > 0) sb.append(", Prob: " + score);
		return new String(sb);
	}
}
