/*
 * $Revision: 1.1 $
 * $Id: EntryGadget.java,v 1.1 2003/04/04 10:14:13 niclas Exp $
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
 * A Panel for Main Window to hold a button, a text field, and a label for adding 
 * sequence entries to Alfresco
 * @see java.awt.Panel
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
class EntryGadget extends Panel{
/**
 * Parent window
 */
	private MainWindow parent;
/**
 * Open button
 */
	private Button button ;
/**
 * File name text field
 */
	private TextField tf ;
/**
 * Entry description label
 */
	private Label label ;		// for the descriptor
/**
 * Notified when Entry should be updated
 */
	private EntryUpdater eu ;

/**
 * Creates EntryGadget with specified parent window
 * @param parent parent window
 * @param buttonName button label
 * @param eu Object implementing EntryUpdater interface
 */
	EntryGadget(MainWindow parent, String buttonName, EntryUpdater eu) {
		super();
		this.parent = parent;
		this.eu = eu ;
		
		Dimension pdim = parent.dimension;
//		System.out.println(pdim);
//		setSize(pdim.width-20, 40);
		setSize(780, 30);
		setLayout(new FlowLayout(FlowLayout.LEFT));
//		setLayout(new GridLayout());
//		System.out.println(this);
		// make button, tf, label
		button = new Button(buttonName);
//		button.setSize(120, 30);
		button.setBounds(0,5,120,30);
		button.addActionListener( new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				buttonAction();
			}
		});
		add(button);
		
		tf = new TextField(20);
		tf.addActionListener( new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				textFieldAction();
			}
		});
		tf.setBounds(new Rectangle(new Point(130,5), tf.getMinimumSize()));
		add(tf);
		
		// label to hold id line
		// note stupidly long String of spaces to make show names properly
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 150; i++) {
			sb.append(" ");
		}
		String labelString = new String(sb);
		label = new Label(labelString);
		label.setSize(400, 30);
		label.setBounds(380,5,400, 30);
		add(label);
		
	}
	
/**
 * Opens dialog and asks for filename. Calls openEntry() to make Entry. 
 * Invoked by pressing the open button
 */
	void buttonAction () { // make an entry and call eu.update on it
		FileDialog fd = new FileDialog(parent, "Open Sequence File", FileDialog.LOAD);
		fd.show();
		String path = fd.getDirectory() + fd.getFile();
		openEntry( path );
	}
	
/**
 * Calls openEntry() with text in text field to make Entry. 
 * Invoked by typing return in textfield
 */
	void textFieldAction () {
		openEntry( tf.getText() );
	}
	
/**
 * Creates new Entry and calls EntryUpdater.update()
 * @param filename sequence file name
 */
	private void openEntry(String filename) {
		if (filename.equals("") ) { return; }
		File f = null;
		int sepindex = filename.lastIndexOf(File.separatorChar);
		if (sepindex != -1) {
			String path = filename.substring(0,sepindex);
			if (!path.startsWith("/")){
				path = SystemConstants.USRDIR + File.separator + path;
			}
			String name = filename.substring(sepindex+1, filename.length());
			f = new File(path, name);
		} else {
			f = new File(SystemConstants.USRDIR, filename);
		}
// 		if (!f.exists() ) { parent.status.setText(filename + ": no such file"); return;}
		Entry ent = null;
		try {
		ent = new Entry(f);
		} catch (FileNotFoundException fnf) { parent.status.setText(filename + ": no such file"); return;}
		if (ent != null) {
			eu.update(ent);
		}
		parent.status.setText("OK");
	}
	
/**
 * Updates textfield and label
 * @param e Entry
 */
	void update (Entry e) {
    System.out.println("EntryGadget.update(), Batch: " + parent.wc.batch);
    if (!parent.wc.batch) { // Checking if this really has to be done. Kind of ugly.
		  tf.setText (e.getFilename()) ;
		  System.out.println("Comment label will be set to: "+e.getComment());
		  label.setText(e.getComment()) ;
		  label.setVisible(true);
		  label.addNotify();
    }
	}
	
/**
 * Resets textfield and label
 */
	void reset() {
		tf.setText("");
		label.setText("");
	}
	
	/**
	 * Hides button and text field
	 */
	public void hideButtonAndTextField() {
		button.setVisible(false);
		tf.setVisible(false);
		label.setSize(600, 30);
	}
}
