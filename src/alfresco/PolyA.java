/*
 * $Revision: 1.1 $
 * $Id: PolyA.java,v 1.1 2003/04/04 10:14:46 niclas Exp $
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
 * Glyph that represents a polyA signal
 * @see alfresco.SeqFeature 
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class PolyA extends SeqFeature  {
	/**
	 * Creates PolyA of specified Entry with specified range
	 * @param ent Entry the PolyA belongs to
	 * @param r the range
	 */
	public PolyA(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.orange;
		canvHeight = 10;
		
	}
}
