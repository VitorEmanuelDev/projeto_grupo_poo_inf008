package genius;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Representa o placar
 */
public class Placar {

	private Integer fase;
	private Integer pontuacao;
	Instant tempoInicioJogada;       
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
	 * Aumenta a pontuacao de acordo com o nivel
	 */
	public void aumentarPontuacao() {
		pontuacao += fase * 10;
	}

	/**
	 * Incrementa a fase e atribui uma pontuacao
	 */
	public void proximaFase() {
		fase++;
		if(fase != 1) {
			pontuacao += fase;
		}
	}

	/**
	 * @return  informa a fase do jogador
	 */
	public Integer getFase() {
		return fase;
	}

	/**
	 * @return informa a pontuacao do jogador
	 */
	public Integer getPontuacao() {
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

	public Integer ultimaPontuacaoAcrescentada() {
		if(fase != 1) {
			int ultimaPontuacao = fase;
			for (int i = 1; i < fase; i++) {
				ultimaPontuacao += (fase - 1) * 10;
			}
			return ultimaPontuacao;
		}
		
		return 0;
	}
}
