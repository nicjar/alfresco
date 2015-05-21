/*
 * $Revision: 1.1 $
 * $Id: CoordVal.java,v 1.1 2003/04/04 10:13:58 niclas Exp $
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
import java.util.*;
import java.io.*;

/**
 * Holds information about the canvas an Entry (and children) will be draw to,
 * as well as offset information about the Entry<p>
 * A CoordVal is initially set for each Entry. All children uses the CoordVal to
 * determine were they should draw themselfs.
 * @see java.io.Serializable
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class CoordVal implements Serializable{
/**
 * Maximum number of tiers: -MAXTIERS - MAXTIERS
 */
	static int MAXTIERS = 10;

/**
 * Drawing Canvas
 */
	Canvas canv;

/**
 * Zoom level of Entry
 */
	double zoom;

/**
 * Width of drawing Canvas
 */
	int	canvWidth,

/**
 * x of entry
 */
		originX;

/**
 * Height of drawing Canvas
 */
	int	canvHeight,
	
/**
 * y of entry
 */ 
		originY,

/**
 * Tier of Entry
 */
		entryTier;
	
/**
 * Creates a CoordVal with the specified Canvas and tier
 * @param c drawing Canvas
 * @param tier tier of Entry
 */
 	CoordVal(Canvas c, int tier) {
		canv = c;
		entryTier = tier;
		Dimension d = canv.getSize();
		canvWidth = d.width;
		canvHeight = d.height;
		int centerY = canvHeight/2;
		calcMaxTiers();
		originY = centerY - centerY * entryTier/(MAXTIERS+1);
	}
	
// /**
//  * Sets the width and height for the CoordVal
//  * @param d size Dimension
//  */
//  	void setSize(Dimension d) {
// 		canvWidth = d.width;
// 		canvHeight = d.height;
// 		int centerY = canvHeight/2;
// 		originY = centerY - centerY * entryTier/(MAXTIERS+1);		
// 	}
	
/**
 * Updates the width and height, and y pos
 */
 	void updateSize() {
		Dimension d = canv.getSize();
		canvWidth = d.width;
		canvHeight = d.height;		
		int centerY = canvHeight/2;
		calcMaxTiers();
		originY = centerY - centerY * entryTier/(MAXTIERS+1);		
	}
	
	/**
	 * Calculates MAXTIERS dependent on canvas size
	 */
	private void calcMaxTiers() {
		MAXTIERS = (canvHeight/2) / 25;
	}
	
/**
 * Sets x value
 * @param x new x value
 */
 	void setX(int x) {
		originX = x;
	}
	
/**
 * Gets x value
 * @return the x value
 */
 	int getX() {
		return originX;
	}
	
/**
 * Gets the center x position of drawing Canvas
 * @return center x of Canvas
 */
 	int getCanvCenterX () {
		return canvWidth/2;
	}
	
/**
 * Centers on position specified
 * @param seqPos position to center on
 */
 	public void centerXOn (int seqPos) {
		originX = getCanvCenterX() - (int) Math.round(seqPos*zoom);
	}
	
/**
 * Gets the center x position of Entry
 * @return center x position
 */
 	public int getPosInCenter() {
		return (int) ((-originX + getCanvCenterX()) / zoom);
	}
	
/**
 * Gets absolute difference of Entry and tier specified
 * @return abs tier diference
 * @param otherTier other tier
 */
 	public int tierDiff(int otherTier) {
		return Math.abs(Math.abs(otherTier)-Math.abs(entryTier))*((canvHeight/2)/(MAXTIERS+1));
	}
}
