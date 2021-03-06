/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.5                                                               *
 * Copyright (C) 1998-2009  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JavaScript target adapted for http://autotagcloud.com/                  *
 * Oleg Mazko.                                                             *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

/**
 * HiLowEmitter
 * 
 * @author Gerwin Klein
 * @version $Revision: 586 $, $Date: 2010-03-07 10:59:36 +0200 (Вск, 07 Мар 2010) $
 */
public class HiLowEmitter extends PackEmitter {

  /** number of entries in expanded array */
  private int numEntries;

  /**
   * Create new emitter for values in [0, 0xFFFFFFFF] using hi/low encoding.
   * 
   * @param name   the name of the generated array
   */
  public HiLowEmitter(String name) {
    super(name);
  }

  /**
   * Emits hi/low pair unpacking code for the generated array. 
   * 
   * @see jflex.PackEmitter#emitUnpack()
   */
  public void emitUnpack() {
    // close last string chunk:
    println("\";");
    nl();
//    println("  private static int [] zzUnpack"+name+"() {");
//    println("    int [] result = new int["+numEntries+"];");
//    println("    int offset = 0;");
    println("  function zzUnpack"+name+"() {");
    println("    var result = new Array("+numEntries+");");
    println("    var offset = 0;");

    for (int i = 0; i < chunks; i++) {
      println("    offset = zzUnpack"+name+"_helper("+constName()+"_PACKED_"+i+", offset, result);");
    }

    println("    return result;");
    println("  }");

    nl();
//    println("  private static int zzUnpack"+name+"(String packed, int offset, int [] result) {");
//    println("    int i = 0;  /* index in packed string  */");
//    println("    int j = offset;  /* index in unpacked array */");
//    println("    int l = packed.length();");
    println("  function zzUnpack"+name+"_helper(packed, offset, result) {");
    println("    var i = 0;  /* index in packed string  */");
    println("    var j = offset;  /* index in unpacked array */");
    println("    var l = packed.length;");
    println("    while (i < l) {");
//    println("      int high = packed.charAt(i++) << 16;");
//    println("      result[j++] = high | packed.charAt(i++);");
    println("      var high = packed.charCodeAt(i++) << 16;");
    println("      result[j++] = high | packed.charCodeAt(i++);");
    println("    }");
    println("    return j;");
    println("  }");

    super.emitUnpack();
  }

  /**
   * Emit one value using two characters. 
   *
   * @param val  the value to emit
   * @prec  0 <= val <= 0xFFFFFFFF 
   */
  public void emit(int val) {
    numEntries+= 1;
    breaks();
    emitUC(val >> 16);
    emitUC(val & 0xFFFF);        
  }
}
