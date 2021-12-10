package genius;

import java.util.ArrayList;
import java.util.List;

public class Campeonato {

	private String nome;
	private List<Jogador> jogadores;

	public Campeonato() {
		this.jogadores = new ArrayList<Jogador>();
	}
	
	public Campeonato(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}
	
}
