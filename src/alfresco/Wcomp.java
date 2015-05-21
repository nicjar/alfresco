/*
 * $Revision: 1.1 $
 * $Id: Wcomp.java,v 1.1 2003/04/04 10:15:26 niclas Exp $
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
 * The Alfresco application class.<P>
 * The name comes from the working name of the application before it was 
 * decided that it should be called Alfresco (<B>W</B>orkbench for <B>COMP</B>arison or
 * <B>W</B>indow for <B>COMP</B>arison).
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Wcomp implements UsefulConstants {
/**
 * Main window
 */
	MainWindow mw;
/**
 * Dialog for RegionSet analysis
 */
	RegionDialog pw;
/**
 * Dialog for dba alignment parameters
 */
	DbaParamDialog dbapw;
// /**
//  * Dialog for newdba alignment parameters
//  */
// 	NewDbaParamDialog newdbapw;
// /**
//  * Dialog for dbb alignment parameters
//  */
// 	DbbParamDialog dbbpw;
/**
 * The current EntryPair of application
 */
	EntryPair entries;
/**
 * Trx Factor binding sites that should be visible
 */
	TFBSSet tfSet;
/**
 * A temporary directory for storing temporary files. Deleted when application is
 * exited
 */
	File tmpdir;
/**
 * An off-screen image for drawing using "double buffering"	
 */
	Image offScreenImage;
/**
 * The Graphics of the off-screen image
 */
	Graphics offScreenGraphics;
/**
 * Font for drawing DNA sequence
 */
	public Font DNAFONT = new Font("Monospaced", Font.PLAIN, 14);
/**
 * Width of the dna font
 */
	public int DNAFONTWIDTH;
/**
 * If it is an application (true) or an applet (false)
 */
	boolean application;
/**
 * If it should be run in batch mode (no gui, set of analyses)
 */
	boolean batch;
/**
 * A CORBA server instance for applets
 */
	alfresco.server.AlfrescoServer pairServer;
  
  AlfrescoResources resources;

/**
 * Creates new Wcomp
 * @param application true if it is an application, false if it is an applet
 */
	public Wcomp(boolean application, boolean batch) {
		this.application = application;
    this.batch = batch;
		entries = new EntryPair(this);
		mw = null;
// 		System.out.println("Batch: " + batch);
		if (application) {
      resources = new AlfrescoResources();
			mw = MainWindow.instance(this);
			if(!batch) { mw.show(); }
			pw = RegionDialog.instance(this);
			tmpdir = new File(SystemConstants.TMPDIR);
			tmpdir.mkdir();
// 			System.out.println("usrdir: " + SystemConstants.USRDIR);
// 			System.out.println("tmpdir: " + SystemConstants.TMPDIR);
// 			System.out.println("DOTTERPATH = " + SystemConstants.DOTTERPATH);
// 			System.out.println("CPGPATH = " + SystemConstants.CPGPATH);
// 			System.out.println("DBAPATH = " + SystemConstants.DBAPATH);
// 			System.out.println("REPEATMASKERPATH = " + SystemConstants.REPEATMASKERPATH);
// 			System.out.println("GENSCANPATH = " + SystemConstants.GENSCANPATH);
// 			System.out.println("TSSWPATH = " + SystemConstants.TSSWPATH);
		} else {
			mw = MainWindow.instance(this);
			mw.show();
		}
		if (!batch) {
// 			System.out.println("Will create graphics. Batch: " + batch);
			offScreenImage = mw.seqCan.createImage(mw.seqCanvWidth, mw.seqCanvHeight);
			offScreenGraphics = offScreenImage.getGraphics();
		}
		dbapw = DbaParamDialog.instance(this, 
		                                DBAMATCH_A, 
		                                DBAMATCH_B, 
		                                DBAMATCH_C, 
		                                DBAMATCH_D, 
		                                DBAUMATCH, 
		                                DBAGAP, 
		                                DBABLOCKOPEN);
	};
	
// 	protected void finalize() {
// 	}
/**
 * Gets the AlfrescoResources object
 * @return AlfrescoResources object
 */
	public AlfrescoResources getResources() { 
    return resources; 
  }
	
/**
 * Updates the EntryGadget for sequence 1
 * @param e entry 1
 */
	public void updateEntry1(Entry e) {
// 		if (application)
			mw.entryGadget1.update(e);
 		if (entriesSet()) { drawEntries(); }
	}
	
/**
 * Updates the EntryGadget for sequence 2
 * @param e entry 2
 */
	public void updateEntry2(Entry e) {
// 		if (application)
			mw.entryGadget2.update(e);
 		if (entriesSet()) { drawEntries(); }
	}
	
/**
 * Sets entry 1 to the Entry specified, updates the EntryGadget, and
 * draw the entries if both are set
 * @param e the entry
 */
	public void setEntry1(Entry e) {
		entries.setEntry1(e);
// 		if (application)
			mw.entryGadget1.update(e);
 		if (entriesSet()) { drawEntries(); }
	}
	
/**
 * Sets entry 2 to the Entry specified, updates the EntryGadget, and
 * draw the entries if both are set
 * @param e the entry
 */
	public void setEntry2(Entry e) {
		entries.setEntry2(e);
// 		if (application)
			mw.entryGadget2.update(e);
 		if (entriesSet()) { drawEntries(); }
	}
/**
 * Gets the current EntryPair
 * @return entry pair
 */
	public EntryPair getEntryPair() { return entries; }
	
/**
 * Gets entry1 of the current EntryPair
 * @return entry 1
 */
	public Entry getEntry1() { return entries.entry1; }
	
/**
 * Gets entry2 of the current EntryPair
 * @return entry1
 */
	public Entry getEntry2() { return entries.entry2; }
	
/**
 * Checks if both entries are set
 * @return true if both entries are set, otherwise false
 */
	public boolean entriesSet() { 
		if (entries != null) {
			return entries.entriesSet();
		}
		return false;
	}
	
	/**
	 * gets the only instance of TFBSEditDialog
	 * @return dialog window instace
	 */
	public TFBSEditDialog getTFBSDialog() {
// 		if (tfSet == null) 
// 			tfSet = new TFBSSet();
// 		getTFSet();
		return TFBSEditDialog.instance(this);
	}
	
	/**
	 * gets TFBSSet
	 * @return TFBS set
	 */
	public TFBSSet getTFSet() {
		if (tfSet == null) 
			tfSet = new TFBSSet();
		return tfSet;
	}
	
/**
 * Clears the off-screen graphics, draws an image of the current EntryPair
 * and all children, and then draws the image to the Canvas
 */
	void drawEntries() {
		if (offScreenGraphics != null) {
			Graphics g = mw.seqCan.getGraphics();
			offScreenGraphics.clearRect(0, 0, mw.seqCanvWidth, mw.seqCanvWidth);
			entries.draw(offScreenGraphics);
			if(offScreenImage == null) return;
			g.drawImage(offScreenImage,0,0,mw.seqCan);
			g.dispose();
		}
	}

/**
 * For calling from paint method of SeqCan	
 */
	void drawEntries(Graphics g) {
		entries.draw(offScreenGraphics);
		g.drawImage(offScreenImage,0,0,mw.seqCan);
	}
	
/**
 * Updates the off-screen Image and Graphics to the current width and height 
 * of the main window Canvas
 */
	public void updateGraphics() {
		if (mw != null) {
			offScreenImage = mw.seqCan.createImage(mw.seqCanvWidth, mw.seqCanvHeight);
			if(offScreenImage == null) return;
			offScreenGraphics = offScreenImage.getGraphics();
			if (entries != null) {
				entries.updateCoordVals();
			}
		}
	}
	
/**
 * Quits the application. Deletes the tmpdir.
 * @return something
 * @param theparam descript
 */
	public void quit () { 
		if(application) {	
			String files[] = tmpdir.list();
	// 		System.out.println(files.length);
			if (files != null) {
				for(int i = 0; i < files.length; i++) {
					File tmp = new File(tmpdir, files[i]);
					tmp.delete();
				}
			}
			tmpdir.delete();
		}
		System.exit(0); 
	}
	/**
	 * Determines if it is an application or an applet
	 * @return true if application, false if applet
	 */
	public boolean isApplication() {
		return application;
	}
/**
 * Removes the current EntryPair
 */
	public void resetEntries() { 
		entries = new EntryPair(this); 
		pw.resetDotterSet();
	}
	
	/**
	 * Gets the MainWindow
	 * @return main window
	 */
	public MainWindow getMainWindow() {
		return mw;
	}
	
	/**
	 * Sets CORBA pair server
	 * @param server the CORBA server
	 */
	public void setServer(alfresco.server.AlfrescoServer server) {
		pairServer = server;
	}
	/**
	 * Gets CORBA pair server
	 * @return server instance
	 */
	public alfresco.server.AlfrescoServer getServer() {
		return pairServer;
	}
	
}
