package br.sistema.bancario.controller;

import br.sistema.bancario.repository.ContaRepository;

public class ContaController {
    private ContaRepository repository;

    public ContaController(ContaRepository repository) {
        this.repository = repository;
    }
}
