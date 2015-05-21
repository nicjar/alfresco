/*
 * $Revision: 1.1 $
 * $Id: SeqFeature.java,v 1.1 2003/04/04 10:15:05 niclas Exp $
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
 * An abstract Glyph class that represents a sequence feature. A SeqFeature
 * is associated with an Entry and a SeqRange
 * @see alfresco.Glyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public abstract class SeqFeature extends Glyph{
/**
 * The Entry the feature belongs to
 */
	Entry entry;
/**
 * The range of the feature
 */
	SeqRange range;
/**
 * If the SeqFeature is predicted
 */
	boolean predicted = false;
/**
 * Name of prediction method
 */
	String predMethod;
/**
 * A probability score associated with the SeqFeature
 */
	double score = 0;
/**
 * Frame of SeqFeature either 0, 1, or 2. A value of -1 == undef
 */
	int frame = -1;
	

/**
 * Creates a SeqFeature without specifying the range
 * @param ent parent Entry
 */	
	public SeqFeature(Entry ent) {
 		super();
		entry = ent;
		complement = false;
		coord = entry.coord;
		tier = coord.entryTier;
	}
		
/**
 * Creates a SeqFeature of the specified Entry, with the specified start and stop
 * on the upper strand
 * @param entry parent Entry
 * @param start feature start
 * @param stop feature stop
 */
	SeqFeature(Entry ent, int start, int stop) {
		super();
		entry = ent;
		complement = false;
		range = new SeqRange(start, stop, complement);
		coord = entry.coord;
		tier = coord.entryTier;
	}
	
/**
 * Creates a SeqFeature of the specified Entry, with the specified range
 * on the upper strand
 * @param entry parent Entry
 * @param r range of feature
 */
	SeqFeature(Entry ent, SeqRange r) {
		super();
		entry = ent;
		range = r;
		complement = r.complement;
		coord = entry.coord;
		if (coord != null) {
			tier = coord.entryTier;
		}
	}
	
/**
 * Creates a SeqFeature of the specified Entry, with the specified start and stop
 * on the strand specified by compl
 * @param entry parent Entry
 * @param start feature start
 * @param stop feature stop
 */
	SeqFeature(Entry ent, int start, int stop, boolean compl) {
		super();
		entry = ent;
		range = new SeqRange(start, stop, compl);
		complement = compl;
		coord = entry.coord;
		tier = coord.entryTier;
	}
	
/**
 * Creates a SeqFeature of the specified Entry, with the specified range
 * on the strand specified by compl
 * @param entry parent Entry
 * @param r range of feature
 */
	SeqFeature(Entry ent, SeqRange r, boolean compl) {
		super();
		entry = ent;
		range = r;
		complement = compl;
		coord = entry.coord;
		tier = coord.entryTier;
	}
	
/**
 * Gets the SeqRange
 * @return range
 */
	public SeqRange getSeqRange() {
		return range;
	}

/**
 * Sets the SeqRange of the feature
 * @param range the range
 */	
	public void setSeqRange(SeqRange range) {
 		this.range = range;
 }	
/**
 * Gets the parent Entry
 * @return parent Entry
 */
	public Entry getEntry() {
		return entry;
	}
	
	/**
	 * Sets the tier of the SeqFeature.
	 * Note! Use absolute value of tier! Will set it to positive or negative
	 * depending on the tier of coordVal
	 * @param tier absolute tier value
	 */
	public void setTier(int tier) {
		int t = Math.abs(tier);
		if (coord.entryTier > 0) {
			super.setTier(t);
		} else {
			super.setTier(-t);
		}
	}
	/**
	 * Gets the absolute tier of the SeqFeature.
	 * Note! Will return a positive value even if the feature has a negative tier
	 * @return absolute tier value
	 */
	public int getTier() {
    return Math.abs(super.getTier());
	}
	
/**
 * Gets the DNA sequence of the feature as a String
 * @return DNA sequence
 */
	public String getSequence() {
		return entry.getSequence(range.getStart(), range.getStop());
	}

/**
 * Gets the repeat masked DNA sequence of the feature as a String
 * @return masked DNA sequence
 */
	public String getRepeatMaskedSequence() {
		return entry.getRepeatMaskedSequence(range);
	}

/**
 * Evaluates if the SeqFeature equals another object
 * @return true if SeqFeature equals object, otherwise false
 * @param obj The object to evaluate	
 */
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
// 		if (obj instanceof this.getClass().getName()) {
			SeqFeature sf = (SeqFeature) obj;
			if (this.entry.equals(sf.entry) && this.range.equals(sf.range) ) {
				return true;
			}
		}
		return false;
	}

/**
 * Evaluates if the SeqFeature overlaps another SeqFeature
 * @return true if SeqFeature overlaps SeqFeature, otherwise false
 * @param sf The SeqFeature to evaluate	
 */
	public boolean overlap(SeqFeature sf) {
		if (this.entry.equals(sf.entry))
			if ( this.tier == sf.tier )
				if ( (this.complement && sf.complement) || (!this.complement && !sf.complement) )
					if (this.range.overlap(sf.range))
						return true;
		return false;
	}

/**
 * Sets whether the SeqFeature is predicted or not
 * @param pred true if SeqFeature is predicted, otherwise false
 */	
	public void predicted(boolean pred) {
 		predicted = pred;
	}	

/**
 * Determines if SeqFeature is predicted or not
 * @return true if SeqFeature is predicted, otherwise false
 */	
	public boolean isPredicted() {
		return predicted;
	}
	
	/**
	 * Sets the name of the prediction method for the SeqFeature
	 * @param method method name
	 */
	public void setPredMethod(String method) {
		predMethod = method;
	}
	
	/**
	 * Gets the name of the prediction method
	 * @return method name
	 */
	public String getPredMethod() {
		return predMethod;
	}
	
	/**
	 * Sets the score for the SeqFeature
	 * @param s score
	 */
	public void setScore(double s) {
		score = s;
	}
	
	/**
	 * Gets the score for the SeqFeature
	 * @return score 
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Sets the frame for the SeqFeature
	 * @param f frame (0-2)
	 */
	public void setFrame(int f) {
		frame = (f > -1 && f < 3)? f: (-1);
	}
	
	/**
	 * Gets the frame for the SeqFeature
	 * @return score 
	 */
	public int getFrame() {
		return frame;
	}
	
/**
 * Gets the boundingRect of the SeqFeature
 * @return Rectangle defining the bounds of the SeqFeature
 */
 	public Rectangle getBounds() {
// 		if (visible) {
// 			return boundingRect;
// 		}
// 		return null;
			if (tier == coord.entryTier) {
				if (!complement) {
					canvY = coord.originY - canvHeight;
				} else {
					canvY = coord.originY;
				}
	// 			canvY = coord.originY - canvHeight/2;
			} else {
				int diff = coord.tierDiff(tier);
				if (tier > 0) { 
					if (!complement) {
						canvY = coord.originY - canvHeight - diff; 
					} else {
						canvY = coord.originY - diff;
					}
				} else if (tier < 0) { 
					if (!complement) {
						canvY = coord.originY - canvHeight + diff; 
					} else {
						canvY = coord.originY + diff;
					}
				}
				else { System.out.println("Kablooeeyh! Tier is 0!"); }
			}
			zoom = coord.zoom;
// 			canvX =  coord.originX + (int) Math.round(range.begin*zoom);
// 			canvWidth = (int) (range.length() * zoom);
// 			if (canvWidth == 0) { canvWidth = 1; }

			realX =  coord.originX + (int) Math.round(range.begin*zoom);
			realWidth = (int) Math.round(range.length() * zoom);
			if (realWidth == 0) { realWidth = 1; }
			realXend = realX + realWidth;
			if (realX > ABSXMAX || realXend < -ABSXMAX) { //outside drawing area
				if (realX > ABSXMAX) {
					canvX = ABSXMAX;
				} else {
					canvX = -(ABSXMAX + 2);
				}
// 				canvX = realX;	// this will be caught by Reciprocal and not drawn, I hope...
// 				canvWidth = realWidth;
// 				boundingRect = null;
				canvWidth = 1;
// 				boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
// 				return;
				return new Rectangle(canvX, canvY, canvWidth, canvHeight);
			}
			if (realX < -ABSXMAX) {
				canvX = -ABSXMAX;
			} else {
				canvX = realX;
			}
			if (realXend > ABSXMAX) {
				canvWidth = ABSXMAX - canvX;
			} else {
// 				canvWidth = realWidth;
				canvWidth = realXend - canvX;
			}
			
			boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
			return boundingRect;
// 			return new Rectangle(canvX, canvY, canvWidth, canvHeight);
		
	}
	
/**
 * Draws SeqFeature to Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		// default drawing method
		if (visible) {
	// 		System.out.println("drawing SeqFeature " + this);
	// 		System.out.println(coord + " zoom: " + zoom);
// 			if (tier == coord.entryTier) {
// 				if (!complement) {
// 					canvY = coord.originY - canvHeight;
// 				} else {
// 					canvY = coord.originY;
// 				}
// 	// 			canvY = coord.originY - canvHeight/2;
// 			} else {
// 				int diff = coord.tierDiff(tier);
// 				if (tier > 0) { 
// 					if (!complement) {
// 						canvY = coord.originY - canvHeight - diff; 
// 					} else {
// 						canvY = coord.originY - diff;
// 					}
// 				} else if (tier < 0) { 
// 					if (!complement) {
// 						canvY = coord.originY - canvHeight + diff; 
// 					} else {
// 						canvY = coord.originY + diff;
// 					}
// 				}
// 				else { System.out.println("Kablooeeyh! Tier is 0!"); }
// 			}
// 			zoom = coord.zoom;
// // 			canvX =  coord.originX + (int) Math.round(range.begin*zoom);
// // 			canvWidth = (int) (range.length() * zoom);
// // 			if (canvWidth == 0) { canvWidth = 1; }
// 
// 			realX =  coord.originX + (int) Math.round(range.begin*zoom);
// 			realWidth = (int) (range.length() * zoom);
// 			if (realWidth == 0) { realWidth = 1; }
// 			realXend = realX + realWidth;
// 			if (realX > ABSXMAX || realXend < -ABSXMAX) { //outside drawing area
// 				if (realX > ABSXMAX) {
// 					canvX = ABSXMAX;
// 				} else {
// 					canvX = -(ABSXMAX + 2);
// 				}
// // 				canvX = realX;	// this will be caught by Reciprocal and not drawn, I hope...
// // 				canvWidth = realWidth;
// // 				boundingRect = null;
// 				canvWidth = 1;
// 				boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
// 				return; 
// 			}
// 			if (realX < -ABSXMAX) {
// 				canvX = -ABSXMAX;
// 			} else {
// 				canvX = realX;
// 			}
// 			if (realXend > ABSXMAX) {
// 				canvWidth = ABSXMAX - canvX;
// 			} else {
// // 				canvWidth = realWidth;
// 				canvWidth = realXend - canvX;
// 			}
// 			
// 			boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
			boundingRect = getBounds();
			g.setColor(fillColor);
			g.fillRect(canvX, canvY, canvWidth, canvHeight);
		}
	}
	
/**
 * String representation of SeqFeature
 * @return string representation
 */
	public String toString() {
// 		String classn = this.getClass().getName();
// 		int dotind = classn.lastIndexOf('.');
// 		if (dotind >= 0) {
// 			classn = classn.substring(dotind + 1);
// 		}
		StringBuffer sb = new StringBuffer();
		if (this.isPredicted()) sb.append("Predicted ");
		sb.append(this.getClassName() + " " + entry + " "+ range);
		if (predMethod != null) sb.append(", " + predMethod);
		if (score > 0) sb.append(" Score: " + score);
		return new String(sb);
	}

/**
 * Returns a name for a FastaFile for the feature
 * @return subsequence file name
 */
	public String getSubSeqFileName() {
		return "." + entry.getFilename() + "_" + range.getStart() + "-" + range.getStop();
	}
	
/**
 * Gets a gff line description of the feature
 * @return gff line
 */
	public String gffString() {
		StringBuffer sb = new StringBuffer();
		sb.append(entry.getFilename());
		if (predMethod == null) {
			sb.append("\tAlfresco\t");
		} else {
			sb.append("\t" + predMethod + "\t");
		}
		sb.append(this.getClassName()+"\t");
		sb.append(String.valueOf(range.getStart())+"\t");
		sb.append(String.valueOf(range.getStop())+"\t");
		if (score != 0) {
			sb.append(String.valueOf(score) + "\t");
		} else {
			sb.append("0\t");
		}
		if (!complement) {
			sb.append("+\t");
		} else {
			sb.append("-\t");
		}
		if (frame > -1) {
			sb.append(String.valueOf(String.valueOf(frame)));
		} else {
			sb.append(String.valueOf("."));
		}
		return new String(sb);
	}
}

