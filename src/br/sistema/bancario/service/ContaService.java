package br.sistema.bancario.service;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.model.ContaBonus;
import br.sistema.bancario.model.ContaPoupanca;
import br.sistema.bancario.repository.ContaRepository;

import java.util.List;

public class ContaService {
    private final ContaRepository repository;

    public ContaService(ContaRepository repository) {
        this.repository = repository;
    }

    public Conta cadastrarConta(String numero) {
        if (repository.buscarPorNumero(numero).isPresent()) {
            throw new IllegalArgumentException("Erro! Já existe conta com número " + numero);
        }
        Conta novaConta = new Conta(numero);
        repository.salvar(novaConta);
        return novaConta;
    }

    public ContaBonus cadastrarContaBonus(String numero) {
        if (repository.buscarPorNumero(numero).isPresent()) {
            throw new IllegalArgumentException("Erro: Conta já cadastrada.");
        }
        ContaBonus novaConta = new ContaBonus(numero);
        repository.salvar(novaConta);
        return novaConta;
    }

    public ContaPoupanca cadastrarContaPoupanca(String numero, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Erro: O valor do parâmetro é negativo");
        }
        if (repository.buscarPorNumero(numero).isPresent()) {
            throw new IllegalArgumentException("Erro: Conta já cadastrada.");
        }
        ContaPoupanca novaConta = new ContaPoupanca(numero, saldoInicial);
        repository.salvar(novaConta);
        return novaConta;
    }

    public Conta consultarConta(String numero) {
        return repository.buscarPorNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Conta não encontrada."));
    }

    public double consultarSaldo(String numero) {
        return consultarConta(numero).getSaldo();
    }

    public void creditar(String numero, double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: O valor do parâmetro é negativo");
        }
        Conta conta = consultarConta(numero);
        conta.creditar(valor);

        if (conta instanceof ContaBonus) {
            int pontos = (int) (valor / 100);
            ((ContaBonus) conta).adicionarPontos(pontos);
        }
    }

    public void debitar(String numero, double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: O valor do parâmetro é negativo");
        }
        Conta conta = consultarConta(numero);
        if (conta.getSaldo() < valor) {
            throw new IllegalArgumentException("Erro: Saldo insuficiente.");
        }
        conta.debitar(valor);
    }

    public void transferir(String origem, String destino, double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: O valor do parâmetro é negativo");
        }
        Conta contaOrigem = consultarConta(origem);
        Conta contaDestino = consultarConta(destino);

        if (contaOrigem.getSaldo() < valor) {
            throw new IllegalArgumentException("Erro: Saldo insuficiente na conta de origem.");
        }

        contaOrigem.debitar(valor);
        contaDestino.creditar(valor);

        if (contaDestino instanceof ContaBonus) {
            int pontos = (int) (valor / 150);
            ((ContaBonus) contaDestino).adicionarPontos(pontos);
        }
    }

    public void renderJuros(double taxa) {
        if (taxa < 0) {
            throw new IllegalArgumentException("Erro: O valor do parâmetro é negativo");
        }
        List<Conta> contas = repository.listarTodas();
        for (Conta c : contas) {
            if (c instanceof ContaPoupanca) {
                ((ContaPoupanca) c).renderJuros(taxa);
            }
        }
    }
}