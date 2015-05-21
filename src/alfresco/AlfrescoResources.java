/*
* $Revision: 1.1 $
* $Id: AlfrescoResources.java,v 1.1 2003/04/04 10:13:41 niclas Exp $
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
package alfresco;
import java.io.*;
import java.util.*;

/**
 * Resources and parameters for external programs
 * Associated with a .alfrescorc file
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class AlfrescoResources extends Hashtable{
	
  String homedir;
  File RcFile;
  boolean unix = true;
  static String[] PROGS = {
    "CpG", 
    "Genscan", 
    "RepeatMasker", 
    "BlastWise",
    "Genewise", 
    "BlEst_Genome",
    "BlGenewise",
    "Est_Genome",
    "BlastAlign", 
    "Dba",
    "Spidey"
//     "Dotter"
    };
  
//   static String IORURL = "http://kisac.cgr.ki.se/nic/methods_server.ior";
  static String IORURL = "http://diamond.ukhx.astrazeneca.net:6311/methods_server.ior";
/**
 * Creates an AlfrescoResources object from the specified file. If the file exists it is loaded,
 * otherwise a default AlfrescoResources object (and file) is created.
 * Note! Needs a default hashtable from each Method object containing a Vector of 
 * Executable names, and parameter values.
 */
 	public AlfrescoResources(File rcfile) {
    String os = System.getProperty("os.name");
    if(os.indexOf("Mac") != -1 || os.indexOf("Windows") != -1) {
      unix = false;
    }
    if (unix) {
      Runtime rt = Runtime.getRuntime();
      Properties syspr = new Properties();
      InputStream stdout = null;
      try {
        Process p = rt.exec("env");
			  stdout = p.getInputStream();
			  p.waitFor();
        syspr.load(stdout);

      }catch (Exception e) { e.printStackTrace(); }
      this.put("system", syspr);
    }
	  RcFile = rcfile;
    initialize();
  }
/**
 * Creates an AlfrescoResources object. If a ~/.alfrescorc file exists it is loaded,
 * otherwise a default AlfrescoResources object is created.
 * Note! Needs a default hashtable from each Method object containing a Vector of 
 * Executable names, and parameter values.
 */
 	public AlfrescoResources() {
    String os = System.getProperty("os.name");
    if(os.indexOf("Mac") != -1 || os.indexOf("Windows") != -1) {
      unix = false;
    }
    if (unix) {
      Runtime rt = Runtime.getRuntime();
      Properties syspr = new Properties();
      InputStream stdout = null;
      try {
        Process p = rt.exec("env");
			  stdout = p.getInputStream();
			  p.waitFor();
        syspr.load(stdout);

      }catch (Exception e) { e.printStackTrace(); }
      this.put("system", syspr);
    }
//     homedir = syspr.getProperty("HOME");
    homedir = System.getProperty("user.home");
//     RcFile = new File(homedir + "/.alfrescorc");
    RcFile = new File(homedir + System.getProperty("file.separator") + ".alfrescorc");
    initialize();
  }
  
/**
 * Initializes the resource object If the resource file file exists it is loaded,
 * otherwise a default AlfrescoResources object is created.
 * Note! Needs a default hashtable from each Method object containing a Vector of 
 * Executable names, and parameter values.
 */
 	private void initialize() {
    if (RcFile.exists()) {
      // Vector with all methodnames to use for checking if all methods have resource info
      Vector methods = new Vector();
      for (int i=0; i < PROGS.length; i++) {
        methods.addElement(PROGS[i]);
      }
      
      // do the right thing
      FileReader fr = null;
      try {
        String linesep = System.getProperty("line.separator");
        fr = new FileReader(RcFile);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
//         Properties currProps = null;
        StringBuffer sb = null;
        String currName = null;
        while(line != null) {
          if (line.startsWith("#")) {
             if (sb != null) {
//                byte [] barr = new String(sb).getByteArray();
               ByteArrayInputStream bais = new ByteArrayInputStream(new String(sb).getBytes());
               Properties pr = new Properties();
               pr.load(bais);
               this.put(currName, pr);
             }
             // get the method name
             int space = line.indexOf(" ");
             currName = line.substring(1,space);
//              System.out.println("current method name: " + currName);
             // remove name from the methods vector. if all methods have rc info
             // the methods vector will be empty at the end.
             methods.removeElement(currName);
             
             sb = new StringBuffer();
//              sb.append(line + "\n");
             sb.append(line + linesep);
             
            // remember to read in date stamp line
//              sb.append(br.readLine() + "\n");
             sb.append(br.readLine() + linesep);
             line = br.readLine();
             continue;
          }
//           sb.append(line + "\n");
          sb.append(line + linesep);
          line = br.readLine();
        }
        // again to get the last properties
        if (sb != null) {
 //                byte [] barr = new String(sb).getByteArray();
          ByteArrayInputStream bais = new ByteArrayInputStream(new String(sb).getBytes());
          Properties pr = new Properties();
          pr.load(bais);
          this.put(currName, pr);
        }
        if (!methods.isEmpty()) {
          Enumeration missing = methods.elements();
          while (missing.hasMoreElements()) {
            String methname = (String) missing.nextElement();
            this.put(methname, initializeMethodProps(methname));
          }
          saveProperties();
//         } else {
//           System.out.println("Resources for all methods exist");
        }
              
      } catch (IOException ioe) { ioe.printStackTrace(); }
//         catch (InterruptedException ie) { ie.printStackTrace(); }
        
    } else {
      for (int i=0; i < PROGS.length; i++) {
        this.put(PROGS[i], initializeMethodProps(PROGS[i]));
      }
      saveProperties();
//       try {
//         FileOutputStream out = new FileOutputStream (RcFile.getPath(), true); // append = true
//         Enumeration props = this.keys();
//         while (props.hasMoreElements()) {
//           String name = (String) props.nextElement();
//           if (name.equals("system")) { continue; }
// //           System.out.println("Got as far as the props names");
//           Properties p = (Properties) this.get(name);
// //           System.out.println("Got as far as the props");
//           System.out.println("Properties for " + name + ": " + p);
//           String header = name + " properties";
//           p.save(out, header);
//           System.out.println("Header: " + header);
//         }
//       } catch (IOException ioe) { ioe.printStackTrace(); }
    }
	}
  
  /**
   * Initializes all values for a method specified by name
   * @return properties object for the method
   * @param progname name of the method
   */
  public Properties initializeMethodProps(String progname) {
    Properties prop = new Properties();
    System.out.println("Looking for alfresco." + progname);
    Hashtable defhash = null;
    try {
      Class cl = Class.forName("alfresco." + progname);
      java.lang.reflect.Method defmeth = cl.getDeclaredMethod("getDefaults", new Class[0]);
      System.out.println("Method: "+ defmeth);
      defhash = (java.util.Hashtable) defmeth.invoke(null, new Object[0]);
      System.out.println ("Default hash: " + defhash);
  //         Hashtable defhash = cl.getDefaults();
    } catch (Exception e) { e.printStackTrace(); }
    if (unix) {
       // Get paths for executables
      // Note! Some methods use more than one executable...
      Vector execs = (Vector) defhash.get("executables");
      int localexec = 0;
      Enumeration exenum = execs.elements();
    //         System.out.println("Got as far as the vector of executables");
      EXEC:while (exenum.hasMoreElements()) {
        String exename = (String) exenum.nextElement();
    //           System.out.println("Got as far as the executable name");
        String outline = null;
    //           String errline = null;
        // change the executable checking section by iterating over all PATHs
        Properties syspr = (Properties) get("system");
        String paths = (String) syspr.get("PATH");
        StringTokenizer patht = new StringTokenizer(paths,":");
        Runtime rt = Runtime.getRuntime();
        PATHS: while (patht.hasMoreTokens()) {
          String path = patht.nextToken() + "/" + exename;
          try {
//             System.out.println("testing: " + path + ": ");
            Process p = rt.exec( "ls " + path );
			      InputStream stout = p.getInputStream();
			      InputStreamReader stdoutr = new InputStreamReader(stout);
			      BufferedReader stdoutbr = new BufferedReader(stdoutr);
    // 			        InputStream sterr = p.getErrorStream();
    // 			        InputStreamReader errR = new InputStreamReader(sterr);
    // 			        BufferedReader errbr = new BufferedReader(errR);
			      p.waitFor();
			      outline = stdoutbr.readLine();
    //               errline = errbr.readLine();
  //           System.out.println(outline);
          }catch (IOException ioe) { System.out.println(); continue PATHS; }
          catch (InterruptedException ie) {continue;}

          if (outline != null) {
    //             prop.put(exename, outline);
            System.out.println(exename + " path: " + path);
            prop.put(exename, path);
            localexec++;
            continue EXEC;
          }
        }
        prop.put(exename, ""); // if no path was found. to generate an empty field in the settings dialog
      }
      if (localexec == execs.size()) {
        prop.put("local", "true");
      } else {
        prop.put("local", "false");
      }
    } else {
       prop.put("local", "false");
    }
    
    prop.put("iorurl", IORURL);
    Enumeration keys = defhash.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      if (key.equals("executables")) { continue; }
  //           System.out.println("Got as far as the key name");
      prop.put(key, defhash.get(key));
    }
    return prop;
  }
  
  /**
   * Gets the properties object for the specified method
   * @return properties object
   * @param programName name of program
   */
  public Properties getProgamProperties(String programName) {
//     System.out.println(this);
    return (Properties) this.get(programName);
  }
  
  /**
   * Gets the properties object for the specified method
   * @return properties object
   * @param programName name of program
   */
  public void saveProperties() {
    try {
//       FileOutputStream out = new FileOutputStream (RcFile.getPath(), true); // append = true
      FileOutputStream out = new FileOutputStream (RcFile.getPath(), false); // append = false
      Enumeration props = this.keys();
      while (props.hasMoreElements()) {
        String name = (String) props.nextElement();
        if (name.equals("system")) { continue; }
  //           System.out.println("Got as far as the props names");
        Properties p = (Properties) this.get(name);
  //           System.out.println("Got as far as the props");
//         System.out.println("Properties for " + name + ": " + p);
        String header = name + " properties";
        p.save(out, header);
//         System.out.println("Header: " + header);
      }
      out.close();
    } catch (IOException ioe) { ioe.printStackTrace(); }
    
  }

}
