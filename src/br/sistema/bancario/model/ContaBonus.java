package br.sistema.bancario.model;

public class ContaBonus extends Conta {
    private int pontuacao;

    public ContaBonus(String numero) {
        super(numero);
        this.pontuacao = 10; // Pontuação inicial
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }
}