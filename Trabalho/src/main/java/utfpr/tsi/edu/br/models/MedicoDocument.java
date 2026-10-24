package utfpr.tsi.edu.br.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dados_brutos")
public class MedicoDocument {

    @Id
    private String id;
    private String nome;
    private String crm;
    private String dataInscricao;
    private String primeiraInscricaoUf;
    private String inscricao;
    private String inscricoesOutroEstado;
    private String especialidades;
    private String endereco;
    private String telefone;
    private String instituicaoGraduacao;
    private String anoFormatura;
    private String situacao;

    public MedicoDocument() {
    }

    public MedicoDocument(
            String nome,
            String crm,
            String dataInscricao,
            String primeiraInscricaoUf,
            String inscricao,
            String inscricoesOutroEstado,
            String especialidades,
            String endereco,
            String telefone,
            String instituicaoGraduacao,
            String anoFormatura,
            String situacao
    ) {
        this.nome = nome;
        this.crm = crm;
        this.dataInscricao = dataInscricao;
        this.primeiraInscricaoUf = primeiraInscricaoUf;
        this.inscricao = inscricao;
        this.inscricoesOutroEstado = inscricoesOutroEstado;
        this.especialidades = especialidades;
        this.endereco = endereco;
        this.telefone = telefone;
        this.instituicaoGraduacao = instituicaoGraduacao;
        this.anoFormatura = anoFormatura;
        this.situacao = situacao;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(String dataInscricao) { this.dataInscricao = dataInscricao; }

    public String getPrimeiraInscricaoUf() { return primeiraInscricaoUf; }
    public void setPrimeiraInscricaoUf(String primeiraInscricaoUf) { this.primeiraInscricaoUf = primeiraInscricaoUf; }

    public String getInscricao() { return inscricao; }
    public void setInscricao(String inscricao) { this.inscricao = inscricao; }

    public String getInscricoesOutroEstado() { return inscricoesOutroEstado; }
    public void setInscricoesOutroEstado(String inscricoesOutroEstado) { this.inscricoesOutroEstado = inscricoesOutroEstado; }

    public String getEspecialidades() { return especialidades; }
    public void setEspecialidades(String especialidades) { this.especialidades = especialidades; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getInstituicaoGraduacao() { return instituicaoGraduacao; }
    public void setInstituicaoGraduacao(String instituicaoGraduacao) { this.instituicaoGraduacao = instituicaoGraduacao; }

    public String getAnoFormatura() { return anoFormatura; }
    public void setAnoFormatura(String anoFormatura) { this.anoFormatura = anoFormatura; }

    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
}