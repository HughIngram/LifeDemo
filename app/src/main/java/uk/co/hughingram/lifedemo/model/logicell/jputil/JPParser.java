//***************************************************************************
//  JPParser 1.0
//    10/9/2000
//    Some parsing functions
//
//    Jean-Philippe Rennard
//    alife@rennard.org
//    http://www.rennard.org/alife
//***************************************************************************
package uk.co.hughingram.lifedemo.model.logicell.jputil;

import java.util.*;

public class JPParser {

  public final static char OP_NOT='~';
  public final static char OP_AND='^';
  public final static char OP_OR='v';
  public final static char OP_XOR='w';

  public JPParser() {
  }

  /** Transform an Infixed expression to Postfixed for evaluation.
  strinf assumed to be syntactically correct. */
  public static String InFixToPostFix(String strinf) {
    String StrPost="";
    int IndInf, IndPost;
    char C, Elem;
    boolean More;
    Stack St=new Stack();
    IndInf=IndPost=0;
    strinf += "#";
    C=strinf.charAt(IndInf);
    while(C!='#') {
      if(IsBoolOper(C)) {
        More=!St.empty();
        while(More) {
          if(OperPriority( ((Character)St.peek()).charValue() )>=OperPriority(C)) {
            Elem=((Character)St.pop()).charValue();
            StrPost += Elem;
            More=!St.empty();
          }
          else
            More=false;
        }
        St.push(new Character(C));
      } // is Oper
      else if(C!='(' && C!=')') {
        StrPost += C;
      }
      else if(C==')') {
        Elem=((Character)St.pop()).charValue();
        while(Elem!='(') {
          StrPost += Elem;
          Elem=((Character)St.pop()).charValue();
        }
      }
      else {
        St.push(new Character('('));
      }
      IndInf++;
      C=strinf.charAt(IndInf);
    } // while !#
    while(!St.empty())
      StrPost += ((Character)St.pop()).charValue();
    return(StrPost);
  }

  /** test if a char is an operator */
  public static boolean IsOper(char c) {
    return(IsBoolOper(c));
  }

  /** Test if a char is a boolean operator */
  public static boolean IsBoolOper(char o) {
    if(o==OP_AND || o==OP_OR || o==OP_NOT || o==OP_XOR)
      return(true);
    return(false);
  }
  /** Test if a char is a boolean not unary operator */
  public static boolean IsBoolBinaryOper(char o) {
    if(o==OP_AND || o==OP_OR || o==OP_XOR)
      return(true);
    return(false);
  }

  /** Test if a char is a unary operator */
  public static boolean IsUnaryOper(char op) {
    if(op==OP_NOT)
      return(true);
    return(false);
  }
  
  /** Returns # of entries in a string */
  public static int NbEntry(String str) {
    int n=0;
    char c;
    for(int i=0;i<str.length();i++) {
      c=str.charAt(i);
      if(!IsOper(c))
        n++;
    }
    return(n);
  }

  /** Returns priority level of an operator. -1 if unknown */
  static private int OperPriority(char op) {
    if(op=='(')
      return(0);
    if(op==OP_AND)
      return(1);
    if(op==OP_OR)
      return(1);
    if(op==OP_NOT)
      return(2);
    return(-1);
  }

  /** Manages special connectors : xor ... */
  public static String SetConnectors(String entry) {
    char c;
    int sl=0;
    int sr=0;
    int el=0;
    int er=0;
    String s="";
    String expl="";
    String expr="";
    boolean found=true;
    while(found) {
      for(int i=0; i<entry.length();i++) {
        found=false;
        c=entry.charAt(i);
        if(c==JPParser.OP_XOR) {
          found=true;
          el=i+1;
          er=CalcPosEndExpr(entry, el);
          sr=i-1;
          sl=CalcPosBegExpr(entry, sr);
          expl=entry.substring(sl,sr+1);
          expr=entry.substring(el,er+1);
          s=entry.substring(0,sl);
          s += "(("+expl+OP_OR+expr+")^~("+expl+OP_AND+expr+"))";
          s += entry.substring(er+1);
          entry=s;
          break;
        }
      } // for entry length
    } // while found
    return(entry);
  }

  /** Calc the begin pos of an expression */
  static private int CalcPosBegExpr(String entry, int sr) {
    String s=entry.substring(0,sr+1);
    int pos=s.length()-1;
    if(sr==0)
      return(sr);
    if(s.charAt(pos)!=')')
      return(sr);
    int op=0;
    int cp=1;
    char c;
    for(int i=pos-1; i>=0; i--) {
      c=s.charAt(i);
      if(c==')')
        cp++;
      if(c=='(')
        op++;
      if(op==cp)
        return(i);
    }
    return(sr);
  }

  /** Calc the final pos of an expression */
  static private int CalcPosEndExpr(String entry, int el) {
    String s=entry.substring(el);
    int base=1;
    if(s.charAt(0)==JPParser.OP_NOT) {
      if(s.charAt(1)!='(')
        return(el+1);
      base++;
    }
    else if(s.charAt(0)!='(')
      return(el);
    int op=1;
    int cp=0;
    char c;
    for(int i=base; i<s.length();i++) {
      c=s.charAt(i);
      if(c=='(')
        op++;
      if(c==')')
        cp++;
      if(op==cp)
        return(el+i);
    }
    return(el);
  }

} 