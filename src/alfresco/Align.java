/*
 * $Revision: 1.1 $
 * $Id: Align.java,v 1.1 2003/04/04 10:13:43 niclas Exp $
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
 * Class description
 * @see 
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public abstract class Align {
/**
 * seq 1 char array
 */
	char [] seq1;
/**
 * seq2 char array	
 */
	char [] seq2;
/**
 * length of seq1 array
 */
	int len1;
/**
 * length of seq2 array
 */
	int len2;
/**
 * range for seq 1
 */ 	
	SeqRange range1;
/**
 * range for seq 2
 */ 
	SeqRange range2;
/**
 * dynamic programing matrix	
 */
	short [][] matrix;
/**
 * AlignmentBlock object for storing the alignment	
 */
	AlignmentBlock alB;
/**
 * Match score	
 */
	short match;
/**
 * Mismatch score	
 */	
	short mismatch;
/**
 * Gap score	
 */		
	short gap;
/**
 * Buffer for aligned seq1	
 */
	StringBuffer sb1;
/**
 * Buffer for aligned seq2	
 */
	StringBuffer sb2;
/**
 * Buffer for match string	
 */	
	StringBuffer sbMatch;
/**
 * Number of matches	
 */
	int matches;
/**
 * Number of mismatches	
 */	
	int mismatches;
/**
 * Number of gaps	
 */	
	int gaps;
/**
 * Score of the alignment	
 */
	int score;
	
	/**
	 * Creates an Align object from the SeqFeatures specified
	 * @param feature1 first feature
	 * @param feature2 second feature
	 */
	public Align(SeqFeature feature1, SeqFeature feature2) {
		setDefaultParameters();
		alignFeatures(feature1, feature2);
	}

		/**
	 * Creates an Align object from the SeqFeatures specified
	 * @param feature1 first feature
	 * @param feature2 second feature
	 * @param match match score
	 * @param mismatch mismatch score
	 * @param gap gap score
	 */
	public Align(SeqFeature feature1, SeqFeature feature2, int match, int mismatch, int gap) {
		this.match = (short)match;
		this.mismatch = (short)mismatch;
		this.gap = (short)gap;
		alignFeatures(feature1, feature2);
	}
	
	/**
	 * Sets default parameters. Subclasses have to implement this method.
	 */
	public abstract void setDefaultParameters() ;
	
	/**
	 * Makes the seq arrays from the features specified and match, mismatch, and gap
	 * @param feature1 first feature
	 * @param feature2 second feature
	 */
	public void alignFeatures(SeqFeature feature1, SeqFeature feature2) {
		seq1 = feature1.getSequence().toCharArray();
		seq2 = feature2.getSequence().toCharArray();
		range1 = feature1.getSeqRange();
		range2 = feature2.getSeqRange();
		align();
		alB = new AlignmentBlock( new String(sb1),
		                          new String(sb2),
		                          new String(sbMatch),
		                          range1,
		                          range2,
		                          feature1.getEntry(),
		                          feature2.getEntry());
// 		System.out.println("Created " + alB);
		int ident = (int) Math.round( (100.0 * matches) / (matches + mismatches) );
		alB.setValues(ident, gaps, score);
	}
	
	/**
	 * Does the actual aligning
	 */
	public void align() {
		len1 = seq1.length;
		len2 = seq2.length;
		matrix = new short[len1+1][len2+1];
// 		System.out.println("Created matrix " + (len1+1) + "x" + (len2+1) + 
// 				" = " + ((len1+1)*(len2+1)) + " cells" );
		doDynamicProgramming();
	}
	
	/**
	 * An abstract method to be implemented by subclasses for the actual alignment algorithm
	 * Must set sb1, sb2, sbMatch, matches, mismatches, gaps, and score
	 */
	public abstract void doDynamicProgramming();
	
	/**
	 * gets alignment block
	 * @return alignment block
	 */
	public AlignmentBlock getAlignmentBlock() {
		return alB;
	}

}
