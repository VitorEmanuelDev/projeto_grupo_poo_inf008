package genius;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Genius implements ActionListener {

	/**
	 * Classe que contém a lógica principal do jogo, sendo o controlador do funcionamento deste
	 */
	private GeniusGUI gui;
	private int toques;
	private int indicePadraoSequenciaCor;
	private int indiceJogadorAtual;
	private int indiceJogadorErrou;
	private Integer moduloVelocidade;
	private int offsetFase;
	private Campeonato campeonatoAtual;
	private Timer temporizador;
	private SequenciaCores sequenciaAtual;

	private static final int TEMPORIZADOR_DELAY = 20;

	private boolean jogoRodando;
	private boolean avancarFase;
	private int indicePadraoSequenciaJogador;
	
	/**
	 * Construtor, cria o frame principal, inicializa o temporizador
	 */
	public Genius() {
		toques = 0;
		indicePadraoSequenciaCor = 0;
		indicePadraoSequenciaJogador = 0;
		indiceJogadorErrou = -1;
		indiceJogadorAtual = -1;
		jogoRodando = false;
		avancarFase = false;
		sequenciaAtual = new SequenciaCores(0);
	}

	/**
	 * Inicializa classe visual e temporizador de atualizações da tela do jogo
	 */
	public void run() {
		gui = new GeniusGUI(this);
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
	}

	// função main, inicializa a classe Genius e inicia o jogo
	public static void main(String[] args) throws InterruptedException{
		Genius genius = new Genius();
		genius.run();
	}

	/**
	 * Inicia campeonato, define a velocidade e dificuldade do campeonato,
	 * além de inicializar a jogada do primeiro jogador
	 * @param velocidade   velocidade a ser utilizada pelo jogo
	 * @param quantidadeSequencia    número de sequências inicial a ser utilizada pelo jogo
	 */
	public void iniciaCampeonato(int velocidade, int quantidadeSequencia) {
		moduloVelocidade = velocidade;
		offsetFase = quantidadeSequencia;
		temporizador.start();
		jogoRodando = true;
		indiceJogadorAtual = 0;
		gui.triggerTodasPiscando(false);
		iniciaProximaFase();
	}

	/**
	 * Finaliza campeonato, para o temporizador, retorna estado para o inicial (antes do campeonato)
	 * e gera relatório final do campeonato
	 */
	private void finalizaCampeonato() {
		gui.alertaJogoTerminado(true);
		jogoRodando = false;
		indiceJogadorAtual = -1;
		indiceJogadorErrou = -1;
		gui.criaRelatorioFinal(campeonatoAtual);
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
		// gera nova sequência de cores
		sequenciaAtual = new SequenciaCores(campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual());
		indicePadraoSequenciaCor = 0;
		indicePadraoSequenciaJogador = 0;
		toques = 0;
	}

	/**
	 * Cria nova sequência baseado na fase atual e reinicializa fase
	 */
	public void reiniciaFase() {
		sequenciaAtual = new SequenciaCores(campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual());
		indicePadraoSequenciaCor = 0;
		indicePadraoSequenciaJogador = 0;
		toques = 0;
	}

	/**
	 * Verifica se o jogador acertou a jogada, contabilizando o seu acerto ou erro, e também
	 * verifica se ocorreu algum empate, iniciando rodada extras caso necessário
	 * @param indiceCorClicada   indice da cor clicada pelo usuário
	 */
	public void checaJogada(int indiceCorClicada) {
		toques = 0;
		if (indiceCorClicada == sequenciaAtual.getElemento(indicePadraoSequenciaJogador)) {
			campeonatoAtual.getJogador(indiceJogadorAtual).incrementaPontuacao();
			indicePadraoSequenciaJogador += 1;
			indiceJogadorErrou = -1;
			avancarFase = indicePadraoSequenciaJogador >= sequenciaAtual.getQuantidade();
		} else if (indiceJogadorAtual + 1 < campeonatoAtual.getQuantidadeJogadores()) {
			indiceJogadorErrou = indiceJogadorAtual;
			indiceJogadorAtual += 1;
			if (campeonatoAtual.getJogador(indiceJogadorAtual).getFaseAtual() != 0) {
				campeonatoAtual.getJogador(indiceJogadorAtual).retomaJogada();
				reiniciaFase();
			} else {
				iniciaProximaFase();
			}
		} else if (campeonatoAtual.checaEmpate()) {
			gui.mostraDialogMensagem("Continuar jogo em fase extra.", "Ocorreu um empate!!!");

			indiceJogadorAtual = 0;
			campeonatoAtual.getJogador(indiceJogadorAtual).retomaJogada();
			reiniciaFase();
		} else {
			finalizaCampeonato();
		}
	}

	// função chamada pelo temporizador, responsável por gerenciar comportamentos
	// da interfase gráfica, quando o jogo deve avançar de fase e ativar os botões de cores
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jogoRodando) {
			return;
		}

		toques += 1;

		if (indicePadraoSequenciaCor < sequenciaAtual.getQuantidade()) {
			if (toques % moduloVelocidade == 0) {
				gui.ativaBotaoCor(sequenciaAtual.getElemento(indicePadraoSequenciaCor));
				indicePadraoSequenciaCor += 1;
				toques = 0;
			} else if (toques % 20 == 0) {
				gui.triggerTodasPiscando(false);
			}
		} else if (avancarFase) { // caso true, significa que devemos ir para a proxima fase
			gui.triggerTodasPiscando(true);
			if (toques % 60 == 0) {
				gui.triggerTodasPiscando(false);
				indicePadraoSequenciaJogador = 0;
				avancarFase = false;
				toques = 0;
				iniciaProximaFase();
			}
		} else if (toques % 20 == 0) {
			if (indicePadraoSequenciaCor == sequenciaAtual.getQuantidade()) {
				// somente entramos nesse if caso for ativado o ultimo botão da sequência
				indicePadraoSequenciaCor += 1;
			}
			gui.triggerTodasPiscando(false);
			toques = 0;
		}

		// otimização, somente redenha a tela atual caso toques seja 0
		if (toques == 0) {
			gui.repaint();
		}
	}

	/**
	 * Retorna booleano que representa se o jogo esta esperando entrada do jogador ou não
	 * @return   true caso o jogo espere clique do jogador, false caso contrário
	 */
	public boolean jogadorPodeClicar() {
		boolean estaMostrandoSequencia = indicePadraoSequenciaCor <= sequenciaAtual.getQuantidade();
		return jogoRodando && !estaMostrandoSequencia && !avancarFase;
	}

	/**
	 * Pausa jogada atual, parando o temporizador e pausando o jogador atual
	 */
	public void pausaJogada() {
		if (jogoRodando) {
			campeonatoAtual.getJogador(indiceJogadorAtual).pausaJogada();
			jogoRodando = false;
			temporizador.stop();
		}
	}

	/**
	 * Retoma jogada atual, reiniciando o temporizador e continuando o tempo do jogador atual
	 */
	public void retomaJogada() {
		if (!jogoRodando) {
			campeonatoAtual.getJogador(indiceJogadorAtual).retomaJogada();
			jogoRodando = true;
			temporizador.restart();
		}
	}

	/**
	 * @return tetorna o campeonato atual do jogo
	 */
	public Campeonato getCampeonatoAtual() {
		return campeonatoAtual;
	}

	/**
	 * Define o campeonato que será disputado
	 * @param novoCampeonato    novo campeonato a ser disputado
	 */
	public void setCampeonatoAtual(Campeonato novoCampeonato) {
		campeonatoAtual = novoCampeonato;
	}

	/**
	 * @return retorna indice do jogador atual
	 */
	public Integer getIndiceJogadorAtual() {
		return indiceJogadorAtual;
	}

	/**
	 * Define o jogador atual do jogo
	 * @param novoIndice   novo indice a ser utilizado como jogador atual
	 */
	public void setIndiceJogadorAtual(Integer novoIndice) {
		indiceJogadorAtual = novoIndice;
	}

	/**
	 * @return retorna velocidade do jogo
	 */
	public Integer getModuloVelocidade() {
		return moduloVelocidade;
	}

	/**
	 * O offsetFase é utilizado como base das fases dos jogadores, definindo a dificuldade do jogo
	 * @return   retorna a dificuldade atual do jogo
	 */
	public Integer getOffsetFase() {
		return offsetFase;
	}

	/**
	 * Define a dificuldade atual
	 * @param novoOffset   nova dificuldade a ser utilizada como base
	 */
	public void setOffsetFase(Integer novoOffset) {
		offsetFase = novoOffset;
	}

	/**
	 * Define a velocidade que será usada no jogo
	 * @param novaVelocidade    nova velocidade a ser utilizada
	 */
	public void setModuloVelocidade(Integer novaVelocidade) {
		moduloVelocidade = novaVelocidade;
	}

	/**
	 * @return Retorna jogador que errou, caso contrário retorna nulo
	 */
	public Jogador getJogadorErrou() {
		if (indiceJogadorErrou != -1) {
			return campeonatoAtual.getJogador(indiceJogadorErrou);
		}
		return null;
	}

	/**
	 * @return Retorna jogador atual, caso o jogo não esteja em progresso retorna nulo
	 */
	public Jogador getJogadorAtual() {
		if (indiceJogadorAtual != -1) {
			return campeonatoAtual.getJogador(indiceJogadorAtual);
		}
		return null;
	}
}