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
import java.awt.*;
// For 7 segments digit display, segments are :
//             A          -
//            B C        | |
//             D          -
//            E F        | |
//             G          -



/** Output display management */
public class COpDispView extends JPView {

  /** point to the Universe */
  private CLogicellUniverse MyUniverse;

  public COpDispView(CLogicellUniverse univ,Color c) {
      super(c);
      MyUniverse=univ;
  }

  /** Paint View Background */
  void PaintBackground() {
    ResizeGrBuff();
    ClearBkg();
  }
  /** Draw Ouptput according to mode */
  void DrawOutput(int m) {
    PaintBackground();
    switch(m) {
      case CLogicellUniverse.M_DIGIT2 :
        DrawDigit();
      break;
      case CLogicellUniverse.M_DIGIT :
        DrawReducedDigit(MyUniverse.EntryBinVal);
      break;
      case CLogicellUniverse.M_LIGHT :
        DrawLight();
      break;
      case CLogicellUniverse.M_BINADD :
        DrawBinAdd();
      break;
      case CLogicellUniverse.M_BINADD2 :
        DrawBinAdd2();
      break;
      case CLogicellUniverse.M_CONWAY :
        DrawConway();
      break;
      default :
        DrawSimpleOutput();
    }
  }

  /** Init Light with LightOff */
  void InitDrawLight() {
    ImgBuff=MyUniverse.App.MotherApplet.ImConway;// .ImLightOff;
    this.getGraphics().drawImage(ImgBuff,0,0,Width,Height,this);
  }

  /** Draw Conway image */
  void DrawConway() {
    ImgBuff=MyUniverse.App.MotherApplet.ImConway;
    this.getGraphics().drawImage(ImgBuff,0,0,Width,Height,this);
  }


  /** Draw simple output */
  private void DrawSimpleOutput() {
    if(MyUniverse.GetMode()!=CLogicellUniverse.M_EQUATION)
      return;
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_NONE)
      return;
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_FALSE)
      GrBuff.setColor(Color.black);
    else
      GrBuff.setColor(Color.white);
    GrBuff.fillArc(2, 2, Width-4, Height-4,0,360);
  }

  /** Draw Light (two-way switch) */
  private void DrawLight() {
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_TRUE)
      ImgBuff=MyUniverse.App.MotherApplet.ImLightOn;
    else if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_FALSE)
      ImgBuff=MyUniverse.App.MotherApplet.ImLightOff;
    this.getGraphics().drawImage(ImgBuff,0,0,Width , Height,this);
  }

  /** Draw BinAdd */
  private void DrawBinAdd() {
    ClearBkg();
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_NONE ||
          MyUniverse.GetLtOutputState(1)==CLogiTemplate.LT_NONE)
      return;
    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.BOLD,(int)(Width*.25)));
    String res="";
    int sum=0;
    if(MyUniverse.GetLtOutputState(1)==CLogiTemplate.LT_TRUE)
      { res +="1"; sum +=2; }
    else
      res +="0";
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_TRUE)
      { res +="1"; sum += 1; }
    else
      res +="0";
    res += "b="+sum;
    WriteCenterX(res, (int)(0.7*Height));
  }

  /** Draw BinAdd2 */
  private void DrawBinAdd2() {
    ClearBkg();
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_NONE ||
          MyUniverse.GetLtOutputState(1)==CLogiTemplate.LT_NONE ||
          MyUniverse.GetLtOutputState(2)==CLogiTemplate.LT_NONE)
      return;

    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.BOLD,(int)(Height*.25)));
    String res="";
    int sum=0;
    // r1
    if(MyUniverse.GetLtOutputState(1)==CLogiTemplate.LT_TRUE)
      { res +="1"; sum += 4; }
    else
      res +="0";
    // s1
    if(MyUniverse.GetLtOutputState(2)==CLogiTemplate.LT_TRUE)
      { res +="1"; sum += 2; }
    else
      res +="0";
    // s0
    if(MyUniverse.GetLtOutputState(0)==CLogiTemplate.LT_TRUE)
      { res +="1"; sum += 1; }
    else
      res +="0";

    res += "="+sum;
    FontMetrics fm=GrBuff.getFontMetrics();
    int sw=fm.stringWidth(res);
    int h=fm.getHeight();
    GrBuff.drawString(res,Width/2-(sw/2),(int)(0.7*Height));
  }

  /** Draw a digit with B, D and G segments known */
  private void DrawReducedDigit(int v) {
    ClearBkg();

    // set B segment
    if(v==0 || v==4 || v==5 || v==6 || v==8 || v==9)
      DrawDigitSegment('B',CLogiTemplate.LT_TRUE);
    else
      DrawDigitSegment('B',CLogiTemplate.LT_FALSE);
    // set D segment
    if(!(v==0 || v==1 || v==7) & v!=-1)
      DrawDigitSegment('D',CLogiTemplate.LT_TRUE);
    else
      DrawDigitSegment('D',CLogiTemplate.LT_FALSE);
    // set G segment
    if(v==0 || v==2 || v==3 || v==5 || v==6 || v==8 || v==9)
      DrawDigitSegment('G',CLogiTemplate.LT_TRUE);
    else
      DrawDigitSegment('G',CLogiTemplate.LT_FALSE);

    // Led A
    DrawDigitSegment('A',MyUniverse.GetLtOutputState(0));
    // Led E
    DrawDigitSegment('E',MyUniverse.GetLtOutputState(1));
    // Led C
    DrawDigitSegment('C',MyUniverse.GetLtOutputState(2));
    // Led F
    DrawDigitSegment('F',MyUniverse.GetLtOutputState(3));
  }

  /** Draw Digit */
  private void DrawDigit() {
    ClearBkg();
    // Led A
    DrawDigitSegment('A',MyUniverse.GetLtOutputState(0));
    // Led C
    DrawDigitSegment('C',MyUniverse.GetLtOutputState(1));
    // Led F
    DrawDigitSegment('F',MyUniverse.GetLtOutputState(2));
    // Led D
    DrawDigitSegment('D',MyUniverse.GetLtOutputState(3));
    // Led B
    DrawDigitSegment('B',MyUniverse.GetLtOutputState(4));
    // Led G
    DrawDigitSegment('G',MyUniverse.GetLtOutputState(5));
    // Led E
    DrawDigitSegment('E',MyUniverse.GetLtOutputState(6));
  }


  /** Resize Graphic Buffer and Steps */
  public void ResizeGrBuff() {
    if(MyUniverse.GetMode()==CLogicellUniverse.M_LIGHT)
      SetDim();
    else
      super.ResizeGrBuff();
  }


  /** Draw a digit segment */
  private void DrawDigitSegment(char s, int v) {

    int LineW=2;
    int LineL=(int)(Width*0.4);
    int LineH=(int)(Height*0.2);
    int DeltaV=2;
    int DeltaH=2;
    if(v==CLogiTemplate.LT_TRUE)
      GrBuff.setColor(Color.white);
    else
      GrBuff.setColor(Color.black);
    switch(s) {
      case 'A' :
        GrBuff.fillRect((Width-LineL)/2, (Height-2*LineH-2*LineW)/2, LineL, LineW);
      break;
      case 'B' :
        GrBuff.fillRect((Width-LineL)/2-DeltaH, (Height-2*LineH-2*LineW)/2+DeltaV, LineW, LineH);
      break;
      case 'C' :
        GrBuff.fillRect((Width-LineL)/2+LineL, (Height-2*LineH-2*LineW)/2+DeltaV, LineW, LineH);
      break;
      case 'D' :
        GrBuff.fillRect((Width-LineL)/2, (Height-2*LineH-2*LineW)/2+DeltaV+LineH , LineL, LineW);
      break;
      case 'E' :
        GrBuff.fillRect((Width-LineL)/2-DeltaH, (Height-2*LineH-2*LineW)/2+DeltaV+LineH+DeltaV, LineW, LineH);
      break;
      case 'F' :
        GrBuff.fillRect((Width-LineL)/2+LineL, (Height-2*LineH-2*LineW)/2+DeltaV+LineH+DeltaV, LineW, LineH);
      break;
      case 'G' :
        GrBuff.fillRect((Width-LineL)/2, (Height-2*LineH-2*LineW)/2+DeltaV+LineH+DeltaV+LineH, LineL, LineW);
      break;
    }
  }

}