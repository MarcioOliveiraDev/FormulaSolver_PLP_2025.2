package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative1.memory.EntradaVaziaException;
import li2.plp.imperative1.memory.ErroTipoEntradaException;
import li2.plp.imperative2.memory.ValorPlanilha;

public class ComandoAtribuicaoCelula implements Comando {

    private Id nomePlanilha;
    private Expressao linha;
    private Expressao coluna;
    private Expressao expressaoParaGuardar;

    public ComandoAtribuicaoCelula(Id nomePlanilha, Expressao linha, Expressao coluna, Expressao expressaoParaGuardar) {
        this.nomePlanilha = nomePlanilha;
        this.linha = linha;
        this.coluna = coluna;
        this.expressaoParaGuardar = expressaoParaGuardar;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa ambiente) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException, EntradaVaziaException, ErroTipoEntradaException {
        Valor valLinha = linha.avaliar(ambiente);
        Valor valColuna = coluna.avaliar(ambiente);

        ValorPlanilha planilha = (ValorPlanilha) ambiente.get(nomePlanilha);

        planilha.setCelula(
                ((ValorInteiro) valLinha).valor(),
                ((ValorInteiro) valColuna).valor(),
                expressaoParaGuardar // passando a expressão para avaliação tardia de propósito!
        );

        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa ambiente) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException, EntradaVaziaException {
        return linha.checaTipo(ambiente) && coluna.checaTipo(ambiente) && expressaoParaGuardar.checaTipo(ambiente);
    }
}
