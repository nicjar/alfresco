/*
 * $Revision: 1.2 $
 * $Id: MethodsDialog.java,v 1.2 2003/04/07 10:08:22 niclas Exp $
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
 * Dialog window for method parameters.
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class MethodsDialog extends Frame {
/**
 * Parent application
 */
	Wcomp wc ;
/**
 * Resource object for the application  
 */
  AlfrescoResources resources;
/**
 * Properties object for the method
 */
  Properties properties;
	
	
/**
 * Creates new MethodsDialog, 
 * @param wc Parent application
 * @param methodName name of method
 */
	public MethodsDialog(Wcomp wc, String methodName){
		super(methodName + "settings");
		this.wc = wc;
    this.resources = wc.getResources();
    this.properties = resources.getProgamProperties(methodName);
//     System.out.println(properties);
    int rows = properties.size() + 1;
// 		setSize(185,270);
		setSize(330,rows * 34);
		setLayout(new GridLayout(rows,1));
		FlowLayout rLayout = new FlowLayout(FlowLayout.RIGHT);
		FlowLayout cLayout = new FlowLayout(FlowLayout.CENTER);
    
    Enumeration propNames = properties.propertyNames();
    while (propNames.hasMoreElements()) {
      String name = (String) propNames.nextElement();
      Panel p = new Panel();
      
      if (name.equals("local")) {
        boolean local = true;
        if (properties.getProperty(name).equals("false")) {
          local = false;
        }
        CheckboxGroup cbg = new CheckboxGroup();
        Checkbox lcb = new Checkbox("local",local, cbg); 
        Checkbox rcb = new Checkbox("CORBA",!local, cbg);
        p.add(lcb);
        p.add(rcb); 
        p.setLayout(cLayout);
        
      } else if (name.endsWith("_CB")) { // set up a checkbox group for boolean flags
        Label l = new Label(name.substring(0, name.indexOf("_CB")));
        String val = properties.getProperty(name);
        boolean yes = true;
        if (!val.equals("true")) yes = false;
        CheckboxGroup cbg = new CheckboxGroup();
        Checkbox tcb = new Checkbox("true", yes, cbg); 
        Checkbox fcb = new Checkbox("false",!yes, cbg);
        p.add (l);
        p.add(tcb);
        p.add(fcb); 
        p.setLayout(cLayout);
        
      } else {
        Label l = new Label(name);
        String val = properties.getProperty(name);
        TextField tf = new TextField(val, 20);
        p.add(l);
        p.add(tf);
        p.setLayout(rLayout);
        
      }
      
      p.setName(name);
      add(p);
    }
    

		Button okB = new Button("OK");
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValues();
// 				setVisible(false);
				dispose();
			}
		});
		
		Button applyB = new Button("Apply");
		applyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValues();
			}
		});
		
		Button cancB = new Button("Cancel");
		cancB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
// 				setVisible(false);
				dispose();
			}
		});
		Panel buttonP = new Panel();
		buttonP.setLayout(cLayout);
		buttonP.add(okB);
		buttonP.add(applyB);
		buttonP.add(cancB);
    buttonP.setName("Buttons");
		add(buttonP);


	}
	
	
	/**
	 * Sets the values to the values in the different TextFields
	 */
	public void setValues() {
    Component [] comps = getComponents();
    PANELS: for (int i = 0; i < comps.length; i++) {
      Component comp = comps[i];
      String cname = comp.getName();
      System.out.println("Component name: " + cname);
      if (cname.equals("Buttons")) {
        continue;
//       } else if (cname.equals("local")) {
//         
      } else {
        Component [] subcomps = ((Container) comp).getComponents();
        String currentLabel = null;
        for (int j = 0; j < subcomps.length; j++) {
          Component subcomp = subcomps[j];
          if (subcomp instanceof Label) { 
            currentLabel = ((Label)subcomp).getText();
            continue; 
          } else if (subcomp instanceof Checkbox) {
            CheckboxGroup cbg = ((Checkbox) subcomp).getCheckboxGroup();
            Checkbox selected = cbg.getSelectedCheckbox();
            String label = selected.getLabel();
            if (label.equals("local")) {
              properties.put("local", "true");
              continue PANELS;
            } else if (label.equals("CORBA")){
              properties.put("local", "false");
              continue PANELS;
            } else { // for boolean flags
              properties.put(currentLabel + "_CB", label);
            }
          } else {
            TextField tf = (TextField) subcomp;
            properties.put(cname, ((TextField) subcomp).getText() );
          }   
        }
        
      }
      
    }
    resources.saveProperties();
// // 		match = Integer.parseInt(matchTF.getText());
// // 		umatch = Integer.parseInt(mismatchTF.getText());
// // 		gap = Integer.parseInt(gapTF.getText());
// // 		blockopen = Integer.parseInt(blockopenTF.getText());
// 		matchA = Double.valueOf(matchATF.getText()).doubleValue();
// 		matchB = Double.valueOf(matchBTF.getText()).doubleValue();
// 		matchC = Double.valueOf(matchCTF.getText()).doubleValue();
// 		matchD = Double.valueOf(matchDTF.getText()).doubleValue();
// 		umatch = Double.valueOf(umatchTF.getText()).doubleValue();
// 		gap = Double.valueOf(gapTF.getText()).doubleValue();
// 		blockopen = Double.valueOf(blockopenTF.getText()).doubleValue();
// // 		System.out.println(match + ", " + mismatch + ", " + gap + ", " + blockopen);
	}
	
	

}
