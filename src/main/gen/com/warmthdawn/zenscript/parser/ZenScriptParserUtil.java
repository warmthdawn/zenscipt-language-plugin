package com.warmthdawn.zenscript.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import java.util.Collection;

public class ZenScriptParserUtil extends GeneratedParserUtilBase {
    public static boolean any(PsiBuilder builder, int level) {
        builder.advanceLexer();
        return true;
    }

}
