package br.sistema.bancario;

import br.sistema.bancario.repository.ContaRepository;
import br.sistema.bancario.service.ContaService;
import br.sistema.bancario.view.ConsoleView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        
        ContaRepository repository = context.getBean(ContaRepository.class);
        ContaService service = context.getBean(ContaService.class);
        
        ConsoleView view = new ConsoleView(service);
        view.iniciar();
    }
}