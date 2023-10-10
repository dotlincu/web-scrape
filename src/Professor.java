class Professor {
    private String nome;
    private String area;
    private String email;

    public Professor(String nome, String email, String area) {
        this.nome = nome;
        this.area = area;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getArea() {
        return area;
    }

    public String getEmail() {
        return email;
    }
}

class ProfessorSI extends Professor {
    public ProfessorSI(String nome, String email, String area) {
        super(nome, email, area);
    }
}

class ProfessorENP extends Professor {
    private String ramal;
    private String sala;

    public ProfessorENP(String nome, String area, String email, String ramal, String sala) {
        super(nome, area, email);
        this.ramal = ramal;
        this.sala = sala;
    }

    public String getRamal() {
        return ramal;
    }

    public String getSala() {
        return sala;
    }
}
