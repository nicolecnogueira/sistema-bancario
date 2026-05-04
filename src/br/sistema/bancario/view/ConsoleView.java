package br.sistema.bancario.view;

import br.sistema.bancario.controller.ContaController;

public class ConsoleView {
    private ContaController controller;

    public ConsoleView(ContaController controller) {
        this.controller = controller;
    }

    public void iniciar() {}
}
