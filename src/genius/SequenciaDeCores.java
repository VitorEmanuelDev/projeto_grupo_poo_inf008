package genius;

import java.util.Random;

/**
 * Representa uma sequencia de inteiros
 */
public class SequenciaDeCores {

	private int quantidade;
	private int[] sequencia;
	private Random randomizador;
	/**
	 * Cria uma nova sequencia de cores com inteiros aleatorios para qualquer quantidade
	 * @param quantidade   quantidade da sequencia
	 */
	public SequenciaDeCores(int quantidade) {
		this.quantidade = quantidade;
		sequencia = new int[quantidade];
		randomizador = new Random();
		criarSequencia();
	}

	/**
	 * Cria uma sequencia aleatoria de cores
	 */
	private void criarSequencia() {
		for (int i = 0; i < quantidade; i++) {
			sequencia[i] = randomizador.nextInt(4);
		}
	}

	/**
	 * Pegar elemento da sequencia num indice especifico
	 * @param indice     indice na sequencia
	 * @return  elemento da sequencia em determinado indice. Se o indice for invalido, retorna -1
	 */
	public int getIndice(int indice) {
		if (indice < 0 || indice >= quantidade) {
			return -1;
		}
		return sequencia[indice];
	}

	/**
	 * @return quantidade da sequencia
	 */
	public int getQuantidade() {
		return quantidade;
	}
}
