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

public class ExpAvgFormula extends ExpFormulaMultipleArgs {

    public ExpAvgFormula(ListaExpressao args) {
        super(args);
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        // Se a lista for nula ou vazia, retorna 0 para evitar divisão por zero
        if (args == null || args.getHead() == null) {
             return new ValorInteiro(0);
        }
        
        int soma = 0;
        int contador = 0;

        for (Expressao expressao : args) {
            Valor val = expressao.avaliar(amb);
            soma += ((ValorInteiro) val).valor();
            contador++;
        }
        
        // Divisão Inteira (ex: média de 10 e 3 será 6, não 6.5)
        return new ValorInteiro(soma / contador);
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
        return new ExpAvgFormula(args);
    }
}