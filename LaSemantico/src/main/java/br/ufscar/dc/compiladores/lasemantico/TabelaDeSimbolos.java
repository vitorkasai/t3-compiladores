package br.ufscar.dc.compiladores.lasemantico;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vinij
 */
public class TabelaDeSimbolos {

    public enum TipoLA {
        INTEIRO,
        REAL,
        CADEIA,
        LOGICO,
        INVALIDO,
        TIPO,
        IDENT
    }

    class EntradaTabelaDeSimbolos {
        TipoLA tipo;
        String nome;

        private EntradaTabelaDeSimbolos(String nome, TipoLA tipo) {
            this.tipo = tipo;
            this.nome = nome;
        }
    }

    private final Map<String, EntradaTabelaDeSimbolos> tabela;

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public TipoLA verificar(String nome) {
        return tabela.get(nome).tipo;
    }

    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionar(String nome, TipoLA tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }


}