# Apresentação do Projeto FormulaSolver
## Escopo e Gramática BNF

**Disciplina:** (IN1007) Paradigmas de Linguagens de Programação - Cin/UFPE
**Professor:** Augusto Sampaio
**Equipe 8 - FormulaSolver:**
* Lucas Migge de Barros <lmb4@cin.ufpe.br>
* Márcio Oliveira de Brito <mob2@cin.ufpe.br>
* Sidney dos Santos de Araújo <ssa2@cin.ufpe.br>


---

## 1. Introdução ao Projeto

### 1.1 Contextualização

O projeto FormulaSolver visa estender o framework PLP Java (especificamente a partir da linguagem Funcional 1) para criar um interpretador capaz de resolver fórmulas em um formato similar ao encontrado em planilhas eletrônicas. A inspiração inicial vem de um projeto similar desenvolvido em Swift, que utiliza uma sintaxe puramente funcional (`NOME_FUNCAO(arg1; arg2; ...)`). O objetivo final é permitir a leitura de dados de um arquivo (como CSV) e avaliar células que contenham essas fórmulas, resolvendo referências entre elas.

### 1.2 Motivação

A motivação para este projeto inclui:

* **Aplicabilidade Prática:** Planilhas e suas fórmulas são ferramentas ubíquas, e entender seu funcionamento interno é relevante.
* **Integração com PLP:** Aplicar e estender os conceitos aprendidos nas linguagens do framework PLP (ambientes, tipos, avaliação, parsing) para um domínio prático.
* **Análise de Dependências:** O desafio de resolver fórmulas em células envolve a análise e resolução de dependências entre elas.
* **Parsing e Semântica:** Implementar um parser para a sintaxe funcional das fórmulas e definir a semântica operacional para sua avaliação.
* **Manipulação de Dados Externos:** Adicionar a capacidade de interagir com dados externos (arquivos CSV).

---

## 2. Escopo do Projeto

### 2.1 Objetivos Principais

#### 2.1.1 Objetivo Geral
Desenvolver um interpretador, estendendo a linguagem Funcional 1 do framework PLP, capaz de analisar e avaliar fórmulas com sintaxe funcional, incluindo a capacidade de resolver referências a outras células (inicialmente em memória, com visão futura para leitura de arquivos CSV).

#### 2.1.2 Objetivos Específicos

1.  **Adaptar a Linguagem Funcional 1:** Utilizar a base da LF1 para suportar a sintaxe `NOME_FUNCAO(arg1; arg2; ...)`.
2.  **Implementar Funções Nativas:** Definir e implementar as funções básicas de planilhas como funções pré-definidas na linguagem (ex: `SUM`, `AND`, `OR`, `NOT`, `IF`, `EQ`).
3.  **Suportar Tipos de Dados:** Utilizar os tipos existentes no framework (`ValorInteiro`, `ValorBooleano`, `ValorString`) e garantir a checagem de tipo apropriada para os argumentos das funções.
4.  **Implementar Resolução de Referências:** Permitir que as fórmulas usem identificadores que representam outras "células" (ex: `A1`, `B2`), buscando seus valores no ambiente de execução.
5.  **(Opcional/Futuro) Leitura de CSV:** Implementar a capacidade de ler um arquivo CSV, mapear seus dados para um ambiente e resolver as fórmulas presentes nas células.

### 2.2 Funcionalidades Planejadas

#### 2.2.1 Sintaxe de Fórmulas

A linguagem usará uma sintaxe funcional, onde toda operação é uma chamada de função:

`NOME_FUNCAO(arg1; arg2; ...)`

*Observação: O separador de argumentos será o ponto e vírgula (`;`), diferente da vírgula (`,`) padrão do `Funcional1.jj`, exigindo modificação no parser.*

#### 2.2.2 Operações/Funções Suportadas

| Função | Descrição | Sintaxe Exemplo | Retorno Esperado |
|---|---|---|---|
| `SUM` | Soma argumentos numéricos | `SUM(1; 2; 3)` | `6` |
| `AND` | "E" lógico | `AND(TRUE; FALSE)` | `FALSE` |
| `OR` | "OU" lógico | `OR(TRUE; FALSE)` | `TRUE` |
| `NOT` | Negação lógica | `NOT(FALSE)` | `TRUE` |
| `IF` | Condicional | `IF(TRUE; 10; 20)` | `10` |
| `EQ` | Igualdade (entre números ou strings) | `EQ(5; 5)` | `TRUE` |

#### 2.2.3 Tipos de Dados

- **Inteiro:** `ValorInteiro`
- **Booleano:** `ValorBooleano`
- **String:** `ValorString`
- **Referência:** `Id` (para referenciar outras células, ex: `A1`, `B2`)

#### 2.2.4 Exemplo Prático

Considerando um ambiente (contexto) onde `A1` tem valor `10` e `B1` tem valor `TRUE`:
```java
// Fórmula a ser avaliada
String formula = "IF(EQ(B1; TRUE); SUM(A1; 5); 0)";

// Ambiente de Execução simulando as células
AmbienteExecucaoFuncional amb = new ContextoExecucaoFuncional();
amb.map(new Id("A1"), new ValorInteiro(10));
amb.map(new Id("B1"), new ValorBooleano(true));

// Execução (simplificada)
// O parser transformaria a string 'formula' em objetos Expressao
// A avaliação resolveria B1 para TRUE, EQ daria TRUE,
// o IF selecionaria SUM(A1; 5), resolveria A1 para 10,
// SUM daria 15.
Valor resultado = //... avaliar a fórmula parseada com o ambiente ...
System.out.println(resultado); // Esperado: 15 
```
---

## 3. Gramática EBNF Formal
A gramática será baseada na da Linguagem Funcional 1, mas adaptada para a sintaxe específica de fórmulas e com foco na aplicação de funções pré-definidas (ao invés da definição de novas funções pelo usuário via fun).

```
<programa>      ::= <expressao>

<expressao>     ::= <aplicacao>
                  | <literal>
                  | <referencia_celula>
                  | "(" <expressao> ")"

<aplicacao>     ::= NOME_FUNCAO LPAREN <lista_argumentos> RPAREN

<lista_argumentos> ::= <expressao> { SEPARADOR <expressao> }

<literal>       ::= VALOR_INTEIRO | VALOR_BOOLEANO | VALOR_STRING

<referencia_celula> ::= ID // Identificador representando uma célula, ex: A1, B2
```

### Terminais (Tokens - Baseados no PLP Java):

- `NOME_FUNCAO`: SUM, AND, OR, NOT, IF, EQ (Identificadores específicos pré-definidos)
- `LPAREN`: (
- `RPAREN`: )
- `SEPARADOR`: ; (Requer alteração no parser do Funcional1)
- `VALOR_INTEIRO`: Representa ValorInteiro (ex: 10, -5)
- `VALOR_BOOLEANO`: Representa ValorBooleano (TRUE, FALSE)
- `VALOR_STRING`: Representa ValorString (ex: "texto")
- `ID`: Representa Id (usado para referências como A1)

Nota: Esta BNF simplificada foca na estrutura das fórmulas. A gramática completa do Funcional1.jj seria a base, com modificações para o separador e potencialmente removendo/ignorando a definição de novas funções (fun ...).

## 4. Passos de implementação
1. Configuração do Ambiente: Clonar o repositório PLP e configurar o ambiente de desenvolvimento Java (Maven, IDE).
2. Escolha da Base: Utilizar o projeto Funcional1 como ponto de partida.
3. Modificação do Parser:
   - Alterar o arquivo Funcional1.jj para usar ; como separador de argumentos na regra PAplicacao (ou similar) em vez de ,.
   - Regerar as classes do parser via Maven (mvn generate-sources).
4. Definição das Funções Nativas:
- Criar classes Java que implementem a lógica de cada função (SUM, AND, etc.). Estas classes podem encapsular ou reutilizar as ExpBinaria e ExpUnaria existentes (como ExpSoma, ExpAnd), mas adaptadas para receber uma lista de argumentos (List<Expressao>).
- Implementar a validação de tipos e aridade dentro de cada função.
- Criar instâncias DefFuncao para cada função nativa. Carregamento das Funções: Modificar o Programa ou o ContextoExecucaoFuncional inicial para pré-carregar as definições das funções nativas no ambiente funcional.

5. Adaptação da Avaliação (Aplicacao.avaliar): Modificar a classe Aplicacao para que, ao avaliar, ela busque a DefFuncao no ambiente e execute sua lógica específica (passando os argumentos avaliados). A lógica atual de Aplicacao.avaliar pode precisar ser ajustada para lidar com as funções nativas de forma diferente das funções definidas pelo usuário (se estas últimas não forem permitidas).

6. Resolução de Referências (Id.avaliar): A classe Id já busca o valor no ambiente. Garantir que o ambiente de execução contenha os mapeamentos Id (célula) -> Valor.
7.Testes: Criar arquivos de teste (.txt ou similar) com fórmulas simples e complexas, incluindo aninhamento e referências, para validar o interpretador.
8.  Leitor CSV: Implementar uma classe para ler arquivos CSV, popular o AmbienteExecucao com os valores das células e, em seguida, iterar sobre as células que contêm fórmulas para avaliá-las. Isso exigirá uma estratégia para lidar com dependências cíclicas ou ordem de avaliação.

Projeto FormulaSolver - Uma extensão da Linguagem Funcional 1 para interpretar fórmulas de planilhas.
*Centro de Informática - Universidade Federal de Pernambuco - 2025* 