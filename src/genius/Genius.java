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

	private Campeonato campeonatoAtual;
	private Integer tamanhoLista;
	private List<JTextField> nomesJogadores = new ArrayList<JTextField>();
	private List<JTextField> apelidosJogadores = new ArrayList<JTextField>();
	private SequenciaDeCores sequenciaAtual;
	private JButton botaoPrincipal;
	private JButton botaoInserirDados;
	private JComboBox<String> comboBoxDificuldade;
	private JComboBox<String> comboBoxVelocidade;
	private Integer moduloVelocidade;
	private QuadradosCores[] botoesCores;
	private Timer temporizador;
	private Sons som = new Sons();

	private AudioClip[] arraySonoro = {som.getAudioVerde(),som.getAudioVermelho(),som.getAudioAmarelo(),som.getAudioAzul()};


	// CONSTANTES:
	private static final String NOME = "Genius - Projeto POO!";
	private static final int LARGURA = 600;
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
	private boolean avancarFase = false;
	private boolean mostrarSequencia = false;
	private int toques = 0;
	private int indiceDePadroesDoJogador;
	private int indiceDePadroesDoJogo;
	private int indexJogadorAtual = 0;


	// Rodar jogo
	public static void main(String[] args) throws InterruptedException{
		@SuppressWarnings("unused")
		Genius genius = new Genius();
	}


	/**
	 * Construtor cria o frame a desenha os graficos do jogo
	 */
	public Genius() {
		criarFrame();
		criarBotaoPrincipal();
		botoesCores = new QuadradosCores[NUM_QUADRADOS];
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
		inicializarQuadradosDeCores();
		repaint();
	}

	private void criarEntradaDeDadosInicial() {
		Integer[] options = {1, 2, 3, 4, 5, 6, 7, 8};
		tamanhoLista = (Integer)JOptionPane.showInputDialog(null, "Escolha o número de jogadores:", 
				"Jogadores", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (tamanhoLista == null) return;
		
		campeonatoAtual = new Campeonato();
		JFrame frame = new JFrame("Entrada inicial de dados");
		frame.getContentPane().setBackground(COR_FUNDO);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));
		painel.setBackground(COR_FUNDO);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

		JLabel labelCampeonato = new JLabel("Nome do campeonato");
		labelCampeonato.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelCampeonato.setForeground(Color.WHITE);
		painel.add(labelCampeonato);
		
		JTextField nomeCampeonato = new JTextField(15);
		painel.add(nomeCampeonato);

		JLabel labelDificuldade = new JLabel("Escolha a dificuldade");
		labelDificuldade.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelDificuldade.setForeground(Color.WHITE);
		painel.add(labelDificuldade);

		String [] dificuldades = {"fácil","médio","difícil"};
		comboBoxDificuldade = new JComboBox<String>(dificuldades);
		comboBoxDificuldade.setBounds(50, 50, 90, 20);
		painel.add(comboBoxDificuldade);

		JLabel labelVelocidade = new JLabel("Escolha a velocidade");
		labelVelocidade.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelVelocidade.setForeground(Color.WHITE);
		painel.add(labelVelocidade);

		String [] velocidades = {"normal", "lento", "rápido"};
		comboBoxVelocidade = new JComboBox<String>(velocidades);
		comboBoxVelocidade.setBounds(50, 50, 90, 20);
		painel.add(comboBoxVelocidade);

		for (int i = 1; i <= tamanhoLista; i++) {
			JLabel labelNome = new JLabel("Nome do jogador " + i);
			labelNome.setForeground(Color.WHITE);
			painel.add(labelNome);
			
			JTextField fieldNome = new JTextField(15);
			painel.add(fieldNome);
			nomesJogadores.add(fieldNome);
			
			JLabel labelApelido = new JLabel("Apelido do jogador " + i);
			labelApelido.setAlignmentX(Component.LEFT_ALIGNMENT);
			labelApelido.setForeground(Color.WHITE);
			painel.add(labelApelido);
			
			JTextField fieldApelido = new JTextField(15);
			painel.add(fieldApelido);
			apelidosJogadores.add(fieldApelido);

			Jogador jogador = new Jogador("Jogador " + i, "J" + i);
			campeonatoAtual.adicionaJogador(jogador);
		}

		painel.add(Box.createVerticalGlue());
		JScrollPane scrollPane = new JScrollPane(painel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(scrollPane);
        
        criarBotaoInserirDados(frame, nomeCampeonato);
        frame.pack();
		frame.setVisible(true);
	}

	private void criarRelatorioFinal() {
		Long jogadaMaisRapida = Long.MAX_VALUE;
		Long [] listaJogadaMaisRapida = new Long[tamanhoLista];
		Long [] somaTotalTempo = new Long[tamanhoLista];

		for (int i = 0; i < tamanhoLista; i++) {
			somaTotalTempo[i] = 0L;
			for (int j = 0; j < campeonatoAtual.getJogador(i).getTempoJogadas().size(); j++) {
				long atual = campeonatoAtual.getJogador(i).getTempoJogada(j);
				somaTotalTempo[i] += atual;
				if (atual < jogadaMaisRapida) {
					jogadaMaisRapida = atual;
					listaJogadaMaisRapida[i] = jogadaMaisRapida;
					if (listaJogadaMaisRapida[i] == Long.MAX_VALUE) {
						listaJogadaMaisRapida[i] = 99L;
					}
				}
			}
		}

		String[] colunas = new String[] {
				"Nome", "Apelido", "Pontuacao", "Mais rapida (s)", "Tempo total"
		};
		Object[][] dados = new Object[tamanhoLista][5];

		for (int i = 0; i < tamanhoLista; i++) {
			dados[i][0] = campeonatoAtual.getJogador(i).getNome();
			dados[i][1] = campeonatoAtual.getJogador(i).getApelido();
			dados[i][2] = campeonatoAtual.getJogador(i).getPontuacao();
			dados[i][3] = listaJogadaMaisRapida[i];
			dados[i][4] = somaTotalTempo[i];
		}

		JTable tabela = new JTable(dados, colunas);
		tabela.setDefaultEditor(Object.class, null);
		tabela.setShowGrid(false);
		tabela.setForeground(Color.WHITE);
		tabela.setFillsViewportHeight(true);
		tabela.setBackground(COR_FUNDO);
		tabela.getTableHeader().setBackground(COR_FUNDO);
		tabela.getTableHeader().setForeground(Color.WHITE);
		tabela.getAutoResizeMode();
		JFrame frame = new JFrame("Resultados do " + campeonatoAtual.getNome() + " - " + java.time.LocalDate.now());
		frame.setSize(220, 50);
		frame.getContentPane().setBackground(COR_FUNDO);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JScrollPane jspane = new JScrollPane(tabela);
		jspane.setSize(220, 50);
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
	}

	/**
	 * Cria o botao principal
	 */
	private void criarBotaoPrincipal() {
		botaoPrincipal = new JButton("JOGAR");
		botaoPrincipal.setBackground(Color.GREEN);
		botaoPrincipal.setForeground(Color.BLACK);
		botaoPrincipal.setFocusPainted(false);
		botaoPrincipal.setFont(new Font("Comic", Font.BOLD, 10));
		int offset_x = 250;
		int offset_y = 292;
		botaoPrincipal.setBounds(offset_x, offset_y, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botaoPrincipal.addActionListener(new botaoPrincipalListener());
		setLayout(null);
		add(botaoPrincipal);
	}

	/**
	 * Cria o botao para pop up de inserir dados
	 * @param frame 
	 * @param nomeCampeonato 
	 */
	private void criarBotaoInserirDados(JFrame frame, JTextField nomeCampeonato) {
		botaoInserirDados = new JButton("INICIAR");
		botaoInserirDados.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		botaoInserirDados.setBackground(Color.GREEN);
		botaoInserirDados.setForeground(Color.BLACK);
		botaoInserirDados.setFocusPainted(false);
		botaoInserirDados.setFont(new Font("Comic", Font.BOLD, 10));
		botaoInserirDados.addActionListener(new botaoInserirDadosListener(nomeCampeonato, frame));
		frame.add(botaoInserirDados);
	}

	/**
	 *Inicializa os quadrados 
	 */
	private void inicializarQuadradosDeCores() {
		botoesCores[0] = new QuadradosCores(Color.GREEN, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 105);
		botoesCores[1] = new QuadradosCores(Color.RED, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 105);
		botoesCores[2] = new QuadradosCores(Color.YELLOW, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 320);
		botoesCores[3] = new QuadradosCores(Color.BLUE, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 320);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
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

		// Desenhar quadrados de botoesCores
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			botoesCores[i].desenharColorirQuadrado(g);
		}

		// Desenhar placar:
		g.setColor(Color.WHITE);
		if (jogoRodando) {
			g.setFont(new Font("Comic", Font.BOLD, 14));
			g.drawString("Jogador: " + campeonatoAtual.getJogador(indexJogadorAtual).getApelido(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET - 20);
			g.drawString("Fase:  " + campeonatoAtual.getJogador(indexJogadorAtual).getFaseAtual(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET);
			g.drawString("Pontos totais: " + campeonatoAtual.getJogador(indexJogadorAtual).getPontuacao()
					+ " (+" + campeonatoAtual.getJogador(indexJogadorAtual).ultimaPontuacaoAcrescentada() + ")",
					(LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET + 20);
		}
		// Texto quando quaisquer jogador errar
		if (jogoTerminado || jogadorErrou) {
			g.setFont(new Font("Comic", Font.BOLD, 20));
			g.setColor(Color.RED);
			int indexJogadorErrou = indexJogadorAtual - 1;
			// se indexJogadorAtual for 0 então significa que o ultimo jogador errou, nesse caso
			// usamos o tamanho da lista de jogadores `campeonatoAtual.getJogadores()` para pegar o index do ultimo jogador
			if (indexJogadorAtual == 0) {
				indexJogadorErrou = campeonatoAtual.getQuantidadeJogadores() - 1;
			}

			g.drawString(campeonatoAtual.getJogador(indexJogadorErrou).getNome() + " errou!!!", (LARGURA/2) - 80,  550);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jogoRodando)
			return;

		if (jogoTerminado) {
			finalizaCampeonato();
			repaint();
			return;
		}

		toques++;

		if (mostrarSequencia) {
			if (toques % moduloVelocidade == 0) {
				avanceSequencia();
				toques = 0;
			}
			else if (toques % 20 == 0) {
				triggerTodasPiscando(false);
			}
		}

		else if (avancarFase) {
			triggerTodasPiscando(true);
			if (toques % 60 == 0) {
				triggerTodasPiscando(false);
				iniciarProximaFase();
				toques = 0;
			}
		} else { // rodada do jogador
			if (toques % 20 == 0) {
				triggerTodasPiscando(false);
				toques = 0;
			}
		}
		repaint();
	}

	private void iniciarCampeonato() {
		if (comboBoxVelocidade.getSelectedIndex() == 0) {
			moduloVelocidade = 50;
		} else if (comboBoxVelocidade.getSelectedIndex() == 1) {
			moduloVelocidade = 70;
		} else {
			moduloVelocidade = 30;
		}
		temporizador.start();
		botaoPrincipal.setText("PAUSAR");
		jogoRodando = true;
		triggerTodasPiscando(false);
		iniciarJogada();
	}

	/**
	 * Iniciar o jogo pelo temporizador, reinicializando o placar e iniciando uma sequencia
	 */
	private void iniciarJogada() {
		campeonatoAtual.getJogador(indexJogadorAtual).avancaFase();
		iniciarUmaSequencia();
	}

	/**
	 * Iniciar um sequencia com o numero piscadas equivalente a fase do jogador
	 */
	private void iniciarUmaSequencia() {
		int numeroSequencias = campeonatoAtual.getJogador(indexJogadorAtual).getFaseAtual();
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
		if (indiceDePadroesDoJogo >= sequenciaAtual.getQuantidade()) {
			mostrarSequencia = false;
			return;
		}
		botoesCores[sequenciaAtual.getIndice(indiceDePadroesDoJogo)].setPiscada(true);
		arraySonoro[sequenciaAtual.getIndice(indiceDePadroesDoJogo)].play();
		indiceDePadroesDoJogo++;
	}

	/**
	 * 
	 * Suba um fase e atualize o placar
	 */
	private void avancarFase() {
		avancarFase = true;
		campeonatoAtual.getJogador(indexJogadorAtual).avancaFase();
	}

	/**
	 * Inicia o proximo fase
	 */
	private void iniciarProximaFase() {
		avancarFase = false;
		sequenciaAtual = null;
		iniciarUmaSequencia();
	}

	/**
	 * jogo terminado, parar temporizador, mas manter placar
	 * 
	 */
	private void finalizaCampeonato() {
		alertaJogoTerminado(true);
		jogoRodando = false;
		jogoTerminado = true;
		jogadorErrou = false;
		botaoPrincipal.setText("JOGAR");
		indexJogadorAtual = 0;
		temporizador.stop();
	}

	/**
	 * Alert que todos os quadrados acarca de uma mudanca no estado do jogo
	 * @param bool true se o jogo tiver terminado ou falso, caso contrario
	 */
	private void alertaJogoTerminado(boolean bool) {
		jogoTerminado = bool;
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			botoesCores[i].setJogoTerminado(bool);
		}
	}

	/**
	 *  Mudar o piscar dos botoes de cores
	 * @param bool  true se os quadrados devem estar piscando, do contrario, false
	 */
	private void triggerTodasPiscando(boolean bool) {
		for (int i = 0; i< NUM_QUADRADOS; i++) {
			botoesCores[i].setPiscada(bool);
		}
	}

	/**
	 * Listener class to the Main button
	 */
	private class botaoPrincipalListener implements ActionListener {
		private botaoPrincipalListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (botaoPrincipal.getText() == "JOGAR") {
				alertaJogoTerminado(false);
				criarEntradaDeDadosInicial();
			} else {
				if (!jogoRodando) {
					campeonatoAtual.getJogador(indexJogadorAtual).retomaJogada();
					botaoPrincipal.setText("PAUSAR");
				} else {
					campeonatoAtual.getJogador(indexJogadorAtual).pausaJogada();
					botaoPrincipal.setText("CONTINUAR");	
				}
				jogoRodando = !jogoRodando;
			}
		}
	}

	/**
	 * Listener class to the Main button
	 */
	private class botaoInserirDadosListener implements ActionListener {

		JTextField nomeCampeonato;
		JFrame frameInserirDados;

		private botaoInserirDadosListener(JTextField nomeCampeonato, JFrame frame) {
			this.nomeCampeonato = nomeCampeonato;
			frameInserirDados = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!jogoRodando) {
				String nomeCampeonatoStr = nomeCampeonato.getText();
				System.out.println("nome campeonato: " + nomeCampeonatoStr);

				if (nomeCampeonatoStr != null && !nomeCampeonatoStr.isEmpty()) {
					campeonatoAtual.setNome(nomeCampeonatoStr);		
				} else {
					campeonatoAtual.setNome("Desafio Genius");
				}

				for (int i = 0; i < tamanhoLista; i++) {
					String nomeJogador = nomesJogadores.get(i).getText();
					System.out.println("nome jogador " + nomeJogador);
					if (nomeJogador != null && !nomeJogador.isEmpty()) {
						campeonatoAtual.getJogador(i).setNome(nomeJogador);
					}

					String apelidoJogador = apelidosJogadores.get(i).getText();
					System.out.println("apelido jogador " + apelidoJogador);
					if (apelidoJogador != null && !apelidoJogador.isEmpty()) {
						campeonatoAtual.getJogador(i).setApelido(apelidoJogador);
					}				
				}		
				alertaJogoTerminado(false);
				frameInserirDados.setVisible(false);
				iniciarCampeonato();
			}
		}
	}

	// Mouse Listeners:
	@Override
	public void mousePressed(MouseEvent e) {
		if (jogoRodando && !mostrarSequencia && !avancarFase) {
			int indiceDaCorClicada = coordenadasQuadrado(e.getX(), e.getY());
			if (indiceDaCorClicada == -1) {
				return;
			}
			botoesCores[indiceDaCorClicada].setPiscada(true);
			repaint();
			toques = 0;
			if (indiceDaCorClicada == sequenciaAtual.getIndice(indiceDePadroesDoJogador)) {
				jogadorErrou = false;
				campeonatoAtual.getJogador(indexJogadorAtual).incrementaPontuacao();
				indiceDePadroesDoJogador++;
				if (indiceDePadroesDoJogador >= sequenciaAtual.getQuantidade()) {
					avancarFase();
				}
			} else if (indexJogadorAtual + 1 < campeonatoAtual.getQuantidadeJogadores()) {
				jogadorErrou = true;
				indexJogadorAtual++;
				toques = 0;
				// Se o jogador Atual ja tiver passado uma ou mais fases, iniciamos somente uma sequencia
				// com a quantidade equivalente a fase, caso for a primeira vez que o jogador estiver jogando
				// iniciamos a jogada dele
				if (campeonatoAtual.getJogador(indexJogadorAtual).getFaseAtual() > 0) {
					iniciarUmaSequencia();
				} else {
					iniciarJogada();
				}
			} else {
				if (campeonatoAtual.checaEmpate()) {
					indexJogadorAtual = 0;
					JOptionPane.showConfirmDialog(null, 
							"Continuar jogo em fase extra.", "Ocorreu um empate!!!", JOptionPane.DEFAULT_OPTION);
					// recomeçando as sequencias
					iniciarUmaSequencia();
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
			if (botoesCores[i].estaDentroDasCoordenadas(x, y)) {
				return i;
			}
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