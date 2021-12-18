package genius;

import java.util.ArrayList;
import java.util.List;

public class Campeonato implements java.io.Serializable {

	/**
	 * Representação de um campeonato, contém um nome, a lista de competidores
	 * e também uma lista com jogadores em situação de empate pelo primeiro lugar
	 */
	private static final long serialVersionUID = -3346276515940504216L;
	// TODO consider putting here the indexCurrentPlayer, velocityModule and offsetPhase
	private String nome;
	private List<Jogador> jogadores;
	private List<Jogador> jogadoresEmpatados;

	/**
	 * Construtor, inicializa a lista de jogadores do campeonato
	 */
	public Campeonato() {
		jogadores = new ArrayList<Jogador>();
		jogadoresEmpatados = new ArrayList<Jogador>();
	}

	/**
	 * Construtor, inicializa a lista de jogadores do campeonato e também com o nome
	 */
	public Campeonato(String nome) {
		this.nome = nome;
		jogadores = new ArrayList<Jogador>();
		jogadoresEmpatados = new ArrayList<Jogador>();
	}

	/**
	 * @return nome do campeonato
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Muda o nome do campeonato
	 * @param nome      novo nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Retorna lista de todos os jogadores do campeonato
	 * @return     lista de todos os jogadores
	 */
	public List<Jogador> getJogadores() {
		return jogadores;
	}

	/**
	 * Muda lista de jogadores do campeonato
	 * @param jogadores   nova lista de jogadores
	 */
	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	/**
	 * Retorna jogador em determinado index, caso a lista de jogadores em situação de
	 * empate não estiver vazia esta vai ser utilizado para recuperar o jogador, caso
	 * esteja vazia, a lista de todos os jogadores será usada
	 * @param indexJogador    index do jogador a ser retornado
	 * @return  jogador no indexJogador
	 */
	public Jogador getJogador(int indexJogador) {
		if (jogadoresEmpatados.isEmpty()) {
			return jogadores.get(indexJogador);
		} else {
			return jogadoresEmpatados.get(indexJogador);
		}
	}

	/**
	 * Adiciona jogador a lista de jogadores
	 * @param jogador     jogador a ser adicionado
	 */
	public void adicionaJogador(Jogador jogador) {
		jogadores.add(jogador);
	}

	/**
	 * Retorna a quantidade de jogadores
	 * @return    caso houverem 2 ou mais jogadores empatadores,
	 *            retorna a quantidade de jogadores empatados,
	 *            caso contrário retorna o numero de jogadores totais
	 */
	public int getQuantidadeJogadores() {
		if (jogadoresEmpatados.isEmpty()) {
			return jogadores.size();
		} else {
			return jogadoresEmpatados.size();
		}
	}

	/**
	 * Verifica e popula lista de jogadores empatados pelo primeiro lugar
	 * @return    true se o número de jogadores empatados for 2 ou mais, false caso contrario
	 */
	public boolean checaEmpate() {
		jogadoresEmpatados.clear();

		int maiorPontuacao = 0;
		for (Jogador jogador : jogadores) {
			if (jogador.getPontuacao() > maiorPontuacao) {
				maiorPontuacao = jogador.getPontuacao();
			}
		}
		for (Jogador jogador : jogadores) {
			if (jogador.getPontuacao() == maiorPontuacao) {
				jogadoresEmpatados.add(jogador);
			}
		}
		
		if (jogadoresEmpatados.size() == 1) {
			jogadoresEmpatados.clear();
		}
		
		return !jogadoresEmpatados.isEmpty();
	}
}
