/*
* $Revision: 1.1 $
* $Id: SeqRange.java,v 1.1 2003/04/04 10:15:06 niclas Exp $
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

/**
 * Describes an inclusive sequence range.
 * A length 1 range is a position
 * @see java.io.Serializable
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class SeqRange implements Serializable{
// 	Entry entry;
	int begin;
	int end;
/**
 * Whether the range is reverse complement or not
 */
	public boolean complement;
	
/**
 * Default constructor, same as <pre>new SeqRange(0, 0, false)</pre>
 */
	public SeqRange() {
		begin = 0; 
		end = 0; 
		complement = false; 
	}
	
/**
 * Creates SeqRange with specified begining and end, NOT reverse complement
 * @param start start position of range
 * @param stop end position of range
 */
	public SeqRange(int start, int stop) { 
		begin = start; 
		end = stop; 
		complement = false; 
	}

/**
 * Creates SeqRange with specified begining and end, reverse complement
 * as specified by compl
 * @param start start position of range
 * @param stop end position of range
 * @param compl true if range is reverse complement, otherwise false
 */
	public SeqRange(int start, int stop, boolean compl) { 
		begin = start; 
		end = stop; 
		complement = compl; 
	}
	
// 	SeqRange(Entry ent) {
// 		begin = 0; 
// 		end = 0; 
// 		inititalize(ent);	
// 	}
	
	
// 	SeqRange(int start, int stop, Entry ent) { 
// 		begin = start; 
// 		end = stop; 
// 		inititalize(ent);
// 	}
	
/**
 * Creates SeqRange with the values of other SeqRange
 * @param other other SeqRange
 */
	public SeqRange(SeqRange other) { 
		begin = other.begin; 
		end = other.end; 
		complement = other.complement;
// 		inititalize(other.entry);
	}

// 	private	void inititalize(Entry ent) {
// 		entry = ent;
// //		canvHeight = 10;
// 	}
	
/**
 * Sets begin and end
 * @param start start position of range
 * @param stop end position of range
 */
	public void setRange(int start, int stop) { begin = start; end = stop; }
	
/**
 * Sets begin
 * @param start start position of range
 */
	public void setStart(int start) { begin = start; }
	
/**
 * Sets end
 * @param stop end position of range
 */
	public void setStop(int stop) { end = stop; }
	
/**
 * Gets begin
 * @return begin
 */
	public int getStart() { return begin; }
	
/**
 * Gets end
 * @return end
 */
	public int getStop() { return end; }
	
/**
 * Gets length of range
 * @return something
 */
	public int length() { return end-begin+1; }
	
/**
 * Determines if SeqRange is a length-1 range
 * @return true if length == 1, otherwise false
 */
	public boolean isPosition() { return begin==end?true:false; }
	
/**
 * Determines if SeqRange is equal to another SeqRange
 * @return true if range is same as other range, otherwise false
 * @param other the other SeqRange
 */
	public boolean isEqual(SeqRange other) { 
		return this.begin == other.begin && this.end == other.end?true:false;
	}
	
/**
 * Determines if SeqRange is reverse complement
 * @return true if range is reverse complement, otherwise false
 */
	public boolean isComplement() {
	 return complement;
	}
	
	/**
	 * Sets complement to boolean value specified
	 * @param compl true if range is reverse complement, otherwise false
	 */
	public void setComplement(boolean compl) {
		complement = compl;
	}
	
	/**
	 * Returns a new SeqRange double the length with the same centre as
	 * the specified range. Checks that range is not out of bounds of the
	 * specified Entry.
	 * @return doubled range
	 * @param ent the Entry the SeqRange belongs to
	 */
	public SeqRange getDoubleRange(Entry ent) {
		int newstart = begin - length()/2;
		if (newstart < 1) newstart = 1;
		int newstop = end + length()/2;
		if (newstop > ent.getLength()) newstop = ent.getLength();
		return new SeqRange(newstart, newstop);
	}
	
	/**
	 * Returns a new SeqRange three times the length with the same centre as
	 * the specified range. Checks that range is not out of bounds of the
	 * specified Entry.
	 * @return trippeled range
	 * @param ent the Entry the SeqRange belongs to
	 */
	public SeqRange getTrippleRange(Entry ent) {
		int newstart = begin - length();
		if (newstart < 1) newstart = 1;
		int newstop = end + length();
		if (newstop > ent.getLength()) newstop = ent.getLength();
		return new SeqRange(newstart, newstop);
	}

/**
 * Determines if SeqRange overlaps another SeqRange
 * @return true if range overlaps other range, otherwise false
 * @param other the other SeqRange
 */
	public boolean overlap(SeqRange other) {
		if ( this.begin >= other.begin && this.begin <= other.end ) {
			return true;
		}
		if ( this.end >= other.begin && this.end <= other.end ) { 
			return true; 
		}
		if ( this.begin <= other.begin && this.end >= other.end ) {
			return true;
		}
		return false;
	}
	
// 	String getSequence (int begin, int end){
// 		if (entry != null) {
// 			return entry.getSequence(begin, end);
// 		}
// 		return null;
// 	}
	
	
/**
 * String representation of SeqRange
 * @return string representation
 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		Integer b = new Integer(begin);
		buf.append(b.toString());
		buf.append("-");
		Integer e = new Integer(end);
		buf.append(e.toString());
		buf.append("]");
		return buf.toString();
	}
/**
 * Suffix String representation of SeqRange (_start-end)
 * @return suffix string representation
 */
	public String toSuffixString() {
		StringBuffer buf = new StringBuffer();
		buf.append("_");
		Integer b = new Integer(begin);
		buf.append(b.toString());
		buf.append("-");
		Integer e = new Integer(end);
		buf.append(e.toString());
// 		buf.append("]");
		return buf.toString();
	}
}
