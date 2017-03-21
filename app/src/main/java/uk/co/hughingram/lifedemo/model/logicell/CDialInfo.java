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

/** Info box management */
public class CDialInfo extends Dialog {

  CLogicellUI CallingApp;
  Panel panel1 = new Panel();
  BorderLayout borderLayout1 = new BorderLayout();
  Panel panel2 = new Panel();
  Button button1 = new Button();
  TextArea textArea1 = new TextArea();

  public CDialInfo(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    CallingApp=(CLogicellUI)frame;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
      add(panel1);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
 

  public CDialInfo(Frame frame) {
    this(frame, "", false);
  }

  public CDialInfo(Frame frame, boolean modal) {
    this(frame, "", modal);
  }

  public CDialInfo(Frame frame, String title) {
    this(frame, title, false);
  }

  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    button1.setLabel("OK");
    button1.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        button1_mouseClicked(e);
      }
    });
    textArea1.setText("");
    textArea1.setEditable(false);
    panel1.add(panel2, BorderLayout.SOUTH);
    panel2.add(button1, null);
    panel1.add(textArea1, BorderLayout.CENTER);
    String s=CallingApp.World.GetInfoText(CallingApp.World.GetMode(),
                         CallingApp.ConwPatCrt);
    textArea1.setText(s);
  }

  protected void processWindowEvent(WindowEvent e) {
    if(e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  void cancel() {
    dispose();
  }

  void button1_mouseClicked(MouseEvent e) {
    cancel();
  }
} 