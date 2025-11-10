# Extensão da Linguagem Imperativa 2: Planilhas
Este documento descreve as extensões implementadas sobre a base da Imperativa2, com o objetivo de adicionar um novo tipo de dados: Planilha (inspirado no Excel/Google Sheets).

## 1. Objetivos das Alterações
O objetivo principal foi integrar um tipo sheet na linguagem, permitindo:

1. Declarar planilhas com dimensões fixas. 
2. Atribuir valores e fórmulas a células individuais. 
3. Aceder a células usando tanto índices numéricos (ex: p[0][0]) quanto referências de texto (ex: p["A1"]). 
4. Implementar um motor de fórmulas extensível (ex: SUM, AND) que opera sobre os dados das células. 
5. Garantir que a impressão de uma planilha (write(p)) mostre a grelha totalmente avaliada, com os resultados das fórmulas.

## 2. Funcionalidades Implementadas
1. Declaração de Planilhas: Introdução da palavra-chave sheet para declarar uma nova planilha com dimensões [linhas][colunas]. 
2. Atribuição e Acesso por Índice: Suporte para p[i][j] := ... e write(p[i][j]). 
3. Atribuição e Acesso por Referência: Suporte para p["ref"] := ... e write(p["ref"]), onde "ref" é uma string no formato "A1", "B2", etc. 
4. Validação de Limites: A planilha armazena as suas dimensões e lança um erro de execução se houver uma tentativa de escrita ou leitura fora dos limites (ex: p[10][10] numa sheet p[2][2]). 
5. Motor de Fórmulas: Implementação de uma arquitetura extensível para fórmulas (ExpFormulaMultipleArgs) que aceita uma lista variável de argumentos. 
6. Fórmulas Nativas: SUM(arg1, arg2, ...) e AND(arg1, arg2, ...) que operam sobre múltiplas expressões. 
7. Avaliação da Planilha: Modificação do comportamento do write(p) (através da alteração no Id.java) para que ele imprima a grelha 2D com todas as fórmulas calculadas.

## 3. Arquivos Modificados e Criados
Para implementar estas funcionalidades, os seguintes ficheiros foram criados ou alterados:

### 3.1. Ficheiros Base Modificados
1. Imperativa2/src/li2/plp/imperative2/parser/Imperative2.jj:
2. Adicionados novos TOKENs: SHEET, SUM_FORMULA, AND_FORMULA. 
3. Modificada a regra PDeclaracao para aceitar DeclaracaoPlanilha. 
4. Modificada a regra PComandoSimples para diferenciar PAtribuicao de ComandoAtribuicaoCelula e ComandoAtribuicaoCelulaRef (usando LOOKAHEAD). 
5. Modificada a regra PExpPrimaria para diferenciar PId de ExpressaoAcessoCelula e ExpressaoAcessoCelulaRef (usando LOOKAHEAD) e para aceitar as novas fórmulas (PExpSumFormula, PExpAndFormula). 
6. Expressoes2/src/le2/plp/expressions2/expression/Id.java: Modificado o método avaliar() para chamar valor.avaliar(ambiente). Esta foi a alteração crucial que permite ao write(p) imprimir a planilha avaliada, em vez de apenas a referência. 
7. Imperativa2/src/li2/plp/imperative2/command/ListaExpressao.java:
8. Implementada a interface Iterable<Expressao> para permitir iteração fácil (for-each) nas classes de fórmulas.

### 3.2. Packages Novos Criados 
1. Tipo e Valor da Planilha:
   1. Imperativa2/src/li2/plp/imperative2/expression/TipoPlanilha.java: (Define o tipo sheet para o AmbienteCompilacao). 
   2. Imperativa2/src/li2/plp/imperative2/expression/ValorPlanilha.java: (A implementação principal da planilha, usando um HashMap interno para "matriz esparsa", mas mantendo os limites de dimensão para validação e impressão).

2. Declaração:
   1. Imperativa2/src/li2/plp/imperative2/declaration/DeclaracaoPlanilha.java: (Classe que implementa a declaração sheet p[l][c]).

3. Comandos de Atribuição:
   1. Imperativa2/src/li2/plp/imperative2/command/ComandoAtribuicaoCelula.java: (Implementa p[i][j] := ...). 
   2. Imperativa2/src/li2/plp/imperative2/command/ComandoAtribuicaoCelulaRef.java: (Implementa p["ref"] := ...).

4. Expressões de Acesso:
   1. Imperativa2/src/li2/plp/imperative2/expression/ExpAcessoCelula.java: (Implementa a leitura p[i][j]). 
   2. Imperativa2/src/li2/plp/imperative2/expression/ExpAcessoCelulaRef.java: (Implementa a leitura p["ref"]).

5. Arquitetura de Fórmulas:
   1. Imperativa2/src/li2/plp/imperative2/expression/formulas/ExpFormulaMultipleArgs.java: (Classe base abstrata para fórmulas com N argumentos). 
   2. Imperativa2/src/li2/plp/imperative2/expression/formulas/ExpSumFormula.java: (Implementa SUM(...) iterando pela ListaExpressao). 
   3. Imperativa2/src/li2/plp/imperative2/expression/formulas/ExpAndFormula.java: (Implementa AND(...) iterando pela ListaExpressao).

4. Exemplos de Uso

```
// programa
{
    sheet p[3][3],
    var x = 2;
    
    p[0][0] := 10;
    p["A2"] := x * 5;
    p["B1"] := SUM(p["A1"], p["A2"], 5);
    p["C1"] := AND(p["A1"] == 10, p["A2"] > 5);
    write(p)
 }
 
// Esperado:
// [
// [10, 25, true]
// [10, 0, 0]
// [0, 0, 0]
// ]
```

## 5. Indicadores de Futuros Passos
1. A arquitetura atual está pronta para ser expandida. Os próximos passos lógicos seriam:
2. Testes Unitários: Criar testes JUnit para o ValorPlanilha (especialmente converteRefParaIndices e validarLimites) e para as lógicas de avaliação de cada fórmula (ExpSumFormula, etc.). 
3. Testes de Integração: Automatizar a execução dos ficheiros .txt e fazer asserções sobre o ListaValor de saída, em vez de depender da verificação visual do write(). 
4. Fórmulas com Intervalos (Range): Modificar o parser e as fórmulas (como SUM) para aceitar sintaxe de intervalo, como SUM(p, "A1:B10"). 
5. Mais Fórmulas: Adicionar mais funções nativas (MEDIA, MIN, MAX, IF) que herdem de ExpFormulaMultipleArgs ou de uma nova classe base de fórmula. 
6. Robustez de Tipos: Melhorar as fórmulas (ex: SUM) para lidar com tipos mistos (ex: SUM(10, "texto") deveria ignorar o texto e retornar 10, em vez de lançar uma exceção de cast).