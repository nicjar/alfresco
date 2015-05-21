/*
 * $Revision: 1.1 $
 * $Id: ReciprocalFinder.java,v 1.1 2003/04/04 10:14:50 niclas Exp $
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
/**
 * Class for implementing the finding of reciprocal exons in two sequences
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class ReciprocalFinder {
// /**
//  * first entry	
//  */
// 	Entry entry1;
// /**
//  * second entry	
//  */	
// 	Entry entry2;
/**
 * Vector with predicted exons from entry1	
 */
	Vector exons1;
/**
 * Vector with predicted exons from entry2	
 */	
	Vector exons2;
/**
 * Vector of reciprocals	
 */
	Vector recip;
/**
 *   Properties for DBA
 */
  Properties dbaprop;
/**
 * Array of alignment blocks sorted in decending order on basescore	
 */
// 	AlignmentBlock [] basescorelist;
// 	NewDba [] basescorelist;
	Dba [] basescorelist;
	
	/**
	 * Creates new FindReciprocals
	 * @param ent1 first entry
	 * @param ent1 second entry
	 */
	public ReciprocalFinder(Entry ent1, Entry ent2, Properties dbaprop) {
// 		entry1 = ent1;
// 		entry2 = ent2;
		exons1 = getPredExon(ent1);
		exons2 = getPredExon(ent2);
    this.dbaprop = dbaprop;
		align();
// 		recip = findReci();
// 		checkOrder();
	}
	
	/**
	 * gets vector of reciprocals
	 * @return vector of reciprocals
	 */
	public Vector getReciprocals() {
		return recip;
	}
	
	/**
	 * Gets predicted Exons from an Entry
	 * @return Vector of predicted Exons
	 * @param ent Entry
	 */
	private Vector getPredExon(Entry ent) {
		Vector result = new Vector();
		Enumeration exen = ent.getExons().elements();
		while (exen.hasMoreElements()) {
			Exon ex = (Exon) exen.nextElement();
			if (ex.isPredicted()) result.addElement(ex);
		}
		return result;
	}
	
	/**
	 * Aligns all exons vs all exons and sorts them in decending order
	 */
	public void align() {
// 		Vector blockVect = new Vector();
		Vector rec = new Vector();
		int len1 = exons1.size();
		int len2 = exons2.size();
		Enumeration en1 = exons1.elements();
		System.out.print("Aligning all vs. all...");
		long start = System.currentTimeMillis();
		for (int i = 0; i < len1; i++) {
			Exon upperEx = (Exon) exons1.elementAt(i);
			Exon bestLower = null;
// 			AlignmentBlock bestBl = null;
// 			NewDba bestAl = null;
			double bestScore = 0;
			for (int j = 0; j < len2; j++) {
				Exon lowerEx = (Exon) exons2.elementAt(j);
// 				NWAlign al = new NWAlign(upperEx, lowerEx);
// 				SWAlign al = new SWAlign(upperEx, lowerEx);
// 				AlignmentBlock bl = al.getAlignmentBlock();
// 				if (bestLower == null) {
// 					bestLower = lowerEx;
// 					bestBl = bl;
// 				} else {
// 					if (bl.getScore() > bestBl.getScore()) {
// 						bestLower = lowerEx;
// 						bestBl = bl;
// 					}
// 				}
// 				NewDba al = new NewDba(upperEx, lowerEx);
				Dba al = new Dba(upperEx, lowerEx, dbaprop);
				if (bestLower == null) {
					bestLower = lowerEx;
// 					bestAl = al;
					bestScore = al.getOverallScore();
				} else {
// 					if (al.getOverallScore() > bestAl.getOverallScore()) {
					if (al.getOverallScore() > bestScore) {
						bestLower = lowerEx;
// 						bestAl = al;
						bestScore = al.getOverallScore();
					}
				}
			}
			// make a reciprocal and add to result vector
			if (bestLower != null) {
// 				if (bestBl.getScore() > 0) {
// 					blockVect.addElement(bestBl);
					
// 					blockVect.addElement(bestAl);
					Reciprocal bestRec = new Reciprocal(upperEx, bestLower);
					bestRec.setScore(bestScore);
					rec.addElement(bestRec);
// 				}
			}
		}
		long stop = System.currentTimeMillis();
		System.out.println(" runtime: " + (stop - start) + " millisecs");
// 		basescorelist = new AlignmentBlock[blockVect.size()];
// 		basescorelist = new NewDba[blockVect.size()];
// 		blockVect.copyInto(basescorelist);
// 		qsort(basescorelist, 0, basescorelist.length - 1);
		System.out.print("Sorting reciprocals...");
		start = System.currentTimeMillis();
		qsort(rec, 0, rec.size() - 1);
		stop = System.currentTimeMillis();
		System.out.println(" runtime: " + (stop - start) + " millisecs");
// 		Vector sortedRecs = new Vector(rec.size());
// 		for (int i = 0; i < basescorelist.length; i++) {
// // 			AlignmentBlock bl = basescorelist[i];
// 			NewDba bl = basescorelist[i];
// // 			System.out.println(bl.getIdentity() + ", " + bl.getBaseScore());
// 			System.out.println(bl.getOverallScore());
// 			Enumeration recen = rec.elements();
// 			while (recen.hasMoreElements()) {
// 				Reciprocal r = (Reciprocal) recen.nextElement();
// 				if (bl.getRange1().isEqual(r.getFeature1().getSeqRange())) {
// // 				if (bl.getRange1().overlap(r.getFeature1().getSeqRange())) {
// 					sortedRecs.addElement(r);
// 				}
// 			}
// 		}
		recip = new Vector();
// 		if (sortedRecs.size() > 0) {
		if (rec.size() > 0) {
			System.out.print("Checking order...");
			start = System.currentTimeMillis();
// 			System.out.println(rec);
// 			Enumeration srecen = sortedRecs.elements();
			Enumeration srecen = rec.elements();
			recip.addElement(srecen.nextElement());
			while(srecen.hasMoreElements()) {
				Reciprocal r = (Reciprocal) srecen.nextElement();
				if (!cross(r, recip)) recip.addElement(r);
			}
			stop = System.currentTimeMillis();
			System.out.println(" runtime: " + (stop - start) + " millisecs");
		}
		
	}
	
	/**
	 * Quick sort algorithm. Sorts an array of AlignmentBlocks in decending 
	 * order on the basescore
	 * @param blocklist alignment block array
	 * @param left left index
	 * @param right right index
	 */
// 	private void qsort(AlignmentBlock[] blocklist, int left, int right) {
// 	private void qsort(NewDba[] blocklist, int left, int right) {
	private void qsort(Vector list, int left, int right) {
		int last;
		
		if (left >= right) return;
		
// 		swap(blocklist, left, (left+right)/2);
		swap(list, left, (left+right)/2);
		last = left;
		for (int i = left+1; i <= right; i++) {
// 			if (blocklist[i].getBaseScore() > blocklist[left].getBaseScore()) 

// 			if (blocklist[i].getIdentity() > blocklist[left].getIdentity()) 
// 			if (blocklist[i].getOverallScore() > blocklist[left].getOverallScore())
// 				swap(blocklist, ++last, i);
			
			if (((Reciprocal) list.elementAt(i)).getScore() > ((Reciprocal) list.elementAt(left)).getScore() )
				swap(list, ++last, i);
			
		}
// 		swap(blocklist, left, last);
// 		qsort(blocklist, left, last-1);
// 		qsort(blocklist, last+1, right);
		swap(list, left, last);
		qsort(list, left, last-1);
		qsort(list, last+1, right);
	}
	
	/**
	 * Swaps the places of the objects of the specified indexes in the  
	 * AlignmentBlock array
	 * @param blocklist alignment block array
	 * @param i index
	 * @param j index
	 */
// 	private void swap(AlignmentBlock[] blocklist, int i, int j) {
// 	private void swap(NewDba[] blocklist, int i, int j) {
	private void swap(Vector list, int i, int j) {
// 		AlignmentBlock tmp;
// 		tmp = blocklist[i];
// 		blocklist[i] = blocklist[j];
// 		blocklist[j] = tmp;
		Object tmp;
		tmp = list.elementAt(i);
		list.setElementAt(list.elementAt(j), i);
		list.setElementAt(tmp, j);
	}
	
	/**
	 * Determines if the reciprocal crosses another reciprocal in the vector
	 * @return true if rec crosses another reciprocal, otherwise false
	 * @param rec reciprocal to be checked
	 * @param recs vector of reciprocals
	 */
	public boolean cross(Reciprocal rec, Vector recs) {
		boolean cross = false;
		SeqRange mr1 = rec.getFeature1().getSeqRange();
		SeqRange mr2 = rec.getFeature2().getSeqRange();
		Enumeration recen = recs.elements();		
		while(recen.hasMoreElements()) {
			Reciprocal r = (Reciprocal) recen.nextElement();
			SeqRange cr1 = r.getFeature1().getSeqRange();
			SeqRange cr2 = r.getFeature2().getSeqRange();
			if(mr1.getStop() < cr1.getStart() && mr2.getStart() > cr2.getStop()) {
				cross = true;
			} else if(mr1.getStart() > cr1.getStop() && mr2.getStop() < cr2.getStart()) {
				cross = true;
			}
// 		System.out.println(rec + (cross?" crosses ":" doesn't cross ") + r);
		}
		return cross;
	}
	
	/**
	 * Finds the best matches and returns a vector of reciprocals
	 * @return vector of reciprocals
	 */
	private Vector findReci() {
		Vector r = new Vector();
		Enumeration en1 = exons1.elements();
		int len1 = exons1.size();
		int len2 = exons2.size();
		for (int i = 0; i < len1; i++) {
			Exon upperEx = (Exon) exons1.elementAt(i);
			Exon bestLower = null;
			AlignmentBlock bestBl = null;
			for (int j = 0; j < len2; j++) {
				Exon lowerEx = (Exon) exons2.elementAt(j);
				SWAlign al = new SWAlign(upperEx, lowerEx);
				AlignmentBlock bl = al.getAlignmentBlock();
				if (bestLower == null) {
					if (passCutoffs(bl, upperEx, lowerEx)) {
						bestLower = lowerEx;
						bestBl = bl;
					}
				} else {
					if (passCutoffs(bl, upperEx, lowerEx)) {
						if (bl.getScore() > bestBl.getScore()) {
							bestLower = lowerEx;
							bestBl = bl;
						}
					}
				}
			}
			// make a reciprocal and add to result vector
			if (bestLower != null) {
				r.addElement(new Reciprocal(upperEx, bestLower));
			}
		}
		return r;
	}
	
	/**
	 * Checks the order of reciprocals and removes reciprocals that violate co-linear 
	 * ordering of features.
	 */
	private void checkOrder() {
		// sort recip vector on feature1
		// check sorted recip vector if
		//		f1.start > prev1.stop
		//		f2.start > prev2.stop
		//		f1.stop < next1.start
		//		f2.stop < next2.start
		// Note! this algogithm can not be trusted to delete the correct reciprocal
		// if the 'crossing' reciprocals are adjacent to each other

		Vector tmpV = new Vector(recip.size());
		Enumeration ren = recip.elements();
		while(ren.hasMoreElements()) {
			Reciprocal r = (Reciprocal) ren.nextElement();
			if (tmpV.isEmpty()) {
				tmpV.addElement(r);
// 				System.out.println("Added first element " + r);
			} else {
				SeqFeature sf1 = r.getFeature1();
				boolean added = false;
				int tsize = tmpV.size();
				for(int i = 0; i < tsize; i++) {
					Reciprocal cr = (Reciprocal) tmpV.elementAt(i);
					if (sf1.getSeqRange().getStop() < cr.getFeature1().getSeqRange().getStart()) {
						tmpV.insertElementAt(r, i);
						added = true;
// 						System.out.println("Added element " + r + ", at pos " + i);
						break;
					}
				}
				if (!added) {
					tmpV.addElement(r);
// 					System.out.println("Added element " + r);
				}
			}
		}
// 		System.out.println("Sorted reciprocals");
// 		Enumeration ten = tmpV.elements();
// 		while(ten.hasMoreElements()) {
// 			Reciprocal r = (Reciprocal) ten.nextElement();
// 			System.out.println(r);
// 		}
		int size = tmpV.size();
		boolean badPrev = false;
		for (int i = 0; i < size; i++) {
			Reciprocal r = (Reciprocal) tmpV.elementAt(i);
			Reciprocal prevR = null;
			Reciprocal nextR = null;
			if(i > 0) prevR = (Reciprocal) tmpV.elementAt(i-1);
			if(i < size - 1) nextR = (Reciprocal) tmpV.elementAt(i+1);
			boolean ok = true;
			SeqRange cr1 = r.getFeature1().getSeqRange();
			SeqRange cr2 = r.getFeature2().getSeqRange();
			if (prevR != null && !badPrev ) {
				SeqRange pr1 = prevR.getFeature1().getSeqRange();
				SeqRange pr2 = prevR.getFeature2().getSeqRange();
				if(!cr1.overlap(pr1) ) {
					if(cr1.getStart() < pr1.getStop()) ok = false;
				}
				if (!cr2.overlap(pr2)) {
					if(cr2.getStart() < pr2.getStop()) ok = false;
				}
			}
			if(nextR != null) {
				SeqRange nr1 = nextR.getFeature1().getSeqRange();
				SeqRange nr2 = nextR.getFeature2().getSeqRange();
				if(!cr1.overlap(nr1) ) {
					if(cr1.getStop() > nr2.getStart()) ok = false;
				}
				if (!cr2.overlap(nr2)) {
					if(cr2.getStop() > nr2.getStart()) ok = false;
				}
			}
			if(!ok) {
				recip.removeElement(r);
				badPrev = true;
			}
		}
	}
	
	/**
	 * Determines if AlignmentBlock is good enough
	 * @return true if alignment is good enough, otherwise false
	 * @param block alignment block
	 */
	private boolean passCutoffs(AlignmentBlock block, Exon e1, Exon e2) {
		if(block.getLength() < 20) return false; // totally arbitrary
		int minLen = Math.min(e1.getSeqRange().length(), e2.getSeqRange().length());
		if (minLen/block.getLength() > 2) return false; // al.length < 50% of shortest exon
		if (block.getIdentity() < 70) return false; // Id < 70%
		return true;
	}
	
}
