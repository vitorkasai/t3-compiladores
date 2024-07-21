package br.ufscar.dc.compiladores.lasemantico;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author vinij
 */
public class Escopos {

    private LinkedList<TabelaDeSimbolos> pilhaDeTabelas;

    // Construtor para o escopo e definição da pilha utilizando uma linkedList
    public Escopos() {
        pilhaDeTabelas = new LinkedList<>();
        criarNovoEscopo();
    }

    public void criarNovoEscopo() {
        pilhaDeTabelas.push(new TabelaDeSimbolos());
    }

    // Apenas verifica qual é o escopo sem precisar remover ele da pilha
    public TabelaDeSimbolos obterEscopoAtual() {
        return pilhaDeTabelas.peek();
    }

    public List<TabelaDeSimbolos> percorrerEscoposAninhados() {
        return pilhaDeTabelas;
    }

    public void abandonarEscopo() {
        pilhaDeTabelas.pop();
    }
}