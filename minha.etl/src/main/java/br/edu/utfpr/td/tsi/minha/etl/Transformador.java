package br.edu.utfpr.td.tsi.minha.etl;

public abstract class Transformador<I, O> {

    public abstract O transformar(I item);
}
