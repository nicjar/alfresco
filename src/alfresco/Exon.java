/*
 * $Revision: 1.1 $
 * $Id: Exon.java,v 1.1 2003/04/04 10:14:19 niclas Exp $
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
 * Glyph that represents an coding exon
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Exon extends SeqFeature {
	
  boolean confirmed;
  
/**
 * Creates Exon of specified Entry with specified range on the upper strand
 * @param ent Entry the Exon belongs to
 * @param r the range
 */
	Exon(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.green;
    confirmed = true;
		darker(2);
		canvHeight = 10;
	}
		
/**
 * Creates Exon of specified Entry with specified range on the strand
 * specified by compl
 * @param ent Entry the Exon belongs to
 * @param r the range
 * @param compl true if Exon is on lower strand, otherwise false
 */
	Exon(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.green;
    confirmed = true;
		darker(2);
		canvHeight = 10;
	}
	
	/**
	 * Gets the gene that the Exon belongs to
	 * @return gene
	 */
	public Gene getGene() {
		Glyph parent = getParent();
		if(parent != null && parent instanceof CDS) {
			Glyph grandparent = parent.getParent();
			if (grandparent != null && grandparent instanceof Gene) {
				return (Gene) grandparent;
			}
		}
		return null;
	}
  
/**
 * Sets whether the SeqFeature is predicted or not
 * @param pred true if SeqFeature is predicted, otherwise false
 */	
	public void predicted(boolean pred) {
 		predicted = pred;
    confirmed = !predicted;
    brighter(2);
	}	

/**
 * Sets whether the SeqFeature is confirmed or not
 * @param pred true if SeqFeature is confirmed, otherwise false
 */	
	public void confirmed(boolean conf) {
    this.confirmed = conf;
//     System.out.println(confirmed?"":"un" + "confirming exon");
    origColor = Color.green;
    if (this.confirmed) {
      origColor = origColor.darker();
      origColor = origColor.darker();
    }
    fillColor = origColor;
	}	

/**
 * Determines if SeqFeature is predicted or not
 * @return true if SeqFeature is predicted, otherwise false
 */	
	public boolean isConfirmed() {
		return confirmed;
	}
// /**
//  * String representation of SeqFeature
//  * @return string representation
//  */
// 	public String toString() {
//     return super.toString() + (confirmed?", confirmed":", unconfirmed");
//   }
	
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
		
// 	public void draw(Graphics g) {
// 		//System.out.println("Drawing exon\nParent: " + parent);
// // 		zoom = parent.getZoom();
// 		super.draw(g);
// 		//System.out.println("zoom: " + zoom + " tier: " + tier);
// // 		// System.out.println(range);
// // 		canvWidth = range.length()/zoom;
// // 		canvY = parent.canvY - canvHeight/2;
// // 		canvX = entry.canvX + range.begin/zoom;
// // 		boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
// // 		g.setColor(fillColor);
// // 		g.fillRect(canvX, canvY, canvWidth, canvHeight);
// 	}
}

