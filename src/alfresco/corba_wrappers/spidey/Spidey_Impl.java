/*
* $Revision: 1.3 $
* $Id: Spidey_Impl.java,v 1.3 2003/04/08 07:15:53 niclas Exp $
 *
 * This file is part of Alfresco
 * Copyright (C) 2003  Niclas Jareborg
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
 * For more information contact Niclas at niclas.jareborg@astrazeneca.com
*/
package alfresco.corba_wrappers.spidey;
import java.io.*;
import java.util.*;
import org.omg.CORBA.*;
import alfresco.*;
import alfresco.corba_wrappers.*;


/**
 * Implements the Spidey:Method interface
 * @see alfresco.corba_wrappers.spidey.Spidey
 * @version 1
 * @author Niclas Jareborg, AstraZeneca
 */

public class Spidey_Impl extends _SpideyImplBase implements alfresco.corba_wrappers.Method, TmpDirWriter {

private String gSeq;
private String gSeqName;
private String eSeq;
private String eSeqName;
private String option;
private GffDataStruct data;
private File tmpDir;
private Properties prop;

/**
 * Constructor
 */ 
  public Spidey_Impl() {
    String [] supl = { "" };
     data = new GffDataStruct("", "", supl);
     System.out.println("constructing a alfresco.corba_wrappers.spidey.Spidey_Impl object");
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
    this.option = input.parameters[0];
    System.out.println("alfresco.corba_wrappers.spidey.Spidey_Impl Seqs: " + gSeqName + ", " + eSeqName + " Option: " + option);
  }
/**
 * Runs the analysis. 
 */
  public void run() {
    // Do the dirty stuff !!
    if (tmpDir == null) {
      System.out.println("alfresco.corba_wrappers.spidey.Spidey_Impl: tmpDir not set!");
    }
    FastaFile gFile = new FastaFile(tmpDir.getPath(), gSeqName, ">" + gSeqName, gSeq);
    gFile.write();
    FastaFile eFile = new FastaFile(tmpDir.getPath(), eSeqName, ">" + eSeqName, eSeq);
    eFile.write();
    alfresco.Spidey spr = new alfresco.Spidey(gFile, eFile, option, prop);
    spr.start();
    try {
      spr.join();
      data.gff = spr.getGff();
    } catch (Exception e) { e.printStackTrace(); }
    System.out.println(data.gff);
  }
/**
 * Gets the resultant data
 * @return gff data structure
 */
  public GffDataStruct getGffData() throws alfresco.corba_wrappers.NoOutputException{
    if (data == null) { 
      throw new alfresco.corba_wrappers.NoOutputException("Spidey produced no output"); 
    }
    return data;
  }
}
