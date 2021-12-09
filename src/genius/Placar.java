package genius;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Representa o placar
 */
public class Placar {

	private int fase;
	private int pontuacao;
	Instant tempoInicioJogada ;       
	Instant tempoFimJogada;
	ArrayList<Long> tempoDaJogada;

	/**
	 * Construtor cria um novo placar
	 */
	public Placar() {
		fase = 0;
		pontuacao = 0;
		tempoDaJogada = new ArrayList<>();
	}

	/**
	 * Reinicia o placar
	 */
	public void reinicializar() {
		fase = 0;
		pontuacao = 0;
	}

	/**
	 * Incrementa a fase e atribui uma pontuacao
	 */
	public void proximaFase() {
		fase++;
		if(fase != 1)
			pontuacao += fase;
	}

	/**
	 * Aumenta a pontuacao de acordo com o nivel
	 */
	public void aumentarPontuacao() {
		pontuacao += fase * 10;
	}

	/**
	 * @return  informa a fase do jogador
	 */
	public int getFase() {
		return fase;
	}

	/**
	 * @return informa a pontuacao do jogador
	 */
	public int getPontuacao() {
		return pontuacao;
	}

	public Instant getTempoInicioJogada() {
		return tempoInicioJogada;
	}

	public void setTempoInicioJogada(Instant tempoInicioJogada) {
		this.tempoInicioJogada = tempoInicioJogada;
	}

	public Instant getTempoFimJogada() {
		return tempoFimJogada;
	}

	public void setTempoFimJogada(Instant tempoFimJogada) {
		this.tempoFimJogada = tempoFimJogada;
	}

	public ArrayList<Long> getTempoDaJogada() {
		return tempoDaJogada;
	}

	public void setTempoDaJogada(ArrayList<Long> tempoDaJogada) {
		this.tempoDaJogada = tempoDaJogada;
	}

}
