package br.edu.utfpr.td.tsi.minha.etl;

import java.util.List;

public abstract class Carregador<T> {

    public abstract void carregar(List<T> itens);
}
