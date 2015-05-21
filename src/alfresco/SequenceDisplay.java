/*
 * $Revision: 1.1 $
 * $Id: SequenceDisplay.java,v 1.1 2003/04/04 10:15:09 niclas Exp $
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
 * Frame for displaying sequence
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class SequenceDisplay extends Frame {
/**
 * Source Entry of the sequence to be displayed
 */
	Entry entry;
/**
 * The range defining the sequence
 */
	SeqRange range;
/**
 * Scroll pane holding the Canvas
 */
	ScrollPane seqPane;
/**
 * Canvas for drawing the sequence on
 */
	Canvas seqC;
/**
 * Format save button
 */
	Button formatSaveB;
/**
 * Fasta save button
 */
	Button fastaSaveB;
/**
 * Gff save button
 */
	Button gffSaveB;
/**
 * Close button
 */
	Button closeB;
/**
 * SeqStringFormat object for formatting of the sequence
 */
	SeqStringFormat formatSeq;
/**
 * Display font
 */
	Font seqFont;
/**
 * Font height
 */
	int fontH;
	
/**
 * Creates a SequenceDisplay with the sequence of the specified entry and range
 * @param ent source Entry
 * @param r sequence range
 */
	SequenceDisplay(Entry ent, SeqRange r) {
		super(ent.toString() + " " + r.toString());
		entry = ent;
		range = r;
		formatSeq = new SeqStringFormat(entry, range);
		setup();
	}
	
/**
 * Creates a SequenceDisplay with the sequence of the specified SeqFeature
 * @param sf feature
 */
	SequenceDisplay(SeqFeature sf) {
		super();
		entry = sf.getEntry();
		range = sf.getSeqRange();
		setTitle(entry.toString() + " " + range.toString());
		formatSeq = new SeqStringFormat(entry, range);
		setup();
	}
	
/**
 * Creates a SequenceDisplay with the sequence of the specified String
 * @param seq sequence String
 * @param title title of sequence
 */
	SequenceDisplay(String seq, String title) {
		super(title);
// 		entry = sf.getEntry();
// 		range = sf.getSeqRange();
// 		setTitle(entry.toString() + " " + range.toString());
		formatSeq = new SeqStringFormat(seq);
		setup();
	}
	/**
	 * setsup the window and formats sequence
	 */
	private void setup() {
		setLayout(new FlowLayout());
		seqFont = new Font("Monospaced", Font.PLAIN, 12);
		seqPane = new ScrollPane();
		seqC = new Canvas(){
			public void paint(Graphics g) {
				drawSeq(g);
			}
		};
		FontMetrics fm = seqC.getFontMetrics(seqFont);
		fontH = fm.getHeight();
		int fontW = fm.charWidth('A');
// 		System.out.println(fontW);
		int seqCW = 67 * fontW;
		int seqCH;
		if (range != null) {
			seqCH = (range.length()/50 + 2) * fontH;
		} else {
			seqCH = (formatSeq.getSeqLength()/50 + 2) * fontH;
		}
		seqPane.setSize(seqCW + 10, 260);
		seqC.setSize(seqCW,seqCH);
		seqC.setBackground(Color.white);
		seqPane.add(seqC);
		add(seqPane);

// 		setBounds(300,300, 510, 330);
		setBounds(300,300, seqCW + 30, 330);
		formatSaveB = new Button("Save to file");
		formatSaveB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formatSave();
			}
		});
		add(formatSaveB);
		fastaSaveB = new Button("Save to Fasta file");
		fastaSaveB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fastaSave();
			}
		});
		add(fastaSaveB);

		gffSaveB = new Button("Save to gff file");
		gffSaveB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gffSave();
			}
		});
		add(gffSaveB);
    
		closeB = new Button("Close");
		closeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(closeB);
		
	}
	
/**
 * Draws the sequence to the Graphics specified
 */
	public void drawSeq(Graphics g) {
		g.setFont(seqFont);
		String theSeq = formatSeq.getFormatSeq();
		int i = theSeq.indexOf("\n");
		int prev = 0;
		int row = 1;
		while (i < theSeq.length() && i >= 0) {
			g.drawString(theSeq.substring(prev, i), 10, 5 + fontH * row);
			prev = i+1;
			i = theSeq.indexOf("\n", i+1);
			row++;
// 			System.out.println("i: " + i + " prev: " + prev + " row: " + row);
		}
	}
	
	/**
	 * Saves sequence to file in same format as in SequenceDisplay
	 * Note! should be declared private but this is broken in jdk1.1.3
	 */
	public void formatSave() {
		FileDialog fd = getFileDialog();
		if (fd == null) { return; }
		File outf = new File(fd.getDirectory(), fd.getFile());
		FileWriter fw = null;
		try {
			fw = new FileWriter(outf);
			if (entry != null) {
				fw.write(entry.toString() + " " + range.toString() + "\n");
			} else {
				fw.write(this.getTitle() + "\n");
			}
			fw.write(formatSeq.getFormatSeq());
			fw.flush();
			fw.close();
		} catch (IOException ioe) { System.out.println(ioe); }
	}
	
	/**
	 * Saves sequence to file in Fasta format
	 * Note! should be declared private but this is broken in jdk1.1.3
	 */
	public void fastaSave() {
		FileDialog fd = getFileDialog();
		if (fd == null) { return; }
		FastaFile ff;
		if (entry != null) {
			ff = new FastaFile(fd.getDirectory(), fd.getFile(), entry, range);
		} else {
			ff = new FastaFile(fd.getDirectory(), fd.getFile(), ">" + this.getTitle(), formatSeq.getSeqString());
		}
		ff.write(); 
	}
	
	/**
	 * Saves relative feature locations to file in gff format
	 * Note! should be declared private but this is broken in jdk1.1.3
	 */
	public void gffSave() {
		FileDialog fd = getGffFileDialog();
		if (fd == null) { return; }
		GffFile gf = null;
		if (entry != null) {
			gf = new GffFile(fd.getDirectory(), fd.getFile(), entry, range);
// 		} else {
// 			ff = new FastaFile(fd.getDirectory(), fd.getFile(), ">" + this.getTitle(), formatSeq.getSeqString());
		}
		gf.write(); 
	}
	
	/**
	 * Opens a file dialog asking for a save filename
	 * Note! should be declared private but this is broken in jdk1.1.3
	 * @return a FileDialog instance if file name != null, otherwise null
	 */
	public FileDialog getFileDialog() {
		FileDialog fd = new FileDialog(this, "Save sequence", FileDialog.SAVE);
		if (entry != null) {
			fd.setFile(entry.getFilename() + "_" + range.getStart() + "-" + range.getStop());
		} 
		fd.show();
		if (fd.getFile() == null) { return null; }
		return fd;
	}
	
	/**
	 * Opens a file dialog asking for a save filename for gff file
	 * Note! should be declared private but this is broken in jdk1.1.3
	 * @return a FileDialog instance if file name != null, otherwise null
	 */
	public FileDialog getGffFileDialog() {
		FileDialog fd = new FileDialog(this, "Save gff", FileDialog.SAVE);
		if (entry != null) {
			fd.setFile(entry.getFilename() + "_" + range.getStart() + "-" + range.getStop() + ".gff");
		} 
		fd.show();
		if (fd.getFile() == null) { return null; }
		return fd;
	}
/**
 * For testing
 */
	public static void main(String[] arg) {
		String fname = "MMIL10Z";
		File f = new File(fname);
		Entry ent = null;
		try {
			ent = new Entry(f);
		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
		SequenceDisplay seqD = new SequenceDisplay(ent, new SeqRange(1234,2345));
		seqD.show();
// 		seqD.drawSeq();
	}
}
