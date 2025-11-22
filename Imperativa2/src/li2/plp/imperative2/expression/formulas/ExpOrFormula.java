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

public class ExpOrFormula extends ExpFormulaMultipleArgs {

    public ExpOrFormula(ListaExpressao args) {
        super(args);
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        boolean resultado = false;
        for (Expressao expressao : args) {
            Valor val = expressao.avaliar(amb);
            if (((ValorBooleano) val).valor()) {
                resultado = true;
                break; // Otimização: se achou um true, já é true
            }
        }
        return new ValorBooleano(resultado);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        for (Expressao expressao : args) {
            if (!expressao.getTipo(amb).eBooleano()) return false;
        }
        return true;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) {
        return TipoPrimitivo.BOOLEANO;
    }

    @Override
    public Expressao clone() {
        return new ExpOrFormula(args);
    }
}