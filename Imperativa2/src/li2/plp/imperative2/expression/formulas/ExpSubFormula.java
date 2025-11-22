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

public class ExpSubFormula extends ExpFormulaMultipleArgs {

    public ExpSubFormula(ListaExpressao args) {
        super(args);
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        // Se não houver argumentos, retornamos 0
        if (args == null || args.getHead() == null) {
             return new ValorInteiro(0);
        }
        
        // 1. Coletar os valores em uma lista Java
        List<Integer> valores = new ArrayList<Integer>();
        for (Expressao expressao : args) {
            Valor val = expressao.avaliar(amb);
            valores.add(((ValorInteiro) val).valor());
        }
        
        // 2. Inverter a lista para restaurar a ordem original (ex: [3, 10] -> [10, 3])
        Collections.reverse(valores);

        // 3. Aplicar a lógica de subtração
        int resultado = 0;
        boolean primeiro = true;

        for (Integer valor : valores) {
            if (primeiro) {
                resultado = valor;
                primeiro = false;
            } else {
                resultado -= valor;
            }
        }
        return new ValorInteiro(resultado);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        for (Expressao expressao : args) {
            if (!expressao.getTipo(amb).eInteiro()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return TipoPrimitivo.INTEIRO;
    }

    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        return this;
    }

    @Override
    public Expressao clone() {
        return new ExpSubFormula(args);
    }
}