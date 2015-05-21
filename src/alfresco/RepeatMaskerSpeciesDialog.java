/*
 * $Revision: 1.1 $
 * $Id: RepeatMaskerSpeciesDialog.java,v 1.1 2003/04/04 10:14:57 niclas Exp $
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

public class RepeatMaskerSpeciesDialog extends Dialog {
/**
 * option string for seq 1
 */
	String option1;
/**
 * option string for seq 2
 */
	String option2;
/**
 * Checkbox group for seq 1
 */
	CheckboxGroup group1;
/**
 * Checkbox group for seq 2
 */
	CheckboxGroup group2;
/**
 * cancel flag
 */
	boolean cancel = false;
/**
 * Name of seq 1
 */
	String name1 = null;
/**
 * Name of seq 2
 */	
	String name2 = null;

/**
 * Creates Dialog with the specified parent
 * @param parent parent window
 */
	public RepeatMaskerSpeciesDialog (Frame parent) {
		super(parent, "Select Species", true);
		setup();
	}
	
/**
 * Creates Dialog with the specified parent
 * @param parent parent window
 * @param n1 name of seq 1
 * @param n2 name of seq 2
 */
	public RepeatMaskerSpeciesDialog (Frame parent, String n1, String n2) {
		super(parent, "Select Species", true);
		name1 = n1;
		name2 = n2;
		setup();
	}
/**
 * Sets up the window component
 */	
	public void setup() {
		setLocation(150,150);
 		GridBagLayout gbl = new GridBagLayout();
 		GridBagConstraints gbc = new GridBagConstraints();
// 		setLayout(new FlowLayout());
		setLayout(gbl);

		Panel panel1 = new Panel();
		
// 		panel1.setLayout(new GridLayout(5,1));
		panel1.setLayout(gbl);
		if (name1 == null) name1 = "Sequence 1";
		Label nameL1 = new Label(name1);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(nameL1, gbc);
		panel1.add(nameL1);
		
		group1 = new CheckboxGroup();
		gbc.anchor = GridBagConstraints.WEST;
// 		gbl.setConstraints(group1, gbc);
		Checkbox hCB1 = new Checkbox("Human", true, group1);
		Checkbox mCB1 = new Checkbox("Rodent", false, group1);
		Checkbox oCB1 = new Checkbox("Other Mammalian", false, group1);
		Checkbox dCB1 = new Checkbox("Drosophila", false, group1);
		Checkbox aCB1 = new Checkbox("Arabidopsis", false, group1);
		gbl.setConstraints(hCB1, gbc);
		gbl.setConstraints(mCB1, gbc);
		gbl.setConstraints(oCB1, gbc);
		gbl.setConstraints(dCB1, gbc);
		gbl.setConstraints(aCB1, gbc);
		panel1.add(hCB1);
		panel1.add(mCB1);
		panel1.add(oCB1);
		panel1.add(dCB1);
		panel1.add(aCB1);
		
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbl.setConstraints(panel1, gbc);
		add(panel1);

		Panel panel2 = new Panel();
// 		panel2.setLayout(new GridLayout(5,1));
		panel2.setLayout(gbl);
		if (name2 == null) name2 = "Sequence 2";
		Label nameL2 = new Label(name2);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(nameL2, gbc);
		panel2.add(nameL2);
		
		group2 = new CheckboxGroup();
		gbc.anchor = GridBagConstraints.WEST;
// 		gbl.setConstraints(group2, gbc);
// 		panel2.add(new Checkbox("Human", true, group2));
// 		panel2.add(new Checkbox("Mouse", false, group2));
// 		panel2.add(new Checkbox("Other Mammalian", false, group2));
		Checkbox hCB2 = new Checkbox("Human", true, group2);
		Checkbox mCB2 = new Checkbox("Rodent", false, group2);
		Checkbox oCB2 = new Checkbox("Other Mammalian", false, group2);
		Checkbox dCB2 = new Checkbox("Drosophila", false, group2);
		Checkbox aCB2 = new Checkbox("Arabidopsis", false, group2);
		gbl.setConstraints(hCB2, gbc);
		gbl.setConstraints(mCB2, gbc);
		gbl.setConstraints(oCB2, gbc);
		gbl.setConstraints(dCB2, gbc);
		gbl.setConstraints(aCB2, gbc);
		panel2.add(hCB2);
		panel2.add(mCB2);
		panel2.add(oCB2);
		panel2.add(dCB2);
		panel2.add(aCB2);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(panel2, gbc);
		add(panel2);

		Panel buttonP = new Panel();
		Button okB = new Button("OK");
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String choice1 = group1.getSelectedCheckbox().getLabel();
				String choice2 = group2.getSelectedCheckbox().getLabel();
				option1 = getOpt(choice1);
				option2 = getOpt(choice2);
				dispose();
			}
		});
		buttonP.add(okB);
// 		Panel okP = new Panel();
// 		okP.add(okB);
// 		panel1.add(okP);

		Button cancelB = new Button("Cancel");
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel = true;
				dispose();
			}
		});
		buttonP.add(cancelB);		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(buttonP, gbc);
		add(buttonP);
		
// 		Panel cP = new Panel();
// 		cP.add(cancelB);
// 		panel2.add(cP);
		pack();
	}

/**
 * Checks if the cancel button was pressed
 * @return true if cancel button was pressed, otherwise false
 */
	public boolean wasCanceled() {
		return cancel;
	}

/**
 * Gets the option string for seq 1
 * @return option string, either "", "-rod", "-mam", "-dr", or "-ar"
 */
	public String getOption1() {
		return option1;
	}

/**
 * Gets the option string for seq 2
 * @return option string, either "", "-rod", "-mam", "-dr", or "-ar"
 */
	public String getOption2() {
		return option2;
	}

/**
 * returns the option string based on the choice string
 * @return option string, either "", "-mus", or "-cow"
 * @param choice choice string
 */
	public String getOpt(String choice) {
		if (choice.equals("Human")) {
			return "";
		} else if (choice.equals("Rodent")) {
			return "-rod";
		} else if (choice.equals("Other Mammalian")){
			return "-mam";
		}else if (choice.equals("Drosophila")){
			return "-dr";
		}else if (choice.equals("Arabidopsis")){
			return "-ar";
		}
    return "";
	}
}
