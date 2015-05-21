/*
 * $Revision: 1.1 $
 * $Id: Intron.java,v 1.1 2003/04/04 10:14:34 niclas Exp $
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
 * Glyph that represents an intron
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Intron extends SeqFeature {
  
  static final long serialVersionUID = -7386334359241968405L;
/**
 * Creates Intron of specified Entry with specified range on the upper strand
 * @param ent Entry the Intron belongs to
 * @param r the range
 */
	Intron(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.green;
		brighter(5);
		canvHeight = 4;
		visible = false;
	}
		
/**
 * Creates Intron of specified Entry with specified range on the strand
 * specified by compl
 * @param ent Entry the Intron belongs to
 * @param r the range
 * @param compl true if Intron is on lower strand, otherwise false
 */
	Intron(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.green;
		brighter(5);
		canvHeight = 4;
		visible = false;
	}
	
	/**
	 * Gets the gene that the Intron belongs to
	 * @return gene
	 */
	public Gene getGene() {
		Glyph parent = getParent();
		if(parent != null && parent instanceof Gene) {
			return (Gene) parent;
		}
		return null;
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
		
	/**
 * Gets the boundingRect of the SeqFeature
 * @return Rectangle defining the bounds of the SeqFeature
 */
 	public Rectangle getBounds() {
			if (tier == coord.entryTier) {
				if (!complement) {
					canvY = coord.originY - canvHeight;
				} else {
					canvY = coord.originY;
				}
	// 			canvY = coord.originY - canvHeight/2;
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
      if(!complement) {
        canvY -= 3; // to center on exons
      } else {
        canvY += 3; // to center on exons
      }
			zoom = coord.zoom;

			realX =  coord.originX + (int) Math.round(range.begin*zoom);
			realWidth = (int) Math.round(range.length() * zoom);
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
// 				boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
// 				return;
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
// 				canvWidth = realWidth;
				canvWidth = realXend - canvX;
			}
			
			boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
			return boundingRect;
// 			return new Rectangle(canvX, canvY, canvWidth, canvHeight);
		
	}

	
}

