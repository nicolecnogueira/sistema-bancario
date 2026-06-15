package br.sistema.bancario.service;

import br.sistema.bancario.model.Conta;
import br.sistema.bancario.model.ContaBonus;
import br.sistema.bancario.model.ContaPoupanca;
import br.sistema.bancario.repository.ContaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContaService - Operacoes do banco")
class ContaServiceTest {

    private static final double DELTA = 0.0001;

    private ContaRepository repository;
    private ContaService service;

    @BeforeEach
    void setUp() {
        repository = new ContaRepository();
        service = new ContaService(repository);
    }

    @Nested
    @DisplayName("Cadastrar Conta")
    class CadastrarConta {

        @Test
        @DisplayName("Conta Simples e criada com saldo zero")
        void cadastrarContaSimples() {
            Conta c = service.cadastrarConta("123");

            assertEquals(Conta.class, c.getClass());
            assertEquals("123", c.getNumero());
            assertEquals(0.0, c.getSaldo(), DELTA);
            assertSame(c, repository.buscarPorNumero("123").orElseThrow());
        }

        @Test
        @DisplayName("Conta Bonus e criada com 10 pontos iniciais")
        void cadastrarContaBonus() {
            ContaBonus c = service.cadastrarContaBonus("200");

            assertEquals(10, c.getPontuacao());
            assertEquals(0.0, c.getSaldo(), DELTA);
            assertInstanceOf(ContaBonus.class, repository.buscarPorNumero("200").orElseThrow());
        }

        @Test
        @DisplayName("Conta Poupanca e criada com o saldo inicial informado")
        void cadastrarContaPoupanca() {
            ContaPoupanca c = service.cadastrarContaPoupanca("300", 500.0);

            assertEquals(500.0, c.getSaldo(), DELTA);
            assertInstanceOf(ContaPoupanca.class, repository.buscarPorNumero("300").orElseThrow());
        }

        @Test
        @DisplayName("Conta Poupanca nao aceita saldo inicial negativo")
        void cadastrarContaPoupancaSaldoNegativo() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.cadastrarContaPoupanca("300", -1.0));
            assertTrue(repository.buscarPorNumero("300").isEmpty());
        }

        @Test
        @DisplayName("Nao permite cadastrar conta com numero ja existente")
        void naoPermiteNumeroDuplicado() {
            service.cadastrarConta("123");

            assertThrows(IllegalArgumentException.class, () -> service.cadastrarConta("123"));
            assertEquals(1, repository.listarTodas().size());
        }
    }

    @Nested
    @DisplayName("Consultar Conta")
    class ConsultarConta {

        @Test
        @DisplayName("Retorna a Conta Simples cadastrada")
        void consultarContaSimples() {
            service.cadastrarConta("123");
            service.creditar("123", 100.0);

            Conta c = service.consultarConta("123");

            assertEquals(Conta.class, c.getClass());
            assertEquals("123", c.getNumero());
            assertEquals(100.0, c.getSaldo(), DELTA);
        }

        @Test
        @DisplayName("Retorna a Conta Bonus com a pontuacao")
        void consultarContaBonus() {
            service.cadastrarContaBonus("200");

            Conta c = service.consultarConta("200");

            assertInstanceOf(ContaBonus.class, c);
            assertEquals(10, ((ContaBonus) c).getPontuacao());
        }

        @Test
        @DisplayName("Retorna a Conta Poupanca")
        void consultarContaPoupanca() {
            service.cadastrarContaPoupanca("300", 250.0);

            Conta c = service.consultarConta("300");

            assertInstanceOf(ContaPoupanca.class, c);
            assertEquals(250.0, c.getSaldo(), DELTA);
        }

        @Test
        @DisplayName("Consultar conta inexistente lanca excecao")
        void consultarContaInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.consultarConta("999"));
        }
    }

    @Nested
    @DisplayName("Consultar Saldo")
    class ConsultarSaldo {

        @Test
        @DisplayName("Retorna o saldo de uma conta existente")
        void consultarSaldoExistente() {
            service.cadastrarConta("123");
            service.creditar("123", 150.0);

            assertEquals(150.0, service.consultarSaldo("123"), DELTA);
        }

        @Test
        @DisplayName("Conta inexistente lanca excecao")
        void consultarSaldoInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.consultarSaldo("999"));
        }
    }

    @Nested
    @DisplayName("Credito")
    class Credito {

        @Test
        @DisplayName("Caso normal: valor e somado ao saldo")
        void creditoNormal() {
            service.cadastrarConta("123");

            service.creditar("123", 200.0);

            assertEquals(200.0, service.consultarSaldo("123"), DELTA);
        }

        @Test
        @DisplayName("Nao permite valor negativo")
        void creditoValorNegativo() {
            service.cadastrarConta("123");

            assertThrows(IllegalArgumentException.class, () -> service.creditar("123", -50.0));
            assertEquals(0.0, service.consultarSaldo("123"), DELTA);
        }

        @Test
        @DisplayName("Bonificacao Bonus: 1 ponto para cada R$ 100 depositados")
        void creditoBonificacaoBonus() {
            ContaBonus cb = service.cadastrarContaBonus("200");

            service.creditar("200", 540.0);

            assertEquals(540.0, cb.getSaldo(), DELTA);
            assertEquals(15, cb.getPontuacao());
        }
    }

    @Nested
    @DisplayName("Debito")
    class Debito {

        @Test
        @DisplayName("Caso normal: valor e subtraido do saldo")
        void debitoNormal() {
            service.cadastrarConta("123");
            service.creditar("123", 500.0);

            service.debitar("123", 200.0);

            assertEquals(300.0, service.consultarSaldo("123"), DELTA);
        }

        @Test
        @DisplayName("Nao permite valor negativo")
        void debitoValorNegativo() {
            service.cadastrarConta("123");
            service.creditar("123", 500.0);

            assertThrows(IllegalArgumentException.class, () -> service.debitar("123", -100.0));
            assertEquals(500.0, service.consultarSaldo("123"), DELTA);
        }

        @Test
        @DisplayName("Nao permite o saldo da conta ficar negativo")
        void debitoNaoPermiteSaldoNegativo() {
            service.cadastrarConta("123");
            service.creditar("123", 100.0);

            assertThrows(IllegalArgumentException.class, () -> service.debitar("123", 200.0));
            assertEquals(100.0, service.consultarSaldo("123"), DELTA);
        }
    }

    @Nested
    @DisplayName("Transferencia")
    class Transferencia {

        @Test
        @DisplayName("Caso normal: debita origem e credita destino")
        void transferenciaNormal() {
            service.cadastrarConta("123");
            service.cadastrarConta("456");
            service.creditar("123", 1000.0);

            service.transferir("123", "456", 300.0);

            assertEquals(700.0, service.consultarSaldo("123"), DELTA);
            assertEquals(300.0, service.consultarSaldo("456"), DELTA);
        }

        @Test
        @DisplayName("Nao permite valor negativo")
        void transferenciaValorNegativo() {
            service.cadastrarConta("123");
            service.cadastrarConta("456");
            service.creditar("123", 1000.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.transferir("123", "456", -100.0));
            assertEquals(1000.0, service.consultarSaldo("123"), DELTA);
            assertEquals(0.0, service.consultarSaldo("456"), DELTA);
        }

        @Test
        @DisplayName("Nao permite a conta de origem ficar com saldo negativo")
        void transferenciaSaldoInsuficiente() {
            service.cadastrarConta("123");
            service.cadastrarConta("456");
            service.creditar("123", 100.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.transferir("123", "456", 200.0));
            assertEquals(100.0, service.consultarSaldo("123"), DELTA);
            assertEquals(0.0, service.consultarSaldo("456"), DELTA);
        }

        @Test
        @DisplayName("Bonificacao Bonus: 1 ponto para cada R$ 150 recebidos")
        void transferenciaBonificacaoBonus() {
            service.cadastrarConta("123");
            ContaBonus destino = service.cadastrarContaBonus("200");
            service.creditar("123", 1000.0);

            service.transferir("123", "200", 300.0);

            assertEquals(300.0, destino.getSaldo(), DELTA);
            assertEquals(12, destino.getPontuacao());
        }
    }

    @Nested
    @DisplayName("Render Juros")
    class RenderJuros {

        @Test
        @DisplayName("Aplica o rendimento somente nas contas Poupanca")
        void renderJurosAplicaSomenteEmPoupanca() {
            service.cadastrarContaPoupanca("300", 200.0);
            service.cadastrarContaPoupanca("301", 1000.0);
            service.cadastrarConta("123");
            service.creditar("123", 500.0);

            service.renderJuros(10.0);

            assertEquals(220.0, service.consultarSaldo("300"), DELTA);
            assertEquals(1100.0, service.consultarSaldo("301"), DELTA);
            assertEquals(500.0, service.consultarSaldo("123"), DELTA);
        }

        @Test
        @DisplayName("Nao permite taxa negativa")
        void renderJurosTaxaNegativa() {
            service.cadastrarContaPoupanca("300", 200.0);

            assertThrows(IllegalArgumentException.class, () -> service.renderJuros(-5.0));
            assertEquals(200.0, service.consultarSaldo("300"), DELTA);
        }
    }
}
