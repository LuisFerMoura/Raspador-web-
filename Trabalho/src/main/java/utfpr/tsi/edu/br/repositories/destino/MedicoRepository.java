package utfpr.tsi.edu.br.repositories.destino;

import org.springframework.data.mongodb.repository.MongoRepository;
import utfpr.tsi.edu.br.models.Medico;

public interface MedicoRepository extends MongoRepository<Medico, String> {
}