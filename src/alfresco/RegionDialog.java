/*
 * $Revision: 1.1 $
 * $Id: RegionDialog.java,v 1.1 2003/04/04 10:14:52 niclas Exp $
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
 * Dialog window for the Region Set functionality.
 * Implements Singelton design pattern, there can only be one 
 * RegionDialog instance.
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class RegionDialog extends Frame {
/**
 * Holds the only possible instance of RegionDialog
 */
	private static RegionDialog instance = null;
/**
 * Window title
 */
	static String title = "Region set parameters";
/**
 * Parent application
 */
	Wcomp wc ;
/**
 * Cutoff value for DotterRegions 
 */
	int cutOffVal = 100;
// 	boolean excludeCDS1 = true;
// 	boolean excludeCDS2 = true;
/**
 * Scrollbar for cutoff value
 */
	Scrollbar cutoffSB;
/**
 * Text field for cutoff value
 */
	TextField cutoffvalTF;
/**
 * Label for cutoff
 */
	Label cutoffL;
/**
 * Label for min cutoff value
 */
	Label minL;
/**
 * Label for max cutoff value
 */
	Label maxL;
/**
 * Panel holding cutoff scrollbar, textfield, and labels 
 */
	Panel scrollbarP;

/**
 * Label for exclude repeat checkboxes	
 */
	Label excludeRepL;
/**
 * Checkbox for excluding repeats in sequence 1
 */
	Checkbox excRep1CB;
/**
 * Checkbox for excluding repeats in sequence 2
 */
	Checkbox excRep2CB;
/**
 * Panel holding exclude repeat checkboxes and label
 */
	Panel repCheckboxP;	
	
/**
 * Label for exclude cds checkboxes	
 */
	Label excludeCdsL;
/**
 * Checkbox for excluding cds in sequence 1
 */
	Checkbox excCds1CB;
/**
 * Checkbox for excluding cds in sequence 2
 */
	Checkbox excCds2CB;
/**
 * Panel holding exclude cds checkboxes and label
 */
	Panel cdsCheckboxP;

/**
 * Button for initializing Region set functionality
 */
	Button initializeB;
/**
 * Button for adding a region set to the EntryPair of the application
 */
	Button addB;
/**
 * Button for removing a region set from the EntryPair of the application
 */
	Button removeB;
/**
 * Button to close the RegionDialog
 */
	Button cancB;
/**
 * Panel holding buttons
 */
	Panel buttonP;

/**
 * A set of DotterRegions 
 */
	DotterRegionSet dotterSet;
/**
 * Vector of ranges to be excluded in sequence 1 when finding RegionSets
 */
	Vector excludeV1;
/**
 * Vector of ranges to be excluded in sequence 2 when finding RegionSets
 */
	Vector excludeV2;
	
	
/**
 * Creates new RegionDialog, but can not be called directly. Use instance() instead.
 * @param wc Parent application
 */
	private RegionDialog(Wcomp wc){
		super(title);
		this.wc = wc;
		
		//reinstate ?
// 		addComponentListener( new ComponentAdapter() {
// 			public void componentShown(ComponentEvent e) {
// 				if (dotterSet != null) {
// 					wc.entries.setDotterRegions(dotterSet);
// 					excludeV1 = wc.entries.entry1.getExclude();
// 					excludeV2 = wc.entries.entry2.getExclude();
// 					updateDotterSet();
// 				}
// 			}
// 		});
		setSize(300,170);
		setLayout(new BorderLayout());
		cutoffSB = new Scrollbar(Scrollbar.HORIZONTAL, cutOffVal, 0, 1, 255);
		cutoffSB.addAdjustmentListener( new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent ae) {
				cutOffVal = ae.getValue();
				updateDotterSet();
				cutoffvalTF.setText(Integer.toString(cutOffVal));
			}
		});
		cutoffvalTF = new TextField(Integer.toString(cutOffVal),5);
		cutoffvalTF.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cutOffVal = Integer.parseInt(cutoffvalTF.getText());
				updateDotterSet();
				cutoffSB.setValue(cutOffVal);
			}
		});
		cutoffL = new Label("Cutoff:  ", Label.LEFT);
		minL = new Label("1", Label.RIGHT);
		maxL = new Label("255", Label.LEFT);
		scrollbarP = new Panel();
		scrollbarP.add(cutoffL);
		scrollbarP.add(minL);
		scrollbarP.add(cutoffSB);
		scrollbarP.add(maxL);
		scrollbarP.add(cutoffvalTF);
		add("North",scrollbarP);

		excludeRepL = new Label("Exclude Repeats in");
		excRep1CB = new Checkbox("Sequence 1", true);
		excRep2CB = new Checkbox("Sequence 2", true);
		repCheckboxP = new Panel();
		repCheckboxP.setLayout( new GridLayout(3,1,0,0));
		repCheckboxP.add(excludeRepL);
		repCheckboxP.add(excRep1CB);
		repCheckboxP.add(excRep2CB);
		repCheckboxP.setEnabled(false);
		add("West", repCheckboxP);

		excludeCdsL = new Label("Exclude CDS in");
		excCds1CB = new Checkbox("Sequence 1", true);
		excCds2CB = new Checkbox("Sequence 2", true);
		cdsCheckboxP = new Panel();
		cdsCheckboxP.setLayout( new GridLayout(3,1,0,0));
		cdsCheckboxP.add(excludeCdsL);
		cdsCheckboxP.add(excCds1CB);
		cdsCheckboxP.add(excCds2CB);
		cdsCheckboxP.setEnabled(false);
		add("East", cdsCheckboxP);

		initializeB = new Button("Initialize");
		initializeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegionDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				// get vector of CDS ranges to exclude depending 
				// on state of exc1CB and exc2CB
				if (excCds1CB.getState()) {
					excludeV1 = RegionDialog.this.wc.entries.entry1.getExonRanges();
				}
				if (excCds2CB.getState()) {
					excludeV2 = RegionDialog.this.wc.entries.entry2.getExonRanges();
				}
				// get vector of repeat ranges to exclude depending 
				// on state. !! Must add checkboxes for repeats!!!!
				if (repCheckboxP.isEnabled()) {
					if (excRep1CB.getState()) {
						Vector tmpv = RegionDialog.this.wc.entries.entry1.getRepeats();
						Enumeration tmpe = tmpv.elements();
						while(tmpe.hasMoreElements()) {
							SeqRange r = (SeqRange) tmpe.nextElement();
							excludeV1.addElement(r);
						}
					}
					if (excRep2CB.getState()) {
						Vector tmpv = RegionDialog.this.wc.entries.entry2.getRepeats();
						Enumeration tmpe = tmpv.elements();
						while(tmpe.hasMoreElements()) {
							SeqRange r = (SeqRange) tmpe.nextElement();
							excludeV2.addElement(r);
						}
					}
				}
				dotterSet = new DotterRegionSet(RegionDialog.this.wc.entries, excludeV1, excludeV2);
				RegionDialog.this.wc.entries.setDotterRegions(dotterSet);
				updateDotterSet();
				initializeB.setEnabled(false);
			}
		});

		addB = new Button("Add");
		addB.setEnabled(false);
		addB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AttributeDialog dialog = new AttributeDialog(RegionDialog.this, "Add Region Set");
				Color color = dialog.getColor();
				if (color != null) {
					RegionDialog.this.wc.entries.currentRegionSet.setColor(color);
					RegionDialog.this.wc.entries.addRegionSet();
					RegionDialog.this.wc.drawEntries();
					addB.setEnabled(false);
				}
			}
		});
		
		removeB = new Button("Remove");
		removeB.setEnabled(false);
		removeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (RegionDialog.this.wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = RegionDialog.this.wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof RegionSet) {
							RegionDialog.this.wc.entries.removeChild(gl);
						}
					}
					RegionDialog.this.wc.drawEntries();
					removeB.setEnabled(false);
				}
			}
		});
		
		
		cancB = new Button("Cancel");
		cancB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RegionDialog.this.wc.entries.entriesSet()) {
					RegionDialog.this.wc.entries.removeDotterRegions();
					RegionDialog.this.wc.entries.currentRegionSet = null;
					RegionDialog.this.wc.drawEntries();
				}
				setVisible(false);
			}
		});
		buttonP = new Panel();
		buttonP.add(initializeB);
		buttonP.add(addB);
		buttonP.add(removeB);
		buttonP.add(cancB);
		add("South",buttonP);


	}
	
/**
 * Returns a RegionDialog instance. Makes sure that there is only one instance of RegionDialog
 * @return a RegionDialog instance
 * @param wc the parent application
 */
	static RegionDialog instance(Wcomp wc) {
		if (instance == null) {
			instance = new RegionDialog(wc);
		}
		return instance;
	}
	
/**
 * Updates the set of DotterRegions to be shown depending on the cutoff value
 */
	void updateDotterSet() {
		if (wc.entries.dotterRegions != null) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			dotterSet.updateChildren(cutOffVal);
			wc.drawEntries();
			this.setCursor(Cursor.getDefaultCursor());
		}
	}
	
/**
 * Removes current DotterSet
 */
	void resetDotterSet() {
		dotterSet = null;
		initializeB.setEnabled(true);

	}
	
/**
 * This member class defines a Color choosing Dialog
 * @see java.awt.Dialog
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 	public class AttributeDialog extends Dialog {
		Canvas blackCanvas;
		Canvas darkGrayCanvas;
		Canvas grayCanvas;
		Canvas lightGrayCanvas;
		Canvas blueCanvas;
		Canvas cyanCanvas;
		Canvas greenCanvas;
		Canvas yellowCanvas;
		Canvas orangeCanvas;
		Canvas magentaCanvas;
		Canvas pinkCanvas;
		Canvas redCanvas;
		
		Panel labelPanel;
		Panel buttonPanel;
		
		Label colorLabel;
		Panel colorPanel;
		
		Button okButton;
		Button cancelButton;
		ClickListener listener;
		Color color = Color.cyan;

		AttributeDialog(RegionDialog parent, String title) {
			super(parent, title, true);
			setLayout( new FlowLayout() );
			Point pp = parent.getLocationOnScreen();
			setBounds(pp.x - 160, pp.y,150,200);
// 			setSize(150, 200);
			setBackground(Color.white);
			listener = new ClickListener();
			blackCanvas = new Canvas();
			blackCanvas.setBackground(Color.black);
			blackCanvas.setSize(25,25);
			blackCanvas.addMouseListener(listener);
			add(blackCanvas);
			darkGrayCanvas = new Canvas();
			darkGrayCanvas.setBackground(Color.darkGray);
			darkGrayCanvas.setSize(25,25);
			darkGrayCanvas.addMouseListener(listener);
			add(darkGrayCanvas);
			grayCanvas = new Canvas();
			grayCanvas.setBackground(Color.gray);
			grayCanvas.setSize(25,25);
			grayCanvas.addMouseListener(listener);
			add(grayCanvas);
			lightGrayCanvas = new Canvas();
			lightGrayCanvas.setBackground(Color.lightGray);
			lightGrayCanvas.setSize(25,25);
			lightGrayCanvas.addMouseListener(listener);
			add(lightGrayCanvas);
			blueCanvas = new Canvas();
			blueCanvas.setBackground(Color.blue);
			blueCanvas.setSize(25,25);
			blueCanvas.addMouseListener(listener);
			add(blueCanvas);
			cyanCanvas = new Canvas();
			cyanCanvas.setBackground(Color.cyan);
			cyanCanvas.setSize(25,25);
			cyanCanvas.addMouseListener(listener);
			add(cyanCanvas);
			greenCanvas = new Canvas();
			greenCanvas.setBackground(Color.green);
			greenCanvas.setSize(25,25);
			greenCanvas.addMouseListener(listener);
			add(greenCanvas);
			yellowCanvas = new Canvas();
			yellowCanvas.setBackground(Color.yellow);
			yellowCanvas.setSize(25,25);
			yellowCanvas.addMouseListener(listener);
			add(yellowCanvas);
			orangeCanvas = new Canvas();
			orangeCanvas.setBackground(Color.orange);
			orangeCanvas.setSize(25,25);
			orangeCanvas.addMouseListener(listener);
			add(orangeCanvas);
			magentaCanvas = new Canvas();
			magentaCanvas.setBackground(Color.magenta);
			magentaCanvas.setSize(25,25);
			magentaCanvas.addMouseListener(listener);
			add(magentaCanvas);
			pinkCanvas = new Canvas();
			pinkCanvas.setBackground(Color.pink);
			pinkCanvas.setSize(25,25);
			pinkCanvas.addMouseListener(listener);
			add(pinkCanvas);
			redCanvas = new Canvas();
			redCanvas.setBackground(Color.red);
			redCanvas.setSize(25,25);
			redCanvas.addMouseListener(listener);
			add(redCanvas);

			labelPanel = new Panel();
			colorLabel = new Label("Color:    ");
			labelPanel.add(colorLabel);
			colorPanel = new Panel();
			colorPanel.setBackground(color);
			colorPanel.setSize(25,25);
			labelPanel.add(colorPanel);
			add(labelPanel);
			
			buttonPanel = new Panel();
			okButton = new Button("OK");
			okButton.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPanel.add(okButton);
			
			cancelButton = new Button("Cancel");
			cancelButton.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					color = null;
					dispose();
				}
			});
			buttonPanel.add(cancelButton);
			add(buttonPanel);
			show();
			
		}
		
/**
 * Gets the choosen color
 * @return choosen Color
 */
		public Color getColor() {
			return color;
		}
/**
 * This member class defines a ClickListener for the color panels
 * @see java.awt.event.MouseAdapter
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 		class ClickListener extends MouseAdapter {
			ClickListener() { super(); }
			
			public void mouseClicked(MouseEvent e) {
				Canvas p = (Canvas) e.getComponent();
				color = p.getBackground();
				colorPanel.setBackground(color);
			}
		}
	}

}
