package br.sistema.bancario.controller;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.model.ContaBonus;
import br.sistema.bancario.model.ContaPoupanca;
import br.sistema.bancario.repository.ContaRepository;

import java.util.List;
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
        if (valor <= 0) return "Erro: valor deve maior que zero.";

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
        if (valor <= 0) return "Erro: valor deve maior que zero.";
        Optional<Conta> conta = repository.buscarPorNumero(numero);
        if (conta.isEmpty()) return "Erro: Conta não encontrada.";

        if (!conta.get().debitar(valor)) {
            return "Erro: operacao excede limite minimo de saldo (R$ -1000,00).";
        }
        return "Sucesso! Débito de R$ " + String.format("%.2f", valor) + " realizado.";
    }

    public String transferir(String origem, String destino, double valor) {
        if (valor <= 0) return "Erro: valor deve maior que zero.";

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

        if (!contaOrigem.get().debitar(valor)) {
            return "Erro: transferencia excede limite minimo de saldo (R$ -1000,00) na origem.";
        }

        contaDestino.get().creditar(valor);

        if (contaDestino.get() instanceof ContaBonus) {
            int pontos = (int) (valor / 150);
            ((ContaBonus) contaDestino.get()).adicionarPontos(pontos);
        }

        return "Transferência realizada.";
    }

    public String cadastrarContaPoupanca(String numero, double saldoInicial) {
        if (repository.buscarPorNumero(numero).isPresent()) {
            return "Erro: Conta já cadastrada.";
        }

        repository.salvar(new ContaPoupanca(numero, saldoInicial));
        return "Sucesso! Conta Poupança " + numero + " criada com saldo R$ "
                + String.format("%.2f", saldoInicial) + ".";
    }

    public String renderJuros(double taxa) {
        List<Conta> contas = repository.listarTodas();
        int aplicadas = 0;
        for (Conta c : contas) {
            if (c instanceof ContaPoupanca) {
                ((ContaPoupanca) c).renderJuros(taxa);
                aplicadas++;
            }
        }
        return "Juros de " + taxa + "% aplicados em " + aplicadas + " conta(s) poupança.";
    }

    public String consultarDadosConta(String numero) {
    Optional<Conta> contaOpt = repository.buscarPorNumero(numero);

    if (contaOpt.isEmpty()) {
        return "Erro: Conta não encontrada.";
    }

    Conta conta = contaOpt.get();
    String tipo = "Simples";
    String infoAdicional = "";

    if (conta instanceof ContaBonus) {
        tipo = "Bônus";
        infoAdicional = "\nPontuação: " + ((ContaBonus) conta).getPontuacao() + " pontos";
    } else if (conta instanceof ContaPoupanca) {
        tipo = "Poupança";
    }

    return "--- DADOS DA CONTA ---" +
           "\nTipo: " + tipo +
           "\nNúmero: " + conta.getNumero() +
           "\nSaldo: R$ " + String.format("%.2f", conta.getSaldo()) +
           infoAdicional;
}
}
