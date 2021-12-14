package genius;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Genius extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 3221282515071077635L;
	private Campeonato campeonatoAtual;
	private List<JTextField> nomesJogadores = new ArrayList<JTextField>();
	private List<JTextField> apelidosJogadores = new ArrayList<JTextField>();
	private SequenciaDeCores sequenciaAtual;
	private JButton botaoPrincipal;
	private JButton botaoInserirDados;
	private JButton botaoSalvarCarregar;
	private JButton botaoAjuda;
	private JComboBox<String> comboBoxDificuldade;
	private JComboBox<String> comboBoxVelocidade;
	private Integer moduloVelocidade;
	private QuadradosCores[] botoesCores;
	private Timer temporizador;
	private Sons som = new Sons();
	private int offsetFase;
	private int toques = 0;
	private int indiceDePadroesDoJogador;
	private int indiceDePadroesDoJogo;
	private int indexJogadorAtual = 0;

	private AudioClip[] arraySonoro = { som.getAudioVerde(), som.getAudioVermelho(), som.getAudioAmarelo(), som.getAudioAzul() };


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
		botoesCores = new QuadradosCores[NUM_QUADRADOS];
		temporizador = new Timer(TEMPORIZADOR_DELAY, this);
		inicializarQuadradosDeCores();
		criarBotaoPrincipal();
		criarBotaoSalvarCarregarCampeonato();
		criarBotaoAjuda();
		repaint();
	}

	private void criarEntradaDeDadosInicial() {
		Integer[] options = {1, 2, 3, 4, 5, 6, 7, 8};
		Integer tamanhoLista = (Integer) JOptionPane.showInputDialog(null, "Escolha o número de jogadores:", 
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
		Long [] listaJogadaMaisRapida = new Long[campeonatoAtual.getQuantidadeJogadores()];
		Long [] somaTotalTempo = new Long[campeonatoAtual.getQuantidadeJogadores()];

		for (int i = 0; i < campeonatoAtual.getQuantidadeJogadores(); i++) {
			Long jogadaMaisRapida = Long.MAX_VALUE;
			somaTotalTempo[i] = 0L;
			listaJogadaMaisRapida[i] = 0L;
			for (int j = 0; j < campeonatoAtual.getJogador(i).getTempoJogadas().size(); j++) {
				long atual = campeonatoAtual.getJogador(i).getTempoJogada(j);
				somaTotalTempo[i] += atual;
				if (atual < jogadaMaisRapida) {
					jogadaMaisRapida = atual;
					listaJogadaMaisRapida[i] = jogadaMaisRapida;
					if (listaJogadaMaisRapida[i] == Long.MAX_VALUE) {
						listaJogadaMaisRapida[i] = 0L;
					}
				}
			}
		}

		String[] colunas = new String[] {
				"Nome", "Apelido", "Fase", "Pontuacao", "Mais rapida (s)", "Tempo total"
		};
		Object[][] dados = new Object[campeonatoAtual.getQuantidadeJogadores()][6];

		for (int i = 0; i < campeonatoAtual.getQuantidadeJogadores(); i++) {
			dados[i][0] = campeonatoAtual.getJogador(i).getNome();
			dados[i][1] = campeonatoAtual.getJogador(i).getApelido();
			dados[i][2] = campeonatoAtual.getJogador(i).getFaseAtual();
			dados[i][3] = campeonatoAtual.getJogador(i).getPontuacao();
			dados[i][4] = listaJogadaMaisRapida[i];
			dados[i][5] = somaTotalTempo[i];
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
	 * Criar frame principal
	 */
	private void criarFrame() {
		JFrame frame = new JFrame(NOME);
		frame.setSize(LARGURA, ALTURA);
		frame.getContentPane().setBackground(COR_FUNDO);   
		frame.setVisible(true);
		frame.add(this);
		frame.addMouseListener(this);
		frame.setResizable(false);
		setLayout(null);
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
		add(botaoPrincipal);
	}

	/**
	 * Cria o botao de salvar/carregar campeonato
	 */
	private void criarBotaoSalvarCarregarCampeonato() {
		botaoSalvarCarregar = new JButton("CARREGAR CAMPEONATO");
		botaoSalvarCarregar.setBackground(Color.BLACK);
		botaoSalvarCarregar.setForeground(Color.WHITE);
		botaoSalvarCarregar.setFocusPainted(false);
		botaoSalvarCarregar.setFont(new Font("Comic", Font.BOLD, 10));
		botaoSalvarCarregar.setBounds(11, 10, LARGURA_BOTAO_PRINCIPAL + 90, ALTURA_BOTAO_PRINCIPAL);
		botaoSalvarCarregar.addActionListener(new botaoSalvarCarregarListener());
		add(botaoSalvarCarregar);
	}

	/**
	 * Cria o botao ajuda
	 */
	private void criarBotaoAjuda() {
		botaoAjuda = new JButton("AJUDA");
		botaoAjuda.setBackground(Color.BLACK);
		botaoAjuda.setForeground(Color.WHITE);
		botaoAjuda.setFocusPainted(false);
		botaoAjuda.setFont(new Font("Comic", Font.BOLD, 10));
		botaoAjuda.setBounds(487, 10, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botaoAjuda.addActionListener(new botaoAjudaListener());
		add(botaoAjuda);
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
			g.drawString("Fase:  " + (campeonatoAtual.getJogador(indexJogadorAtual).getFaseAtual() - offsetFase), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET);
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

			g.drawString(campeonatoAtual.getJogador(indexJogadorErrou).getApelido() + " errou!!!", (LARGURA/2) - 80,  550);
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
		offsetFase = 0;
		if (comboBoxDificuldade.getSelectedIndex() == 1) {
			offsetFase = 2;
		} else if (comboBoxDificuldade.getSelectedIndex() == 2) {
			offsetFase = 4;
		}
		temporizador.start();
		botaoPrincipal.setText("PAUSAR");
		botaoSalvarCarregar.setText("SALVAR CAMPEONATO");
		jogoRodando = true;
		triggerTodasPiscando(false);
		iniciarJogada();
	}

	/**
	 * Iniciar o jogo pelo temporizador, reinicializando o placar e iniciando uma sequencia
	 */
	private void iniciarJogada() {
		campeonatoAtual.getJogador(indexJogadorAtual).setFaseAtual(offsetFase);
		campeonatoAtual.getJogador(indexJogadorAtual).avancaFase();
		iniciarUmaSequencia();
	}

	/**
	 * Iniciar um sequencia com o numero piscadas equivalente a fase do jogador
	 */
	private void iniciarUmaSequencia() {
		sequenciaAtual = new SequenciaDeCores(campeonatoAtual.getJogador(indexJogadorAtual).getFaseAtual());
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
		botaoSalvarCarregar.setText("CARREGAR CAMPEONATO");
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
	 * Listener do botão principal
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
	 * Listener do botão de inserir dados iniciais
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

				for (int i = 0; i < campeonatoAtual.getQuantidadeJogadores(); i++) {
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
				nomesJogadores.clear();
				apelidosJogadores.clear();
				alertaJogoTerminado(false);
				frameInserirDados.setVisible(false);
				iniciarCampeonato();
			}
		}
	}
	
	/**
	 * Listener do botao de salvar/carregar dados de um campeonato
	 */
	private class botaoSalvarCarregarListener implements ActionListener {
		private botaoSalvarCarregarListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			frame.setSize(220, 50);
			JFileChooser seletorArquivo = new JFileChooser();
			seletorArquivo.setFileFilter(new FileNameExtensionFilter("GENIUS campeonato", "genius"));
			if (botaoSalvarCarregar.getText() == "CARREGAR CAMPEONATO") {
				seletorArquivo.setDialogTitle("Especifique o arquivo que deseja abrir");
				int opcaoSelecionada = seletorArquivo.showOpenDialog(frame);
				// somente avançamos se foi selecionado um arquivo válido
				if (opcaoSelecionada != JFileChooser.APPROVE_OPTION) return;

				// conterá dados necessários para continuar o campeonato (indexJogadorAtual, moduloVelocidade, offsetFase)
				String primeiraLinha = "";

				try {
					File arquivoSelecionado = seletorArquivo.getSelectedFile();
					FileInputStream arquivoEntrada = new FileInputStream(arquivoSelecionado.getAbsoluteFile());

					// le primeira linha (ou seja, até que cheguemos no \n)
					for (int byteChar = arquivoEntrada.read(); byteChar != '\n'; byteChar = arquivoEntrada.read()) {
						primeiraLinha += (char) byteChar; // convertendo para tipo caracter
					}

					ObjectInputStream entrada = new ObjectInputStream(arquivoEntrada);
					campeonatoAtual = (Campeonato) entrada.readObject();
					entrada.close();
					entrada.close();
				} catch (IOException excecao) {
					excecao.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Falha! Arquivo inválido.", "Erro ao Carregar Campeonato", JOptionPane.ERROR_MESSAGE);
					return;
				} catch (ClassNotFoundException excecao) {
					excecao.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Falha! Arquivo inválido.", "Erro ao Carregar Campeonato", JOptionPane.ERROR_MESSAGE);
					return;
				}
                // separando a primeira linha para pegar os dados desejados
				String[] valores = primeiraLinha.split(" ");
				moduloVelocidade = Integer.parseInt(valores[1]);
				offsetFase = Integer.parseInt(valores[2]);
				indexJogadorAtual = Integer.parseInt(valores[0]);
				
				botaoPrincipal.setText("PAUSAR");
				botaoSalvarCarregar.setText("SALVAR CAMPEONATO");

				// quando salvo, o jogador atual teve seu tempo pausado, logo
				// devemos retomar a jogada (sair do estado pausado)
				campeonatoAtual.getJogador(indexJogadorAtual).retomaJogada();
				temporizador.start();
				jogoRodando = true;
				alertaJogoTerminado(false);
				toques = 0; // zera contador de atualizações da tela
				iniciarUmaSequencia();
			} else {
				// utilizado para salvar o estado do jogoRodando (evitar que o jogo continue
				// enquanto estiver na tela de salvamento)
				boolean jogoRodandoEstado = jogoRodando;
				campeonatoAtual.getJogador(indexJogadorAtual).pausaJogada();
				jogoRodando = false;

				seletorArquivo.setDialogTitle("Especifique o caminho do arquivo que deseja salvar");
				int opcaoSelecionada = seletorArquivo.showSaveDialog(frame);
				// somente avançamos se foi selecionado um arquivo válido
				if (opcaoSelecionada != JFileChooser.APPROVE_OPTION) return;

				try {
					String caminhoArquivoSelecionado = seletorArquivo.getSelectedFile().getAbsolutePath();
					// caso o usuário não adicionar .genius no final do nome do arquivo, vamos adicioná-lo
					if (!caminhoArquivoSelecionado.endsWith(".genius")) {
						caminhoArquivoSelecionado += ".genius";
					}
					FileOutputStream arquivoSaida = new FileOutputStream(caminhoArquivoSelecionado);

					// usado para salvar dados importantes para o continuamento do campeonato na carga
					String primeiraLinha = indexJogadorAtual + " " + moduloVelocidade + " " + offsetFase + "\n";
					arquivoSaida.write(primeiraLinha.getBytes());

					ObjectOutputStream escritorSaida = new ObjectOutputStream(arquivoSaida);
					escritorSaida.writeObject(campeonatoAtual);
					escritorSaida.close();
					arquivoSaida.close();
				} catch (IOException excecao) {
					excecao.printStackTrace();
					JOptionPane.showMessageDialog(null,
						"Falha! " + excecao.getMessage(), "Erro ao Salvar Campeonato", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showConfirmDialog(null,
						"Sucesso, arquivo salvo!", "Salvar Campeonato", JOptionPane.DEFAULT_OPTION);

				// retornando ao estado anterior do jogo
				campeonatoAtual.getJogador(indexJogadorAtual).retomaJogada();
				jogoRodando = jogoRodandoEstado;
			}
		}
	}

	/**
	 * Listener do botão de ajuda
	 */
	private class botaoAjudaListener implements ActionListener {
		private botaoAjudaListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,"O GENIUS é um jogo de memória visual e sonora, com três níveis de velocidade\n"
					+ "(normal, lento e rápido) e de dificuldade (fácil, médio e difícil).\n\n" 
					+ "O jogo vai gerar sequências que o jogador deve seguir. Cada cor tem um som\n"
					+ "correspondente. Você precisa clicar nelas em sequência.\n\n"
					+ "A cada jogada o GENIUS acende uma luz e emite um som a mais, formando\n"
					+ "uma sequência diferente cada vez maior que deve ser repetida pelo jogador.\n\n"
					+ "Se você errar a sequência, a sua rodada termina e vai para o próximo jogador,\n"
					+ "se não houver mais jogadores o jogo termina.\n\n"
					+ "O jogo pode ser pausado e retomado do mesmo ponto e também é possível salvar\n"
					+ "o estado atual do campeonato em um arquivo e carregá-lo posteriormente.\n\n"
					+ "Trabalho Avaliativo da disciplina Programação Orientada a Objetos.\n\n"
					+ "Discentes: George Neres, Jean Andrade, Lucas Fonsêca e Vitor Emanuel", "Ajuda", JOptionPane.DEFAULT_OPTION);
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
				if ((campeonatoAtual.getJogador(indexJogadorAtual).getFaseAtual() - offsetFase) > 0) {
					campeonatoAtual.getJogador(indexJogadorAtual).retomaJogada();
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
					campeonatoAtual.getJogador(indexJogadorAtual).retomaJogada();
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