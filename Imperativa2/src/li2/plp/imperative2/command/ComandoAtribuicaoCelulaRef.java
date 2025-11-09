package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative1.memory.EntradaVaziaException;
import li2.plp.imperative1.memory.ErroTipoEntradaException;
import li2.plp.imperative2.expression.ValorPlanilha;

public class ComandoAtribuicaoCelulaRef implements Comando {

    private Id nomePlanilha;
    private ValorString ref;
    private Expressao expressaoParaGuardar;

    public ComandoAtribuicaoCelulaRef(Id nomePlanilha, ValorString ref, Expressao expressaoParaGuardar) {
        this.nomePlanilha = nomePlanilha;
        this.ref = ref;
        this.expressaoParaGuardar = expressaoParaGuardar;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa ambiente) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException, EntradaVaziaException, ErroTipoEntradaException {
        ValorPlanilha planilha = (ValorPlanilha) ambiente.get(nomePlanilha);
        String refString = ref.valor();

        planilha.setCelula(refString, expressaoParaGuardar); // passando a expressão para avaliação tardia de propósito
        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa ambiente) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException, EntradaVaziaException {
        return expressaoParaGuardar.checaTipo(ambiente);
    }
}
