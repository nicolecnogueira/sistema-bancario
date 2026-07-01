package br.sistema.bancario;

import br.sistema.bancario.service.ContaService;
import br.sistema.bancario.view.ConsoleView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        if (Arrays.asList(args).contains("--cli")) {
            ContaService service = context.getBean(ContaService.class);
            ConsoleView view = new ConsoleView(service);
            view.iniciar();
        }
    }
}
