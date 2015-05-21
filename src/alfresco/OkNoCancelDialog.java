/*
 * $Revision: 1.1 $
 * $Id: OkNoCancelDialog.java,v 1.1 2003/04/04 10:14:45 niclas Exp $
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
 * A message dialog with Ok, No, and Cancel buttons
 * @see java.awt.Dialog
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class OkNoCancelDialog extends Dialog {
/**
 * Ok button
 */
	Button okB;
/**
 * No button
 */
	Button noB;
/**
 * Cancel button
 */
	Button cancelB;
/**
 * Message label
 */
	Label questionL;
/**
 * Answer pressed
 */
	int answer;
/**
 * Ok constant
 */
	static final int OK = 1;
/**
 * No constant
 */
	static final int NO = 2;
/**
 * Cancel constant
 */
	static final int CANCEL = 0;
	
/**
 * Creates new OkNoCancelDialog with specified parent window and question
 * @param parent parent window
 * @param question question to be displayed
 */
	OkNoCancelDialog(Frame parent, String question) {
		super(parent, " ",true);
		init(question, true);
	}
	
/**
 * Creates new OkNoCancelDialog with specified parent window and question
 * @param parent parent window
 * @param question question to be displayed
 * @param okb true if dialog should have a No button, otherwise false
 */
	OkNoCancelDialog(Frame parent, String question, boolean okb) {
		super(parent, " ",true);
		init(question, okb);
	}
	
	/**
	 * inits the componenets
	 * @param nob true if the No button should be visible, otherwise false
	 */
	private void init(String question, boolean nob) {
		questionL = new Label(question, Label.CENTER);
		setLayout(new FlowLayout());
// 		Point pp = parent.getLocationOnScreen();
		Point pp = getParent().getLocationOnScreen();
		setBounds(pp.x + 100, pp.y + 100,200,120);
// 		setSize(200,120);
// 		setBackground(Color.white);
		Panel qPanel = new Panel();
		qPanel.setSize(200,100);
		qPanel.add(questionL);
		add(qPanel);
		Panel bPanel = new Panel();
		bPanel.setSize(200, 50);
		okB = new Button("OK");
		okB.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer = OK;
				OkNoCancelDialog.this.dispose();
			}
		});
		bPanel.add(okB);
		if (nob) {
			noB = new Button("No");
			noB.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					answer = NO;
					OkNoCancelDialog.this.dispose();
				}
			});
			bPanel.add(noB);
		}

		cancelB = new Button("Cancel");
		cancelB.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer = CANCEL;
				OkNoCancelDialog.this.dispose();
			}
		});
		bPanel.add(cancelB);
		add(bPanel);
		
	}
	
/**
 * Gets the answer pressed
 * @return Either OkNoCancelDialog.OK, OkNoCancelDialog.NO, or OkNoCancelDialog.CANCEL
 */
	public int getAnswer(){
		return answer;
	}
}
