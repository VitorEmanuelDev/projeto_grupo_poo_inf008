package genius;

import java.applet.AudioClip;
import java.util.Random;

/**
 * Representa uma sequencia de inteiros
 */
public class SequenciaDeCores {
	private int tamanho;
	private int[] sequencia;
	private Random random;
	/**
	 * Cria uma nova sequencia com inteiros aleatorios para qualquer tamanho
	 * @param tamanho   tamanho da sequencia
	 */
	public SequenciaDeCores(int tamanho) {
		this.tamanho = tamanho;
		sequencia = new int[tamanho];
		random = new Random();
		criarSequencia();
	}

	/**
	 * Cria uma sequencia aleatoria de cores
	 */
	private void criarSequencia() {
		for (int i = 0; i < tamanho; i++) {
			sequencia[i] = random.nextInt(4);
			
		}
	}

	/**
	 * Pegar elemento da sequencia num indice especifico
	 * @param indice     indice na sequencia
	 * @return  elemento da sequencia em determinado indice. Se o indice for invalido, retorna -1
	 */
	public int getIndice(int indice) {
		if (indice < 0 || indice >= tamanho)
			return -1;
		return sequencia[indice];
	}

	/**
	 * @return  tamanho da sequencia
	 */
	public int getTamanho() {
		return tamanho;
	}
}
