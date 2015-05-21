/*
 * $Revision: 1.1 $
 * $Id: FeatureEditDialog.java,v 1.1 2003/04/04 10:14:21 niclas Exp $
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
import java.lang.reflect.*;

/**
 * Dialog for creating/editing SeqFeature objects
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class FeatureEditDialog extends Frame {
	
// /**
//  * Window title
//  */
// 	String title = "Create/Edit sequence feature";
/**
 * The current EntryPair of the application	
 */
	EntryPair entries;
/**
 * Entry the feature belongs to
 */
	Entry entry;
/**
 * The feature	
 */
	SeqFeature feature;
/**
 * Whether the feature exists or not. Changes label of add button	
 */
	boolean featureExists = false;
/**
 * Choice menu for selecting parent entry
 */
	Choice entryChoice;
/**
 * List for available features
 */
	java.awt.List featureList;
/**
 * Label for entries	
 */
	Label entryL;
/**
 * Label for features	
 */
	Label featureL;
/**
 * Label for whether feature is rev-complement or not	
 */
	Label revL;
/**
 * Label for start position	
 */
	Label startL;
/**
 * Label for stop position	
 */	
	Label stopL;
/**
 * Label for tier level	
 */	
	Label tierL;
/**
 * Choice menu for selecting rev-compl
 */
	Choice revChoice;
/**
 * TextField for start position	
 */	
	TextField startTF;
/**
 * TextField for stop position	
 */	
	TextField stopTF;
/**
 * TextField for tier level
 */	
	TextField tierTF;
/**
 * Ok button
 */
	Button okB;
/**
 * Add button
 */	
	Button addB;
/**
 * Cancel button
 */	
	Button cancelB;
/**
 * Panel for listP and valueP
 */
	Panel compP;
/**
 * Panel for Entry and feature lists
 */
	Panel listP;
/**
 * Panel for value labels and text fields
 */
	Panel valueP;
/**
 * Panel for buttons
 */
	Panel buttonP;
	
	
	/**
	 * Creates a new FeatureEditDialog with the specified EntryPair
	 * @param entp current EntryPair of application
	 */
	public FeatureEditDialog(EntryPair entp) {
		super("Create/Edit sequence feature");
		entries = entp;
		initialize();
	}

	/**
	 * Creates a new FeatureEditDialog with the specified feature
	 * @param entp current EntryPair of application
	 * @param ent Entry to which feature should belong
	 */
	public FeatureEditDialog(EntryPair entp, Entry ent) {
		super("Create/Edit sequence feature");
		entries = entp;
		entry = ent;
		initialize();
		entryChoice.select(entry.getFilename());
		startTF.setText("1");
		stopTF.setText(String.valueOf(entry.getLength()));
		tierTF.setText("1");
	}
	
	/**
	 * Creates a new FeatureEditDialog with the specified feature
	 * @param entp current EntryPair of application
	 * @param feature feature to be edited
	 */
	public FeatureEditDialog(EntryPair entp, SeqFeature feature) {
		super("Create/Edit sequence feature");
		entries = entp;
		this.feature = feature;
		entry = feature.getEntry();
		featureExists = true;
		initialize();
		entryChoice.select(entry.getFilename());
		int i = 0;
		String fn = feature.getClassName();
		if (fn.equals("Selector")) {
			featureExists = false;
			addB.setLabel("Add");
		} else {
			while(i < featureList.getItemCount()) {
				String item = featureList.getItem(i);
				if (item.equals(fn)) break;
				i++;
			}
			featureList.select(i);
		}
		if (feature.getSeqRange().isComplement()) revChoice.select("Yes");
		startTF.setText(String.valueOf(feature.getSeqRange().getStart()));
		stopTF.setText(String.valueOf(feature.getSeqRange().getStop()));
		tierTF.setText(String.valueOf(feature.getTier()));
	}
	
	/**
	 * Adds all components to window
	 */
	private void initialize() {
		
		setBounds(200, 200, 320, 260);
		setLayout(new FlowLayout());
		
		compP = new Panel(new GridLayout(1,2));
 		
 		GridBagLayout gbl = new GridBagLayout();
 		GridBagConstraints gbc = new GridBagConstraints();
 		gbc.gridwidth = GridBagConstraints.REMAINDER;
 		gbc.anchor = GridBagConstraints.WEST;
 		
 		listP = new Panel(gbl);

		entryL = new Label("Entry:");
		gbl.setConstraints(entryL, gbc);
		listP.add(entryL);

		entryChoice = new Choice();
		entryChoice.add(" ");
		entryChoice.add(entries.entry1.getFilename());
		entryChoice.add(entries.entry2.getFilename());
		gbl.setConstraints(entryChoice, gbc);
		listP.add(entryChoice);
		
		featureL = new Label("Feature type:");
		gbl.setConstraints(featureL, gbc);
		listP.add(featureL);

		featureList = new java.awt.List(7, false);
		featureList.add("CpGIsland");
		featureList.add("Exon");
		featureList.add("Intron");
		featureList.add("Misc");
		featureList.add("Repeat");
		featureList.add("DNAtranspRepeat");
		featureList.add("LINERepeat");
		featureList.add("LTRRepeat");
		featureList.add("LowComplexityRepeat");
		featureList.add("Nrepeat");
		featureList.add("SINERepeat");
		featureList.add("SimpleRepeat");
		featureList.add("Similarity");
		featureList.add("TATA");
		featureList.add("TFBS");
		featureList.add("TrxStart");
		featureList.add("UTR");
		gbl.setConstraints(featureList, gbc);
		listP.add(featureList);

		compP.add(listP);
		
 		valueP = new Panel(gbl);
 		revL = new Label("Rev-Compl:");
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(revL, gbc);
		valueP.add(revL);
 		revChoice = new Choice();
 		revChoice.add("No");
 		revChoice.add("Yes");
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(revChoice, gbc);
		valueP.add(revChoice);
 		
		startL = new Label("Start:");
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(startL, gbc);
		valueP.add(startL);
		startTF = new TextField(5);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(startTF, gbc);
		valueP.add(startTF);
		
		stopL = new Label("Stop:");
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(stopL, gbc);
		valueP.add(stopL);
		stopTF = new TextField(5);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(stopTF, gbc);
		valueP.add(stopTF);
		
		tierL = new Label("Tier (1-" + CoordVal.MAXTIERS + "):");
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(tierL, gbc);
		valueP.add(tierL);
		tierTF = new TextField(5);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(tierTF, gbc);
		valueP.add(tierTF);
		compP.add(valueP);
		add(compP);
		
		buttonP = new Panel();
		okB = new Button("OK");
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateFeature();
				dispose();
			}
		});
		buttonP.add(okB);
		
		
		addB = new Button("Change");
		if (!featureExists) addB.setLabel("Add");
		addB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateFeature();
			}
		});
		buttonP.add(addB);
		
		cancelB = new Button("Cancel");
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonP.add(cancelB);
		add(buttonP);
	
	}
	
/**
 * updates and/or adds feature to canvas
 */
	public void updateFeature() {
		if (featureExists) {
			SeqRange r = feature.getSeqRange();
			r.setStart(Integer.parseInt(startTF.getText()));
			r.setStop(Integer.parseInt(stopTF.getText()));
			r.setComplement(revChoice.getSelectedItem().equals("Yes"));
			feature.setTier(Integer.parseInt(tierTF.getText()));
		} else {
			Class cl = null;
			Class paramarr[] = new Class[2];
			try {
				cl = Class.forName("alfresco." + featureList.getSelectedItem());
// 				System.out.println("Class: " + cl);
				paramarr[0] = Class.forName("alfresco.Entry");
				paramarr[1] = Class.forName("alfresco.SeqRange");
			} catch (ClassNotFoundException cnf) { System.out.println(cnf); }
			
			Constructor cons = null;
			try {
					cons = cl.getDeclaredConstructor(paramarr);
				
			} catch (NoSuchMethodException nsm) { System.out.println(nsm); }
			
			String name = entryChoice.getSelectedItem();
			if (name.equals(entries.entry1.getFilename())) {
				entry = entries.entry1;
			} else if (name.equals(entries.entry2.getFilename())){
				entry = entries.entry2;
			} else {
				ErrorDialog ed = new ErrorDialog(this, "You must select the Entry that the feature should belong to!");
				ed.show();
				return;
			}
			
			SeqRange r = new SeqRange(Integer.parseInt(startTF.getText()), 
			                          Integer.parseInt(stopTF.getText()),
			                          revChoice.getSelectedItem().equals("Yes"));
			Object initargs[] = new Object[2];
			initargs[0] = entry;
			initargs[1] = r;
			try {
				feature = (SeqFeature) cons.newInstance(initargs);
			}catch (IllegalAccessException ia) { System.out.println(ia); }
			 catch (InvocationTargetException it) { System.out.println(it); }
			 catch (InstantiationException ie) { System.out.println(ie); }
			 
			if (feature != null) entry.addChild(feature);
			featureExists = true;
			addB.setLabel("Change");
		}
		entries.wc.drawEntries();
		
	}
}
