/*
 * $Revision: 1.2 $
 * $Id: CDS.java,v 1.2 2005/06/28 11:27:42 niclas Exp $
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
import alfresco.CodonTranslator;
// import CodonTranslator;

/**
 * CDS represents the coding sequence of a gene. Children are Exon objects.<p>
 * @see alfresco.CompositeSeqFeature
 * @see alfresco.Exon
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class CDS extends CompositeSeqFeature {

/**
 * Constructs CDS without specifying an Entry	
 */
	CDS() {
		super();
	}

/**
 * Construct CDS
 * @param ent parent Entry
 */
	CDS(Entry ent) {
		super(ent);
	}
	
/**
 * Construct CDS 
 * @param ent parent Entry
 * @param children Vector of child objects (Exon)
 */
	CDS(Entry ent, Vector children) {
		super(ent, children);
	}
	
	/**
	 * Gets the spliced sequence of the coding exons
	 * @return coding sequence string
	 */
	public String getCodingSequence() {
		Vector kids = sortChildren();
		Enumeration kidsen = kids.elements();
		StringBuffer sb = new StringBuffer();
		while (kidsen.hasMoreElements()){
			Exon e = (Exon) kidsen.nextElement();
			String s = e.getSequence();
			sb.append(s);
		}
		return new String(sb);
	}
	
	/**
	 * Copies the children vector and sorts the exons in ascending order on position.
	 * @retun Vector of sorted children
	 */
	private Vector sortChildren() {
		Vector kids = (Vector) children.clone();
		qsort(kids, 0, kids.size()-1);
		return kids;
	}
	
	
	/**
	 * Quick sort algorithm. Sorts an Vector of SeqFeatures in ascendig order 
	 * on the position
	 * @param list SequenceFeature vector
	 * @param left left index
	 * @param right right index
	 */
	private void qsort(Vector list, int left, int right) {
		int last;
		
		if (left >= right) return;
		
		swap(list, left, (left+right)/2);
		last = left;
		for (int i = left+1; i <= right; i++) {
			SeqRange ri = ((SeqFeature) list.elementAt(i)).getSeqRange();
			SeqRange rl = ((SeqFeature) list.elementAt(left)).getSeqRange();
			if (ri.getStart() < rl.getStart())
				swap(list, ++last, i);
			
		}
		swap(list, left, last);
		qsort(list, left, last-1);
		qsort(list, last+1, right);
	}
	
	/**
	 * Swaps the places of the objects of the specified indexes in the  
	 * SeqFeature vector
	 * @param SequenceFeature vector
	 * @param i index
	 * @param j index
	 */
	private void swap(Vector list, int i, int j) {
		Object tmp;
		tmp = list.elementAt(i);
		list.setElementAt(list.elementAt(j), i);
		list.setElementAt(tmp, j);
	}
	
	
	
	/**
	 * Gets the amino acid translation of the coding sequence
	 * @return amino acid sequence string
	 */
	public String getAminoAcidSequence() {
		alfresco.CodonTranslator ct = new alfresco.CodonTranslator(CodonTranslator.UNIVERSAL);
    char [] aachars = ct.translateSequenceToOneLetter(getCodingSequence()).toCharArray();
    for (int i = 0; i < aachars.length; i++) {
      if (aachars[i] == 'X') aachars[i] = '*';
    }
		return new String(aachars);
	}
/**
 * If x, y is within boundingRect returns the Exon clicked or parent Entry.
 * @return The Glyph clicked
 * @param x mouse x position
 * @param y mouse y position
 */	
	public Glyph clicked(int x, int y) {
		Glyph g = super.clicked(x, y);
		if (g != null && g.equals(this)) { return this.parent; }
		return g;
	}
}

