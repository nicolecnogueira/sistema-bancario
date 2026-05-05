package br.sistema.bancario.view;

import br.sistema.bancario.controller.ContaController;

import java.util.Scanner;

public class ConsoleView {
    private ContaController controller;
    private Scanner scanner;

    public ConsoleView(ContaController controller) {
        this.controller = controller;
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
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarContaView();
                        break;
                    case 2:
                        consultarSaldoView();
                        break;
                    case 3:
                        creditarView();
                        break;
                    case 4:
                        debitarView();
                        break;
                    case 5:
                        transferirView();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } else {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.nextLine();
            }
        }
    }

    private void cadastrarContaView() {
        System.out.print("Digite o número da nova conta: ");
        String numero = scanner.nextLine();

        String mensagem = controller.cadastrarConta(numero);
        System.out.println(mensagem);
    }

    private void consultarSaldoView() {
        System.out.print("Digite o número da conta para consulta: ");
        String numero = scanner.nextLine();
        System.out.println(controller.consultarSaldo(numero));
    }

    private void creditarView() {
        System.out.print("Digite o número da conta: ");
        String numero = scanner.nextLine();
        System.out.print("Digite o valor do crédito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        System.out.println(controller.creditar(numero, valor));
    }

    private void debitarView() {
        System.out.print("Digite o número da conta: ");
        String numero = scanner.nextLine();
        System.out.print("Digite o valor do débito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        System.out.println(controller.debitar(numero, valor));
    }

    private void transferirView() {
        System.out.print("Digite o número da conta de origem: ");
        String origem = scanner.nextLine();
        System.out.print("Digite o número da conta de destino: ");
        String destino = scanner.nextLine();
        System.out.print("Digite o valor da transferência: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        System.out.println(controller.transferir(origem, destino, valor));
    }
}