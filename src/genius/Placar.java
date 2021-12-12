package genius;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Representa o placar
 */
public class Placar {

	private Integer _fase;
	private Integer _pontuacao;
	Instant _tempoInicioJogada;       
	Instant _tempoFimJogada;
	ArrayList<Long> _tempoDaJogada;

	/**
	 * Construtor cria um novo placar
	 */
	public Placar() {
		_fase = 0;
		_pontuacao = 0;
		_tempoDaJogada = new ArrayList<>();
	}

	/**
	 * Reinicia o placar
	 */
	public void reinicializar() {
		_fase = 0;
		_pontuacao = 0;
	}

	/**
	 * Aumenta a pontuacao de acordo com o nivel
	 */
	public void aumentarPontuacao() {
		_pontuacao += _fase * 10;
	}

	/**
	 * Incrementa a fase e atribui uma pontuacao
	 */
	public void proximaFase() {
		_fase++;
		if(_fase != 1) {
			_pontuacao += _fase;
		}
	}

	/**
	 * @return  informa a fase do jogador
	 */
	public Integer getFase() {
		return _fase;
	}

	/**
	 * @return informa a pontuacao do jogador
	 */
	public Integer getPontuacao() {
		return _pontuacao;
	}

	public Instant getTempoInicioJogada() {
		return _tempoInicioJogada;
	}

	public void setTempoInicioJogada(Instant tempoInicioJogada) {
		_tempoInicioJogada = tempoInicioJogada;
	}

	public Instant getTempoFimJogada() {
		return _tempoFimJogada;
	}

	public void setTempoFimJogada(Instant tempoFimJogada) {
		_tempoFimJogada = tempoFimJogada;
	}

	public ArrayList<Long> getTempoDaJogada() {
		return _tempoDaJogada;
	}

	public void setTempoDaJogada(ArrayList<Long> tempoDaJogada) {
		_tempoDaJogada = tempoDaJogada;
	}

	public Integer getPontuacaoTemp() {
		if(_fase == 1) {
			return (_fase - 1) * 10;
		} else {
			return _fase + (_fase - 1) * 10;
		}
	}
}
