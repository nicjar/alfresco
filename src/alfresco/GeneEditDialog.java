/*
 * $Revision: 1.1 $
 * $Id: GeneEditDialog.java,v 1.1 2003/04/04 10:14:24 niclas Exp $
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
import java.awt.*;
import java.awt.event.*;
/**
 * A dialog window for changing the tier and name of a Gene
 * @see java.awt.Dialog 
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class GeneEditDialog extends Dialog {
/**
 * Name text field
 */
	TextField nameTF;
/**
 * tier text field	
 */
	TextField tierTF;
/**
 * The set gene name
 */
	String name;
/**
 * The set tier value
 */
	int tier;
/**
 * Answer variable	
 */
	int answer;
/**
 * Ok constant
 */
	static final int OK = 1;
/**
 * Cancel constant
 */
	static final int CANCEL = 0;
	
	/**
	 * Creates new GeneEditDialog
	 * @param parent parent window
	 */
	public GeneEditDialog(Frame parent, String currentName, int currentTier) {
		super(parent,"Gene", true);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
// 		setLayout(new FlowLayout());
		setLayout(gbl);
		Point pp = parent.getLocationOnScreen();
		setBounds(pp.x + 100, pp.y + 100,200,120);
		setSize(300, 200);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		Label nl = new Label("Gene name:");
		gbl.setConstraints(nl, gbc);
		add(nl);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		nameTF = new TextField(currentName, 15);
		gbl.setConstraints(nameTF, gbc);
		add(nameTF);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		Label l = new Label("Tier:");
		gbl.setConstraints(l, gbc);
		add(l);
		int cTierVal = Math.abs(currentTier);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		tierTF = new TextField(String.valueOf(cTierVal), 4);
		gbl.setConstraints(tierTF, gbc);
		add(tierTF);
		tierTF.selectAll();
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		Button okB = new Button("OK");
		okB.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer = OK;
				tier = Integer.parseInt(tierTF.getText());
				name = nameTF.getText();
				GeneEditDialog.this.dispose();
			}
		});
		gbl.setConstraints(okB, gbc);
		add(okB);
// 		Button applyB = new Button("Apply");
// 		applyB.addActionListener( new ActionListener() {
// 			public void actionPerformed(ActionEvent e) {
// 				answer = APPLY;
// 				tier = Integer.parseInt(tierTF.getText());
// 			}
// 		});
// 		add(applyB);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		Button cancelB = new Button("Cancel");
		cancelB.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer = CANCEL;
				GeneEditDialog.this.dispose();
			}
		});
		gbl.setConstraints(cancelB, gbc);
		add(cancelB);
		pack();
	}
	
/**
 * Gets the answer pressed
 * @return Either GeneEditDialog.OK, or GeneEditDialog.CANCEL
 */
	public int getAnswer(){
		return answer;
	}

/**
 * Gets the name
 * @return name
 */
	public String getName(){
		return name;
	}
	
/**
 * Gets the tier
 * @return tier
 */
	public int getTier(){
		return tier;
	}
}
