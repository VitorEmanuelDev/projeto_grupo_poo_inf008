package genius;

public class Jogador {
// TODO analisar possibilidade de uso do temporalizar aqui ou no placar
	Placar placar;
	String nome;
	String apelido;
	
	public Jogador() {
	
	}

	public Jogador(String nome) {
		this.nome = nome;
		this.placar = new Placar();
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
