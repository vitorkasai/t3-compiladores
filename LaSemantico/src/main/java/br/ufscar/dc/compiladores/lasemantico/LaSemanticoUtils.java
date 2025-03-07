package br.ufscar.dc.compiladores.lasemantico;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

/**
 *
 * @author vinij
 */
public class LaSemanticoUtils {
    public static List<String> errosSemanticos = new ArrayList<>();

    public static void adicionarErroSemantico(Token token, String mensagem) {
        int linha = token.getLine();
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }

    
    // Verificar o tipo da expressao
    public static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.ExpressaoContext ctx) {
        TabelaDeSimbolos.TipoLA ret = null;
        
        // Vamos verificar o tipo para cada termo lógico que está na expressao
        for (LAParser.Termo_logicoContext ta : ctx.termo_logico()) {
            TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoLA.INVALIDO) {
                ret = TabelaDeSimbolos.TipoLA.INVALIDO;
            }
        }
        return ret;
    }

    // Verificar o tipo do termo
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Termo_logicoContext ctx) {
        TabelaDeSimbolos.TipoLA ret = null;
        
        // Verificar o tipo para cada fator dentro do termo
        for (LAParser.Fator_logicoContext ta : ctx.fator_logico()) {
            TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoLA.INVALIDO) {
                ret = TabelaDeSimbolos.TipoLA.INVALIDO;
            }
        }

        return ret;
    }

    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Fator_logicoContext ctx) {
        return verificarTipo(escopos, ctx.parcela_logica());
    }

    
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_logicaContext ctx) {
        TabelaDeSimbolos.TipoLA ret;
        
        // Verificar se a parcela contém uma expressão relacional
        if (ctx.exp_relacional() != null) {
            // Caso tenha, então vamos identificar o tipo
            ret = verificarTipo(escopos, ctx.exp_relacional());
        } else {
            // Se não tiver, pegamos o tipo lógico
            ret = TabelaDeSimbolos.TipoLA.LOGICO;
        }

        return ret;
    }

    // Vamos verificar os tipos das expressões relacionais que temos
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Exp_relacionalContext ctx) {
        TabelaDeSimbolos.TipoLA ret = null;
        
        // Tendo um operador relacional
        if (ctx.op_relacional() != null) {
            // Percorre expressões aritméticas
            for (LAParser.Exp_aritmeticaContext ta : ctx.exp_aritmetica()) {
                //Vamos verificar o tipo de cada uma
                TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, ta);
                Boolean auxNumeric = aux == TabelaDeSimbolos.TipoLA.REAL || aux == TabelaDeSimbolos.TipoLA.INTEIRO;
                Boolean retNumeric = ret == TabelaDeSimbolos.TipoLA.REAL || ret == TabelaDeSimbolos.TipoLA.INTEIRO;
                if (ret == null) {
                    ret = aux;
                } else if (!(auxNumeric && retNumeric) && aux != ret) {
                    ret = TabelaDeSimbolos.TipoLA.INVALIDO;
                }
            }
            if (ret != TabelaDeSimbolos.TipoLA.INVALIDO) {
                ret = TabelaDeSimbolos.TipoLA.LOGICO;
            }
        } else {
            ret = verificarTipo(escopos, ctx.exp_aritmetica(0));
        }

        return ret;
    }

    
    // Verificando o tipo da expressão aritmética
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Exp_aritmeticaContext ctx) {
        TabelaDeSimbolos.TipoLA ret = null;
        for (LAParser.TermoContext ta : ctx.termo()) {
            TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoLA.INVALIDO) {
                ret = TabelaDeSimbolos.TipoLA.INVALIDO;
            }
        }

        return ret;
    }

    
    //Verificando o tipo do nosso termo
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.TermoContext ctx) {
        TabelaDeSimbolos.TipoLA ret = null;
        for (LAParser.FatorContext fa : ctx.fator()) {
            TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, fa);
            Boolean auxNumeric = aux == TabelaDeSimbolos.TipoLA.REAL || aux == TabelaDeSimbolos.TipoLA.INTEIRO;
            Boolean retNumeric = ret == TabelaDeSimbolos.TipoLA.REAL || ret == TabelaDeSimbolos.TipoLA.INTEIRO;
            if (ret == null) {
                ret = aux;
            } else if (!(auxNumeric && retNumeric) && aux != ret) {
                ret = TabelaDeSimbolos.TipoLA.INVALIDO;
            }
        }
        return ret;
    }

    // Verificando o tipo do nosso fator
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.FatorContext ctx) {
        TabelaDeSimbolos.TipoLA ret = null;

        for (LAParser.ParcelaContext fa : ctx.parcela()) {
            TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, fa);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoLA.INVALIDO) {
                ret = TabelaDeSimbolos.TipoLA.INVALIDO;
            }
        }
        return ret;
    }

    // Verificando o tipo da parcela
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.ParcelaContext ctx) {
        TabelaDeSimbolos.TipoLA ret;
        if (ctx.parcela_nao_unario() != null) {
            ret = verificarTipo(escopos, ctx.parcela_nao_unario());
        } else {
            ret = verificarTipo(escopos, ctx.parcela_unario());
        }
        return ret;
    }

    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return verificarTipo(escopos, ctx.identificador());
        }
        return TabelaDeSimbolos.TipoLA.CADEIA;
    }

    // Vamos verificar o tipo de identificadores
    private static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.IdentificadorContext ctx) {
        StringBuilder nomeVar = new StringBuilder();
        TabelaDeSimbolos.TipoLA ret = TabelaDeSimbolos.TipoLA.INVALIDO;
        
        // Percorrendo identificadores
        for (int i = 0; i < ctx.IDENT().size(); i++) {
            // Adicionando o nome
            nomeVar.append(ctx.IDENT(i).getText());
            if (i != ctx.IDENT().size() - 1) {
                nomeVar.append("."); // Adiciona o ponto
            }
        }
        
        // Percorrendo as tabelas dos escopos e verificando se existe o nome da variável dentro
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            if (tabela.existe(nomeVar.toString())) {
                ret = verificarTipo(escopos, nomeVar.toString());
            }
        }
        System.out.println(nomeVar);
        return ret;
    }

    public static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_unarioContext ctx) {
        if (ctx.NUM_INT() != null) {
            return TabelaDeSimbolos.TipoLA.INTEIRO;
        }
        if (ctx.NUM_REAL() != null) {
            return TabelaDeSimbolos.TipoLA.REAL;
        }
        if (ctx.identificador() != null) {
            return verificarTipo(escopos, ctx.identificador());
        }
        if (ctx.IDENT() != null) {
            TabelaDeSimbolos.TipoLA ret;
            ret = verificarTipo(escopos, ctx.IDENT().getText());
            return getTipoLA(escopos, ctx, ret);
        } else {
            TabelaDeSimbolos.TipoLA ret = null;
            return getTipoLA(escopos, ctx, ret);
        }
    }

    private static TabelaDeSimbolos.TipoLA getTipoLA(Escopos escopos, LAParser.Parcela_unarioContext ctx, TabelaDeSimbolos.TipoLA ret) {
        for (LAParser.ExpressaoContext fa : ctx.expressao()) {
            TabelaDeSimbolos.TipoLA aux = verificarTipo(escopos, fa);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != TabelaDeSimbolos.TipoLA.INVALIDO) {
                ret = TabelaDeSimbolos.TipoLA.INVALIDO;
            }
        }
        return ret;
    }

    public static TabelaDeSimbolos.TipoLA verificarTipo(Escopos escopos, String nomeVar) {
        TabelaDeSimbolos.TipoLA type = null;
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            type = tabela.verificar(nomeVar);
        }
        return type;
    }
}