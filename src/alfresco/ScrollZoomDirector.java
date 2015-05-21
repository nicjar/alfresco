/*
 * $Revision: 1.1 $
 * $Id: ScrollZoomDirector.java,v 1.1 2003/04/04 10:15:02 niclas Exp $
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
 * A class to mediate the interactions and behaviour of scrollbars and
 * zoombars. <p>
 * part of the Mediator design pattern
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */ 
 
public class ScrollZoomDirector {
/**
 * The window the scroll and zoom bars belong to
 */
	MainWindow mw;
	Entry entry1;
	Entry entry2;
/**
 * Scrollbar for entry 1 from the MainWindow
 */
	ColleagueScrollbar scroll1;
/**
 * Scrollbar for entry 2 from the MainWindow
 */
	ColleagueScrollbar scroll2;
/**
 * Zoombar for entry 1 from the MainWindow
 */
	ColleagueScrollbar zoom1;
/**
 * Zoombar for entry 2 from the MainWindow
 */
	ColleagueScrollbar zoom2;
/**
 * Scrollbar for synchronous scrolling from the MainWindow
 */
	ColleagueScrollbar synchSB;
/**
 * Scrollbar for synchronous zooming from the MainWindow
 */
	ColleagueScrollbar synchZSB;
/**
 * Mainwindow Label for displaying zoomlevel for entry1
 */
	ZoomLabel zoomLabel1; 
/**
 * Mainwindow Label for displaying zoomlevel for entry2
 */
	ZoomLabel zoomLabel2;
// 	int relZoomMax;
/**
 * Length of the longest Entry
 */
	int maxLen;
/**
 * Combined length of the two Entries + the length of the SeqCanv of the main window
 */
	int combLen;
/**
 * Combined length of the two Entries
 */
	int joinLen;
/**
 * Minimum value of zoomlevel
 */
	double zoomMin;
/**
 * Maximum value of zoomlevel
 */
	double zoomMax;
/**
 * Used in zoombar transformation
 */
	double lambda;
	
/**
 * Creates a ScrollZoomDirector for the specified MainWindow
 * @param window the MainWindow containing the scroll and zoom bars to managed
 */
	ScrollZoomDirector(MainWindow window){
		mw = window;
		this.scroll1 = mw.scroll1;
		scroll1.setDirector(this);
		this.scroll2 = mw.scroll2;
		scroll2.setDirector(this);
		this.zoom1 = mw.zoom1;
		zoom1.setDirector(this);
		this.zoom2 = mw.zoom2;
		zoom2.setDirector(this);
		this.synchSB = mw.synchSB;
		synchSB.setDirector(this);
		this.synchZSB = mw.synchZSB;
		synchZSB.setDirector(this);
		this.zoomLabel1 = mw.zoomLabel1;
		this.zoomLabel2 = mw.zoomLabel2;
	}
	
/**
 * Sets the Entries and updates values for scrollbars
 */
	void setEntries() {
		entry1 = mw.wc.entries.entry1;
		entry2 = mw.wc.entries.entry2;
		maxLen = Math.max(entry1.canvWidth, entry2.canvWidth);
		calculateCombLen();
// 		int max = joinLen + mw.seqCanvWidth;
		int vis = mw.seqCanvWidth;
// 		scroll1.setMinMax(-max ,joinLen);
		scroll1.setVisibleAmount(vis);
// 		scroll1.setValue(entry1.coord.getX());
// 		updateIncr(scroll1, entry1.coord.zoom);
		updateScroll(entry1,scroll1);	// added 980205
// 		scroll2.setMinMax(-max ,joinLen);
		scroll2.setVisibleAmount(vis);
// 		scroll2.setValue(entry2.coord.getX());
// 		updateIncr(scroll2,entry2.coord.zoom);
		updateScroll(entry2,scroll2);	// added 980205
// 		synchSB.setMinMax(-max ,joinLen);
		synchSB.setVisibleAmount(vis);
// 		synchSB.setValue(getCombX());
// 		updateIncr(synchSB,entry2.coord.zoom);
		updateSynchScroll();
// 		System.out.println("Entries set to: " + entry1 + ", " + entry2);
	}
	
/**
 * Performes the action called by changing a scrollbar, and manages interactions between 
 * scroll and zoom bars
 * @param sb the scroll or zoom bar that was changed
 * @param e the Event that caused the change
 */
// 	void scrollBarChanged(ColleagueScrollbar sb, AdjustmentEvent e){
	void scrollBarChanged(AdjustmentEvent e){
		ColleagueScrollbar sb = (ColleagueScrollbar) e.getAdjustable();
		
		if (sb.equals(scroll1) || sb.equals(scroll2) || sb.equals(synchSB)) {
			if (sb.equals(synchSB)) {
				double zoom = Math.max(entry1.coord.zoom, entry2.coord.zoom);
				int diff = e.getValue() - sb.getPrevVal();
				int bp = (int)(diff/zoom);
				scrollEntry(entry1, scroll1, -bp);
				scrollEntry(entry2, scroll2, -bp);
				sb.setPrevVal(e.getValue());
			} else if (sb.equals(scroll1)) {
				entry1.setX(-e.getValue());
			} else if (sb.equals(scroll2)) {
				entry2.setX(-e.getValue());
			}			
		} else if (sb.equals(zoom1) || sb.equals(zoom2) || sb.equals(synchZSB)) {
			if (sb.equals(synchZSB)) {
				zoomEntry(entry1, e);
				zoomEntry(entry2, e);
				calculateCombLen();
				updateScroll(entry1,scroll1);
				updateScroll(entry2,scroll2);
				updateSynchScroll();
				zoom1.setValue(e.getValue());
				zoom2.setValue(e.getValue());
				zoomLabel1.setValue(entry1.coord.zoom);
				zoomLabel2.setValue(entry2.coord.zoom);
			} else if (sb.equals(zoom1)) {
				zoomEntry(entry1, e);
				calculateCombLen();
				updateScroll(entry1,scroll1);
				updateScroll(entry2,scroll2);
				updateSynchScroll();
				synchZSB.setValue(e.getValue());
				zoomLabel1.setValue(entry1.coord.zoom);
			} else if (sb.equals(zoom2)) {
				zoomEntry(entry2, e);
				calculateCombLen();
				updateScroll(entry1,scroll1);
				updateScroll(entry2,scroll2);
				updateSynchScroll();
				synchZSB.setValue(e.getValue());
				zoomLabel2.setValue(entry2.coord.zoom);
			}
		}
		mw.wc.drawEntries();
	}
	
/**
 * Scrolls the specified Entry the specified number of bp's
 * @param ent Entry to be scrolled
 * @param sb the scroll bar involved
 * @param bp the number of bp's to scroll
 */
	void scrollEntry(Entry ent, ColleagueScrollbar sb, int bp){
		int currX = ent.coord.getX() + (int) Math.round(ent.coord.zoom * bp);
		ent.setX(currX);
		sb.setPrevVal(sb.getValue());
		sb.setValue(-currX);	
	}
	
/**
 * Re-calculates a scrollbars max and min values
 * @param ent entry associated with the scrollbar
 * @param sb the scrollbar to be updated
 */
	void updateScroll(Entry ent, ColleagueScrollbar sb) {
		int max = joinLen + mw.seqCanvWidth;
		sb.setMinMax(-max ,joinLen);
		sb.setValue(-ent.coord.getX());
		updateIncr(sb, ent.coord.zoom);
	}
	
/**
 * Re-calculates the synchro-scrollbar's max and min values
 */
	void updateSynchScroll() {
		int max = joinLen + mw.seqCanvWidth;
		synchSB.setMinMax(-max ,joinLen);
		synchSB.setValue(getCombX());
		synchSB.setPrevVal(getCombX());
		double zoom = Math.max(entry1.coord.zoom, entry2.coord.zoom);
		updateIncr(synchSB,zoom);
	}
	
/**
 * Changes a scrollbars increment value depending on the zoomlevel
 * @param sb scrollbar to be updated
 */
	void updateIncr(ColleagueScrollbar sb, double zoom) {
		int unitIncr;
		if ( zoom > 1 ) {
			unitIncr = 1;
		} else {
			unitIncr = (int) Math.round(1/zoom);
		}
		sb.setUnitIncrement(unitIncr);
		sb.setBlockIncrement(10*unitIncr);
	}
	
/**
 * Calculates the combLen = joinLen + width of seqCanv
 */
	void calculateCombLen() {
		calculateJoinLen();
		combLen = 2*joinLen + mw.seqCanvWidth;
	}
	
/**
 * Calculates the joinLen = width of entry1 + width of entry1 
 */
	void calculateJoinLen() {
		joinLen = entry1.getPixelWidth() + entry2.getPixelWidth();
	}
	
/**
 * Gets the combined x position of both entries
 * @return x pos of leftmost entry
 */
	int getCombX() {
		return Math.min(entry1.coord.getX(), entry2.coord.getX());
	}
	
	
/**
 * Zooms the specified entry
 * @param ent Entry to be zoomed
 * @param e the Event that caused the zooming
 */
	void zoomEntry(Entry ent, AdjustmentEvent e){
		int centerPos = ent.coord.getPosInCenter();
		ent.coord.zoom = calcZoomFactor(e.getValue());
		ent.coord.centerXOn(centerPos);
	}
	
/**
 * Sets the min and max values for the zoombars and calculates lambda
 * @param min min zoomlevel
 * @param max max zoomlevel
 */
	void setZoomMinMax(double min, double max) {
		zoomMin = min;
		zoomMax = max;
		lambda = Math.log((zoomMax + 1)/zoomMin);
		zoom1.setMinimum(0);
		zoom2.setMinimum(0);
		synchZSB.setMinimum(0);
		zoom1.setMaximum(100);
		zoom2.setMaximum(100);
		synchZSB.setMaximum(100);
		zoom1.setVisibleAmount(0);
		zoom2.setVisibleAmount(0);
		synchZSB.setVisibleAmount(0);
	}
	
/**
 * Transforms a value on the linear integral zoombar scale to a zoomfactor (zoomlevel) value
 *
 * <pre>Zoombar transformation
 * 
 * 0 <--> 1
 * val = scrollbar value
 * zf  = zoomfactor
 * 
 * zf = Ce^(lambda*val)
 * where val=0, zf = zoomMin  -> zoomMin = C
 * where val=1, zf = zoomMax  -> lambda = log(zoomMax/C) => lambda = log(zoomMax/zoomMin)
 * 
 * zoomfactor = zoomMin * e ^ lambda * val
 * if 0 <--> 100
 * zoomfactor = zoomMin * e ^ lambda * val/100</pre>
 *
 * @return zoomfactor
 * @param val zoombar value
 */
	double calcZoomFactor(int val) {
		double z = zoomMin * Math.pow(Math.E, lambda * (double) val/100);
		if (z > zoomMax) {
			return zoomMax;
		} else {
			return z;
		} 
	} 	
}
