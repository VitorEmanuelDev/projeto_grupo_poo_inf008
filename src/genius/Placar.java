package genius;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o placar
 */
public class Placar {

	private Integer fase;
	private Integer pontuacao;
	private Instant instanteFaseAtual;
	private Long tempoPrePausa;
	private List<Long> tempoJogadas;

	/**
	 * Construtor cria um novo placar
	 */
	public Placar() {
		fase = 0;
		pontuacao = 0;
		// o L no final do numero significa que é para ser interpretado como `long`
		tempoPrePausa = 0L;
		instanteFaseAtual = null;
		tempoJogadas = new ArrayList<>();
	}

	/**
	 * Reinicia o placar
	 */
	public void reinicializar() {
		fase = 0;
		pontuacao = 0;
		instanteFaseAtual = null;
		tempoPrePausa = 0L;
		tempoJogadas.clear();
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
		if (fase != 1) {
			tempoJogadas.add(Duration.between(instanteFaseAtual, Instant.now()).getSeconds() + tempoPrePausa);
			tempoPrePausa = 0L;
			pontuacao += fase;
		}
		instanteFaseAtual = Instant.now();
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

	public Long getTempoJogada(int indexJogada) {
		return tempoJogadas.get(indexJogada);
	}

	public List<Long> getTempoJogadas() {
		return tempoJogadas;
	}

	public void pausaJogada() {
		tempoPrePausa += Duration.between(instanteFaseAtual, Instant.now()).getSeconds();
	}

	public void retomaJogada() {
		instanteFaseAtual = Instant.now();
	}

	public Integer ultimaPontuacaoAcrescentada() {
		if (fase != 1) {
			int ultimaPontuacao = fase;
			for (int i = 1; i < fase; i++) {
				ultimaPontuacao += (fase - 1) * 10;
			}
			return ultimaPontuacao;
		}

		return 0;
	}
}
