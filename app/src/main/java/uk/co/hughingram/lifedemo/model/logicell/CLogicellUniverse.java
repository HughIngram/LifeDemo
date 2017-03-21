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
import java.util.*;

/** This class is the Universe of Logicell, the place where everything happens. */
public class CLogicellUniverse implements Runnable {


  /** Maximum entries, in fact the algorithm is not limited. */ 
  static final int MAXENTRY=4;
  static final int DefaultSpeed=0;
  static final int DefaultDisplayStep=5;
  static final char FirstEntry='A';
  /** Working mode */
  static final int M_INIT=-1;
  static final int M_EQUATION=0;
  static final int M_CONWAY=1;
  static final int M_LIGHT=2;
  static final int M_DIGIT=3;
  static final int M_DIGIT2=4;
  static final int M_BINADD=5;
  static final int M_BINADD2=6;

  // Interface
  /** Pointer to App */
  CLogicellUI App;
  /** Pattern size x */
  int PatHeight=480;
  /** Pattern size y */
  int PatWidth=480;
  /** View */
  CLogicellView MainView;
  /** Output View */
  COpDispView OpDispView;
  /** Thread */
  Thread LogicellUnivThread;
  /** Value of bin entry in digit mode */
  int EntryBinVal=-1;
  /** Running flag */
  boolean IsRunning=false;
  /** Iteration delay */
  int Speed=DefaultSpeed;

long Time1, TimeCalc, TimeDisp, TimePoint, TimeAdd;

  /** Remove empty block every # generation. Woks only in Conway mode. */
  private final int RemoveEmptyGen=5;
  /** Speed offset used for speed modification. */
  private final int SpeedOffset=10;
  /** Lookup table */
  private boolean LookUp[];
  /** Contains the current cells blocks. Just a pointer */
  private Vector CellsBlocs;
  /** Contains the working list. Just a pointer */
  private Vector BlocTmp;
  /** 2 lists of blocks */
  private Vector Blocks1, Blocks2;
  /** A pattern */
  private CPattern Pattern;
  /** Logical structure e.d. a list of templates (equations)
  representing a problem */
  private CLogiTemplate[] LogiTempl;
  /** Current templates OutputStates. Used to display output only
  when a state changes. */
  private int[] LTOutput;
  /** Template # in a problem */
  private int NbTemplate;
  /** Mode */
  private int Mode=M_INIT;
  private boolean SolFound=false;
  private int Generation=0;
  /** Display only each x generations */
  private int DisplayStep=DefaultDisplayStep;

  /** Constructor */
  public CLogicellUniverse(CLogicellUI lu)  {
    App=lu;
    LookUp=new boolean[512];
    InitLookUp();
    MainView=new CLogicellView(this,Color.black);
    OpDispView=new COpDispView(this, Color.blue);
    Blocks1=new Vector();
    Blocks2=new Vector();
    Mode=M_INIT;
    InitWorld();
  }

  /** Init the world */
  void InitWorld() {
    CellsBlocs=Blocks1;
  }

  /** Init View */
  public void InitView() {
    MainView.ResizeGrBuff();
    MainView.PaintBackground();
    OpDispView.ResizeGrBuff();
    OpDispView.PaintBackground();
  }

  /** Launch thread */
  public void run() {
    Thread thisThread=Thread.currentThread();
    IsRunning=true;
    while(LogicellUnivThread==thisThread) {
     Go();
     try {
        Thread.sleep(Speed);
      } catch (InterruptedException e) { }
    } // while
  }

  /** Start the world */
  void Launch() {
      if(LogicellUnivThread==null)
        LogicellUnivThread=new Thread(this);
      Time1=System.currentTimeMillis();
      IsRunning=true;
      SolFound=false;
      LogicellUnivThread.start();
  }

  /** Construct a Logical Problem e.d. a combination of templates. */
  public void GenLogiProblem(String templ[], int st, boolean entries[], int se) {
    NbTemplate=st;
    LogiTempl=null;
    LogiTempl=new CLogiTemplate[NbTemplate];
    LTOutput=new int[NbTemplate];
    // compute the value of the entry
    if(Mode==M_DIGIT || Mode==M_DIGIT2) {
      EntryBinVal=0;
      EntryBinVal += (entries[0] ? 1 : 0);
      EntryBinVal += (entries[1] ? 2 : 0);
      EntryBinVal += (entries[2] ? 4 : 0);
      EntryBinVal += (entries[3] ? 8 : 0);
    }
    else
      EntryBinVal=-1;
    if(Mode!=M_LIGHT)
      OpDispView.ResizeGrBuff();
    OpDispView.PaintBackground();
    OpDispView.repaint();
    Generation=0;
    // Template's Position management
    int tx=0;
    int ty=0;
    Blocks1.removeAllElements();
    Blocks2.removeAllElements();
    int ycrt=0;
    int my=-8;
    int mx=0;
    for(int i=0; i<st; i++) {
      LogiTempl[i]=new CLogiTemplate(this, templ[i], entries, tx, ycrt);
      if( ((Mode==M_DIGIT2 && (i==2 || i==4)) || (Mode==M_BINADD2 && i==1)
              || (Mode==M_DIGIT && i==1))
              && (Mode!=M_BINADD) ) {
        tx=0;
        mx = (tx>mx ? tx : mx);

        ty = LogiTempl[i].CTHeight;
        ty *= 1.05;
        my = (ty>my ? ty : my);


        ycrt += my+0;
      }
      else {
        tx += LogiTempl[i].CTWidth;
          tx *=1.05;
        mx = (tx>mx ? tx : mx);
        ty = LogiTempl[i].CTHeight;
          ty *= 1.05;
        my = (ty>my ? ty : my);
      }
      for(int j=0;j < LogiTempl[i].Blocks1.size(); j++) {
        Blocks1.addElement((CCells)LogiTempl[i].Blocks1.elementAt(j));
        Blocks2.addElement((CCells)LogiTempl[i].Blocks2.elementAt(j));
      }
      LTOutput[i]=CLogiTemplate.LT_NONE;
    }
    MainView.SetGridSize(mx, ycrt+my);
    DrawWorld();
  }

  /** Construct a simple conway pattern */
  void GenConwayPat(int pat) {
    NbTemplate=1;
    LogiTempl=null;
    LogiTempl=new CLogiTemplate[NbTemplate];
    LogiTempl[0]=new CLogiTemplate(this, pat);
    LTOutput=new int[NbTemplate];
    OpDispView.PaintBackground();
    OpDispView.repaint();
    Generation=0;
    Blocks1.removeAllElements();
    Blocks2.removeAllElements();
    for(int j=0;j < LogiTempl[0].Blocks1.size(); j++) {
      Blocks1.addElement((CCells)LogiTempl[0].Blocks1.elementAt(j));
      Blocks2.addElement((CCells)LogiTempl[0].Blocks2.elementAt(j));
    }
    if(pat!=CPattern.P_RANDOM && pat!=CPattern.P_BIGUN && pat!=CPattern.P_MAKEGUN)
      MainView.SetGridSize(500, 500);
    else
      MainView.SetGridSize(250, 250);
    DrawWorld();
  }

  /** Run the World in View e.g. creates a new pop and draw world. */
  public void Go() {
    GenNewBlocs();
    if(Generation%DisplayStep==0 || !IsRunning) {
      DrawWorld();
      App.DispGen(Generation);
    }
    boolean cont=false;
    boolean newstate=false;
    int v;
    for(int i=0; i<NbTemplate; i++) {
      v=CheckOutput(LogiTempl[i], Generation);
      if(v!=LTOutput[i]) {
        newstate=true;
        LTOutput[i]=v;
      }
      if(v==CLogiTemplate.LT_NONE)
        cont=true;
    }
    if(newstate)
      DrawOutput();
    if(!cont) {
      SolFound=true;
      stop();
      System.out.println("Solution: "+(System.currentTimeMillis()-Time1)+" millisec.");
      App.EndSearch(SolFound);
    }
  }

  /** Set the Mode */
  public void SetMode(int mode) {
    Mode=mode;
  }
  /** Get Mode */
  public int GetMode() {
    return(Mode);
  }
   /** Default drawing */
   synchronized public void DrawWorld() {
    DrawWorld(MainView);
   }

  /** Draw Equation */
  void DrawEquation() {
    if(Mode==M_CONWAY)
      return;
    for(int i=0;i<NbTemplate;i++)
      MainView.DispEquation(LogiTempl[i]);
  }

  /** Draw Output */
  void DrawOutput() {
    OpDispView.DrawOutput(Mode);
    OpDispView.repaint();
  }

  /** Remove views */
  public void RemoveViews() {
    MainView.destroy();
    OpDispView.destroy();
  }

  public void stop() {
    IsRunning=false;
    LogicellUnivThread=null;
    App.stop();
  }
  /** This is the end... */
  public void end() {
     stop();
     RemoveViews();
  }

  /** Get Info text */
  String GetInfoText(int mode, int pat) {
    switch(Mode) {
      case M_INIT :
        return(
          "Sélectionnez un mode.");
      case M_EQUATION :
        return(App.Texts.Txt[19]);
      case M_LIGHT :
        return(App.Texts.Txt[20]);
      case M_BINADD :
        return(App.Texts.Txt[21]);
      case M_BINADD2 :
        return(App.Texts.Txt[22]);
      case M_DIGIT :
        return(App.Texts.Txt[23]);
      case M_DIGIT2 :
        return(App.Texts.Txt[24]);
      case M_CONWAY :
      default:
        if(pat==CPattern.P_RPENTO)
          return(App.Texts.Txt[25]);
        if(pat==CPattern.P_ACORN)
          return(App.Texts.Txt[26]);
        if(pat==CPattern.P_RABBITS)
          return(App.Texts.Txt[37]);
        if(pat==CPattern.P_BIGUN)
          return(App.Texts.Txt[38]);
        if(pat==CPattern.P_MAKEGUN)
          return(App.Texts.Txt[39]);
        return(App.Texts.Txt[27]);
    }
  }

  /** Get Speed */
  public int GetSpeed() {
    return(Speed);
  }
  /** Increase speed */
  public void SpeedIncr() {
    SetSpeed(Speed - SpeedOffset);
  }
  /** Decrease speed */
  public void SpeedDecr() {
    SetSpeed(Speed + SpeedOffset);    
  }
  /** Return a Logitempl output state */
  public int GetLtOutputState(int i) {
    return LogiTempl[i].GetOutputState(Generation);
  }
  

  /** Check Ouput Value */
  private int CheckOutput(CLogiTemplate lt, int gen) {
    return(lt.GetOutputState(gen));
  }
  
  /** Generation of next pop. For each cell, generation of the 3x3
    neighbourood and process new value. */
  private synchronized void GenNewBlocs() {
    // New blocks
    BlocTmp=(CellsBlocs==Blocks1 ? Blocks2 : Blocks1);
    int SizeCrt=CellsBlocs.size();
    for(int i=0;i<SizeCrt;i++) {
      CCells bcrt=(CCells) CellsBlocs.elementAt(i);
      CCells bt=(CCells) BlocTmp.elementAt(i);
      bt.CellsVal=0x0L;
      int nbvoisin=0;
      long vcrt=0L;
      boolean ccrt=false;
      long Cells, NCells, NECells, ECells, SECells, SCells, SWCells, WCells, NWCells;
      Cells =bcrt.CellsVal;
      NCells = (bcrt.NBloc!=null ? bcrt.NBloc.CellsVal : 0x0L);
      NECells = (bcrt.NEBloc!=null ? bcrt.NEBloc.CellsVal : 0x0L);
      ECells = (bcrt.EBloc!=null ? bcrt.EBloc.CellsVal : 0x0L);
      SECells = (bcrt.SEBloc!=null ? bcrt.SEBloc.CellsVal : 0x0L);
      SCells = (bcrt.SBloc!=null ? bcrt.SBloc.CellsVal : 0x0L);
      SWCells = (bcrt.SWBloc!=null ? bcrt.SWBloc.CellsVal : 0x0L);
      WCells = (bcrt.WBloc!=null ? bcrt.WBloc.CellsVal : 0x0L);
      NWCells = (bcrt.NWBloc!=null ? bcrt.NWBloc.CellsVal : 0x0L);

      // North-West corner
      vcrt = ( ( ((0x1L & NWCells) << 2) |
              (0x3L & (NCells >>> 6)) ) << 6 ) |
              ( ( ((0x1L & (WCells >>> 56)) << 2) |
              ((0x3L & (Cells >>> 62))) ) << 3) |
              ( ((0x1L & (WCells >>> 48)) << 2) |
              ((0x3L & (Cells >>> 54))) );
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.NBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x,bcrt.y-1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x,bcrt.y-1));
        SizeCrt++;
      }
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }
      if(ccrt && bcrt.NWBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y-1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y-1));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 63);

      // North Side
      for(int j=1;j<7;j++) {
          vcrt = (0x7L & (NCells >>> (6-j))) << 6 |
            ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
            ( 0x7L & (Cells >>> (45-(j-9))) );
          ccrt = ((vcrt >> 4) & 0x1L) >0;
          if(ccrt && bcrt.NBloc==null) {
            CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x,bcrt.y-1));
            BlocTmp.addElement(new CCells(BlocTmp,bcrt.x,bcrt.y-1));
            SizeCrt++;
          }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 63-j);
      }

      // North-East corner
      vcrt =  ( ( ((0x3L & NCells)<<1) |
                      (0x1L & (NECells >>> 7)) ) << 6 ) |
                    ( ( ((0x3L & (Cells >>> 56))<<1) |
                      (0x1L & (ECells >>> 63)) ) <<3 ) |
                    ( ((0x3L & (Cells >>> 48)) << 1) |
                      (0x1L & (ECells >>> 55)) );
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.NBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x,bcrt.y-1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x,bcrt.y-1));
        SizeCrt++;
      }
      if(ccrt && bcrt.EBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
        SizeCrt++;
      }
      if(ccrt && bcrt.NEBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y-1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y-1));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 56);

      // Cell 8 (left, line 2)
      vcrt =  (( (((WCells >>> 56 ) & 0x1L) << 2 ) |
                (0x3L & (Cells >>> 62 )) ) <<6 ) |
                (( ( ((WCells >>> 48) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 54 )) ) <<3 ) |
                ( ( ((WCells >>> 40) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 46 )));
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }

      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 55);

      // Cells 9 to 14
      for(int j=9;j<15; j++) {
        vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
          ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
          ( 0x7L & (Cells >>> (45-(j-9))) );
        ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 63-j);
      }

      // Cell 15 (East side)
      vcrt =  (( (((ECells >>> 63) & 0x1L) << 0 ) |
                (0x3L & (Cells >>> 56)) << 1) <<6 ) |
              (( ( ((ECells >>> 55) & 0x1L) << 0 ) |
                (0x3L & (Cells >>> 48)) << 1) <<3 ) |
              ( ( ((ECells >>> 47) & 0x1L) << 0 ) |
                (0x3L & (Cells >>> 40))<<1);
      ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(ccrt && bcrt.EBloc==null) {
          CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
          BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
          SizeCrt++;
        }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 48);

      // Cell 16 (West Side)
      vcrt =  (( (((WCells >>> 48 ) & 0x1L) << 2 ) |
                (0x3L & (Cells >>> 54 )) ) <<6 ) |
                (( ( ((WCells >>> 40) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 46 )) ) <<3 ) |
                ( ( ((WCells >>> 32) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 38 )));
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 47);

      // Cells 17 to 22
      for(int j=17;j<23; j++) {
        vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
          ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
          ( 0x7L & (Cells >>> (45-(j-9))) );
        ccrt = ((vcrt >> 4) & 0x1L) >0;

      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 63-j);
      }

      // Cell 23 (East side)
      vcrt =  (( (((ECells >>> 55) & 0x1L) << 0 ) |
                (0x3L & (Cells >>> 48)) << 1) <<6 ) |
              (( ( ((ECells >>> 47) & 0x1L) << 0 ) |
                (0x3L & (Cells >>> 40)) << 1) <<3 ) |
              ( ( ((ECells >>> 39) & 0x1L) << 0 ) |
                (0x3L & (Cells >>> 32))<<1);
      ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(ccrt && bcrt.EBloc==null) {
          CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
          BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
          SizeCrt++;
        }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 40);

      // Cell 24 (West Side)
      vcrt =  (( (((WCells >>> 40 ) & 0x1L) << 2 ) |
                (0x3L & (Cells >>> 46 )) ) <<6 ) |
                (( ( ((WCells >>> 32) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 38 )) ) <<3 ) |
                ( ( ((WCells >>> 24) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 30 )));
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 39);

      // Cells 25 to 30
      for(int j=25;j<31; j++) {
        vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
          ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
          ( 0x7L & (Cells >>> (45-(j-9))) );
        ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(LookUp[(int)vcrt])
          bt.CellsVal |= (0x1L << 63-j);
      }

      // Cell 31 (East side)
      vcrt =  (( (((ECells >>> 47) & 0x1L)  ) |
                (0x3L & (Cells >>> 40)) << 1) <<6 ) |
              (( ( ((ECells >>> 39) & 0x1L)  ) |
                (0x3L & (Cells >>> 32)) << 1) <<3 ) |
              ( ( ((ECells >>> 31) & 0x1L)  ) |
                (0x3L & (Cells >>> 24))<<1);
      ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(ccrt && bcrt.EBloc==null) {
          CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
          BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
          SizeCrt++;
        }

      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 32);

      // Cell 32 (West Side)
      vcrt =  (( (((WCells >>> 32 ) & 0x1L) << 2 ) |
                (0x3L & (Cells >>> 38 )) ) <<6 ) |
                (( ( ((WCells >>> 24) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 30 )) ) <<3 ) |
                ( ( ((WCells >>> 16) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 22 )));
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }

      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 31);

      // Cells 33 to 38
      for(int j=33;j<39; j++) {
        vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
          ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
          ( 0x7L & (Cells >>> (45-(j-9))) );
        ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(LookUp[(int)vcrt])
          bt.CellsVal |= (0x1L << 63-j);
      }

      // Cell 39 (East side)
      vcrt =  (( (((ECells >>> 39) & 0x1L)  ) |
                (0x3L & (Cells >>> 32)) << 1) <<6 ) |
              (( ( ((ECells >>> 31) & 0x1L)  ) |
                (0x3L & (Cells >>> 24)) << 1) <<3 ) |
              ( ( ((ECells >>> 23) & 0x1L)  ) |
                (0x3L & (Cells >>> 16))<<1);
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.EBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 24);

      // Cell 40 (West Side)
      vcrt =  (( (((WCells >>> 24 ) & 0x1L) << 2 ) |
                (0x3L & (Cells >>> 30 )) ) <<6 ) |
                (( ( ((WCells >>> 16) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 22 )) ) <<3 ) |
                ( ( ((WCells >>> 8) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 14 )));
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 23);

      // Cells 41 to 46
      for(int j=41;j<47; j++) {
        vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
          ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
          ( 0x7L & (Cells >>> (45-(j-9))) );
        ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(LookUp[(int)vcrt])
          bt.CellsVal |= (0x1L << 63-j);
      }
  
      // Cell 47 (East side)
      vcrt =  (( (((ECells >>> 31) & 0x1L)  ) |
                (0x3L & (Cells >>> 24)) << 1) <<6 ) |
              (( ( ((ECells >>> 23) & 0x1L)  ) |
                (0x3L & (Cells >>> 16)) << 1) <<3 ) |
              ( ( ((ECells >>> 15) & 0x1L)  ) |
                (0x3L & (Cells >>> 8))<<1);
      ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(ccrt && bcrt.EBloc==null) {
          CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
          BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
          SizeCrt++;
        }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 16);

      // Cell 48 (West Side)
      vcrt =  (( (((WCells >>> 16 ) & 0x1L) << 2 ) |
                (0x3L & (Cells >>> 22 )) ) <<6 ) |
                (( ( ((WCells >>> 8) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 14 )) ) <<3 ) |
                ( ( ((WCells >>> 0) & 0x1L) << 2 ) |
                  (0x3L & (Cells >>> 6 )));
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 15);

      // Cells 49 to 54
      for(int j=49;j<55; j++) {
        vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
          ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
          ( 0x7L & (Cells >>> (45-(j-9))) );
        ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(LookUp[(int)vcrt])
          bt.CellsVal |= (0x1L << 63-j);
      }

      // Cell 55 (East side)
      vcrt =  (( (((ECells >>> 23) & 0x1L) ) |
                (0x3L & (Cells >>> 16)) << 1) <<6 ) |
              (( ( ((ECells >>> 15) & 0x1L)  ) |
                (0x3L & (Cells >>> 8)) << 1) <<3 ) |
              ( ( ((ECells >>> 7) & 0x1L)  ) |
                (0x3L & (Cells >>> 0))<<1);
      ccrt = ((vcrt >> 4) & 0x1L) >0;
        if(ccrt && bcrt.EBloc==null) {
          CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
          BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
          SizeCrt++;
        }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 8);

      // Cell 56 (SW corner)
      vcrt =  ( ( ((0x1L & (WCells >>> 8))<<2) |
                (0x3L & (Cells >>> 14)) ) << 6 ) |
              ( ( ((0x1L & (WCells >>> 0))<<2) |
                (0x3L & (Cells >>> 6)) ) << 3 ) |
              ( ((0x1L & (SWCells >>> 56)) << 2) |
                (0x3L & (SCells >>> 62)) );
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.SBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x,bcrt.y+1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x,bcrt.y+1));
        SizeCrt++;
      }
      if(ccrt && bcrt.WBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y));
        SizeCrt++;
      }
      if(ccrt && bcrt.SWBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x-1,bcrt.y+1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x-1,bcrt.y+1));
        SizeCrt++;
      }
      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L << 7);

      // Cells 57-62 (South side)
      for(int j=57; j<63; j++) {
          vcrt =  ( (0x7L & (Cells >>> (61-(j-9)))) << 6 ) |
            ( (0x7L & (Cells >>> (53-(j-9)))) << 3 ) |
            ( (0x7L & (SCells >>> (56+(62-j)))) );
          ccrt = ((vcrt >> 4) & 0x1L) >0;
          if(ccrt && bcrt.SBloc==null) {
            CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x,bcrt.y+1));
            BlocTmp.addElement(new CCells(BlocTmp,bcrt.x,bcrt.y+1));
            SizeCrt++;
          }
        if(LookUp[(int)vcrt])
          bt.CellsVal |= (0x1L << 63-j);
      }

      // Cell 63 (SE corner)
      vcrt =  ( ( ((0x3L & (Cells >>> 8)) << 1) |
                (0x1L & (ECells >>> 15)) ) << 6 ) |
              ( ( ((0x3L & (Cells >>> 0)) << 1) |
                (0x1L & (ECells >>> 7)) ) <<3 ) |
              ( ((0x3L & (SCells >>> 56)) << 1) |
                ((0x1L & (SECells >>> 63))) );
      ccrt = ((vcrt >> 4) & 0x1L) >0;
      if(ccrt && bcrt.SBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x,bcrt.y+1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x,bcrt.y+1));
        SizeCrt++;
      }
      if(ccrt && bcrt.EBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y));
        SizeCrt++;
      }
      if(ccrt && bcrt.SEBloc==null) {
        CellsBlocs.addElement(new CCells(CellsBlocs,bcrt.x+1,bcrt.y+1));
        BlocTmp.addElement(new CCells(BlocTmp,bcrt.x+1,bcrt.y+1));
        SizeCrt++;
      }

      if(LookUp[(int)vcrt])
        bt.CellsVal |= (0x1L);
    } // For each block
    // remove empty blocks every "RemoveEmptyGen" generations if Conway Mode
    if(Mode==M_CONWAY) {
      if(Generation%RemoveEmptyGen ==0 ) {
        CCells bt,bt2;
        for(int i=0;i<BlocTmp.size();i++) {
          bt=(CCells) (BlocTmp.elementAt(i));
          bt2=(CCells) (CellsBlocs.elementAt(i));
          if(bt.CellsVal==0x0L) {
            bt.DestroyBloc();
            BlocTmp.removeElementAt(i);
            bt2.DestroyBloc();
            CellsBlocs.removeElementAt(i);
          }
        }
      }
    }

    // swap lists
    CellsBlocs=BlocTmp;
    Generation++;
  }

  /** Init lookup table */
  private void InitLookUp() {
    int v;
    boolean c;
    for(int i=0;i<512;i++) {
      LookUp[i]=false;;
     c = ((i >> 4) & 0x1L) >0;
     v=Neighbour(i,c);
     if( (c && v ==2 || v==3) || (!c && v == 3) )
      LookUp[i]=true;
    }
  }

  /** Calc #neighbour */
  private int Neighbour(long v, boolean c) {
    int n=0;
    n += v & 0x1L;
    n += (v >> 1) & 0x1L;
    n += (v >> 2) & 0x1L;
    n += (v >> 3) & 0x1L;
    n += (v >> 5) & 0x1L;
    n += (v >> 6) & 0x1L;
    n += (v >> 7) & 0x1L;
    n += (v >> 8) & 0x1L;
    return(n);
  }

  /** Draw World */
   private void DrawWorld(JPView jpv) {
    CLogicellView bv=(CLogicellView)jpv;
    bv.PaintBackground();

    if(Mode==M_INIT)
      bv.DispBkgText();
    else   {
      for(int i=0;i<CellsBlocs.size();i++)
        ((CCells) CellsBlocs.elementAt(i)).Live(bv);
    }
    bv.repaint();
  }

  /** Set speed */
  private void SetSpeed(int s) {
    Speed=Math.max(s,0);
    DisplayStep=(Speed>0 ? 1 : DefaultDisplayStep);
  }
}


