package net.geral.lib.edt;

//
//import javax.swing.JComponent;
//import javax.swing.SwingUtilities;
//
////adapted from: http://www.java.net/blog/alexfromsun/archive/2006/02/debugging_swing.html
//aspect EdtRuleChecker {
//  private boolean             isStressChecking = true;
//
//  public pointcut anySwingMethods(JComponent c):
//             target(c) && call(* *(..));
//
//  public pointcut threadSafeMethods():         
//             call(* repaint(..)) || 
//             call(* revalidate()) ||
//             call(* invalidate()) ||
//             call(* getListeners(..)) ||
//             call(* add*Listener(..)) ||
//             call(* remove*Listener(..));
//
//  // calls of any JComponent method, including subclasses
//  before(JComponent c): anySwingMethods(c) && 
//                              !threadSafeMethods() &&
//                              !within(EdtRuleChecker) { 
//    if (!SwingUtilities.isEventDispatchThread()
//        && (isStressChecking || c.isShowing())) {
//      throw new EdtViolationException(thisJoinPoint.getSourceLocation() , thisJoinPoint.getSignature() ,false);
//    }
//  } 
//
//  // calls of any JComponent constructor, including subclasses
//  before(): call(JComponent+.new(..)) {
//    if (isStressChecking && !SwingUtilities.isEventDispatchThread()) {
//      throw new EdtViolationException(thisJoinPoint.getSourceLocation() , thisJoinPoint.getSignature() ,true);
//    }
//  }
// }
