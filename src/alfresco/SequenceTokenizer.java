/*
* $Revision: 1.1 $
* $Id: SequenceTokenizer.java,v 1.1 2003/04/04 10:15:10 niclas Exp $
*
* This file is part of Alfresco
* Copyright (C) 1999  Niclas Jareborg
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
*
*/
package alfresco;
import java.util.*;
import java.io.*;
/**
 * SequenceTokenizer implements the Enumeration interface and is used 
 * for retrieving sequences one at a time from a FastaFile
 * @see alfresco.FastaFile
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class SequenceTokenizer implements Enumeration{
  Vector sequences;
  Enumeration sequenceEnum;
  /**
   * Creates a SequenceTokenizer from a Vector of lines from a FastaFile
   * @param param descr
   */
  public SequenceTokenizer(FastaFile f) {
    sequences = new Vector();
    if (f != null) {
      FileReader fr=null;
		  try {
		    fr = new FileReader(f);
  		  BufferedReader br = new BufferedReader(fr);
  //   		Vector lines = new Vector();
  		  String inline = null;
        inline = br.readLine();
  		  SEQ: while (inline != null) {
  //   			lines.addElement(inline);
          if (inline.startsWith(">")) {  
            // save seq to one stringbuffer
            StringBuffer sb = new StringBuffer();
            while (inline != null) {
              sb.append(inline);
              if (inline.startsWith(">")) sb.append("\n");
            // read new line and check if new sequence
              inline = br.readLine();
              if (inline == null || inline.startsWith(">") ) {
                sequences.addElement(new String(sb));
                continue SEQ;
              }
            }
          } else {
            // Read a new line?
          }
  		  }
		  } catch(Exception e) { 
        e.printStackTrace();
  //       throw fnf;
      }
    }
    sequenceEnum = sequences.elements();
  }

  /**
   * Implements Enumeration.nextElement
   * @return Sequence string, comment on one line and the sequence on another line
   */
  public Object nextElement() {
    return sequenceEnum.nextElement();
  }

  /**
   * Returns next sequence as a string
   * @return Sequence string
   */
  public String nextSequence() {
    return (String) this.nextElement();
  }
  /**
   * Implements Enumeration.hasMoreElements
   * @return true if more elements are available, otherwise false
   */
  public boolean hasMoreElements() {
    return sequenceEnum.hasMoreElements();
  }
  
  /**
   * Resets the "cursor to the first sequence"
   */
  public void reset() {
    sequenceEnum = sequences.elements();
  }
}
