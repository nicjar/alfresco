/*
 * $Revision: 1.1 $
 * $Id: ErrorDialog.java,v 1.1 2003/04/04 10:14:16 niclas Exp $
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
 * A dialog window for displaying error messages
 * @see java.awt.Dialog
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class ErrorDialog extends Dialog {
/**
 * Dismiss button
 */
	Button dismissB;
/**
 * Message label
 */
	Label messageL;
	
	/**
	 * Creates a new ErrorDialog with the specified parent window and message
	 * @param parent parent window
	 * @param message error message
	 */
	public ErrorDialog(Frame parent, String message) {
		super(parent, "Error", true);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 2;
		setLayout(gbl);
		
		messageL = new Label(message, Label.CENTER);
		gbl.setConstraints(messageL, gbc);
		add(messageL);
		Dimension d = messageL.getSize();
		Point pp = parent.getLocationOnScreen();
		setBounds(pp.x + 50, pp.y + 50, d.width + 20, 150);

		dismissB = new Button("Dismiss");
		gbc.gridheight = 1;
		gbl.setConstraints(dismissB, gbc);
		dismissB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(dismissB);
		pack();
	}
} 
