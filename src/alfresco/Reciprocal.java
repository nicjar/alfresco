/*
* $Revision: 1.1 $
* $Id: Reciprocal.java,v 1.1 2003/04/04 10:14:49 niclas Exp $
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
 * A Glyph to hold information of reciprocal regions of two entries.<p>
 * It is possible to create a feature-less reciprocal, features can be set later
 * @see alfresco.Glyph
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public class Reciprocal extends Glyph {
	Entry entry1;
	Entry entry2;
/**
 * Feature of Entry 1
 */
	SeqFeature feature1;
/**
 * Feature of Entry 2
 */
	SeqFeature feature2;
/**
 * X position of feature 1
 */
	int topX; 
/**
 * Y position of feature 1
 */
	int topY; 
/**
 * X position of feature 2
 */
	int bottomX;
/**
 * Y position of feature 2
 */
	int bottomY;
/**
 * A score possibly associated with the reciprocality	
 */
	double score = 0;
/**
 * Empty default constructor
 */
	private Reciprocal() {}
	
/**
 * Creates a Reciprocal with the specified features
 * @param f1 feature 1
 * @param f2 feature 2
 */
	public Reciprocal(SeqFeature f1, SeqFeature f2) {
		feature1 = f1;
		feature2 = f2;
		entry1 = f1.entry;
		entry2 = f2.entry;
		fillColor = Color.gray;
		if (!feature1.isVisible() || !feature2.isVisible())
			setVisible(false);
	}
	
/**
 * Sets SeqFeatures
 * @param f1 feature 1
 * @param f2 feature 2
 */
	public void setFeatures(SeqFeature f1, SeqFeature f2) { feature1 = f1; feature2 = f2; }
	
	/**
	 * Gets feature1
	 * @return feature
	 */
	public SeqFeature getFeature1() {
		return feature1;
	}
	
	/**
	 * Gets feature2
	 * @return feature
	 */
	public SeqFeature getFeature2() {
		return feature2;
	}
	
	/**
	 * Determines if the reciprocal contains the specified SeqFeature.
	 * @return true if contains feature, otherwise false
	 * @param feature feature to check
	 */
	public boolean containsFeature(SeqFeature feature) {
		if(feature.equals(feature1) || feature.equals(feature2)) return true;
		return false;
	}
	
	/**
	 * gets the score associated with the reciprocal, e.g. an 
	 * alignment score
	 * @return score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * sets the score associated with the reciprocal, e.g. an 
	 * alignment score
	 * @param s score value
	 */
	public void setScore(double s) {
		score = s;
	}
	
/**
 * Draws the Reciprocal to the Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		if (visible) {
			if((feature1.canvX > ABSXMAX || feature1.canvX+canvWidth < -ABSXMAX) || (feature2.canvX > ABSXMAX || feature2.canvX+canvWidth < -ABSXMAX) ) {
				boundingRect = null;
				return;
			}
			topX = feature1.canvX + feature1.canvWidth/2;
			bottomX = feature2.canvX + feature2.canvWidth/2;
			topY = feature1.canvY + feature1.canvHeight;
			bottomY = feature2.canvY;
			g.setColor(fillColor);
			g.drawLine(topX, topY, bottomX, bottomY);
			int x = -1;
			int w = -1;
			if (topX > bottomX) {
				x = bottomX;
				w = topX - bottomX;
			} else {
				x = topX;
				w = bottomX - topX;
			}
			boundingRect = new Rectangle(x, topY, w, bottomY - topY);
		}
	}
	
/**
 * Calls dotter for the two features. Two fasta files with the sequences
 * of the features, and a dotter feature file for the entries are created in the TMPDIR
 */
	public void callDotter() {
		SeqRange range1 = feature1.getSeqRange();
		SeqRange range2 = feature2.getSeqRange();
		String fn1 = entry1.getFilename() + "_" + range1.getStart() + "-" + range1.getStop();
		String fn2 = entry2.getFilename() + "_" + range2.getStart() + "-" + range2.getStop();
		FastaFile ff1 = new FastaFile(SystemConstants.TMPDIR, fn1, entry1, range1);
		FastaFile ff2 = new FastaFile(SystemConstants.TMPDIR, fn2, entry2, range2);
		ff1.write();
		ff2.write();
		String featurefn = entry1.getFilename() + "-" + entry2.getFilename() + ".ftr";
		DotterFeatureFile featuref = new DotterFeatureFile(SystemConstants.TMPDIR, featurefn, entry1, entry2);
		featuref.write();
		
		int qOffset = range1.getStart()-1;
		int sOffset = range2.getStart()-1;
		Runtime rt = Runtime.getRuntime();
		try {
// 			rt.exec(DOTTERPATH + "dotter -f " + featurefn + " -q " + qOffset + " -s " + sOffset + " " + fn1 + " " + fn2);
// 			rt.exec(DOTTERPATH + "dotter -f " + featuref.getAbsolutePath() + " -q " + qOffset + " -s " + sOffset + " " + ff1.getAbsolutePath() + " " + ff2.getAbsolutePath());
			Process p = rt.exec(SystemConstants.DOTTERPATH + "dotter -f " + featuref.getAbsolutePath() + " -q " + qOffset + " -s " + sOffset + " " + ff1.getAbsolutePath() + " " + ff2.getAbsolutePath());
// 			rt.exec("dotter -q " + qOffset + " -s " + sOffset + " " + fn1 + " " + fn2);
			InputStream stderr = p.getErrorStream();
			InputStreamReader stderrr = new InputStreamReader(stderr);
			BufferedReader stderrbr = new BufferedReader(stderrr);
			String errline;
			errline = stderrbr.readLine();
			while (errline != null) {
				System.out.println(errline);
				errline = stderrbr.readLine();
			}

		} catch (IOException ioe) { System.out.println(ioe); }
	}	
	
/**
 * Returns a line in cgff format of the two features.
 * <pre>&lt;feature_type1&gt;	&lt;start&gt;	&lt;stop&gt;	&lt;feature_type2&gt;	&lt;start&gt;	&lt;stop&gt;</pre>
 * @return a cgff line
 */
	public String cgffString() {
		StringBuffer sb = new StringBuffer();
		sb.append(feature1.getClassName()+"\t");
		sb.append(feature1.getSeqRange().getStart()+"\t");
		sb.append(feature1.getSeqRange().getStop()+"\t");
		sb.append(feature2.getClassName()+"\t");
		sb.append(feature2.getSeqRange().getStart()+"\t");
		sb.append(feature2.getSeqRange().getStop());
		return new String(sb);		
	}
	
/**
 * String representation of the Reciprocal
 * @return string representation
 */
	public String toString() {
		return "Reciprocal: " + feature1 + " - " + feature2;
	}
	
}
