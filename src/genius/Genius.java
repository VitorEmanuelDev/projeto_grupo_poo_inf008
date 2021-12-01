package genius;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game's flow and interface
 */
public class Genius extends JPanel implements ActionListener, MouseListener {

	private static Genius genius;
	private List<Jogador> jogadores;
	private SequenciaDeCores sequenciaAtual;
	private JButton botao;
	private QuadradosCores[] cores;
	private Timer temporizador;

	// CONSTANTES:
	private static final String NOME = "Genius - Projeto POO!";
	private static final int LARGURA   = 600;
	private static final int ALTURA  = 600;
	private static final int ESPACO_PADS_LARGURA = 470;
	private static final int ESPACO_PADS_ALTURA = 470;
	private static final int ESPACO_PADS_OFFSET = (ALTURA - ESPACO_PADS_ALTURA) / 4;
	private static final int ESPACO_EXTRA_Y = 50;
	private static final int ESCPACO_PADS = 25;
	private static final int LARGURA_PADS = ESPACO_PADS_LARGURA / 2 - ESCPACO_PADS *2;
	private static final int ALTURA_PADS = ESPACO_PADS_ALTURA / 2 - ESCPACO_PADS *2;
	private static final int NUM_PADS = 4;
	private static final int LARGURA_BOTAO_PRINCIPAL = 100;
	private static final int ALTURA_BOTAO_PRINCIPAL = 25;
	private static final int TEMPORIZADOR_DELAY = 20;
	private static final Color COR_FUNDO = new Color(0,0,0);//new Color(255,255,255);


	// COMANDOS DO JOGO
	private boolean jogoRodando;
	private boolean jogoTerminado;
	private boolean avancarDeFase;
	private boolean mostrarSequencia;
	private int cliques;
	private int indiceDePadroesDoJogador;
	private int indiceDePadroesDoJogo;
	private int indexJogadorAtual;


	/**
	 * Construtor cria o frame a desenha os graficos do jogo
	 */
	public Genius() {
		criarFrame();
		criarBotaoPrincipal();

		//placar = new Placar();
		jogadores = new ArrayList<Jogador>();
		jogadores.add(new Jogador("player 1"));
		jogadores.add(new Jogador("player 2"));
		indexJogadorAtual = 0;
		cores = new QuadradosCores[NUM_PADS];
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
		jogoRodando = false;
		jogoTerminado = false;
		avancarDeFase = false;
		mostrarSequencia = false;
		cliques = 0;
		inicializarQuadradosDeCores();
		repaint();
	}

	/**
	 * Criar frame
	 */
	private void criarFrame() {
		JFrame frame = new JFrame(NOME);
		frame.setSize(LARGURA, ALTURA);
		frame.getContentPane().setBackground(COR_FUNDO);
		frame.setVisible(true);
		frame.add(this);
		frame.addMouseListener(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Cria o notao principal
	 */
	private void criarBotaoPrincipal() {
		botao = new JButton("INICIAR");
		botao.setBackground(Color.GREEN);
		botao.setForeground(Color.BLACK);
		botao.setFocusPainted(false);
		botao.setFont(new Font("Comic", Font.BOLD, 10));
		int offset_x = 250;
		int offset_y = 292;
		botao.setBounds(offset_x, offset_y, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botao.addActionListener(new MainButtonListener());
		setLayout(null);
		add(botao);
	}

	/**
	 *Inicializa os quadrados 
	 */
	private void inicializarQuadradosDeCores() {
		cores[0] = new QuadradosCores(Color.GREEN, LARGURA_PADS, ALTURA_PADS, 100, 105);
		cores[1] = new QuadradosCores(Color.RED, LARGURA_PADS, ALTURA_PADS, 315, 105);
		cores[2] = new QuadradosCores(Color.YELLOW, LARGURA_PADS, ALTURA_PADS, 100, 320);
		cores[3] = new QuadradosCores(Color.BLUE, LARGURA_PADS, ALTURA_PADS, 315, 320);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (genius != null)
			paint((Graphics2D) g);
	}

	/**
	 * Paint the game's graphics
	 * @param forma2D     2D graphics context
	 */
	private void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(COR_FUNDO);
		g.fillRect(0, 0, LARGURA, ALTURA);


		// Desenhar quadrados de cores
		for (int i = 0; i < NUM_PADS; i++) {
			cores[i].desenharColorirQuadrado(g);
		}

		botao.setText("JOGAR");

		// Desenhar placar:
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic", Font.BOLD, 14));
		g.drawString("Jogador: " + jogadores.get(indexJogadorAtual).getNome(), (LARGURA/2) - 60,  ESCPACO_PADS + ESPACO_PADS_OFFSET);
		g.drawString("Fase:  " + jogadores.get(indexJogadorAtual).getPlacar().getFase(), (LARGURA/2) - 60,  ESCPACO_PADS + ESPACO_PADS_OFFSET + 20 );
		g.drawString("Pontuacao:  " + jogadores.get(indexJogadorAtual).getPlacar().getPontuacao(), (LARGURA/2) - 60,  ESCPACO_PADS + ESPACO_PADS_OFFSET + 40);
		 
		// Texto durante o jogo
		g.setFont(new Font("Comic", Font.BOLD, 20));
		if (jogoTerminado) {
			g.setColor(Color.RED);
			g.drawString("Errou!!!", (LARGURA/2) - 40,  550);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jogoRodando)
			return;

		if (jogoTerminado) {
			JogoTerminado();
			repaint();
			return;
		}

		cliques++;

		if (mostrarSequencia) {
			if (cliques % 40 == 0) {
				avanceSequencia();
				cliques = 0;
			}
			else if (cliques % 20 == 0) {
				triggerTodasPiscando(false);
			}
		}

		else if (avancarDeFase) {
			triggerTodasPiscando(true);
			if (cliques % 60 == 0) {
				triggerTodasPiscando(false);
				iniciarProximaFase();
				cliques = 0;
			}
		}
		else { // rodada do jogador
			if (cliques % 20 == 0) {
				triggerTodasPiscando(false);
				cliques = 0;
			}
		}
		repaint();
	}


	/**
	 * Iniciar o jogo pelo temporizador, reinicializando o placar e iniciando uma sequencia
	 */
	private void iniciarJogo() {
		temporizador.start();
		triggerTodasPiscando(false);
		jogoRodando = true;
		jogoTerminado = false;
		jogadores.get(indexJogadorAtual).getPlacar().reinicializar();
		jogadores.get(indexJogadorAtual).getPlacar().proximaFase();
		iniciarUmaSequencia();
	}

	/**
	 * Iniciar um sequencia com o numero piscadas equivalente ao fase do jogador
	 */
	private void iniciarUmaSequencia() {
		sequenciaAtual = new SequenciaDeCores(jogadores.get(indexJogadorAtual).getPlacar().getFase());
		indiceDePadroesDoJogo = 0;
		indiceDePadroesDoJogador = 0;
		mostrarSequencia = true;
	}

	/**
	 * Avance para o proximo elemento da sequencia
	 */
	private void avanceSequencia() {
		if (indiceDePadroesDoJogo >= sequenciaAtual.getTamanho()) {
			mostrarSequencia = false;
			return;
		}
		cores[sequenciaAtual.getIndice(indiceDePadroesDoJogo)].setPiscada(true);
		indiceDePadroesDoJogo++;
	}

	/**
	 * 
	 * Suba um fase e atualize o placar
	 */
	private void avancarDeFase() {
		avancarDeFase = true;
		jogadores.get(indexJogadorAtual).getPlacar().proximaFase();
	}

	/**
	 * Inicia o proximo fase
	 */
	private void iniciarProximaFase() {
		avancarDeFase = false;
		sequenciaAtual = null;
		iniciarUmaSequencia();
	}

	/**
	 * jogo terminado, parar temporizador, mas manter placar
	 * 
	 */
	private void JogoTerminado() {
		alertaJogoTerminado(true);
		jogoRodando = false;
		jogoTerminado = true;
		temporizador.stop();
	}

	/**
	 * Alert que todos os quadrados acarca de uma mudanca no estado do jogo
	 * @param bool true se o jogo tiver terminado ou falso, caso contrario
	 */
	private void alertaJogoTerminado(boolean bool) {
		for (int i = 0; i < NUM_PADS; i++)
			cores[i].setJogoTerminado(bool);
	}


	/**
	 *  Mudar o piscar das cores
	 * @param bool  true se os quadrados devem estar piscando, do contrario, false
	 */
	private void triggerTodasPiscando(boolean bool) {
		for (int i = 0; i< NUM_PADS; i++)
			cores[i].setPiscada(bool);
	}


	/**
	 * Listener class to the Main button
	 */
	private class MainButtonListener implements ActionListener {
		private MainButtonListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!jogoRodando) {
				alertaJogoTerminado(false);
				iniciarJogo();
			}
		}
	}


	// Mouse Listeners:
	@Override
	public void mousePressed(MouseEvent e) {
		if (jogoRodando && !mostrarSequencia && !avancarDeFase) {
			int indiceDaCorClicada = coordenadasQuadrado(e.getX(), e.getY());
			if (indiceDaCorClicada == -1)
				return;
			cores[indiceDaCorClicada].setPiscada(true);
			repaint();
			cliques = 0;
			if (indiceDaCorClicada == sequenciaAtual.getIndice(indiceDePadroesDoJogador)) {
				jogadores.get(indexJogadorAtual).getPlacar().aumentarPontuacao();
				indiceDePadroesDoJogador++;
				if (indiceDePadroesDoJogador >= sequenciaAtual.getTamanho()) {
					avancarDeFase();
				}
			} else if (indexJogadorAtual < jogadores.size()) {
				indexJogadorAtual++;
			} else {
				jogoTerminado = true;
			}
		}
	}

	/**
	 * Pega o indice do quadrado colorido dentro de uma dada coordenada
	 * @param x     x coordinate
	 * @param y     y coordinate
	 * @return      indice do quadrado de cor dentro das coordenadas; -1 se nenhum quadrado estiver dentro
	 * 
	 */
	private int coordenadasQuadrado(int x, int y) {
		for (int i = 0; i < NUM_PADS; i++) {
			if (cores[i].estaDentroDasCoordenadas(x, y))
				return i;
		}
		return -1;
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}

	// Rodar jogo
	public static void main(String[] args) {
		genius = new Genius();
	}
}
