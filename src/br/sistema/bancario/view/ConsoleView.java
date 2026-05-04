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
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarContaView();
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
}