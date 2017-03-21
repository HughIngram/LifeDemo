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
import java.applet.*;

/** Browser interface for Logicell */
public class Logicell extends Applet {
  static final int LANGFR=0;
  static final int LANGEN=1;

  int Language=0;
  boolean isStandalone = false;
  GridLayout gridLayout1 = new GridLayout();
  Button button1 = new Button();
  /** Light images */
  Image ImLightOff, ImLightOn, ImConway;
  /** User Interface */
  CLogicellUI LogicellUI;
  //Construire l'applet
  public Logicell() {
  }
  //Initialiser l'applet
  public void init() {
    try { Language = Integer.parseInt(getParameter("Language", ""));
    } catch (Exception e) { e.printStackTrace(); }
    try  {
      jbInit();
    }
    catch(Exception e)  {
      e.printStackTrace();
    }
  }

  /**Component Init. */
  private void jbInit() throws Exception {
    if(Language==LANGEN)
      button1.setLabel("Push to lauch applet");
    else
      button1.setLabel("Appuyez pour démarrer");
    button1.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        button1_actionPerformed(e);
      }
    });
    this.setLayout(gridLayout1);
    this.add(button1, null);
  }

  public String getAppletInfo() {
    return "Information applet";
  }

  //Obtenir une valeur de paramètre
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }


  public String[][] getParameterInfo() {
    return null;
  }

  public String GetVersionNum() {
    return(GetVersion()+"-"+GetVersionDate());
  }

  public String GetVersion() {
    return("1.0");
  }
  public String GetVersionDate() {
    return("28/10/2000");
  }
  public String GetName() {
    return("LogiCell");
  }

  public String GetAuthor() {
    return("Jean-Philippe Rennard");
  }

  public String GetCopyright() {
    return("Copyright(c)-"+GetAuthor()+"-2000");
  }
  public String GetInfos() {
    String s;
    if(Language==LANGEN)
      s="\nCellular automata and boolean functions.\n";
    else
      s="\nAutomate cellulaire et fonctions booléennes.\n";
    return(
      GetCopyright()+"\n"+s+
      "\nwww.rennard.org/alife"+"\n"+
      "alife@rennard.org");
  }
  /** Launch Logicell App */
  void button1_actionPerformed(ActionEvent e) {
    ImLightOn=getImage(getCodeBase(), "LightOn.gif");
    ImLightOff=getImage(getCodeBase(), "LightOff.gif");
    ImConway=getImage(getCodeBase(), "Conway.gif");

    LogicellUI=new CLogicellUI(this,Language);
    Dimension dim= getToolkit().getScreenSize();
    LogicellUI.setSize(Math.max((int)(0.75*dim.width),600),
                        Math.max((int)(0.75*dim.height),440));

//    LogicellUI.setSize(800,600);
//    GavApp.setLocation(dim.width/2-GavApp.getSize().width/2,
//            dim.height/2-GavApp.getSize().height/2);


    LogicellUI.setLocation(20,20);  // Or doesn't work properly under Linux !
    LogicellUI.show();
    LogicellUI.start();

  }

  public void destroy() {
    if(LogicellUI!=null)
      LogicellUI.End();
  }
}

