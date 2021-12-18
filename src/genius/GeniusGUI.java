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

public class GeniusGUI extends JPanel implements MouseListener {

	/**
	 * Classe principal da instância gráfica do jogo
	 */
	private static final long serialVersionUID = 3221282515071077635L;
	private List<JTextField> nomesJogadores = new ArrayList<JTextField>();
	private List<JTextField> apelidosJogadores = new ArrayList<JTextField>();
	private JButton botaoPrincipal;
	private JButton botaoInserirDados;
	private JButton botaoSalvarCarregar;
	private JButton botaoAjuda;
	private JComboBox<String> comboBoxDificuldade;
	private JComboBox<String> comboBoxVelocidade;
	private QuadradosCores[] botoesCores;
	private Sons som = new Sons();
	private Genius parent;

	private AudioClip[] arraySonoro = { som.getAudioVerde(), som.getAudioVermelho(), som.getAudioAmarelo(), som.getAudioAzul() };


	// CONSTANTES:
	private static final String NOME_JOGO = "Genius - Projeto POO!";
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
	private static final Color COR_FUNDO = new Color(0,0,0);

	/**
	 * Construtor, cria o frame principal e botões do jogo
	 */
	public GeniusGUI(Genius parent) {
		this.parent = parent;
		criaJanelaPrincipal();
		inicializaQuadradosCores();
		criaBotaoPrincipal();
		criaBotaoSalvarCarregar();
		criaBotaoAjuda();
		repaint();
	}

	/*
	 * Cria tela adicional de coleta de dados do campeonato, tanto seus participantes quanto a
	 * dificuldade, velocidade e nome do campeonato
	 */
	public void criarEntradaDeDadosInicial() { // TODO pensar em forma de retornar os dados
		Integer[] options = {1, 2, 3, 4, 5, 6, 7, 8};
		Integer tamanhoLista = (Integer) JOptionPane.showInputDialog(null, "Escolha o número de jogadores:", 
				"Jogadores", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (tamanhoLista == null) return;
		
		Campeonato campeonatoAtual = new Campeonato();
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

		String [] dificuldades = {"fácil", "médio", "difícil"};
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
		}

		painel.add(Box.createVerticalGlue());
		JScrollPane scrollPane = new JScrollPane(painel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(scrollPane);
        
        criaBotaoInserirDados(frame, nomeCampeonato);
        frame.pack();
		frame.setVisible(true);
	}

	/*
	 * Cria e mostra tela de relatório de desempenho dos jogadores durante o campeonato
	 */
	public void criaRelatorioFinal(Campeonato campeonato) {
		// inicializa e popula jogada mais rápida e soma total do tempo jogado pelo jogador
		Long [] listaJogadaMaisRapida = new Long[campeonato.getQuantidadeJogadores()];
		Long [] somaTotalTempo = new Long[campeonato.getQuantidadeJogadores()];
		for (int i = 0; i < campeonato.getQuantidadeJogadores(); i++) {
			Long jogadaMaisRapida = Long.MAX_VALUE;
			somaTotalTempo[i] = 0L;
			listaJogadaMaisRapida[i] = 0L;
			for (int j = 0; j < campeonato.getJogador(i).getTempoJogadas().size(); j++) {
				long atual = campeonato.getJogador(i).getTempoJogada(j);
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

		// popula colunas e dados utilizados pela tabela de relatório
		String[] colunas = new String[] {
				"Nome", "Apelido", "Fase", "Pontuacao", "Mais rapida (s)", "Tempo total"
		};
		Object[][] dados = new Object[campeonato.getQuantidadeJogadores()][6];
		for (int i = 0; i < campeonato.getQuantidadeJogadores(); i++) {
			dados[i][0] = campeonato.getJogador(i).getNome();
			dados[i][1] = campeonato.getJogador(i).getApelido();
			dados[i][2] = campeonato.getJogador(i).getFaseAtual() - parent.getOffsetFase(); // pegar o offset da classe pai
			dados[i][3] = campeonato.getJogador(i).getPontuacao();
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

		JFrame frame = new JFrame("Resultados do " + campeonato.getNome() + " - " + java.time.LocalDate.now()); // TODO talvez pegar dificuldade
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
	 * Cria frame principal
	 */
	private void criaJanelaPrincipal() { // TODO talvez errado e desnecessário
		JFrame frame = new JFrame(NOME_JOGO);
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
	 * Cria o botão principal
	 */
	private void criaBotaoPrincipal() {
		botaoPrincipal = new JButton("JOGAR");
		botaoPrincipal.setBackground(Color.GREEN);
		botaoPrincipal.setForeground(Color.BLACK);
		botaoPrincipal.setFocusPainted(false);
		botaoPrincipal.setFont(new Font("Comic", Font.BOLD, 10));
		botaoPrincipal.setBounds(250, 292, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botaoPrincipal.addActionListener(new botaoPrincipalListener());
		add(botaoPrincipal);
	}

	/**
	 * Cria o botão de salvar/carregar campeonato
	 */
	private void criaBotaoSalvarCarregar() {
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
	 * Cria o botão ajuda
	 */
	private void criaBotaoAjuda() {
		botaoAjuda = new JButton("AJUDA");
		botaoAjuda.setBackground(Color.BLACK);
		botaoAjuda.setForeground(Color.WHITE);
		botaoAjuda.setFocusPainted(false);
		botaoAjuda.setFont(new Font("Comic", Font.BOLD, 10));
		botaoAjuda.setBounds(475, 10, LARGURA_BOTAO_PRINCIPAL, ALTURA_BOTAO_PRINCIPAL);
		botaoAjuda.addActionListener(new botaoAjudaListener());
		add(botaoAjuda);
	}

	/**
	 * Cria o botão para popup de inserção de dados do campeonato
	 * @param frame   instância gráfica onde o botão será adicionado
	 * @param nomeCampeonato    instância de campo de texto para leitura do nome do campeonato
	 */
	private void criaBotaoInserirDados(JFrame frame, JTextField nomeCampeonato) {
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
	 * Inicializa os quadrados 
	 */
	private void inicializaQuadradosCores() {
		botoesCores = new QuadradosCores[NUM_QUADRADOS];
		botoesCores[0] = new QuadradosCores(Color.GREEN, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 105);
		botoesCores[1] = new QuadradosCores(Color.RED, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 105);
		botoesCores[2] = new QuadradosCores(Color.YELLOW, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 100, 320);
		botoesCores[3] = new QuadradosCores(Color.BLUE, LARGURA_QUADRADOS, ALTURA_QUADRADOS, 315, 320);
	}

	@Override
	protected void paintComponent(Graphics grafico) {
		super.paintComponent(grafico);
		paint((Graphics2D) grafico);
	}

	/**
	 * Renderiza os itens gráficos do jogo
	 * @param grafico     contexto de grafico 2D
	 */
	private void paint(Graphics2D grafico) {
		grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		grafico.setColor(COR_FUNDO);
		grafico.fillRect(0, 0, LARGURA, ALTURA);

		// Desenha quadrados do botoesCores
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			botoesCores[i].desenharColorirQuadrado(grafico);
		}

		Jogador jogadorAtual = parent.getJogadorAtual();
		if (jogadorAtual != null) {
			grafico.setColor(Color.WHITE);
			grafico.setFont(new Font("Comic", Font.BOLD, 14));
			//grafico.clearRect(); TODO
			grafico.drawString("Jogador: " + jogadorAtual.getApelido(), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET - 20);
			grafico.drawString("Fase:  " + (jogadorAtual.getFaseAtual() - parent.getOffsetFase()), (LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET);
			grafico.drawString("Pontos totais: " + jogadorAtual.getPontuacao() + " (+" + jogadorAtual.ultimaPontuacaoAcrescentada() + ")",
					(LARGURA/2) - 60,  ESCPACO_QUADRADOS + ESPACO_QUADRADOS_OFFSET + 20);
		}

		Jogador jogadorErrou = parent.getJogadorErrou();
		if (jogadorErrou != null) {
			grafico.setFont(new Font("Comic", Font.BOLD, 20));
			grafico.setColor(Color.RED);
			grafico.drawString(jogadorErrou.getApelido() + " errou!!!", (LARGURA/2) - 80,  550);
		}
	}

	/**
	 * Pisca a cor atual da sequência e toca o som correspondente
	 */
	public void ativaBotaoCor(int indicePadraoSequencia) {
		botoesCores[indicePadraoSequencia].setPiscada(true);
		arraySonoro[indicePadraoSequencia].play();
	}

	public void repaintBotoesCores() {
		// Desenha quadrados do botoesCores
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			botoesCores[i].desenharColorirQuadrado((Graphics2D) this.getGraphics());
		}
	}

	/**
	 * Alerta os quadrados de cores e botões acerca da mudança de estado do jogo
	 * @param bool    true se o jogo estiver terminado, false caso contrário
	 */
	public void alertaJogoTerminado(boolean ehJogoTerminado) {
		if (ehJogoTerminado) {
			botaoPrincipal.setText("JOGAR");
			botaoSalvarCarregar.setText("CARREGAR CAMPEONATO");
		}
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			botoesCores[i].setDesabilitado(ehJogoTerminado);
		}
	}

	/**
	 * Muda o piscar dos botões de cores
	 * @param bool  true se os quadrados devem estar piscando, false caso contrário
	 */
	public void triggerTodasPiscando(boolean bool) {
		for (int i = 0; i < NUM_QUADRADOS; i++) {
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
			} else if (botaoPrincipal.getText() == "CONTINUAR") {
				botaoPrincipal.setText("PAUSAR");
				parent.retomaJogada();
			} else {
				botaoPrincipal.setText("CONTINUAR");
				parent.pausaJogada();
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
			if (botaoPrincipal.getText() == "JOGAR") {
				//inicializa novo campeonato com nome padrão
				Campeonato campeonato = new Campeonato("Campeonato Amistoso");

				String nomeCampeonatoStr = nomeCampeonato.getText();
				if (nomeCampeonatoStr != null && !nomeCampeonatoStr.isEmpty()) {
					campeonato.setNome(nomeCampeonatoStr);		
				}
				parent.setCampeonatoAtual(campeonato);

				for (int i = 1; i <= nomesJogadores.size(); i++) {
					// inicializa novo jogador com nome e apelido padrão
					Jogador jogador = new Jogador("Jogador " + i, "J" + i);
					
					String nomeJogador = nomesJogadores.get(i - 1).getText();
					if (nomeJogador != null && !nomeJogador.isEmpty()) {
						jogador.setNome(nomeJogador);
					}

					String apelidoJogador = apelidosJogadores.get(i - 1).getText();
					if (apelidoJogador != null && !apelidoJogador.isEmpty()) {
						jogador.setApelido(apelidoJogador);
					}

					campeonato.adicionaJogador(jogador);
				}
				nomesJogadores.clear();
				apelidosJogadores.clear();
				alertaJogoTerminado(false);
				frameInserirDados.setVisible(false);
				botaoPrincipal.setText("PAUSAR");
				int velocidade = 50;
				if (comboBoxVelocidade.getSelectedIndex() == 1) {
					velocidade = 70;
				} else if (comboBoxVelocidade.getSelectedIndex() == 2) {
					velocidade = 30;
				}
				int quantidadeSequencia = 0;
				if (comboBoxDificuldade.getSelectedIndex() == 1) {
					quantidadeSequencia = 2;
				} else if (comboBoxDificuldade.getSelectedIndex() == 2) {
					quantidadeSequencia = 4;
				}
				parent.iniciaCampeonato(velocidade, quantidadeSequencia);
			}
		}
	}

	// TODO move to Genius part of the logic
	/**
	 * Listener do botão de salvar/carregar dados de um campeonato
	 */
	private class botaoSalvarCarregarListener implements ActionListener {
		private botaoSalvarCarregarListener() {}
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame(); // TODO analisar necessidade
			frame.setSize(220, 50);
			JFileChooser seletorArquivo = new JFileChooser();
			seletorArquivo.setFileFilter(new FileNameExtensionFilter("GENIUS campeonato", "genius"));
			ManuseadorArquivoCampeonato manuseadorArquivo = new ManuseadorArquivoCampeonato();

			if (botaoSalvarCarregar.getText() == "CARREGAR CAMPEONATO") {
				try {
					seletorArquivo.setDialogTitle("Especifique o arquivo que deseja abrir");
					int opcaoSelecionada = seletorArquivo.showOpenDialog(frame);
					// somente avançamos se foi selecionado um arquivo válido
					if (opcaoSelecionada != JFileChooser.APPROVE_OPTION) {
						return;
					}

					File arquivoSelecionado = seletorArquivo.getSelectedFile();
					Campeonato campeonato = manuseadorArquivo.carregaCampeonato(arquivoSelecionado.getAbsolutePath()); // TODO botar 
					parent.setCampeonatoAtual(campeonato);

					// recupera dados do campeonato no cabeçalho
					String[] valores = manuseadorArquivo.getLinhaCabecalho().split(" ");
					parent.setIndexJogadorAtual(Integer.parseInt(valores[0]));
					parent.setModuloVelocidade(Integer.parseInt(valores[1]));
					parent.setOffsetFase(Integer.parseInt(valores[2]));

					mostraDialogMensagem("Sucesso, campeonato carregado!", "Carregar Campeonato");
					botaoPrincipal.setText("PAUSAR");
					botaoSalvarCarregar.setText("SALVAR CAMPEONATO");

					// quando salvo, o jogador atual teve seu tempo pausado, logo
					// devemos retomar a jogada (sair do estado pausado)
					alertaJogoTerminado(false);
					parent.retomaJogada();
					parent.reiniciaFase();
				} catch (IOException excecao) {
					excecao.printStackTrace();
					mostraDialogMensagem("Falha! Arquivo inválido.", "Carregar Campeonato", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException excecao) {
					excecao.printStackTrace();
					mostraDialogMensagem("Falha! Arquivo inválido.", "Carregar Campeonato", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				// pausando o jogo para evitar que o jogo continue
				// enquanto estiver na tela de salvamento
				parent.pausaJogada();

				try {
					// TODO mover seletorArquivo
					seletorArquivo.setDialogTitle("Especifique o caminho do arquivo que deseja salvar");
					
					int opcaoSelecionada = seletorArquivo.showSaveDialog(frame);
					// somente avançamos se foi selecionado um arquivo válido
					if (opcaoSelecionada != JFileChooser.APPROVE_OPTION) {
						return;
					}

					// salvando dados importantes para a continuação posterior do campeonato
					manuseadorArquivo.setLinhaCabecalho(parent.getIndexJogadorAtual() + " " + parent.getModuloVelocidade() + " " + parent.getOffsetFase());

					File arquivoSelecionado = seletorArquivo.getSelectedFile();
					manuseadorArquivo.salvaCampeonato(parent.getCampeonatoAtual(), arquivoSelecionado.getAbsolutePath());

					mostraDialogMensagem("Sucesso, arquivo salvo!", "Salvar Campeonato");
				} catch (IOException excecao) {
					excecao.printStackTrace();
					mostraDialogMensagem("Falha! " + excecao.getMessage(), "Salvar Campeonato", JOptionPane.ERROR_MESSAGE);
				}

				// retoma jogada
				parent.retomaJogada();
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
			mostraDialogMensagem("O GENIUS é um jogo de memória visual e sonora, com três níveis de velocidade\n"
					+ "(normal, lento e rápido) e de dificuldade (fácil, médio e difícil).\n\n" 
					+ "O jogo vai gerar sequências que o jogador deve seguir. Cada cor tem um som\n"
					+ "correspondente. Você precisa clicar nelas em sequência.\n\n"
					+ "A cada jogada o GENIUS acende uma luz e emite um som a mais, formando\n"
					+ "uma sequência diferente cada vez maior que deve ser repetida pelo jogador.\n\n"
					+ "Se você errar a sequência, a sua rodada termina e vai para o próximo jogador,\n"
					+ "se não houver mais jogadores o jogo termina.\n\n"
					+ "O jogo pode ser pausado e retomado do mesmo ponto, também é possível salvar\n"
					+ "o estado atual do campeonato em um arquivo e carregá-lo posteriormente.\n\n"
					+ "Trabalho Avaliativo da disciplina Programação Orientada a Objetos.\n\n"
					+ "Discentes: George Neres, Jean Andrade, Lucas Fonsêca e Vitor Emanuel", "Ajuda");
			}
		}

	// Listeners do mouse:
	@Override
	public void mousePressed(MouseEvent e) {
		if (parent.jogadorPodeClicar()) {
			int indiceCorClicada = coordenadasQuadrado(e.getX(), e.getY());
			if (indiceCorClicada == -1) {
				return;
			}
			botoesCores[indiceCorClicada].setPiscada(true); // TODO talvez ativaBotaoCor
			repaint();
			parent.checaJogada(indiceCorClicada);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}

	/**
	 * Pega o indice do quadrado colorido dentro de uma dada coordenada
	 * @param x     coordenada x
	 * @param y     coordenada y
	 * @return      indice do quadrado de cor dentro das coordenadas; -1 se nenhum quadrado estiver dentro
	 */
	private int coordenadasQuadrado(int x, int y) {
		for (int i = 0; i < NUM_QUADRADOS; i++) {
			if (botoesCores[i].estaDentroDasCoordenadas(x, y)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Mostra na tela um dialog de mensagem
	 * @param corpoConteudo    conteúdo da mensagem a ser mostrada
	 * @param titulo           titulo do dialog que vai ser criado
	 * @param tipoJanela       tipo do dialog a ser mostrado
	 */
	public void mostraDialogMensagem(String corpoConteudo, String titulo, int tipoJanela) {
		JOptionPane.showMessageDialog(null, corpoConteudo, titulo, tipoJanela);
	}

	/**
	 * Mostra na tela um dialog de mensagem com a opção padrão do JOptionPane
	 * @param corpoConteudo    conteúdo da mensagem a ser mostrada
	 * @param titulo           titulo do dialog que vai ser criado
	 */
	public void mostraDialogMensagem(String corpoConteudo, String titulo) {
		mostraDialogMensagem(corpoConteudo, titulo, JOptionPane.DEFAULT_OPTION);
	}
}