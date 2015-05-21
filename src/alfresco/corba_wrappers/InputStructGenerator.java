/*
* $Revision: 1.1 $
* $Id: InputStructGenerator.java,v 1.1 2003/04/04 11:32:37 niclas Exp $
*
* This file is part of Alfresco
* Copyright (C) 1998-1999  Niclas Jareborg
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
package alfresco.corba_wrappers;
import java.util.*;

/**
 * Client class for easily generating alfresco.corba_wrappers.InputStruct
 * @see alfresco.corba_wrappers.InputStruct
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class InputStructGenerator {
  Vector names;
  Vector seqs;
  Vector parameters;
  
  /**
   * Creates InputStructGenerator
   */
  public InputStructGenerator (){
    names = new Vector();
    seqs = new Vector();
    parameters = new Vector();
  }
  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void addInput(String name, String seq) {
    names.addElement(name);
    seqs.addElement(seq);
  }
  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void addParameter(String param) {
    parameters.addElement(param);
  }
  
  /**
   * Gets InputStruct
   * @return input struct
   */
  public InputStruct getInputStruct(){
    String [] namesA = {""};
    String [] seqsA = {""};
    String [] parametersA = {""};
    
    if (names.size() > 0) {
      namesA = new String [names.size()];
      names.copyInto(namesA);
    }
    
    if (seqs.size() > 0) {
      seqsA = new String [seqs.size()];
      seqs.copyInto(seqsA);
    }
    
    if (parameters.size() > 0) {
      parametersA = new String [parameters.size()];
      parameters.copyInto(parametersA);
    }
    
    return new InputStruct(namesA, seqsA, parametersA);
  }
  
  public static void main (String [] args) {
    InputStructGenerator isg = new InputStructGenerator();
    isg.addInput("seq1","aaact");
    isg.addInput("seq2","gcgct");
//     isg.addParameter("-m");
    InputStruct is = isg.getInputStruct();
    for (int i = 0; i < is.names.length; i++) {
      System.out.println(is.names[i] + ": " + is.seqs[i]);
    }
    for (int i = 0; i < is.parameters.length; i++) {
      System.out.print("parameters: " +is.parameters[i] + " ");
    }
    System.out.println();
  }

}
