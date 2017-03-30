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
//    Modified by Hugh Ingram 2017-03-30
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

//import jpUtil.*;
import uk.co.hughingram.lifedemo.model.logicell.jputil.JPParser;
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
  CLogicellUniverse myUniverse;
  /** User Entry (equation) */
  String entryU;
  /** Processed Entry (xor...) */
  String entry;
  /** Position of the template */
  int x, y=0;
  /** Size */
  int ctWidth, ctHeight;
  
  /** Template blocks */
  Vector<CCells> blocks1, blocks2;


  /** Entry in postfix notation */
  private String PostFixEntry;
  /** Entries tab */
  private boolean Entries[]=new boolean[CLogicellUniverse.MAXENTRY];
  /** Entries # */
  private int nbEntries;
  /** Position of the Output cell */
  private int outpCellX, outpCellY =0;
  /** outputBlock */
  private CCells outputBlock;
  /** outputGeneration */
  private int outputGeneration;
  /** Output value */
  private int OutputVal=LT_NONE;

  /** Construct a simple conway pattern */
  public CLogiTemplate(CLogicellUniverse univ, int pattype) {
    myUniverse=univ;
    blocks1=new Vector<>();
    blocks2=new Vector<>();
    Stack<CLogiComp> result = new Stack<>();
    CLogiComp DumComp = new CLogiComp(pattype);
    result.push(DumComp);
    genBlocks(result);
  }

  /** Construct a logical structure */
  public CLogiTemplate(CLogicellUniverse univ, String _e, boolean entries[], int _x, int _y) {
    OutputVal=LT_NONE;
    x= _x;
    y= _y;                            
    myUniverse=univ;
    blocks1 = new Vector<>();
    blocks2 = new Vector<>();
    entry=_e;
    entryU=entry;
    for (int i=0;i<CLogicellUniverse.MAXENTRY;i++) {
        Entries[i] = entries[i];
    }
    parseEntry();
  }

  /** Get output cell state */
  int GetOutputState(int gen) {
    if(myUniverse.GetMode()==CLogicellUniverse.M_CONWAY)
      return(LT_NONE);

    if(OutputVal!=LT_NONE || gen<(outputGeneration +1))
      return(OutputVal);

    int dx= outpCellX -(outputBlock.x*8);
    int dy= outpCellY -(outputBlock.y*8);
    long pos= 0x1L << 63-((dx%8)+(dy*8));
    if((outputBlock.cellsVal & pos) != 0)
      OutputVal=LT_TRUE;
    else
      OutputVal=LT_FALSE;
    return(OutputVal);
  }

  /** Parse entry string and generates blocks. */
  private boolean parseEntry() {
    // set special connectors (xor)
    entry= JPParser.SetConnectors(entry);
    char c;
    // Transform Infixed entry to Postfixed
    String entryPost;
    entryPost=JPParser.InFixToPostFix(entry);

    // set comp directions
    nbEntries = JPParser.nbEntry(entryPost);
    boolean entryDir[] = new boolean[nbEntries];
    int rcrt = 0;
    // init entries tab
    for(int i = 0; i<entryPost.length(); i++) {
      c =entryPost.charAt(i);
      if(!JPParser.IsOper(c)) {
          entryDir[rcrt++]=true;
      }
    }

    // for each ~ inverse the corresponding entries recursively
    boolean prec = false;
    int rang=0;
    for(int i=0; i<entryPost.length(); i++) {
      c=entryPost.charAt(i);
      if(!JPParser.isBoolOper(c))
        rang++;
      if(c==JPParser.OP_NOT) {
        int j=i-1;
        int nc=0;
        char cc=entryPost.charAt(j);
        j=i;
        int r=rang-1;
        while(nc>=0 && j>=0) {
          cc=entryPost.charAt(j);
          if(!JPParser.isBoolOper(cc)) {
            entryDir[r] = !entryDir[r];
            nc--;
            r--;
            j--;
          }
          else {
            while(JPParser.isBoolOper(cc)){
              if(cc!=JPParser.OP_NOT)
                nc++;
                j--;
            cc=entryPost.charAt(j);
            }
          }
        }
      } // C=='~'
    }

    // Evaluate expression
    entryPost += '#';
    Stack<CLogiComp> results = new Stack<>();
    CLogiComp leftComp, rightComp, dumComp;
    int pc=0;
    c=entryPost.charAt(pc);
    int entcrt=0;  // index of current entry
    while(c!='#') {
      // Simple entry
      if(!JPParser.isBoolOper(c)) {
        dumComp=new CLogiComp(Entries[c-CLogicellUniverse.FirstEntry],entryDir[entcrt]);
        if(!dumComp.IsValid())
          return(false);
        results.push(dumComp);
        entcrt++;
      }
      // Unary operator
      else if(JPParser.isUnaryOper(c)) {
        rightComp = results.pop();
        if(pc<entryPost.length()-2)   // is it the last op ?
          dumComp=new CLogiComp(c,rightComp,false);
        else
          dumComp=new CLogiComp(c,rightComp,true);
        if(!dumComp.IsValid())
          return(false);
        results.push(dumComp);
      }
      // Binary operator
      else {
        rightComp = results.pop();
        leftComp = results.pop();
        if(pc<entryPost.length()-2)   // is it the last op ?
          dumComp=new CLogiComp(leftComp, c, rightComp, false);
        else
          dumComp=new CLogiComp(leftComp, c, rightComp, true);
        if(!dumComp.IsValid())
          return(false);
        results.push(dumComp);
      }
      pc++;
      c=entryPost.charAt(pc);
    }

    genBlocks(results);

      // Normalize (>0)
      int minx=9999999;
      int miny=9999999;
      int maxx=-9999999;
      int maxy=-9999999;
      CCells cel;
      // find the greatest and smallest coordinates of blocks
      for (int i=0; i<blocks1.size(); i++) {
          cel = blocks1.elementAt(i);
          if(cel.x<minx) minx=cel.x;
          if(cel.y<miny) miny=cel.y;
          if(cel.x>maxx) maxx=cel.x;
          if(cel.y>maxy) maxy=cel.y;
      }

      if (minx < 0 || miny < 0) {
          for (int i=0; i < blocks1.size(); i++) {
              cel = blocks1.elementAt(i);
              cel.x += minx < 0 ? (-1 * minx) : minx;
              cel.y += miny < 0 ? (-1 * miny) : miny;
              cel = blocks2.elementAt(i);
              cel.x += minx < 0 ? (-1 * minx) : minx;
              cel.y += miny < 0 ? (-1 * miny) : miny;
          }
          outpCellX += minx < 0 ? (-1 * minx * 8) : 0;
          outpCellY += miny < 1 ? (-1 * miny * 8) : 0;
      }

      // normalise the output cell
      for(int i=0; i < blocks1.size(); i++) {
          cel = blocks1.elementAt(i);
          if(cel.x == (outpCellX /8) && cel.y == (outpCellY /8)) {
              outputBlock = cel;
              break;
          }
      }
      outputGeneration = (outpCellY -7) * 4 - 2;

      // Move template to pos
      for(int i=0;i<blocks1.size();i++) {
          cel= blocks1.elementAt(i);
          cel.x += (x/8);
          cel.y += (y/8);
      }
      outpCellX += 8*(x/8);
      outpCellY += 8*(y/8);
      for(int i=0;i<blocks2.size();i++) {
          cel = blocks2.elementAt(i);
          cel.x += (x/8);
          cel.y += (y/8);
      }
      return(true);
  }

  /** Generate blocks */
  private void genBlocks(Stack<CLogiComp> results) {
      // generates blocks
      final CLogiComp lc = results.peek();
      for (int j = 0; j < lc.nbPat; j++) {
          final CPattern pat = lc.patTab[j];
          for (int k = 0; k < pat.NbBlocks; k++) {
              if (pat.BlockValues[k]!=0L) {
                  boolean found=false;
                  CCells cel;
                  int pos = 0;
                  for(int bl=0; bl<blocks1.size(); bl++) {
                      cel = (CCells) blocks1.elementAt(bl);
                      if (cel.x==pat.BlockPos[k][0] &&
                              cel.y==pat.BlockPos[k][1]) {
                          found=true;
                          pos=bl;
                      }
                  }
                  if(found) {
                      cel=(CCells)blocks1.elementAt(pos);
                      cel.cellsVal |= pat.BlockValues[k];
                      cel=(CCells)blocks2.elementAt(pos);
                      cel.cellsVal |= pat.BlockValues[k];
                  }
                  else {
                      blocks1.addElement(new CCells(blocks1, pat.BlockPos[k][0],
                              pat.BlockPos[k][1],pat.BlockValues[k]));
                      blocks2.addElement(new CCells(blocks2, pat.BlockPos[k][0],
                              pat.BlockPos[k][1],pat.BlockValues[k]));
                  }
              }
          } // next pat block
      } // next pat
      outpCellX = lc.ExitX;
      outpCellY = lc.ExitY;

      ctWidth = Math.abs(lc.MaxX-lc.MinX);
      ctHeight = Math.abs(lc.MaxY-lc.MinY);
  }

}
