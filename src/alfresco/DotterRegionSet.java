/*
* $Revision: 1.1 $
* $Id: DotterRegionSet.java,v 1.1 2003/04/04 10:14:11 niclas Exp $
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
 * A composite glyph to hold Dotter information about two sequences.<P>
 * @see alfresco.CompositeGlyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class DotterRegionSet extends CompositeGlyph {
/**
 * EntryPair holding two sequences
 */
	EntryPair entryPair;
/**
 * First sequence self compare DotterFile
 */
	DotterFile dottFile11;
/**
 * Second sequence self compare DotterFile
 */
	DotterFile dottFile22;
/**
 * First against second sequence compare DotterFile
 */
	DotterFile dottFile12;
/**
 * SeqRange generator
 */
	transient SeqRangeFactory srf;
/**
 * Dotter score cutoff for the DotterRegionSet
 */
	int cutoff = 0;
/**
 * Vector of ranges to be excluded from sequence 1
 */
	Vector exclude1;
/**
 * Vector of ranges to be excluded from sequence 2
 */
	Vector exclude2;
	
	
/**
 * Creates DotterRegionSet masking ranges specified in the exclude vectors, and creates three DotterFiles
 * @param entp EntryPair holding the two sequences
 * @param entp exclude1 Vector of ranges to be excluded from sequence 1
 * @param entp exclude2 Vector of ranges to be excluded from sequence 2
 */
	DotterRegionSet(EntryPair entp, Vector excl1, Vector excl2) {
		super();
		parent = entp;
		entryPair = entp;
		this.exclude1 = excl1;
		this.exclude2 = excl2;
		srf = new SeqRangeFactory();
		if (entryPair.entriesSet()){
			String origfn1 = entryPair.entry1.getFilename();
			String origfn2 = entryPair.entry2.getFilename();
			if (origfn2.endsWith("\n")) { 
				System.out.println("Still newline in filename!");
				return;
			}
			String filename1 = origfn1 + ".masked";
			String filename2 = origfn2 + ".masked";
			FastaFile seqfile1 = new FastaFile(SystemConstants.TMPDIR, filename1, entryPair.entry1);
			FastaFile seqfile2 = new FastaFile(SystemConstants.TMPDIR, filename2, entryPair.entry2);
			maskSeq(seqfile1,exclude1);
			maskSeq(seqfile2,exclude2);
			seqfile1.write();
			seqfile2.write();
// 			String outf11 = "seq1-seq1.dotter";
// 			String outf22 = "seq2-seq2.dotter";
// 			String outf12 = "seq1-seq2.dotter";
			String outf11 = origfn1 + "-" + origfn1 + ".dotter";
			String outf22 = origfn2 + "-" + origfn2 + ".dotter";
			String outf12 = origfn1 + "-" + origfn2 + ".dotter";
			
			int minSeqLen = 0;
			if (entryPair.entry1.getLength() < entryPair.entry2.getLength() ) {
				minSeqLen = entryPair.entry1.getLength();
			} else {
				minSeqLen = entryPair.entry2.getLength();
			}
			int dottZoom = minSeqLen/100;
			System.out.println("Dotter zoomfactor: " + dottZoom);
			if ( dottZoom == 0 ) { dottZoom = 1; }
// 			dottFile11 = new DotterFile(filename1, filename1, outf11, dottZoom, srf);
// 			dottFile22 = new DotterFile(filename2, filename2, outf22, dottZoom, srf);
// 			dottFile12 = new DotterFile(filename1, filename2, outf12, dottZoom, srf);
			dottFile11 = new DotterFile(seqfile1.getAbsolutePath(), seqfile1.getAbsolutePath(), outf11, dottZoom, srf);
			dottFile22 = new DotterFile(seqfile2.getAbsolutePath(), seqfile2.getAbsolutePath(), outf22, dottZoom, srf);
			dottFile12 = new DotterFile(seqfile1.getAbsolutePath(), seqfile2.getAbsolutePath(), outf12, dottZoom, srf);
			dottFile11.open();
			dottFile22.open();
			dottFile12.open();
		}
	}
	
/**
 * 	Sets children to a vector of DotterRegion objects with score above cutoff,
 * 	excluding ranges in exclude1 and exclude2.
 * 	@param co The cutoff value
 * 	@param exclude1 A Vector of SeqRange obj to be excluded from sequence 1
 * 	@param exclude2 A Vector of SeqRange obj to be excluded from sequence 2
 */
	public void updateChildren(int co, Vector exclude1, Vector exclude2) {
		cutoff = co;
		children = new Vector();
		Vector points11 = dottFile11.aboveCutoff(cutoff, exclude1, exclude1, true);
		Vector points22 = dottFile22.aboveCutoff(cutoff, exclude2, exclude2, true);
		Vector points12 = dottFile12.aboveCutoff(cutoff, exclude1, exclude2, false);
		makeChildren(points11, entryPair.entry1, entryPair.entry1);
		makeChildren(points22, entryPair.entry2, entryPair.entry2);
		makeChildren(points12, entryPair.entry1, entryPair.entry2);
	}

/**
 * 	Sets children to a vector of DotterRegion objects with score above cutoff.
 * 	@param co The cutoff value
 */
	public void updateChildren(int co) {
		cutoff = co;
		children = new Vector();
		Vector points11 = dottFile11.aboveCutoff(cutoff, true);
		Vector points22 = dottFile22.aboveCutoff(cutoff, true);
		Vector points12 = dottFile12.aboveCutoff(cutoff, false);
		makeChildren(points11, entryPair.entry1, entryPair.entry1);
		makeChildren(points22, entryPair.entry2, entryPair.entry2);
		makeChildren(points12, entryPair.entry1, entryPair.entry2);
	}
	
/**
 * Creates DotterRegions from DotterPoints specified
 * @param points Vector of DotterPoints
 * @param horEntry Entry corresponding to the horizontal sequence
 * @param vertEntry Entry corresponding to the vertical sequence
 */
	private void makeChildren(Vector points, Entry horEntry, Entry vertEntry) {
		Enumeration enum = points.elements();
		while (enum.hasMoreElements()) {
			DotterPoint dp = (DotterPoint) enum.nextElement();
			DotterRegion drh = new DotterRegion(horEntry, dp.getHorizontal());
			DotterRegion drv = new DotterRegion(vertEntry, dp.getVertical());
			if (!isChild(drh)) {
				addChild(drh);
			}
			if (!isChild(drv)) {
				addChild(drv);
			}
		}
	}
	
/**
 * Checks if a DotterRegion is a child already
 * @return true if region is a child, otherwise false
 * @param dottreg the region to be checked
 */
	private boolean isChild(DotterRegion dottreg) {
		Enumeration childEnum = children.elements();
		while (childEnum.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) childEnum.nextElement();
			if (dr.equals(dottreg)) { return true; }
		}
		return false;
	}
	
/**
 * Masks the ranges specified in a sequence file
 * @param ff FastaFile to be masked
 * @param exclude vector of ranges
 */
	private void maskSeq(FastaFile ff, Vector exclude) {
		Enumeration exclRanges = exclude.elements();
		while (exclRanges.hasMoreElements()) {
			SeqRange exclRange = (SeqRange) exclRanges.nextElement();
			ff.mask(exclRange);
		}
	}
	
// 	public void addChild(Glyph gl) {
// 		super.addChild(gl);
// // 		DotterRegion region = (DotterRegion) gl;
// // 		if (coord != null) {
// // 			coord = new CoordVal(entry.parentEntryPair.application.mw.seqCan, tier);
// // 		} 
// // 		region.coord = region.entry.coord;
// 		
// 	}
	
/**
 * Re-populates score array in the three DotterFiles after loading from file
 */
	public void populateDottFileScores() {
		srf = new SeqRangeFactory();
		dottFile11.populateScores(srf);
		dottFile22.populateScores(srf);
		dottFile12.populateScores(srf);
	}
}
