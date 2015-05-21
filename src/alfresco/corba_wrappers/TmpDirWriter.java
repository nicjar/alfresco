/*
* $Revision: 1.1 $
* $Id: TmpDirWriter.java,v 1.1 2003/04/04 11:32:58 niclas Exp $
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

package alfresco.corba_wrappers;
import java.io.*;
import java.util.*;

/**
 * Interface for methods that should be able to write to a directory
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */
public interface TmpDirWriter {
  /**
   * Sets temporary directory to write to
   * @param dir tempory directory file
   */
  public void setTmpDir(File dir);
  
  public void setProperties(Properties prop);
}
