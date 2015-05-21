/*
* $Revision: 1.1 $
* $Id: RegionSet.java,v 1.1 2003/04/04 10:14:53 niclas Exp $
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
 * A composite Glyph to hold information about SeqRanges similar to each other.<P>
 * @see alfresco.CompositeGlyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class RegionSet extends CompositeGlyph{
/**
 * The Entry that the query range belongs to
 */
	Entry firstEntry;
/**
 * The other entry of the EntryPair
 */
	Entry secondEntry;
/**
 * The query range
 */
	SeqRange qRange;
/**
 *   Properties for DBA
 */
  Properties dbaprop;
/**
 * Vector of dba alignments
 */
	Vector dbas;
/**
 * Warning string if no dba alignment can be found for RegionSet
 */
	String message = null;
	
/**
 * Creates a new RegionSet using the specified DotterRegion
 * @param ep the EntryPair holding the Entries to be searched
 * @param query the DotterRegion to which other similar regions should be found
 */
	RegionSet(EntryPair ep, DotterRegion query, Properties dbaprop) {
		super();
		firstEntry = query.entry;
		boolean inverted = false;
    this.dbaprop = dbaprop;
		if (firstEntry.equals(ep.entry2)) { inverted = true; }
		if (inverted) {
			secondEntry = ep.entry1;
		} else {
			secondEntry = ep.entry2;
		}
// 		Vector firstExclude = firstEntry.getExclude();
// 		Vector secondExclude = secondEntry.getExclude();
		Vector firstExclude = new Vector();
		Vector secondExclude = new Vector();
		DotterRegionSet drs = ep.dotterRegions;
		int cutoff = drs.cutoff;
		qRange = query.getSeqRange();
		firstExclude.addElement(qRange);
		if (!inverted) { // ie firstEntry == entry1
			Vector selfHitPoints = drs.dottFile11.getMatches(qRange, cutoff, firstExclude, DotterFile.HORIZONTAL);
			makeChildren(selfHitPoints, firstEntry, DotterFile.HORIZONTAL);
			selfHitPoints = drs.dottFile11.getMatches(qRange, cutoff, firstExclude, DotterFile.VERTICAL);
			makeChildren(selfHitPoints, firstEntry, DotterFile.VERTICAL);
			
			Vector otherHitPoints = drs.dottFile12.getMatches(qRange, cutoff, secondExclude, DotterFile.HORIZONTAL);
			makeChildren(otherHitPoints, secondEntry, DotterFile.HORIZONTAL);
			
		} else {	// ie firstEntry == entry2
			Vector selfHitPoints = drs.dottFile22.getMatches(qRange, cutoff, firstExclude, DotterFile.HORIZONTAL);
			makeChildren(selfHitPoints, firstEntry, DotterFile.HORIZONTAL);
			selfHitPoints = drs.dottFile22.getMatches(qRange, cutoff, firstExclude, DotterFile.VERTICAL);
			makeChildren(selfHitPoints, firstEntry, DotterFile.VERTICAL);
			
			Vector otherHitPoints = drs.dottFile12.getMatches(qRange, cutoff, secondExclude, DotterFile.VERTICAL);
			makeChildren(otherHitPoints, secondEntry, DotterFile.VERTICAL);
		}
		dbas = new Vector();
		DotterRegion clickedRegion = new DotterRegion(firstEntry, qRange);
// 		System.out.println("dba " + clickedRegion + " vs ");
		Vector firstMask = null;
// 		Vector secondMask = null;
		if(firstEntry.equals(ep.entry1)) {
			firstMask = drs.exclude1;
		} else {
			firstMask = drs.exclude2;
		}
// 		if(secondEntry.equals(ep.entry1)) {
// 			secondMask = drs.exclude1;
// 		} else {
// 			secondMask = drs.exclude2;
// 		}
		int qStart = 0;
		int qStop = 0;
		Enumeration kids = children.elements();
		while (kids.hasMoreElements()) {
			DotterRegion reg = (DotterRegion) kids.nextElement();
// 			System.out.println(reg);
// 			SeqRange drange = reg.getSeqRange().getDoubleRange(reg.getEntry());
			SeqRange drange = reg.getSeqRange().getTrippleRange(reg.getEntry());
			int[] minmaxF = null;
			int[] minmaxR = null;
			Vector secondMask = null;
			if(reg.getEntry().equals(ep.entry1)) {
				secondMask = drs.exclude1;
			} else {
				secondMask = drs.exclude2;
			}
// 			System.out.println("Dba(" + clickedRegion.getEntry() + ",");
// 			System.out.println("    " + reg.getEntry() + ",");
// 			System.out.println("    " + clickedRegion.getSeqRange().getTrippleRange(clickedRegion.getEntry()) + ",");
// 			System.out.println("    " + drange + ",");
// 			System.out.println("    " + firstMask + ",");
// 			System.out.println("    " + secondMask + ");");
// 			Dba dF = new Dba(clickedRegion.getEntry(), 
			Dba dF = new Dba(clickedRegion.getEntry(), 
			                reg.getEntry(), 
// 			                clickedRegion.getSeqRange().getDoubleRange(clickedRegion.getEntry()),
			                clickedRegion.getSeqRange().getTrippleRange(clickedRegion.getEntry()),
			                drange,
			                firstMask,
			                secondMask,
                      dbaprop);
			dF.onlyOverlapping(clickedRegion.getSeqRange(), reg.getSeqRange());
			if(dF.hasBlocks()) {
// 				System.out.println("dF has blocks");
				minmaxF = dF.getMinMax();
				dbas.addElement(dF);
			}
			drange.setComplement(true);
// 			Dba dR = new Dba(clickedRegion.getEntry(), 
			Dba dR = new Dba(clickedRegion.getEntry(), 
			                reg.getEntry(), 
// 			                clickedRegion.getSeqRange().getDoubleRange(clickedRegion.getEntry()),
			                clickedRegion.getSeqRange().getTrippleRange(clickedRegion.getEntry()),
			                drange,
			                firstMask,
			                secondMask,
                      dbaprop );
			dR.onlyOverlapping(clickedRegion.getSeqRange(), reg.getSeqRange());
			if(dR.hasBlocks()) {
				minmaxR = dR.getMinMax();
				dbas.addElement(dR);
			}
			if(!dF.hasBlocks() && !dR.hasBlocks()) { 
				System.out.println("Kablooooeeeeeeyyy! No dba for this reg set");
				
				continue;
			}
			
			int[] minmax;
			if(minmaxR != null && minmaxF != null ) { 
				minmax = new int[4];
				minmax[0] = Math.min(minmaxF[0], minmaxR[0]);
				minmax[1] = Math.max(minmaxF[1], minmaxR[1]);
				minmax[2] = Math.min(minmaxF[2], minmaxR[2]);
				minmax[3] = Math.max(minmaxF[3], minmaxR[3]);
			} else if (minmaxF != null) {
				minmax = minmaxF;
			} else {
				minmax = minmaxR;
			}
// 			if (minmax[0] == 0) { 
// 				System.out.println("Kablooooeeeeeeyyy! No dba for this reg set"); 
// 				return; 
// 			} else {
// 				dbas.addElement(d);		
// 			}
			if (qStart == 0) { 
				qStart = minmax[0];
				qStop = minmax[1];
			} else {
				if (qStart > minmax[0]) qStart = minmax[0];
				if (qStop < minmax[1]) qStop = minmax[1];
			}
			reg.setSeqRange(new SeqRange(minmax[2], minmax[3]));
		}
		if (qStart != 0) {
			clickedRegion.setSeqRange(new SeqRange(qStart, qStop));
		} else {
			message = "Warning: No dba alignment was found for this RegionSet, try changing dba settings";
// 			throw new WarningMessageException("No dba alignment was found for this RegionSet, try changing dba settings");
		}
		clickedRegion.select(true);
		addChild(clickedRegion);
	}
	
/**
 * Adds found DotterRegions to children Vector
 * @param hitPoints Vector of DotterPoints being similar
 * @param ent The Entry the DotterRegions belong to
 * @param dir Direction of entry in dotter matrix, either DotterFile.HORIZONTAL or DotterFile.VERTICAL
 */
	private void makeChildren(Vector hitPoints, Entry ent, int dir) {
		if (hitPoints != null) {
			Enumeration pointEnum = hitPoints.elements();
			while(pointEnum.hasMoreElements()) {
				DotterPoint dp = (DotterPoint) pointEnum.nextElement();
				if (dir == DotterFile.HORIZONTAL) {
					addChild(new DotterRegion(ent, dp.getVertical()));
				} else {
					addChild(new DotterRegion(ent, dp.getHorizontal()));
				}
			}
		}
	}
	
/**
 * Changes the tier of the children to be one level away from Entry
 */
	public void promote() {
		Enumeration childEnum = children.elements();
		while(childEnum.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) childEnum.nextElement();
			if (dr.tier > 0) { dr.tier++; }
			else { dr.tier--; }
			dr.origColor = dr.fillColor = Color.cyan;
		}
	}
	
/**
 * Sets the color of children
 * @param c the color
 */
	public void setColor(Color c) {
		Enumeration childEnum = children.elements();
		while(childEnum.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) childEnum.nextElement();
			dr.origColor = dr.fillColor = c;
		}		
	}
	
/**
 * Whether to select children or not
 * @param select true if children should be selected, false if not
 */
	public void select(boolean select) {
		Enumeration ce = children.elements();
		while (ce.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) ce.nextElement();
			dr.select(select);
		}
	}	
	
/**
 * Determines if a child was clicked
 * @return clicked Glyph, or null if outside bounding rect
 * @param x mouse x position
 * @param y mouse y position
 */
	public Glyph clicked(int x, int y) {
		// System.out.println();
		if (boundingRect.contains(x,y)) {
			Enumeration ce = children.elements();
			while (ce.hasMoreElements()) {
				DotterRegion dr = (DotterRegion) ce.nextElement();
				DotterRegion cdr = (DotterRegion) dr.clicked(x, y);
				if (cdr != null) { 
// 					highlight(true);
					System.out.println("Clicked DotterRegion " + cdr + " tier: " + cdr.tier);
					//return cdr; 
					return this;
				}
			}
			// return this;
// 			highlight(false);
		}
// 		highlight(false);
		return null;
	}
	
// 	public void addChild(DotterRegion dr) {
// 		super.addChild((Glyph) dr);
// // 		dr.coord = dr.entry.coord;
// 	}
	
/**
 * Calls dotter with the query range and ranges of all other DotterRegions.
 * Fasta format files of sequences are written to TMPDIR
 */
	public void callDotter() {
		String featurefn = firstEntry.getFilename() + "-" + secondEntry.getFilename() + ".ftr";
		DotterFeatureFile featuref = new DotterFeatureFile(SystemConstants.TMPDIR, featurefn, firstEntry, secondEntry);
		featuref.write();
// 		int flankLen = qRange.length()/2;
		int flankLen = qRange.length();
		DotterRegion qRegion = null;
		Enumeration ce = children.elements();
		while (ce.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) ce.nextElement();
			
// 			if (qRange.isEqual(dr.range)) {
// 				qRegion = dr;
// 			}
			if(qRange.overlap(dr.range)) {
				qRegion = dr;	
			}
		}
		String qfn = qRegion.entry.getFilename().toLowerCase() + "_" + qRange.getStart() + "-" + qRange.getStop();
		int qStart = qRange.getStart()-flankLen;
		if (qStart < 1) { qStart = 1; }
		FastaFile qFile = new FastaFile(SystemConstants.TMPDIR, qfn, qRegion.entry, new SeqRange(qStart,qRange.getStop()+flankLen));
		qFile.write(); 
		int qOffset = qStart-1;
		Runtime rt = Runtime.getRuntime();
		ce = children.elements();
		while (ce.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) ce.nextElement();
// 			if (!qRange.isEqual(dr.range)) { //old
// 			if (!qRegion.overlap(dr)) {	// newer
			if (!qRegion.equals(dr)) {	// newest
				DotterRegion otherRegion = dr;
				int oStart = otherRegion.range.getStart()-flankLen;
				if (oStart < 1) { oStart = 1; }
				SeqRange newRange = new SeqRange(oStart,otherRegion.range.getStop()+flankLen);
				int oOffset = oStart-1;
				String ofn = otherRegion.entry.getFilename().toLowerCase() + "_" + newRange.getStart() + "-" + newRange.getStop();
// 				String ofn = "otherfile";
				FastaFile oFile = new FastaFile(SystemConstants.TMPDIR, ofn, otherRegion.entry, newRange);
				oFile.write();
				try {
// 					Process p = rt.exec(DOTTERPATH + "dotter -f " + featurefn + " -q " + qOffset + " -s " + oOffset + " " + qfn + " " + ofn);
					Process p = rt.exec(SystemConstants.DOTTERPATH + "dotter -f " + featuref.getAbsolutePath() + " -q " + qOffset + " -s " + oOffset + " " + qFile.getAbsolutePath() + " " + oFile.getAbsolutePath());
// 					Process p = rt.exec("dotter -q " + qOffset + " -s " + oOffset + " " + qfn + " " + ofn);
// 					Process p = rt.exec("shit qfn ofn");
// 					InputStream stdout = p.getInputStream();
// 					InputStream stderr = p.getErrorStream();
// 					InputStreamReader stdoutr = new InputStreamReader(stdout);
// 					InputStreamReader stderrr = new InputStreamReader(stderr);
// 					BufferedReader stdoutbr = new BufferedReader(stdoutr);
// 					BufferedReader stderrbr = new BufferedReader(stderrr);
// 					String errline, outline;
// 					errline = stderrbr.readLine();
// 					outline = stdoutbr.readLine();
// 					while (errline != null) {
// 						System.out.println(errline);
// 						errline = stderrbr.readLine();
// 					}
// 					while (outline != null) {
// 						System.out.println(outline);
// 						outline = stdoutbr.readLine();
// 					}
				} catch (IOException ioe) { System.out.println(ioe); }
// 				catch (InterruptedException ie) { System.out.println(ie); }
			}
		}
		
		
	}
	/**
	 * Shows dba alignments for this region set in an AlignmentDisplay
	 */
	public void showDbas() {
		Enumeration dbaen = dbas.elements();
		while(dbaen.hasMoreElements()) {
			Dba d = (Dba) dbaen.nextElement();
// 			NewDba d = (NewDba) dbaen.nextElement();
			AlignmentDisplay alD = new AlignmentDisplay(d.getAlignments());
			alD.show();
		}
	}
	/**
	 * String representation of regionSet
	 * @return string representation
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("RegionSet: ");
		Enumeration ce = children.elements();
		while (ce.hasMoreElements()) {
			DotterRegion dr = (DotterRegion) ce.nextElement();
			buf.append(dr.toString() + " ");
		}
		return buf.toString();
	}
	/**
	 * Determines if there is a warning associated with this RegionSet
	 * @return true if ther is awarning, otherwise false
	 */
	public boolean isWarning() {
// 		return !message.equals("");
		if (message != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the warning associated with this RegionSet
	 * @return warning string
	 */
	public String getWarning() {
		return message;
	}
	
}
