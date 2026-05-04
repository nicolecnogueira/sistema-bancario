package br.sistema.bancario.controller;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.repository.ContaRepository;

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
}
