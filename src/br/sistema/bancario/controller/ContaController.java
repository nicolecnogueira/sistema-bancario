package br.sistema.bancario.controller;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.model.ContaBonus;
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

    public String cadastrarContaBonus(String numero) {
        if (repository.buscarPorNumero(numero).isPresent()) {
            return "Erro: Conta já cadastrada.";
        }
        repository.salvar(new ContaBonus(numero));
        return "Sucesso! Conta Bônus " + numero + " criada com 10 pontos.";
    }

    public String consultarSaldo(String numero) {
        Optional<Conta> conta = repository.buscarPorNumero(numero);

        if (conta.isPresent()) {
            return "Saldo da conta " + numero + ": R$ " + String.format("%.2f", conta.get().getSaldo());
        }

        return "Erro: Conta não encontrada.";
    }

    public String creditar(String numero, double valor) {
        Optional<Conta> contaOpt = repository.buscarPorNumero(numero);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();
            conta.creditar(valor);

            if (conta instanceof ContaBonus) {
                int pontos = (int) (valor / 100);
                ((ContaBonus) conta).adicionarPontos(pontos);
            }
            return "Crédito realizado com sucesso.";
        }

        return "Erro: Conta não encontrada.";
    }

    public String debitar(String numero, double valor) {
        Optional<Conta> conta = repository.buscarPorNumero(numero);

        if (conta.isPresent()) {
            if (conta.get().getSaldo() < valor) {
                return "Erro: Saldo insuficiente.";
            }

            conta.get().debitar(valor);
            return "Sucesso! Débito de R$ " + String.format("%.2f", valor) + " realizado.";
        }

        return "Erro: Conta não encontrada.";
    }

    public String transferir(String origem, String destino, double valor) {
        Optional<Conta> contaOrigem = repository.buscarPorNumero(origem);
        Optional<Conta> contaDestino = repository.buscarPorNumero(destino);

        if (contaOrigem.isEmpty() || contaDestino.isEmpty()) {
            return "Erro: Uma das contas não existe.";
        }
        if (contaOrigem.get().getSaldo() < valor) {
            return "Erro: Saldo insuficiente na origem.";
        }

        if (contaOrigem.get().getSaldo() < valor) {
            return "Erro: Saldo insuficiente na conta de origem.";
        }

        contaOrigem.get().debitar(valor);
        contaDestino.get().creditar(valor);

        if (contaDestino.get() instanceof ContaBonus) {
            int pontos = (int) (valor / 200);
            ((ContaBonus) contaDestino.get()).adicionarPontos(pontos);
        }

        return "Transferência realizada.";
    }
}
