/*
 * $Revision: 1.1 $
 * $Id: DotterFeatureFile.java,v 1.1 2003/04/04 10:14:07 niclas Exp $
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

/**
 * Class to generate dotter feature files.<P>
 * @see java.io.File
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class DotterFeatureFile extends File implements UsefulConstants{
/**
 * Source of first set of features
 */
	Entry firstEntry;

/**
 * Source of second set of features
 */
	Entry secondEntry;
	
/**
 * Creates a DotterFeatureFile with the directory path and name specified
 * @param path directory path of file
 * @param filename name of file
 * @param firstEnt first Entry
 * @param secondEnt second Entry
 */
	DotterFeatureFile (String path, String filename, Entry firstEnt, Entry secondEnt) {
		super(path, filename);
		firstEntry = firstEnt;
		secondEntry = secondEnt;
	}
	
/**
 * Gets the feature information from the two Entries end writes to file
 */
	public void write() {
		FileWriter fw = null;
		try{
			fw = new FileWriter(this);
			fw.write("# dotter feature format 2\n");
			Enumeration cdsEnum1 = firstEntry.getExonRanges().elements();
			while (cdsEnum1.hasMoreElements()) {
				SeqRange r = (SeqRange) cdsEnum1.nextElement();
				fw.write("100 @1 genes " + r.getStart() + " " + r.getStop() + " GREEN exon\n");
			}
			Enumeration cdsEnum2 = secondEntry.getExonRanges().elements();
			while (cdsEnum2.hasMoreElements()) {
				SeqRange r = (SeqRange) cdsEnum2.nextElement();
				fw.write("100 @2 genes " + r.getStart() + " " + r.getStop() + " GREEN exon\n");
			}
			Enumeration utrEnum1 = firstEntry.getUTRs().elements();
			while (utrEnum1.hasMoreElements()) {
				SeqRange r = (SeqRange) utrEnum1.nextElement();
				fw.write("100 @1 genes " + r.getStart() + " " + r.getStop() + " YELLOW utr\n");
			}
			Enumeration utrEnum2 = secondEntry.getUTRs().elements();
			while (utrEnum2.hasMoreElements()) {
				SeqRange r = (SeqRange) utrEnum2.nextElement();
				fw.write("100 @2 genes " + r.getStart() + " " + r.getStop() + " YELLOW utr\n");
			}
			Enumeration repEnum1 = firstEntry.getRepeats().elements();
			while (repEnum1.hasMoreElements()) {
				SeqRange r = (SeqRange) repEnum1.nextElement();
				fw.write("50 @1 repeats " + r.getStart() + " " + r.getStop() + " RED repeat\n");
			}
			Enumeration repEnum2 = secondEntry.getRepeats().elements();
			while (repEnum2.hasMoreElements()) {
				SeqRange r = (SeqRange) repEnum2.nextElement();
				fw.write("50 @2 repeats " + r.getStart() + " " + r.getStop() + " RED repeat\n");
			}
			Enumeration cpgEnum1 = firstEntry.getCpGs().elements();
			while (cpgEnum1.hasMoreElements()) {
				SeqRange r = (SeqRange) cpgEnum1.nextElement();
				fw.write("50 @1 CpG_islands " + r.getStart() + " " + r.getStop() + " YELLOW CpG\n");
			}
			Enumeration cpgEnum2 = secondEntry.getCpGs().elements();
			while (cpgEnum2.hasMoreElements()) {
				SeqRange r = (SeqRange) cpgEnum2.nextElement();
				fw.write("50 @2 CpG_islands " + r.getStart() + " " + r.getStop() + " YELLOW CpG\n");
			}
			Enumeration simEnum1 = firstEntry.getConservedElements().elements();
			while (simEnum1.hasMoreElements()) {
				SeqRange r = (SeqRange) simEnum1.nextElement();
				fw.write("50 @1 Conserved_elements " + r.getStart() + " " + r.getStop() + " BLUE cons\n");
			}
			Enumeration simEnum2 = secondEntry.getConservedElements().elements();
			while (simEnum2.hasMoreElements()) {
				SeqRange r = (SeqRange) simEnum2.nextElement();
				fw.write("50 @2 Conserved_elements " + r.getStart() + " " + r.getStop() + " BLUE cons\n");
			}
			EntryPair ep = (EntryPair) firstEntry.parent;
			Enumeration epen = ep.getChildEnumeration();
			while (epen.hasMoreElements()) {
				Glyph gl = (Glyph) epen.nextElement();
				if(gl instanceof RegionSet) {
					RegionSet rs = (RegionSet) gl;
					Enumeration rsen = rs.getChildEnumeration();
					while(rsen.hasMoreElements()) {
						DotterRegion dr = (DotterRegion) rsen.nextElement();
						SeqRange r = dr.getSeqRange();
						if (firstEntry.equals(dr.getEntry())) {
							fw.write("50 @1 Conserved_regions " + r.getStart() + " " + r.getStop() + " BLUE cons_reg\n");
						} if (secondEntry.equals(dr.getEntry())) {
							fw.write("50 @2 Conserved_regions " + r.getStart() + " " + r.getStop() + " BLUE cons_reg\n");
						}
					}
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException ioe) { System.out.println(ioe); }
	}
	
}
