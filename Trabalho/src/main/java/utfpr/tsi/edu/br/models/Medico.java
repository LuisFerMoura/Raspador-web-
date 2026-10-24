package utfpr.tsi.edu.br.models;

import java.util.List;

public class Medico {
    private String id;
    private String nome;
    private String crm;
    private String estado;
    private String situacao;
    private Formacao formacao;
    private List<Especialidade> especialidades;
    private String endereco;
    private List<String> telefones;
    private List<InscricaoOutroEstado> inscricoesOutroEstado;

    public Medico() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Formacao getFormacao() {
        return formacao;
    }

    public void setFormacao(Formacao formacao) {
        this.formacao = formacao;
    }

    public List<Especialidade> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Especialidade> especialidades) {
        this.especialidades = especialidades;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

    public List<InscricaoOutroEstado> getInscricoesOutroEstado() {
        return inscricoesOutroEstado;
    }

    public void setInscricoesOutroEstado(List<InscricaoOutroEstado> inscricoesOutroEstado) {
        this.inscricoesOutroEstado = inscricoesOutroEstado;
    }

    public static class Formacao {
        private String instituicao;
        private String campus;
        private String instituicaoRevalidacao;
        private Integer anoGraduacao;

        public Formacao() {
        }

        public String getInstituicao() {
            return instituicao;
        }

        public void setInstituicao(String instituicao) {
            this.instituicao = instituicao;
        }

        public String getCampus() {
            return campus;
        }

        public void setCampus(String campus) {
            this.campus = campus;
        }

        public String getInstituicaoRevalidacao() {
            return instituicaoRevalidacao;
        }

        public void setInstituicaoRevalidacao(String instituicaoRevalidacao) {
            this.instituicaoRevalidacao = instituicaoRevalidacao;
        }

        public Integer getAnoGraduacao() {
            return anoGraduacao;
        }

        public void setAnoGraduacao(Integer anoGraduacao) {
            this.anoGraduacao = anoGraduacao;
        }
    }

    public static class Especialidade {
        private String descricao;
        private String rqe;

        public Especialidade() {
        }

        public Especialidade(String descricao, String rqe) {
            this.descricao = descricao;
            this.rqe = rqe;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getRqe() {
            return rqe;
        }

        public void setRqe(String rqe) {
            this.rqe = rqe;
        }
    }

    public static class InscricaoOutroEstado {
        private String estado;
        private String crm;
        private String situacao;

        public InscricaoOutroEstado() {
        }

        public InscricaoOutroEstado(String estado, String crm, String situacao) {
            this.estado = estado;
            this.crm = crm;
            this.situacao = situacao;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getCrm() {
            return crm;
        }

        public void setCrm(String crm) {
            this.crm = crm;
        }

        public String getSituacao() {
            return situacao;
        }

        public void setSituacao(String situacao) {
            this.situacao = situacao;
        }
    }
}