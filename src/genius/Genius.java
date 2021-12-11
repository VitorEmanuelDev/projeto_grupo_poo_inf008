package genius;

import javax.swing.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class Genius extends JPanel implements ActionListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Genius genius;//singleton?
	private Campeonato campeonato;
	private Integer tamanhoLista;//teste hardcoded
	private List<JTextField> nomesJogadores = new ArrayList<JTextField>();
	private List<JTextField> apelidosJogadores = new ArrayList<JTextField>();
	private SequenciaDeCores sequenciaAtual;
	private JButton botaoIniciar;
	private JButton botaoPause;
	private JButton botaoInserirDados;
	private JComboBox<String> comboBoxDificuldade;
	private QuadradosCores[] cores;
	private Timer temporizador;
	private Long instantePrePausa = (long) 0;
	private Sons som = new Sons();

	private AudioClip[] arraySonoro = {som.getAudioVerde(),som.getAudioVermelho(),som.getAudioAmarelo(),som.getAudioAzul()};


	// CONSTANTES:
	private static final String NOME = "Genius - Projeto POO!";
	private static final int LARGURA   = 600;
	private static final int ALTURA  = 600;
	private static final int ESPACO_QUADRADOS_LARGURA = 470;
	private static final int ESPACO_QUADRADOS_ALTURA = 470;
	private static final int ESPACO_QUADRADOS_OFFSET = (ALTURA - ESPACO_QUADRADOS_ALTURA) / 4;
	private static final int ESCPACO_QUADRADOS = 25;
	private static final int LARGURA_QUADRADOS = ESPACO_QUADRADOS_LARGURA / 2 - ESCPACO_QUADRADOS *2;
	private static final int ALTURA_QUADRADOS = ESPACO_QUADRADOS_ALTURA / 2 - ESCPACO_QUADRADOS *2;
	private static final int NUM_QUADRADOS = 4;
	private static final int LARGURA_BOTAO_PRINCIPAL = 100;
	private static final int ALTURA_BOTAO_PRINCIPAL = 25;
	private static final int TEMPORIZADOR_DELAY = 20;
	private static final Color COR_FUNDO = new Color(0,0,0);


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


	// Rodar jogo
	public static void main(String[] args) throws InterruptedException{
		genius = new Genius();
	}


	/**
	 * Construtor cria o frame a desenha os graficos do jogo
	 */
	public Genius() {
		criarFrame();
		criarBotaoPrincipal();
		criarBotaoPause();
		cores = new QuadradosCores[NUM_QUADRADOS];
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
		inicializarQuadradosDeCores();
		repaint();
	}

	private void criarEntradaDeDadosInicial() {
		campeonato = new Campeonato();
		JFrame frame = new JFrame("Entrada inicial de dados");
		JLabel labelCampeonato = new JLabel();
		JTextField nomeCampeonato = new JTextField(15);
		frame.setSize(400, 400);
		frame.getContentPane().setBackground(COR_FUNDO);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		labelCampeonato.setForeground(Color.WHITE);
		labelCampeonato.setText("Nome do campeonato");
		frame.add(labelCampeonato);
		frame.add(nomeCampeonato);

		JLabel labelNumeroJogadores = new JLabel();
		labelNumeroJogadores.setForeground(Color.WHITE);

		JLabel labelDificuldade = new JLabel("Escolha a dificuldade:");
		labelDificuldade.setForeground(Color.WHITE);
		String [] dificuldades = {"fácil","médio","difícil"};        
		comboBoxDificuldade = new JComboBox<String>(dificuldades);
		comboBoxDificuldade.setBounds(50, 50, 90, 20);
		frame.add(labelDificuldade);
		frame.add(comboBoxDificuldade);

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

			campeonato.getJogadores().add(jogador);
			nomesJogadores.add(fieldNome);
			apelidosJogadores.add(fieldApelido);
		}

		criarBotaoInserirDados(frame, nomeCampeonato);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
	}

	private void criarRelatorioFinal() {

		Long jogadaMaisRapida = Long.MAX_VALUE;

		Long [] listaJogadaMaisRapida = new Long[tamanhoLista];

		for(int i = 0; i < tamanhoLista; i++) {

			for(int j = 0; j < campeonato.getJogadores().get(i).getPlacar().getTempoDaJogada().size(); j++) {
				long atual = campeonato.getJogadores().get(i).getPlacar().getTempoDaJogada().get(j);
				if(atual < jogadaMaisRapida) {
					jogadaMaisRapida = atual;
					listaJogadaMaisRapida[i] = jogadaMaisRapida;
					if(listaJogadaMaisRapida[i] == Long.MAX_VALUE) {
						listaJogadaMaisRapida[i] = (long) 99.9;
					}
				}
			}
		}

		String[] colunas = new String[] {
				"Nome", "Apelido", "Pontuacao", "Jogada mais rapida (segundos)"
		};

		Object[][] dados = new Object[tamanhoLista][4];

		for(int i = 0; i < tamanhoLista; i++) {
			for(int j = 0; j < 4; j++) {
				if(j == 0)
					dados[i][j] = campeonato.getJogadores().get(i).getNome();
				if(j == 1)
					dados[i][j] = campeonato.getJogadores().get(i).getApelido();
				if(j == 2)
					dados[i][j] = campeonato.getJogadores().get(i).getPlacar().getPontuacao();
				if(j == 3)
					dados[i][j] = listaJogadaMaisRapida[i];
			}
		}

		JTable tabela = new JTable(dados, colunas);
		tabela.setGridColor(Color.BLACK);
		tabela.setBackground(Color.WHITE);
		tabela.getAutoResizeMode();
		JFrame frame = new JFrame("Resultados do " + campeonato.getNome() + " - " + java.time.LocalDate.now());
		//frame.setSize(200, 50);
		//frame.getContentPane().setBackground(COR_FUNDO);  //cor e tamanhho precisam ser modificados 
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JScrollPane jspane = new JScrollPane(tabela);
		jspane.setSize(200, 50);
		jspane.setBackground(COR_FUNDO);
		frame.add(jspane);  
		frame.pack();
		jspane.setVisible(true);
		frame.setVisible(true);
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

		Integer[] options = {1, 2, 3, 4, 5, 6, 7 ,8};
		tamanhoLista = (Integer)JOptionPane.showInputDialog(null, "Escolha o número de jogadores:", 
				"Jogadores", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

	}



	/**
	 * Cria o botao principal
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
	 * Cria o botao pause
	 */
	private void criarBotaoPause() {
		botaoPause = new JButton("PAUSAR");
		botaoPause.setBackground(Color.GREEN);
		botaoPause.setForeground(Color.BLACK);
		botaoPause.setFocusPainted(false);
		botaoPause.setFont(new Font("Comic", Font.BOLD, 10));
		int offset_x = 250;
		int offset_y = 292;
		botaoPause.setBounds(offset_x, offset_y, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botaoPause.addActionListener(new botaoPausarListener());
		setLayout(null);
		add(botaoPause);
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
		cores[0] = new QuadradosCores(Color.GREEN, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 105);
		cores[1] = new QuadradosCores(Color.RED, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 105);
		cores[2] = new QuadradosCores(Color.YELLOW, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 320);
		cores[3] = new QuadradosCores(Color.BLUE, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 320);
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
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			cores[i].desenharColorirQuadrado(g);
		}

		// Desenhar placar:
		g.setColor(Color.WHITE);
		if (jogoRodando) {
			g.setFont(new Font("Comic", Font.BOLD, 14));
			int display = indexJogadorAtual + 1;
			g.drawString("Jogador " + display + ": " + campeonato.getJogadores().get(indexJogadorAtual).getNome(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET - 20);
			g.drawString("Fase:  " + campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getFase(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET);
			g.drawString("Pontos da fase anterior:  " + campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getPontuacaoTemp(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET + 20);
			g.drawString("Pontos totais:  " + campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getPontuacao(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET + 40);
		}
		// Texto durante o jogo
		g.setFont(new Font("Comic", Font.BOLD, 20));
		if (jogoTerminado || jogadorErrou) {
			g.setColor(Color.RED);
			// se indexJogadorAtual for 0 então significa que o ultimo jogador errou, nesse caso
			// usamos o tamanho da lista de campeonato.getJogadores() como base para pegar o index adequado
			int indexJogadorErrou;

			if(indexJogadorAtual == 0) {
				indexJogadorErrou = campeonato.getJogadores().size() - 1;
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
		botaoPause.setVisible(jogoRodando);
		botaoIniciar.setVisible(jogoTerminado);
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().setTempoInicioJogada(Instant.now());
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().proximaFase();	
		iniciarUmaSequencia();
	}

	/**
	 * Continuar o jogo pelo temporizador e iniciando uma sequencia para desempate
	 */
	private void reiniciarJogadaParaDesempate() {
		temporizador.start();
		triggerTodasPiscando(false);
		jogoRodando = true;
		jogoTerminado = false;
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().proximaFase();
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().setTempoInicioJogada(Instant.now());
		iniciarUmaSequencia();
	}

	/**
	 * Iniciar um sequencia com o numero piscadas equivalente a fase do jogador
	 */
	private void iniciarUmaSequencia() {
		int numeroSequencias = campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getFase();
		if (comboBoxDificuldade.getSelectedIndex() == 1) {
			numeroSequencias += 2;
		} else if (comboBoxDificuldade.getSelectedIndex() == 2) {
			numeroSequencias += 4;
		}
		sequenciaAtual = new SequenciaDeCores(numeroSequencias);
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
		arraySonoro[sequenciaAtual.getIndice(indiceDePadroesDoJogo)].play();
		indiceDePadroesDoJogo++;
	}

	/**
	 * 
	 * Suba um fase e atualize o placar
	 */
	private void avancarDeFase() {
		avancarDeFase = true;
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().proximaFase();
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().setTempoFimJogada(Instant.now());	      
		Instant tempoInicioJogada = campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getTempoInicioJogada();
		Instant tempoFimJogada = campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getTempoFimJogada();
		Long tempoDeJogada = Duration.between(tempoInicioJogada, tempoFimJogada).getSeconds();
		tempoDeJogada = tempoDeJogada + instantePrePausa;
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getTempoDaJogada().add(tempoDeJogada);
		instantePrePausa = (long) 0;
	}

	/**
	 * Inicia o proximo fase
	 */
	private void iniciarProximaFase() {
		avancarDeFase = false;
		sequenciaAtual = null;
		campeonato.getJogadores().get(indexJogadorAtual).getPlacar().setTempoInicioJogada(Instant.now());
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
		jogoTerminado = bool;
		for (int i = 0; i < NUM_QUADRADOS; i++)
			cores[i].setJogoTerminado(bool);
	}

	/**
	 *  Mudar o piscar das cores
	 * @param bool  true se os quadrados devem estar piscando, do contrario, false
	 */
	private void triggerTodasPiscando(boolean bool) {
		for (int i = 0; i< NUM_QUADRADOS; i++)
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
	private class botaoPausarListener implements ActionListener {
		private botaoPausarListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!jogoRodando) {
				botaoPause.setText("PAUSAR");
			} else {
				campeonato.getJogadores().get(indexJogadorAtual).getPlacar().setTempoFimJogada(Instant.now());
				Instant momentoPause = campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getTempoFimJogada();
				Instant antesPause = campeonato.getJogadores().get(indexJogadorAtual).getPlacar().getTempoInicioJogada();
				instantePrePausa = Duration.between(antesPause, momentoPause).getSeconds();
				botaoPause.setText("CONTINUAR");	
			}
			jogoRodando = !jogoRodando;
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
						campeonato.getJogadores().get(i).setNome(nomeJogador);
					}else {
						campeonato.getJogadores().get(i).setNome("Player " + display);
					}

					String apelidoJogador = apelidosJogadores.get(i).getText();
					System.out.println("apelido jogador " + apelidoJogador);//test
					if(apelidoJogador != null && !apelidoJogador.isEmpty()) {
						campeonato.getJogadores().get(i).setApelido(apelidoJogador);
					}else {
						campeonato.getJogadores().get(i).setApelido(apelidoJogador);
					}					
				}
				campeonato.setJogadores(campeonato.getJogadores());		
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
			} else if (indexJogadorAtual + 1 < campeonato.getJogadores().size()) {
				jogadorErrou = true;
				indexJogadorAtual++;
				cliques = 0;
				iniciarJogada();
			} else {

				boolean empate = false;
				if(campeonato.getJogadores().get(0).getPlacar().getPontuacao() 
						== campeonato.getJogadores().get(1).getPlacar().getPontuacao()) {
					empate = true;
				}

				if(empate) {
					int input = JOptionPane.showConfirmDialog(null, 
							"Continuar jogo em fase extra.", "Ocorreu um empate!!!", JOptionPane.DEFAULT_OPTION);
					System.out.println(input);
					if(input == 0) {
						//iniciarJogada();
						indexJogadorAtual = 0;
						reiniciarJogadaParaDesempate();
					}
				} else {
					jogoTerminado = true;

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							criarRelatorioFinal();
						}
					});
				}

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
		for (int i = 0; i < NUM_QUADRADOS; i++) {
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


}
