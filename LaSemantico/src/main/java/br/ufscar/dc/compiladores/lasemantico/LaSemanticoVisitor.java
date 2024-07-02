/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufscar.dc.compiladores.lasemantico;
import org.antlr.v4.runtime.Token;
/**
 *
 * @author vinij
 */
public class LaSemanticoVisitor extends LABaseVisitor<Void> {

    TabelaDeSimbolos tabela;    
    
    
    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        tabela = new TabelaDeSimbolos();
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDecl_local_global(LAParser.Decl_local_globalContext ctx) {
        LAParser.VariavelContext minhaVariavel = ctx.declaracao_local().;
        
        Token t = ctx.declaracao_local().variavel().DOIS_PONTOS();
        
        String nomeVar = ctx.declaracao_local().variavel().getText();
        String strTipoVar = ctx.declaracao_local().tipo_basico().getText();
        TabelaDeSimbolos.TipoLA tipoVar = TabelaDeSimbolos.TipoLA.INVALIDO;
        switch (strTipoVar) {
            case "inteiro":
                tipoVar = TabelaDeSimbolos.TipoLA.INTEIRO;
                break;
            case "real":
                tipoVar = TabelaDeSimbolos.TipoLA.REAL;
                break;
            case "literal":
                tipoVar = TabelaDeSimbolos.TipoLA.LITERAL;
                break;
            case "logico":
                tipoVar = TabelaDeSimbolos.TipoLA.LITERAL;
                break;
            default:
                // Nunca irá acontecer, pois o analisador sintático
                // não permite
                break;
        }

        // Verificar se a variável já foi declarada
        if (tabela.existe(nomeVar)) {
            LaSemanticoUtils.adicionarErroSemantico(ctx.declaracao_local().variavel()., "Variável " + nomeVar + " já existe");
        } else {
            tabela.adicionar(nomeVar, tipoVar);
        }

        return super.visitDeclaracao(ctx);
    }

    @Override
    public Void visitExpressao(LAParser.ExpressaoContext ctx) {
        return super.visitExpressao(ctx); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Void visitTermo(LAParser.TermoContext ctx) {
        return super.visitTermo(ctx); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        return super.visitDeclaracao_local(ctx); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    @Override
    public Void visitDeclaracoes(LAParser.DeclaracoesContext ctx) {
        return super.visitDeclaracoes(ctx); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        return super.visitVariavel(ctx); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
}
