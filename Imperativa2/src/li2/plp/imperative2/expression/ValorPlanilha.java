package li2.plp.imperative2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

import java.util.HashMap;

public class ValorPlanilha implements Valor {

    private HashMap<String, Expressao> celulasRaw;
    private HashMap<String, Expressao> celulasAvaliadas;
    private int linhas;
    private int colunas;

    public ValorPlanilha(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.celulasRaw = new HashMap<>();
        this.celulasAvaliadas = new HashMap<>();
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {


        this.celulasAvaliadas.clear();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                String ref = converteIndicesParaRef(i, j);
                Expressao exp = this.getCelula(ref);

                Valor valorResultado = exp.avaliar(amb);
                celulasAvaliadas.put(ref.toUpperCase(), valorResultado);
            }
        }

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
        ValorPlanilha clone = new ValorPlanilha(linhas, colunas);
        clone.celulasRaw = new HashMap<>(this.celulasRaw);
        return clone;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < linhas; i++) {
            sb.append("\n  [");
            for (int j = 0; j < colunas; j++) {
                String ref = converteIndicesParaRef(i, j);
                Expressao val = this.celulasAvaliadas.get(ref.toUpperCase());

                if (val == null) {
                    val = this.getCelula(i, j);
                }

                sb.append(val.toString());
                if (j < colunas - 1) sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("\n]");
        return sb.toString();
    }

    // Método primário de leitura (por String)
    public Expressao getCelula(String ref) {
        String key = ref.toUpperCase();
        Expressao expressao = celulasRaw.get(key);

        if (expressao == null) {
            return new ValorInteiro(0);
        }
        return expressao;
    }

    // Método primário de escrita (por String)
    public void setCelula(String ref, Expressao expressao) {
        int[] indices = converteRefParaIndices(ref);

        validarLimites(indices[0], indices[1], ref);

        String key = ref.toUpperCase();
        celulasRaw.put(key, expressao);
    }

    // --- Métodos de conveniência (para os comandos) ---

    // Leitura por (i, j)
    public Expressao getCelula(int i, int j) {
        validarLimites(i, j, null);

        String ref = converteIndicesParaRef(i, j);
        return getCelula(ref);
    }

    public void setCelula(int i, int j, Expressao expressao) {
        validarLimites(i, j, null);

        String ref = converteIndicesParaRef(i, j);
        setCelula(ref, expressao);
    }

    // validacao

    // Converte (0, 0) para "A1"
    private static String converteIndicesParaRef(int row, int col) {
        char colChar = (char) ('A' + col);
        String rowStr = Integer.toString(row + 1);
        return colChar + rowStr;
    }

    // Converte "A1" para [0, 0]
    private static int[] converteRefParaIndices(String ref) {
        if (ref == null || ref.length() < 2) {
            throw new RuntimeException("Referência de célula inválida." + ref);
        }
        String refUpper = ref.toUpperCase();
        char colChar = refUpper.charAt(0);
        String rowStr = refUpper.substring(1);

        if (colChar < 'A' || colChar > 'Z') {
            throw new RuntimeException("Coluna inválida na referência A1: " + colChar);
        }
        try {
            int col = colChar - 'A';
            int row = Integer.parseInt(rowStr) - 1;
            return new int[]{ row, col };
        } catch (NumberFormatException e) {
            throw new RuntimeException("Linha inválida na referência A1: " + rowStr);
        }
    }

    /**
     * Verifica se os índices (i, j) estão dentro dos limites [0..linhas-1] e [0..colunas-1].
     * Lança uma RuntimeException se estiverem fora.
     */
    private void validarLimites(int i, int j, String ref) {
        if (i < 0 || i >= this.linhas || j < 0 || j >= this.colunas) {
            String refStr = (ref != null) ? java.lang.String.format(" ('%s')", ref) : "";
            throw new RuntimeException(String.format(
                    "Erro: Posição %s (%d, %d) está fora dos limites definidos para a planilha [0..%d][0..%d]",
                    refStr, i, j, this.linhas - 1, this.colunas - 1
            ));
        }
    }
}