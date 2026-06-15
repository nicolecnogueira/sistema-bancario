package br.sistema.bancario.model;

public class Conta {
    public static final double LIMITE_SALDO_NEGATIVO = -1000.0;

    private String numero;
    private double saldo;

    public Conta(String numero) {
        this.numero = numero;
        this.saldo = 0.0;
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void creditar(double valor) {
        this.saldo += valor;
    }

    public boolean debitar(double valor) {
        if (this.saldo - valor < LIMITE_SALDO_NEGATIVO) {
            return false;
        }

        this.saldo -= valor;
        return true;
    }
}
