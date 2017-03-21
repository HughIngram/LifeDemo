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

/** CCells : this class manages and draws blocks of 8x8 cells */
public class CCells {

static long Tps, TpsA=0;

  /** position of the bloc */
  int x,y;
  /** # empty generations */
  private int EmptyGen=0;
  /** Point to North bloc */
  CCells NBloc;
  /** Point to NEast bloc */
  CCells NEBloc;
  /** Point to Eastern bloc */
  CCells EBloc;
  /** Point to SouthEast bloc */
  CCells SEBloc;
  /** Point to South bloc */
  CCells SBloc;
  /** Point to SouthWest bloc */
  CCells SWBloc;
  /** Point to Western bloc */
  CCells WBloc;
  /** Point to NorthWest bloc */
  CCells NWBloc;
  /** Cells values : long 64 bits = 8x8 */
  long cellsVal=0L;

  /** Pointer to the List which contains blocks */
  private Vector FatherList;

  /** Construct cells at pos */
  public CCells(Vector bv,int _x, int _y) {
    FatherList=bv;
    x=_x;
    y=_y;
    NBloc=null;
    NEBloc=null;
    EBloc=null;
    SEBloc=null;
    SBloc=null;
    SWBloc=null;
    WBloc=null;
    NWBloc=null;
    EmptyGen=0;
    cellsVal=0L;
    SetPointers();
  }
  
  /** Construct cells at pos according to cells values */
  public CCells(Vector bv,int _x, int _y, long v) {
    FatherList=bv;
    x=_x;
    y=_y;
    NBloc=null;
    NEBloc=null;
    EBloc=null;
    SEBloc=null;
    SBloc=null;
    SWBloc=null;
    WBloc=null;
    NWBloc=null;
    EmptyGen=0;
    cellsVal=v;
    SetPointers();
  }

  /** destruction of a bloc, updates pointers */
  void DestroyBloc() {
    if(SBloc!=null) SBloc.NBloc=null;
    if(SEBloc!=null) SEBloc.NWBloc=null;
    if(EBloc!=null) EBloc.WBloc=null;
    if(NEBloc!=null) NEBloc.SWBloc=null;
    if(NBloc!=null) NBloc.SBloc=null;
    if(NWBloc!=null) NWBloc.SEBloc=null;
    if(WBloc!=null) WBloc.EBloc=null;
    if(SWBloc!=null) SWBloc.NEBloc=null;
  }

  // commented out
  /** Live : draw the CCells (blocks), manages blocks of 2x2 cells */
//  void Live(JPView v) {
//    CLogicellView lv = (CLogicellView) v;
//
//    int a=(int)(lv.HorizStepD*8*x);
//    int b=(int)(lv.VerticStepD*8*y);
//    int hs=Math.max(lv.HorizStep,1);
//    int vs=Math.max(lv.VerticStep,1);
//    double hsd=lv.HorizStepD;
//    double vsd=lv.VerticStepD;
//
//    int p=0;
//    long vcrt, blcrt;
//    Graphics Gr=lv.GrBuff;
//    // First 2 lines
//
//    // Display South-East 2x2 cells
//    vcrt = cellsVal & 0x0303L;
//    if(vcrt !=0) {
//      p=0;
//
//      blcrt=(vcrt & 0x0300L) >> 6;
//      blcrt |= (vcrt & 0x03L);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(6*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(6*vsd+b), hs, vs);
//    }
//    // Display ESE 2x2 cells
//    vcrt = cellsVal & 0x0C0CL;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x0C00L) >> 8;
//      blcrt |= (vcrt & 0xCL) >> 2;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(6*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(6*vsd+b), hs, vs);
//    }
//    // Display WSW 2x2 cells
//    vcrt = cellsVal & 0x3030L;
//    if(vcrt !=0) {
//      p = 0;
//      blcrt =(vcrt & 0x3000L) >> 10;
//      blcrt |= (vcrt & 0x30L) >> 4;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(6*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(6*vsd+b), hs, vs);
//    }
//    // Display SW 2x2 cells
//    vcrt=cellsVal & 0xC0C0L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0xC000L) >> 12;
//      blcrt |= (vcrt & 0xC0L) >> 6;
//     blcrt |= (vcrt & 0x30L) >> 4;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(7*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(6*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(6*vsd+b), hs, vs);
//    }
//
//    // 2d 2 lines
//
//    // Display South-East 2x2 cells
//    vcrt=cellsVal & 0x03030000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x03000000L) >> 22;
//      blcrt |= (vcrt & 0x030000L) >> 16;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(4*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(4*vsd+b), hs, vs);
//    }
//
//    // Display ESE 2x2 cells
//    vcrt=cellsVal & 0x0C0C0000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x0C000000L) >> 24;
//      blcrt |= (vcrt & 0xC0000L) >> 18;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(4*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(4*vsd+b), hs, vs);
//    }
//    // Display WSW 2x2 cells
//    vcrt=cellsVal & 0x30300000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x30000000L) >> 26;
//      blcrt |= (vcrt & 0x300000L) >> 20;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(4*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(4*vsd+b), hs, vs);
//    }
//    // Display SW 2x2 cells
//    vcrt=cellsVal & 0xC0C00000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0xC0000000L) >> 28;
//      blcrt |= (vcrt & 0xC00000L) >> 22;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(5*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(4*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(4*vsd+b), hs, vs);
//    }
//
//    // 3d 2 lines
//
//    // Display South-East 2x2 cells
//    vcrt=cellsVal & 0x030300000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x030000000000L) >> 38;
//      blcrt |= (vcrt & 0x0300000000L) >> 32;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(2*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(2*vsd+b), hs, vs);
//    }
//
//    // Display ESE 2x2 cells
//    vcrt=cellsVal & 0x0C0C00000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x0C0000000000L) >> 40;
//      blcrt |= (vcrt & 0xC00000000L) >> 34;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(2*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(2*vsd+b), hs, vs);
//    }
//    // Display WSW 2x2 cells
//    vcrt=cellsVal & 0x303000000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x300000000000L) >> 42;
//      blcrt |= (vcrt & 0x3000000000L) >> 36;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(2*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(2*vsd+b), hs, vs);
//    }
//    // Display SW 2x2 cells
//    vcrt=cellsVal & 0xC0C000000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0xC00000000000L) >> 44;
//      blcrt |= (vcrt & 0xC000000000L) >> 38;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(3*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(2*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(2*vsd+b), hs, vs);
//    }
//
//    // 4th 2 lines
//
//    // Display South-East 2x2 cells
//    vcrt=cellsVal & 0x0303000000000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x0300000000000000L) >> 54;
//      blcrt |= (vcrt & 0x03000000000000L) >> 48;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(7*hsd+a),(int)(0*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(6*hsd+a),(int)(0*vsd+b), hs, vs);
//    }
//
//    // Display ESE 2x2 cells
//    vcrt=cellsVal & 0x0C0C000000000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x0C00000000000000L) >> 56;
//      blcrt |= (vcrt & 0x0C000000000000L) >> 50;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(5*hsd+a),(int)(0*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(4*hsd+a),(int)(0*vsd+b), hs, vs);
//    }
//    // Display WSW 2x2 cells
//    vcrt=cellsVal & 0x3030000000000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0x3000000000000000L) >> 58;
//      blcrt |= (vcrt & 0x30000000000000L) >> 52;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(3*hsd+a),(int)(0*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(2*hsd+a),(int)(0*vsd+b), hs, vs);
//    }
//    // Display SW 2x2 cells
//    vcrt=cellsVal & 0xC0C0000000000000L;
//    if(vcrt !=0) {
//      p=0;
//      blcrt=(vcrt & 0xC000000000000000L) >> 60;
//      blcrt |= (vcrt & 0xC0000000000000L) >> 54;
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(1*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(1*hsd+a),(int)(0*vsd+b), hs, vs);
//      if( ((blcrt >> p++) & 0x1L)>0 )
//        Gr.fillRect((int)(0*hsd+a),(int)(0*vsd+b), hs, vs);
//    }
//  }

  /** Set pointer to neighbours. Both directions.
  This function search the neighbours of a new cell.
  The best way should have been a wonderful BTree or hashtable
  but I think I've worked enough, sorry ;=) */
  private void SetPointers() {
    int xcrt,ycrt;
    CCells c;
    int nbf=0;
    for(int i=FatherList.size()-1;i>=0;i--) {
      c=(CCells)(FatherList.elementAt(i));
      xcrt=c.x;
      ycrt=c.y;
      if(xcrt==x && ycrt==y+1) { SBloc=c; c.NBloc=this; nbf++; }
      if(xcrt==x+1 && ycrt==y+1) { SEBloc=c; c.NWBloc=this; nbf++; }
      if(xcrt==x+1 && ycrt==y) { EBloc=c;  c.WBloc=this; nbf++; }
      if(xcrt==x+1 && ycrt==y-1) {  NEBloc=c; c.SWBloc=this; nbf++; }
      if(xcrt==x && ycrt==y-1) { NBloc=c; c.SBloc=this; nbf++; }
      if(xcrt==x-1 && ycrt==y-1) { NWBloc=c; c.SEBloc=this; nbf++; }
      if(xcrt==x-1 && ycrt==y) { WBloc=c; c.EBloc=this; nbf++; }
      if(xcrt==x-1 && ycrt==y+1) { SWBloc=c; c.NEBloc=this; nbf++; }
      if(nbf==8) break;
    }
  }
  
}