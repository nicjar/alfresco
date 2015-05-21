/*
* $Revision: 1.1 $
* $Id: DotterFile.java,v 1.1 2003/04/04 10:14:08 niclas Exp $
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
import java.io.*;
import java.util.*;

/**
 * DotterFile stores the result from dotter batch file.<p>
 * @see java.io.File
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class DotterFile extends File {
/**
 * Dotter file format
 */
	byte fileformat;
/**
 * Zoomfactor of dotter file
 */
	int zoomfactor;
/**
 * Horizontal length of result matrix
 */
	int horizontal_len;
/**
 * Vertical length of result matrix
 */
	int vertical_len;
/**
 * Scaling factor (not used)
 */
	int pixel_factor;
/**
 * Sliding window length (not used)
 */
	int window_length;
/**
 * Length of score matrix name (not used)
 */
	int score_matrix_name_length;
/**
 * Name of score matrix (not used)
 */
	byte[] score_matrix_name;
/**
 * Score matrix [24][24] (not used)
 */
	int[][] score_matrix = new int[24][24];
/**
 * store the result matrix [vertical_len][horizontal_len]
 */
	int[][] matrix;
/**
 * Array of Vectors with DotterPoints indexed on score
 */
	transient Vector[] scores;
/**
 * To specify vertical sequence of the matrix
 */
	static final int VERTICAL = 0;
/**
 * To specify horizontal sequence of the matrix
 */
	static final int HORIZONTAL = 1;
/**
 * Generates SeqRanges
 */
	transient SeqRangeFactory srf;
	
/**
 * Creates new DotterFile and calls dotter with two sequence files.<p> 
 * If zoom == 0 use default zoomfactor
 * @param seqf1 first sequence file
 * @param seqf2 second sequence file
 * @param outfn name of dotter output file
 * @param zoom zoomfactor
 * @param srf SeqRange generator
 */
	DotterFile (String seqf1, String seqf2, String outfn, int zoom, SeqRangeFactory srf) {
// 		String outfn = seqf1 + "-" + seqf2 + ".dotter";
		super(SystemConstants.TMPDIR, outfn);
		this.srf = srf;
// 		System.out.println("The file " + outfn + (this.exists()?" does":" does not") + " exist.");
// 		if (!this.exists() ) {
			String cmnd = "";
			if (zoom == 0 ) {
				cmnd = SystemConstants.DOTTERPATH + "dotter -b " + this.getAbsolutePath() + " " + seqf1 + " " + seqf2;
			} else {
				cmnd = SystemConstants.DOTTERPATH + "dotter -z " + zoom + " -b " + this.getAbsolutePath() + " " + seqf1 + " " + seqf2;
			}
			Runtime rt = Runtime.getRuntime();
			Process dps = null;
			long start = System.currentTimeMillis();
			try {
				System.out.println("Calling " + cmnd);
				dps = rt.exec(cmnd);
				InputStream stderr = dps.getErrorStream();
				dps.waitFor();
				InputStreamReader stderrr = new InputStreamReader(stderr);
				BufferedReader stderrbr = new BufferedReader(stderrr);
				String errline = stderrbr.readLine();
				while (errline != null) {
					System.out.println(errline);
					errline = stderrbr.readLine();
				}
				
			} catch (InterruptedException ie) { System.out.println("dotter interupted:\n" + ie); }
			catch (IOException ioe) { System.out.println(ioe); }
			long stop = System.currentTimeMillis();
			System.out.println("Run time: "+(stop-start)+" milliseconds");
// 		}
	}
	
	
/**
 * Constructor for using existing dotter output file
 * @param dottfile name of dotter output file
 */
	public DotterFile (String dottfile) {
		super(dottfile);
		srf = new SeqRangeFactory();
	}
	
/**
 * Constructor for using existing dotter output file
 * @param path directory path
 * @param dottfile name of dotter output file
 */
	DotterFile (String path, String dottfile) {
		super(path, dottfile);
	}

/**
 * Reads the dotter output file and parses the result into the results array
 */
	public void open() {
// 		int shift = 0;
// 		if ("Linux".equals(System.getProperty("os.name"))) {
// 			shift = 24; // for linux
// 		}
		FileInputStream fis = null;
		try {
		fis = new FileInputStream(this);
		} catch (FileNotFoundException fnf) {System.out.println(fnf); }
		DataInputStream dis = new DataInputStream(fis);
		System.out.println("Reading dotter file " + this.getName() + "...");
		long start = System.currentTimeMillis();
		try {
			fileformat = dis.readByte();
			System.out.println("Read fileformat = "+fileformat);
// 			zoomfactor = dis.readInt();
			zoomfactor = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte());
			System.out.println("Read zoomfactor = "+zoomfactor);
// 			horizontal_len = dis.readInt();
			horizontal_len = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte());
			System.out.println("Read horizontal_len = "+horizontal_len);
// 			vertical_len = dis.readInt(); 
			vertical_len = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte()); 
			System.out.println("Read vertical_len = "+vertical_len);
// 			pixel_factor = dis.readInt();
			pixel_factor = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte());
			System.out.println("Read pixel_factor = "+pixel_factor);
// 			window_length = dis.readInt();
			window_length = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte());
			System.out.println("Read window_length = "+window_length);
// 			score_matrix_name_length = dis.readInt();
			score_matrix_name_length = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte());
			System.out.println("Read score_matrix_name_length = "+ score_matrix_name_length);
			score_matrix_name = new byte[score_matrix_name_length];
			for (int i=0; i<score_matrix_name_length; i++) {
				score_matrix_name[i] = dis.readByte();
			}
			System.out.println("Read score_matrix_name = "+score_matrix_name);
			for (int i=0;i<24;i++) {
				for (int j=0;j<24;j++) {
					score_matrix[i][j] = dis.readInt();
// 					score_matrix[i][j] = getInt(dis.readByte(), dis.readByte(), dis.readByte(), dis.readByte());
				}
			}
			System.out.println("Read score_matrix");
			matrix = new int[vertical_len][horizontal_len];
			scores = new Vector[255];
			for (int i=0; i<vertical_len;i++) {
				for (int j=0; j<horizontal_len;j++) {
					int val = (int) dis.readByte();
					if (val < 0) { val += 256; }
					matrix[i][j] = val;
					if (val != 0) {
						SeqRange hr = srf.getSeqRange((j*zoomfactor)+1, j*zoomfactor + (zoomfactor));
						SeqRange vr = srf.getSeqRange((i*zoomfactor)+1, i*zoomfactor + (zoomfactor));
						DotterPoint dp = new DotterPoint(hr, vr, val);
						if (scores[val-1] == null) { scores[val-1] = new Vector(); }
						scores[val-1].addElement(dp);	
					}
				}
			}
			System.out.println("Read result_matrix, "+(vertical_len*horizontal_len)+" points");
		} catch (IOException ioe) {System.out.println(ioe);}
		long stop = System.currentTimeMillis();
		System.out.println("Run time: "+(stop-start)+" millisecs");
	}
	
	/**
	 * checks the byteorder and returns the reasonable value
	 * @return int
	 * @param b1 first byte
	 * @param b2 second byte
	 * @param b3 third byte
	 * @param b4 fourth byte
	 */
	private int getInt(byte b1, byte b2, byte b3, byte b4) {
		int x1 = (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
		int x2 = b1 + (b2 << 8) + (b3 << 16) + (b4 << 24);
		System.out.println("x1: " + x1 + ", x2: " + x2);
		if (x1 < 1000000) {
			return x1;
		} else {
			return x2;
		}
	}

/**
 * Gets a Vector of DotterPoint objects with score above cutoff that match range, excluding
 * ranges specified.
 * @return	Vector of DotterPoints
 * @param range The SeqRange to find matches to.
 * @param cutoff The score cutoff.
 * @param exclude A Vector of SeqRange objects to be excluded from the return Vector
 * @param direction The direction of the sequence that range belongs to, either HORIZONTAL or VERTICAL.
 */
	public Vector getMatches(SeqRange range, int cutoff, Vector exclude, int direction) {	
		Vector resultVect = new Vector();
		int middle = range.getStart() + (range.getStop()-range.getStart())/2;
		int pos = (middle-1)/zoomfactor;
		int maxlen=0;
		if (direction == HORIZONTAL) {
			maxlen = vertical_len;
		} else if (direction == VERTICAL) {
			maxlen = horizontal_len;
		} else {
			System.out.println("Bad direction value:"+ direction+", only 0 or 1 allowed");
			System.out.println("It's been a bad week, hasn't it? Goodbye.");
			System.exit(1);
		}
		for (int i = 0; i<maxlen; i++) {
			SeqRange posRange = new SeqRange((i*zoomfactor)+1, i*zoomfactor + zoomfactor);
			if (!findOverlap(posRange, exclude)) {
				int value = 0;
				if (direction == HORIZONTAL) {
					value = matrix[i][pos];
					if (value >= cutoff) {
						resultVect.addElement( new DotterPoint(range, posRange, value) );
					}
				} else if(direction == VERTICAL) {
					value = matrix[pos][i];
					if (value >= cutoff) {
						resultVect.addElement( new DotterPoint(posRange, range, value) );
					}
				} 
			}
		}
		return resultVect;
	}
	
/**
 * Gets a Vector of DotterPoint objects with score above cutoff that match range, without 
 * excluding any ranges
 * @return	Vector of DotterPoints
 * @param range The SeqRange to find matches to.
 * @param cutoff The score cutoff.
 * @param direction The direction of the sequence that range belongs to, either HORIZONTAL or VERTICAL.
 */
	public Vector getMatches(SeqRange range, int cutoff, int direction) {	
		Vector resultVect = new Vector();
		int middle = range.getStart() + (range.getStop()-range.getStart())/2;
		int pos = (middle-1)/zoomfactor;
		int maxlen=0;
		if (direction == HORIZONTAL) {
			maxlen = vertical_len;
		} else if (direction == VERTICAL) {
			maxlen = horizontal_len;
		} else {
			System.out.println("Bad direction value:"+ direction+", only 0 or 1 allowed");
			System.out.println("It's been a bad week, hasn't it? Goodbye.");
			System.exit(1);
		}
		for (int i = 0; i<maxlen; i++) {
			SeqRange posRange = new SeqRange((i*zoomfactor)+1, i*zoomfactor + zoomfactor);
// 			if (!findOverlap(posRange, exclude)) {
				int value = 0;
				if (direction == HORIZONTAL) {
					value = matrix[i][pos];
					if (value >= cutoff) {
						resultVect.addElement( new DotterPoint(range, posRange, value) );
					}
				} else if(direction == VERTICAL) {
					value = matrix[pos][i];
					if (value >= cutoff) {
						resultVect.addElement( new DotterPoint(posRange, range, value) );
					}
				} 
// 			}
		}
		return resultVect;
	}

/**
 * Gets a vector with DotterPoint objects above cutoff.
 * @return Vector of DotterPoints
 * @param cutoff The score cutoff.
 * @param horizExcl A vector of SeqRange objects to exclude if present in horizontal sequence
 * @param vertExcl A vector of SeqRange objects to exclude if present in vertical sequence
 * @param excludeSelf wheter to exclude identical ranges or not
 */
	public Vector aboveCutoff(int cutoff, Vector horizExcl, Vector vertExcl, boolean excludeSelf) {
		Vector resultVect = new Vector();
		for (int i = cutoff-1; i < 255; i++) {
			if (scores[i] != null) {
				Enumeration pointsEnum = scores[i].elements();
				SCORE: while (pointsEnum.hasMoreElements()) {
					DotterPoint dp = (DotterPoint) pointsEnum.nextElement();
					SeqRange hor = dp.getHorizontal();
					SeqRange ver = dp.getVertical();
					if(excludeSelf && hor.isEqual(ver)) { continue SCORE; }
					if (!findOverlap(hor, horizExcl) && !findOverlap(ver, vertExcl)) {
						resultVect.addElement(dp);
// 						System.out.println("Found point, " + dp);
					}
				}
			}
		} 
		return resultVect;
	}

/**
 * Finds DotterPoints above a certain cutoff
 * @return Vector of DotterPoints
 * @param cutoff Cutoff value. Score >= cutoff
 * @param excludeSelf wheter to exclude identical ranges or not
 */
	public Vector aboveCutoff(int cutoff, boolean excludeSelf) {
		Vector resultVect = new Vector();
		for (int i = cutoff-1; i < 255; i++) {
			if (scores[i] != null) {
				Enumeration pointsEnum = scores[i].elements();
				SCORE: while (pointsEnum.hasMoreElements()) {
					DotterPoint dp = (DotterPoint) pointsEnum.nextElement();
					SeqRange hor = dp.getHorizontal();
					SeqRange ver = dp.getVertical();
					if(excludeSelf && hor.isEqual(ver)) { continue SCORE; }
// 					if (!findOverlap(hor, horizExcl) && !findOverlap(ver, vertExcl)) {
						resultVect.addElement(dp);
// 						System.out.println("Found point, " + dp);
// 					}
				}
			}
		} 
		return resultVect;
	}
	
/**
 * Determines if a range overlaps any range in the set of ranges to be excluded
 * @return true if range overlaps, otherwise false
 * @param range range to be checked
 * @param exclude Vector of ranges to be excluded
 */
	private boolean findOverlap(SeqRange range, Vector exclude) {
		Enumeration exclEnum = exclude.elements();
		while (exclEnum.hasMoreElements()) {
			SeqRange exclRange = (SeqRange) exclEnum.nextElement();
			if (range.overlap(exclRange)) {return true;}
		}
		return false;
	}
	

	
// 	void print() {
// // 		System.out.println("fileformat = "+ (int) fileformat);
// // 		System.out.println("zoomfactor = "+ zoomfactor);
// // 		System.out.println("horizontal_len = "+ horizontal_len);
// // 		System.out.println("vertical_len = "+ vertical_len);
// // 		System.out.println("pixel_factor = "+ pixel_factor);
// // 		System.out.println("window_length = "+ window_length);
// // 		System.out.println("score_matrix_name_length = "+ score_matrix_name_length);
// // 		System.out.print("score_matrix_name = ");
// // 		for (int i=0;i<score_matrix_name_length;i++) {
// // 			System.out.print( (char) score_matrix_name[i] );
// // 		}
// // 		System.out.println();
// // 		System.out.println("score_matrix");
// // 		for (int i=0;i<24;i++) {
// // 			for (int j=0;j<24;j++) {
// // 				System.out.print(score_matrix[i][j] + " ");
// // 			}
// // 			System.out.print("\n");
// // 		}
// 		System.out.println("result_matrix");
// 		for (int i=0; i<vertical_len;i++) {
// 			for (int j=0; j<horizontal_len;j++) {
// 				System.out.print( matrix[i][j] + " ");
// 			}
// 			System.out.print( "\n" );
// 		}
// 	}
	
/**
 * Populates the scores array after loading object from file.
 * @param srf SeqRange generator
 */
	void populateScores(SeqRangeFactory srf) {
		this.srf = srf;
		scores = new Vector[255];
		for (int i=0; i<vertical_len;i++) {
			for (int j=0; j<horizontal_len;j++) {
				int val = matrix[i][j];
				if (val != 0) {
					SeqRange hr = srf.getSeqRange((j*zoomfactor)+1, j*zoomfactor + (zoomfactor));
					SeqRange vr = srf.getSeqRange((i*zoomfactor)+1, i*zoomfactor + (zoomfactor));
					DotterPoint dp = new DotterPoint(hr, vr, val);
					if (scores[val-1] == null) { scores[val-1] = new Vector(); }
					scores[val-1].addElement(dp);	
				}
			}
		}
		
	}
	
/**
 * Saves DotterFile object to a file
 * @param filename name of file
 */
	public void save(String filename) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.flush();
		oos.close();
	}
	
/**
 * Reads a DotterFile object from a file
 * @return a DotterFile object
 * @param filename name of file
 */
	public static DotterFile load(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream ois = new ObjectInputStream(fis);
		DotterFile tmpdott = (DotterFile) ois.readObject();
		ois.close();
		return tmpdott;
	}
	
	/**
	 * Gets result matrix
	 * @return result matrix
	 */
	public int[][] getMatrix() {
		return matrix;
	}
	
	/**
	 * Gets vertical length
	 * @return result vertical length
	 */
	public int getVLen() {
		return vertical_len;
	}
	
	/**
	 * Gets horizontal length
	 * @return result horizontal length
	 */
	public int getHLen() {
		return horizontal_len;
	}
	
	public static void main (String[] arg) {
		String file1 = arg[0];
		String file2 = arg[1];
		int cutoff = Integer.parseInt(arg[2]);
		SeqRangeFactory srf = new SeqRangeFactory();
		String outf = file1 + "-" + file2 + ".dotter";
		DotterFile df = new DotterFile(file1, file2, outf, 75, srf);
		df.open();
// 		for (int i = cutoff-1; i < 255; i++) {
// // 			System.out.println("i = " + i);
// 			if (df.scores[i] != null) {
// 				System.out.println(df.scores[i]);
// 			}
// 		}
		int sum = 0;
		int total = df.horizontal_len*df.vertical_len;
		for (int i = 0; i < 255; i++) {
 			if (df.scores[i] != null) {
 				int n = df.scores[i].size();
 				sum += n;
				System.out.println((i+1) + ": " + n + " " + (int)(100*((double)n/(double)total)) + "%");
			}
		}
		System.out.println("Sum: " + sum + ", 0: " + (total - sum) );
// 		df.print();
	}
}


