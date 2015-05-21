/*
* $Revision: 1.1 $
* $Id: UsefulConstants.java,v 1.1 2003/04/04 10:15:24 niclas Exp $
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
import java.io.*;
import java.awt.*;

/**
 * Interface defining some constants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public interface UsefulConstants {
/**
 * Maximum sequence length	
 */
	public static final int MAXSEQLEN = 500000;
	//public static final int MAXPOS = MAXSEQLEN/256;

/**
 * End of file
 */
	public static final int EOF = -1;
	
// 	public static final PrintStream out = System.out;
// 	public static final DataInputStream in = new DataInputStream( System.in);

/**
 * Absolute number of horizontal pixels that should drawn on Canvas
 */
	public static final int ABSXMAX = 5000;
	
// /**
//  * Dba default match parameter
//  */
// 	public static final int DBAMATCH = 2;
// /**
//  * Dba default mismatch parameter
//  */
// 	public static final int DBAMISMATCH = -3;
// /**
//  * Dba default gap parameter
//  */
// 	public static final int DBAGAP = -6;
// /**
//  * Dba default blockopen parameter
//  */
// 	public static final int DBABLOCKOPEN = -24;
// /**
//  * NewDba default match parameter
//  */
// 	public static final double NEWDBAMATCH = 0.8;
// /**
//  * NewDba default mismatch parameter
//  */
// 	public static final double NEWDBAUMATCH = 0.99;
// /**
//  * NewDba default gap parameter
//  */
// 	public static final double NEWDBAGAP = 0.05;
// /**
//  * NewDba default blockopen parameter
//  */
// 	public static final double NEWDBABLOCKOPEN = 0.01;
// 	
/**
 * Dba default matchA parameter
 */
	public static final double DBAMATCH_A = 0.65;
/**
 * Dba default matchB parameter
 */
	public static final double DBAMATCH_B = 0.75;
/**
 * Dba default matchC parameter
 */
	public static final double DBAMATCH_C = 0.85;
/**
 * Dba default matchD parameter
 */
	public static final double DBAMATCH_D = 0.95;
/**
 * Dba default mismatch parameter
 */
	public static final double DBAUMATCH = 0.99;
/**
 * Dba default gap parameter
 */
	public static final double DBAGAP = 0.05;
/**
 * Dba default blockopen parameter
 */
	public static final double DBABLOCKOPEN = 0.01;
// /**
//  * Font for drawing dna sequence	
//  */
// 	public static final Font DNAFONT = new Font("Monospaced", Font.BOLD, 12);
	
}
