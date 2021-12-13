package genius;

public class Jogador {
	Placar placar;
	String nome;
	String apelido;

	public Jogador(String nome, String apelido) {
		this.nome = nome;
		this.apelido = apelido;
		placar = new Placar();
	}

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
}
