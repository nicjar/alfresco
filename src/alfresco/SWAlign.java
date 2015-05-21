/*
 * $Revision: 1.1 $
 * $Id: SWAlign.java,v 1.1 2003/04/04 10:14:59 niclas Exp $
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
/**
 * Class implementing the Smith-Waterman alignment algortithm
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public class SWAlign extends Align {
	
	/**
	 * Creates a SWAlign, and aligns the to specified features
	 * @param feature1 first feature
	 * @param feature2 second feature
	 */
	public SWAlign(SeqFeature feature1, SeqFeature feature2) {
		super(feature1, feature2);
	}
	
	/**
	 * Creates a SWAlign, and aligns the to specified features
	 * @param feature1 first feature
	 * @param feature2 second feature
	 * @param match match score
	 * @param mismatch mismatch score
	 * @param gap gap score
	 */
	public SWAlign(SeqFeature feature1, 
	               SeqFeature feature2, 
	               int match, 
	               int mismatch, 
	               int gap) {
		super(feature1, feature2, match, mismatch, gap);
	}
	
	/**
	 * Sets default parameters.
	 */
	public void setDefaultParameters() {
		match = 2;
		mismatch = -2;
		gap = -10;
	}
	
	/**
	 * Implements the alignment algorithm, overrides the abstract doDynamicProgramming in
	 * the parent Align class
	 * @return descr
	 * @param param descr
	 */
	public void doDynamicProgramming() {
		short matrixmax = 0;
		int iMax = 0;
		int jMax = 0;
// 		long start = System.currentTimeMillis();
		matrix[0][0] = 0;
		for (int i = 1; i < len1+1; i++) {
			matrix[i][0] = (short)(matrix[i-1][0] + gap); 
		}
		for (int j = 1; j < len2+1; j++) {
			matrix[0][j] = (short)(matrix[0][j-1] + gap);
		}
		int cells = 0;
		try {
			for (int i = 1; i < len1+1; i++) {
				for (int j = 1; j < len2+1; j++) {
					short matchscore = (seq1[i-1] == seq2[j-1])?match:mismatch;
					short diag = matrix[i-1][j-1];
					short max = (short)(diag + matchscore);

					short left = matrix[i-1][j];
					short leftscore = (short)(left + gap);
					if (leftscore > max) {
						max = leftscore;
					}

					short up = matrix[i][j-1];
					short upscore = (short)(up + gap);
					if (upscore > max) {
						max = upscore;
					}
					if (max < 0) max = 0;	
					if (max > matrixmax) {
						matrixmax = max;
						iMax = i;
						jMax = j;
					}
					matrix[i][j] = max;
					cells++;
				}
			}
		} catch (OutOfMemoryError e) { System.out.println("Out of memory, dying. Cells done: " + cells); return; }
		
// 		long stop = System.currentTimeMillis();
// 		long time = stop - start;
// 		
// 		System.out.println("Calculated matrix "); 
// 		System.out.println("runtime: " + time + " millisecs. " +
// 				((double)((len1+1)*(len2+1))/(double)time) + " cells/millisec");
		
		score = matrixmax;
		
		sb1 = new StringBuffer();
		sb2 = new StringBuffer();
		sbMatch = new StringBuffer();
		matches = 0;
		mismatches = 0;
		gaps = 0;
		
		int i = iMax;
		int j = jMax;
		int step = 0;
		char dir = 'd';
		while ( i > 0 || j > 0) {
			short val = matrix[i][j];
			if (val == 0) break;
			if (dir == 'd') {
				if(i > 0) { 
					sb1.insert(0, seq1[i-1]);
				} else {
					sb1.insert(0, "-");
				}
				if (j > 0) {
					sb2.insert(0, seq2[j-1]);
				} else {
					sb2.insert(0, "-");
				}
				if (i > 0 && j > 0) {
					if(seq1[i-1] == seq2[j-1]) {
						sbMatch.insert(0, "|");
						matches++;
					} else {
						sbMatch.insert(0, " ");
						mismatches++;
					}
				} else {
					sbMatch.insert(0, " ");
// 					mismatches++;
				}
			} else if (dir == 'l') {
				sb1.insert(0, seq1[i-1]);
				sb2.insert(0, "-");
				sbMatch.insert(0, " ");
				gaps++;
			} else if (dir == 'u') {
				sb1.insert(0, "-");
				sb2.insert(0, seq2[j-1]);
				sbMatch.insert(0, " ");
				gaps++;
			} else {
				System.out.println("Direction screwed up when adding to seqs!");
				return;
			}
			
			dir = ' ';
			if (i == 0) {
				dir = 'u';
			} else if (j == 0) {
				dir = 'l';
			} else {
				int matchval = (seq1[i-1] == seq2[j-1])?match:mismatch;
				if ( val - gap == matrix[i][j-1]) {
					dir = 'u';
				} else if( val - gap == matrix[i-1][j]) {
					dir = 'l';
				} else if( val - matchval == matrix[i-1][j-1]){
					dir = 'd';
				}  
			}
// 			System.out.println("Step " + ++step + ": " + dir);
			if (dir == 'd') {//diag
				i--;
				j--;
			} else if (dir == 'l') {	//left
				i--;
			} else if (dir == 'u') { //up
				j--;
			} else {
				System.out.println("Direction finding screwed up! Exiting.");
				return;
			}
		}
		range1 = new SeqRange(range1.getStart() + i, range1.getStart() + iMax - 1);
		range2 = new SeqRange(range2.getStart() + j, range2.getStart() + jMax - 1);
	}
	
// 	/**
// 	 * testing
// 	 */
// 	public static void main(String[] args) {
// 		File nwf1 = new File("MMENDOBA_3560-3580");
// 		File nwf2 = new File("HSKER101_3763-3783");
// 		Entry ent1 = null;
// 		Entry ent2 = null;
// 		try {
// 			ent1 = new Entry(nwf1);
// 			ent2 = new Entry(nwf2);
// 		} catch (FileNotFoundException e) { System.out.println(e); }
// 		Exon ex1 = new Exon(ent1, new SeqRange(1,21));
// 		Exon ex2 = new Exon(ent2, new SeqRange(1,21));
// 		SWAlign al = new SWAlign(ex1, ex2);
// 		System.out.print("\t\t");
// 		for (int n=0; n < al.seq1.length; n++) System.out.print(al.seq1[n] + "\t");
// 		System.out.print("\n");
// 		for (int j = 0; j <= 21; j++) {
// 			if (j>0) System.out.print(al.seq2[j-1]);
// 			System.out.print("\t");
// 			for (int i = 0; i <= 21; i++) {
// 				System.out.print(al.matrix[i][j] + "\t");
// 			}
// 			System.out.print("\n");
// 		}
// 	}

}

