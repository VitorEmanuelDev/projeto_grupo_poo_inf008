package genius;

public abstract class Player {
	
	Placar placar;
	
	public Player() {

	}

	public Placar getPontuacao() {
		return placar;
	}

	public void setPontuacao(Placar pontuacao) {
		this.placar = pontuacao;
	}
		

}
