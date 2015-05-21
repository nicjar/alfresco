/*
 * $Revision: 1.3 $
 * $Id: EntryPair.java,v 1.3 2003/04/08 08:12:05 niclas Exp $
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
 * For more information contact Niclas at niclas.jareborg@cgr.ki.se
 */
package alfresco;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.zip.*;
import alfresco.corba_wrappers.*;

/**
 * A class to hold a pair of Entry objects and related information<p>
 * Top Glyph to add to SeqCanvas
 * @see alfresco.CompositeGlyph
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */
 
public class EntryPair extends CompositeGlyph implements Observer, SystemConstants{
/**
 * Application
 */
	transient Wcomp wc;
	Entry entry1;
	Entry entry2;
  
  static final long serialVersionUID = 4598612061724061369L;
/**
 * ReciprocalSet to hold Reciprocal objects
 */
	ReciprocalSet reciprocals;	
/**
 * DotterRegionSet to hold dotterRegion objects
 */
	transient DotterRegionSet dotterRegions;
/**
 * Currently selected Glyph
 */
	transient Glyph selectedGlyph;
/**
 * Vector of currently selected Glyphs
 */
	transient GlyphVector selectedGlyphs;
/**
 * Current RegionSet
 */
	transient RegionSet currentRegionSet;
/**
 * Holds Selectors
 */
	transient SelectorPair selectors;
// 	transient Selector selector;
/**
 * Flag for knowing if dotterRegions have been saved when EntryPair is saved to file	
 */
	boolean drSaved = false;
	
  transient File saveFile;
/**
 * Default constructor
 */
	public EntryPair() {
		super();
		init();	
	}
	
/**
 * Creates new EntryPair.
 * @param wc the application object
 */
	public EntryPair(Wcomp wc) {
		super();
		this.wc = wc;
		init();
	}
	
/**
 * Creates new EntryPair with the Entries specified.
 * @param wc the application object
 * @param ent1 first Entry
 * @param ent2 second Entry
 */
	public EntryPair(Wcomp wc, Entry ent1, Entry ent2) {
		super();
		this.wc = wc;
		setEntry1(ent1);
		setEntry2(ent2);
		setChildren();
		boundingRect = getBounds();
		init();
	} 
	
/**
 * Creates new EntryPair from the alfresco.server.PairStruct specified.
 * @param wc the application object
 * @param pair pairstruct
 */
	public EntryPair(Wcomp wc, alfresco.server.PairStruct pair) {
		super();
		this.wc = wc;
		setEntry1(new Entry(pair.fasta1));
		setEntry2(new Entry(pair.fasta2));
		setChildren();
		init();
		try {
			CgffParser p = new CgffParser(this, pair);
		} catch (Exception e) { e.printStackTrace(); }
		boundingRect = getBounds();
	} 
	
	/**
	 * Does common initializations for all constructors
	 */
	private void init() {
		
		selectedGlyphs = new GlyphVector();
		selectors = new SelectorPair();
		selectors.parent = this;
		children.addElement(selectors);
		reciprocals = new ReciprocalSet();
		reciprocals.parent = this;
		children.addElement(reciprocals);
		wc.getTFSet().addObserver(this);
	}
	
/**
 * Sets application
 * @param wc application
 */
	void setApplication(Wcomp wc) {
		this.wc = wc;
	}
	
	/**
	 * Gets application
	 * @return application
	 */	
	public Wcomp getApplication() {
		return wc;
	}
	
/**
 * Sets first Entry
 * @param ent the Entry
 */
	public void setEntry1(Entry ent) {
		entry1 = ent;
		entry1.parent = this;
		entry1.parentEntryPair = this;
    setChildren();
    
		  entry1.setTier(1);
		  entry1.coord = new CoordVal(wc.mw.seqCan, 1);
		  entry1.addChild(new Scale(entry1));
      entry1.maskN();
//       entry1.addATGs();
		  wc.updateEntry1(entry1);
		  boundingRect = getBounds();
    
	}
	
/**
 * Sets second Entry
 * @param ent the Entry
 */
	public void setEntry2(Entry ent) {
		entry2 = ent;
		entry2.parent = this;
		entry2.parentEntryPair = this;
		setChildren();
    
		  entry2.setTier(-1);
		  entry2.coord = new CoordVal(wc.mw.seqCan, -1);
		  entry2.addChild(new Scale(entry2));
      entry2.maskN();
//       entry2.addATGs();
		  wc.updateEntry2(entry2);
		  boundingRect = getBounds();
    
	}
	/**
   * Gets entry 1
   * @return entry1
   */
  public Entry getEntry1() {
    return entry1;
  }
	/**
   * Gets entry 2
   * @return entry2
   */
  public Entry getEntry2() {
    return entry2;
  }
	/**
	 * Gets the entry with the specified filename
	 * @return entry
	 * @param param entry filename
	 */	
	public Entry getEntry(String name) {
		if (entry1.getFilename().equals(name)) {
			return entry1;
		} else if (entry2.getFilename().equals(name)) {
			return entry2;
		} 
		return null;
			
	}
	
/**
 * Checks if both Entries are set
 * @return true if both Entries are set, otherwise false
 */
	public boolean entriesSet() {
		if (entry1 == null || entry2 == null) { return false; }
		return true;
	}
	
	/**
	 * Makes sure that overlapping genes in the entries are on different tiers
	 */
	public void nudgeOverlappingGenes() {
// 		System.out.println("nudging genes");
		entry1.nudgeOverlappingGenes();
		entry2.nudgeOverlappingGenes();
	}
	
/**
 * Adds both Entries to children Vector
 */
	void setChildren() {
		if (entriesSet()) {
// 			children.addElement(entry1);
// 			children.addElement(entry2);
			// Entries added first in children Vector so that reciprocals will get the correct
			// x positions and widths when drawing
			children.insertElementAt(entry1,0); // first in Vector
			children.insertElementAt(entry2,1);	// second in Vector 
		}
	}
	
/**
 * Sets ReciprocalSet, and adds it to children Vector
 * @param r the ReciprocalSet
 */
	public void setReciprocals(ReciprocalSet r) {
		reciprocals = r;
		children.addElement(reciprocals);
	}

	/**
	 * Adds reciprocal to entry pairs reciprocal set
	 * @param r reciprocal to be added
	 */	
	public void addReciprocal(Reciprocal r) {
		if(reciprocals != null) { 
			reciprocals.addChild(r);
		}
	}
	
/**
 * Sets DotterRegionSet, and adds it to children Vector
 * @param drs the DotterRegionSet
 */
	public void setDotterRegions(DotterRegionSet drs) {
		dotterRegions = drs;
		children.addElement(drs);
	}
	
/**
 * Creates Selector and adds it to Vector of Selectors
 * @param x x position
 * @param y y position
 * @param width width of Selector
 * @param height height of Selector
 * @param singel true if the only Selector, otherwise false
 */
	public void setSelector(int x, int y, int width, int height, boolean singel){
		Selector selector;
		if (y < wc.mw.seqCanvHeight/2) {
			selector = new Selector(entry1, x, y, width, height);
		} else {
			selector = new Selector(entry2, x, y, width, height);
		}
		if (selector == null) { 
			System.out.println("Couldn't create Selector!");
			System.exit(0);
		}
		selectors.addSelector(selector, singel);
// 		if (selector != null && singel) {
// 			children.removeElement(selector);
// 		}
// 		
// 		if (y < wc.mw.seqCanvHeight/2) {
// 			selector = new Selector(entry1, x, y, width, height);
// 		} else {
// 			selector = new Selector(entry2, x, y, width, height);
// 		}
// 		children.addElement(selector);
	}
	
/**
 * Resets the SelectorPair
 */
	public void resetSelector() {
		selectors.reset();
// 		if (selector != null) {
// 			children.removeElement(selector);
// 		}
// 		selector = null;
	}
	
/**
 * removes DotterregionSet
 */
	public void removeDotterRegions() {
// 		if (dotterRegions != null) {
// 			boolean removed = children.removeElement(dotterRegions);
// 			dotterRegions = null;
// 			if (!removed) {
// 				System.out.println("dotterRegions not removed!");
// 			} else {
// 				System.out.println("dotterRegions removed!");
// 			}
// 		}
// 		System.out.println(children);
		Enumeration kids = getChildEnumeration();
		while (kids.hasMoreElements()) {
			Glyph gl = (Glyph) kids.nextElement();
			if (gl instanceof DotterRegionSet) {
				children.removeElement(gl);
			}
		}
	}
	
/**
 * Adds current RegionSet
 */
	public void addRegionSet() {
		children.addElement(currentRegionSet);
		currentRegionSet = null;
	}
	
/**
 * Selects specified Glyph and adds to vector of selected Glyphs
 * @param gl the Glyph to be selected
 */
	public void selectGlyph(Glyph gl) {
		gl.select(true);
		selectedGlyphs.addElement(gl);
// 		System.out.println("Added " + gl);
// 		System.out.println("Selected Glyphs: " + selectedGlyphs + "\n");
	}
	
/**
 * Gets number of selected Glyphs
 * @return number of selected Glyphs
 */
	public int numberOfSelectedGlyphs() {
		return selectedGlyphs.size();
	}
	
/**
 * Gets Enumeration of selected Glyphs
 * @return Enumeration of selected Glyphs
 */
	public Enumeration selectedGlyphs() {
		return selectedGlyphs.elements();
	}
	
/**
 * Deselects previously selected Glyphs and removes them from Vector of selected Glyphs
 */
	public void removeSelectedGlyphs() {
		selectedGlyphs.selectElements(false);
		selectedGlyphs.removeAllElements();
	}
	
/**
 * Removes specified Glyph from Vector of selected Glyphs
 * @return true if Glyph was removed, otherwise false
 * @param gl Glyph to be removed
 */
	public boolean removeSelectedGlyph(Glyph gl) {
		gl.select(false);
		return selectedGlyphs.removeElement(gl);
	}
	
/**
 * Overrides CompositeGlyph.addChild() to prevent use of addChild() for EntryPair.
 * Should not be used. Prints error message.
 * @param gl Glyph to be added
 */
	public void addChild(Glyph child) {
		System.out.println("Use specific methods to set children for EntryPair.");
	}
	
/**
 * Gets clicked child Glyph
 * @return clicked Glyph, or null if no Glyph was found
 * @param x x position
 * @param y y position
 */
	public Glyph clicked(int x, int y) {
		Glyph gl;
		if (dotterRegions != null) {
			gl = dotterRegions.clicked(x, y);
			if (gl != null) { 
				if (gl.equals(dotterRegions)) {
					return super.clicked(x, y);
				} else {
					return gl;
				}
			}
		}
		Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph kgl = (Glyph) kids.nextElement();
			if (kgl instanceof RegionSet) {
				gl = kgl.clicked(x,y);
				if (gl != null) { return gl; }
			}
		}
		gl = super.clicked(x, y);
// 		System.out.println("Clicked " + gl);
		return gl;
	}
	
  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void showUnconfirmedExons(boolean show) {
    entry1.showUnconfirmedExons(show);
    entry2.showUnconfirmedExons(show);
  }
	/**
	 * Gets TFBSs common for both entries.
	 */
	public Vector getCommonTFBSIds() {
		Vector cids = new Vector();
		Enumeration tfs1en = entry1.getTFBSs().elements();
		while (tfs1en.hasMoreElements()){
			String id1 = ((TFBS)tfs1en.nextElement()).getId();
			if (!cids.contains(id1)) {
				Enumeration tfs2en = entry2.getTFBSs().elements();
				while(tfs2en.hasMoreElements()){
					if (id1.equals( ((TFBS)tfs2en.nextElement()).getId() ))
						cids.addElement(id1);
				}
			}
		}
// 		wc.getTFSet().setIDs(cids);
		return cids;
	}
	
	/**
	 * Will update the list of TFBSs to be shown. Defines Observer
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof TFBSSet) {
			Vector ids = (Vector) arg;
			Vector tfs = entry1.getTFBSs();
			Enumeration tfs2en = entry2.getTFBSs().elements();
			while (tfs2en.hasMoreElements()){
				tfs.addElement(tfs2en.nextElement());
			}
			Enumeration tfsen = tfs.elements();
			TF:while(tfsen.hasMoreElements()) {
				TFBS tmp = (TFBS) tfsen.nextElement();
				Enumeration idsen = ids.elements();
				while(idsen.hasMoreElements()) {
					String id = (String) idsen.nextElement();
					if (id.equals(tmp.getId())) {
						tmp.setVisible(true);
						continue TF;
					} 
				}
				tmp.setVisible(false);
			}
			wc.drawEntries();
		}
	}
	
/**
 * Draws child Glyphs to Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		if(entriesSet()) {
			// set initial parameters
			if (zoom == -1) {
				int maxLen = Math.max(entry1.getLength(), entry2.getLength());
				Dimension canDim = wc.mw.seqCan.getSize();
				int width = canDim.width - 40;
// 				System.out.println("maxlen: " + maxLen + " width: " + width + " pixels");
				zoom = (double) width / (double) maxLen;	// pixels/bp
				if (entry1.coord == null) {
					entry1.coord = new CoordVal(wc.mw.seqCan, 1);
					entry1.updateCoord();
				}
				if (entry2.coord == null) {
					entry2.coord = new CoordVal(wc.mw.seqCan, -1);
					entry2.updateCoord();
				}
				entry1.coord.zoom = zoom;
				entry2.coord.zoom = zoom;
				entry1.centerX();
				entry2.centerX();
// 				System.out.println("Initial zoom set to "+zoom);
				wc.mw.zoomLabel1.setValue(zoom);
				wc.mw.zoomLabel2.setValue(zoom);
				wc.mw.szDirector.setEntries();
// 				byte [] s = entry1.getSequence(1, 100);
// 				Font fo = new Font("Monospaced", Font.PLAIN, 12);
				Font fo = wc.DNAFONT;
				int fw = wc.mw.seqCan.getFontMetrics(fo).getMaxAdvance();
				wc.DNAFONTWIDTH = fw;
				System.out.println("CharWidth set to " + fw);
				wc.mw.szDirector.setZoomMinMax(zoom,fw); //width of a courier 12 character
			}

			super.draw(g);
			if (currentRegionSet != null) {
				currentRegionSet.draw(g);
			}
		}
	}
	
/**
 * Updates y positions for Entries, and moves Scales to highest tier
 */
	public void updateCoordVals() {
		if (entriesSet()) {
			entry1.coord.updateSize();
			entry2.coord.updateSize();
			entry1.getScale().setTier(CoordVal.MAXTIERS);
			entry2.getScale().setTier(CoordVal.MAXTIERS);
		}
	}
	
/**
 * Calls Dotter with ranges of features specified
 * @param feature1 first feature
 * @param feature2 second feature
 */
	public void callDotter(SeqFeature feature1, SeqFeature feature2) {
// 		System.out.println("EntryPair.callDotter() called");
		SeqRange range1 = feature1.getSeqRange();
		SeqRange range2 = feature2.getSeqRange();
		Entry ent1 = feature1.getEntry();
		Entry ent2 = feature2.getEntry();
		String fn1 = ent1.getFilename().toLowerCase() + "_" + range1.getStart() + "-" + range1.getStop();
		String fn2 = ent2.getFilename().toLowerCase() + "_" + range2.getStart() + "-" + range2.getStop();;
		FastaFile ff1 = new FastaFile(SystemConstants.TMPDIR, fn1, ent1, range1);
		FastaFile ff2 = new FastaFile(SystemConstants.TMPDIR, fn2, ent2, range2);
		ff1.write();
		ff2.write();
		String featurefn = entry1.getFilename() + "-" + entry2.getFilename() + ".ftr";
		DotterFeatureFile featuref = new DotterFeatureFile(SystemConstants.TMPDIR, featurefn, ent1, ent2);
		featuref.write();
		
		int qOffset = range1.getStart()-1;
		int sOffset = range2.getStart()-1;
		Runtime rt = Runtime.getRuntime();
		try {
// 			rt.exec(DOTTERPATH + "dotter -f " + featurefn + " -q " + qOffset + " -s " + sOffset + " " + fn1 + " " + fn2);
			Process p = rt.exec(SystemConstants.DOTTERPATH + "dotter -f " + featuref.getAbsolutePath() + " -q " + qOffset + " -s " + sOffset + " " + ff1.getAbsolutePath()  + " " + ff2.getAbsolutePath());
// 			rt.exec("dotter -q " + qOffset + " -s " + sOffset + " " + fn1 + " " + fn2);
		} catch (IOException ioe) { System.out.println(ioe); }
	}
	
/**
 * Gets Enumeration of String lines in gff format for Entries
 * @return string enumeration
 */
	public Enumeration gffLines() {
		Vector lines = new Vector();
		if (entry1 != null) {
			Enumeration le = entry1.getGffLines().elements();
			while (le.hasMoreElements()) {
				Object line = le.nextElement();
				lines.addElement(line);
//				lines.insertElementAt(line, 0);
			}
		}
		if (entry2 != null) {
			Enumeration le = entry2.getGffLines().elements();
			while (le.hasMoreElements()) {
				Object line = le.nextElement();
				lines.addElement(line);
//				lines.insertElementAt(line, 0);
			}
		}
		return lines.elements();
	}
	
/**
 * Gets Enumeration of String lines in cgff format for Entries
 * @return string enumeration
 */
	public Enumeration cgffLines() {
		Vector lines = new Vector();
// 		System.out.println("rec children: " + reciprocals.getChildren());
		Enumeration re = reciprocals.getReciprocals();
		while (re.hasMoreElements()) {
			Reciprocal rec = (Reciprocal) re.nextElement();
			lines.addElement(rec.cgffString());
//			lines.insertElementAt(rec.cgffString(), 0);
		}
		return lines.elements();
	}
	
/**
 * Calls cpg and adds CpGIslands to Entries
 */
	public void callCpG() {
  // Local call
//     Properties cpgProp = (Properties) wc.getResources().get("CpG");
// 		CpG cpg1 = new CpG(entry1, cpgProp);
// 		CpG	cpg2 = new CpG(entry2, cpgProp);
//     cpg1.start();
//     cpg2.start();
// 		GffParser gp1 = null;
// 		GffParser gp2 = null;
// 		try {
// 			cpg1.join();
// 			cpg2.join();
// 			gp1 = new GffParser(this, cpg1.parseToGff());
// 			gp2 = new GffParser(this, cpg2.parseToGff());
// 		} catch (Exception e) {
// //       if (wc.mw == null) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
  // CORBA call
//     InputStructGenerator isg1 = new InputStructGenerator ();
//     InputStructGenerator isg2 = new InputStructGenerator ();
//     isg1.addInput(entry1.getFilename(), entry1.getSequence());
//     isg2.addInput(entry2.getFilename(), entry2.getSequence());
//    
//     CorbaMethodRunner cmr1 = new CorbaMethodRunner("CpG", isg1.getInputStruct());
//     CorbaMethodRunner cmr2 = new CorbaMethodRunner("CpG", isg2.getInputStruct());
//     cmr1.start();
//     cmr2.start();
// 		GffParser gp1 = null;
// 		GffParser gp2 = null;
// 		try {
// 			cmr1.join();
// 			cmr2.join();
// 			gp1 = new GffParser(this, cmr1.getGffData().gff);
// 			gp2 = new GffParser(this, cmr2.getGffData().gff);
// 		} catch (Exception e) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
    Properties cpgProp = (Properties) wc.getResources().get("CpG");
    String gff1 = null;
    String gff2 = null;
    GffParser gp1 = null;
		GffParser gp2 = null;
    try {
      if (cpgProp.getProperty("local").equals("true")) {
		    CpG cpg1 = new CpG(entry1, cpgProp);
		    CpG	cpg2 = new CpG(entry2, cpgProp);
        cpg1.start();
        cpg2.start();
			  cpg1.join();
			  cpg2.join();
        gff1 = cpg1.parseToGff();
        gff2 = cpg2.parseToGff();

      } else {
        InputStructGenerator isg1 = new InputStructGenerator ();
        InputStructGenerator isg2 = new InputStructGenerator ();
        isg1.addInput(entry1.getFilename(), entry1.getSequence());
        isg2.addInput(entry2.getFilename(), entry2.getSequence());
//         isg1.addParameter(" -l " + cpgProp.get("l") + " -g " + cpgProp.get("g") + " -o " + cpgProp.get("o"));
//         isg2.addParameter(" -l " + cpgProp.get("l") + " -g " + cpgProp.get("g") + " -o " + cpgProp.get("o"));
        isg1.addParameter("l=" + cpgProp.get("l"));
        isg1.addParameter("g=" + cpgProp.get("g"));
        isg1.addParameter("o=" + cpgProp.get("o"));
        isg2.addParameter("l=" + cpgProp.get("l"));
        isg2.addParameter("g=" + cpgProp.get("g"));
        isg2.addParameter("o=" + cpgProp.get("o"));
        CorbaMethodRunner cmr1 = new CorbaMethodRunner("CpG", isg1.getInputStruct(),cpgProp);
        CorbaMethodRunner cmr2 = new CorbaMethodRunner("CpG", isg2.getInputStruct(),cpgProp);
        cmr1.start();
        cmr2.start();
			  cmr1.join();
			  cmr2.join();
        gff1 = cmr1.getGffData().gff;
        gff2 = cmr2.getGffData().gff;

      }
			gp1 = new GffParser(this, gff1);
			gp2 = new GffParser(this, gff2);
      
		} catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
   
 	}
/**
 * Calls RepeatMasker
 */
	public void callRepeatMasker(String option1, String option2) {
    // Local call
// 		RepeatMasker rm1 = new RepeatMasker(entry1, option1);
// 		RepeatMasker rm2 = new RepeatMasker(entry2, option2);
// 		rm1.start();
// 		rm2.start();
// 		GffParser gp1 = null;
// 		GffParser gp2 = null;
// 		try {
// 			rm1.join();
// 			rm2.join();
// 			gp1 = new GffParser(this, rm1.getGff());
// 			gp2 = new GffParser(this, rm2.getGff());
// 		} catch (Exception e) {
// //       if (wc.mw == null) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
  // CORBA call
//     InputStructGenerator isg1 = new InputStructGenerator ();
//     InputStructGenerator isg2 = new InputStructGenerator ();
//     isg1.addInput(entry1.getFilename(), entry1.getSequence());
//     isg1.addParameter(option1);
//     isg2.addInput(entry2.getFilename(), entry2.getSequence());
//     isg2.addParameter(option2);
//    
//     CorbaMethodRunner cmr1 = new CorbaMethodRunner("RepeatMasker", isg1.getInputStruct());
//     CorbaMethodRunner cmr2 = new CorbaMethodRunner("RepeatMasker", isg2.getInputStruct());
//     cmr1.start();
//     cmr2.start();
// 		GffParser gp1 = null;
// 		GffParser gp2 = null;
// 		try {
// 			cmr1.join();
// 			cmr2.join();
// 			gp1 = new GffParser(this, cmr1.getGffData().gff);
// 			gp2 = new GffParser(this, cmr2.getGffData().gff);
// 		} catch (Exception e) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
		
    // new stuff
    Properties prop = (Properties) wc.getResources().get("RepeatMasker");
    String gff1 = null;
    String gff2 = null;
    GffParser gp1 = null;
		GffParser gp2 = null;
    try {
      if (prop.getProperty("local").equals("true")) {
        RepeatMasker rm1 = new RepeatMasker(entry1, option1, prop);
        RepeatMasker rm2 = new RepeatMasker(entry2, option2, prop);
        rm1.start();
        rm2.start();
        rm1.join();
        rm2.join();
        gff1 = rm1.getGff();
        gff2 = rm2.getGff();
      }else {
        InputStructGenerator isg1 = new InputStructGenerator ();
        InputStructGenerator isg2 = new InputStructGenerator ();
        isg1.addInput(entry1.getFilename(), entry1.getSequence());
        isg1.addParameter(option1);
        isg2.addInput(entry2.getFilename(), entry2.getSequence());
        isg2.addParameter(option2);
        CorbaMethodRunner cmr1 = new CorbaMethodRunner("RepeatMasker", isg1.getInputStruct(), prop);
        CorbaMethodRunner cmr2 = new CorbaMethodRunner("RepeatMasker", isg2.getInputStruct(), prop);
        cmr1.start();
        cmr2.start();
			  cmr1.join();
			  cmr2.join();
        gff1 = cmr1.getGffData().gff;
        gff2 = cmr2.getGffData().gff;
        
      }
			gp1 = new GffParser(this, gff1);
			gp2 = new GffParser(this, gff2);
      
		} catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
    
	}
	
/**
 * Calls genscan and adds predicted genes to Entries
 */
	public void callGenscan() {
    // The old way
// 		Genscan gs1 = new Genscan(entry1);
// 		gs1.addToEntry();
//  		Genscan gs2 = new Genscan(entry2);
// 		gs2.addToEntry();
    
    // Local call
// 		Genscan gs1 = new Genscan(entry1);
//  		Genscan gs2 = new Genscan(entry2);
// 		gs1.start();
// 		gs2.start();
// 		GffParser gp1 = null;
// 		GffParser gp2 = null;
// 		try {
// 			gs1.join();
// 			gs2.join();
// 			gp1 = new GffParser(this, gs1.getGff());
// 			gp2 = new GffParser(this, gs2.getGff());
// 		} catch (Exception e) {
// //       if (wc.mw == null) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
  // CORBA call
//     InputStructGenerator isg1 = new InputStructGenerator ();
//     InputStructGenerator isg2 = new InputStructGenerator ();
//     isg1.addInput(entry1.getFilename(), entry1.getRepeatMaskedSequenceNoLC());
//     isg2.addInput(entry2.getFilename(), entry2.getRepeatMaskedSequenceNoLC());
//    
//     CorbaMethodRunner cmr1 = new CorbaMethodRunner("Genscan", isg1.getInputStruct());
//     CorbaMethodRunner cmr2 = new CorbaMethodRunner("Genscan", isg2.getInputStruct());
//     cmr1.start();
//     cmr2.start();
// 		GffParser gp1 = null;
// 		GffParser gp2 = null;
// 		try {
// 			cmr1.join();
// 			cmr2.join();
// 			gp1 = new GffParser(this, cmr1.getGffData().gff);
// 			gp2 = new GffParser(this, cmr2.getGffData().gff);
// 		} catch (Exception e) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
    
    Properties prop = (Properties) wc.getResources().get("Genscan");
    String gff1 = null;
    String gff2 = null;
    GffParser gp1 = null;
		GffParser gp2 = null;
    try {
      if (prop.getProperty("local").equals("true")) {
        Genscan gs1 = new Genscan(entry1, prop);
        Genscan gs2 = new Genscan(entry2, prop);
        gs1.start();
        gs2.start();
        gs1.join();
        gs2.join();
        gff1 = gs1.getGff();
        gff2 = gs2.getGff();
      } else {
        InputStructGenerator isg1 = new InputStructGenerator ();
        InputStructGenerator isg2 = new InputStructGenerator ();
        isg1.addInput(entry1.getFilename(), entry1.getRepeatMaskedSequenceNoLC());
        isg2.addInput(entry2.getFilename(), entry2.getRepeatMaskedSequenceNoLC());
        isg1.addParameter("subopt=" + prop.get("subopt"));
        isg2.addParameter("subopt=" + prop.get("subopt"));
        CorbaMethodRunner cmr1 = new CorbaMethodRunner("Genscan", isg1.getInputStruct(), prop);
        CorbaMethodRunner cmr2 = new CorbaMethodRunner("Genscan", isg2.getInputStruct(), prop);
        cmr1.start();
        cmr2.start();
			  cmr1.join();
			  cmr2.join();
        gff1 = cmr1.getGffData().gff;
        gff2 = cmr2.getGffData().gff;
        
      }
			gp1 = new GffParser(this, gff1);
			gp2 = new GffParser(this, gff2);
      
		} catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
    
    
	}
/**
 * Calls BlastAlign CORBA server
 */
	public void callBlastnAlign() {
// 		BlastAlignClient ba = null;
// 		try {
// 			ba = new BlastAlignClient(entry1, entry2, METHODS_SERVER_IOR_URL, "blastn");
// 		} catch (alfresco.corba_wrappers.NoOutputException e) {
// //       if (wc.mw == null) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
// 		ba.addToEntries();
    Properties prop = (Properties) wc.getResources().get("BlastAlign");
    GffDataStruct result = null;
    CgffParser cp = null;
    try {
      if (prop.getProperty("local").equals("true")) {
        alfresco.BlastAlign bla = new BlastAlign(this, prop);
        bla.start();
        bla.join();
        result = bla.getGffData();
      }else {
        InputStructGenerator isg = new InputStructGenerator ();
        isg.addInput(entry1.getFilename(), entry1.getRepeatMaskedSequence());
        isg.addInput(entry2.getFilename(), entry2.getRepeatMaskedSequence());
        CorbaMethodRunner cmr = new CorbaMethodRunner("BlastAlign", isg.getInputStruct(), prop);
        cmr.start();
			  cmr.join();
        result = cmr.getGffData();
      }
      cp = new CgffParser(this, result);
    } catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
    
	}
	
// /**
//  * Calls BlastWise 
//  */
// 	public void callBlastWise() {
//     Properties prop = (Properties) wc.getResources().get("BlastWise");
//     String gff1 = null;
//     String gff2 = null;
//     GffParser gp1 = null;
// 		GffParser gp2 = null;
//     try {
//       if (prop.getProperty("local").equals("true")) {
// 		    BlastWise bw1 = new BlastWise(entry1, prop);
// 		    BlastWise bw2 = new BlastWise(entry2, prop);
// 		    bw1.start();
// 		    bw2.start();
// 			  bw1.join();
// 			  bw2.join();
// 			  gff1 = bw1.getGff();
// 			  gff2 = bw2.getGff();
//       } else {
//         InputStructGenerator isg1 = new InputStructGenerator ();
//         InputStructGenerator isg2 = new InputStructGenerator ();
//         isg1.addInput(entry1.getFilename(), entry1.getRepeatMaskedSequence());
//         isg2.addInput(entry2.getFilename(), entry2.getRepeatMaskedSequence());
//         isg1.addParameter("database=" + prop.get("database"));
//         isg2.addParameter("database=" + prop.get("database"));
//         CorbaMethodRunner cmr1 = new CorbaMethodRunner("BlastWise", isg1.getInputStruct(), prop);
//         CorbaMethodRunner cmr2 = new CorbaMethodRunner("BlastWise", isg2.getInputStruct(), prop);
//         cmr1.start();
//         cmr2.start();
// 			  cmr1.join();
// 			  cmr2.join();
//         gff1 = cmr1.getGffData().gff;
//         gff2 = cmr2.getGffData().gff;
//         
//       }
// 			gp1 = new GffParser(this, gff1);
// 			gp2 = new GffParser(this, gff2);
//           
// 		} catch (Exception e) {
// //       if (wc.mw == null) {
//       if (!wc.batch) {
// 			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
//       } else {
//         e.printStackTrace();
//       }
// 			return;
// 		}
// 		
// 	}
/**
 * Calls blGeneWise 
 */
	public void callBlGenewise() {
    Properties prop = (Properties) wc.getResources().get("blGenewise");
    String gff1 = null;
    String gff2 = null;
    GffParser gp1 = null;
		GffParser gp2 = null;
    try {
      if (prop.getProperty("local").equals("true")) {
		    BlGenewise bw1 = new BlGenewise(entry1, prop);
		    BlGenewise bw2 = new BlGenewise(entry2, prop);
		    bw1.start();
		    bw2.start();
			  bw1.join();
			  bw2.join();
			  gff1 = bw1.getGff();
			  gff2 = bw2.getGff();
      } else {
        InputStructGenerator isg1 = new InputStructGenerator ();
        InputStructGenerator isg2 = new InputStructGenerator ();
        isg1.addInput(entry1.getFilename(), entry1.getRepeatMaskedSequence());
        isg2.addInput(entry2.getFilename(), entry2.getRepeatMaskedSequence());
        isg1.addParameter("database=" + prop.get("database"));
        isg2.addParameter("database=" + prop.get("database"));
        CorbaMethodRunner cmr1 = new CorbaMethodRunner("BlGenewise", isg1.getInputStruct(), prop);
        CorbaMethodRunner cmr2 = new CorbaMethodRunner("BlGenewise", isg2.getInputStruct(), prop);
        cmr1.start();
        cmr2.start();
			  cmr1.join();
			  cmr2.join();
        gff1 = cmr1.getGffData().gff;
        gff2 = cmr2.getGffData().gff;
        
      }
			gp1 = new GffParser(this, gff1);
			gp2 = new GffParser(this, gff2);
          
		} catch (Exception e) {
//       if (wc.mw == null) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
		
	}
/**
 * Calls BlEst_Genome 
 */
	public void callBlEst_Genome() {
    Properties prop = (Properties) wc.getResources().get("BlEst_Genome");
    String gff1 = null;
    String gff2 = null;
    GffParser gp1 = null;
		GffParser gp2 = null;
    try {
      if (prop.getProperty("local").equals("true")) {
		    BlEst_Genome beg1 = new BlEst_Genome(entry1, prop);
		    BlEst_Genome beg2 = new BlEst_Genome(entry2, prop);
		    beg1.start();
		    beg2.start();
			  beg1.join();
			  beg2.join();
			  gff1 = beg1.getGff();
			  gff2 = beg2.getGff();
      } else {
        InputStructGenerator isg1 = new InputStructGenerator ();
        InputStructGenerator isg2 = new InputStructGenerator ();
        isg1.addInput(entry1.getFilename(), entry1.getRepeatMaskedSequence());
        isg2.addInput(entry2.getFilename(), entry2.getRepeatMaskedSequence());
        isg1.addParameter("d=" + prop.get("d"));
        isg1.addParameter("h=" + prop.get("h"));
        isg1.addParameter("e=" + prop.get("e"));
        isg2.addParameter("d=" + prop.get("d"));
        isg2.addParameter("h=" + prop.get("h"));
        isg2.addParameter("e=" + prop.get("e"));
        CorbaMethodRunner cmr1 = new CorbaMethodRunner("BlEst_Genome", isg1.getInputStruct(), prop);
        CorbaMethodRunner cmr2 = new CorbaMethodRunner("BlEst_Genome", isg2.getInputStruct(), prop);
        cmr1.start();
        cmr2.start();
			  cmr1.join();
			  cmr2.join();
        gff1 = cmr1.getGffData().gff;
        gff2 = cmr2.getGffData().gff;
        
      }
			gp1 = new GffParser(this, gff1);
			gp2 = new GffParser(this, gff2);
          
		} catch (Exception e) {
//       if (wc.mw == null) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
		
	}
/**
 * Calls est_genome for all sequences specified in the sequence tokenizer 
 */
// 	public void callEst_Genome(SequenceTokenizer seqs1,SequenceTokenizer seqs2 ) {
	public void callEst_Genome(FastaFile f1, FastaFile f2 ) {
    Properties prop = (Properties) wc.getResources().get("Est_Genome");
//     String gff1 = null;
//     String gff2 = null;
//     GffParser gp1 = null;
// 		GffParser gp2 = null;
    Vector processes = new Vector();
    try {
      if (prop.getProperty("local").equals("true")) {
//         Vector eFiles1 = null;
        if (f1 != null) {
//           eFiles1 = new Vector();
          SequenceTokenizer seqs1 = f1.sequences();
          while (seqs1.hasMoreElements()) {
            StringReader sr = new StringReader(seqs1.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callEst_Genome: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            FastaFile f = new FastaFile(TMPDIR, name, comment, seq);
            f.write();
//             eFiles1.addElement(f);
            Est_Genome eg = new Est_Genome(entry1, f, prop);
            eg.start();
            eg.yield();
            processes.addElement(eg);
          }
        }
        // for seq2
//         Vector eFiles2 = null;
        if (f2 != null) {
//           eFiles2 = new Vector();
          SequenceTokenizer seqs2 = f2.sequences();
          while (seqs2.hasMoreElements()) {
            StringReader sr = new StringReader(seqs2.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callEst_Genome: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            FastaFile f = new FastaFile(TMPDIR, name, comment, seq);
            f.write();
//             eFiles2.addElement(f);
            Est_Genome eg = new Est_Genome(entry2, f, prop);
            eg.start();
            eg.yield();
            processes.addElement(eg);
          }
        }

//         Enumeration efen1 = eFiles1.elements();
//         while (efen1.hasMoreElements()) {
//           FastaFile eff = (FastaFile) efen1.nextElement();
//           Est_Genome eg = new Est_Genome(entry1, eff, prop);
//           eg.start();
//           eg.yield();
//           processes.addElement(eg);
//         }
//         Enumeration efen2 = eFiles2.elements();
//         while (efen2.hasMoreElements()) {
//           FastaFile eff = (FastaFile) efen2.nextElement();
//           Est_Genome eg = new Est_Genome(entry2, eff, prop);
//           eg.start();
//           eg.yield();
//           processes.addElement(eg);
//         }
        Enumeration pren = processes.elements();
        while (pren.hasMoreElements()) {
          Est_Genome eg = (Est_Genome) pren.nextElement();
          eg.join();
          GffParser gp = new GffParser(this, eg.getGff());
        }
      } else {
        if (f1 != null) {
          String gName = entry1.getFilename();
          String gSeq = entry1.getRepeatMaskedSequence();
          SequenceTokenizer seqs1 = f1.sequences();
          while (seqs1.hasMoreElements()) {
            StringReader sr = new StringReader(seqs1.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callEst_Genome: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            InputStructGenerator isg = new InputStructGenerator();
            isg.addInput(gName, gSeq);
            isg.addInput(name, seq);
            CorbaMethodRunner cmr = new CorbaMethodRunner("Est_Genome", isg.getInputStruct(), prop);
            cmr.start();
            cmr.yield();
            processes.addElement(cmr);
          }
        }
        if (f2 != null) {
          String gName = entry2.getFilename();
          String gSeq = entry2.getRepeatMaskedSequence();
          SequenceTokenizer seqs2 = f2.sequences();
          while (seqs2.hasMoreElements()) {
            StringReader sr = new StringReader(seqs2.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callEst_Genome: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            InputStructGenerator isg = new InputStructGenerator();
            isg.addInput(gName, gSeq);
            isg.addInput(name, seq);
            CorbaMethodRunner cmr = new CorbaMethodRunner("Est_Genome", isg.getInputStruct(), prop);
            cmr.start();
            cmr.yield();
            processes.addElement(cmr);
          }
        }
        Enumeration pren = processes.elements();
        while (pren.hasMoreElements()) {
          CorbaMethodRunner cmr = (CorbaMethodRunner) pren.nextElement();
          cmr.join();
          GffParser gp = new GffParser(this, cmr.getGffData().gff);
        }
        
      }
		} catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
		
	}
	
/**
 * Calls est_genome for all sequences specified in the sequence tokenizer 
 */

	public void callSpidey(FastaFile f1, FastaFile f2 ) {
    Properties prop = (Properties) wc.getResources().get("Spidey");
    Vector processes = new Vector();
    try {
      // Local execution
      if (prop.getProperty("local").equals("true")) {
        // for seq1
        if (f1 != null) {
          SequenceTokenizer seqs1 = f1.sequences();
          while (seqs1.hasMoreElements()) {
            StringReader sr = new StringReader(seqs1.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callSpidey: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            FastaFile f = new FastaFile(TMPDIR, name, comment, seq);
            f.write();
            Spidey sp = new Spidey(entry1, f, null, prop);
            sp.start();
            sp.yield();
            processes.addElement(sp);
          }
        }
        // for seq2
        if (f2 != null) {
          SequenceTokenizer seqs2 = f2.sequences();
          while (seqs2.hasMoreElements()) {
            StringReader sr = new StringReader(seqs2.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callSpidey: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            FastaFile f = new FastaFile(TMPDIR, name, comment, seq);
            f.write();
            Spidey sp = new Spidey(entry2, f, null, prop);
            sp.start();
            sp.yield();
            processes.addElement(sp);
          }
        }

        Enumeration pren = processes.elements();
        while (pren.hasMoreElements()) {
          Spidey sp = (Spidey) pren.nextElement();
          sp.join();
          GffParser gp = new GffParser(this, sp.getGff());
        }
      } else { // Remote execution
        String s = prop.getProperty("s_CB"); // -s boolean flag
        System.out.println("s_CB: " + s);
        String opt = "";
        if (s.equals("true")) opt = "-s";
        System.out.println("opt: " + opt);
        // for seq1        
        if (f1 != null) {
          String gName = entry1.getFilename();
          String gSeq = entry1.getRepeatMaskedSequence();
          SequenceTokenizer seqs1 = f1.sequences();
          while (seqs1.hasMoreElements()) {
            StringReader sr = new StringReader(seqs1.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callSpidey: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            InputStructGenerator isg = new InputStructGenerator();
            isg.addInput(gName, gSeq);
            isg.addInput(name, seq);
            isg.addParameter(opt);
            CorbaMethodRunner cmr = new CorbaMethodRunner("Spidey", isg.getInputStruct(), prop);
            cmr.start();
            cmr.yield();
            processes.addElement(cmr);
          }
        }
        if (f2 != null) {
          String gName = entry2.getFilename();
          String gSeq = entry2.getRepeatMaskedSequence();
          SequenceTokenizer seqs2 = f2.sequences();
          while (seqs2.hasMoreElements()) {
            StringReader sr = new StringReader(seqs2.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callSpidey: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            InputStructGenerator isg = new InputStructGenerator();
            isg.addInput(gName, gSeq);
            isg.addInput(name, seq);
            isg.addParameter(opt);
            CorbaMethodRunner cmr = new CorbaMethodRunner("Spidey", isg.getInputStruct(), prop);
            cmr.start();
            cmr.yield();
            processes.addElement(cmr);
          }
        }
        Enumeration pren = processes.elements();
        while (pren.hasMoreElements()) {
          CorbaMethodRunner cmr = (CorbaMethodRunner) pren.nextElement();
          cmr.join();
          GffParser gp = new GffParser(this, cmr.getGffData().gff);
        }
        
      }
		} catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
		
	}

/**
 * Calls genewise for all sequences specified in the sequence files 
 */
	public void callGenewise(FastaFile f1, FastaFile f2 ) {
    Properties prop = (Properties) wc.getResources().get("Genewise");
//     String gff1 = null;
//     String gff2 = null;
//     GffParser gp1 = null;
// 		GffParser gp2 = null;
    Vector processes = new Vector();
    try {
      if (prop.getProperty("local").equals("true")) {
//         Vector eFiles1 = null;
        if (f1 != null) {
//           eFiles1 = new Vector();
          SequenceTokenizer seqs1 = f1.sequences();
          while (seqs1.hasMoreElements()) {
            StringReader sr = new StringReader(seqs1.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callGenewise: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            FastaFile f = new FastaFile(TMPDIR, name, comment, seq);
            f.write();
//             eFiles1.addElement(f);
            Genewise gw = new Genewise(entry1, f, prop);
            gw.start();
            gw.yield();
            processes.addElement(gw);
          }
        }
        // for seq2
//         Vector eFiles2 = null;
        if (f2 != null) {
//           eFiles2 = new Vector();
          SequenceTokenizer seqs2 = f2.sequences();
          while (seqs2.hasMoreElements()) {
            StringReader sr = new StringReader(seqs2.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callGenewise: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            FastaFile f = new FastaFile(TMPDIR, name, comment, seq);
            f.write();
//             eFiles2.addElement(f);
            Genewise gw = new Genewise(entry2, f, prop);
            gw.start();
            gw.yield();
            processes.addElement(gw);
          }
        }

//         Enumeration efen1 = eFiles1.elements();
//         while (efen1.hasMoreElements()) {
//           FastaFile eff = (FastaFile) efen1.nextElement();
//           Est_Genome eg = new Est_Genome(entry1, eff, prop);
//           eg.start();
//           eg.yield();
//           processes.addElement(eg);
//         }
//         Enumeration efen2 = eFiles2.elements();
//         while (efen2.hasMoreElements()) {
//           FastaFile eff = (FastaFile) efen2.nextElement();
//           Est_Genome eg = new Est_Genome(entry2, eff, prop);
//           eg.start();
//           eg.yield();
//           processes.addElement(eg);
//         }
        Enumeration pren = processes.elements();
        while (pren.hasMoreElements()) {
          Genewise gw = (Genewise) pren.nextElement();
          gw.join();
          GffParser gp = new GffParser(this, gw.getGff());
        }
      } else {
        if (f1 != null) {
          String gName = entry1.getFilename();
          String gSeq = entry1.getRepeatMaskedSequence();
          SequenceTokenizer seqs1 = f1.sequences();
          while (seqs1.hasMoreElements()) {
            StringReader sr = new StringReader(seqs1.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callGenewise: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            InputStructGenerator isg = new InputStructGenerator();
            isg.addInput(gName, gSeq);
            isg.addInput(name, seq);
            CorbaMethodRunner cmr = new CorbaMethodRunner("Genewise", isg.getInputStruct(), prop);
            cmr.start();
            cmr.yield();
            processes.addElement(cmr);
          }
        }
        if (f2 != null) {
          String gName = entry2.getFilename();
          String gSeq = entry2.getRepeatMaskedSequence();
          SequenceTokenizer seqs2 = f2.sequences();
          while (seqs2.hasMoreElements()) {
            StringReader sr = new StringReader(seqs2.nextSequence());
            BufferedReader br = new BufferedReader(sr);
            String comment = br.readLine();
            System.out.println("callGenewise: Read sequence " + comment);
            String seq = br.readLine();
            int stop = comment.indexOf(" ");
            String name;
            if (stop > 0) { 
              name = comment.substring(1, stop);
            } else {
              name = comment.substring(1);
            }
            InputStructGenerator isg = new InputStructGenerator();
            isg.addInput(gName, gSeq);
            isg.addInput(name, seq);
            CorbaMethodRunner cmr = new CorbaMethodRunner("Genewise", isg.getInputStruct(), prop);
            cmr.start();
            cmr.yield();
            processes.addElement(cmr);
          }
        }
        Enumeration pren = processes.elements();
        while (pren.hasMoreElements()) {
          CorbaMethodRunner cmr = (CorbaMethodRunner) pren.nextElement();
          cmr.join();
          GffParser gp = new GffParser(this, cmr.getGffData().gff);
        }
        
      }
		} catch (Exception e) {
      if (!wc.batch) {
			  ErrorDialog ed = new ErrorDialog(wc.mw, e.getMessage());
      } else {
        e.printStackTrace();
      }
			return;
		}
		
	}
	
	/**
	 * Creates a FindReciprocals object and adds reciprocals
	 */
	public void findReciprocals() {
    Properties dbaprop = (Properties) wc.getResources().get("Dba");
		ReciprocalFinder rf = new ReciprocalFinder(entry1, entry2, dbaprop);
		reciprocals.addChildren(rf.getReciprocals());
	}
	
/**
 * Show introns of Entries
 * @param show true if Introns and their Reciprocals should be visible, otherwise false
 */
	public void showIntrons(boolean show) {
		entry1.showIntrons(show);
		entry2.showIntrons(show);
		reciprocals.showIntronReciprocals(show);		
	}
	
/**
 * Save EntryPair to file
 * @param filename name of output file
 */
	public void save(File file) throws IOException {
// 		System.out.println("Filename: " + filename);
		FileOutputStream fos = new FileOutputStream(file);
		GZIPOutputStream gzos = new GZIPOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(gzos);
		if (wc.pw.dotterSet != null ) { drSaved = true; }
    entry1.chopUpSeqs();
    entry2.chopUpSeqs();
		oos.writeObject(this);
		if (drSaved) {
			oos.writeObject(wc.pw.dotterSet);
		}
		oos.flush();
		oos.close();
	}

/**
 * Opens a dialog for choosing output filename if save file is not set
 * @return true if filename was given, otherwise false
 */
	public boolean interactiveSave() {
		File outfile = getSaveFile();
    if (outfile == null) {
      FileDialog f = new FileDialog(wc.mw, "Save Entry Pair", FileDialog.SAVE);
		  f.show();
		  String fname = f.getFile();
      String dname = f.getDirectory();
//       System.out.println("dir: " + dname + ", file: " + fname);
		  if (fname != null) {
        outfile = new File(dname, fname);
        setSaveFile(outfile);
      } else {
        return false;
      }
    }
		try {
// 				save(fname);
			save(outfile);
		} catch (IOException ioe) { ioe.printStackTrace(); }
		return true;
	}
	
/**
 * Opens a dialog for choosing output filename if save file is not set
 * @return true if filename was given, otherwise false
 */
	public boolean interactiveSaveAs() {
// 		File outfile = getSaveFile();
//     if (outfile == null) {
      
      FileDialog f = new FileDialog(wc.mw, "Save Entry Pair As...", FileDialog.SAVE);
		  f.show();
		  String fname = f.getFile();
      String dname = f.getDirectory();
//       System.out.println("dir: " + dname + ", file: " + fname);
      File outfile = null;
		  if (fname != null) {
        outfile = new File(dname, fname);
        setSaveFile(outfile);
      } else {
        return false;
      }
      
//     }
		try {
// 				save(fname);
			save(outfile);
		} catch (IOException ioe) { ioe.printStackTrace(); }
		return true;
	}
	
/**
 * Gets an EntryPair object from file
 * @return EntryPair object
 * @param filename name of entryPair file
 * @param wc the application the EntryPair object should belong to
 */
// 	public static EntryPair load(String filename, Wcomp wc) throws IOException, ClassNotFoundException {
	public static EntryPair load(File file, Wcomp wc) throws IOException, ClassNotFoundException {
// 		FileInputStream fis = new FileInputStream(filename);
		FileInputStream fis = new FileInputStream(file);
		GZIPInputStream gzin = new GZIPInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(gzin);
		EntryPair tmpEP = (EntryPair) ois.readObject();
		if (tmpEP.drSaved) {
			DotterRegionSet drs = (DotterRegionSet) ois.readObject();
			drs.populateDottFileScores();
			wc.pw.dotterSet = drs;
			tmpEP.dotterRegions = drs;
		}
		tmpEP.zoom = -1;
		tmpEP.setApplication(wc);
// 		tmpEP.selectedGlyphs = new GlyphVector();
// 		tmpEP.selectors = new SelectorPair();
// 		tmpEP.addChild(selectors);
		tmpEP.repopulateTransients();
    tmpEP.setSaveFile(file);
		ois.close();
		return tmpEP;
	}
	
	/**
	 * Description
	 * @return descr
	 * @param param descr
	 */
	public void repopulateTransients() {
		selectedGlyphs = new GlyphVector();
		selectors = new SelectorPair();
		selectors.setParent(this);
		children.addElement(selectors);
    entry1.mergeSeqPieces();
    entry2.mergeSeqPieces();
	}
  /**
   * Sets the current save file
   * @param f file
   */
  public void setSaveFile(File f) {
    saveFile = f;
  }
  /**
   * Gets the current save file
   * @return current file
   */
  public File getSaveFile() {
    return saveFile;
  }
	
	
}
