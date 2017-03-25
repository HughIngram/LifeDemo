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

	/** Pattern size x */
	int PatHeight=480;
	/** Pattern size y */
	int PatWidth=480;
	/** View */
	/** Thread */
	Thread logicellUnivThread;
	/** Value of bin entry in digit mode */
	int entryBinVal=-1;
	/** Running flag */
	boolean isRunning = false;
	/** Iteration delay */
	int speed = DefaultSpeed;

	long Time1, TimeCalc, TimeDisp, TimePoint, TimeAdd;

	/** Remove empty block every # generation. Works only in Conway mode. */
	private final int RemoveEmptyGen=5;
	/** Speed offset used for speed modification. */
	private final int SpeedOffset=10;

	/** Lookup table */
	private boolean lookUp[];

	/** Contains the current cells blocks. Just a pointer */
	private Vector cellsBlocs;
	/** Contains the working list. Just a pointer */
	private Vector blocTmp;
	/** 2 lists of blocks */
	private Vector blocks1, blocks2;
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
	private int Mode = M_INIT;
	private boolean SolFound=false;
	private int generation = 0;
	/** Display only each x generations */
	private int DisplayStep=DefaultDisplayStep;

	/** Constructor */
	public CLogicellUniverse()  {
		lookUp = new boolean[512];
		initLookUp();
//		mainView = new CLogicellView(this,Color.black);
//		opDispView = new COpDispView(this, Color.blue);
		blocks1 = new Vector();
		blocks2 = new Vector();
		Mode = M_INIT;
		initWorld();
	}

	/** Init the world */
	void initWorld() {
		cellsBlocs = blocks1;
	}

	/** Init View */
	public void InitView() {
//		mainView.ResizeGrBuff();
//		mainView.PaintBackground();
//		opDispView.ResizeGrBuff();
//		opDispView.paintBackground();
	}

	/** Launch thread */
	public void run() {
		Thread thisThread=Thread.currentThread();
		isRunning=true;
		while(logicellUnivThread==thisThread) {
			Go();
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) { }
		} // while
	}

	/** Start the world */
	void Launch() {
		if(logicellUnivThread==null)
			logicellUnivThread=new Thread(this);
		Time1=System.currentTimeMillis();
		isRunning=true;
		SolFound=false;
		logicellUnivThread.start();
	}

	// reverse engineer how this method works..
	/** Construct a Logical Problem e.d. a combination of templates.
     * @param templ - the equation string for some other problem?
     * @param st - the equation string.
     * @param entries - ?
     * @param se - not used
     * */
	public void GenLogiProblem(String templ[], int st, boolean entries[], int se) {
		NbTemplate = st;
		LogiTempl = null;
		LogiTempl = new CLogiTemplate[NbTemplate];
		LTOutput = new int[NbTemplate];
		// compute the value of the entry
		if(Mode == M_DIGIT || Mode == M_DIGIT2) {
			entryBinVal=0;
			entryBinVal += (entries[0] ? 1 : 0);
			entryBinVal += (entries[1] ? 2 : 0);
			entryBinVal += (entries[2] ? 4 : 0);
			entryBinVal += (entries[3] ? 8 : 0);
		}
		else
			entryBinVal = -1;
//		if(Mode != M_LIGHT)
//			opDispView.ResizeGrBuff();
//		opDispView.paintBackground();
//		opDispView.repaint();
		generation = 0;
		// Template's Position management
		int tx = 0;
		int ty = 0;
		blocks1.removeAllElements();
		blocks2.removeAllElements();
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

				ty = LogiTempl[i].ctHeight;
				ty *= 1.05;
				my = (ty>my ? ty : my);


				ycrt += my+0;
			}
			else {
				tx += LogiTempl[i].ctWidth;
				tx *=1.05;
				mx = (tx>mx ? tx : mx);
				ty = LogiTempl[i].ctHeight;
				ty *= 1.05;
				my = (ty>my ? ty : my);
			}
			for(int j=0;j < LogiTempl[i].blocks1.size(); j++) {
				blocks1.addElement((CCells)LogiTempl[i].blocks1.elementAt(j));
				blocks2.addElement((CCells)LogiTempl[i].blocks2.elementAt(j));
			}
			LTOutput[i]=CLogiTemplate.LT_NONE;
		}
//		mainView.SetGridSize(mx, ycrt+my);
		drawWorld();
	}

	/** Construct a simple conway pattern */
	void GenConwayPat(int pat) {
		NbTemplate=1;
		LogiTempl=null;
		LogiTempl=new CLogiTemplate[NbTemplate];
		LogiTempl[0]=new CLogiTemplate(this, pat);
		LTOutput=new int[NbTemplate];
//		opDispView.paintBackground();
//		opDispView.repaint();
		generation = 0;
		blocks1.removeAllElements();
		blocks2.removeAllElements();
		for(int j=0;j < LogiTempl[0].blocks1.size(); j++) {
			blocks1.addElement((CCells)LogiTempl[0].blocks1.elementAt(j));
			blocks2.addElement((CCells)LogiTempl[0].blocks2.elementAt(j));
		}
//		if(pat!=CPattern.P_RANDOM && pat!=CPattern.P_BIGUN && pat!=CPattern.P_MAKEGUN)
//			mainView.SetGridSize(500, 500);
//		else
//			mainView.SetGridSize(250, 250);
		drawWorld();
	}

	/** Run the World in View e.g. creates a new pop and draw world. */
	public void Go() {
		genNewBlocs();
		if(generation % DisplayStep==0 || !isRunning) {
			drawWorld();
//			app.DispGen(generation);
		}
		boolean cont=false;
		boolean newstate=false;
		int v;
		for(int i = 0; i < NbTemplate; i++) {
			v = CheckOutput(LogiTempl[i], generation);
			if(v != LTOutput[i]) {
				newstate = true;
				LTOutput[i] = v;
			}
			if(v == CLogiTemplate.LT_NONE)
				cont = true;
		}
		if(newstate)
			DrawOutput();
		if(!cont) {
			SolFound=true;
			stop();
			System.out.println("Solution: "+(System.currentTimeMillis()-Time1)+" millisec.");
//			app.EndSearch(SolFound);
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
	synchronized public void drawWorld() {
//		drawWorld(mainView);
	}

	/** Draw Equation */
	void DrawEquation() {
		if(Mode==M_CONWAY)
			return;
//		for(int i=0;i<NbTemplate;i++)
//			mainView.DispEquation(LogiTempl[i]);
	}

	/** Draw Output */
	void DrawOutput() {
//		opDispView.drawOutput(Mode);
//		opDispView.repaint();
	}

	/** Remove views */
	public void RemoveViews() {
//		mainView.destroy();
//		opDispView.destroy();
	}

	public void stop() {
		isRunning=false;
		logicellUnivThread=null;
//		app.stop();
	}

	/** This is the end... */
	public void end() {
		stop();
		RemoveViews();
	}

	/** Get Info text */
	/*
	String GetInfoText(int mode, int pat) {
		switch(Mode) {
		case M_INIT :
			return(
					"S�lectionnez un mode.");
		case M_EQUATION :
			return(app.Texts.Txt[19]);
		case M_LIGHT :
			return(app.Texts.Txt[20]);
		case M_BINADD :
			return(app.Texts.Txt[21]);
		case M_BINADD2 :
			return(app.Texts.Txt[22]);
		case M_DIGIT :
			return(app.Texts.Txt[23]);
		case M_DIGIT2 :
			return(app.Texts.Txt[24]);
		case M_CONWAY :
		default:
			if(pat==CPattern.P_RPENTO)
				return(app.Texts.Txt[25]);
			if(pat==CPattern.P_ACORN)
				return(app.Texts.Txt[26]);
			if(pat==CPattern.P_RABBITS)
				return(app.Texts.Txt[37]);
			if(pat==CPattern.P_BIGUN)
				return(app.Texts.Txt[38]);
			if(pat==CPattern.P_MAKEGUN)
				return(app.Texts.Txt[39]);
			return(app.Texts.Txt[27]);
		}
	}
	*/

	/** Get Speed */
	public int GetSpeed() {
		return(speed);
	}
	/** Increase speed */
	public void SpeedIncr() {
		SetSpeed(speed - SpeedOffset);
	}
	/** Decrease speed */
	public void SpeedDecr() {
		SetSpeed(speed + SpeedOffset);
	}
	/** Return a Logitempl output state */
	public int GetLtOutputState(int i) {
		return LogiTempl[i].GetOutputState(generation);
	}

	/** Check Ouput Value */
	private int CheckOutput(CLogiTemplate lt, int gen) {
		return(lt.GetOutputState(gen));
	}

	/** Generation of next pop. For each cell, generation of the 3x3
    neighbourhood and process new value. */
	private synchronized void genNewBlocs() {
		// New blocks
		// alternate between using blocks1 and blocks2 as the output being written to.
		blocTmp = (cellsBlocs == blocks1 ? blocks2 : blocks1);
		int sizeCrt = cellsBlocs.size();
		for(int i = 0; i < sizeCrt; i++) {
			// bcrt is the current block being read from in this loop iteration
			CCells bcrt = (CCells) cellsBlocs.elementAt(i);
			// bt is the current block being written to
			CCells bt = (CCells) blocTmp.elementAt(i);
			// set all values in block bt to false
			bt.cellsVal = 0x0L;
			int nbvoisin = 0;
			long vcrt = 0L;
			boolean ccrt = false;
			long cells, nCells, neCells, eCells, seCells, sCells, swCells, wCells, nwCells;
			cells = bcrt.cellsVal;
			// get the values of adjacent blocks. Substitute all false at edges of grid.
			nCells = (bcrt.NBloc != null ? bcrt.NBloc.cellsVal : 0x0L);
			neCells = (bcrt.NEBloc!=null ? bcrt.NEBloc.cellsVal : 0x0L);
			eCells = (bcrt.EBloc!=null ? bcrt.EBloc.cellsVal : 0x0L);
			seCells = (bcrt.SEBloc!=null ? bcrt.SEBloc.cellsVal : 0x0L);
			sCells = (bcrt.SBloc!=null ? bcrt.SBloc.cellsVal : 0x0L);
			swCells = (bcrt.SWBloc!=null ? bcrt.SWBloc.cellsVal : 0x0L);
			wCells = (bcrt.WBloc!=null ? bcrt.WBloc.cellsVal : 0x0L);
			nwCells = (bcrt.NWBloc!=null ? bcrt.NWBloc.cellsVal : 0x0L);

			// North-West corner
			vcrt = ( ( ((0x1L & nwCells) << 2) |
					(0x3L & (nCells >>> 6)) ) << 6 ) |
					( ( ((0x1L & (wCells >>> 56)) << 2) |
							((0x3L & (cells >>> 62))) ) << 3) |
					( ((0x1L & (wCells >>> 48)) << 2) |
							((0x3L & (cells >>> 54))) );
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.NBloc == null) {
				cellsBlocs.addElement(new CCells(cellsBlocs, bcrt.x, bcrt.y-1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x,bcrt.y-1));
				sizeCrt++;
			}
			if(ccrt && bcrt.WBloc == null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}
			if(ccrt && bcrt.NWBloc == null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y-1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y-1));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 63);

			// North Side
			for(int j=1;j<7;j++) {
				vcrt = (0x7L & (nCells >>> (6-j))) << 6 |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(ccrt && bcrt.NBloc==null) {
					cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x,bcrt.y-1));
					blocTmp.addElement(new CCells(blocTmp,bcrt.x,bcrt.y-1));
					sizeCrt++;
				}
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// North-East corner
			vcrt =  ( ( ((0x3L & nCells)<<1) |
					(0x1L & (neCells >>> 7)) ) << 6 ) |
					( ( ((0x3L & (cells >>> 56))<<1) |
							(0x1L & (eCells >>> 63)) ) <<3 ) |
					( ((0x3L & (cells >>> 48)) << 1) |
							(0x1L & (eCells >>> 55)) );
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.NBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x,bcrt.y-1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x,bcrt.y-1));
				sizeCrt++;
			}
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(ccrt && bcrt.NEBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y-1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y-1));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 56);

			// Cell 8 (left, line 2)
			vcrt =  (( (((wCells >>> 56 ) & 0x1L) << 2 ) |
					(0x3L & (cells >>> 62 )) ) <<6 ) |
					(( ( ((wCells >>> 48) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 54 )) ) <<3 ) |
					( ( ((wCells >>> 40) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 46 )));
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}

			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 55);

			// Cells 9 to 14
			for(int j=9;j<15; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 15 (East side)
			vcrt =  (( (((eCells >>> 63) & 0x1L) << 0 ) |
					(0x3L & (cells >>> 56)) << 1) <<6 ) |
					(( ( ((eCells >>> 55) & 0x1L) << 0 ) |
							(0x3L & (cells >>> 48)) << 1) <<3 ) |
					( ( ((eCells >>> 47) & 0x1L) << 0 ) |
							(0x3L & (cells >>> 40))<<1);
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 48);

			// Cell 16 (West Side)
			vcrt =  (( (((wCells >>> 48 ) & 0x1L) << 2 ) |
					(0x3L & (cells >>> 54 )) ) <<6 ) |
					(( ( ((wCells >>> 40) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 46 )) ) <<3 ) |
					( ( ((wCells >>> 32) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 38 )));
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 47);

			// Cells 17 to 22
			for(int j=17;j<23; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;

				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 23 (East side)
			vcrt =  (( (((eCells >>> 55) & 0x1L) << 0 ) |
					(0x3L & (cells >>> 48)) << 1) <<6 ) |
					(( ( ((eCells >>> 47) & 0x1L) << 0 ) |
							(0x3L & (cells >>> 40)) << 1) <<3 ) |
					( ( ((eCells >>> 39) & 0x1L) << 0 ) |
							(0x3L & (cells >>> 32))<<1);
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 40);

			// Cell 24 (West Side)
			vcrt =  (( (((wCells >>> 40 ) & 0x1L) << 2 ) |
					(0x3L & (cells >>> 46 )) ) <<6 ) |
					(( ( ((wCells >>> 32) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 38 )) ) <<3 ) |
					( ( ((wCells >>> 24) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 30 )));
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 39);

			// Cells 25 to 30
			for(int j=25;j<31; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 31 (East side)
			vcrt =  (( (((eCells >>> 47) & 0x1L)  ) |
					(0x3L & (cells >>> 40)) << 1) <<6 ) |
					(( ( ((eCells >>> 39) & 0x1L)  ) |
							(0x3L & (cells >>> 32)) << 1) <<3 ) |
					( ( ((eCells >>> 31) & 0x1L)  ) |
							(0x3L & (cells >>> 24))<<1);
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}

			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 32);

			// Cell 32 (West Side)
			vcrt =  (( (((wCells >>> 32 ) & 0x1L) << 2 ) |
					(0x3L & (cells >>> 38 )) ) <<6 ) |
					(( ( ((wCells >>> 24) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 30 )) ) <<3 ) |
					( ( ((wCells >>> 16) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 22 )));
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}

			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 31);

			// Cells 33 to 38
			for(int j=33;j<39; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 39 (East side)
			vcrt =  (( (((eCells >>> 39) & 0x1L)  ) |
					(0x3L & (cells >>> 32)) << 1) <<6 ) |
					(( ( ((eCells >>> 31) & 0x1L)  ) |
							(0x3L & (cells >>> 24)) << 1) <<3 ) |
					( ( ((eCells >>> 23) & 0x1L)  ) |
							(0x3L & (cells >>> 16))<<1);
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 24);

			// Cell 40 (West Side)
			vcrt =  (( (((wCells >>> 24 ) & 0x1L) << 2 ) |
					(0x3L & (cells >>> 30 )) ) <<6 ) |
					(( ( ((wCells >>> 16) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 22 )) ) <<3 ) |
					( ( ((wCells >>> 8) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 14 )));
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 23);

			// Cells 41 to 46
			for(int j=41;j<47; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 47 (East side)
			vcrt =  (( (((eCells >>> 31) & 0x1L)  ) |
					(0x3L & (cells >>> 24)) << 1) <<6 ) |
					(( ( ((eCells >>> 23) & 0x1L)  ) |
							(0x3L & (cells >>> 16)) << 1) <<3 ) |
					( ( ((eCells >>> 15) & 0x1L)  ) |
							(0x3L & (cells >>> 8))<<1);
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 16);

			// Cell 48 (West Side)
			vcrt =  (( (((wCells >>> 16 ) & 0x1L) << 2 ) |
					(0x3L & (cells >>> 22 )) ) <<6 ) |
					(( ( ((wCells >>> 8) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 14 )) ) <<3 ) |
					( ( ((wCells >>> 0) & 0x1L) << 2 ) |
							(0x3L & (cells >>> 6 )));
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 15);

			// Cells 49 to 54
			for(int j=49;j<55; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( 0x7L & (cells >>> (45-(j-9))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 55 (East side)
			vcrt =  (( (((eCells >>> 23) & 0x1L) ) |
					(0x3L & (cells >>> 16)) << 1) <<6 ) |
					(( ( ((eCells >>> 15) & 0x1L)  ) |
							(0x3L & (cells >>> 8)) << 1) <<3 ) |
					( ( ((eCells >>> 7) & 0x1L)  ) |
							(0x3L & (cells >>> 0))<<1);
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 8);

			// Cell 56 (SW corner)
			vcrt =  ( ( ((0x1L & (wCells >>> 8))<<2) |
					(0x3L & (cells >>> 14)) ) << 6 ) |
					( ( ((0x1L & (wCells >>> 0))<<2) |
							(0x3L & (cells >>> 6)) ) << 3 ) |
					( ((0x1L & (swCells >>> 56)) << 2) |
							(0x3L & (sCells >>> 62)) );
			ccrt = ((vcrt >> 4) & 0x1L) >0;
			if(ccrt && bcrt.SBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x,bcrt.y+1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x,bcrt.y+1));
				sizeCrt++;
			}
			if(ccrt && bcrt.WBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y));
				sizeCrt++;
			}
			if(ccrt && bcrt.SWBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x-1,bcrt.y+1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x-1,bcrt.y+1));
				sizeCrt++;
			}
			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L << 7);

			// Cells 57-62 (South side)
			for(int j=57; j<63; j++) {
				vcrt =  ( (0x7L & (cells >>> (61-(j-9)))) << 6 ) |
						( (0x7L & (cells >>> (53-(j-9)))) << 3 ) |
						( (0x7L & (sCells >>> (56+(62-j)))) );
				ccrt = ((vcrt >> 4) & 0x1L) >0;
				if(ccrt && bcrt.SBloc==null) {
					cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x,bcrt.y+1));
					blocTmp.addElement(new CCells(blocTmp,bcrt.x,bcrt.y+1));
					sizeCrt++;
				}
				if(lookUp[(int)vcrt])
					bt.cellsVal |= (0x1L << 63-j);
			}

			// Cell 63 (SE corner)
			vcrt =  ( ( ((0x3L & (cells >>> 8)) << 1) |
					(0x1L & (eCells >>> 15)) ) << 6 ) |
					( ( ((0x3L & (cells >>> 0)) << 1) |
							(0x1L & (eCells >>> 7)) ) <<3 ) |
					( ((0x3L & (sCells >>> 56)) << 1) |
							((0x1L & (seCells >>> 63))) );
			ccrt = ((vcrt >> 4) & 0x1L) > 0;
			if(ccrt && bcrt.SBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x,bcrt.y+1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x,bcrt.y+1));
				sizeCrt++;
			}
			if(ccrt && bcrt.EBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y));
				sizeCrt++;
			}
			if(ccrt && bcrt.SEBloc==null) {
				cellsBlocs.addElement(new CCells(cellsBlocs,bcrt.x+1,bcrt.y+1));
				blocTmp.addElement(new CCells(blocTmp,bcrt.x+1,bcrt.y+1));
				sizeCrt++;
			}

			if(lookUp[(int)vcrt])
				bt.cellsVal |= (0x1L);

		} // For each block
		// remove empty blocks every "RemoveEmptyGen" generations if Conway Mode
		if(Mode==M_CONWAY) {
			if(generation % RemoveEmptyGen == 0) {
				CCells bt, bt2;
				for (int i = 0; i<blocTmp.size(); i++) {
					bt = (CCells) (blocTmp.elementAt(i));
					bt2 = (CCells) (cellsBlocs.elementAt(i));
					if (bt.cellsVal == 0x0L) {
						bt.DestroyBloc();
						blocTmp.removeElementAt(i);
						bt2.DestroyBloc();
						cellsBlocs.removeElementAt(i);
					}
				}
			}
		}

		// swap lists
		cellsBlocs = blocTmp;
		generation++;
	}

	/** Init lookup table */
	private void initLookUp() {
		int v;
		boolean c;
		for (int i=0; i<512; i++) {
			lookUp[i] = false;;
			c = ((i >> 4) & 0x1L) > 0;
			v = neighbour(i, c);
			if ((c && v == 2 || v==3) || (!c && v == 3))
				lookUp[i] = true;
		}
	}

	/** Calc #neighbour */
	private int neighbour(long v, boolean c) {
		int n = 0;
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
	/*
	private void drawWorld(JPView jpv) {
		CLogicellView bv = (CLogicellView) jpv;
		bv.PaintBackground();

		if(Mode==M_INIT)
			bv.DispBkgText();
		else   {
			for(int i = 0; i < cellsBlocs.size(); i++)
				((CCells) cellsBlocs.elementAt(i)).Live(bv);
		}
		bv.repaint();
	}
	*/

	/** Set speed */
	private void SetSpeed(int s) {
		speed=Math.max(s,0);
		DisplayStep=(speed>0 ? 1 : DefaultDisplayStep);
	}

	public Vector getBlocks() {
		return cellsBlocs;
	}
}


