package li2.plp.imperative2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

public class ExpAcessoCelulaRef implements Expressao {

    private Id nomePlanilha;
    private ValorString ref;

    public ExpAcessoCelulaRef(Id nomePlanilha, ValorString ref) {
        this.nomePlanilha = nomePlanilha;
        this.ref = ref;
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        ValorPlanilha planilha = (ValorPlanilha) amb.get(nomePlanilha);
        String refString = ref.valor();

        Expressao celula = planilha.getCelula(refString);

        return celula.avaliar(amb);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return true;
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
        return new ExpAcessoCelulaRef(nomePlanilha, ref);
    }
}
