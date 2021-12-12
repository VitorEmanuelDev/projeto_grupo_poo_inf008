package genius;

import java.util.ArrayList;
import java.util.List;

public class Campeonato {

	private String _nome;
	private List<Jogador> _jogadores;

	public Campeonato() {
		_jogadores = new ArrayList<Jogador>();
	}
	
	public Campeonato(String nome) {
		_nome = nome;
	}

	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public List<Jogador> getJogadores() {
		return _jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		_jogadores = jogadores;
	}
}
