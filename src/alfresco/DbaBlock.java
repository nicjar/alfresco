/*
* $Revision: 1.1 $
* $Id: DbaBlock.java,v 1.1 2003/04/04 10:14:04 niclas Exp $
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
 * Glyph that represents a dba range
 * @see alfresco.SeqFeature
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class DbaBlock extends SeqFeature {

	AlignmentBlock block;
/**
 * Creates DbaBlock of specified Entry with specified range on the upper strand
 * @param ent DbaBlock the Exon belongs to
 * @param r the range
 */
	DbaBlock(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.blue;
		canvHeight = 8;
	}
		
/**
 * Creates DbaBlock of specified Entry with specified range on the strand
 * specified by compl
 * @param ent Entry the DbaBlock belongs to
 * @param r the range
 * @param compl true if DbaBlock is on lower strand, otherwise false
 */
	DbaBlock(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.blue;
		canvHeight = 8;
	}
	
	/**
	 * Sets the AlignmentBlock associated with this DbaBlock
	 * @param bl alignment block
	 */	
	public void setBlock(AlignmentBlock bl) {
		block = bl;
	}

		/**
	 * Gets the AlignmentBlock associated with this DbaBlock
	 * @return alignment block
	 */	
	public AlignmentBlock getBlock() {
		return block;
	}
}
