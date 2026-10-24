package br.edu.utfpr.td.tsi.minha.etl;

import br.edu.utfpr.td.tsi.minha.etl.Extrator;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

public class ExtratorMongo<T, ID> extends Extrator<T> {

    private MongoRepository<T, ID> repository;

    @Override
    public List<T> extrair() {
        return repository.findAll();
    }

    public void setRepository(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }
}
