package genius;

import java.util.Random;

public class SequenciaDeCores {

	/**
	 * Representa uma sequência de inteiros que representam os botões de cores do jogo
	 */
	private int quantidade;
	private int[] sequencia;
	private Random randomizador;

	/**
	 * Cria uma nova sequência de cores com inteiros aleatórios para quaisquer números de sequência
	 * @param quantidade   quantidade da sequencia
	 */
	public SequenciaDeCores(int quantidade) {
		this.quantidade = quantidade;
		sequencia = new int[quantidade];
		randomizador = new Random();
		criarSequencia();
	}

	/**
	 * Cria uma sequência aleatória de cores
	 */
	private void criarSequencia() {
		for (int i = 0; i < quantidade; i++) {
			sequencia[i] = randomizador.nextInt(4);
		}
	}

	/**
	 * Pegar elemento da sequência num indice específico
	 * @param indice     indice na sequência
	 * @return  elemento da sequencia em determinado indice, se o indice for inválido retorna -1
	 */
	public int getIndice(int indice) {
		if (indice < 0 || indice >= quantidade) {
			return -1;
		}
		return sequencia[indice];
	}

	/**
	 * @return quantidade da sequência
	 */
	public int getQuantidade() {
		return quantidade;
	}
}
