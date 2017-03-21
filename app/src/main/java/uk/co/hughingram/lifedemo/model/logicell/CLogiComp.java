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

/** Manages logical components : A template is a components organisation. */
public class CLogiComp {

  /** # of patterns in the component */
  int NbPat;
  /** Patterns tab : the patterns list of the component */
  CPattern PatTab[];
  /** Component size data */
  int MinX, MinY, MaxX, MaxY;
  /** Component Output position */
  int ExitX, ExitY=0;

  
  /** Validity flag */
  private boolean Valid=false;
  /** Output Offset */
  private int OutputOffset=4;
  /** Component Offset */
  private int CompOffset=4;
  private int x,y;
  private int Entry1X, Entry1Y;
  private int Entry2X, Entry2Y;
  private int OutputX, OutputY;
  private boolean OutpDirection;


 /** Construct a single pattern. */
  public CLogiComp(int pat) {
    if(pat!=CPattern.P_RANDOM && pat!=CPattern.P_BIGUN && pat!=CPattern.P_MAKEGUN)
      x=y=250;
    else {
      x=90;
      y=60;
    }
    NbPat=1;
    PatTab=new CPattern[NbPat];
    PatTab[0]=new CPattern(x,y,pat,false,false);
    PatTab[0].SetPattern();
     Entry1X=PatTab[0].OutputX;
     Entry1Y=PatTab[0].OutputY;
     Entry2X=PatTab[0].OutputX;
     Entry2Y=PatTab[0].OutputY;
     OutputX=PatTab[0].OutputX;
     OutputY=PatTab[0].OutputY;
     OutpDirection=PatTab[0].OutputDir;
     MinX=x;
     MinY=y;
     MaxX=MinX+PatTab[0].NbCols;
     MaxY=MinY+PatTab[0].NbLines;
     Valid=true;
  }


  /** Construct a single entry component with direction */
  public CLogiComp(boolean entry, boolean dir) {
    dir= !dir;
    x=y=0;
    NbPat=1;
    PatTab=new CPattern[NbPat];
    if(entry)
      PatTab[0]=new CPattern(x,y,CPattern.P_ENTRYRT,dir,false);
    else
      PatTab[0]=new CPattern(x,y,CPattern.P_ENTRYRF,dir,false);
    PatTab[0].SetPattern();

    Entry1X=PatTab[0].OutputX;
    Entry1Y=PatTab[0].OutputY;
    Entry2X=PatTab[0].OutputX;
    Entry2Y=PatTab[0].OutputY;
    OutputX=PatTab[0].OutputX;
    OutputY=PatTab[0].OutputY;
    OutpDirection=PatTab[0].OutputDir;

    MinX=x;
    MinY=y;
    MaxX=MinX+PatTab[0].NbCols;
    MaxY=MinY+PatTab[0].NbLines;
    Valid=true;
  }

  /** Construct a unary operator component */
  public CLogiComp(char op, CLogiComp Father, boolean last) {
    if(op!=JPParser.OP_NOT) {
      Valid=false;
      return;
    }
    NbPat=2+Father.NbPat;
    PatTab=new CPattern[NbPat];
    Father.MoveComp(0,0);
    for(int i=0;i<Father.NbPat;i++) {
      PatTab[i]=Father.PatTab[i];
    }

    // Right Output
    if(Father.OutpDirection==CLogiTemplate.RIGHT) {
      int dx;
      // dx has to be even
      dx=Father.MaxX+CompOffset+1+(1*
            ((Father.MaxX+CompOffset+1+CPattern.OUTPUT_LEFT_OFFSET)-
              Father.OutputX)%2);
      PatTab[Father.NbPat+0]=new CPattern(dx, Father.OutputY-CPattern.ENTRY_OPYOFS+0,//1,
                                CPattern.P_GUN30R,true,false);
      PatTab[Father.NbPat+0].SetPattern();
      Entry1X=Father.OutputX;
      Entry1Y=Father.OutputY;
      Entry2X=Father.OutputX;
      Entry2Y=Father.OutputY;
      OutputX=PatTab[Father.NbPat+0].OutputX;
      OutputY=PatTab[Father.NbPat+0].OutputY;
      OutpDirection=PatTab[Father.NbPat+0].OutputDir;
      int eat1x=Math.abs(PatTab[Father.NbPat+0].OutputX-Father.OutputX)/2+
                    Father.OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 5;
      eat1x -= OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        PatTab[Father.NbPat+1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,true,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        PatTab[Father.NbPat+1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,true,false);
      PatTab[Father.NbPat+1].SetPattern();
      MinX=Father.MinX;
      MinY=Father.MinY;
      MaxX=PatTab[Father.NbPat+0].x+PatTab[Father.NbPat+0].NbCols;
      MaxY=PatTab[Father.NbPat+1].y+PatTab[Father.NbPat+1].NbLines;
    }
    // Left Output
    else {
      int dx;
      // dx has to be even
      dx=Father.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
          (((Father.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
              CPattern.OUTPUT_RIGHT_OFFSET)-Father.OutputX)%2);

      PatTab[Father.NbPat+0]=new CPattern(dx, Father.OutputY-CPattern.ENTRY_OPYOFS+0,
                               CPattern.P_GUN30R,false,false);
      PatTab[Father.NbPat+0].SetPattern();
      Entry1X=Father.OutputX;
      Entry1Y=Father.OutputY;
      Entry2X=Father.OutputX;
      Entry2Y=Father.OutputY;
      OutputX=PatTab[Father.NbPat+0].OutputX;
      OutputY=PatTab[Father.NbPat+0].OutputY;
      OutpDirection=PatTab[Father.NbPat+0].OutputDir;

      int eat1x=Math.abs(PatTab[Father.NbPat+0].OutputX-Father.OutputX)/2+
                    PatTab[Father.NbPat+0].OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 6;
      eat1x += OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        PatTab[Father.NbPat+1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,false,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        PatTab[Father.NbPat+1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,false,false);
      PatTab[Father.NbPat+1].SetPattern();
      MinX=PatTab[Father.NbPat+0].x;
      MinY=Father.MinY;
      MaxX=Father.MaxX;
      MaxY=PatTab[Father.NbPat+1].y+PatTab[Father.NbPat+1].NbLines;
    }
    Valid=true;
  }

  /** Construct a binary operator component */
  public CLogiComp(CLogiComp LeftComp, char op, CLogiComp RightComp, boolean last) {
    switch(op) {
      case JPParser.OP_AND :
        Valid=MakeAndComp(LeftComp, RightComp, last);
      break;
      case JPParser.OP_OR :
        Valid=MakeOrComp(LeftComp, RightComp, last);
      break;

      default:
        Valid=false;
    }

  }

  /** Check validity */
  public boolean IsValid() {
    return(Valid);
  }


  /** Construction of an AND component */
  private boolean MakeAndComp(CLogiComp LeftComp, CLogiComp RightComp, boolean last) {
    NbPat=LeftComp.NbPat+RightComp.NbPat+3;
    PatTab=new CPattern[NbPat];
    for(int i=0;i<LeftComp.NbPat;i++) {
      PatTab[i]=LeftComp.PatTab[i];
    }
    int dx;
    // Right output
    if(LeftComp.OutpDirection==CLogiTemplate.RIGHT) {
      dx=LeftComp.MinX-Math.abs(RightComp.MaxX-RightComp.MinX)-CompOffset-1+(1*
          (((LeftComp.MinX-Math.abs(RightComp.MaxX-RightComp.MinX)-CompOffset-1)+
              Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);

      RightComp.SetPos(dx, RightComp.y);
      for(int i=0;i<RightComp.NbPat;i++) {
        PatTab[LeftComp.NbPat+i]=RightComp.PatTab[i];
      }
      int pat0=LeftComp.NbPat+RightComp.NbPat;
      dx=LeftComp.MaxX+CompOffset+1+(1*
              ((LeftComp.MaxX+CompOffset+1+CPattern.OUTPUT_LEFT_OFFSET)-
                LeftComp.OutputX)%2);


      PatTab[pat0]=new CPattern(dx, LeftComp.OutputY-7+0,//1,
            CPattern.P_GUN30R,true,false);
      PatTab[pat0].SetPattern();
      Entry1X=RightComp.OutputX;
      Entry1Y=RightComp.OutputY;
      Entry2X=LeftComp.OutputX;
      Entry2Y=LeftComp.OutputY;
      OutputX=RightComp.OutputX;
      OutputY=RightComp.OutputY;
      OutpDirection=RightComp.OutpDirection;

      int eat1x=Math.abs(PatTab[pat0].OutputX-RightComp.OutputX)/2+
                  RightComp.OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 6;
      eat1x += OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        PatTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,false,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        PatTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,false,false);
      PatTab[pat0+1].SetPattern();

      eat1x=PatTab[pat0].OutputX-(eat1y-PatTab[pat0].OutputY);
      eat1x -= 2;
      PatTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,true,false);
      PatTab[pat0+2].SetPattern();

      MinX=RightComp.MinX;
      MinY=RightComp.MinY;
      MaxX=PatTab[pat0].x+PatTab[pat0].NbCols;
      MaxY=PatTab[pat0+1].y+PatTab[pat0+1].NbLines;
    }
    // Left output
    else {
      dx=LeftComp.MaxX+CompOffset+(1*
          (((LeftComp.MaxX+CompOffset)+
            Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);

      RightComp.SetPos(dx, RightComp.y);
      for(int i=0;i<RightComp.NbPat;i++)
        PatTab[LeftComp.NbPat+i]=RightComp.PatTab[i];
      int pat0=LeftComp.NbPat+RightComp.NbPat;
      dx=LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
          (((LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
              CPattern.OUTPUT_RIGHT_OFFSET)-LeftComp.OutputX)%2);

      PatTab[pat0]=new CPattern(dx, RightComp.OutputY-7+0,//1,
            CPattern.P_GUN30R,false,false);
      PatTab[pat0].SetPattern();
      Entry1X=LeftComp.OutputX;
      Entry1Y=LeftComp.OutputY;
      Entry2X=RightComp.OutputX;
      Entry2Y=RightComp.OutputY;
      OutputX=RightComp.OutputX;
      OutputY=RightComp.OutputY;
      OutpDirection=RightComp.OutpDirection;

      int eat1x=Math.abs(RightComp.OutputX-PatTab[pat0].OutputX)/2+
              PatTab[pat0].OutputX;

      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 5;
      eat1x -= OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        PatTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,true,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        PatTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,true,false);
      PatTab[pat0+1].SetPattern();

      eat1x=PatTab[pat0].OutputX+(eat1y-PatTab[pat0].OutputY);
      eat1x -= 1;
      PatTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,false,false);
      PatTab[pat0+2].SetPattern();
      MinX=PatTab[pat0].x;
      MinY=RightComp.MinY;
      MaxX=RightComp.MaxX;
      MaxY=PatTab[pat0+2].y+PatTab[pat0+2].NbLines;
    }
    return(true);
  }

  /** Construction of an OR component */
  private boolean MakeOrComp(CLogiComp LeftComp, CLogiComp RightComp, boolean last) {
    NbPat=LeftComp.NbPat+RightComp.NbPat+4;
    PatTab=new CPattern[NbPat];
    for(int i=0;i<LeftComp.NbPat;i++) {
      PatTab[i]=LeftComp.PatTab[i];
    }
    int dx;
    Entry1X=LeftComp.OutputX;
    Entry1Y=LeftComp.OutputY;
    // Right Output
    if(LeftComp.OutpDirection==CLogiTemplate.RIGHT) {
      dx=LeftComp.MinX-Math.abs(RightComp.MaxX-RightComp.MinX)-CompOffset-1+(1*
          (((LeftComp.MinX-Math.abs(RightComp.MaxX-RightComp.MinX)-CompOffset-1)+
              Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);
      RightComp.SetPos(dx, RightComp.y);
      for(int i=0;i<RightComp.NbPat;i++) {
        PatTab[LeftComp.NbPat+i]=RightComp.PatTab[i];
      }
      Entry2X=RightComp.OutputX;
      Entry2Y=RightComp.OutputY;

      int pat0=LeftComp.NbPat+RightComp.NbPat;

      dx=RightComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
          (((RightComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
              CPattern.OUTPUT_RIGHT_OFFSET)-RightComp.OutputX)%2);

      PatTab[pat0]=new CPattern(dx, RightComp.OutputY-7,
            CPattern.P_GUN30R,false,false);
      PatTab[pat0].SetPattern();

      OutputX=PatTab[pat0].OutputX;
      OutputY=PatTab[pat0].OutputY;
      OutpDirection=PatTab[pat0].OutputDir;

      dx=LeftComp.MaxX+CompOffset+(1*
          (((LeftComp.MaxX+CompOffset)+
            CPattern.OUTPUT_LEFT_OFFSET)-LeftComp.OutputX)%2);

      PatTab[pat0+1]=new CPattern(dx,RightComp.OutputY-7+0//1
                ,CPattern.P_GUN30R,true,false);
      PatTab[pat0+1].SetPattern();

      int eat1x=Math.abs(PatTab[pat0+1].OutputX-PatTab[pat0].OutputX)/2+
                    PatTab[pat0].OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 0;
      eat1x -= 6;
      eat1x += OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        PatTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,false,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        PatTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_NIL,false,false);
      PatTab[pat0+2].SetPattern();

      eat1x=Math.abs(PatTab[pat0+1].OutputX-RightComp.OutputX)/2+
                    RightComp.OutputX;
      eat1y=RightComp.OutputY+(Math.abs(RightComp.OutputX-eat1x));
      eat1y += 1;
      eat1x += 4;
      eat1y += 4;
      PatTab[pat0+3]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,false,false);
      PatTab[pat0+3].SetPattern();

      MinX=PatTab[pat0].x;
      MinY=LeftComp.MinY;
      MaxX=PatTab[pat0+1].x+PatTab[pat0+1].NbCols;
      MaxY=PatTab[pat0+2].y+PatTab[pat0+2].NbLines;
    }
    // Left Output
    else {
      dx=LeftComp.MaxX+CompOffset+1+(1*
          (((LeftComp.MaxX+CompOffset+1)+
            Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);

      RightComp.SetPos(dx, RightComp.y);
      for(int i=0;i<RightComp.NbPat;i++) {
        PatTab[LeftComp.NbPat+i]=RightComp.PatTab[i];
      }
      Entry2X=RightComp.OutputX;
      Entry2Y=RightComp.OutputY;

      int pat0=LeftComp.NbPat+RightComp.NbPat;
      dx=RightComp.MaxX+CompOffset+(1*
          (((RightComp.MaxX+CompOffset)+
            CPattern.OUTPUT_LEFT_OFFSET)-RightComp.OutputX)%2);
      PatTab[pat0]=new CPattern(dx, RightComp.OutputY-7,
            CPattern.P_GUN30R,true,false);
      PatTab[pat0].SetPattern();

      OutputX=PatTab[pat0].OutputX;
      OutputY=PatTab[pat0].OutputY;
      OutpDirection=PatTab[pat0].OutputDir;

      dx=LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
          (((LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
              CPattern.OUTPUT_RIGHT_OFFSET)-LeftComp.OutputX)%2);

      PatTab[pat0+1]=new CPattern(dx,RightComp.OutputY-7+0//1
                ,CPattern.P_GUN30R,false,false);
      PatTab[pat0+1].SetPattern();

      int eat1x=Math.abs(PatTab[pat0].OutputX-PatTab[pat0+1].OutputX)/2+
                  PatTab[pat0+1].OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 5;
      eat1x -= OutputOffset;
      eat1y += OutputOffset;
      if(last) {
        PatTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,true,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        PatTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_NIL,true,false);
      PatTab[pat0+2].SetPattern();

      eat1x=Math.abs(RightComp.OutputX-PatTab[pat0+1].OutputX)/2+
                          PatTab[pat0+1].OutputX;
      eat1y=RightComp.OutputY+(Math.abs(RightComp.OutputX-eat1x));
      eat1x -= 2;
      eat1x -= 4;
      eat1y += 4;
      PatTab[pat0+3]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,true,false);
      PatTab[pat0+3].SetPattern();

      MinX=PatTab[pat0+1].x;
      MinY=LeftComp.MinY;
      MaxX=PatTab[pat0].x+PatTab[pat0].NbCols;
      MaxY=PatTab[pat0+2].y+PatTab[pat0+2].NbLines;
    }
    return(true);
  }

  /** Set component pos */
  private void SetPos(int px, int py) {
    MoveComp(px-MinX, py-MinY);
  }

  /** Move a component with offset */
  private void MoveComp(int dx, int dy) {
    for(int i=0;i<NbPat;i++)
      PatTab[i].SetPattern(dx, dy);
    Entry1X += dx;
    Entry1Y += dy;
    Entry2X += dx;
    Entry2Y += dy;
    MaxX += dx;
    MaxY += dy;
    MinX += dx;
    MinY += dy;
    OutputX += dx;
    OutputY += dy;
    x += dx;
    y += dy;
    ExitX +=dx;
    ExitY += dy;
  }

}
