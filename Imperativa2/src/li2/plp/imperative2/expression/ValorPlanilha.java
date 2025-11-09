package li2.plp.imperative2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

import java.util.HashMap;

public class ValorPlanilha implements Valor {

    private HashMap<String, Expressao> celulas;
    private int linhas;
    private int colunas;

    public ValorPlanilha(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.celulas = new HashMap<>();
    }

    // métodos para Valor
    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return this;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return true;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return new TipoPlanilha();
    }

    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        return null;
    }

    @Override
    public Expressao clone() {
        return null;
    }

    // métodos específicos de ValorPlanilha
    public Expressao getCelula(String ref) {
        String key = ref;
        Expressao expressao = celulas.get(key);

        if (expressao == null) {
            return new ValorInteiro(0);
        }
        return expressao;
    }

    // Método primário de escrita (por String)
    public void setCelula(String ref, Expressao expressao) {
        String key = ref;
        celulas.put(key, expressao);
    }

    // --- Métodos de conveniência (para os comandos) ---

    public Expressao getCelula(int i, int j) {
        // Traduz (0,0) para "A1" e chama o método primário
        String ref = converteIndicesParaRef(i, j);
        return getCelula(ref);
    }

    public void setCelula(int i, int j, Expressao expressao) {
        // Traduz (0,0) para "A1" e chama o método primário
        String ref = converteIndicesParaRef(i, j);
        setCelula(ref, expressao);
    }

    // referência no formato "A1", "B2", etc.
    private static String converteIndicesParaRef(int row, int col) {
        char colChar = (char) ('A' + col);
        String rowStr = Integer.toString(row + 1);
        return colChar + rowStr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < linhas; i++) {
            sb.append("\n  [");
            for (int j = 0; j < colunas; j++) {
                sb.append(this.getCelula(i, j).toString());
                if (j < colunas - 1) sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("\n]");
        return sb.toString();
    }
}
