package utfpr.tsi.edu.br;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utfpr.tsi.edu.br.models.MedicoDocument;
import utfpr.tsi.edu.br.services.MedicoService;

import java.util.List;

@Component
public class RaspadorRunner implements CommandLineRunner {

    private final MedicoService medicoService;

    public RaspadorRunner(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @Override
    public void run(String... args) {
        String uf = "PR";
        RaspadorHtml raspador = new RaspadorHtml();
        List<MedicoDocument> medicosBrutos = raspador.buscarMedicos(uf);

        if (!medicosBrutos.isEmpty()) {
            medicoService.salvarTodos(medicosBrutos);
        }

        uf = "ES";
        RaspadorHtml raspador2 = new RaspadorHtml();
        List<MedicoDocument> medicosBrutos2 = raspador2.buscarMedicos(uf);

        if (!medicosBrutos2.isEmpty()) {
            medicoService.salvarTodos(medicosBrutos2);
        }

        medicoService.executarEtl();
    }
}