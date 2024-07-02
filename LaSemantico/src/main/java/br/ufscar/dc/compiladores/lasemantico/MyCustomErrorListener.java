package br.ufscar.dc.compiladores.lasemantico;

import java.io.PrintWriter;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

/**
 *
 * @author vinij
 */
public class MyCustomErrorListener implements ANTLRErrorListener {
    
    PrintWriter pw;
    public MyCustomErrorListener(PrintWriter pw) {
       this.pw = pw;    
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String tokenText = ((Token) offendingSymbol).getText();
        if (tokenText.equals("<EOF>")) {
            tokenText = "EOF";
        }
        pw.printf("Linha %d: erro sintatico proximo a %s%n", line, tokenText);
        pw.println("Fim da compilacao");
        pw.flush();
        System.exit(1);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean bln, java.util.BitSet bitset, ATNConfigSet atncs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, java.util.BitSet bitset, ATNConfigSet atncs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atncs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}