/*
 * $Revision: 1.1 $
 * $Id: TFBS.java,v 1.1 2003/04/04 10:15:16 niclas Exp $
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
 * Glyph that represents a transcription factor binding site
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class TFBS extends SeqFeature {
/**
 * TRANSFAC id
 */
	String id;
/**
 * consensus sequence
 */
	String consensus;
/**
 * Creates a TFBS of the specified Entry, with the specified start and stop
 * on the upper strand
 * @param entry parent Entry
 * @param start TFBS start
 * @param stop TFBS stop
 */
	TFBS(Entry ent, int start, int stop) {
		super(ent, start, stop);
		fillColor = Color.pink;
		canvHeight = 6;
		darker(1);
	}
	
/**
 * Creates a TFBS of the specified Entry, with the specified range
 * on the upper strand
 * @param entry parent Entry
 * @param r range of TFBS
 */
	TFBS(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.pink;
		canvHeight = 6;
		darker(1);
	}
	
/**
 * Creates a TFBS of the specified Entry, with the specified start and stop,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param start TFBS start
 * @param stop TFBS stop
 */
	TFBS(Entry ent, int start, int stop, boolean compl) {
		super(ent, start, stop, compl);
		fillColor = Color.pink;
		canvHeight = 6;
		darker(1);
	}
	
/**
 * Creates a TFBS of the specified Entry, with the specified range,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param r range of TFBS
 */
	TFBS(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.pink;
		canvHeight = 6;
		darker(1);
	}
	
/**
	* Gets the id of the TFBS 
	* @return id
	*/
	public String getId() {
		return id;
	}
/**
	* Sets the id of the TFBS 
	* @param id id string
	*/
	public void setId(String id) {
		this.id = id;
	}
	
/**
	* Gets the consensus sequence of the TFBS 
	* @return consensus sequence
	*/
	public String getConsensus() {
		return consensus;
	}
	
/**
	* Sets the consensus of the TFBS 
	* @param cons consensus string
	*/
	public void setConsensus(String cons) {
		this.consensus = cons;
	}
	
/**
	* Gets the gene that the TFBS belongs to
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
		if(visible) {
			g.setColor(Color.blue);
			g.drawRect(canvX, canvY, canvWidth, canvHeight);
		}
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
 * String representation of TFBS
 * @return string representation
 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.isPredicted()) sb.append("Predicted ");
		sb.append("TFBS: " + id + " " + entry + " "+ range);
		sb.append(", cons: " + consensus);
		return new String(sb);
	}
		
	
}

