/*
* $Revision: 1.1 $
* $Id: HCR.java,v 1.1 2003/04/04 10:14:33 niclas Exp $
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
 * Glyph that represents a Highly Conserved Region (HCR) range
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class HCR extends SeqFeature {
	static Color defColor = new Color(0, 170, 255);
	static Color aColor = new Color(0, 132, 223);
	static Color bColor = new Color(0, 114, 170);
	static Color cColor = new Color(0, 75, 108);
	static Color dColor = new Color(0, 57, 85);
	
/**
 * The alignment block associated with the HCR
 */
	AlignmentBlock block;
/**
 * Dbb level, or 0	
 */
	int level = -1;
/**
 * %id
 */
	double id = -1;
/**
 * no. of gaps	
 */
	int gaps = -1;
	
/**
 * Creates HCR of specified Entry with specified range on the upper strand
 * @param ent HCR the Exon belongs to
 * @param r the range
 */
	public HCR(Entry ent, SeqRange r) {
		super(ent, r);
		setGestalt();
	}
		
/**
 * Creates HCR of specified Entry with specified range on the strand
 * specified by compl
 * @param ent Entry the HCR belongs to
 * @param r the range
 * @param compl true if HCR is on lower strand, otherwise false
 */
	public HCR(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		setGestalt();
	}
	
/**
 * Creates HCR of specified Entry with specified range on the upper strand
 * @param ent HCR the Exon belongs to
 * @param r the range
 */
	public HCR(Entry ent, SeqRange r, int level) {
		super(ent, r);
		this.level = level;
		setGestalt();
	}
	
	/**
	 * Sets the look of the HCR depending on level
	 */
	private void setGestalt() {
		if(level >= 90) {
			fillColor = dColor;
			canvHeight = 13;
		} else if (level >= 80) {
			fillColor = cColor;
			canvHeight = 11;
		} else if (level >= 70) {
			fillColor = bColor;
			canvHeight = 9;
		} else if (level >= 60) {
			fillColor = aColor;
			canvHeight = 7;
		} else {
			fillColor = defColor;
			canvHeight = 7;
		}
	}
	
	/**
	 * Sets the AlignmentBlock associated with this HCR
	 * @param bl alignment block
	 */	
	public void setBlock(AlignmentBlock bl) {
		block = bl;
		level = block.getLevel();
		id = block.getIdentity();
		gaps = block.getGaps();
		setGestalt();
	}

		/**
	 * Gets the AlignmentBlock associated with this HCR
	 * @return alignment block
	 */	
	public AlignmentBlock getBlock() {
		return block;
	}
	
	/**
	 * Sets level
	 * @param l dbb level
	 */
	public void setLevel(int l) {
		level = l;
		setGestalt();
	}

	/**
	 * Gets level
	 * @return level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets id
	 * @param i %id
	 */
	public void setId(double i) {
		id = i;
	}

	/**
	 * Gets id
	 * @return %id
	 */
	public double getId() {
		return id;
	}

	/**
	 * Sets gaps
	 * @param l gaps
	 */
	public void setGaps(int g) {
		gaps = g;
	}

	/**
	 * Gets gaps
	 * @return gaps
	 */
	public int getGaps() {
		return gaps;
	}

/**
 * Gets a gff line description of the feature
 * @return gff line
 */
	public String gffString() {
		StringBuffer sb = new StringBuffer(super.gffString());
		if (level >= 0) {
			sb.append("\t" + level + ",");
		} else {
			sb.append("\t0,");
		}
		
		if (id >= 0) {
			sb.append(id + ",");
		} else {
			sb.append("0,");
		}
		
		if (gaps >= 0) {
			sb.append(gaps);
		} else {
			sb.append("0");
		}
		
		return new String(sb);
	}
	
/**
 * String representation of HCR
 * @return string representation
 */
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		if (id >= 0)
			sb.append(", Id: " + id + "%");
		if (gaps >= 0)
			sb.append(" Gaps: " + gaps); 
		return new String(sb);
	}

	
		
/**
 * Gets the boundingRect of the SeqFeature
 * @return Rectangle defining the bounds of the SeqFeature
 */
 	public Rectangle getBounds() {
		if (tier == coord.entryTier) {
			canvY = coord.originY - canvHeight/2;
		} else {
			int diff = coord.tierDiff(tier);
			if (tier > 0) { 
				if (!complement) {
					canvY = coord.originY - canvHeight - diff; 
				} else {
					canvY = coord.originY - diff;
				}
			} else if (tier < 0) { 
				if (!complement) {
					canvY = coord.originY - canvHeight + diff; 
				} else {
					canvY = coord.originY + diff;
				}
			}
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
			return new Rectangle(canvX, canvY, canvWidth, canvHeight);
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
		return boundingRect;
	}
	
}
