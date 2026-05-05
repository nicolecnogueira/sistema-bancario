package br.sistema.bancario.controller;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.repository.ContaRepository;

import java.util.Optional;

public class ContaController {
    private ContaRepository repository;

    public ContaController(ContaRepository repository) {
        this.repository = repository;
    }

    public String cadastrarConta(String numero) {
        if (repository.buscarPorNumero(numero).isPresent()) {
            return "Erro! Já existe conta com número " + numero;
        }

        Conta novaConta = new Conta(numero);
        repository.salvar(novaConta);
        return "Sucesso! Conta " + numero + " criada, saldo inicial de R$ 0.0!";
    }

    public String consultarSaldo(String numero) {
        Optional<Conta> conta = repository.buscarPorNumero(numero);

        if (conta.isPresent()) {
            return "Saldo da conta " + numero + ": R$ " + String.format("%.2f", conta.get().getSaldo());
        }

        return "Erro: Conta não encontrada.";
    }

    public String creditar(String numero, double valor) {
        Optional<Conta> conta = repository.buscarPorNumero(numero);

        if (conta.isPresent()) {
            conta.get().creditar(valor);
            return "Sucesso! Crédito de R$ " + String.format("%.2f", valor) + " realizado.";
        }

        return "Erro: Conta não encontrada.";
    }
}
