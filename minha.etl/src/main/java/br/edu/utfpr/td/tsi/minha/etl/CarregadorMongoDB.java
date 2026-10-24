package br.edu.utfpr.td.tsi.minha.etl;

import br.edu.utfpr.td.tsi.minha.etl.Carregador;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public class CarregadorMongoDB<T,ID> extends Carregador<T> {

    private MongoRepository<T, ID> repository;

    @Override
    public void carregar(List<T> list) {
        repository.saveAll(list);
    }

    public void setRepository(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }
}
