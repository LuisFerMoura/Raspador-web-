package utfpr.tsi.edu.br.repositories.origem;

import utfpr.tsi.edu.br.models.MedicoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoDocumentRepository extends MongoRepository<MedicoDocument, String> {

    boolean existsByCrm(String crm);

    Optional<MedicoDocument> findByCrm(String crm);
}