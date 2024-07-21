package br.ufscar.dc.compiladores.lasemantico;

/**
 * @author vinij
 */
public class LaSemantico extends LABaseVisitor {

    Escopos escopos = new Escopos();

    @Override
    public Object visitDeclaracao_constante(LAParser.Declaracao_constanteContext ctx) {
        // Ao passar pela declaração de constante, vamos verificar se ela já existe no escopo atual, mostrar
        // erro caso já exista ou apenas adicionar no escopo caso contrário
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            LaSemanticoUtils.adicionarErroSemantico(ctx.start, "constante " + ctx.IDENT().getText() + " ja declarado anteriormente");
        } else {
            TabelaDeSimbolos.TipoLA tipo = null;
            String tipoBasicoTexto = ctx.tipo_basico().getText();

            if (tipoBasicoTexto.equals("literal")) {
                tipo = TabelaDeSimbolos.TipoLA.CADEIA;
            } else if (tipoBasicoTexto.equals("inteiro")) {
                tipo = TabelaDeSimbolos.TipoLA.INTEIRO;
            } else if (tipoBasicoTexto.equals("real")) {
                tipo = TabelaDeSimbolos.TipoLA.REAL;
            } else if (tipoBasicoTexto.equals("logico")) {
                tipo = TabelaDeSimbolos.TipoLA.LOGICO;
            }

            escopoAtual.adicionar(ctx.IDENT().getText(), tipo);
        }

        return super.visitDeclaracao_constante(ctx);
    }


    @Override
    public Object visitDeclaracao_tipo(LAParser.Declaracao_tipoContext ctx) {
        // Estamos passando por todas as declarações de tipo, se já existir no escopo atual um tipo com mesmo nome
        // do que estamos passando, então vamos mostrar erro. Se não existir ainda, então adicionamos no escopo atual.
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            LaSemanticoUtils.adicionarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText() + " declarado duas vezes num mesmo escopo");
        } else {
            escopoAtual.adicionar(ctx.IDENT().getText(), TabelaDeSimbolos.TipoLA.TIPO);
        }
        return super.visitDeclaracao_tipo(ctx);
    }

    @Override
    public Object visitDeclaracao_variavel(LAParser.Declaracao_variavelContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        // Quando estamos passando pela declaração de variável, podemos ter mais do que uma sendo definida ao mesmo tempo
        // Portanto, precisamos percorrer todas e verificar se elas já existem no escopo atual
        for (LAParser.IdentificadorContext id : ctx.variavel().identificador()) {
            if (escopoAtual.existe(id.getText())) {
                LaSemanticoUtils.adicionarErroSemantico(id.start, "identificador " + id.getText() + " ja declarado anteriormente");
            } else {
                TabelaDeSimbolos.TipoLA tipo = null;
                String tipoTexto = ctx.variavel().tipo().getText();

                if (tipoTexto.equals("literal")) {
                    tipo = TabelaDeSimbolos.TipoLA.CADEIA;
                } else if (tipoTexto.equals("inteiro")) {
                    tipo = TabelaDeSimbolos.TipoLA.INTEIRO;
                } else if (tipoTexto.equals("real")) {
                    tipo = TabelaDeSimbolos.TipoLA.REAL;
                } else if (tipoTexto.equals("logico")) {
                    tipo = TabelaDeSimbolos.TipoLA.LOGICO;
                }

                escopoAtual.adicionar(id.getText(), tipo);
            }
        }
        return super.visitDeclaracao_variavel(ctx);
    }

    @Override
    public Object visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        // Passa pela declaração global, temos casos de procedimento ou função
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            LaSemanticoUtils.adicionarErroSemantico(ctx.start, ctx.IDENT().getText() + " ja declarado anteriormente");
        } else {
            escopoAtual.adicionar(ctx.IDENT().getText(), TabelaDeSimbolos.TipoLA.TIPO);
        }
        return super.visitDeclaracao_global(ctx);
    }

    @Override
    public Object visitTipo_basico_ident(LAParser.Tipo_basico_identContext ctx) {
        // Percorre todos os escopos aninhados e verifica se o tipo existe em algum deles. 
        // caso não exista, então estamos tentando utilizar um tipo não declarado e vamos mostrar erro
        if (ctx.IDENT() != null) {
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (!escopo.existe(ctx.IDENT().getText())) {
                    LaSemanticoUtils.adicionarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText() + " nao declarado");
                }
            }
        }
        return super.visitTipo_basico_ident(ctx);
    }

    @Override
    public Object visitIdentificador(LAParser.IdentificadorContext ctx) {
        // Pegamos o identificador na primeira posição e verificamos se já existe em algum dos escopos
        // Se não existir, então estamos tentando usar um identificador não declarado, vamos mostrar erro
        for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
            if (!escopo.existe(ctx.IDENT(0).getText())) {
                LaSemanticoUtils.adicionarErroSemantico(ctx.start, "identificador " + ctx.IDENT(0).getText() + " nao declarado");
            }
        }
        return super.visitIdentificador(ctx);
    }

    @Override
    public Object visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        TabelaDeSimbolos.TipoLA tipoExpressao = LaSemanticoUtils.verificarTipo(escopos, ctx.expressao());
        boolean error = false;
        String nomeVar = ctx.identificador().getText();
        if (tipoExpressao != TabelaDeSimbolos.TipoLA.INVALIDO) {
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (escopo.existe(nomeVar)) {
                    TabelaDeSimbolos.TipoLA tipoVariavel = LaSemanticoUtils.verificarTipo(escopos, nomeVar);
                    Boolean expNumeric = tipoExpressao == TabelaDeSimbolos.TipoLA.INTEIRO || tipoExpressao == TabelaDeSimbolos.TipoLA.REAL;
                    Boolean varNumeric = tipoVariavel == TabelaDeSimbolos.TipoLA.INTEIRO || tipoVariavel == TabelaDeSimbolos.TipoLA.REAL;
                    if (!(varNumeric && expNumeric) && tipoVariavel != tipoExpressao) {
                        error = true;
                    }
                }
            }
        } else {
            error = true;
        }

        if (error)
            LaSemanticoUtils.adicionarErroSemantico(ctx.identificador().start, "atribuicao nao compativel para " + nomeVar);

        return super.visitCmdAtribuicao(ctx);
    }

}