package genius;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ManuseadorArquivoCampeonato {

	/**
	 * Manuseador de arquivos, abstração de leitura e escrita de dados de campeonatos em forma persistente
	 */
	private String linhaCabecalho; // utilizada para salvar dados referentes ao estado atual do campeonato

	/**
	 * Construtor, zera a linha de cabeçalho inicial
	 */
	ManuseadorArquivoCampeonato() {
		linhaCabecalho = "";
	}

	/**
	 * @return   retorna a linha de cabeçalho atual
	 */
	public String getLinhaCabecalho() {
		return linhaCabecalho;
	}

	/**
	 * Define a nova linha de cabeçalho
	 * @param linhaCabecalho   nova linha de cabeçalho a ser utilizada
	 */
	public void setLinhaCabecalho(String linhaCabecalho) {
		this.linhaCabecalho = linhaCabecalho;
	}

	/**
	 * Carrega o campeonato baseado no caminho do arquivo especificado
	 * @param caminhoArquivo   caminho do arquivo a ser utilizado
	 * @return   retorna o campeonato lido do arquivo
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Campeonato carregaCampeonato(String caminhoArquivo) throws IOException, ClassNotFoundException {
		FileInputStream arquivoEntrada = new FileInputStream(caminhoArquivo);

		// conterá dados necessários para continuar o campeonato (indexJogadorAtual, moduloVelocidade, offsetFase)
		String tempLinhaCabecalho = "";
		// le primeira linha (ou seja, até que chegue no \n)
		for (int byteChar = arquivoEntrada.read(); byteChar != '\n'; byteChar = arquivoEntrada.read()) {
			tempLinhaCabecalho += (char) byteChar; // convertendo para tipo caracter
		}

		setLinhaCabecalho(tempLinhaCabecalho);

		ObjectInputStream entrada = new ObjectInputStream(arquivoEntrada);
		Campeonato campeonato = (Campeonato) entrada.readObject();

		entrada.close();
		arquivoEntrada.close();

		return campeonato;
	}

	/**
	 * Salva campeonato no caminho especificado 
	 * @param campeonato    campeonato a ser salvo
	 * @param caminhoArquivo    caminho do arquivo onde o campeonato será salvo
	 * @throws IOException
	 */
	public void salvaCampeonato(Campeonato campeonato, String caminhoArquivo) throws IOException {
		// somente permitimos que o arquivo seja escrito caso a linhaCabecalho esteja definida
		if (linhaCabecalho.isEmpty()) {
			return;
		}
		// caso o usuário não adicionar .genius no final do nome do arquivo, vamos adicioná-lo
		if (!caminhoArquivo.endsWith(".genius")) {
			caminhoArquivo += ".genius";
		}
		FileOutputStream arquivoSaida = new FileOutputStream(caminhoArquivo);

		// usado para salvar dados importantes para o continuamento do campeonato na carga
		String primeiraLinhaCabecalho = linhaCabecalho + "\n";
		arquivoSaida.write(primeiraLinhaCabecalho.getBytes());

		ObjectOutputStream escritorSaida = new ObjectOutputStream(arquivoSaida);
		escritorSaida.writeObject(campeonato);

		escritorSaida.close();
		arquivoSaida.close();
	}
}
