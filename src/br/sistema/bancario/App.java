package br.sistema.bancario;

import br.sistema.bancario.controller.ContaController;
import br.sistema.bancario.repository.ContaRepository;
import br.sistema.bancario.view.ConsoleView;

public class App {
    public static void main(String[] args) {
        ContaRepository repository = new ContaRepository();
        ContaController controller = new ContaController(repository);
        ConsoleView view = new ConsoleView(controller);

        view.iniciar();
    }
}
