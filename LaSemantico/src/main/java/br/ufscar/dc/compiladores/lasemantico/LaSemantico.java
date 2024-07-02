/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.ufscar.dc.compiladores.lasemantico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

/**
 *
 * @author vinij
 */
public class LaSemantico {

   public static void main(String[] args) throws FileNotFoundException, IOException {
       System.out.println("teste");
       
        try (PrintWriter pw = new PrintWriter(new File(args[1]))) {            
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lexer = new LALexer(cs);
            
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LAParser parser = new LAParser(tokens);

            Token token = null;
            while( (token = lexer.nextToken()).getType() != Token.EOF) {
                String nomeToken = lexer.VOCABULARY.getDisplayName(token.getType());
                if (nomeToken.equals("ERRO")) {
                    String erroString = "Linha " + token.getLine()+ ": " + token.getText() + " - simbolo nao identificado";
                    pw.println(erroString);
                    pw.println("Fim da compilacao");
                    pw.flush();
                    System.exit(1);
                    break;
                } else if (nomeToken.equals("COMENTARIO_NAO_FECHADO")) {
                    String erroString = "Linha " + token.getLine() + ": comentario nao fechado";
                    pw.println(erroString);
                    pw.println("Fim da compilacao");
                    pw.flush();
                    System.exit(1);
                    break;
                } else if (nomeToken.equals("CADEIA_NAO_FECHADA"))  {
                    String erroString = "Linha " + token.getLine() + ": cadeia literal nao fechada";
                    pw.println(erroString);
                    pw.println("Fim da compilacao");
                    pw.flush();
                    System.exit(1);
                    break;
                }
            }

            lexer.setInputStream(CharStreams.fromFileName(args[0]));
            parser.removeParseListeners();
            MyCustomErrorListener mcel = new MyCustomErrorListener(pw);
            parser.addErrorListener(mcel);

            parser.programa();
        }
    }
}
