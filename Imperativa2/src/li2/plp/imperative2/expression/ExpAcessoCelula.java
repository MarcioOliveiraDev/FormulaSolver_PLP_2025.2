package li2.plp.imperative2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative2.expression.ValorPlanilha;

public class ExpAcessoCelula implements Expressao {

    private Id nomePlanilha;
    private Expressao linha;
    private Expressao coluna;

    public ExpAcessoCelula(Id nomePlanilha, Expressao linha, Expressao coluna) {
        this.nomePlanilha = nomePlanilha;
        this.linha = linha;
        this.coluna = coluna;
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        ValorPlanilha planilha = (ValorPlanilha) amb.get(nomePlanilha);

        Valor valLinha = linha.avaliar(amb);
        Valor valColuna = coluna.avaliar(amb);

        Expressao formulaGuardada = planilha.getCelula(
            ((ValorInteiro) valLinha).valor(),
            ((ValorInteiro) valColuna).valor()
        );

        return formulaGuardada.avaliar(amb); // avaliando o conteúdo apenas na hora de apresentar
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return linha.getTipo(amb).eInteiro() && coluna.getTipo(amb).eInteiro();
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return TipoPrimitivo.INTEIRO;
    }

    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        return null;
    }

    @Override
    public Expressao clone() {
        return new ExpAcessoCelula(nomePlanilha, linha.clone(), coluna.clone());
    }
}
