/*
 * $Revision: 1.1 $
 * $Id: CodonTranslator.java,v 1.1 2005/06/28 11:17:11 niclas Exp $
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
 * For more information contact Niclas at niclas.jareborg@genpat.uu.se
 */

package alfresco;
import java.util.Hashtable;

public class CodonTranslator
{

    public CodonTranslator(int i)
    {
        threeLetter = new Hashtable(65);
        oneLetter = new Hashtable(65);
        switch(i)
        {
        case 0: // '\0'
            threeLetter.put("TTT", "Phe");
            threeLetter.put("TTC", "Phe");
            threeLetter.put("TTA", "Leu");
            threeLetter.put("TTG", "Leu");
            threeLetter.put("TCT", "Ser");
            threeLetter.put("TCC", "Ser");
            threeLetter.put("TCA", "Ser");
            threeLetter.put("TCG", "Ser");
            threeLetter.put("TAT", "Tyr");
            threeLetter.put("TAC", "Tyr");
            threeLetter.put("TAA", "Stop");
            threeLetter.put("TAG", "Stop");
            threeLetter.put("TGT", "Cys");
            threeLetter.put("TGC", "Cys");
            threeLetter.put("TGA", "Stop");
            threeLetter.put("TGG", "Trp");
            threeLetter.put("CTT", "Leu");
            threeLetter.put("CTC", "Leu");
            threeLetter.put("CTA", "Leu");
            threeLetter.put("CTG", "Leu");
            threeLetter.put("CCT", "Pro");
            threeLetter.put("CCC", "Pro");
            threeLetter.put("CCA", "Pro");
            threeLetter.put("CCG", "Pro");
            threeLetter.put("CAT", "His");
            threeLetter.put("CAC", "His");
            threeLetter.put("CAA", "Gln");
            threeLetter.put("CAG", "Gln");
            threeLetter.put("CGT", "Arg");
            threeLetter.put("CGC", "Arg");
            threeLetter.put("CGA", "Arg");
            threeLetter.put("CGG", "Arg");
            threeLetter.put("ATT", "Ile");
            threeLetter.put("ATC", "Ile");
            threeLetter.put("ATA", "Ile");
            threeLetter.put("ATG", "Met");
            threeLetter.put("ACT", "Thr");
            threeLetter.put("ACC", "Thr");
            threeLetter.put("ACA", "Thr");
            threeLetter.put("ACG", "Thr");
            threeLetter.put("AAT", "Asn");
            threeLetter.put("AAC", "Asn");
            threeLetter.put("AAA", "Lys");
            threeLetter.put("AAG", "Lys");
            threeLetter.put("AGT", "Ser");
            threeLetter.put("AGC", "Ser");
            threeLetter.put("AGA", "Arg");
            threeLetter.put("AGG", "Arg");
            threeLetter.put("GTT", "Val");
            threeLetter.put("GTC", "Val");
            threeLetter.put("GTA", "Val");
            threeLetter.put("GTG", "Val");
            threeLetter.put("GCT", "Ala");
            threeLetter.put("GCC", "Ala");
            threeLetter.put("GCA", "Ala");
            threeLetter.put("GCG", "Ala");
            threeLetter.put("GAT", "Asp");
            threeLetter.put("GAC", "Asp");
            threeLetter.put("GAA", "Glu");
            threeLetter.put("GAG", "Glu");
            threeLetter.put("GGT", "Gly");
            threeLetter.put("GGC", "Gly");
            threeLetter.put("GGA", "Gly");
            threeLetter.put("GGG", "Gly");
            oneLetter.put("TTT", "F");
            oneLetter.put("TTC", "F");
            oneLetter.put("TTA", "L");
            oneLetter.put("TTG", "L");
            oneLetter.put("TCT", "S");
            oneLetter.put("TCC", "S");
            oneLetter.put("TCA", "S");
            oneLetter.put("TCG", "S");
            oneLetter.put("TAT", "Y");
            oneLetter.put("TAC", "Y");
            oneLetter.put("TAA", "X");
            oneLetter.put("TAG", "X");
            oneLetter.put("TGT", "C");
            oneLetter.put("TGC", "C");
            oneLetter.put("TGA", "X");
            oneLetter.put("TGG", "W");
            oneLetter.put("CTT", "L");
            oneLetter.put("CTC", "L");
            oneLetter.put("CTA", "L");
            oneLetter.put("CTG", "L");
            oneLetter.put("CCT", "P");
            oneLetter.put("CCC", "P");
            oneLetter.put("CCA", "P");
            oneLetter.put("CCG", "P");
            oneLetter.put("CAT", "H");
            oneLetter.put("CAC", "H");
            oneLetter.put("CAA", "Q");
            oneLetter.put("CAG", "Q");
            oneLetter.put("CGT", "R");
            oneLetter.put("CGC", "R");
            oneLetter.put("CGA", "R");
            oneLetter.put("CGG", "R");
            oneLetter.put("ATT", "I");
            oneLetter.put("ATC", "I");
            oneLetter.put("ATA", "I");
            oneLetter.put("ATG", "M");
            oneLetter.put("ACT", "T");
            oneLetter.put("ACC", "T");
            oneLetter.put("ACA", "T");
            oneLetter.put("ACG", "T");
            oneLetter.put("AAT", "N");
            oneLetter.put("AAC", "N");
            oneLetter.put("AAA", "K");
            oneLetter.put("AAG", "K");
            oneLetter.put("AGT", "S");
            oneLetter.put("AGC", "S");
            oneLetter.put("AGA", "R");
            oneLetter.put("AGG", "R");
            oneLetter.put("GTT", "V");
            oneLetter.put("GTC", "V");
            oneLetter.put("GTA", "V");
            oneLetter.put("GTG", "V");
            oneLetter.put("GCT", "A");
            oneLetter.put("GCC", "A");
            oneLetter.put("GCA", "A");
            oneLetter.put("GCG", "A");
            oneLetter.put("GAT", "D");
            oneLetter.put("GAC", "D");
            oneLetter.put("GAA", "E");
            oneLetter.put("GAG", "E");
            oneLetter.put("GGT", "G");
            oneLetter.put("GGC", "G");
            oneLetter.put("GGA", "G");
            oneLetter.put("GGG", "G");
            return;
        }
    }

    public String translateCodonToOneLetter(String s)
    {
        return (String)oneLetter.get(s);
    }

    public String translateCodonToThreeLetter(String s)
    {
        return (String)threeLetter.get(s);
    }

    public String translateSequenceToOneLetter(String s)
    {
        StringBuffer stringbuffer = new StringBuffer(s.length());
        for(int i = 0; i < s.length() - 2; i += 3)
        {
            String s1 = translateCodonToOneLetter(s.substring(i, i + 3));
            if(s1 != null)
                stringbuffer.append(s1);
            else
                stringbuffer.append("-");
        }

        return stringbuffer.toString();
    }

    public String translateSequenceToThreeLetter(String s)
    {
        StringBuffer stringbuffer = new StringBuffer(s.length());
        for(int i = 0; i < s.length() - 2; i += 3)
        {
            String s1 = translateCodonToThreeLetter(s.substring(i, i + 3));
            if(s1 != null)
                stringbuffer.append(s1);
            else
                stringbuffer.append("---");
        }

        return stringbuffer.toString();
    }

    public static final int UNIVERSAL = 0;
    public static final int HUMAN = 0;
    protected Hashtable threeLetter;
    protected Hashtable oneLetter;
}