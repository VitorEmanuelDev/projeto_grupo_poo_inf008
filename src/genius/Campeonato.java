package genius;

import java.util.ArrayList;
import java.util.List;

public class Campeonato {

	private String _nome;
	private List<Jogador> _jogadores;
	private List<Jogador> _jogadoresEmpatados;

	public Campeonato() {
		_jogadores = new ArrayList<Jogador>();
		_jogadoresEmpatados = new ArrayList<Jogador>();
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

	public Jogador getJogador(int indexJogador) {
		if (_jogadoresEmpatados.isEmpty()) {
			return _jogadores.get(indexJogador);
		} else {
			return _jogadoresEmpatados.get(indexJogador);
		}
	}
	
	public void adicionaJogador(Jogador jogador) {
		_jogadores.add(jogador);
	}
	
	public int getQuantidadeJogadores() {
		if (_jogadoresEmpatados.isEmpty()) {
			return _jogadores.size();
		} else {
			return _jogadoresEmpatados.size();
		}
	}
	
	public boolean checaEmpate() {
		_jogadoresEmpatados.clear();

		int maiorPontuacao = 0;
		for (Jogador jogador : _jogadores) {
			if (jogador.getPlacar().getPontuacao() > maiorPontuacao) {
				maiorPontuacao = jogador.getPlacar().getPontuacao();
			}
		}
		for (Jogador jogador : _jogadores) {
			if (jogador.getPlacar().getPontuacao() == maiorPontuacao) {
				_jogadoresEmpatados.add(jogador);
			}
		}
		
		if (_jogadoresEmpatados.size() == 1) {
			_jogadoresEmpatados.clear();
		}
		
		return !_jogadoresEmpatados.isEmpty();
	}
}
