package genius;

import java.util.List;

public class Jogador implements java.io.Serializable {

	/**
	 * Representação de um Jogador, este contém um nome, apelido e placar
	 */
	private static final long serialVersionUID = 4577235192388939683L;
	private Placar placar;
	private String nome;
	private String apelido;

	/**
	 * Construtor, cria um novo jogador com determinado nome e apelido
	 * @param nome    nome do jogador
	 * @param apelido    apelido do jogador
	 */
	public Jogador(String nome, String apelido) {
		this.nome = nome;
		this.apelido = apelido;
		this.placar = new Placar();
	}

	/**
	 * @return   nome do jogador
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Muda nome do jogador
	 * @param nome   novo nome do jogador
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return   apelido do jogador
	 */
	public String getApelido() {
		return apelido;
	}

	/**
	 * Muda apelido do jogador
	 * @param apelido   novo apelido do jogador
	 */
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	/**
	 * Incrementa pontuação do jogador
	 */
	public void incrementaPontuacao() {
		placar.aumentarPontuacao();
	}

	/**
	 * Retorna fase atual do jogador
	 * @return    fase que o jogador esta atualmente
	 */
	public int getFaseAtual() {
		return placar.getFase();
	}

	/**
	 * Muda fase atual do jogador
	 * @param novaFase    nova fase do jogador
	 */
	public void setFaseAtual(int novaFase) {
		placar.setFase(novaFase);
	}

	/**
	 * Pausa contador de tempo da jogada
	 */
	public void pausaJogada() {
		placar.pausaJogada();
	}

	/**
	 * Retoma contagem do contador de tempo da jogada
	 */
	public void retomaJogada() {
		placar.retomaJogada();
	}

	/**
	 * Incrementa fase do jogador
	 */
	public void avancaFase() {
		placar.proximaFase();
	}

	/**
	 * Retorna pontuação atual do jogador
	 * @return  pontuação atual do jogador
	 */
	public int getPontuacao() {
		return placar.getPontuacao();
	}

	/**
	 * Retorna pontuação obtida pelo jogador na última jogada
	 * @return   última pontuação do jogador
	 */
	public int ultimaPontuacaoAcrescentada() {
		return placar.ultimaPontuacaoAcrescentada();
	}

	/**
	 * Retorna tempo de jogada no index especificado
	 * @param indexJogada    index da jogada desejada
	 * @return  tempo em segundos da jogada desejada
	 */
	public Long getTempoJogada(int indexJogada) {
		return placar.getTempoJogada(indexJogada);
	}

	/**
	 * Retorna lista de todos os tempos de jogadas do jogador
	 * @return  lista com tempo em segundos de todas as jogadas
	 */
	public List<Long> getTempoJogadas() {
		return placar.getTempoJogadas();
	}
}
