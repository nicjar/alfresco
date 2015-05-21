/*
 * $Revision: 1.1 $
 * $Id: TATA.java,v 1.1 2003/04/04 10:15:15 niclas Exp $
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
 * Glyph that represents a TATA box
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class TATA extends SeqFeature {

/**
 * Creates a TATA of the specified Entry, with the specified start and stop
 * on the upper strand
 * @param entry parent Entry
 * @param start TATA start
 * @param stop TATA stop
 */
	TATA(Entry ent, int start, int stop) {
		super(ent, start, stop);
		fillColor = Color.orange;
		canvHeight = 10;
		darker(1);
	}
	
/**
 * Creates a TATA of the specified Entry, with the specified range
 * on the upper strand
 * @param entry parent Entry
 * @param r range of TATA
 */
	TATA(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.orange;
		canvHeight = 10;
		darker(1);
	}
	
/**
 * Creates a TATA of the specified Entry, with the specified start and stop,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param start TATA start
 * @param stop TATA stop
 */
	TATA(Entry ent, int start, int stop, boolean compl) {
		super(ent, start, stop, compl);
		fillColor = Color.orange;
		canvHeight = 10;
		darker(1);
	}
	
/**
 * Creates a TATA of the specified Entry, with the specified range,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param r range of TATA
 */
	TATA(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.orange;
		canvHeight = 10;
		darker(1);
	}
	
/**
	 * Gets the gene that the TATA belongs to
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
		super.draw(g);
		g.setColor(Color.blue);
		g.drawRect(canvX, canvY, canvWidth, canvHeight);
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

