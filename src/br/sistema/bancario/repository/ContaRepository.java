package br.sistema.bancario.repository;

import br.sistema.bancario.model.Conta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContaRepository {
    private final List<Conta> contas = new ArrayList<>();

    public void salvar(Conta conta) {
        contas.add(conta);
    }

    public Optional<Conta> buscarPorNumero(String numero) {
        return contas.stream()
                .filter(c -> c.getNumero().equals(numero))
                .findFirst();
    }

    public List<Conta> listarTodas() {
        return contas;
    }
}
