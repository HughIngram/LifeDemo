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
package logicell;

import jpUtil.*;
import java.util.*;

/** Generates the global structure according to Logical String entry.
A LogiTemplate represents an equation. */
public class CLogiTemplate {
  // directions as booleans : true=Right, left=false. Allows direct inversion.
  final static boolean LEFT=false;
  final static boolean RIGHT=true;
  // Output Values
  static final int LT_NONE=-1;
  static final int LT_TRUE=1;
  static final int LT_FALSE=0;

  // Interface
  /** pointer to Universe */
  CLogicellUniverse MyUniverse;
  /** User Entry (equation) */
  String EntryU;
  /** Processed Entry (xor...) */
  String Entry;
  /** Position of the template */
  int x, y=0;
  /** Size */
  int CTWidth, CTHeight;
  /** Template blocks */
  Vector Blocks1, Blocks2;


  /** Entry in postfix notation */
  private String PostFixEntry;
  /** Entries tab */
  private boolean Entries[]=new boolean[CLogicellUniverse.MAXENTRY];
  /** Entries # */
  private int NbEntries;
  /** Position of the Output cell */
  private int OutpCellX, OutpCellY=0;
  /** OutputBlock */
  private CCells OutputBlock;
  /** OutputGeneration */
  private int OutputGeneration;
  /** Output value */
  private int OutputVal=LT_NONE;

  /** Construct a simple conway pattern */
  public CLogiTemplate(CLogicellUniverse univ, int pattype) {
    MyUniverse=univ;
    Blocks1=new Vector();
    Blocks2=new Vector();
    Stack Result=new Stack();
    CLogiComp DumComp=new CLogiComp(pattype);
    Result.push(DumComp);
    GenBlocks(Result);
  }

  /** Construct a logical structure */
  public CLogiTemplate(CLogicellUniverse univ, String _e,
                            boolean entries[], int _x, int _y) {
    OutputVal=LT_NONE;
    x= _x;
    y= _y;                            
    MyUniverse=univ;
    Blocks1=new Vector();
    Blocks2=new Vector();    
    Entry=_e;
    EntryU=Entry;
    for(int i=0;i<CLogicellUniverse.MAXENTRY;i++)
      Entries[i]=entries[i];
    ParseEntry();
  }

  /** Get output cell state */
  int GetOutputState(int gen) {
    if(MyUniverse.GetMode()==CLogicellUniverse.M_CONWAY)
      return(LT_NONE);

    if(OutputVal!=LT_NONE || gen<(OutputGeneration+1))
      return(OutputVal);

    int dx=OutpCellX-(OutputBlock.x*8);
    int dy=OutpCellY-(OutputBlock.y*8);
    long pos= 0x1L << 63-((dx%8)+(dy*8));
    if((OutputBlock.CellsVal & pos) != 0)
      OutputVal=LT_TRUE;
    else
      OutputVal=LT_FALSE;
    return(OutputVal);
  }

  /** Parse entry string and generates blocks. */
  private boolean ParseEntry() {
    // set special connectors (xor)
    Entry=JPParser.SetConnectors(Entry);
    char C;
    // Transform Infixed entry to Postfixed
    String EntryPost;
    EntryPost=JPParser.InFixToPostFix(Entry);

    // set comp directions
    NbEntries=JPParser.NbEntry(EntryPost);
    boolean EntryDir[]=new boolean[NbEntries];
    int rcrt=0;
    // init entries tab
    for(int i=0;i<EntryPost.length();i++) {
      C=EntryPost.charAt(i);
      if(!JPParser.IsOper(C)) {
        EntryDir[rcrt++]=true;
      }
    }
    // for each ~ inverse the corresponding entries recursively
    boolean prec=false;
    int rang=0;
    for(int i=0;i<EntryPost.length();i++) {
      C=EntryPost.charAt(i);
      if(!JPParser.IsBoolOper(C))
        rang++;
      if(C==JPParser.OP_NOT) {
        int j=i-1;
        int nc=0;
        char cc=EntryPost.charAt(j);
        j=i;
        int r=rang-1;
        while(nc>=0 && j>=0) {
          cc=EntryPost.charAt(j);
          if(!JPParser.IsBoolOper(cc)) {
            EntryDir[r] = !EntryDir[r];
            nc--;
            r--;
            j--;
          }
          else {
            while(JPParser.IsBoolOper(cc)){
              if(cc!=JPParser.OP_NOT)
                nc++;
                j--;
            cc=EntryPost.charAt(j);
            }
          }
        }
      } // C=='~'
    }

    // Evaluate expression
    EntryPost += '#';
    Stack Results=new Stack();
    CLogiComp LeftComp, RightComp, DumComp;
    int pc=0;
    C=EntryPost.charAt(pc);
    int entcrt=0;  // index of current entry
    while(C!='#') {
      // Simple entry
      if(!JPParser.IsBoolOper(C)) {
        DumComp=new CLogiComp(Entries[C-CLogicellUniverse.FirstEntry],EntryDir[entcrt]);
        if(!DumComp.IsValid())
          return(false);
        Results.push(DumComp);
        entcrt++;
      }
      // Unary operator
      else if(JPParser.IsUnaryOper(C)) {
        RightComp=(CLogiComp)Results.pop();
        if(pc<EntryPost.length()-2)   // is it the last op ?
          DumComp=new CLogiComp(C,RightComp,false);
        else
          DumComp=new CLogiComp(C,RightComp,true);
        if(!DumComp.IsValid())
          return(false);
        Results.push(DumComp);
      }
      // Binary operator
      else {
        RightComp=(CLogiComp)Results.pop();
        LeftComp=(CLogiComp)Results.pop();
        if(pc<EntryPost.length()-2)   // is it the last op ?
          DumComp=new CLogiComp(LeftComp, C, RightComp, false);
        else
          DumComp=new CLogiComp(LeftComp, C, RightComp, true);
        if(!DumComp.IsValid())
          return(false);
        Results.push(DumComp);
      }
      pc++;
      C=EntryPost.charAt(pc);
    }

    GenBlocks(Results);

    // Normalize (>0)
    int minx=9999999;
    int miny=9999999;
    int maxx=-9999999;
    int maxy=-9999999;
    CCells cel;
    for(int i=0;i<Blocks1.size();i++) {
      cel=(CCells)Blocks1.elementAt(i);
      if(cel.x<minx) minx=cel.x;
      if(cel.y<miny) miny=cel.y;
      if(cel.x>maxx) maxx=cel.x;
      if(cel.y>maxy) maxy=cel.y;
    }

    if(minx<0 || miny<0) {
      for(int i=0;i<Blocks1.size();i++) {
        cel=(CCells)Blocks1.elementAt(i);
        cel.x += minx<0 ? (-1*minx) : minx;
        cel.y += miny<0 ? (-1*miny) : miny;
        cel=(CCells)Blocks2.elementAt(i);
        cel.x += minx<0 ? (-1*minx) : minx;
        cel.y += miny<0 ? (-1*miny) : miny;
      }
      OutpCellX += minx<0 ? (-1*minx*8) : 0;
      OutpCellY += miny<0 ? (-1*miny*8) : 0;
    }

    for(int i=0;i<Blocks1.size();i++) {
      cel=(CCells)Blocks1.elementAt(i);
      if( cel.x == (OutpCellX/8) && cel.y == (OutpCellY/8)) {
        OutputBlock=cel;
        break;
        }
    }
    OutputGeneration=(OutpCellY-7)*4-2;

    // Move template to pos
    for(int i=0;i<Blocks1.size();i++) {
      cel=(CCells)Blocks1.elementAt(i);
      cel.x += (x/8);
      cel.y += (y/8);
    }
    OutpCellX += 8*(x/8);
    OutpCellY += 8*(y/8);
    for(int i=0;i<Blocks2.size();i++) {
      cel=(CCells)Blocks2.elementAt(i);
      cel.x += (x/8);
      cel.y += (y/8);
    }
    return(true);
  }

  /** Generate blocks */
  private void GenBlocks(Stack Results) {
    // generates blocks
    CLogiComp Lc;
    CPattern Pat;

    Lc=((CLogiComp)Results.peek());
    for(int j=0;j<Lc.NbPat;j++) {
      Pat=Lc.PatTab[j];
      for(int k=0;k<Pat.NbBlocks;k++) {
        if(Pat.BlockValues[k]!=0L) {
          boolean found=false;
          CCells cel;
          int pos=0;
          for(int bl=0;bl<Blocks1.size();bl++) {
            cel=(CCells)Blocks1.elementAt(bl);
            if(cel.x==Pat.BlockPos[k][0] &&
                  cel.y==Pat.BlockPos[k][1]) {
                found=true;
                pos=bl;
            }
          }
          if(found) {
            cel=(CCells)Blocks1.elementAt(pos);
            cel.CellsVal |= Pat.BlockValues[k];
            cel=(CCells)Blocks2.elementAt(pos);
            cel.CellsVal |= Pat.BlockValues[k];
          }
          else {
            Blocks1.addElement(new CCells(Blocks1, Pat.BlockPos[k][0],
                                    Pat.BlockPos[k][1],Pat.BlockValues[k]));
            Blocks2.addElement(new CCells(Blocks2, Pat.BlockPos[k][0],
                                    Pat.BlockPos[k][1],Pat.BlockValues[k]));
          }
        }
      } // next pat block
    } // next pat
    OutpCellX=Lc.ExitX;
    OutpCellY=Lc.ExitY;

    CTWidth=Math.abs(Lc.MaxX-Lc.MinX);
    CTHeight=Math.abs(Lc.MaxY-Lc.MinY);
  }

}
