package li2.plp.imperative2.expression.formulas;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative2.command.ListaExpressao;

public abstract class ExpFormulaMultipleArgs implements Expressao {
    protected ListaExpressao args;

    public ExpFormulaMultipleArgs(ListaExpressao args) {
        this.args = args;
    }

    public ListaExpressao getArgs() {
        return args;
    }

    @Override
    public abstract Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException;

    @Override
    public abstract Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException;

    @Override
    public abstract Expressao clone();

    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        return this;
    }
}
