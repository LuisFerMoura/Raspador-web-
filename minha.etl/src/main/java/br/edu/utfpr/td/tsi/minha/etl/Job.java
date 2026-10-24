package br.edu.utfpr.td.tsi.minha.etl;


import java.util.ArrayList;
import java.util.List;

public class Job<I, O> {
    private Extrator<I> extrator;
    private Transformador<I,O> transformador;
    private Carregador<O> carregador;

    public void setCarregador(Carregador carregador) {
        this.carregador = carregador;
    }

    public void setExtrator(Extrator extrator) {
        this.extrator = extrator;
    }

    public void setTransformador(Transformador transformador) {
        this.transformador = transformador;
    }

    public void executar() {
        List<O> dadosTransformados = new ArrayList<>();
        List<I> dadosBrutos = extrator.extrair();
        for (I item : dadosBrutos) {
            O dadoTransformado = transformador.transformar(item);
            dadosTransformados.add(dadoTransformado);
        }
        carregador.carregar(dadosTransformados);

    }

}
