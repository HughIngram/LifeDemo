//***************************************************************************
//  Logicell 1.0
//    v:0.0 17/6/2000
//    v:1.0 28/10/2000
//
//  This program shows the capability of a Conway cellular automata to manage
//  boolean functions.
//
//    Jean-Philippe Rennard 2000
//    alife@rennard.org
//    http://www.rennard.org/alife
//
//    This program is free software; you can redistribute it and/or
//    modify it under the terms of the GNU General Public License
//    as published by the Free Software Foundation.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//***************************************************************************
package uk.co.hughingram.lifedemo.model.logicell;

import java.util.*;

/** This class contains patterns defs */
public class CPattern {
  // Patterns name def
  static final int P_NIL=-1;
  static final int P_RPENTO=0;
  static final int P_GUN30R=1;
  static final int P_EATERR=2;
  static final int P_ENTRYRF=3;
  static final int P_ENTRYRT=4;
  static final int P_OUTPUT=5;
  static final int P_RANDOM=6;
  static final int P_ACORN=7;
  static final int P_RABBITS=8;
  static final int P_BIGUN=9;
  static final int P_MAKEGUN=10;

  static final int ENTRY_WIDTH=40;
  static final int ENTRY_OPYOFS=7;
  // what do these do???
  static final int OUTPUT_RIGHT_OFFSET=21;
  static final int OUTPUT_LEFT_OFFSET=14;

  /** Pattern pos */
  int x,y;
  int NbLines=1;
  int NbCols=1;
  /** Direction of the Output of that pattern */
  boolean OutputDir;
  /** Postion of the output */
  int OutputX, OutputY;
  /** Numbre of blocks */
  int NbBlocks=0;
  /** Long value of each block */
  long BlockValues[];
  /** position of each block. 0=x, 1=y */
  int BlockPos[][];

  /** Is the pattern mirrored ? */ 
  private boolean MirrorH, MirrorV;
  /** Name of the pattern */
  private int PatName;
  /** Hexa strings representing the lines */
  private Vector Lines;
  /** Binary representation of the string */
  private Vector LineValues;


  /** Default contructor. Creates a Rpento in 0,0 */
  public CPattern() {
    x=0;
    y=0;
    CreatePattern(P_RPENTO);
    LineValues = new Vector();
    CalcPattern(false,false);
  }

  /** Construct a pattern at x,y and mirror it*/
  public CPattern(int _x, int _y, int patname, boolean mirrorh, boolean mirrorv) {
    MirrorH=mirrorh;
    MirrorV=mirrorv;
    x=_x+0;
    y=_y+0;
    PatName=patname;
    CreatePattern(patname);
    InitPattern();
    LineValues = new Vector();
  }

  /** Set patterns position and blocks values */
  void SetPattern() {
    CalcPattern(MirrorH, MirrorV);
  }

  /** Set patterns position and blocks values at pos */
  void SetPattern(int _x, int _y) {
    x += _x;
    y += _y;
    CalcPattern(MirrorH, MirrorV);
  }

  /** Set default pattern pos and param */
  private void InitPattern() {
    switch(PatName) {
      case P_NIL:
        NbLines=1;
        OutputDir=false;
      break;

      case P_GUN30R:
        NbLines=9;
        OutputDir=(MirrorH ? CLogiTemplate.LEFT : CLogiTemplate.RIGHT);
        OutputX=(MirrorH ? x+14 : x+21);
        OutputY=y+7;
        NbCols=36;
      break;
      case P_ENTRYRF:
        NbLines=14;
        OutputDir=(MirrorH ? CLogiTemplate.LEFT : CLogiTemplate.RIGHT);
        OutputX=(MirrorH ? x+14 : x+21);
        OutputY=y+7;
        NbCols=36;
      break;
      case P_ENTRYRT:
        NbLines=14;
        OutputDir=(MirrorH ? CLogiTemplate.LEFT : CLogiTemplate.RIGHT);
        OutputX=(MirrorH ? x+14 : x+21);
        OutputY=y+7;
        NbCols=36;
      break;
      case P_EATERR:
        NbLines=4;
        NbCols=4;
      break;
      case P_OUTPUT:
        NbLines=6;
        NbCols=9;
      break;
      case P_RANDOM :
        NbLines=128;
        NbCols=64;
      break;
      case P_ACORN :
        NbLines=3;
        NbCols=8;
      break;
      case P_RABBITS :
        NbLines=3;
        NbCols=8;
      break;
      case P_BIGUN :
        NbLines=13;
        NbCols=52;
      break;
      case P_MAKEGUN :
        NbLines=14;
        NbCols=48;
      break;
      case P_RPENTO:
      default:
        NbLines=3;
        NbCols=4;
      break;
    }
  }

  /** Patterns Def */
  private void CreatePattern(int patname) {
    switch(patname) {
      case P_NIL:
        NbLines=1;
        Lines = new Vector(NbLines);
        Lines.addElement("000000000");
        OutputDir=false;
        NbCols=0;
      break;
      case P_GUN30R:
        NbLines=9;
        Lines = new Vector(NbLines);
        Lines.addElement("000000400");
        Lines.addElement("000000500");
        Lines.addElement("00C0000C0");
        Lines.addElement("0220000C3");
        Lines.addElement("0410000C3");
        Lines.addElement("CD1008500");
        Lines.addElement("C41004400");
        Lines.addElement("02201C000");
        Lines.addElement("00C000000");
        OutputDir=(MirrorH ? CLogiTemplate.LEFT : CLogiTemplate.RIGHT);
        OutputX=(MirrorH ? x+14 : x+21);
        OutputY=y+7;
        NbCols=36;
      break;
      case P_ENTRYRF:
        NbLines=14;
        Lines = new Vector(NbLines);
        Lines.addElement("000000400");
        Lines.addElement("000000500");
        Lines.addElement("00C0000C0");
        Lines.addElement("0220000C3");
        Lines.addElement("0410000C3");
        Lines.addElement("CD1008500");
        Lines.addElement("C41004400");
        Lines.addElement("02201C000");
        Lines.addElement("00C000000");
        Lines.addElement("000000000");
        Lines.addElement("000001800");
        Lines.addElement("000001000");
        Lines.addElement("000000E00");
        Lines.addElement("000000200");
        OutputDir=(MirrorH ? CLogiTemplate.LEFT : CLogiTemplate.RIGHT);
        OutputX=(MirrorH ? x+14 : x+21);
        OutputY=y+7;
        NbCols=36;
      break;
      case P_ENTRYRT:
        NbLines=14;
        Lines = new Vector(NbLines);
        Lines.addElement("000000400");
        Lines.addElement("000000500");
        Lines.addElement("00C0000C0");
        Lines.addElement("0220000C3");
        Lines.addElement("0410000C3");
        Lines.addElement("CD1008500");
        Lines.addElement("C41004400");
        Lines.addElement("02201C000");
        Lines.addElement("00C000000");
        Lines.addElement("000000000");
        Lines.addElement("000001800");
        Lines.addElement("000001000");
        Lines.addElement("000001E00");
        Lines.addElement("000000200");
        OutputDir=(MirrorH ? CLogiTemplate.LEFT : CLogiTemplate.RIGHT);

        OutputX=(MirrorH ? x+14 : x+21);
        OutputY=y+7;
        NbCols=36;
      break;
      case P_EATERR:
        NbLines=4;
        Lines = new Vector(NbLines);
        Lines.addElement("C");
        Lines.addElement("8");
        Lines.addElement("7");
        Lines.addElement("1");
        NbCols=4;
      break;
      case P_OUTPUT:
        NbLines=6;
        Lines = new Vector(NbLines);
        Lines.addElement("033");
        Lines.addElement("113");
        Lines.addElement("1E0");
        Lines.addElement("000");
        Lines.addElement("180");
        Lines.addElement("180");
        NbCols=9;
      break;
      case P_RANDOM:
        NbLines=128;
        NbCols=64;
        Lines=new Vector(NbLines);
        String s="";
        for(int i=0; i<NbLines; i++) {
          s="";
          for(int j=0;j<NbCols;j++)
            s += (Math.random()>0.8 ? "1" : "0");
          long l=BinStrToLong(s);
          String s2=Long.toHexString(l);
          for (int j=s2.length();j<=16;j++)
            s2="0"+s2;
          Lines.addElement(s2);
        }
      break;
      case P_ACORN :
        NbLines=3;
        Lines=new Vector(NbLines);
        Lines.addElement("40");
        Lines.addElement("10");
        Lines.addElement("CE");
        NbCols=8;
      break;
      case P_RABBITS :
        NbLines=3;
        Lines=new Vector(NbLines);
        Lines.addElement("8E");
        Lines.addElement("E4");
        Lines.addElement("40");
        NbCols=8;
      break;
      case P_BIGUN :
        NbLines=13;
        Lines=new Vector(NbLines);
        Lines.addElement("0000E00000000");
        Lines.addElement("0000200000000");
        Lines.addElement("0000200000000");
        Lines.addElement("0000400000000");
        Lines.addElement("00000001C000C");
        Lines.addElement("0000400100004");
        Lines.addElement("0000200100000");
        Lines.addElement("8000200080000");
        Lines.addElement("C000E00000000");
        Lines.addElement("0000000080000");
        Lines.addElement("0000000100000");
        Lines.addElement("0000000100000");
        Lines.addElement("00000001C0000");
        NbCols=52;
      break;
      case P_MAKEGUN :
        NbLines=14;
        Lines=new Vector(NbLines);
        Lines.addElement("000080000000");
        Lines.addElement("0000A0001000");
        Lines.addElement("0000C0001400");
        Lines.addElement("A00400001800");
        Lines.addElement("600300000000");
        Lines.addElement("400600000000");
        Lines.addElement("000000000000");
        Lines.addElement("000000000000");
        Lines.addElement("00300000C000");
        Lines.addElement("00180000A00E");
        Lines.addElement("002000008008");
        Lines.addElement("000000100004");
        Lines.addElement("000000180000");
        Lines.addElement("000000280000");
        NbCols=48;
      break;
      case P_RPENTO:
      default:
        NbLines=3;
        Lines = new Vector(NbLines);
        Lines.addElement("4");
        Lines.addElement("C");
        Lines.addElement("6");
        NbCols=4;
      break;
    }
  }

  /** Vertical mirror of a pattern (not used) */
  private void MirrorVert(Vector lines) {
    String str,s;
    Vector ltmp=new Vector(NbLines);
    for(int i=NbLines;i>0;i--) {
      ltmp.addElement((String)lines.elementAt(i-1));
    }
    lines.removeAllElements();
    for(int i=0;i<NbLines;i++) {
      lines.addElement((String)ltmp.elementAt(i));
    }
  }

  /** Generates the corresponding blocks long values and mirror pattern */
  private void CalcPattern(boolean mirrorh, boolean mirrorv) {
    LineValues.removeAllElements();
    InitPattern();
    if(!mirrorh) {
        OutputDir=CLogiTemplate.RIGHT;
        OutputX=x+OUTPUT_RIGHT_OFFSET;
        OutputY=y+7;
    }
    else {
        OutputDir=CLogiTemplate.LEFT;
        OutputX=x+OUTPUT_LEFT_OFFSET;
        OutputY=y+7;
    }

    String cl=(String) (Lines.elementAt(0));
    int stleng=cl.length();
    int valcrt=0;
    String strcrt="";
    String s,sx;
    int DeltaX=0;
    // for each line
    for(int i=0;i<NbLines;i++) {
      // construct a binary string
      strcrt="";
      cl=(String) (Lines.elementAt(i));
      for(int j=0;j<stleng;j++) {
        s="0x"+cl.substring(j,j+1);
        s = Integer.toBinaryString(Integer.decode(s).intValue());
        int ln=s.length();
        for(int k=ln;k<4;k++)
          s = "0"+s;
        strcrt += s;
      }
      sx="";

      if(mirrorh) {
        for(int j=strcrt.length();j>0;j--) {
          sx += strcrt.substring(j-1,j);
        }
        strcrt=sx;
      }
      // add x offset
      DeltaX=0;
      sx="";
      if(x>0) {
        for(int j=0;j<(x%8);j++) {
          sx += "0";
          DeltaX=0;
        }
      }
      else if(x<0) {
        for(int j=0;j<8+(x%8);j++) {
          sx += "0";
          DeltaX=-1;
        }
      }
      strcrt = sx+strcrt;
      // complete last block horizontal
      int ln=strcrt.length();
      if(ln%8 !=0) {
        for(int j=ln%8;j<8;j++)
          strcrt += "0";
      }
      LineValues.addElement(strcrt);
      NbCols=strcrt.length();
    } // for each line

    if(mirrorv) {
      MirrorVert(LineValues);
    }
    // y Offset
    strcrt="";
    // creates a 0 string
    for(int i=0;i<NbCols;i++)
      strcrt += "0";
    // add it y time at the beginning
    for(int i=0;i<(y%8);i++) {
      LineValues.insertElementAt(strcrt,i);
    }
    NbLines += (y%8);
    // complete last blocks vertical
    if(NbLines%8!=0) {
      for(int i=NbLines%8;i<8;i++) {
        LineValues.addElement(strcrt);
        NbLines++;
      }
    }
    // Creates blocks values
    NbBlocks = (NbCols/8) * (NbLines/8);
    BlockValues = new long[NbBlocks];
    BlockPos = new int[NbBlocks][2];
    int numbloccrt=0;
    for(int poshor=0;poshor<(NbCols/8);poshor++) {
      for(int posver=0;posver<(NbLines/8);posver++) {
        strcrt="";
        for(int i=0;i<8;i++) {
          strcrt += ((String)LineValues.elementAt(i+posver*8))
            .substring(poshor*8,poshor*8+8);
        }
        BlockPos[numbloccrt][0]=poshor+((x)/8)+DeltaX;
        BlockPos[numbloccrt][1]=posver+(y/8);
        BlockValues[numbloccrt]=BinStrToLong(strcrt);
        numbloccrt++;
      }
    } // for each block
  }

  /** Return long value of a binary string */
  private long BinStrToLong(String s) {
    long v=0L;
    long v2=0L;
    long res=0L;
    int lc=s.length();
    for(int i=lc-2;i>lc/2;i--)
      v += Math.pow(2*(s.charAt(i)-48),lc-i-1);
    for(int i=lc/2;i>0;i--)
      v2 += Math.pow(2*(s.charAt(i)-48),lc-i-1);
      v |= v2;

    // last digit extra because 0^0=1
    if(s.charAt(lc-1)==49)
      v++;
    if(s.charAt(0)==49)
      v |= (0x1L << 63);
    return(v);
  }

}

