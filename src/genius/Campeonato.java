package genius;

import java.util.ArrayList;
import java.util.List;

public class Campeonato implements java.io.Serializable {

	/**
	 * Representação de um campeonato, contém um nome, a lista de competidores
	 * e também uma lista com jogadores em situação de desempate pelo primeiro lugar
	 */
	private static final long serialVersionUID = -3346276515940504216L;
	private String nome;
	private List<Jogador> jogadores;
	private List<Jogador> jogadoresEmpatados;

	public Campeonato() {
		jogadores = new ArrayList<Jogador>();
		jogadoresEmpatados = new ArrayList<Jogador>();
	}
	
	public Campeonato(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return this.nome;
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

	public Jogador getJogador(int indexJogador) {
		if (jogadoresEmpatados.isEmpty()) {
			return jogadores.get(indexJogador);
		} else {
			return jogadoresEmpatados.get(indexJogador);
		}
	}
	
	public void adicionaJogador(Jogador jogador) {
		jogadores.add(jogador);
	}

	public int getQuantidadeJogadores() {
		if (jogadoresEmpatados.isEmpty()) {
			return jogadores.size();
		} else {
			return jogadoresEmpatados.size();
		}
	}
	
	public boolean checaEmpate() {
		jogadoresEmpatados.clear();

		int maiorPontuacao = 0;
		for (Jogador jogador : jogadores) {
			if (jogador.getPlacar().getPontuacao() > maiorPontuacao) {
				maiorPontuacao = jogador.getPlacar().getPontuacao();
			}
		}
		for (Jogador jogador : jogadores) {
			if (jogador.getPlacar().getPontuacao() == maiorPontuacao) {
				jogadoresEmpatados.add(jogador);
			}
		}
		
		if (jogadoresEmpatados.size() == 1) {
			jogadoresEmpatados.clear();
		}
		
		return !jogadoresEmpatados.isEmpty();
	}
}
