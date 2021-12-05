package genius;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.FlowLayout;
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

	private static Genius genius;//singleton?
	private Campeonato campeonato = new Campeonato();;
	private int tamanhoLista = 2;//teste hardcoded
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private List<JTextField> nomesJogadores = new ArrayList<JTextField>();
	private List<JTextField> apelidosJogadores = new ArrayList<JTextField>();
	private SequenciaDeCores sequenciaAtual;
	private JButton botaoIniciar;
	private JButton botaoInserirDados;
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
	private boolean jogoRodando = false;
	private boolean jogoTerminado = false;
	private boolean jogadorErrou = false;
	private boolean avancarDeFase = false;
	private boolean mostrarSequencia = false;
	private int cliques = 0;
	private int indiceDePadroesDoJogador;
	private int indiceDePadroesDoJogo;
	private int indexJogadorAtual = 0;


	/**
	 * Construtor cria o frame a desenha os graficos do jogo
	 */
	public Genius() {
		criarFrame();
		criarBotaoPrincipal();
		//placar = new Placar();  
		//jogadores.add(new Jogador("player 1"));
		//jogadores.add(new Jogador("player 2"));
		cores = new QuadradosCores[NUM_PADS];
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
		inicializarQuadradosDeCores();
		repaint();
	}

	private void criarEntradaDeDadosInicial() {
	
		JFrame frame = new JFrame("Entrada inicial de dados");
		JLabel labelCampeonato = new JLabel();
		JTextField nomeCampeonato = new JTextField(15);
		frame.setSize(400, 400);
		frame.getContentPane().setBackground(COR_FUNDO);
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
		labelCampeonato.setForeground(Color.WHITE);
		labelCampeonato.setText("Nome do campeonato");
		frame.add(labelCampeonato);
		frame.add(nomeCampeonato);
		
		JLabel labelNumeroJogadores = new JLabel();
		labelNumeroJogadores.setForeground(Color.WHITE);

		for(int i = 0; i < tamanhoLista; i++) {
			Jogador jogador = new Jogador();
			jogador.setPlacar(new Placar());
			JLabel labelNome = new JLabel();
			JLabel labelApelido = new JLabel();
			JTextField fieldNome = new JTextField(15);
			JTextField fieldApelido = new JTextField(15);
			labelNome.setForeground(Color.WHITE);
			labelApelido.setForeground(Color.WHITE);
			int display = i + 1;
			labelNome.setText("Nome do jogador " + display);
			frame.add(labelNome);
			frame.add(fieldNome);	
			labelApelido.setText("Apelido do jogador " + display);
			frame.add(labelApelido);
			frame.add(fieldApelido);	

			jogadores.add(jogador);
			nomesJogadores.add(fieldNome);
			apelidosJogadores.add(fieldApelido);
			
		}
	
		criarBotaoInserirDados(frame, nomeCampeonato);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
	}

	/**
	 * Criar frame
	 */
	private void criarFrame() {
		JFrame frame = new JFrame(NOME);
		frame.setSize(LARGURA, ALTURA);
		frame.getContentPane().setBackground(COR_FUNDO);   
		/*
		 * combobox para escolhe do numero de jogadores
		 * 
		 * https://www.javatpoint.com/java-jcombobox
		 * 
		 * 
		 * */
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
		botaoIniciar = new JButton("JOGAR");
		botaoIniciar.setBackground(Color.GREEN);
		botaoIniciar.setForeground(Color.BLACK);
		botaoIniciar.setFocusPainted(false);
		botaoIniciar.setFont(new Font("Comic", Font.BOLD, 10));
		int offset_x = 250;
		int offset_y = 292;
		botaoIniciar.setBounds(offset_x, offset_y, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botaoIniciar.addActionListener(new botaoIniciarListener());
		setLayout(null);
		add(botaoIniciar);
	}

	/**
	 * Cria o botao para pop up de inserir dados
	 * @param frame 
	 * @param nomeCampeonato 
	 */
	private void criarBotaoInserirDados(JFrame frame, JTextField nomeCampeonato) {
		botaoInserirDados = new JButton("INICIAR");
		botaoInserirDados.setBackground(Color.GREEN);
		botaoInserirDados.setForeground(Color.BLACK);
		botaoInserirDados.setFocusPainted(false);
		botaoInserirDados.setFont(new Font("Comic", Font.BOLD, 10));
		botaoInserirDados.addActionListener(new botaoInserirDadosListener(nomeCampeonato, frame));
		setLayout(null);
		frame.add(botaoInserirDados);	
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

		// Desenhar placar:
		g.setColor(Color.WHITE);
		if (jogoRodando) {
			g.setFont(new Font("Comic", Font.BOLD, 14));
			System.out.println(campeonato.getNome());
			System.out.println(campeonato.getJogadores().get(indexJogadorAtual));
			System.out.println(campeonato.getJogadores().get(indexJogadorAtual).getNome());
			g.drawString("Jogador: " + campeonato.getJogadores().get(indexJogadorAtual).getNome(), (LARGURA/2) - 60,  ESCPACO_PADS + ESPACO_PADS_OFFSET);
			g.drawString("Fase:  " + campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getFase(), (LARGURA/2) - 60,  ESCPACO_PADS + ESPACO_PADS_OFFSET + 20 );
			g.drawString("Pontuacao:  " + campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getPontuacao(), (LARGURA/2) - 60,  ESCPACO_PADS + ESPACO_PADS_OFFSET + 40);
		}
		// Texto durante o jogo
		g.setFont(new Font("Comic", Font.BOLD, 20));
		if (jogoTerminado || jogadorErrou) {
			g.setColor(Color.RED);
			// se indexJogadorAtual for 0 então significa que o ultimo jogador errou, nesse caso
			// usamos o tamanho da lista de jogadores como base para pegar o index adequado
			int indexJogadorErrou;

			if(indexJogadorAtual == 0) {
				indexJogadorErrou = jogadores.size() - 1;
			}else {
				indexJogadorErrou = indexJogadorAtual - 1;
			}
			g.drawString(campeonato.getJogadores().get(indexJogadorErrou).getNome() + " errou!!!", (LARGURA/2) - 80,  550);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jogoRodando)
			return;

		if (jogoTerminado) {
			finalizaJogo();
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
	private void iniciarJogada() {
		temporizador.start();
		triggerTodasPiscando(false);
		jogoRodando = true;
		jogoTerminado = false;
		botaoIniciar.setVisible(jogoTerminado);
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().reinicializar();
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().proximaFase();
		iniciarUmaSequencia();
	}

	/**
	 * Iniciar um sequencia com o numero piscadas equivalente ao fase do jogador
	 */
	private void iniciarUmaSequencia() {
		sequenciaAtual = new SequenciaDeCores(campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getFase());
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
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().proximaFase();
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
	private void finalizaJogo() {
		alertaJogoTerminado(true);
		jogoRodando = false;
		jogoTerminado = true;
		jogadorErrou = false;
		botaoIniciar.setVisible(jogoTerminado);
		indexJogadorAtual = 0;
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
	private class botaoIniciarListener implements ActionListener {
		private botaoIniciarListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!jogoRodando) {
				alertaJogoTerminado(false);
				criarEntradaDeDadosInicial();
				//iniciarJogada();
			}
		}
	}

	/**
	 * Listener class to the Main button
	 */
	private class botaoInserirDadosListener implements ActionListener {
		
		JTextField nomeCampeonato;
		JFrame frame;
		
		private botaoInserirDadosListener(JTextField nomeCampeonato, JFrame frame) {
			 this.nomeCampeonato = nomeCampeonato;
			 this.frame = frame;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!jogoRodando) {
				
				String nomeCampeonatoStr = nomeCampeonato.getText();
				System.out.println("nome campeonato " + nomeCampeonatoStr);//test
			
				if(nomeCampeonatoStr != null && !nomeCampeonatoStr.isEmpty()) {
					campeonato.setNome(nomeCampeonatoStr);		
				}else {
					campeonato.setNome("Desafio Genius");
				}
				
				for(int i = 0; i < tamanhoLista; i++) {
					
					String nomeJogador = nomesJogadores.get(i).getText();
					System.out.println("nome jogador " + nomeJogador);//test
					int display = i + 1;
					if(nomeJogador != null && !nomeJogador.isEmpty()) {
						jogadores.get(i).setNome(nomeJogador);
					}else {
						jogadores.get(i).setNome("Player " + display);
					}
			
					String apelidoJogador = apelidosJogadores.get(i).getText();
					System.out.println("apelido jogador " + apelidoJogador);//test
					if(apelidoJogador != null && !apelidoJogador.isEmpty()) {
						jogadores.get(i).setApelido(apelidoJogador);
					}else {
						jogadores.get(i).setApelido(apelidoJogador);
					}					
				}
				campeonato.setJogadores(jogadores);		
				alertaJogoTerminado(false);
				frame.setVisible(false);
				iniciarJogada();
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
				jogadorErrou = false;
				campeonato.getJogadores().get(indexJogadorAtual).getPlacar().aumentarPontuacao();
				indiceDePadroesDoJogador++;
				if (indiceDePadroesDoJogador >= sequenciaAtual.getTamanho()) {
					avancarDeFase();
				}
			} else if (indexJogadorAtual + 1 < jogadores.size()) {
				jogadorErrou = true;
				indexJogadorAtual++;
				cliques = 0;
				iniciarJogada();
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
