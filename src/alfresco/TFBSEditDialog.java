/*
* $Revision: 1.1 $
* $Id: TFBSEditDialog.java,v 1.1 2003/04/04 10:15:17 niclas Exp $
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
 * Dialog for selecting TFBS to show
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public class TFBSEditDialog extends Frame implements Observer {
/**
 * The only instance of TFBSEditDialog
 */
	private static TFBSEditDialog instance;
// /**
//  * Vector of TFBSs
//  */
// 	Vector tfVector;
/**
 * Application
 */
	Wcomp wc;
/**
 * TFBSSet to observ
 */
	TFBSSet tfSet;
/**
 * panel for all list
 */
	Panel allP;
/**
 * panel for buttons
 */
	Panel buttonP;
/**
 * panel for selection list
 */	
	Panel selP;
/**
 * List of all TFBSs
 */
	java.awt.List tfList;
/**
 * List of TFBSs selected for viewing
 */
	java.awt.List selList;
/**
 * reset selection button	for tfList
 */
	Button resetAllB;
/**
 * select all button for tfList
 */
	Button selectAllB;
/**
 * apply button	
 */
	Button applyB;
/**
 * TFBSs in both entries button	
 */
	Button bothB;
/**
 * done button	
 */	
	Button doneB;
/**
 * arrow button	
 */	
	Button arrowB;
/**
 * reset selection button for selList
 */
	Button resetSelB;
/**
 * select all button for selList
 */
	Button selectSelB;
/**
 * remove selelected item button
 */
	Button removeSelB;
/**
 * clear selList button
 */
	Button clearSelB;
	
	
	/**
	 * Cretes a new TFBSEditDialog
	 * @param set TFBSSet to update/watch
	 */
	private TFBSEditDialog(Wcomp wc) {
		super("Select binding sites");
		this.wc = wc;
// 		tfSet = wc.tfSet;
		tfSet = wc.getTFSet();
		tfSet.addObserver(this);
// 		tfVector = new Vector();
		setBounds(200, 200, 470, 350);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
// 		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		
		Panel allP = new Panel();
		allP.setLayout(gbl);
		
		Label allL = new Label("All TFBSs:");
		gbl.setConstraints(allL, gbc);
		allP.add(allL);
		
		tfList = new java.awt.List(10, true);
// 		tfList.addActionListener(new ActionListener() {
// 			public void actionPerformed(ActionEvent e) {
// 				int[] selected = tfList.getSelectedIndexes();
// 				Vector ids = new Vector(selected.length);
// 				for(int i = 0; i < selected.length; i++)
// 					ids.addElement(tfList.getItem(selected[i]));
// 				tfSet.setIDs(ids);
// 			}
// 		});
		gbl.setConstraints(tfList, gbc);
		allP.add(tfList);
		
		bothB = new Button("TFBSs in both entries");
		bothB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector ids = TFBSEditDialog.this.wc.getEntryPair().getCommonTFBSIds();
				int[] selected = tfList.getSelectedIndexes();
				for(int i = 0; i < selected.length; i++) 
					tfList.deselect(selected[i]);
				Enumeration iden = ids.elements();
				SELECTED: while(iden.hasMoreElements()) {
					String id = (String) iden.nextElement();
					for(int i = 0; i < tfList.getItemCount(); i++) {
							if (tfList.getItem(i).equals(id)) {
								tfList.select(i);
								continue SELECTED;
							}
					}
				}
			}
		});		
		gbl.setConstraints(bothB, gbc);
		allP.add(bothB);
		
		resetAllB = new Button("Clear Selection");
		resetAllB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selected = tfList.getSelectedIndexes();
				for(int i = 0; i < selected.length; i++) 
					tfList.deselect(selected[i]);
			}
		});	
		gbl.setConstraints(resetAllB, gbc);
		allP.add(resetAllB);

		selectAllB = new Button("Select all");
		selectAllB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector ids = new Vector(tfList.getItemCount());
				for (int i = 0; i < tfList.getItemCount(); i++)
					tfList.select(i);
// 					ids.addElement(tfList.getItem(i));
// 				tfSet.setIDs(ids);
			}
		});		
		gbl.setConstraints(selectAllB, gbc);
		allP.add(selectAllB);
		add(allP);

// 		gbc.gridheight = 3;
// 		gbc.gridwidth = GridBagConstraints.RELATIVE;
		
		buttonP = new Panel();
		buttonP.setLayout(gbl);
		
		
		arrowB = new Button("-->");
		arrowB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
// 				Vector ids = new  Vector();
				int[] selected = tfList.getSelectedIndexes();
				for(int i = 0; i < selected.length; i++) 
					addToSelList(tfList.getItem(selected[i]));
				for(int i = 0; i < selList.getItemCount(); i++) {
					selList.select(i);
				}
				tfSet.setIDs(getSelIds());
// 					sel.addItem(tfList.getItem(selected[i]));
// 				tfSet.setIDs(sel);
			}
		});		
		gbl.setConstraints(arrowB, gbc);
		buttonP.add(arrowB);
		
// 		applyB = new Button("Apply selection");
// 		applyB.addActionListener(new ActionListener() {
// 			public void actionPerformed(ActionEvent e) {
// // 				int[] selected = tfList.getSelectedIndexes();
// 				int[] selected = selList.getSelectedIndexes();
// 				Vector ids = new Vector(selected.length);
// // 				if (cselected.length > 0) {
// // 					for(int i = 0; i < cselected.length; i++)
// // 						ids.addElement(selList.getItem(cselected[i]));
// // 					
// // 				}
// 				for(int i = 0; i < selected.length; i++)
// 					ids.addElement(selList.getItem(selected[i]));
// // 					ids.addElement(selList.getItem(selected[i]));
// 				tfSet.setIDs(ids);
// 			}
// 		});		
// 		gbl.setConstraints(applyB, gbc);
// 		buttonP.add(applyB);
		
// 		gbc.gridheight = GridBagConstraints.REMAINDER;
		doneB = new Button("Done");
		doneB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		gbl.setConstraints(doneB, gbc);
		buttonP.add(doneB);
		
		add(buttonP);
		
		selP = new Panel();
		selP.setLayout(gbl);
		
		Label selL = new Label("TFBSs to show:");
		gbl.setConstraints(selL, gbc);
		selP.add(selL);
		
		selList = new java.awt.List(10, true);
		selList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
// 				int[] selected = selList.getSelectedIndexes();
// 				Vector ids = new Vector(selected.length);
// 				for(int i = 0; i < selected.length; i++)
// 					ids.addElement(selList.getItem(selected[i]));
				tfSet.setIDs(getSelIds());
			}
		});
		selList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
// 				int[] selected = selList.getSelectedIndexes();
// 				Vector ids = new Vector(selected.length);
// 				for(int i = 0; i < selected.length; i++)
// 					ids.addElement(selList.getItem(selected[i]));
				tfSet.setIDs(getSelIds());
			}
		});
// 		gbc.gridwidth = GridBagConstraints.REMAINDER;
// 		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbl.setConstraints(selList, gbc);
		selP.add(selList);
		
		resetSelB = new Button("Clear Selection");
		resetSelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
// 				int[] selected = selList.getSelectedIndexes();
// 				for(int i = 0; i < selected.length; i++) 
// 					selList.deselect(selected[i]);
				tfSet.setIDs(new Vector(1));
			}
		});	
		gbl.setConstraints(resetSelB, gbc);
		selP.add(resetSelB);
		
		selectSelB = new Button("Select all");
		selectSelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector ids = new Vector(selList.getItemCount());
				for (int i = 0; i < selList.getItemCount(); i++)
// 					selList.select(i);
					ids.addElement(selList.getItem(i));
				tfSet.setIDs(ids);
			}
		});
		gbl.setConstraints(selectSelB, gbc);
		selP.add(selectSelB);
		
		removeSelB = new Button("Remove item");
		removeSelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selected = selList.getSelectedIndexes();
				for (int i = selected.length - 1; i >= 0; i--) 
					selList.remove(selected[i]);
				tfSet.setIDs(getSelIds());
			}
		});
		gbl.setConstraints(removeSelB, gbc);
		selP.add(removeSelB);
		
		clearSelB = new Button("Remove all items");
		clearSelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selList.removeAll();
				tfSet.setIDs(new Vector(1));
			}
		});
		gbl.setConstraints(clearSelB, gbc);
		selP.add(clearSelB);
		
		add(selP);
		
// 		clearB = new Button("Clear Selection");
// 		clearB.addActionListener(new ActionListener() {
// 			public void actionPerformed(ActionEvent e) {
// // 				int[] selected = tfList.getSelectedIndexes();
// // 				for(int i = 0; i < selected.lenght; i++) 
// // 					tfList.deselect(selected[i]);
// 				tfSet.setIDs(new Vector(1));				
// 			}
// 		});		
// 		gbl.setConstraints(clearB, gbc);
// 		add(clearB);
		
// 		gbc.gridheight = GridBagConstraints.RELATIVE;
// 		selectAllB = new Button("Select all");
// 		selectAllB.addActionListener(new ActionListener() {
// 			public void actionPerformed(ActionEvent e) {
// 				Vector ids = new Vector(tfList.getItemCount());
// 				for (int i = 0; i < tfList.getItemCount(); i++)
// // 					tfList.select(i);
// 					ids.addElement(tfList.getItem(i));
// 				tfSet.setIDs(ids);
// 			}
// 		});		
// 		gbl.setConstraints(selectAllB, gbc);
// 		add(selectAllB);
		
		
// 		pack();
		
	}
	
	/**
	 * Returns a TFBSEditDialog instance
	 * @return TFBSEditDialog
	 */
	public static TFBSEditDialog instance(Wcomp wc) {
		if (instance == null) {
			instance = new TFBSEditDialog(wc);
		}
		return instance;
	}
	
	/**
	 * Will update the list of selected TFBSs. Defines Observer
	 */
	public void update(Observable o, Object arg) {
// 		System.out.println("TFBSEditDialog: updating");
		if (o instanceof TFBSSet) {
			Vector ids = (Vector) arg;
			int[] selected = selList.getSelectedIndexes();
			for(int i = 0; i < selected.length; i++) 
				selList.deselect(selected[i]);
			//loop through lists to select items
			Enumeration iden = ids.elements();
			while (iden.hasMoreElements()){
				String id = (String) iden.nextElement();
				for (int i = 0; i < selList.getItemCount(); i++) 
					if (id.equals(selList.getItem(i))) {
						selList.select(i);
						continue;
					}
			}
		}
	}
	
	/**
	 * Adds a TFBS id to list
	 * @param id TFBS id
	 */
	public void addTFBS(String id) {
		String[] items = tfList.getItems();
		for(int i = 0; i < items.length; i++) {
			if (id.compareTo(items[i]) < 0) {
				tfList.addItem(id, i);
				return;
			} else if (id.compareTo(items[i]) == 0) {
				return;
			}
		}
		tfList.addItem(id);
	}
	
	/**
	 * Adds a TFBS id to sel list
	 * @param id TFBS id
	 */
	public void addToSelList(String id) {
		String[] items = selList.getItems();
		for(int i = 0; i < items.length; i++) {
			if (id.compareTo(items[i]) < 0) {
				selList.addItem(id, i);
				return;
			} else if (id.compareTo(items[i]) == 0) {
				return;
			}
		}
		selList.addItem(id);
	}
	
	/**
	 * Gets a vector of selected ids from sel list
	 * @return Vector of id strings
	 */
	public Vector getSelIds() {
		int[] selected = selList.getSelectedIndexes();
		Vector ids = new Vector(selected.length);
		for (int i = 0; i < selected.length; i++) 
			ids.addElement(selList.getItem(selected[i]));
		return ids;
	}
	
	/**
	 * Removes a TFBS id from list
	 * @return true if there are no more TFBSs to select, otherwise false
	 * @param id TFBS id
	 */
	public boolean removeTFBS(String id) {
		tfList.remove(id);
		return tfList.getItemCount() == 0? true: false;
	}
	
// 	/**
// 	 * Gets a vector of ids that should be visible
// 	 * @return vector of id strings
// 	 */
// 	public Vector getIds() {
// 		
// 	}
	
}
