package genius;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ManuseadorArquivoCampeonato {

	/**
	 * Manuseador de arquivos, abstração de leitura e escrita de dados em forma persistente
	 */
	private String linhaCabecalho; // utilizada para salvar dados referentes ao estado atual do campeonato

	ManuseadorArquivoCampeonato() {
		linhaCabecalho = "";
	}

	public String getLinhaCabecalho() {
		return linhaCabecalho;
	}

	public void setLinhaCabecalho(String linhaCabecalho) {
		this.linhaCabecalho = linhaCabecalho;
	}

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
