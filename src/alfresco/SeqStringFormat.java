/*
* $Revision: 1.1 $
* $Id: SeqStringFormat.java,v 1.1 2003/04/04 10:15:08 niclas Exp $
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
 * Class for formating a DNA sequence string for display in a SequenceDisplay
 * window
 * @see alfresco.SequenceDisplay
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class SeqStringFormat {
/**
 * Source Entry of the sequence
 */
	Entry entry;
/**
 * Range of the Entry that defines the sequence
 */
	SeqRange range;
/**
 * Array of characters to hold the sequence
 */
	char[] seqArr;
/**
 * The formated sequence
 */
	String formatSeq;
	
/**
 * Creates a SeqStringFormat from the specified entry and range
 * @param ent source entry
 * @param r defining range
 */
	SeqStringFormat(Entry ent, SeqRange r) {
		entry = ent;
		range = r;
// 		String seq = entry.getSequence(range).toUpperCase();
		String seq = entry.getSequence(range);
		seqArr = seq.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < seqArr.length; i++) {
			sb.append(seqArr[i]);
			if ((i+1) % 50 == 0) {
				sb.append(" " + (i+ range.getStart()) + "\n");
				if (i != seqArr.length-1) {
					sb.append(alignRight(i+ range.getStart() +1) + " ");
				}
			}
		}
		sb.insert(0, alignRight(range.getStart()) + " ");
		sb.append("\n");
		formatSeq = sb.toString();
		
	}

/**
 * Creates a SeqStringFormat from the specified string and range. Used to
 * format sequence strings of alignments (i.e. might have gaps).
 * @param seq the sequence string
 * @param r defining range
 */	
	public SeqStringFormat(String seq, SeqRange r) {
		range = r;
		boolean comp = range.isComplement();
//  		seqArr = seq.toUpperCase().toCharArray();
 		seqArr = seq.toCharArray();
 		StringBuffer sb = new StringBuffer();
 		int gaps = 0;
		for (int i = 0; i < seqArr.length; i++) {
			if (seqArr[i] == '-') gaps++;
			sb.append(seqArr[i]);
			if ((i+1) % 50 == 0) {
				if (!comp) {
					sb.append(" " + (i+ range.getStart() - gaps) + "\n");
				} else {
					sb.append(" " + (range.getStop() - (i - gaps)) + "\n");
				}
				if (i != seqArr.length-1) {
					if (!comp) {
						sb.append(alignRight(i+ range.getStart() + 1 - gaps) + " ");
					} else {
						sb.append(alignRight(range.getStop() - (i - gaps) -1 ) + " ");
					}
				}
			}
		}
		if (!comp) {
			sb.insert(0, alignRight(range.getStart()) + " ");
			sb.append(" " + range.getStop() + "\n");	
		} else {
			sb.insert(0, alignRight(range.getStop()) + " ");
			sb.append(" " + range.getStart() + "\n");
		}
		
		formatSeq = sb.toString();
 }
 
 /**
 * Creates a SeqStringFormat from the specified sequence string
 * @param seq sequence string
 */
	SeqStringFormat(String seq) {
// 		entry = ent;
// 		range = r;
// // 		String seq = entry.getSequence(range).toUpperCase();
// 		String seq = entry.getSequence(range);
		seqArr = seq.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < seqArr.length; i++) {
			sb.append(seqArr[i]);
			if ((i+1) % 50 == 0) {
				sb.append(" " + (i+ 1) + "\n");
				if (i != seqArr.length-1) {
					sb.append(alignRight(i+ 1 +1) + " ");
				}
			}
		}
		sb.insert(0, alignRight(1) + " ");
		sb.append("\n");
		formatSeq = sb.toString();
		
	}

 
/**
 * Inserts appropriate number of spaces before leading position number to
 * line up first base on line
 * @return position as a String with leading spaces
 * @param i position
 */
	private String alignRight(int i) {
		StringBuffer sb = new StringBuffer(String.valueOf(i));
		int spaces = 0;
		if (i/10 == 0) {
			spaces = 5;
		} else if (i/100 == 0) {
			spaces = 4;
		} else if (i/1000 == 0) {
			spaces = 3;
		} else if (i/10000 == 0) {
			spaces = 2;
		} else if (i/100000 == 0) {
			spaces = 1;
		}
		
		if (spaces > 0) {
			for (int j = 0; j < spaces; j++) {
				sb.insert(0, " ");
			}
		}
		return new String(sb);
	}
	
	
	/**
	 * Gets the length of the sequence
	 * @return sequence length
	 */
	public int getSeqLength() {
		return seqArr.length;
	}
/**
 * Gets the formated sequence string
 * @return formated sequence string
 */
	public String getFormatSeq() {
		return formatSeq;
	}
/**
 * Gets the unformated sequence string
 * @return unformated sequence string
 */
	public String getSeqString() {
		return new String(seqArr);
	}
}
