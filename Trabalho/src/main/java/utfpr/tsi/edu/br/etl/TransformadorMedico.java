package utfpr.tsi.edu.br.etl;

import br.edu.utfpr.td.tsi.minha.etl.Transformador;
import utfpr.tsi.edu.br.models.Medico;
import utfpr.tsi.edu.br.models.MedicoDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformadorMedico extends Transformador<MedicoDocument, Medico> {

    private static final String UF_REGEX = "(?:AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)";
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("\\(?\\d{2}\\)?\\s?\\d{4,5}[-\\s]?\\d{4}");

    @Override
    public Medico transformar(MedicoDocument bruto) {
        Medico medico = new Medico();
        medico.setId(bruto.getId());

        if (bruto.getNome() != null) {
            medico.setNome(bruto.getNome().trim());
        }

        if (bruto.getCrm() != null) {
            Pattern pattern = Pattern.compile("(\\d+)(?:/(" + UF_REGEX + "))?", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(bruto.getCrm());

            if (matcher.find()) {
                medico.setCrm(matcher.group(1));
                if (matcher.group(2) != null) {
                    medico.setEstado(matcher.group(2).toUpperCase());
                }
            } else {
                medico.setCrm(bruto.getCrm().replaceAll("\\D+", ""));
            }
        }

        if (medico.getEstado() == null) {
            medico.setEstado(extrairUfSeguranca(bruto));
        }

        if (bruto.getSituacao() != null) {
            medico.setSituacao(bruto.getSituacao().replace("Situação:", "").trim());
        }

        if (bruto.getInscricoesOutroEstado() != null && !bruto.getInscricoesOutroEstado().trim().isEmpty()) {
            List<Medico.InscricaoOutroEstado> listaOutrosEstados = new ArrayList<>();

            String textoLimpo = bruto.getInscricoesOutroEstado()
                    .replaceAll("(?i)Inscriç[õo]e?s?\\s+em\\s+outro[s]?\\s+estado[s]?:?", "")
                    .trim();

            String regexOutroEstado = "\\b(" + UF_REGEX + ")[\\s/\\-]*?(?:CRM[-\\s]*)?(\\d+)(?:\\s*\\(([^)]+)\\))?";
            Pattern patternOutroEstado = Pattern.compile(regexOutroEstado, Pattern.CASE_INSENSITIVE);
            Matcher matcherOutroEstado = patternOutroEstado.matcher(textoLimpo);

            while (matcherOutroEstado.find()) {
                String ufOutro = matcherOutroEstado.group(1).toUpperCase();
                String crmOutro = matcherOutroEstado.group(2) != null ? matcherOutroEstado.group(2) : "";
                String situacaoOutro = matcherOutroEstado.group(3) != null ? matcherOutroEstado.group(3).trim() : "";

                listaOutrosEstados.add(new Medico.InscricaoOutroEstado(ufOutro, crmOutro, situacaoOutro));
            }

            if (!listaOutrosEstados.isEmpty()) {
                medico.setInscricoesOutroEstado(listaOutrosEstados);
            }
        }

        if (bruto.getEndereco() != null) {
            String end = bruto.getEndereco().replace("Endereço:", "").replaceAll("[\\r\\n]+", " ").trim();
            if (!end.toLowerCase().contains("exibição não autorizada")) {
                medico.setEndereco(end);
            }
        }

        if (bruto.getTelefone() != null) {
            String telTexto = bruto.getTelefone();
            if (!telTexto.toLowerCase().contains("exibição não autorizada")) {
                List<String> telefones = extrairTelefones(telTexto);
                if (!telefones.isEmpty()) {
                    medico.setTelefones(telefones);
                }
            }
        }

        Medico.Formacao formacao = new Medico.Formacao();

        if (bruto.getInstituicaoGraduacao() != null) {
            String inst = bruto.getInstituicaoGraduacao()
                    .replace("Instituição de Graduação:", "")
                    .trim();

            String nomeInstituicaoBruto = inst;


            if (inst.contains("Instituição de Revalidação:")) {
                String[] partesEstrangeira = inst.split("Instituição de Revalidação:");

                nomeInstituicaoBruto = partesEstrangeira[0];

                if (partesEstrangeira.length > 1) {
                    formacao.setInstituicaoRevalidacao(partesEstrangeira[1].replaceAll("[\\r\\n]+", " ").trim());
                }
            }


            if (nomeInstituicaoBruto.contains("-")) {
                String[] partes = nomeInstituicaoBruto.split("-");
                formacao.setInstituicao(partes[0].replaceAll("[\\r\\n]+", " ").trim());
                if (partes.length > 1) {
                    formacao.setCampus(partes[1].replace("CAMPUS", "").replaceAll("[\\r\\n]+", " ").trim());
                }
            } else {
                formacao.setInstituicao(nomeInstituicaoBruto.replaceAll("[\\r\\n]+", " ").trim());
            }
        }

        if (bruto.getAnoFormatura() != null) {
            try {
                String anoNumerico = bruto.getAnoFormatura().replaceAll("\\D+", "");
                if (!anoNumerico.isEmpty()) {
                    formacao.setAnoGraduacao(Integer.parseInt(anoNumerico));
                }
            } catch (NumberFormatException ignored) {
            }
        }
        medico.setFormacao(formacao);

        List<Medico.Especialidade> listaEspecialidades = new ArrayList<>();
        String textoEspecialidades = bruto.getEspecialidades();

        if (textoEspecialidades != null && !textoEspecialidades.trim().isEmpty()) {
            String textoMinusculo = textoEspecialidades.toLowerCase();

            boolean ehEspecialidadeValida = !textoMinusculo.contains("sem especialidade")
                    && !textoMinusculo.contains("nenhuma especialidade")
                    && !textoMinusculo.contains("exibição não autorizada");

            if (ehEspecialidadeValida) {
                String[] linhas = textoEspecialidades.split("\\r?\\n");

                for (String linha : linhas) {
                    String linhaLimpa = linha.trim();
                    if (linhaLimpa.isEmpty()) continue;

                    if (linhaLimpa.toUpperCase().contains("RQE")) {
                        String[] partes = linhaLimpa.split("(?i)RQE[:\\sºn°]*");

                        String desc = partes[0]
                                .replaceAll("(?i)Especialidade[s]?/Área[s]? de Atuação:", "")
                                .replaceAll("^[^a-zA-ZÀ-ÿ]+", "")
                                .replaceAll("[^a-zA-ZÀ-ÿ0-9()]+$", "")
                                .trim();

                        String rqe = partes.length > 1 ? partes[1].replaceAll("\\D+", "").trim() : "";

                        if (!desc.isEmpty()) {
                            listaEspecialidades.add(new Medico.Especialidade(desc, rqe));
                        }

                    } else {
                        String desc = linhaLimpa
                                .replaceAll("(?i)Especialidade[s]?/Área[s]? de Atuação:", "")
                                .replaceAll("^[^a-zA-ZÀ-ÿ]+", "")
                                .replaceAll("[^a-zA-ZÀ-ÿ0-9()]+$", "")
                                .trim();

                        if (!desc.isEmpty()) {
                            listaEspecialidades.add(new Medico.Especialidade(desc, ""));
                        }
                    }
                }
            }
        }
        medico.setEspecialidades(listaEspecialidades);

        return medico;
    }

    private List<String> extrairTelefones(String texto) {
        List<String> listaTelefones = new ArrayList<>();
        if (texto == null) return listaTelefones;

        Matcher matcher = TELEFONE_PATTERN.matcher(texto);

        while (matcher.find()) {
            String telefoneEncontrado = matcher.group().trim();
            if (!listaTelefones.contains(telefoneEncontrado)) {
                listaTelefones.add(telefoneEncontrado);
            }
        }

        return listaTelefones;
    }

    private String extrairUfSeguranca(MedicoDocument bruto) {
        Pattern pattern = Pattern.compile("/(" + UF_REGEX + ")", Pattern.CASE_INSENSITIVE);

        if (bruto.getInscricoesOutroEstado() != null) {
            Matcher matcher = pattern.matcher(bruto.getInscricoesOutroEstado());
            if (matcher.find()) return matcher.group(1).toUpperCase();
        }

        return null;
    }
}