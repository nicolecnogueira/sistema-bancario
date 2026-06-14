package br.sistema.bancario.controller;

import br.sistema.bancario.dto.TransferenciaDTO;
import br.sistema.bancario.model.Conta;
import br.sistema.bancario.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/banco/conta")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/")
    public ResponseEntity<?> cadastrarConta(@RequestParam String numero, @RequestParam(defaultValue = "1") int tipo, @RequestParam(defaultValue = "0") double saldoInicial) {
        if (tipo == 2) {
            return ResponseEntity.ok(contaService.cadastrarContaBonus(numero));
        } else if (tipo == 3) {
            return ResponseEntity.ok(contaService.cadastrarContaPoupanca(numero, saldoInicial));
        } else {
            return ResponseEntity.ok(contaService.cadastrarConta(numero));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> consultarConta(@PathVariable("id") String id) {
        return ResponseEntity.ok(contaService.consultarConta(id));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<Map<String, Double>> consultarSaldo(@PathVariable("id") String id) {
        return ResponseEntity.ok(Map.of("saldo", contaService.consultarSaldo(id)));
    }

    @PutMapping("/{id}/credito")
    public ResponseEntity<Map<String, String>> creditar(@PathVariable("id") String id, @RequestParam double valor) {
        contaService.creditar(id, valor);
        return ResponseEntity.ok(Map.of("mensagem", "Crédito realizado com sucesso."));
    }

    @PutMapping("/{id}/debito")
    public ResponseEntity<Map<String, String>> debitar(@PathVariable("id") String id, @RequestParam double valor) {
        contaService.debitar(id, valor);
        return ResponseEntity.ok(Map.of("mensagem", "Débito realizado com sucesso."));
    }

    @PutMapping("/transferencia")
    public ResponseEntity<Map<String, String>> transferir(@RequestBody TransferenciaDTO dto) {
        contaService.transferir(dto.getFrom(), dto.getTo(), dto.getAmount());
        return ResponseEntity.ok(Map.of("mensagem", "Transferência realizada com sucesso."));
    }

    @PutMapping("/rendimento")
    public ResponseEntity<Map<String, String>> renderJuros(@RequestParam double taxa) {
        contaService.renderJuros(taxa);
        return ResponseEntity.ok(Map.of("mensagem", "Rendimentos aplicados com sucesso."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }
}