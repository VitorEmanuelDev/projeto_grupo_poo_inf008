package genius;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Genius implements ActionListener {

	// ter classe gui aqui?
	private GeniusGUI gui;
	private int toques;
	private int indicePadraoJogada;
	private int indicePadraoSequenciaAtual;
	private int indiceJogadorAtual;
	private Integer moduloVelocidade;
	private int offsetFase;
	private Campeonato campeonatoAtual;
	private Timer temporizador;
	private SequenciaCores sequenciaAtual;

	private static final int TEMPORIZADOR_DELAY = 20;

	private boolean jogoRodando;
	private boolean avancarFase;
	private int indiceJogadorErrou;
	
	/**
	 * Construtor, cria o frame principal, inicializa o temporizador
	 */
	public Genius() {
		toques = 0;
		indicePadraoJogada = 0;
		indicePadraoSequenciaAtual = 0;
		indiceJogadorAtual = 0;
		sequenciaAtual = new SequenciaCores(0);
		jogoRodando = false;
		avancarFase = false;
		indiceJogadorErrou = -1;
	}

	/*
	 * Inicia o campeonato, definindo a velocidade e dificuldade do campeonato,
	 * além de inicializar a jogada do primeiro jogador
	 */
	public void iniciaCampeonato(int velocidade, int quantidadeSequencia) {
		moduloVelocidade = velocidade;
		offsetFase = quantidadeSequencia;
		temporizador.start();
		jogoRodando = true;
		gui.triggerTodasPiscando(false);
		iniciaProximaFase();
	}

	/**
	 * Finaliza o campeonato, para temporizador e retorna estado para o inicial (antes do campeonato)
	 */
	private void finalizaCampeonato() {
		gui.alertaJogoTerminado(true);
		jogoRodando = false;
		indiceJogadorAtual = 0;
		indiceJogadorErrou = -1;
		temporizador.stop();
	}

	/**
	 * Inicia a fase do jogador baseado na dificuldade e a sequência de cores para a jogada
	 */
	private void iniciaProximaFase() {
		// Definimos a fase atual baseado na dificuldade selecionada, e posteriormente,
		// subtraimos esse valor para obtermos a numeração da fase adequada
		if (campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual() == 0) {
			campeonatoAtual.getJogador(indiceJogadorAtual).setFaseAtual(offsetFase);
		}
		campeonatoAtual.getJogador(indiceJogadorAtual).avancaFase();
		gui.atualizaPlacar(campeonatoAtual.getJogador(indiceJogadorAtual));
		// gera nova sequência de cores
		sequenciaAtual = new SequenciaCores(campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual());
		indicePadraoSequenciaAtual = 0;
	}

	public void reiniciaFase() {
		gui.atualizaPlacar(campeonatoAtual.getJogador(indiceJogadorAtual));
		sequenciaAtual = new SequenciaCores(campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual());
		indicePadraoSequenciaAtual = 0;
		toques = 0;
	}

	public void checaJogada(int indiceCorClicada) {
		toques = 0;
		if (indiceCorClicada == sequenciaAtual.getElemento(indicePadraoJogada)) {
			campeonatoAtual.getJogador(indiceJogadorAtual).incrementaPontuacao();
			gui.atualizaPlacar(campeonatoAtual.getJogador(indiceJogadorAtual));
			indicePadraoJogada += 1;
			indiceJogadorErrou = -1;
			avancarFase = indicePadraoJogada >= sequenciaAtual.getQuantidade();
		} else if (indiceJogadorAtual + 1 < campeonatoAtual.getQuantidadeJogadores()) {
			indiceJogadorErrou = indiceJogadorAtual;
			indiceJogadorAtual += 1;
			indicePadraoJogada = 0;
			if (campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual() != 0) {
				campeonatoAtual.getJogador(indiceJogadorAtual).retomaJogada();
				reiniciaFase();
			} else {
				toques = 0;
				iniciaProximaFase();
			}
		} else if (campeonatoAtual.checaEmpate()) {
			gui.mostraDialogMensagem("Continuar jogo em fase extra.", "Ocorreu um empate!!!");

			indiceJogadorAtual = 0;
			campeonatoAtual.getJogador(indiceJogadorAtual).retomaJogada();
			reiniciaFase();
		} else {
			finalizaCampeonato();
			// TODO ver com a gui fica com o final do jogo
			gui.criaRelatorioFinal(campeonatoAtual);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jogoRodando) {
			return;
		}

		toques += 1;

		if (indicePadraoSequenciaAtual < sequenciaAtual.getQuantidade()) {
			if (toques % moduloVelocidade == 0) {
				gui.ativaBotaoCor(indicePadraoSequenciaAtual);
				indicePadraoSequenciaAtual += 1;
				toques = 0;
			}
			else if (toques % 20 == 0) {
				gui.triggerTodasPiscando(false);
			}
		} else if (avancarFase) { // caso true, significa que devemos ir para a proxima fase
			indicePadraoJogada = 0;
			avancarFase = false;
			gui.triggerTodasPiscando(true);
			//gui.repaint();
			if (toques % 60 == 0) { // consider do not have it here
				gui.triggerTodasPiscando(false);
				iniciaProximaFase();
				//gui.repaint();
				toques = 0;
			}
		} else if (toques % 20 == 0) {
			gui.triggerTodasPiscando(false);
			//gui.repaint();
			toques = 0;
		}
		// TODO colocar condicional, nao pode sempre atualizar a tela
		// caso o indiceJogadorErrou diferente de -1, significa que devemos alertar que um jogador errou
		if (indiceJogadorErrou != -1) {
			gui.alertaJogadorErrou(campeonatoAtual.getJogador(indiceJogadorErrou));
		}

	}

	public void run() {
		gui = new GeniusGUI(this);
		temporizador = new Timer(/*TEMPORIZADOR_DELAY */ 20, this);
	}

	// Roda jogo
	public static void main(String[] args) throws InterruptedException{
		Genius genius = new Genius();
		genius.run();
	}

	/**
	 * Retorna booleano que representa que o jogo esta rodando ou não TODO corrigir doc
	 * @return   true caso o jogo esteja em progresso, false caso contrário
	 */
	public boolean getJogoRodando() {
		boolean estaMostrandoSequencia = indicePadraoSequenciaAtual < sequenciaAtual.getQuantidade();
		return jogoRodando && !estaMostrandoSequencia && !avancarFase;
	}

	public Integer getOffsetFase() {
		return offsetFase;
	}

	public void setOffsetFase(Integer novoOffset) {
		offsetFase = novoOffset;
	}

	public void pausaJogada() {
		if (jogoRodando) {
			campeonatoAtual.getJogador(indiceJogadorAtual).pausaJogada();
			jogoRodando = false;
			temporizador.stop();
		}
	}

	public void retomaJogada() {
		if (!jogoRodando) {
			campeonatoAtual.getJogador(indiceJogadorAtual).retomaJogada();
			jogoRodando = true;
			temporizador.restart();
		}
	}

	public Campeonato getCampeonatoAtual() {
		return campeonatoAtual;
	}

	public void setCampeonatoAtual(Campeonato novoCampeonato) {
		campeonatoAtual = novoCampeonato;
	}

	public Integer getIndexJogadorAtual() {
		return indiceJogadorAtual;
	}

	public void setIndexJogadorAtual(Integer novoIndex) {
		indiceJogadorAtual = novoIndex;
	}

	public Integer getModuloVelocidade() {
		return moduloVelocidade;
	}

	public void setModuloVelocidade(Integer novaVelocidade) {
		moduloVelocidade = novaVelocidade;
	}
}
