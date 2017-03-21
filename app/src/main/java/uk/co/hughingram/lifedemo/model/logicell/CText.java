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

/** Texts for language management */ 
public class CText {

  String Txt[]=new String[40];

  public CText(int lang) {
    switch(lang) {
      case Logicell.LANGEN:
        Txt[0]="Step";
        Txt[1]="Go";
        Txt[2]="Stop";
        Txt[3]="Gen:";
        Txt[4]="And";
        Txt[5]="Or";
        Txt[6]="Not";
        Txt[7]="Xor";
        Txt[8]="New";
        Txt[9]="Two-way";
        Txt[10]="Digit";
        Txt[11]="Equation";
        Txt[12]="Pento";
        Txt[13]="Digit 2";
        Txt[14]="BinAdd";
        Txt[15]="BinAdd 2";
        Txt[16]="Acor";
        Txt[17]="Rand";
        Txt[18]="About";
        Txt[19]=
          "*** EQUATION ***\n\n"+
          "Enter an equation, set boolean entries ABCD\n"+
          "and test the result.\n\n"+
          "Theoretically in Logicell the number of argument\n"+
          "is not limited. Entries can be repeated indefinitely.\n\n";
        Txt[20]=
          "*** Two-way switch ***\n\n"+
          "Any change of a switch A or B will reverse\n"+
          "the light's state.\n\n"+
          "That's what you have in your corridor.\n\n"+
          "P400 response time : 5''.";
        Txt[21]=
          "*** 1 bit Binary Adder ***\n\n"+
          "Evaluate the binary sum A+B\n"+
          "with True=1, False=0.\n\n"+
          "You will have a 2 bits result :\n"+
          "0+0=00, 1+0=01, 0+1=01, 1+1=10\n"+
          "Reminder : 10b=2.\n\n"+
          "P400 response time : 5''.";
        Txt[22]=
          "*** 2 bits Binary adder ***\n\n"+
          "Evaluate the binary sum BA+DC\n"+
          "with True=1, False=0.\n\n"+
          "You will have a 3 bits result.\n"+
          "For example : BA=10b=2, DC=01b=1, BA+DC=11b=3.\n"+
          "Reminder : 11b=3.\n\n"+
          "P400 response time : 2'30''.";
        Txt[23]=
          "*** 7 segments digital display ***\n\n"+
          "Choose a binary between 0 and 9\n"+
          "with the 4 entry bits.\n\n"+
          "A is the low weight bit,\n"+
          "for example : D=0, C=1, B=0, A=1 = 0101b = 5.\n\n"+
          "In this mode, 3 segments are given to accelerate\n"+
          "the search.\n\n"+
          "P400 response time : 45''.";
        Txt[24]=
          "*** 7 segments digital display ***\n\n"+
          "Choose a binary between 0 and 9\n"+
          "with the 4 entry bits.\n\n"+
          "Warning : A is the low weight bit,\n"+
          "for example : D=0, C=1, B=0, A=1 = 0101b = 5.\n\n"+
          "In this mode, the 7 segments are managed\n"+
          "the search.\n\n"+
          "P400 response time : 5'30''";
        Txt[25]=
            "*** CONWAY : Rpento ***\n\n"+
            "From 5 cells, this pattern evolves\n"+
            "during 1103 generations.";
        Txt[26]=
            "*** CONWAY : Acorn ***\n\n"+
            "From 7 cells, this pattern evolves\n"+
            "during 5206 generations.";
        Txt[27]=
          "*** CONWAY : Random ***\n\n"+
          "128 lines by 64 columns\n"+
          "random filling.";
        Txt[28]=
          "The maximum value is 9 !";
        Txt[29]=
         "Value : ";
        Txt[30]=
          "Working...";
        Txt[31]=
          "Speed+";
        Txt[32]=
          "Speed-";
        Txt[33]=
          "Sum: ";
        Txt[34]=
          "Rab.";
        Txt[35]=
          "BiG.";
        Txt[36]=
          "G30";
        Txt[37]=
            "*** CONWAY : Rabbits ***\n\n"+
            "From 9 cells, this pattern evolves\n"+
            "during 17330 generations.";
        Txt[38]=
            "*** CONWAY : Bi-Gun ***\n\n"+
            "This pattern makes a bi-gun.";
        Txt[39]=
            "*** CONWAY : Make Gun ***\n\n"+
            "This pattern makes the classical p30 gun\n"+
            "wich is the basic pattern of Logicell.";
      break;
      default:
        Txt[0]="Pas à pas";
        Txt[1]="Go";
        Txt[2]="Stop";
        Txt[3]="Gen:";
        Txt[4]="Et";
        Txt[5]="Ou";
        Txt[6]="Non";
        Txt[7]="Xor";
        Txt[8]="Nouv.";
        Txt[9]="Va-et-vient";
        Txt[10]="Afficheur";
        Txt[11]="Equation";
        Txt[12]="Pento";
        Txt[13]="Afficheur 2";
        Txt[14]="BinAdd";
        Txt[15]="BinAdd 2";
        Txt[16]="Glan";
        Txt[17]="Aléa";
        Txt[18]="A Propos";
        Txt[19]=
          "*** EQUATION ***\n\n"+
          "Saisissez une équation, fixez la valeur des entrées\n"+
          "ABCD et testez le résultat.\n\n"+
          "En théorie LogiCell n'est pas limité\n"+
          "dans le nombre d'arguments. Toute entrée\n"+
          "peut-être répétée autant que nécessaire.\n\n";
        Txt[20]=
          "*** VA-ET-VIENT ***\n\n"+
          "L'appui sur l'un des 2 interrupteurs\n"+
          "A ou B, inverse l'état de l'ampoule.\n\n"+
          "C'est le système que vous trouvez à chacune\n"+
          "des extrémités de votre couloir.\n"+
          "Temps de réponse sur un P400 : 5''.";
        Txt[21]=
          "*** Additionneur binaire sur 1 bit ***\n\n"+
          "Calculez le résultat de l'addition\n"+
          "binaire A+B.\n"+
          "avec Vrai=1, Faux=0.\n\n"+
          "Le résultat s'affiche sur 2 bits.\n"+
          "On a :\n"+
          "0+0=00, 1+0=01, 0+1=01, 1+1=10\n"+
          "On rappelle qu'en binaire: 10b=2.\n"+
          "Temps de réponse sur un P400 : 5''.";
        Txt[22]=
          "*** Additionneur binaire sur 2 fois 2 bits ***\n\n"+
          "Calculez le résultat de l'addition\n"+
          "binaire BA+DC\n"+
          "avec Vrai=1, Faux=0.\n\n"+
          "Le résultat s'affiche sur 3 bits.\n"+
          "Par exemple : BA=10b=2, DC=01b=1, BA+DC=11b=3.\n"+
          "On rappelle qu'en binaire 11b=3.\n"+
          "Temps de réponse sur un P400 : 2'30''.";
        Txt[23]=
          "*** Afficheur digital 7 segments ***\n\n"+
          "Choisissez un nombre binaire entre 0 et 9\n"+
          "à partir des 4 bits d'entrées.\n"+
          "Attention : A est le bit de poids faible, \n"+
          "soit par exemple D=0, C=1, B=0, A=1 = 0101 = 5.\n\n"+
          "Dans ce mode, 3 segments sont donnés pour accélérer\n"+
          "la recherche.\n"+
          "Temps de réponse sur un P400 : 45''.";
        Txt[24]=
          "*** Afficheur digital 7 segments ***\n\n"+
          "Choisissez un nombre binaire entre 0 et 9\n"+
          "à partir des 4 bits d'entrées.\n\n"+
          "Attention : A est le bit de poids faible, \n"+
          "soit par exemple D=0, C=1, B=0, A=1 = 0101 = 5.\n\n"+
          "Dans ce mode, les 7 segments sont gérés.\n"+
          "Temps de réponse sur un P400 : 5'30''.";
        Txt[25]=
            "*** CONWAY : Rpento ***\n\n"+
            "A partir de 5 cellules, cette figure\n"+
            "évolue durant 1103 générations.";
        Txt[26]=
            "*** CONWAY : Gland ***\n\n"+
            "A partir de 7 cellules, cette figure\n"+
            "évolue durant 5206 générations.";
        Txt[27]=
          "*** CONWAY : Random ***\n\n"+
          "remplissage aléatoire\n"+
          "de 128 lignes par 64 colonnes.";
        Txt[28]=
          "La valeur maximum 9 !";
        Txt[29]=
         "Valeur : ";
        Txt[30]=
          "Construction en cours...";
        Txt[31]=
          "Vites.+";
        Txt[32]=
          "Vites.-";
        Txt[33]=
          "Somme: ";
        Txt[34]=
          "Lapin";
        Txt[35]=
          "BiG.";
        Txt[36]=
          "G30";
        Txt[37]=
            "*** CONWAY : Lapins ***\n\n"+
            "A partir de 9 cellules, cette figure\n"+
            "évolue durant 17330 générations.";
        Txt[38]=
            "*** CONWAY : Bi-Gun ***\n\n"+
            "Cette figure génère un canon double.";
        Txt[39]=
            "*** CONWAY : Constructeur de canon ***\n\n"+
            "Cette figure génère le très fameux canon\n"+
            "de période 30.\n"+
            "Ce canon est à la base de Logicell.";

    }
  }

}