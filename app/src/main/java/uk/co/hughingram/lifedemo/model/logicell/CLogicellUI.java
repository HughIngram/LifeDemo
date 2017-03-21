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

/** User Interface definition */
public class CLogicellUI extends Frame {

   final int PAN_INIT=0;
   final int PAN_RUNNING=2;
   final int PAN_STOP=3;
   final int PAN_RUN=4;
   final int PAN_SAEQ=5;
   /** Default equation */
   final String DefEquation="B^A";
  /** Panel mode */
  int PanMode=PAN_INIT;
  /** Selected conway pattern */
  int ConwPatCrt=CPattern.P_RPENTO;
  /** Current Equation */
  String EqCrt;
  /** pointer to Mother applet */
  public Logicell MotherApplet;
  /** Language management */
  CText Texts;
  /** The World ! */
  CLogicellUniverse world;

  /** UI defs */
  BorderLayout borderLayout1 = new BorderLayout();
  Panel panel1 = new Panel();
  Button BtnStep = new Button();
  GridLayout gridLayout1 = new GridLayout();
  Button BtnGo = new Button();
  Button BtnStop = new Button();
  Label LabGene = new Label();
  Checkbox CbE4 = new Checkbox();
  Checkbox CbE3 = new Checkbox();
  Checkbox CbE2 = new Checkbox();
  Checkbox CbE1 = new Checkbox();
  TextField TfEntry = new TextField();
  Button BtnValEntry = new Button();
  Panel panel3 = new Panel();
  GridLayout gridLayout2 = new GridLayout();
  Panel panel5 = new Panel();
  Panel panel6 = new Panel();
  Button BtnEqA = new Button();
  Button BtnEqB = new Button();
  Button BtnEqC = new Button();
  Button BtnEqD = new Button();
  Button BtnEqAnd = new Button();
  Button BtnEqOr = new Button();
  Button BtnEqNot = new Button();
  Button BtnEqXor = new Button();
  Button BtnEqNew = new Button();
  GridLayout gridLayout6 = new GridLayout();
  GridLayout gridLayout7 = new GridLayout();
  Button BtnVv = new Button();
  Button BtnDigit2 = new Button();
  Panel panel10 = new Panel();
  GridLayout gridLayout9 = new GridLayout();
  Button BtnEq = new Button();
  Button BtnRpento = new Button();
  Panel panel2 = new Panel();
  GridLayout gridLayout10 = new GridLayout();
  Button BtnDigit1 = new Button();
  Button BtnBinAdd = new Button();
  Button BtnBinAdd2 = new Button();
  Button BtnAcorn = new Button();
  Button BtnRnd = new Button();
  Button BtnAbout = new Button();
  Button BtnEqOp = new Button();
  Button BtnEqCp = new Button();
  Panel panel11 = new Panel();
  GridLayout gridLayout11 = new GridLayout();
  Panel panel12 = new Panel();
  GridLayout gridLayout12 = new GridLayout();
  Panel panel13 = new Panel();
  Panel panel14 = new Panel();
  GridLayout gridLayout13 = new GridLayout();
  GridLayout gridLayout14 = new GridLayout();
  Panel panel15 = new Panel();
  Panel panel16 = new Panel();
  GridLayout gridLayout15 = new GridLayout();
  GridLayout gridLayout16 = new GridLayout();
  Panel panel4 = new Panel();
  GridLayout gridLayout3 = new GridLayout();
  Button BtnSpeedM = new Button();
  Button BtnSpeedP = new Button();
  Panel panel7 = new Panel();
  GridLayout gridLayout4 = new GridLayout();
  Panel panel8 = new Panel();
  GridLayout gridLayout5 = new GridLayout();
  Panel panel9 = new Panel();
  GridLayout gridLayout8 = new GridLayout();
  Button BtnRabbit = new Button();
  Panel panel17 = new Panel();
  GridLayout gridLayout17 = new GridLayout();
  Panel panel18 = new Panel();
  GridLayout gridLayout18 = new GridLayout();
  Panel panel19 = new Panel();
  GridLayout gridLayout19 = new GridLayout();
  Button BtnBigun = new Button();
  Button BtnMakegun = new Button();
  Label LblSpeed = new Label();

  /** Construct a UI in an applet with the language */
  public CLogicellUI(Logicell ma, int lang) {
    super(ma.GetName()+" "+ma.GetVersionNum()+" "+ma.GetAuthor());
    MotherApplet=ma;
    Texts=new CText(lang);
    try  {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /** Init the UI : creates World and add Views */
  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    BtnStep.setForeground(Color.darkGray);
    BtnStep.setLabel(Texts.Txt[0]);// "Step");
    BtnStep.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnStep_actionPerformed(e);
      }
    });
    panel1.setBackground(Color.black);
    panel1.setLayout(gridLayout1);
    this.addWindowListener(new java.awt.event.WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    BtnGo.setFont(new java.awt.Font("Dialog", 1, 12));
    BtnGo.setForeground(Color.darkGray);
    BtnGo.setLabel(Texts.Txt[1]);//"Go");
    BtnGo.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnGo_actionPerformed(e);
      }
    });
    BtnStop.setForeground(Color.darkGray);
    BtnStop.setLabel(Texts.Txt[2]);//"Stop");
    BtnStop.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnStop_actionPerformed(e);
      }
    });
    LabGene.setForeground(Color.darkGray);
    LabGene.setAlignment(1);
    LabGene.setText("0");//"Gen:");
    CbE4.setForeground(Color.black);
    CbE4.setName("E1");
    CbE4.setLabel("D");
    CbE4.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        CbE4_itemStateChanged(e);
      }
    });
    CbE3.setLabel("C");
    CbE3.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        CbE3_itemStateChanged(e);
      }
    });
    CbE3.setForeground(Color.black);
    CbE3.setName("E1");
    CbE2.setLabel("B");
    CbE2.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        CbE2_itemStateChanged(e);
      }
    });
    CbE2.setForeground(Color.black);
    CbE2.setName("E1");
    CbE1.setLabel("A");
    CbE1.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        CbE1_itemStateChanged(e);
      }
    });
    CbE1.setForeground(Color.black);
    CbE1.setName("E1");
    BtnValEntry.setForeground(Color.darkGray);
    BtnValEntry.setLabel("OK");
    BtnValEntry.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnValEntry_actionPerformed(e);
      }
    });
    TfEntry.setBackground(Color.white);
    EqCrt=DefEquation;
    TfEntry.setText("");
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentResized(ComponentEvent e) {
        this_componentResized(e);
      }
    });
    panel3.setLayout(gridLayout2);
    gridLayout2.setColumns(3);
    gridLayout2.setHgap(5);
    gridLayout2.setVgap(3);
    BtnEqA.setLabel("A");
    BtnEqA.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqA_actionPerformed(e);
      }
    });
    BtnEqB.setLabel("B");
    BtnEqB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqB_actionPerformed(e);
      }
    });
    BtnEqC.setLabel("C");
    BtnEqC.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqC_actionPerformed(e);
      }
    });      
    BtnEqD.setLabel("D");
    BtnEqD.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqD_actionPerformed(e);
      }
    });
    BtnEqAnd.setLabel(Texts.Txt[4]);//"And");
    BtnEqAnd.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqAnd_actionPerformed(e);
      }
    });
    BtnEqOr.setLabel(Texts.Txt[5]);//"Or");
    BtnEqOr.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqOr_actionPerformed(e);
      }
    });
    BtnEqNot.setLabel(Texts.Txt[6]);//"Not");
    BtnEqNot.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqNot_actionPerformed(e);
      }
    });
    BtnEqXor.setLabel(Texts.Txt[7]);//"Xor");
    BtnEqXor.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqXor_actionPerformed(e);
      }
    });
    BtnEqNew.setForeground(Color.darkGray);
    BtnEqNew.setLabel(Texts.Txt[8]);//"New");
    BtnEqNew.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqNew_actionPerformed(e);
      }
    });
    panel6.setLayout(gridLayout6);
    gridLayout6.setColumns(1);
    gridLayout6.setRows(2);
    gridLayout6.setVgap(3);
    panel5.setLayout(gridLayout7);
    gridLayout7.setColumns(4);
    BtnVv.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnVv.setLabel(Texts.Txt[9]);//"Va et vient");
    BtnVv.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnVv_actionPerformed(e);
      }
    });
    BtnDigit2.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnDigit2.setLabel(Texts.Txt[10]);//"Digit");
    BtnDigit2.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnDigit2_actionPerformed(e);
      }
    });
    panel10.setLayout(gridLayout9);
    panel10.setBackground(Color.red);
    panel3.setBackground(Color.gray);
    panel6.setBackground(Color.gray);
    panel6.setForeground(Color.white);
    BtnEq.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnEq.setLabel(Texts.Txt[11]);//"Equation");
    BtnEq.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEq_actionPerformed(e);
      }
    });
    BtnRpento.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnRpento.setLabel(Texts.Txt[12]);//"RPento");
    BtnRpento.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnRpento_actionPerformed(e);
      }
    });
    panel2.setLayout(gridLayout10);
    gridLayout10.setColumns(10);
    BtnDigit1.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnDigit1.setLabel(Texts.Txt[13]);//"Digit2");
    BtnDigit1.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnDigit1_actionPerformed(e);
      }
    });
    BtnBinAdd.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnBinAdd.setLabel(Texts.Txt[14]);//"BinAdd");
    BtnBinAdd.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnBinAdd_actionPerformed(e);
      }
    });
    BtnBinAdd2.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnBinAdd2.setLabel(Texts.Txt[15]);//"BinAdd2");
    BtnBinAdd2.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnBinAdd2_actionPerformed(e);
      }
    });
    BtnAcorn.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnAcorn.setLabel(Texts.Txt[16]);//"Acorn");
    BtnAcorn.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnAcorn_actionPerformed(e);
      }
    });
    BtnRnd.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnRnd.setLabel(Texts.Txt[17]);//"Random");
    BtnRnd.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnRnd_actionPerformed(e);
      }
    });
    BtnAbout.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnAbout.setLabel(Texts.Txt[18]);//"About");
    BtnAbout.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnAbout_actionPerformed(e);
      }
    });
    BtnEqOp.setForeground(Color.darkGray);
    BtnEqOp.setLabel("(");
    BtnEqOp.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqOp_actionPerformed(e);
      }
    });
    BtnEqCp.setForeground(Color.darkGray);
    BtnEqCp.setLabel(")");
    BtnEqCp.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnEqCp_actionPerformed(e);
      }
    });
    panel11.setLayout(gridLayout11);
    gridLayout11.setColumns(1);
    gridLayout11.setRows(2);
    gridLayout11.setVgap(3);
    panel12.setLayout(gridLayout12);
    gridLayout12.setColumns(5);
    panel13.setLayout(gridLayout13);
    gridLayout13.setColumns(4);
    panel14.setLayout(gridLayout14);
    gridLayout14.setColumns(7);
    panel15.setLayout(gridLayout15);
    gridLayout15.setColumns(2);
    gridLayout15.setRows(2);
    gridLayout15.setVgap(3);
    panel16.setLayout(gridLayout16);
    gridLayout16.setColumns(2);
    gridLayout16.setRows(2);
    gridLayout16.setVgap(3);
    panel4.setLayout(gridLayout3);
    gridLayout3.setColumns(3);
    BtnSpeedM.setForeground(Color.darkGray);
    BtnSpeedM.setLabel(Texts.Txt[32]);
    BtnSpeedM.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnSpeedM_actionPerformed(e);
      }
    });
    BtnSpeedP.setForeground(Color.darkGray);
    BtnSpeedP.setLabel(Texts.Txt[31]);//"s+");
    BtnSpeedP.addActionListener(new java.awt.event.ActionListener() {
                    
      public void actionPerformed(ActionEvent e) {
        BtnSpeedP_actionPerformed(e);
      }
    });
    panel7.setLayout(gridLayout4);
    gridLayout4.setColumns(2);
    gridLayout4.setRows(2);
    gridLayout4.setVgap(3);
    panel8.setLayout(gridLayout5);
    gridLayout5.setColumns(2);
    panel9.setLayout(gridLayout8);
    BtnRabbit.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnRabbit.setLabel(Texts.Txt[34]);
    BtnRabbit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnRabbit_actionPerformed(e);
      }
    });
    panel17.setLayout(gridLayout17);
    gridLayout17.setColumns(2);
    panel18.setLayout(gridLayout18);
    gridLayout18.setColumns(2);
    panel19.setLayout(gridLayout19);
    gridLayout19.setColumns(2);
    BtnBigun.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnBigun.setLabel(Texts.Txt[35]);
    BtnBigun.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnBigun_actionPerformed(e);
      }
    });
    BtnMakegun.setFont(new java.awt.Font("Dialog", 0, 10));
    BtnMakegun.setLabel(Texts.Txt[36]);
    BtnMakegun.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        BtnMakegun_actionPerformed(e);
      }
    });
    LblSpeed.setAlignment(1);
    this.add(panel1, BorderLayout.CENTER);
    this.add(panel3, BorderLayout.SOUTH);
    panel3.add(panel11, null);
    panel11.add(TfEntry, null);
    panel11.add(panel12, null);
    panel12.add(LabGene, null);
    panel12.add(BtnSpeedM, null);
    panel12.add(BtnSpeedP, null);
    panel12.add(LblSpeed, null);
    panel3.add(panel6, null);
    panel6.add(panel13, null);
    panel13.add(panel4, null);
    panel4.add(BtnStop, null);
    panel4.add(BtnGo, null);
    panel4.add(BtnStep, null);
    panel6.add(panel14, null);
    panel14.add(CbE4, null);
    panel14.add(CbE3, null);
    panel14.add(CbE2, null);
    panel14.add(CbE1, null);
    panel14.add(BtnValEntry, null);
    panel3.add(panel5, null);
    panel5.add(panel7, null);
    panel7.add(panel8, null);
    panel8.add(BtnEqOp, null);
    panel8.add(BtnEqCp, null);
    panel7.add(panel9, null);
    panel9.add(BtnEqNew, null);
    panel5.add(panel15, null);
    panel15.add(BtnEqA, null);
    panel15.add(BtnEqB, null);
    panel15.add(BtnEqOr, null);
    panel15.add(BtnEqAnd, null);

    panel5.add(panel16, null);
    panel16.add(BtnEqC, null);
    panel16.add(BtnEqD, null);
    panel16.add(BtnEqXor, null);
    panel16.add(BtnEqNot, null);
    panel5.add(panel10, null);
    this.add(panel2, BorderLayout.NORTH);
    panel2.add(BtnEq, null);
    panel2.add(BtnVv, null);
    panel2.add(BtnBinAdd, null);
    panel2.add(BtnDigit2, null);
    panel2.add(BtnBinAdd2, null);
    panel2.add(BtnDigit1, null);
    panel2.add(panel17, null);
    panel17.add(BtnRpento, null);
    panel17.add(BtnAcorn, null);
    panel2.add(panel18, null);
    panel18.add(BtnRabbit, null);
    panel18.add(BtnRnd, null);
    panel2.add(panel19, null);
    panel19.add(BtnBigun, null);
    panel19.add(BtnMakegun, null);
    panel2.add(BtnAbout, null);

    world=new CLogicellUniverse(this);
    panel1.add(world.mainView,null);
    panel10.add(world.opDispView,null);
  }

 /** Init the World */
  public void start() {
    if(world.GetSpeed()==0)
      BtnSpeedP.enable(false);
    world.InitView();
    SetLowPanel(PanMode);
    world.drawWorld();
    LblSpeed.setText(Integer.toString(world.speed));
  }

  /** Good-Bye */
  void this_windowClosing(WindowEvent e) {
    End();
  }
  /** Stop */
  void stop() {
  }
  /** End */
  void End() {
    synchronized(world) {world.end();}
    dispose();
  }
  /** Init the grid */
  void BtnStep_actionPerformed(ActionEvent e) {
    world.Go();
  }

  void BtnGo_actionPerformed(ActionEvent e) {
    SetLowPanel(PAN_RUNNING);
    world.Launch();
  }

  void BtnStop_actionPerformed(ActionEvent e) {
    world.stop();
    SetLowPanel(PAN_STOP);    
  }
  /** Generation display */
  void DispGen(int g) {
    this.LabGene.setText(Integer.toString(g));
  }

  /** Validate the Entry and set the problem or conway pattern */
  void ValEntry() {
    if(world.GetMode()==CLogicellUniverse.M_DIGIT ||
          world.GetMode()==CLogicellUniverse.M_DIGIT2) {
      if(CbE4.getState() && (CbE2.getState() || CbE3.getState())) {
        JPAlert al=new JPAlert(this, Texts.Txt[28]);//"La valeur maximum est neuf !");
        return;
      }
    }

    BtnEqAnd.enable(false);
    BtnEqOr.enable(false);
    BtnEqXor.enable(false);
    BtnEqNot.enable(false);

    boolean entries[]=new boolean[CLogicellUniverse.MAXENTRY];
    for(int i=0;i<CLogicellUniverse.MAXENTRY;i++)
      entries[i]=false;
    entries[0]=this.CbE1.getState();
    entries[1]=this.CbE2.getState();
    entries[2]=this.CbE3.getState();
    entries[3]=this.CbE4.getState();
    int NbTemplate=1;
    panel10.setBackground(Color.red);

    String[] en;
    switch(world.GetMode()) {
      case CLogicellUniverse.M_DIGIT2 :
        NbTemplate=7;
        en=new String[NbTemplate];
        en[0]="~(AwC)vBvD";  // Led A
        en[1]="~(AwB)v~C";  // Led C
        en[2]="AvCv~BvD";  // Led F
        en[3]="(B^C^~A)v(BwC)vD"; // Led D
        en[4]="(C^~D^~(A^B))vDv(~A^~B^~C^~D)";  // Led B
        en[5]="(~AvBvCvD)^(AvBv~CvD)^(~Av~Bv~CvD)"; // Led G
        en[6]="(~CvB)^~A";  // Led E
      break;
      case CLogicellUniverse.M_DIGIT :
        NbTemplate=4;
        en=new String[NbTemplate];
        en[0]="~(AwC)vBvD";  // Led A
        en[1]="(~CvB)^~A";  // Led E
        en[2]="~(AwB)v~C";  // Led C
        en[3]="AvCv~BvD";  // Led F

      break;
      case CLogicellUniverse.M_LIGHT :
        NbTemplate=1;
        en=new String[NbTemplate];
        en[0]="~(AwB)";
      break;
      case CLogicellUniverse.M_CONWAY :
        NbTemplate=1;
        en=new String[NbTemplate];
        en[0]="";
      break;
      case CLogicellUniverse.M_BINADD :
        NbTemplate=2;
        en=new String[NbTemplate];
        en[0]="AwB";
        en[1]="A^B";
      break;
      case CLogicellUniverse.M_BINADD2 :
        NbTemplate=3;
        en=new String[NbTemplate];
        en[0]="AwC";   // s0
        en[1]="(B^D)v((BwD)^(A^C))";
        en[2]="DwBw(A^C)";  // s1
        /*
        en[0]="AwC";   // s0
        en[1]="(B^(A^C))v(((A^C)wB)^D)";  // r1
        en[2]="Dw(Bw(A^C))";  // s1
        */
      break;
      case CLogicellUniverse.M_EQUATION :
      default:
        NbTemplate=1;
        en=new String[NbTemplate];
        en[0]=TfEntry.getText();
      break;
    }

    // Problem or pattern construction
    if(world.GetMode()!=CLogicellUniverse.M_CONWAY) {
      String s=TfEntry.getText();
      TfEntry.setText(Texts.Txt[30]);// "Construction en cours ...");
      world.GenLogiProblem(en, NbTemplate, entries, world.MAXENTRY);
      TfEntry.setText(s);
    }
    else
      world.GenConwayPat(ConwPatCrt);
    // set info text
    if(world.GetMode()==CLogicellUniverse.M_DIGIT ||
          world.GetMode()==CLogicellUniverse.M_DIGIT2)
      TfEntry.setText(Texts.Txt[29]+Integer.toString(world.entryBinVal));
    else if(world.GetMode()==CLogicellUniverse.M_LIGHT)
      TfEntry.setText(en[0]);
    else if(world.GetMode()==CLogicellUniverse.M_BINADD) {
      int e1=(CbE1.getState() ? 1 : 0);
      int e2=(CbE2.getState() ? 1 : 0);
      TfEntry.setText(Texts.Txt[33]+e1+" + "+e2+" = "+(e1+e2));
    }
    else if(world.GetMode()==CLogicellUniverse.M_BINADD2) {
      int e1=(CbE1.getState() ? 1 : 0) + (CbE2.getState() ? 2 : 0);
      int e2=(CbE3.getState() ? 1 : 0) + (CbE4.getState() ? 2 : 0);
      TfEntry.setText(Texts.Txt[33]+e1+" + "+e2+" = "+(e1+e2));
    }
    else if(world.GetMode()!=CLogicellUniverse.M_EQUATION)
      TfEntry.setText("");

    world.DrawEquation();

    if(world.GetMode()==CLogicellUniverse.M_DIGIT ||
            world.GetMode()==CLogicellUniverse.M_DIGIT2)
      world.DrawOutput();    
  }
  
  /** Set all entries to false */
  void InitEntries() {
    CbE1.setState(false);
    CbE2.setState(false);
    CbE3.setState(false);
    CbE4.setState(false);
  }

  /** End of search */
  void EndSearch(boolean res) {
    SetLowPanel(PAN_STOP);
    if(res) {
      BtnGo.enable(false);
      BtnStep.enable(false);
    }
  }

  void BtnValEntry_actionPerformed(ActionEvent e) {
    BtnGo.enable(true);
    BtnStep.enable(true);
    ValEntry();
  }

  /** Resize */
  void this_componentResized(ComponentEvent e) {
    boolean isr=false;
    if(world.isRunning) {
      synchronized(world) {world.stop();}
      isr=true;
    }
    world.InitView();
    world.drawWorld();
    world.DrawOutput();
    if(isr) {
      world.Launch();
    }
  }
  /** Decrease speed */
  void BtnSpeedM_actionPerformed(ActionEvent e) {
    world.SpeedDecr();
      BtnSpeedP.enable(true);
    LblSpeed.setText(" "+Integer.toString(world.speed));
  }

  /** Increase speed */
  void BtnSpeedP_actionPerformed(ActionEvent e) {
    world.SpeedIncr();
    if(world.GetSpeed()==0)
      BtnSpeedP.enable(false);
    LblSpeed.setText(" "+Integer.toString(world.speed));      
  }

  // Mode Management
  /** Set mode */
  void SetMode(int m) {
    InitEntries();
    world.SetMode(m);
    SetLowPanel(PAN_RUN);
    DispInfoBox();
    ValEntry();
    if(m==CLogicellUniverse.M_CONWAY)
      world.opDispView.DrawConway();
    else if(m==CLogicellUniverse.M_LIGHT)
      world.opDispView.InitDrawLight();
  }
  /** Set  Equation mode */
  void BtnEq_actionPerformed(ActionEvent e) {
    EqCrt=DefEquation;
    TfEntry.setText(EqCrt);
    SetMode(CLogicellUniverse.M_EQUATION);
  }
  /** Set  Conway/RPento mode */
  void BtnRpento_actionPerformed(ActionEvent e) {
    ConwPatCrt=CPattern.P_RPENTO;
    SetMode(CLogicellUniverse.M_CONWAY);
  }
  /** Set  Conway/Acorn mode */
  void BtnAcorn_actionPerformed(ActionEvent e) {
    ConwPatCrt=CPattern.P_ACORN;
    SetMode(CLogicellUniverse.M_CONWAY);
  }
  /** Set Conway/Rabbits mode */
  void BtnRabbit_actionPerformed(ActionEvent e) {
    ConwPatCrt=CPattern.P_RABBITS;
    SetMode(CLogicellUniverse.M_CONWAY);
  }
  /** Set Conway/BiGun mode */
  void BtnBigun_actionPerformed(ActionEvent e) {
    ConwPatCrt=CPattern.P_BIGUN;
    SetMode(CLogicellUniverse.M_CONWAY);

  }
    /** Set  Conway/BiGun mode */
  void BtnMakegun_actionPerformed(ActionEvent e) {
    ConwPatCrt=CPattern.P_MAKEGUN;
    SetMode(CLogicellUniverse.M_CONWAY);
  }

  /** Set  Conway/Random mode */
  void BtnRnd_actionPerformed(ActionEvent e) {
    ConwPatCrt=CPattern.P_RANDOM;
    SetMode(CLogicellUniverse.M_CONWAY);
//    World.OpDispView.DrawConway();
  }
  /** Set  Light (two-way switch) mode */
  void BtnVv_actionPerformed(ActionEvent e) {
//    World.OpDispView.InitDrawVV();
    SetMode(CLogicellUniverse.M_LIGHT);
    SetLowPanel(PAN_RUNNING);
    world.Launch();
  }
  /** Set  Digit mode */
  void BtnDigit2_actionPerformed(ActionEvent e) {
    SetMode(CLogicellUniverse.M_DIGIT);
  }
  /** Set  Digit mode */
  void BtnDigit1_actionPerformed(ActionEvent e) {
    SetMode(CLogicellUniverse.M_DIGIT2);
  }
  /** Set Binary add (1 bit) mode */
  void BtnBinAdd_actionPerformed(ActionEvent e) {
    SetMode(CLogicellUniverse.M_BINADD);
  }
  /** Set Binary add (2 bits) mode */
  void BtnBinAdd2_actionPerformed(ActionEvent e) {
    SetMode(CLogicellUniverse.M_BINADD2);
  }


  // Dynamic change of entries if mode Light
  void CbE1_itemStateChanged(ItemEvent e) {
    BtnGo.enable(false);
    BtnStep.enable(false);
    if(world.GetMode()==CLogicellUniverse.M_LIGHT) {
      SetLowPanel(PAN_RUNNING);
      ValEntry();
      world.Launch();
    }
  }

  void CbE2_itemStateChanged(ItemEvent e) {
    BtnGo.enable(false);
    BtnStep.enable(false);
    if(world.GetMode()==CLogicellUniverse.M_LIGHT) {
      SetLowPanel(PAN_RUNNING);
      ValEntry();
      world.Launch();
    }
  }

  void CbE3_itemStateChanged(ItemEvent e) {
    BtnGo.enable(false);
    BtnStep.enable(false);
  }
  void CbE4_itemStateChanged(ItemEvent e) {
    BtnGo.enable(false);
    BtnStep.enable(false);
  }


  /** Display Info box */
  void DispInfoBox() {
    CDialInfo Box=new CDialInfo(this, MotherApplet.GetName(), true);
    Dimension dim= getToolkit().getScreenSize();
    Box.setSize(Math.max(dim.width/3,320),Math.max(dim.height/3,240));
    Box.setLocation(this.getLocation().x+(this.getSize().width/2)-
        (Box.getSize().width/2),this.getLocation().y+(this.getSize().height/2)-
        (Box.getSize().height/2));
    Box.show();
  }
  /** Display About box */
  void BtnAbout_actionPerformed(ActionEvent e) {

    CLogicellAbout Box=new CLogicellAbout(this,MotherApplet.GetName(),true);
    Dimension dim= getToolkit().getScreenSize();
    Box.setSize(dim.width/3,dim.height/3);
    Box.setLocation(this.getLocation().x+(this.getSize().width/2)-
        (Box.getSize().width/2),this.getLocation().y+(this.getSize().height/2)-
        (Box.getSize().height/2));
    Box.show();
  }

  /** Entry of a new equation */
  void BtnEqNew_actionPerformed(ActionEvent e) {
    EqCrt="";
    BtnGo.enable(false);
    BtnStep.enable(false);
    TfEntry.setText(EqCrt);
    SetEqBtn();
  }

  void BtnEqA_actionPerformed(ActionEvent e) {
    AddEqEntry('A');
  }
  void BtnEqB_actionPerformed(ActionEvent e) {
    AddEqEntry('B');
  }
  void BtnEqC_actionPerformed(ActionEvent e) {
    AddEqEntry('C');
  }
  void BtnEqD_actionPerformed(ActionEvent e) {
    AddEqEntry('D');
  }
  void BtnEqAnd_actionPerformed(ActionEvent e) {
    AddEqEntry(JPParser.OP_AND);
  }
  void BtnEqOr_actionPerformed(ActionEvent e) {
    AddEqEntry(JPParser.OP_OR);
  }
  void BtnEqNot_actionPerformed(ActionEvent e) {
    AddEqEntry(JPParser.OP_NOT);
  }
  void BtnEqXor_actionPerformed(ActionEvent e) {
    AddEqEntry(JPParser.OP_XOR);
  }
  void BtnEqOp_actionPerformed(ActionEvent e) {
    AddEqEntry('(');
  }
  void BtnEqCp_actionPerformed(ActionEvent e) {
    AddEqEntry(')');
  }

  /** Add a character to the Equation */
  void AddEqEntry(char e) {
    EqCrt += e;
    TfEntry.setText(EqCrt);
    SetEqBtn();
  }
  /** Manage Equation panel */
  void SetEqBtn() {
    char c;
    if(EqCrt.length()!=0)
      c=EqCrt.charAt(EqCrt.length()-1);
    else c=JPParser.OP_AND;  // just to avoid empty test
    // if last char is an operator or a ( or nothing, ok for an entry or a (
    if(JPParser.IsBoolOper(c) || c=='(' ) {
      BtnEqA.enable(true);
      BtnEqB.enable(true);
      BtnEqC.enable(true);
      BtnEqD.enable(true);
      BtnEqOp.enable(true);

      BtnEqAnd.enable(false);
      BtnEqOr.enable(false);
      BtnEqXor.enable(false);
      BtnEqCp.enable(false);
    }
    // if last char is not an oper, neither is next
    else if(!JPParser.IsBoolBinaryOper(c)) {
      BtnEqA.enable(false);
      BtnEqB.enable(false);
      BtnEqC.enable(false);
      BtnEqD.enable(false);
      BtnEqOp.enable(false);

      BtnEqAnd.enable(true);
      BtnEqOr.enable(true);
      BtnEqXor.enable(true);
    }
    // check  ')'
    int nbop=0;
    int nbcp=0;
    int nboper=0;
    int nben=0;
    for(int i=0;i<EqCrt.length();i++) {
      if(EqCrt.charAt(i)=='(')
        nbop++;
      else if(EqCrt.charAt(i)==')')
        nbcp++;
      else if(JPParser.IsBoolOper(EqCrt.charAt(i)))
        nboper++;
      else
        nben++;
    }
    // if ')'='(' or no oper or no entry, no '('
    if(nbcp>=nbop || EqCrt.length()<2 || nboper<1 || nben<1 || c=='(')
      BtnEqCp.enable(false);
    else if(!JPParser.IsBoolOper(c) || c==')')
      BtnEqCp.enable(true);
    // if nothing or last char is an oper or a '(', Not is aloud
    if(EqCrt.length()==0 || JPParser.IsBoolBinaryOper(c) || c=='(')
      BtnEqNot.enable(true);
    else
      BtnEqNot.enable(false);
    // check validity of equation
    if(nboper>=1 && nben>=1 && nbcp==nbop && !JPParser.IsBoolOper(c))
      BtnValEntry.enable(true);
    else
      BtnValEntry.enable(false);
  }

  /** Low panel management */
  void SetLowPanel(int pm) {
    switch(pm) {
      case PAN_INIT :
        BtnEqA.enable(false);
        BtnEqB.enable(false);
        BtnEqC.enable(false);
        BtnEqD.enable(false);
        BtnEqOp.enable(false);
        BtnEqAnd.enable(false);
        BtnEqOr.enable(false);
        BtnEqNot.enable(false);
        BtnEqXor.enable(false);
        BtnEqCp.enable(false);
        BtnEqNew.enable(false);
        BtnValEntry.enable(false);
        CbE1.enable(false);
        CbE2.enable(false);
        CbE3.enable(false);
        CbE4.enable(false);
        BtnStep.enable(false);
        BtnStop.enable(false);
        BtnGo.enable(false);
        TfEntry.enable(false);
      break;
      case PAN_RUN :
      default :
        BtnEqA.enable(false);
        BtnEqB.enable(false);
        BtnEqC.enable(false);
        BtnEqD.enable(false);
        BtnEqOp.enable(false);
        BtnEqAnd.enable(false);
        BtnEqOr.enable(false);
        BtnEqNot.enable(false);
        BtnEqXor.enable(false);
        BtnEqCp.enable(false);
        BtnEqNew.enable(false);
        BtnValEntry.enable(true);
        CbE1.enable(true);
        CbE2.enable(true);
        CbE3.enable(true);
        CbE4.enable(true);
        BtnStep.enable(true);
        BtnStop.enable(false);
        BtnGo.enable(true);
        TfEntry.enable(false);
        if(world.GetMode()==CLogicellUniverse.M_LIGHT ||
                  world.GetMode()==CLogicellUniverse.M_BINADD) {
            CbE3.enable(false);
            CbE4.enable(false);
        }
        if(world.GetMode()==CLogicellUniverse.M_EQUATION)
          BtnEqNew.enable(true);
        if(world.GetMode()==CLogicellUniverse.M_CONWAY) {
          CbE1.enable(false);
          CbE2.enable(false);
          CbE3.enable(false);
          CbE4.enable(false);
        }
      break;
      case PAN_RUNNING :
        BtnGo.enable(false);
        BtnStep.enable(false);
        BtnStop.enable(true);
        BtnValEntry.enable(false);
        CbE1.enable(false);
        CbE2.enable(false);
        CbE3.enable(false);
        CbE4.enable(false);
        panel2.enable(false);
      break;
      case PAN_STOP :
        BtnGo.enable(true);
        BtnStep.enable(true);
        BtnStop.enable(false);
        BtnValEntry.enable(true);
        CbE1.enable(true);
        CbE2.enable(true);
        CbE3.enable(true);
        CbE4.enable(true);
        if(world.GetMode()==CLogicellUniverse.M_BINADD ||
            world.GetMode()==CLogicellUniverse.M_LIGHT) {
          CbE3.enable(false);
          CbE4.enable(false);
        }
        if(world.GetMode()==CLogicellUniverse.M_CONWAY) {
          CbE1.enable(false);
          CbE2.enable(false);
          CbE3.enable(false);
          CbE4.enable(false);
        }
        panel2.enable(true);
      break;
    }
  }


/*
  void BtnZoomM_actionPerformed(ActionEvent e) {
    World.MainView.SetGridSize(0.5F);
    World.DrawWorld();
  }
  */

}
