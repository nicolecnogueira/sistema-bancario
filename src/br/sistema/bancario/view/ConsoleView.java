package br.sistema.bancario.view;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.model.ContaBonus;
import br.sistema.bancario.model.ContaPoupanca;
import br.sistema.bancario.service.ContaService;

import java.util.Scanner;

public class ConsoleView {
    private final ContaService contaService;
    private final Scanner scanner;

    public ConsoleView(ContaService contaService) {
        this.contaService = contaService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== SISTEMA BANCÁRIO ===");
            System.out.println("1. Cadastrar Conta");
            System.out.println("2. Consultar Saldo");
            System.out.println("3. Crédito");
            System.out.println("4. Débito");
            System.out.println("5. Transferência");
            System.out.println("6. Render Juros");
            System.out.println("7. Consultar Dados da Conta");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1: cadastrarContaView(); break;
                    case 2: consultarSaldoView(); break;
                    case 3: creditarView(); break;
                    case 4: debitarView(); break;
                    case 5: transferirView(); break;
                    case 6: renderJurosView(); break;
                    case 7: consultarDadosContaView(); break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida.");
                scanner.nextLine();
            }
        }
    }

    private void cadastrarContaView() {
        try {
            System.out.print("Digite o número da conta: ");
            String numero = scanner.nextLine();
            System.out.println("Tipo: 1-Simples, 2-Bônus, 3-Poupança");
            int tipo = scanner.nextInt();
            scanner.nextLine();

            if (tipo == 2) {
                contaService.cadastrarContaBonus(numero);
                System.out.println("Conta Bônus criada.");
            } else if (tipo == 3) {
                System.out.print("Saldo inicial: ");
                double saldo = scanner.nextDouble();
                scanner.nextLine();
                contaService.cadastrarContaPoupanca(numero, saldo);
                System.out.println("Conta Poupança criada.");
            } else {
                contaService.cadastrarConta(numero);
                System.out.println("Conta Simples criada.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void consultarSaldoView() {
        try {
            System.out.print("Digite o número da conta: ");
            String numero = scanner.nextLine();
            System.out.println("Saldo: R$ " + String.format("%.2f", contaService.consultarSaldo(numero)));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void creditarView() {
        try {
            System.out.print("Número: ");
            String numero = scanner.nextLine();
            System.out.print("Valor: ");
            double valor = scanner.nextDouble();
            scanner.nextLine();
            contaService.creditar(numero, valor);
            System.out.println("Crédito efetuado.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void debitarView() {
        try {
            System.out.print("Número: ");
            String numero = scanner.nextLine();
            System.out.print("Valor: ");
            double valor = scanner.nextDouble();
            scanner.nextLine();
            contaService.debitar(numero, valor);
            System.out.println("Débito efetuado.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void transferirView() {
        try {
            System.out.print("Origem: ");
            String orig = scanner.nextLine();
            System.out.print("Destino: ");
            String dest = scanner.nextLine();
            System.out.print("Valor: ");
            double valor = scanner.nextDouble();
            scanner.nextLine();
            contaService.transferir(orig, dest, valor);
            System.out.println("Transferência realizada.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void renderJurosView() {
        try {
            System.out.print("Taxa (%): ");
            double taxa = scanner.nextDouble();
            scanner.nextLine();
            contaService.renderJuros(taxa);
            System.out.println("Rendimentos aplicados.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void consultarDadosContaView() {
        try {
            System.out.print("Número: ");
            String numero = scanner.nextLine();
            Conta conta = contaService.consultarConta(numero);

            String tipo = "Simples";
            String bonus = "";
            if (conta instanceof ContaBonus) {
                tipo = "Bônus";
                bonus = "\nBônus: " + ((ContaBonus) conta).getPontuacao();
            } else if (conta instanceof ContaPoupanca) {
                tipo = "Poupança";
            }

            System.out.println("--- DADOS --- \nTipo: " + tipo + "\nNúmero: " + conta.getNumero() + "\nSaldo: R$ " + String.format("%.2f", conta.getSaldo()) + bonus);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}