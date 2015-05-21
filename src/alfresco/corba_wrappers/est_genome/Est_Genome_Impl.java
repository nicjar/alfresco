/*
* $Revision: 1.1 $
* $Id: Est_Genome_Impl.java,v 1.1 2003/04/04 11:39:15 niclas Exp $
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
package alfresco.corba_wrappers.est_genome;
import java.io.*;
import java.util.*;
import org.omg.CORBA.*;
import alfresco.*;
import alfresco.corba_wrappers.*;


/**
 * Implements the Est_Genome:Method interface
 * @see alfresco.corba_wrappers.cpg.Est_Genome
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class Est_Genome_Impl extends _Est_GenomeImplBase implements alfresco.corba_wrappers.Method, TmpDirWriter {

private String gSeq;
private String gSeqName;
private String eSeq;
private String eSeqName;
private GffDataStruct data;
private File tmpDir;
private Properties prop;

/**
 * Constructor
 */ 
  public Est_Genome_Impl() {
    String [] supl = { "" };
     data = new GffDataStruct("", "", supl);
  }
/**
 * Sets temporary directory for writing files
 * @param td temporary directory file object
 */
  public void setTmpDir(File td) {
    tmpDir = td;
  }
/**
 * Sets property object for method
 * @param prop Properties object
 */
  public void setProperties(Properties prop) {
    this.prop = prop;    
  }
/**
 * Takes the input necessary for running the method
 * @param seq the DNA sequence
 * @param name the name of sequence
 */
  public void input(InputStruct input) {
    this.gSeq = input.seqs[0];
    this.gSeqName = input.names[0];
    this.eSeq = input.seqs[1];
    this.eSeqName = input.names[1];
  }
/**
 * Runs the analysis. 
 */
  public void run() {
    // Do the dirty stuff !!
    if (tmpDir == null) {
      System.out.println("alfresco.corba_wrappers.est_genome.Est_Genome_Impl: tmpDir not set!");
    }
    FastaFile gFile = new FastaFile(tmpDir.getPath(), gSeqName, ">" + gSeqName, gSeq);
    gFile.write();
    FastaFile eFile = new FastaFile(tmpDir.getPath(), eSeqName, ">" + eSeqName, eSeq);
    eFile.write();
    alfresco.Est_Genome egr = new alfresco.Est_Genome(gFile, eFile, prop);
    egr.start();
    try {
      egr.join();
      data.gff = egr.getGff();
    } catch (Exception e) { e.printStackTrace(); }
    
  }
/**
 * Gets the resultant data
 * @return gff data structure
 */
  public GffDataStruct getGffData() throws alfresco.corba_wrappers.NoOutputException{
    if (data == null) { 
      throw new alfresco.corba_wrappers.NoOutputException("Est_Genome produced no output"); 
    }
    return data;
  }
}
