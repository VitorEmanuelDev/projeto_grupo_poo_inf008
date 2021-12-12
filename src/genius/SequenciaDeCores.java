package genius;

import java.util.Random;

/**
 * Representa uma sequencia de inteiros
 */
public class SequenciaDeCores {
	private int _quantidade;
	private int[] _sequencia;
	private Random _randomizador; // TODO considerar uso de lista ou tipo array
	/**
	 * Cria uma nova sequencia de cores com inteiros aleatorios para qualquer quantidade
	 * @param quantidade   quantidade da sequencia
	 */
	public SequenciaDeCores(int quantidade) {
		_quantidade = quantidade;
		_sequencia = new int[_quantidade];
		_randomizador = new Random();
		criarSequencia();
	}

	/**
	 * Cria uma sequencia aleatoria de cores
	 */
	private void criarSequencia() {
		for (int i = 0; i < _quantidade; i++) {
			_sequencia[i] = _randomizador.nextInt(4);	
		}
	}

	/**
	 * Pegar elemento da sequencia num indice especifico
	 * @param indice     indice na sequencia
	 * @return  elemento da sequencia em determinado indice. Se o indice for invalido, retorna -1
	 */
	public int getIndice(int indice) {
		if (indice < 0 || indice >= _quantidade)
			return -1;
		return _sequencia[indice];
	}

	/**
	 * @return quantidade da sequencia
	 */
	public int getTamanho() {
		return _quantidade;
	}
}
