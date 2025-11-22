package li2.plp.imperative2.expression.formulas;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative2.command.ListaExpressao;

public class ExpMaxFormula extends ExpFormulaMultipleArgs {

    public ExpMaxFormula(ListaExpressao args) {
        super(args);
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        if (args == null || args.getHead() == null) return new ValorInteiro(0);

        int max = Integer.MIN_VALUE;
        boolean encontrouAlgum = false;

        for (Expressao expressao : args) {
            Valor val = expressao.avaliar(amb);
            int valorInt = ((ValorInteiro) val).valor();
            if (valorInt > max) {
                max = valorInt;
            }
            encontrouAlgum = true;
        }
        return new ValorInteiro(encontrouAlgum ? max : 0);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        for (Expressao expressao : args) {
            if (!expressao.getTipo(amb).eInteiro()) return false;
        }
        return true;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) {
        return TipoPrimitivo.INTEIRO;
    }

    @Override
    public Expressao clone() {
        return new ExpMaxFormula(args);
    }
}