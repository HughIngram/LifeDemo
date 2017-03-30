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

import uk.co.hughingram.lifedemo.model.logicell.jputil.JPParser;

/** Manages logical components : A template is a components organisation. */
public class CLogiComp {

  /** # of patterns in the component */
  int nbPat;
  /** Patterns tab : the patterns list of the component */
  CPattern patTab[];
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
    nbPat =1;
    patTab =new CPattern[nbPat];
    patTab[0]=new CPattern(x,y,pat,false,false);
    patTab[0].SetPattern();
    Entry1X= patTab[0].OutputX;
    Entry1Y= patTab[0].OutputY;
    Entry2X= patTab[0].OutputX;
    Entry2Y= patTab[0].OutputY;
    OutputX= patTab[0].OutputX;
    OutputY= patTab[0].OutputY;
    OutpDirection= patTab[0].OutputDir;
    MinX=x;
    MinY=y;
    MaxX=MinX+ patTab[0].NbCols;
    MaxY=MinY+ patTab[0].NbLines;
    Valid=true;
  }


  /** Construct a single entry component with direction */
  public CLogiComp(boolean entry, boolean dir) {
    dir= !dir;
    x=y=0;
    nbPat =1;
    patTab =new CPattern[nbPat];
    if(entry)
      patTab[0]=new CPattern(x,y,CPattern.P_ENTRYRT,dir,false);
    else
      patTab[0]=new CPattern(x,y,CPattern.P_ENTRYRF,dir,false);
    patTab[0].SetPattern();

    Entry1X= patTab[0].OutputX;
    Entry1Y= patTab[0].OutputY;
    Entry2X= patTab[0].OutputX;
    Entry2Y= patTab[0].OutputY;
    OutputX= patTab[0].OutputX;
    OutputY= patTab[0].OutputY;
    OutpDirection= patTab[0].OutputDir;

    MinX=x;
    MinY=y;
    MaxX=MinX+ patTab[0].NbCols;
    MaxY=MinY+ patTab[0].NbLines;
    Valid=true;
  }

  /** Construct a unary operator component */
  public CLogiComp(char op, CLogiComp Father, boolean last) {
    if(op!=JPParser.OP_NOT) {
      Valid=false;
      return;
    }
    nbPat =2+Father.nbPat;
    patTab =new CPattern[nbPat];
    Father.MoveComp(0,0);
    for(int i = 0; i<Father.nbPat; i++) {
      patTab[i]=Father.patTab[i];
    }

    // Right Output
    if(Father.OutpDirection==CLogiTemplate.RIGHT) {
      int dx;
      // dx has to be even
      dx=Father.MaxX+CompOffset+1+(1*
              ((Father.MaxX+CompOffset+1+CPattern.OUTPUT_LEFT_OFFSET)-
                      Father.OutputX)%2);
      patTab[Father.nbPat +0]=new CPattern(dx, Father.OutputY-CPattern.ENTRY_OPYOFS+0,//1,
              CPattern.P_GUN30R,true,false);
      patTab[Father.nbPat +0].SetPattern();
      Entry1X=Father.OutputX;
      Entry1Y=Father.OutputY;
      Entry2X=Father.OutputX;
      Entry2Y=Father.OutputY;
      OutputX= patTab[Father.nbPat +0].OutputX;
      OutputY= patTab[Father.nbPat +0].OutputY;
      OutpDirection= patTab[Father.nbPat +0].OutputDir;
      int eat1x=Math.abs(patTab[Father.nbPat +0].OutputX-Father.OutputX)/2+
              Father.OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 5;
      eat1x -= OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        patTab[Father.nbPat +1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,true,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        patTab[Father.nbPat +1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,true,false);
      patTab[Father.nbPat +1].SetPattern();
      MinX=Father.MinX;
      MinY=Father.MinY;
      MaxX= patTab[Father.nbPat +0].x+ patTab[Father.nbPat +0].NbCols;
      MaxY= patTab[Father.nbPat +1].y+ patTab[Father.nbPat +1].NbLines;
    }
    // Left Output
    else {
      int dx;
      // dx has to be even
      dx=Father.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
              (((Father.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
                      CPattern.OUTPUT_RIGHT_OFFSET)-Father.OutputX)%2);

      patTab[Father.nbPat +0]=new CPattern(dx, Father.OutputY-CPattern.ENTRY_OPYOFS+0,
              CPattern.P_GUN30R,false,false);
      patTab[Father.nbPat +0].SetPattern();
      Entry1X=Father.OutputX;
      Entry1Y=Father.OutputY;
      Entry2X=Father.OutputX;
      Entry2Y=Father.OutputY;
      OutputX= patTab[Father.nbPat +0].OutputX;
      OutputY= patTab[Father.nbPat +0].OutputY;
      OutpDirection= patTab[Father.nbPat +0].OutputDir;

      int eat1x=Math.abs(patTab[Father.nbPat +0].OutputX-Father.OutputX)/2+
              patTab[Father.nbPat +0].OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 6;
      eat1x += OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        patTab[Father.nbPat +1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,false,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        patTab[Father.nbPat +1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,false,false);
      patTab[Father.nbPat +1].SetPattern();
      MinX= patTab[Father.nbPat +0].x;
      MinY=Father.MinY;
      MaxX=Father.MaxX;
      MaxY= patTab[Father.nbPat +1].y+ patTab[Father.nbPat +1].NbLines;
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
    nbPat =LeftComp.nbPat +RightComp.nbPat +3;
    patTab =new CPattern[nbPat];
    for(int i = 0; i<LeftComp.nbPat; i++) {
      patTab[i]=LeftComp.patTab[i];
    }
    int dx;
    // Right output
    if(LeftComp.OutpDirection==CLogiTemplate.RIGHT) {
      dx=LeftComp.MinX-Math.abs(RightComp.MaxX-RightComp.MinX)-CompOffset-1+(1*
              (((LeftComp.MinX-Math.abs(RightComp.MaxX-RightComp.MinX)-CompOffset-1)+
                      Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);

      RightComp.SetPos(dx, RightComp.y);
      for(int i = 0; i<RightComp.nbPat; i++) {
        patTab[LeftComp.nbPat +i]=RightComp.patTab[i];
      }
      int pat0=LeftComp.nbPat +RightComp.nbPat;
      dx=LeftComp.MaxX+CompOffset+1+(1*
              ((LeftComp.MaxX+CompOffset+1+CPattern.OUTPUT_LEFT_OFFSET)-
                      LeftComp.OutputX)%2);


      patTab[pat0]=new CPattern(dx, LeftComp.OutputY-7+0,//1,
              CPattern.P_GUN30R,true,false);
      patTab[pat0].SetPattern();
      Entry1X=RightComp.OutputX;
      Entry1Y=RightComp.OutputY;
      Entry2X=LeftComp.OutputX;
      Entry2Y=LeftComp.OutputY;
      OutputX=RightComp.OutputX;
      OutputY=RightComp.OutputY;
      OutpDirection=RightComp.OutpDirection;

      int eat1x=Math.abs(patTab[pat0].OutputX-RightComp.OutputX)/2+
              RightComp.OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 6;
      eat1x += OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        patTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,false,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        patTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,false,false);
      patTab[pat0+1].SetPattern();

      eat1x= patTab[pat0].OutputX-(eat1y- patTab[pat0].OutputY);
      eat1x -= 2;
      patTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,true,false);
      patTab[pat0+2].SetPattern();

      MinX=RightComp.MinX;
      MinY=RightComp.MinY;
      MaxX= patTab[pat0].x+ patTab[pat0].NbCols;
      MaxY= patTab[pat0+1].y+ patTab[pat0+1].NbLines;
    }
    // Left output
    else {
      dx=LeftComp.MaxX+CompOffset+(1*
              (((LeftComp.MaxX+CompOffset)+
                      Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);

      RightComp.SetPos(dx, RightComp.y);
      for(int i = 0; i<RightComp.nbPat; i++)
        patTab[LeftComp.nbPat +i]=RightComp.patTab[i];
      int pat0=LeftComp.nbPat +RightComp.nbPat;
      dx=LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
              (((LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
                      CPattern.OUTPUT_RIGHT_OFFSET)-LeftComp.OutputX)%2);

      patTab[pat0]=new CPattern(dx, RightComp.OutputY-7+0,//1,
              CPattern.P_GUN30R,false,false);
      patTab[pat0].SetPattern();
      Entry1X=LeftComp.OutputX;
      Entry1Y=LeftComp.OutputY;
      Entry2X=RightComp.OutputX;
      Entry2Y=RightComp.OutputY;
      OutputX=RightComp.OutputX;
      OutputY=RightComp.OutputY;
      OutpDirection=RightComp.OutpDirection;

      int eat1x=Math.abs(RightComp.OutputX- patTab[pat0].OutputX)/2+
              patTab[pat0].OutputX;

      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 5;
      eat1x -= OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        patTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,true,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        patTab[pat0+1]=new CPattern(eat1x,eat1y,CPattern.P_NIL,true,false);
      patTab[pat0+1].SetPattern();

      eat1x= patTab[pat0].OutputX+(eat1y- patTab[pat0].OutputY);
      eat1x -= 1;
      patTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,false,false);
      patTab[pat0+2].SetPattern();
      MinX= patTab[pat0].x;
      MinY=RightComp.MinY;
      MaxX=RightComp.MaxX;
      MaxY= patTab[pat0+2].y+ patTab[pat0+2].NbLines;
    }
    return(true);
  }

  /** Construction of an OR component */
  private boolean MakeOrComp(CLogiComp LeftComp, CLogiComp RightComp, boolean last) {
    nbPat =LeftComp.nbPat +RightComp.nbPat +4;
    patTab =new CPattern[nbPat];
    for(int i = 0; i<LeftComp.nbPat; i++) {
      patTab[i]=LeftComp.patTab[i];
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
      for(int i = 0; i<RightComp.nbPat; i++) {
        patTab[LeftComp.nbPat +i]=RightComp.patTab[i];
      }
      Entry2X=RightComp.OutputX;
      Entry2Y=RightComp.OutputY;

      int pat0=LeftComp.nbPat +RightComp.nbPat;

      dx=RightComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
              (((RightComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
                      CPattern.OUTPUT_RIGHT_OFFSET)-RightComp.OutputX)%2);

      patTab[pat0]=new CPattern(dx, RightComp.OutputY-7,
              CPattern.P_GUN30R,false,false);
      patTab[pat0].SetPattern();

      OutputX= patTab[pat0].OutputX;
      OutputY= patTab[pat0].OutputY;
      OutpDirection= patTab[pat0].OutputDir;

      dx=LeftComp.MaxX+CompOffset+(1*
              (((LeftComp.MaxX+CompOffset)+
                      CPattern.OUTPUT_LEFT_OFFSET)-LeftComp.OutputX)%2);

      patTab[pat0+1]=new CPattern(dx,RightComp.OutputY-7+0//1
              ,CPattern.P_GUN30R,true,false);
      patTab[pat0+1].SetPattern();

      int eat1x=Math.abs(patTab[pat0+1].OutputX- patTab[pat0].OutputX)/2+
              patTab[pat0].OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 0;
      eat1x -= 6;
      eat1x += OutputOffset;
      eat1y += OutputOffset;

      if(last) {
        patTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,false,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        patTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_NIL,false,false);
      patTab[pat0+2].SetPattern();

      eat1x=Math.abs(patTab[pat0+1].OutputX-RightComp.OutputX)/2+
              RightComp.OutputX;
      eat1y=RightComp.OutputY+(Math.abs(RightComp.OutputX-eat1x));
      eat1y += 1;
      eat1x += 4;
      eat1y += 4;
      patTab[pat0+3]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,false,false);
      patTab[pat0+3].SetPattern();

      MinX= patTab[pat0].x;
      MinY=LeftComp.MinY;
      MaxX= patTab[pat0+1].x+ patTab[pat0+1].NbCols;
      MaxY= patTab[pat0+2].y+ patTab[pat0+2].NbLines;
    }
    // Left Output
    else {
      dx=LeftComp.MaxX+CompOffset+1+(1*
              (((LeftComp.MaxX+CompOffset+1)+
                      Math.abs(RightComp.OutputX-RightComp.MinX))-LeftComp.OutputX)%2);

      RightComp.SetPos(dx, RightComp.y);
      for(int i = 0; i<RightComp.nbPat; i++) {
        patTab[LeftComp.nbPat +i]=RightComp.patTab[i];
      }
      Entry2X=RightComp.OutputX;
      Entry2Y=RightComp.OutputY;

      int pat0=LeftComp.nbPat +RightComp.nbPat;
      dx=RightComp.MaxX+CompOffset+(1*
              (((RightComp.MaxX+CompOffset)+
                      CPattern.OUTPUT_LEFT_OFFSET)-RightComp.OutputX)%2);
      patTab[pat0]=new CPattern(dx, RightComp.OutputY-7,
              CPattern.P_GUN30R,true,false);
      patTab[pat0].SetPattern();

      OutputX= patTab[pat0].OutputX;
      OutputY= patTab[pat0].OutputY;
      OutpDirection= patTab[pat0].OutputDir;

      dx=LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1+(1*
              (((LeftComp.MinX-CPattern.ENTRY_WIDTH-CompOffset-1)+
                      CPattern.OUTPUT_RIGHT_OFFSET)-LeftComp.OutputX)%2);

      patTab[pat0+1]=new CPattern(dx,RightComp.OutputY-7+0//1
              ,CPattern.P_GUN30R,false,false);
      patTab[pat0+1].SetPattern();

      int eat1x=Math.abs(patTab[pat0].OutputX- patTab[pat0+1].OutputX)/2+
              patTab[pat0+1].OutputX;
      int eat1y=OutputY+(Math.abs(OutputX-eat1x));
      eat1x -= 5;
      eat1x -= OutputOffset;
      eat1y += OutputOffset;
      if(last) {
        patTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_OUTPUT,true,false);
        ExitX=(eat1y-OutputY)+OutputX-1;
        ExitY=eat1y;
      }
      else
        patTab[pat0+2]=new CPattern(eat1x,eat1y,CPattern.P_NIL,true,false);
      patTab[pat0+2].SetPattern();

      eat1x=Math.abs(RightComp.OutputX- patTab[pat0+1].OutputX)/2+
              patTab[pat0+1].OutputX;
      eat1y=RightComp.OutputY+(Math.abs(RightComp.OutputX-eat1x));
      eat1x -= 2;
      eat1x -= 4;
      eat1y += 4;
      patTab[pat0+3]=new CPattern(eat1x,eat1y,CPattern.P_EATERR,true,false);
      patTab[pat0+3].SetPattern();

      MinX= patTab[pat0+1].x;
      MinY=LeftComp.MinY;
      MaxX= patTab[pat0].x+ patTab[pat0].NbCols;
      MaxY= patTab[pat0+2].y+ patTab[pat0+2].NbLines;
    }
    return(true);
  }

  /** Set component pos */
  private void SetPos(int px, int py) {
    MoveComp(px-MinX, py-MinY);
  }

  /** Move a component with offset */
  private void MoveComp(int dx, int dy) {
    for(int i = 0; i< nbPat; i++)
      patTab[i].SetPattern(dx, dy);
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
