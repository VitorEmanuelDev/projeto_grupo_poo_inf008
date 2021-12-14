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

	public Jogador(String nome, String apelido) {
		this.nome = nome;
		this.apelido = apelido;
		this.placar = new Placar();
	}

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public void incrementaPontuacao() {
		placar.aumentarPontuacao();
	}
	
	public int getFaseAtual() {
		return placar.getFase();
	}

	public void setFaseAtual(int novaFase) {
		placar.setFase(novaFase);
	}

	public void pausaJogada() {
		placar.pausaJogada();
	}

	public void retomaJogada() {
		placar.retomaJogada();
	}

	public void avancaFase() {
		placar.proximaFase();
	}

	public int getPontuacao() {
		return placar.getPontuacao();
	}

	public int ultimaPontuacaoAcrescentada() {
		return placar.ultimaPontuacaoAcrescentada();
	}

	public Long getTempoJogada(int indexJogada) {
		return placar.getTempoJogada(indexJogada);
	}

	public List<Long> getTempoJogadas() {
		return placar.getTempoJogadas();
	}
}
