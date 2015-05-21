/*
* $Revision: 1.1 $
* $Id: PairDialog.java,v 1.1 2003/04/04 11:25:43 niclas Exp $
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
package alfresco.applet;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for displaying entry pairs (from CORBA server)
 * @see java.awt.Dialog
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class PairDialog extends Dialog {
/**
 * List of entry pairs
 */
	List pairList;
/**
 * OK button
 */
	Button okB;
/**
 * Cancel button
 */
	Button cancelB;
/**
 * Name of selected pair
 */
	String name;
	
	/**
	 * Creates new PairDialog
	 * @param parent parent frame
	 * @param list string array of pair names
	 */
	public PairDialog(Frame parent, String [] list) {
		super(parent, "Select Sequence Pair", true);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		setLayout(gbl);
		pairList = new List(10, false);
// 		pairList.setSize(180, 200);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(pairList, gbc);
		add(pairList);
		
// 		Dimension d = pairList.getSize();
// 		Dimension d = pairList.getPreferredSize();
// 		System.out.println(d);
		Point pp = parent.getLocationOnScreen();
		setBounds(pp.x + 100, pp.y + 100, 180, 230);
// 		setBounds(pp.x + 100, pp.y + 100, d.width + 20, d.height + 50);
		
		for (int i = 0; i < list.length; i++) {
			pairList.addItem(list[i]);
		}
		okB = new Button("OK");
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name = pairList.getSelectedItem();
				dispose();
			}
		});
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbl.setConstraints(okB, gbc);
		add(okB);
		
		cancelB = new Button("Cancel");
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(cancelB, gbc);
		add(cancelB);
		
// 		pack();
	}
	
	/**
	 * Gets the name of the selected entry pair
	 * @return entry pair name, or null
	 */
	public String getSelection() {
		return name;
	}
}
