package br.sistema.bancario.model;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String numero, double saldoInicial) {
        super(numero);
        setSaldo(saldoInicial);
    }

    @Override
    public boolean debitar(double valor) {
        setSaldo(getSaldo() - valor);
        return true;
    }

    public void renderJuros(double taxa) {
        setSaldo(getSaldo() * (1 + taxa / 100.0));
    }
}