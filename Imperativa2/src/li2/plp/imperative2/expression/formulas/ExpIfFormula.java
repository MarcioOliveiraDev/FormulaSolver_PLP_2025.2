package li2.plp.imperative2.expression.formulas;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorBooleano;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

public class ExpIfFormula implements Expressao {

    private Expressao cond;
    private Expressao expTrue;
    private Expressao expFalse;

    public ExpIfFormula(Expressao cond, Expressao expTrue, Expressao expFalse) {
        this.cond = cond;
        this.expTrue = expTrue;
        this.expFalse = expFalse;
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Valor valCond = cond.avaliar(amb);

        if (!(valCond instanceof ValorBooleano)) {
            throw new RuntimeException("O primeiro argumento da fórmula IF deve ser um booleano (true/false).");
        }

        boolean cond = ((ValorBooleano) valCond).valor();

        if (cond) {
            return expTrue.avaliar(amb);
        } else {
            return expFalse.avaliar(amb);
        }
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        boolean condOk = cond.getTipo(amb).eBooleano();
        boolean trueOk = expTrue.checaTipo(amb);
        boolean falseOk = expFalse.checaTipo(amb);

        return condOk && trueOk && falseOk;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return null;
    }

    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        return this;
    }

    @Override
    public Expressao clone() {
        return new ExpIfFormula(cond, expTrue, expFalse);
    }
}
