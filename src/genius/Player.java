package genius;

public class Player {

	Placar placar;
	String nome;

	public Player(Placar placar) {
		this.placar = placar;
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


}
