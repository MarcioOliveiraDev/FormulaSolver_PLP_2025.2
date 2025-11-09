package li2.plp.imperative2.declaration;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.imperative1.declaration.Declaracao;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative1.memory.EntradaVaziaException;
import li2.plp.imperative2.expression.TipoPlanilha;
import li2.plp.imperative2.expression.ValorPlanilha;

public class DeclaracaoPlanilha extends Declaracao {

    private Id id;
    private int linhas;
    private int colunas;

    public DeclaracaoPlanilha(String id, int linhas, int colunas) {
        this.id = new Id(id);
        this.linhas = linhas;
        this.colunas = colunas;
    }

    @Override
    public AmbienteExecucaoImperativa elabora(AmbienteExecucaoImperativa ambiente) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException, EntradaVaziaException {
        ambiente.map(id, new ValorPlanilha(linhas, colunas));
        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa ambiente) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException, EntradaVaziaException {
        ambiente.map(id, new TipoPlanilha());
        return true;
    }
}
