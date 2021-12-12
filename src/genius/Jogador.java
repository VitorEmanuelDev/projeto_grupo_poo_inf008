package genius;

public class Jogador {
// TODO analisar possibilidade de uso do temporalizar aqui ou no placar
	Placar _placar;
	String _nome;
	String _apelido;
	
	public Jogador() {
	
	}

	public Jogador(String nome) {
		_nome = nome;
		_placar = new Placar();
	}

	public Placar getPlacar() {
		return _placar;
	}

	public void setPlacar(Placar placar) {
		_placar = placar;
	}

	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public String getApelido() {
		return _apelido;
	}

	public void setApelido(String apelido) {
		_apelido = apelido;
	}
}
