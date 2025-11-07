package li2.plp.imperative2.memory;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

public class ValorPlanilha implements Valor {

    private Expressao[][] celulas;
    private int linhas;
    private int colunas;

    public ValorPlanilha(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.celulas = new Expressao[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                celulas[i][j] = new ValorInteiro(0);
            }
        }
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
        return null;
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
    public Expressao getCelula(int i, int j) {
        if (i >= 0 && i < linhas && j >= 0 && j < colunas) {
            return celulas[i][j];
        }
        throw new RuntimeException("Índice fora dos limites da planilha.");
    }

    public void setCelula(int i, int j, Expressao expressao) {
        if (i >= 0 && i < linhas && j >= 0 && j < colunas) {
            celulas[i][j] = expressao;
        } else {
            throw new RuntimeException("Índice fora dos limites da planilha.");
        }
    }

    // Este método define como a planilha será impressa na tela (comando write)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < linhas; i++) {
            sb.append("\n  [");
            for (int j = 0; j < colunas; j++) {
                sb.append(celulas[i][j].toString());
                if (j < colunas - 1) sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("\n]");
        return sb.toString();
    }
}
