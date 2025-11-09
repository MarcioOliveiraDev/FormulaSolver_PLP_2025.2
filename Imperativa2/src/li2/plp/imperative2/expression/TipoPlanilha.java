package li2.plp.imperative2.expression;

import li2.plp.expressions1.util.Tipo;

public class TipoPlanilha implements Tipo {
    @Override
    public String getNome() {
        return "";
    }

    @Override
    public boolean eInteiro() {
        return false;
    }

    @Override
    public boolean eBooleano() {
        return false;
    }

    @Override
    public boolean eString() {
        return false;
    }

    @Override
    public boolean eIgual(Tipo tipo) {
        return tipo instanceof TipoPlanilha;
    }

    @Override
    public boolean eValido() {
        return false;
    }

    @Override
    public Tipo intersecao(Tipo outroTipo) {
        return null;
    }

    @Override
    public String toString() {
        return "sheet";
    }
}
