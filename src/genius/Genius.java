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
	 * Por convenção, todo membro da classe vai começar com "_"
	 */
	private Campeonato _campeonatoAtual;
	private Integer tamanhoLista;//teste hardcoded
	private List<JTextField> nomesJogadores = new ArrayList<JTextField>();
	private List<JTextField> apelidosJogadores = new ArrayList<JTextField>();
	private SequenciaDeCores _sequenciaAtual;
	private JButton _botaoIniciar;
	private JButton _botaoPausar;
	private JButton botaoInserirDados;
	private JComboBox<String> _comboBoxDificuldade;
	private JComboBox<String> _comboBoxVelocidade;
	private Integer _moduloVelocidade;
	private QuadradosCores[] _botoesCores;
	private Timer temporizador;
	private Long _instantePrePausa = (long) 0;
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
	private boolean _jogoRodando = false;
	private boolean _jogoTerminado = false;
	private boolean _jogadorErrou = false;
	private boolean _avancarFase = false;
	private boolean _mostrarSequencia = false;
	private int _toques = 0;
	private int _indiceDePadroesDoJogador; // TODO ver utilidade
	private int _indiceDePadroesDoJogo;
	private int _indexJogadorAtual = 0;


	// Rodar jogo
	public static void main(String[] args) throws InterruptedException{
		Genius genius = new Genius();
	}


	/**
	 * Construtor cria o frame a desenha os graficos do jogo
	 */
	public Genius() {
		criarFrame();
		criarBotaoPrincipal();
		criarBotaoPause();
		_botoesCores = new QuadradosCores[NUM_QUADRADOS];
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
		inicializarQuadradosDeCores();
		repaint();
	}

	private void criarEntradaDeDadosInicial() {
		Integer[] options = {1, 2, 3, 4, 5, 6, 7, 8};
		tamanhoLista = (Integer)JOptionPane.showInputDialog(null, "Escolha o número de jogadores:", 
				"Jogadores", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (tamanhoLista == null) return;
		
		_campeonatoAtual = new Campeonato();
		JFrame frame = new JFrame("Entrada inicial de dados");
		JLabel labelCampeonato = new JLabel();
		JTextField nomeCampeonato = new JTextField(15);
		frame.setSize(400, 400);
		frame.getContentPane().setBackground(COR_FUNDO);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		labelCampeonato.setForeground(Color.WHITE);
		labelCampeonato.setText("Nome do campeonato:");
		frame.add(labelCampeonato);
		frame.add(nomeCampeonato);

		JLabel labelNumeroJogadores = new JLabel();
		labelNumeroJogadores.setForeground(Color.WHITE);

		JLabel labelDificuldade = new JLabel("Escolha a dificuldade:");
		labelDificuldade.setForeground(Color.WHITE);
		String [] dificuldades = {"fácil","médio","difícil"};        
		_comboBoxDificuldade = new JComboBox<String>(dificuldades);
		_comboBoxDificuldade.setBounds(50, 50, 90, 20);
		frame.add(labelDificuldade);
		frame.add(_comboBoxDificuldade);
		
		JLabel labelVelocidade = new JLabel("Escolha a velocidade:");
		labelVelocidade.setForeground(Color.WHITE);
		String [] velocidades = {"normal","lento","rápido"};        
		_comboBoxVelocidade = new JComboBox<String>(velocidades);
		_comboBoxVelocidade.setBounds(50, 50, 90, 20);
		frame.add(labelVelocidade);
		frame.add(_comboBoxVelocidade);

		for(int i = 0; i < tamanhoLista; i++) {
			Jogador jogador = new Jogador("Jogador " + (i + 1), "J" + ((i + 1)));
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

			_campeonatoAtual.adicionaJogador(jogador);
			nomesJogadores.add(fieldNome);
			apelidosJogadores.add(fieldApelido);
		}

		criarBotaoInserirDados(frame, nomeCampeonato);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.setVisible(true);
	}

	private void criarRelatorioFinal() {

		Long jogadaMaisRapida = Long.MAX_VALUE;

		Long [] listaJogadaMaisRapida = new Long[tamanhoLista];
		Long [] somaTotalTempo = new Long[tamanhoLista];

		for(int i = 0; i < tamanhoLista; i++) {
			somaTotalTempo[i] = (long) 0;;
			for(int j = 0; j < _campeonatoAtual.getJogador(i).getPlacar().getTempoDaJogada().size(); j++) {
				long atual = _campeonatoAtual.getJogador(i).getPlacar().getTempoDaJogada().get(j);
				somaTotalTempo[i] += atual;
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
				"Nome", "Apelido", "Pontuacao", "Mais rapida (s)", "Tempo total"
		};

		Object[][] dados = new Object[tamanhoLista][5];

		for(int i = 0; i < tamanhoLista; i++) {
			for(int j = 0; j < 5; j++) {
				if(j == 0)
					dados[i][j] = _campeonatoAtual.getJogador(i).getNome();
				if(j == 1)
					dados[i][j] = _campeonatoAtual.getJogador(i).getApelido();
				if(j == 2)
					dados[i][j] = _campeonatoAtual.getJogador(i).getPlacar().getPontuacao();
				if(j == 3)
					dados[i][j] = listaJogadaMaisRapida[i];
				if(j == 4)
					dados[i][j] = somaTotalTempo[i];
			}
		}

		JTable tabela = new JTable(dados, colunas);
		tabela.setGridColor(Color.BLACK);
		tabela.setBackground(Color.WHITE);
		tabela.getAutoResizeMode();
		JFrame frame = new JFrame("Resultados do " + _campeonatoAtual.getNome() + " - " + java.time.LocalDate.now());
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
	}

	/**
	 * Cria o botao principal
	 */
	private void criarBotaoPrincipal() {
		_botaoIniciar = new JButton("JOGAR");
		_botaoIniciar.setBackground(Color.GREEN);
		_botaoIniciar.setForeground(Color.BLACK);
		_botaoIniciar.setFocusPainted(false);
		_botaoIniciar.setFont(new Font("Comic", Font.BOLD, 10));
		int offset_x = 250;
		int offset_y = 292;
		_botaoIniciar.setBounds(offset_x, offset_y, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		_botaoIniciar.addActionListener(new botaoIniciarListener());
		setLayout(null);
		add(_botaoIniciar);
	}

	/**
	 * Cria o botao pause
	 */
	private void criarBotaoPause() {
		_botaoPausar = new JButton("PAUSAR");
		_botaoPausar.setBackground(Color.GREEN);
		_botaoPausar.setForeground(Color.BLACK);
		_botaoPausar.setFocusPainted(false);
		_botaoPausar.setFont(new Font("Comic", Font.BOLD, 10));
		int offset_x = 250;
		int offset_y = 292;
		_botaoPausar.setBounds(offset_x, offset_y, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		_botaoPausar.addActionListener(new botaoPausarListener());
		setLayout(null);
		add(_botaoPausar);
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
		_botoesCores[0] = new QuadradosCores(Color.GREEN, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 105);
		_botoesCores[1] = new QuadradosCores(Color.RED, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 105);
		_botoesCores[2] = new QuadradosCores(Color.YELLOW, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 320);
		_botoesCores[3] = new QuadradosCores(Color.BLUE, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 320);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//if (genius != null) {
			paint((Graphics2D) g);
		//}
	}

	/**
	 * Paint the game's graphics
	 * @param forma2D     2D graphics context
	 */
	private void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(COR_FUNDO);
		g.fillRect(0, 0, LARGURA, ALTURA);

		// Desenhar quadrados de _botoesCores
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			_botoesCores[i].desenharColorirQuadrado(g);
		}

		// Desenhar placar:
		g.setColor(Color.WHITE);
		if (_jogoRodando) {
			g.setFont(new Font("Comic", Font.BOLD, 14));
			g.drawString("Jogador: " + _campeonatoAtual.getJogador(_indexJogadorAtual).getApelido(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET - 20);
			g.drawString("Fase:  " + _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getFase(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET);
			g.drawString("Pontos totais: " + _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getPontuacao()
					+ " (+" + _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().ultimaPontuacaoAcrescentada() + ")",
					(LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET + 20);
		}
		// Texto quando quaisquer jogador errar
		if (_jogoTerminado || _jogadorErrou) {
			g.setFont(new Font("Comic", Font.BOLD, 20));
			g.setColor(Color.RED);
			int indexJogadorErrou = _indexJogadorAtual - 1;
			// se _indexJogadorAtual for 0 então significa que o ultimo jogador errou, nesse caso
			// usamos o tamanho da lista de jogadores `_campeonatoAtual.getJogadores()` para pegar o index do ultimo jogador
			if(_indexJogadorAtual == 0) {
				indexJogadorErrou = _campeonatoAtual.getQuantidadeJogadores() - 1;
			}
			
			g.drawString(_campeonatoAtual.getJogador(indexJogadorErrou).getNome() + " errou!!!", (LARGURA/2) - 80,  550);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!_jogoRodando)
			return;

		if (_jogoTerminado) {
			finalizaJogo();
			repaint();
			return;
		}

		_toques++;

		if (_mostrarSequencia) {
			if (_toques % _moduloVelocidade == 0) {
				avanceSequencia();
				_toques = 0;
			}
			else if (_toques % 20 == 0) {
				triggerTodasPiscando(false);
			}
		}

		else if (_avancarFase) {
			triggerTodasPiscando(true);
			if (_toques % 60 == 0) {
				triggerTodasPiscando(false);
				iniciarProximaFase();
				_toques = 0;
			}
		}
		else { // rodada do jogador
			if (_toques % 20 == 0) {
				triggerTodasPiscando(false);
				_toques = 0;
			}
		}
		repaint();
	}

	/**
	 * Iniciar o jogo pelo temporizador, reinicializando o placar e iniciando uma sequencia
	 */
	private void iniciarJogada() {
		if (_comboBoxVelocidade.getSelectedIndex() == 0) {
			_moduloVelocidade = 50;
		} else if(_comboBoxVelocidade.getSelectedIndex() == 1) {
			_moduloVelocidade = 70;
		} else {
			_moduloVelocidade = 30;
		}
		temporizador.start();
		triggerTodasPiscando(false);
		_jogoRodando = true;
		_jogoTerminado = false;
		_botaoPausar.setVisible(_jogoRodando);
		_botaoIniciar.setVisible(_jogoTerminado);
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().setTempoInicioJogada(Instant.now());
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().proximaFase();
		iniciarUmaSequencia();
	}

	/**
	 * Continuar o jogo pelo temporizador e iniciando uma sequencia para desempate
	 */
	private void reiniciarJogadaParaDesempate() {
		// TODO considerar necessidade dessa função
		temporizador.start();
		triggerTodasPiscando(false);
		_jogoRodando = true;
		_jogoTerminado = false;
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().setTempoInicioJogada(Instant.now());
		iniciarUmaSequencia();
	}

	/**
	 * Iniciar um sequencia com o numero piscadas equivalente a fase do jogador
	 */
	private void iniciarUmaSequencia() {
		int numeroSequencias = _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getFase();
		if (_comboBoxDificuldade.getSelectedIndex() == 1) {
			numeroSequencias += 2;
		} else if (_comboBoxDificuldade.getSelectedIndex() == 2) {
			numeroSequencias += 4;
		}
		_sequenciaAtual = new SequenciaDeCores(numeroSequencias);
		_indiceDePadroesDoJogo = 0;
		_indiceDePadroesDoJogador = 0;
		_mostrarSequencia = true;
	}

	/**
	 * Avance para o proximo elemento da sequencia
	 */
	private void avanceSequencia() {
		if (_indiceDePadroesDoJogo >= _sequenciaAtual.getQuantidade()) {
			_mostrarSequencia = false;
			return;
		}
		_botoesCores[_sequenciaAtual.getIndice(_indiceDePadroesDoJogo)].setPiscada(true);
		arraySonoro[_sequenciaAtual.getIndice(_indiceDePadroesDoJogo)].play();
		_indiceDePadroesDoJogo++;
	}

	/**
	 * 
	 * Suba um fase e atualize o placar
	 */
	private void _avancarFase() {
		_avancarFase = true;
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().proximaFase();
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().setTempoFimJogada(Instant.now());
		Instant tempoInicioJogada = _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getTempoInicioJogada();
		Instant tempoFimJogada = _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getTempoFimJogada();
		Long tempoDeJogada = Duration.between(tempoInicioJogada, tempoFimJogada).getSeconds();
		tempoDeJogada = tempoDeJogada + _instantePrePausa;
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getTempoDaJogada().add(tempoDeJogada);
	}

	/**
	 * Inicia o proximo fase
	 */
	private void iniciarProximaFase() {
		_avancarFase = false;
		_sequenciaAtual = null;
		_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().setTempoInicioJogada(Instant.now());
		iniciarUmaSequencia();
	}

	/**
	 * jogo terminado, parar temporizador, mas manter placar
	 * 
	 */
	private void finalizaJogo() {
		alertaJogoTerminado(true);
		_jogoRodando = false;
		_jogoTerminado = true;
		_jogadorErrou = false;
		_botaoIniciar.setVisible(_jogoTerminado);
		_indexJogadorAtual = 0;
		temporizador.stop();
	}

	/**
	 * Alert que todos os quadrados acarca de uma mudanca no estado do jogo
	 * @param bool true se o jogo tiver terminado ou falso, caso contrario
	 */
	private void alertaJogoTerminado(boolean bool) {
		_jogoTerminado = bool;
		for (int i = 0; i < NUM_QUADRADOS; i++)
			_botoesCores[i].setJogoTerminado(bool);
	}

	/**
	 *  Mudar o piscar dos botoes de cores
	 * @param bool  true se os quadrados devem estar piscando, do contrario, false
	 */
	private void triggerTodasPiscando(boolean bool) {
		for (int i = 0; i< NUM_QUADRADOS; i++)
			_botoesCores[i].setPiscada(bool);
	}

	/**
	 * Listener class to the Main button
	 */
	private class botaoIniciarListener implements ActionListener {
		private botaoIniciarListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!_jogoRodando) {
				alertaJogoTerminado(false);
				criarEntradaDeDadosInicial();
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
			if (!_jogoRodando) {
				// TODO retomar o tempo pausado
				_botaoPausar.setText("PAUSAR");
			} else {
				// TODO mover isso para o jogador
				//_instantePrePausa = (long) 0;
				_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().setTempoFimJogada(Instant.now());
				Instant momentoPause = _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getTempoFimJogada();
				Instant antesPause = _campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().getTempoInicioJogada();
				_instantePrePausa = Duration.between(antesPause, momentoPause).getSeconds();
				_botaoPausar.setText("CONTINUAR");	
			}
			_jogoRodando = !_jogoRodando;
		}

	}

	/**
	 * Listener class to the Main button
	 */
	private class botaoInserirDadosListener implements ActionListener {

		JTextField _nomeCampeonato;
		JFrame _frameInserirDados;

		private botaoInserirDadosListener(JTextField nomeCampeonato, JFrame frame) {
			_nomeCampeonato = nomeCampeonato;
			_frameInserirDados = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!_jogoRodando) {

				String nomeCampeonatoStr = _nomeCampeonato.getText();
				System.out.println("nome campeonato: " + nomeCampeonatoStr);//test

				if(nomeCampeonatoStr != null && !nomeCampeonatoStr.isEmpty()) {
					_campeonatoAtual.setNome(nomeCampeonatoStr);		
				}else {
					_campeonatoAtual.setNome("Desafio Genius");
				}

				for(int i = 0; i < tamanhoLista; i++) {

					String nomeJogador = nomesJogadores.get(i).getText();
					System.out.println("nome jogador " + nomeJogador);
					if(nomeJogador != null && !nomeJogador.isEmpty()) {
						_campeonatoAtual.getJogador(i).setNome(nomeJogador);
					}
					
					String apelidoJogador = apelidosJogadores.get(i).getText();
					System.out.println("apelido jogador " + apelidoJogador);//test
					if(apelidoJogador != null && !apelidoJogador.isEmpty()) {
						_campeonatoAtual.getJogador(i).setApelido(apelidoJogador);
					}				
				}
				_campeonatoAtual.setJogadores(_campeonatoAtual.getJogadores());		
				alertaJogoTerminado(false);
				_frameInserirDados.setVisible(false);
				// TODO criar funcao iniciar campeonato, que vai popular os dados
				iniciarJogada();
			}
		}
	}

	// Mouse Listeners:
	@Override
	public void mousePressed(MouseEvent e) {
		if (_jogoRodando && !_mostrarSequencia && !_avancarFase) {
			int indiceDaCorClicada = coordenadasQuadrado(e.getX(), e.getY());
			if (indiceDaCorClicada == -1) {
				return;
			}
			_botoesCores[indiceDaCorClicada].setPiscada(true);
			repaint();
			_toques = 0;
			if (indiceDaCorClicada == _sequenciaAtual.getIndice(_indiceDePadroesDoJogador)) {
				_jogadorErrou = false;
				_campeonatoAtual.getJogador(_indexJogadorAtual).getPlacar().aumentarPontuacao();
				_indiceDePadroesDoJogador++;
				if (_indiceDePadroesDoJogador >= _sequenciaAtual.getQuantidade()) {
					_avancarFase();
				}
			} else if (_indexJogadorAtual + 1 < _campeonatoAtual.getQuantidadeJogadores()) {
				_jogadorErrou = true;
				_indexJogadorAtual++;
				_toques = 0;
				iniciarJogada();
			} else {
				if (_campeonatoAtual.checaEmpate()) {
					_indexJogadorAtual = 0;
					JOptionPane.showConfirmDialog(null, 
							"Continuar jogo em fase extra.", "Ocorreu um empate!!!", JOptionPane.DEFAULT_OPTION);
					reiniciarJogadaParaDesempate();
				} else {
					_jogoTerminado = true;

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
			if (_botoesCores[i].estaDentroDasCoordenadas(x, y)) {
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
