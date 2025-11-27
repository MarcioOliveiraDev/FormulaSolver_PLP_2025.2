# FormulaSolver: Interpretador de Planilhas (Mini Excel)

O **FormulaSolver** é um interpretador desenvolvido como extensão da linguagem **Imperativa 2** do framework educacional PLP (Paradigmas de Linguagens de Programação). O objetivo central é introduzir o conceito de Planilhas Eletrônicas (*sheet*) dentro de uma linguagem imperativa, permitindo a declaração de matrizes de dados, manipulação de células via referências (ex: "A1") e a execução de fórmulas nativas para agregação de dados.

Diferente de matrizes comuns, o tipo `sheet` implementado suporta uma avaliação dinâmica de expressões e referências cruzadas, aproximando-se do comportamento de softwares como Excel ou Google Sheets.

## 📚 Informações do Projeto

| Tópico | Detalhes |
| :--- | :--- |
| **Disciplina** | (IN1007) Paradigmas de Linguagens de Programação - CIn/UFPE |
| **Professor** | Augusto Sampaio |
| **Linguagem Base** | Extensão da Linguagem Imperativa 2 (PLP Framework) |

### 👥 Equipe 8
* **Lucas Migge de Barros** (`lmb4@cin.ufpe.br`)
* **Márcio Oliveira de Brito** (`mob2@cin.ufpe.br`)
* **Sidney dos Santos de Araújo** (`ssa2@cin.ufpe.br`)

---

## 2. Objetivos

O projeto foi estruturado para atender aos seguintes objetivos acadêmicos e técnicos:

1.  **Extensão da Gramática:** Modificar o parser (JavaCC) da Linguagem Imperativa 2 para suportar a palavra-chave `sheet` e novas estruturas sintáticas de fórmulas.
2.  **Gerenciamento de Estado:** Implementar um novo tipo de valor (`ValorPlanilha`) que encapsula o estado das células e valida limites dimensionais.
3.  **Acesso Híbrido:** Permitir a manipulação de células tanto por índices matriciais (`p[0][0]`) quanto por referências alfanuméricas padrão de planilhas (`p["A1"]`).
4.  **Motor de Fórmulas Extensível:** Criar uma arquitetura (`ExpFormulaMultipleArgs`) que permita a fácil adição de novas funções que aceitam múltiplos argumentos (*Varargs*).
5.  **Avaliação Recursiva:** Garantir que, ao imprimir uma planilha (`write(p)`), todas as referências e fórmulas contidas nas células sejam avaliadas para seus valores primitivos finais.

---

## 3. Gramática Estendida (BNF)

Abaixo estão as principais regras de produção adicionadas à gramática original (`Imperative2.jj`) para suportar o tipo `sheet` e as fórmulas.

### Declaração
```bnf
Declaracao ::= ... 
             | "sheet" <ID> "[" <INTEGER_LITERAL> "]" "[" <INTEGER_LITERAL> "]" 
````

### Comandos (Atribuição em Células)

```bnf
Comando ::= ...
          /* Atribuição via Referência (ex: p["A1"] := 10) */
          | <ID> "[" <STRING_LITERAL> "]" ":=" Expressao 

          /* Atribuição via Índices (ex: p[0][0] := 10) */
          | <ID> "[" <INTEGER_LITERAL> "]" "[" <INTEGER_LITERAL> "]" ":=" Expressao
```

### Expressões (Acesso e Fórmulas)

```bnf
Expressao ::= ...
            /* Acesso ao valor da Célula */
            | <ID> "[" <STRING_LITERAL> "]" 
            | <ID> "[" <INTEGER_LITERAL> "]" "[" <INTEGER_LITERAL> "]"

            /* Fórmulas Matemáticas e Lógicas */
            | "SUM" "(" ListaArgumentos ")"
            | "AVG" "(" ListaArgumentos ")"
            | "SUB" "(" ListaArgumentos ")"
            | "MIN" "(" ListaArgumentos ")"
            | "MAX" "(" ListaArgumentos ")"
            | "AND" "(" ListaArgumentos ")"
            | "OR"  "(" ListaArgumentos ")"
            
            /* Fórmula Condicional */
            | "IF"  "(" Expressao "," Expressao "," Expressao ")"

/* Lista de Argumentos para as funções variádicas */
ListaArgumentos ::= Expressao ( "," Expressao )*
```

-----

## 4\. Implementação e Arquivos

Para atingir os objetivos, a linguagem Imperativa 2 foi estendida com os seguintes componentes principais:

### Modificações na Gramática (`Imperative2.jj`)

Inclusão dos tokens `SHEET`, `SUM`, `AVG`, `MIN`, `MAX`, `AND`, `OR`, `IF`, `SUB` e regras de produção para declaração e manipulação de planilhas.

### Estruturas de Dados

  * **`ValorPlanilha.java`**: Representação em memória da planilha.
  * **`DeclaracaoPlanilha.java`**: Comando para alocação de memória no ambiente de execução.

### Comandos de Atribuição

  * **`ComandoAtribuicaoCelula.java`**: Para atribuição via índice (ex: `p[0][0] := 10`).
  * **`ComandoAtribuicaoCelulaRef.java`**: Para atribuição via string (ex: `p["A1"] := 10`).

### Motor de Fórmulas

Localizado no pacote `src/li2/plp/imperative2/expression/formulas`, contém a lógica das funções inspiradas no Excel. A classe base `ExpFormulaMultipleArgs` facilita a implementação de funções com N argumentos.

  * **Matemáticas:** `ExpSumFormula` (Soma), `ExpAvgFormula` (Média), `ExpSubFormula` (Subtração), `ExpMinFormula`, `ExpMaxFormula`.
  * **Lógicas:** `ExpAndFormula`, `ExpOrFormula`.
  * **Condicional:** `ExpIfFormula` (Similar ao IF do Excel).

-----

## 5\. Sintaxe e Funcionalidades

### Operações Básicas

```java
// 1. Declaração de Planilha
sheet nomeDaPlanilha[linhas][colunas];

// 2. Atribuição via Referência ("Excel style")
nomeDaPlanilha["A1"] := 10;

// 3. Atribuição via Índices de Matriz (Zero-based)
nomeDaPlanilha[0][0] := 10;

// 4. Integração com variáveis do escopo imperativo
int x;
x := 50;
nomeDaPlanilha["B1"] := x;
```

### Tabela de Funções (Fórmulas)

| Função | Descrição | Exemplo de Uso |
| :--- | :--- | :--- |
| **SUM** | Soma todos os valores passados. | `p["A1"] := SUM(10, 20, p["B1"]);` |
| **AVG** | Calcula a média aritmética. | `p["A2"] := AVG(10, 0, 20);` *(Result: 10)* |
| **SUB** | Subtração sequencial (Arg1 - Arg2...). | `p["A3"] := SUB(100, 20);` *(Result: 80)* |
| **MIN** | Retorna o menor valor da lista. | `val := MIN(p["A1"], p["B1"], 0);` |
| **MAX** | Retorna o maior valor da lista. | `val := MAX(10, 50, 5);` |
| **AND** | `true` se **todas** as exp. forem verdadeiras. | `res := AND(x > 0, y < 10);` |
| **OR** | `true` se **pelo menos uma** for verdadeira. | `res := OR(x == 0, p["A1"] == 1);` |
| **IF** | Condicional (Teste, ValorTrue, ValorFalse). | `p["C1"] := IF(x > 10, 1, 0);` |

### Exemplo de Código Completo (.txt)

O exemplo abaixo demonstra a declaração, atribuição mista e uso de fórmulas condicionais e matemáticas.

```java
{
    int x;
    sheet planilha[3][3]; 

    x := 10;
    
    // Atribuições diretas
    planilha["A1"] := 50;
    planilha[0][1] := x; 

    // Uso de Fórmulas com referências
    // A2 = 50 + 10 + 5 = 65
    planilha["A2"] := SUM(planilha["A1"], planilha[0][1], 5); 
    
    // Uso de IF Condicional
    // Se A2 (65) > 60, B1 recebe 1, senão 0.
    planilha["B1"] := IF(planilha["A2"] > 60, 1, 0);

    // Saída de dados 
    // (O comando write avalia todas as células recursivamente antes de imprimir)
    write(planilha); 
    
    write(" Valor individual de A2: ");
    write(planilha["A2"]);
}
```

-----

## 6\. Como Executar

Certifique-se de ter o **Maven** e o **JDK** instalados em sua máquina.

1.  **Gerar os arquivos do Parser (JavaCC):**

    ```bash
    mvn clean generate-sources
    ```

2.  **Compilar o projeto:**

    ```bash
    mvn compile
    ```

3.  **Executar um arquivo de teste:**
    Você pode criar um arquivo `.txt` na raiz ou usar os exemplos existentes na pasta `Testes/`.

    ```bash
    mvn exec:java -Dexec.mainClass="li2.plp.imperative2.Programa" -Dexec.args="Testes/TesteImperativa2.txt"
    ```

<!-- end list -->

```
```
