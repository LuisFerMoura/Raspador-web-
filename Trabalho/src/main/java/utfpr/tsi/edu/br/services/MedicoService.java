package utfpr.tsi.edu.br.services;

import br.edu.utfpr.td.tsi.minha.etl.CarregadorMongoDB;
import br.edu.utfpr.td.tsi.minha.etl.ExtratorMongo;
import br.edu.utfpr.td.tsi.minha.etl.Job;
import org.springframework.stereotype.Service;
import utfpr.tsi.edu.br.etl.TransformadorMedico;
import utfpr.tsi.edu.br.models.Medico;
import utfpr.tsi.edu.br.models.MedicoDocument;
import utfpr.tsi.edu.br.repositories.origem.MedicoDocumentRepository;
import utfpr.tsi.edu.br.repositories.destino.MedicoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoService {

    private final MedicoDocumentRepository origemRepository;
    private final MedicoRepository destinoRepository;

    public MedicoService(MedicoDocumentRepository origemRepository, MedicoRepository destinoRepository) {
        this.origemRepository = origemRepository;
        this.destinoRepository = destinoRepository;
    }


    public List<MedicoDocument> salvarTodos(List<MedicoDocument> medicos) {
        List<MedicoDocument> salvos = new ArrayList<>();

        for (MedicoDocument m : medicos) {
            if (m.getCrm() != null) {
                origemRepository.findByCrm(m.getCrm()).ifPresent(existente -> m.setId(existente.getId()));
                salvos.add(origemRepository.save(m));
            }
        }

        return salvos;
    }


    public void executarEtl() {

        ExtratorMongo<MedicoDocument, String> extrator = new ExtratorMongo<>();
        extrator.setRepository(origemRepository);

        TransformadorMedico transformador = new TransformadorMedico();

        CarregadorMongoDB<Medico, String> carregador = new CarregadorMongoDB<>();
        carregador.setRepository(destinoRepository);

        Job<MedicoDocument, Medico> job = new Job<>();
        job.setExtrator(extrator);
        job.setTransformador(transformador);
        job.setCarregador(carregador);

        job.executar();
    }

    public List<MedicoDocument> listarTodosBrutos() {
        return origemRepository.findAll();
    }

    public List<Medico> listarTodosTransformados() {
        return destinoRepository.findAll();
    }
}