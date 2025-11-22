package li2.plp.imperative2.expression.formulas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class ExpMedianFormula extends ExpFormulaMultipleArgs {

    public ExpMedianFormula(ListaExpressao args) {
        super(args);
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        if (args == null || args.getHead() == null) return new ValorInteiro(0);

        List<Integer> valores = new ArrayList<Integer>();
        for (Expressao expressao : args) {
            Valor val = expressao.avaliar(amb);
            valores.add(((ValorInteiro) val).valor());
        }

        Collections.sort(valores);

        int n = valores.size();
        int mediana;

        if (n % 2 == 1) {
            // Ímpar: pega o do meio
            mediana = valores.get(n / 2);
        } else {
            // Par: média dos dois do meio (divisão inteira)
            int v1 = valores.get((n / 2) - 1);
            int v2 = valores.get(n / 2);
            mediana = (v1 + v2) / 2;
        }

        return new ValorInteiro(mediana);
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
        return new ExpMedianFormula(args);
    }
}