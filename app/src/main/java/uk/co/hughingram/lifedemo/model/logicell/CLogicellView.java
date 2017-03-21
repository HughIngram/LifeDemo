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
import java.awt.event.*;

/** Manages CA display */
public class CLogicellView extends JPView {

  /** Size of a cell */
  int HorizStep, VerticStep;
  /** Size of a cell (double) */
  double HorizStepD, VerticStepD;

  /** pointer to the Universe */
  private CLogicellUniverse MyUniverse;
  /** Number of cells place on the view */
  private int NbHorizPlace, NbVerticPlace;
  /** Zooming factors */
  private double ZoomFactX, ZoomFactY;

  /** Construct a LogicellView with background color */
  public CLogicellView(CLogicellUniverse univ,Color c) {
      super(c);
      MyUniverse=univ;
      NbHorizPlace=480;
      NbVerticPlace=480;
  }

  /** Paint View Background */
  void PaintBackground() {
    ClearBkg();
    // draws a 8 cells steps gris
/*
    if(MyUniverse.GetMode()==CLogicellUniverse.M_CONWAY) {
      Color colcrt=GrBuff.getColor();
      GrBuff.setColor(getBackground());
      GrBuff.fillRect(0,0,Width,Height);
      GrBuff.setColor(colcrt);
      GrBuff.drawRect(0,Width,Height,0);
      for(int i=1;i<NbHorizPlace;i++) {
        GrBuff.setColor(i%8==0 ? Color.red : Color.black);// .blue);
        GrBuff.drawLine((int)(i*(HorizStepD)),0,(int)(i*(HorizStepD)),Height);
      }
      for(int i=1;i<NbVerticPlace;i++) {
        GrBuff.setColor(i%8==0 ? Color.red : Color.black);// .blue);
        GrBuff.drawLine(0,(int)(i*(VerticStepD)),Width,(int)(i*(VerticStepD)));
      }
      GrBuff.setColor(colcrt);
    }
    */
  }

  /** Resize Graphic Buffer and Steps */
  public void ResizeGrBuff() {
    super.ResizeGrBuff();
    SetSteps();
  }
  /** Display Equations */
  void DispEquation(CLogiTemplate lt) {
    double ltw=(double)lt.ctWidth*ZoomFactX/3;
    // Display user Entry
    int cs=Math.min(Math.max((int)ltw/lt.entryU.length(),12),22);
    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.PLAIN, cs));
    FontMetrics fm=getFontMetrics(GrBuff.getFont());
    int posx=(int) (lt.x*ZoomFactX) +
              (int) ( ((lt.ctWidth+lt.x-lt.x)*ZoomFactX-fm.stringWidth(lt.entryU))/2 );
    int posy=(int) (lt.y*ZoomFactY) +
              (int) (lt.ctHeight*ZoomFactY/2) ;
    GrBuff.drawString(lt.entryU, posx, posy);
    if(lt.entryU!=lt.entry) {
    // Display processed entry
    cs=Math.min(Math.max((int)ltw/lt.entry.length(),12),22);
    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.PLAIN, cs));
    fm=getFontMetrics(GrBuff.getFont());
    posx=(int) (lt.x*ZoomFactX) +
          (int) ( ((lt.ctWidth+lt.x-lt.x)*ZoomFactX-fm.stringWidth(lt.entry))/2 );
    posy=(int) (lt.y*ZoomFactY) +
          (int) (lt.ctHeight*ZoomFactY/2) + (int)(cs*1.5);
    GrBuff.drawString(lt.entry, posx, posy);
    }
    repaint();
  }

  /** Display background text */
  void DispBkgText() {
    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.BOLD,20));
    this.WriteCenterX(MyUniverse.app.MotherApplet.GetName()+" "+
          MyUniverse.app.MotherApplet.GetVersion() ,25);
    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.BOLD,10));
    this.WriteCenterX(MyUniverse.app.MotherApplet.GetCopyright(),45);
    this.WriteCenterX("www.rennard.org/alife/, alife@rennard.org",60);

    // It's really not the right place for that ! I know.
    // but I think I'm a bit tired today  ;=)
    GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.PLAIN,12));
    if(MyUniverse.app.MotherApplet.Language==Logicell.LANGEN) {
      WriteCenterX("Logicell shows the capacity of a Conway cellular automata to manage boolean operators.",90);
      WriteCenterX("Equation mode ensures you to test any 4 entries boolean equation.",120);
      WriteCenterX("Some combinatorial applications shows the usefulness of those functions.",150);
      WriteCenterX("A last, since it is a pure Conway, some classical patterns can be tested.",180);
      WriteCenterX("The notation is the classical :",220);
      WriteCenterX("And : ' ^ ', Or : ' v ', Not : ' ~ ', Xor : ' w '", 240);
      GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.BOLD,14));
      WriteCenterX("To begin, select a working mode",280);
      WriteCenterX("in the upper toolbar.",295);
    }
    else {
      WriteCenterX("Logicell montre la capacité d'un automate cellulaire de type Conway,",90);
      WriteCenterX("à gérer l'ensemble des opérations booléennes.",105);
      WriteCenterX("Le mode Equation permet de tester théoriquement n'importe quelle équation",135);
      WriteCenterX("booléennes à 4 entrées. Celles-ci pouvant être répétées autant que nécessaire.",150);
      WriteCenterX("Quelques applications combinatoires montrent l'usage qui peut-être fait de ces fonctions.",180);
      WriteCenterX("Enfin, comme il s'agit d'un Conway pur, certaines structures classiques peuvent être testées.",210);
      WriteCenterX("La notation est classique : Et : ' ^ ', Ou : ' v ', Non : ' ~ ', Xor : ' w '",250);
      GrBuff.setFont(new Font(GrBuff.getFont().getName(), Font.BOLD,14));
      WriteCenterX("Pour démarrer, sélectionnez un mode",290);
      WriteCenterX("dans la barre du haut.",305);
    }
  }

  /** Modify Grid size (Nb cells) by a factor z*/
  void SetGridSize(float z) {
    NbHorizPlace /= z;
    NbVerticPlace /= z;
    ResizeGrBuff();
  }
  /** Set Grid size (Nb cells) */
  void SetGridSize(int h, int w) {
    NbHorizPlace = h;
    NbVerticPlace = w;
    ZoomFactX=(double)Width/(double)h;
    ZoomFactY=(double)Height/(double)w;
    ResizeGrBuff();
  }
  /** Set Best fit Grid size (Nb cells) */
  void setBestGridSize() {
    NbHorizPlace=MyUniverse.PatWidth;
    NbVerticPlace=MyUniverse.PatWidth;
    ResizeGrBuff();    
  }
  /** Mouse Down : Display pos (unused) */
  public boolean mouseDown(Event e,int x,int y) {
//      MyUniverse.SetPosText(((int)((double)x/HorizStepD)+1),((int)((double)y/VerticStepD)+1));
      return(false);
  }

  /** Set the grid steps */
  private void SetSteps() {
    HorizStep=Width/NbHorizPlace;
    VerticStep=Height/NbVerticPlace;
    HorizStepD=(double)Width/(double)NbHorizPlace;
    VerticStepD=(double)Height/(double)NbVerticPlace;
  }
  
}
