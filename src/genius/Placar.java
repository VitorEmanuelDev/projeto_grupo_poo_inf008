package genius;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Placar implements java.io.Serializable {

	/**
	 * Representação do placar de um Jogador, contém a fase e pontuação atual,
	 * além de também ter um contador de tempo de jogadas
	 */
	private static final long serialVersionUID = -2516303699424558751L;
	private Integer fase;
	private Integer pontuacao;
	private Instant instanteFaseAtual;
	private Long tempoPrePausa;
	private List<Long> tempoJogadas;

	/**
	 * Construtor, cria um novo placar e zera os atributos
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
	 * Aumenta a pontuação de acordo com o nível
	 */
	public void aumentarPontuacao() {
		pontuacao += fase * 10;
	}

	/**
	 * Incrementa a fase, atribui uma pontuação e adiciona tempo da jogada
	 */
	public void proximaFase() {
		fase++;
		if (instanteFaseAtual != null) {
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
	 * Modifica fase para novo valor
	 * @param novaFase  novo valor a ser utilizado
	 */
	public void setFase(int novaFase) {
		fase = novaFase;
	}

	/**
	 * @return informa a pontuação do jogador
	 */
	public Integer getPontuacao() {
		return pontuacao;
	}

	/**
	 * Retorna tempo em segundos de determinada fase
	 * @param indexJogada   index do tempo de jogada
	 * @return   tempo em segundos de determinada jogada
	 */
	public Long getTempoJogada(int indexJogada) {
		return tempoJogadas.get(indexJogada);
	}

	/**
	 * Retorna lista do tempo de todas as jogadas
	 * @return  lista com tempo de todas as jogadas
	 */
	public List<Long> getTempoJogadas() {
		return tempoJogadas;
	}

	/**
	 * Pausa a jogada atual, temporariamente deixa de contar o tempo do jogador
	 */
	public void pausaJogada() {
		if (instanteFaseAtual != null) {
			tempoPrePausa += Duration.between(instanteFaseAtual, Instant.now()).getSeconds();
			instanteFaseAtual = null;
		}
	}

	/**
	 * Retoma a jogada, voltando a contar o tempo do jogador
	 */
	public void retomaJogada() {
		instanteFaseAtual = Instant.now();
	}

	/**
	 * Calcula a pontuação da fase anterior e a retorna
	 * @return   ultima pontuação acrescentada ao avançar a fase
	 */
	public Integer ultimaPontuacaoAcrescentada() {
		// somente vamos calcular a última pontuação acrescentada, caso o
		// jogador tenha jogado ao menos uma vez
		if (!tempoJogadas.isEmpty()) {
			int ultimaPontuacao = fase;
			for (int i = 1; i < fase; i++) {
				ultimaPontuacao += (fase - 1) * 10;
			}
			return ultimaPontuacao;
		}

		return 0;
	}
}
