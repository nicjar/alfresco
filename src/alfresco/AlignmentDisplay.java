/*
 * $Revision: 1.1 $
 * $Id: AlignmentDisplay.java,v 1.1 2003/04/04 10:13:45 niclas Exp $
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
import java.text.*;
/**
 * Frame for displaying pairwise sequence alignment
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class AlignmentDisplay extends Frame {
/**
 * Vector of AlignmentBlock objects
 */
	Vector blocks;
/**
 * Scroll pane holding the Canvas
 */
	ScrollPane seqPane;
/**
 * Canvas for drawing the sequence alignment on
 */
	Canvas seqC;
/**
 * Save button
 */
	Button saveB;
/**
 * Reciprocal button
 */
	Button recB;
/**
 * Close button
 */
	Button closeB;
/**
 * Image for storing drawn alignments
 */
	Image alImage;
// /**
//  * SeqStringFormat object for formatting of sequence 1
//  */
// 	SeqStringFormat formatSeq1;
// /**
//  * SeqStringFormat object for formatting of sequence 2
//  */
// 	SeqStringFormat formatSeq2;
/**
 * Formated string of sequence 1
 */
	String fSeq1;
/**
 * Formated string of sequence 2
 */
	String fSeq2;
/**
 * Formated match string
 */
	String fMatch;
/**
 * Name of source Entry filename for sequence 1
 */
	String filename1 = null;
/**
 * Name of source Entry filename for sequence 2
 */
	String filename2 = null;
/**
 * Display font
 */
	Font seqFont;
/**
 * Font height
 */
	int fontH;
/**
 * Canvas width
 */
	int seqCW;
/**
 * Canvas height
 */
	int seqCH;
/**
 * Creates a AlignmentDisplay with the alignments in the Vector
 * @param blocks vector of alignment blocks
 */
	AlignmentDisplay(Vector blocks) {
		super("Alignments");
		this.blocks = blocks;
		setLayout(new FlowLayout());
		seqFont = new Font("Monospaced", Font.PLAIN, 12);
// 		seqFont = new Font("Courier", Font.PLAIN, 12);
// 		System.out.println(seqFont);
		seqPane = new ScrollPane();
		seqC = new Canvas(){
			public void paint(Graphics g){
				drawAl(g);
			}
		};
		FontMetrics fm = seqC.getFontMetrics(seqFont);
		fontH = fm.getHeight();
		int fontW = fm.charWidth('A');
// 		System.out.println(fontW);
		seqCW = 67 * fontW;
		int rows = 0;
		Enumeration blen = blocks.elements();
		while( blen.hasMoreElements()) {
			AlignmentBlock bl = (AlignmentBlock) blen.nextElement();
// 			System.out.println("Getting block: " + bl);
			rows += 5 * (1 + bl.getLength()/50);
		}
		rows += 10; // some extra space
		seqCH = rows * fontH + rows * 5;
		if (seqCH < 260) seqCH = 260;
// 		System.out.println("rows: " + rows + ", seqCH: " + seqCH);
		seqPane.setSize(seqCW + 10, 260);
		seqC.setSize(seqCW,seqCH);
		seqC.setBackground(Color.white);
		seqPane.add(seqC);
		add(seqPane);

// 		setBounds(300,300, 510, 330);
		setBounds(300,300, seqCW + 30, 330);
		saveB = new Button("Save to file");
		saveB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		add(saveB);
		
		recB = new Button("Make Reciprocals");
		recB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeReciprocals();
			}
		});
		add(recB);
		
		
		closeB = new Button("Close");
		closeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(closeB);
		
	}

	/**
	 * Draws the alignments to the Graphics specified
	 * @param g Graphics to draw to
	 */
	public void drawAl(Graphics g) {
		if(alImage == null) {
			alImage = seqC.createImage(seqCW,seqCH);
			Graphics ig = alImage.getGraphics();
			ig.setFont(seqFont);
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			Enumeration blen = blocks.elements();
			int row = 1;
			while( blen.hasMoreElements()) {
				AlignmentBlock bl = (AlignmentBlock) blen.nextElement();
				String fn1 = bl.getEntry1().getFilename();
				String fn2 = bl.getEntry2().getFilename();
				if (filename1 == null) {
					filename1 = fn1;
					filename2 = fn2;
				}
				StringBuffer sb = new StringBuffer();
				sb.append(fn1 + " x ");
				if(bl.getRange2().isComplement()) { sb.append("REV-COMPL "); }
				sb.append(fn2);
				ig.drawString(new String(sb), 10, 5 + fontH * row++);
				sb = new StringBuffer();
				sb.append("Length: " + bl.getLength() + ", ");
				sb.append("Ident: " + bl.getIdentity() + "%, ");
				sb.append("Gaps: " + bl.getGaps() + ", ");
				sb.append("Score: " + nf.format(bl.getScore()));
				int level = bl.getLevel();
// 				System.out.println("Level: " + level);
				if (level > 0) {
					sb.append(", Level: " + level);
				}
// 				java.math.BigDecimal sbd = new java.math.BigDecimal(bl.getScore());
// 				sbd = sbd.setScale(2, java.math.BigDecimal.ROUND_HALF_EVEN);
// 				System.out.println(sbd);
// 				sb.append("Score: " + sbd.toString());
				ig.drawString(new String(sb), 10, 5 + fontH * row++);
				fSeq1 = new SeqStringFormat(bl.getSeq1(), bl.getRange1()).getFormatSeq();
				fSeq2 = new SeqStringFormat(bl.getSeq2(), bl.getRange2()).getFormatSeq();
				fMatch = formatMatchString(bl.getMatchString());
				int i1 = fSeq1.indexOf("\n");
				int i2 = fSeq2.indexOf("\n");
				int iM = fMatch.indexOf("\n");
				int prev1 = 0;
				int prev2 = 0;
				int prevM = 0;
				while(i1 >= 0) {
					ig.drawString(fSeq1.substring(prev1, i1), 10, 5 + fontH * row);
// 					prev1 = i1;
					prev1 = i1 + 1;
					i1 = fSeq1.indexOf("\n", i1+1);
						ig.drawString(fMatch.substring(prevM, iM), 10, 5 + fontH * (row+1));
// 						prevM = iM;
						prevM = iM + 1;
						iM = fMatch.indexOf("\n", iM+1);
					ig.drawString(fSeq2.substring(prev2, i2), 10, 5 + fontH * (row+2));
// 					prev2 = i2;
					prev2 = i2 + 1;
					i2 = fSeq2.indexOf("\n", i2+1);
					row += 4;
				}
			}
			ig.dispose();
		}
		g.drawImage(alImage, 0, 0, seqC);		
	}
		
	/**
	 * Formats the match string for display
	 * @return formated String
	 * @param m match string to be formated
	 */
	public String formatMatchString(String m) {
		char[] arr = m.toCharArray();
 		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if((i+1) % 50 == 0) {
				sb.append("\n       ");
			}
// 			if((i+1) == arr.length) {
// 				int rem = 50 - ((i+1) % 50);
// 				for(int j = 0; j < rem; j++) {
// 					sb.append(" ");
// 				}
// 				sb.append("      ");
// 			}
		}
		sb.insert(0, "       ");
		sb.append("       \n");
		return new String(sb);
	}
	/**
	 * Opens FileDialog and saves alignment to file
	 */
	public void save() {
		FileDialog fd = new FileDialog(this, "Save alignment", FileDialog.SAVE);
		fd.setFile(filename1 + "-" + filename2 + ".al");
		fd.show();
		String fn = fd.getFile();
		if (fn == null) { return; }
		File outf = new File(fd.getDirectory(), fn);
		FileWriter fw = null;
		try {
			fw = new FileWriter(outf);
			fw.write("\ndba alignment of " + filename1 + " x " + filename2 + "\n\n");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			Enumeration blen = blocks.elements();
			while( blen.hasMoreElements()) {
				AlignmentBlock bl = (AlignmentBlock) blen.nextElement();
				StringBuffer sb = new StringBuffer();
				sb.append("Length: " + bl.getLength() + ", ");
				sb.append("Ident: " + bl.getIdentity() + "%, ");
				sb.append("Gaps: " + bl.getGaps() + ", ");
				sb.append("Score: " + nf.format(bl.getScore()));
// 				java.math.BigDecimal sbd = new java.math.BigDecimal(bl.getScore());
// 				sbd = sbd.setScale(2, java.math.BigDecimal.ROUND_HALF_EVEN);
// // 				System.out.println(sbd);
// 				sb.append("Score: " + sbd.toString());
				
				fw.write(new String(sb) + "\n");
				fSeq1 = new SeqStringFormat(bl.getSeq1(), bl.getRange1()).getFormatSeq();
				char[] fSeqArr1 = fSeq1.toCharArray();
				fSeq2 = new SeqStringFormat(bl.getSeq2(), bl.getRange2()).getFormatSeq();
				char[] fSeqArr2 = fSeq2.toCharArray();
				fMatch = formatMatchString(bl.getMatchString());
				char[] fSeqArrM = fMatch.toCharArray();
				sb = new StringBuffer();
				int i = 0;
				int j = 0;
				int k = 0;
				for (; i < fSeqArr1.length; i++) {
					char c = fSeqArr1[i];
					sb.append(c);
					if (c == '\n') {
// 						if(j != 0) sb.append(' ');
						for (; fSeqArrM[j] != '\n'; j++) {
							sb.append(fSeqArrM[j]);
						}
						sb.append('\n');
						j++;
						for (; fSeqArr2[k] != '\n'; k++) {
							sb.append(fSeqArr2[k]);
						}
						sb.append("\n\n");
						k++;
					}
				}
				fw.write(new String(sb));
				fw.write("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException ioe) { ioe.printStackTrace(); }
		
	}
	
	/**
	 * Makes Features and Reciprocals of the alignment blocks, and adds them to 
	 * the entries.
	 */	
	public void makeReciprocals() {
		Enumeration blen = blocks.elements();
		while (blen.hasMoreElements()) {
			AlignmentBlock bl = (AlignmentBlock) blen.nextElement();
// 			DbaBlock block1 = new DbaBlock(bl.getEntry1(), bl.getRange1());
			HCR block1 = new HCR(bl.getEntry1(), bl.getRange1(), bl.getLevel());
			block1.setBlock(bl);
// 			DbaBlock block2 = new DbaBlock(bl.getEntry2(), bl.getRange2());
			HCR block2 = new HCR(bl.getEntry2(), bl.getRange2(), bl.getLevel());
			block2.setBlock(bl);
			bl.getEntry1().addChild(block1);
			bl.getEntry2().addChild(block2);
			Reciprocal rec = new Reciprocal(block1, block2);
			EntryPair entp = (EntryPair) bl.getEntry1().getParent();
			entp.addReciprocal(rec); 
			entp.getApplication().drawEntries();
		}
	}
	
// /**
//  * For testing
//  */
// 	public static void main(String[] arg) {
// 		String fname = "MMIL10Z";
// 		File f = new File(fname);
// 		Entry ent = null;
// 		try {
// 			ent = new Entry(f);
// 		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
// 		SequenceDisplay seqD = new SequenceDisplay(ent, new SeqRange(1234,2345));
// 		seqD.show();
// // 		seqD.drawSeq();
// 	}
}
