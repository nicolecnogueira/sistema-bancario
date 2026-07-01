# Sistema Bancario

Projeto desenvolvido para a disciplina de Gerência de Configuração e Mudanças (DIM0517), disciplina do bacharelado em Tecnologia da Informação com enfase em Desenvolvimento de Software da UFRN/IMD.

## Equipe
* Gabriel Ribeiro Barbosa da Silva [(gabriel-ribeiro-099)](https://github.com/gabriel-ribeiro-099)
* Nicole Carvalho Nogueira [(nicolecnogueira)](https://github.com/nicolecnogueira)

## Stack de Desenvolvimento
* **Linguagem de Programação:** Java 17
* **Framework:** Spring Boot
* **Build:** Maven

## Como Executar a Aplicação

Pré-requisito: **JDK 17** (e Maven). A aplicação sobe a **API REST na porta 8080**.

### Opção 1 — Imagem Docker (Docker Hub)
```bash
docker run -p 8080:8080 gabrielrbs/sistema-bancario:latest
```

### Opção 2 — Build local da imagem
```bash
docker build -f dockerfile -t sistema-bancario .
docker run -p 8080:8080 sistema-bancario
```

### Opção 3 — Via Maven / JAR
```bash
mvn -B package
java -jar target/sistema-bancario-1.0.0.jar
```

> **Modo console (CLI):** a aplicação também tem uma interface de console. Para usá-la,
> passe o argumento `--cli`:
> ```bash
> java -jar target/sistema-bancario-1.0.0.jar --cli
> ```
> Sem `--cli`, apenas a API REST é iniciada (uso em container/produção).

## Imagem no Docker Hub

A imagem é publicada automaticamente no Docker Hub a cada liberação em produção:

**https://hub.docker.com/r/gabrielrbs/sistema-bancario**

## Endpoints da API

Base: `http://localhost:8080/banco/conta`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/banco/conta/?numero={n}&tipo={1\|2\|3}&saldoInicial={s}` | Cadastra conta (tipo 1=Simples, 2=Bônus, 3=Poupança; `saldoInicial` só para Poupança) |
| `GET` | `/banco/conta/{numero}` | Consulta os dados da conta |
| `GET` | `/banco/conta/{numero}/saldo` | Consulta o saldo |
| `PUT` | `/banco/conta/{numero}/credito?valor={v}` | Credita um valor |
| `PUT` | `/banco/conta/{numero}/debito?valor={v}` | Debita um valor |
| `PUT` | `/banco/conta/transferencia` | Transfere entre contas (corpo JSON) |
| `PUT` | `/banco/conta/rendimento?taxa={t}` | Aplica rendimento (apenas Poupança) |

### Exemplos (curl)

```bash
# Cadastrar conta simples (numero 123)
curl -X POST "http://localhost:8080/banco/conta/?numero=123&tipo=1"

# Cadastrar conta poupança com saldo inicial
curl -X POST "http://localhost:8080/banco/conta/?numero=300&tipo=3&saldoInicial=500"

# Consultar conta
curl "http://localhost:8080/banco/conta/123"

# Consultar saldo
curl "http://localhost:8080/banco/conta/123/saldo"

# Creditar R$ 200
curl -X PUT "http://localhost:8080/banco/conta/123/credito?valor=200"

# Debitar R$ 50
curl -X PUT "http://localhost:8080/banco/conta/123/debito?valor=50"

# Transferir R$ 100 de 123 para 456
curl -X PUT "http://localhost:8080/banco/conta/transferencia" \
  -H "Content-Type: application/json" \
  -d '{"from":"123","to":"456","amount":100}'

# Render juros de 10% nas contas poupança
curl -X PUT "http://localhost:8080/banco/conta/rendimento?taxa=10"
```

---
Entrega da atividade 3 unidade 3
