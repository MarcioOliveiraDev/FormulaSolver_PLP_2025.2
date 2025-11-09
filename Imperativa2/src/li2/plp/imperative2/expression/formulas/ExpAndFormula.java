package li2.plp.imperative2.expression.formulas;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorBooleano;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative2.command.ListaExpressao;

public class ExpAndFormula extends ExpFormulaMultipleArgs {

    public ExpAndFormula(ListaExpressao args) {
        super(args);
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        for (Expressao exp : getArgs()) {
            ValorBooleano valor = (ValorBooleano) exp.avaliar(amb);
            if (!valor.valor()) { // se algum for false, retorna false
                return new ValorBooleano(false);
            }
        }

        return new ValorBooleano(true);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        for (Expressao exp : getArgs()) {
            if (!exp.getTipo(amb).eBooleano()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return TipoPrimitivo.BOOLEANO;
    }

    @Override
    public Expressao clone() {
        return new ExpAndFormula(args);
    }
}
