/*
 * $Revision: 1.1 $
 * $Id: Gene.java,v 1.1 2003/04/04 10:14:23 niclas Exp $
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
 * Glyph representing a Gene<p>
 * Children are SeqFeature or CompositeSeqFeature objects
 * @see alfresco.CompositeSeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Gene extends CompositeSeqFeature {
/**
 * Name of gene
 */
	String name;
	
/**
 * Creates Gene with specified name
 * @param n gene name
 */
	Gene(String n) {
		super();
		name = n;
		fillColor = Color.blue;
	}

/**
 * Creates Gene of specified Entry with specified name
 * @param ent entry the Gene belongs to
 * @param n gene name
 */
	Gene(Entry ent, String n) {
		super(ent);
		name = n;
		fillColor = Color.blue;
		}

/**
 * Creates Gene of specified Entry with specified name and children
 * @param ent entry the Gene belongs to
 * @param n gene name
 * @param children Vector of child Glyphs
 */
	Gene(Entry ent, String n, Vector children) {
		super(ent, children);
		name = n;
		fillColor = Color.blue;
	}
	
	/**
	 * Gets gene name
	 * @return gene name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets gene name
	 * @param gname gene name
	 */
	public void setName(String gname) {
		name = gname;
	}

	/**
 * Adds a Glyph to the Vector of children. Exons are automatically added
 * to the CDS of the entry
 * @param gl the Glyph to be added
 */
	public void addChild(Glyph gl) {
		if (gl instanceof Exon) {
			getCDS().addChild(gl);
		} else {
			super.addChild(gl);
		}
	}
	
	/**
	 * Gets the CDS of the Gene. If the Gene hasn't got a CDS a new is created.
	 * @return CDS object
	 */
	public CDS getCDS() {
		Enumeration kids = children.elements();
		while(kids.hasMoreElements()) {
			Object o = kids.nextElement();
			if (o instanceof CDS) return (CDS) o;
		}
		CDS newCDS = new CDS(entry);
		addChild(newCDS);
		return newCDS;
	}
	
	/**
 * Confirms exons of the CDS of the Gene
 * @param conf true if Exons should be confirmed, otherwise false
 */
	public void confirmExons(boolean conf) {
    CDS c = getCDS();
    Vector exons = c.getChildren();
    Enumeration exen = exons.elements();
    while(exen.hasMoreElements()) {
      Exon e = (Exon) exen.nextElement();
      e.confirmed(conf);
    }
	}
  
	
	/**
	 * Checks if gene overlaps another gene
	 * @return true if genes are overlapping
	 * @param g other gene
	 */
	public boolean overlap(Gene g) {
// 		System.out.println(this + " " + this.getSeqRange() + ", " +
// 				               g + " " + g.getSeqRange());
		if (this.getSeqRange().overlap(g.getSeqRange())) return true;
		return false;
	}
	
/**
 * Draws Gene and child Glyphs to Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		// drawing behavior might depend on zoomlevel
		// for now just call default drawing and draw an outline around boundingRect
		// and the gene name
		super.draw(g);
		g.setColor(fillColor);
		boundingRect = getBounds();
		if (boundingRect == null) { return; }
		g.drawRect(boundingRect.x, boundingRect.y, boundingRect.width, boundingRect.height);
    Font f = new Font("Dialog", Font.PLAIN, 12);
//     Font f = Font.getFont("Dialog");
//     Font f = g.getFont();
//     System.out.println("Font: " + f);
    g.setFont(f);
		if (tier > 0) {
			if (complement) {
				g.drawString("<"+name, boundingRect.x, boundingRect.y-2);
			} else {
				g.drawString(name+">", boundingRect.x, boundingRect.y-2);
			}
		} else {
			if (complement) {
				g.drawString("<"+name, boundingRect.x, boundingRect.y+boundingRect.height+12);
			} else {
				g.drawString(name+">", boundingRect.x, boundingRect.y+boundingRect.height+12);
			}
		}
	}
	
/**
 * String representation of Gene
 * @return string representation
 */
	public String toString() {
		return "Gene: " + name;
	}
	
	
}

