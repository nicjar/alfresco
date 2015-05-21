/*
 * $Revision: 1.1 $
 * $Id: DbaParamDialog.java,v 1.1 2003/04/04 10:14:05 niclas Exp $
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
/**
 * Dialog window for dba alignment parameters.
 * Implements Singelton design pattern, there can only be one 
 * DbaParamDialog instance.
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class DbaParamDialog extends Frame {
/**
 * Holds the only possible instance of DbaParamDialog
 */
	private static DbaParamDialog instance = null;
/**
 * Window title
 */
	static String title = "dba settings";
/**
 * Parent application
 */
	Wcomp wc ;
/**
 * Match A value
 */
	double matchA;
/**
 * Match B value
 */
	double matchB;
/**
 * Match C value
 */
	double matchC;
/**
 * Match D value
 */
	double matchD;
/**
 * Umatch penalty value
 */
	double umatch;
/**
 * Gap penalty value
 */	
	double gap;
/**
 * Blockopen penalty value
 */
	double blockopen;
	
/**
 * Match A value label
 */
	Label matchAL;
/**
 * Match A value text field
 */
	TextField matchATF;
/**
 * Panel holding match A label and text field
 */	
	Panel matchAP;

/**
 * Match B value label
 */
	Label matchBL;
/**
 * Match B value text field
 */
	TextField matchBTF;
/**
 * Panel holding match B label and text field
 */	
	Panel matchBP;

/**
 * Match C value label
 */
	Label matchCL;
/**
 * Match C value text field
 */
	TextField matchCTF;
/**
 * Panel holding match C label and text field
 */	
	Panel matchCP;

/**
 * Match D value label
 */
	Label matchDL;
/**
 * Match D value text field
 */
	TextField matchDTF;
/**
 * Panel holding match D label and text field
 */	
	Panel matchDP;

/**
 * umatch value label
 */
	Label umatchL;
/**
 * umatch value text field
 */
	TextField umatchTF;
/**
 * Panel holding mismatch label and text field
 */	
	Panel umatchP;

/**
 * Gap value label
 */
	Label gapL;
/**
 * Gap value text field
 */
	TextField gapTF;
/**
 * Panel holding gap label and text field
 */	
	Panel gapP;

/**
 * Blockopen value label
 */
	Label blockopenL;
/**
 * Blockopen value text field
 */
	TextField blockopenTF;
/**
 * Panel holding blockopen label and text field
 */	
	Panel blockopenP;
/**
 * Button to set parameter values and close the DbaParamDialog
 */
	Button okB;
/**
 * Button to set parameter values
 */
	Button applyB;
/**
 * Button to close the DbaParamDialog
 */
	Button cancB;
/**
 * Panel holding buttons
 */
	Panel buttonP;

	
	
/**
 * Creates new DbaParamDialog, but can not be called directly. Use instance() instead.
 * @param wc Parent application
 * @param mA match A weight
 * @param mB match B weight
 * @param mC match C weight
 * @param mD match D weight
 * @param u umatch weight
 * @param g gap weight
 * @param b blockopen weight
 */
	private DbaParamDialog(Wcomp wc, 
	                       double mA, 
	                       double mB, 
	                       double mC, 
	                       double mD, 
												 double u, 
												 double g, 
												 double b){
		super(title);
		this.wc = wc;
		matchA = mA;
		matchB = mB;
		matchC = mC;
		matchD = mD;
		umatch = u;
		gap = g;
		blockopen = b;
		setSize(185,270);
		setLayout(new GridLayout(8,1));
		FlowLayout rLayout = new FlowLayout(FlowLayout.RIGHT);
		FlowLayout cLayout = new FlowLayout(FlowLayout.CENTER);

		matchAL = new Label("Match level A:");
		matchATF = new TextField(String.valueOf(matchA), 4);
		matchAP = new Panel();
		matchAP.setLayout(rLayout);
		matchAP.add(matchAL);
		matchAP.add(matchATF);
		add(matchAP);
		
		matchBL = new Label("Match level B:");
		matchBTF = new TextField(String.valueOf(matchB), 4);
		matchBP = new Panel();
		matchBP.setLayout(rLayout);
		matchBP.add(matchBL);
		matchBP.add(matchBTF);
		add(matchBP);
		
		matchCL = new Label("Match level C:");
		matchCTF = new TextField(String.valueOf(matchC), 4);
		matchCP = new Panel();
		matchCP.setLayout(rLayout);
		matchCP.add(matchCL);
		matchCP.add(matchCTF);
		add(matchCP);
		
		matchDL = new Label("Match level D:");
		matchDTF = new TextField(String.valueOf(matchD), 4);
		matchDP = new Panel();
		matchDP.setLayout(rLayout);
		matchDP.add(matchDL);
		matchDP.add(matchDTF);
		add(matchDP);
		
		umatchL = new Label("Umatch:");
		umatchTF = new TextField(String.valueOf(umatch), 4);
		umatchP = new Panel();
		umatchP.setLayout(rLayout);
		umatchP.add(umatchL);
		umatchP.add(umatchTF);
		add(umatchP);
		
		gapL = new Label("Gap:");
		gapTF = new TextField(String.valueOf(gap), 4);
		gapP = new Panel();
		gapP.setLayout(rLayout);
		gapP.add(gapL);
		gapP.add(gapTF);
		add(gapP);
		
		blockopenL = new Label("Blockopen:");
		blockopenTF = new TextField(String.valueOf(blockopen), 4);
		blockopenP = new Panel();
		blockopenP.setLayout(rLayout);
		blockopenP.add(blockopenL);
		blockopenP.add(blockopenTF);
		add(blockopenP);

		okB = new Button("OK");
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValues();
				setVisible(false);
			}
		});
		
		applyB = new Button("Apply");
		applyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValues();
			}
		});
		
		cancB = new Button("Cancel");
		cancB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonP = new Panel();
		buttonP.setLayout(cLayout);
		buttonP.add(okB);
		buttonP.add(applyB);
		buttonP.add(cancB);
		add(buttonP);


	}
	
/**
 * Returns a DbabParamDialog instance. Makes sure that there is only one instance of DbaParamDialog
 * @return a DbaParamDialog instance
 * @param wc the parent application
 * @param mA match A weight
 * @param mB match B weight
 * @param mC match C weight
 * @param mD match D weight
 * @param u umatch weight
 * @param g gap weight
 * @param b blockopen weight
 */
	static DbaParamDialog instance(Wcomp wc, 
	                                  double mA, 
	                      						double mB, 
	                      						double mC, 
	                      						double mD, 
																		double u, 
																		double g, 
																		double b) {
		if (instance == null) {
			instance = new DbaParamDialog(wc, mA, mB, mC, mD, u, g, b);
		}
		return instance;
	}
	
	/**
	 * Sets the values to the values in the different TextFields
	 */
	public void setValues() {
// 		match = Integer.parseInt(matchTF.getText());
// 		umatch = Integer.parseInt(mismatchTF.getText());
// 		gap = Integer.parseInt(gapTF.getText());
// 		blockopen = Integer.parseInt(blockopenTF.getText());
		matchA = Double.valueOf(matchATF.getText()).doubleValue();
		matchB = Double.valueOf(matchBTF.getText()).doubleValue();
		matchC = Double.valueOf(matchCTF.getText()).doubleValue();
		matchD = Double.valueOf(matchDTF.getText()).doubleValue();
		umatch = Double.valueOf(umatchTF.getText()).doubleValue();
		gap = Double.valueOf(gapTF.getText()).doubleValue();
		blockopen = Double.valueOf(blockopenTF.getText()).doubleValue();
// 		System.out.println(match + ", " + mismatch + ", " + gap + ", " + blockopen);
	}
	
	

}
