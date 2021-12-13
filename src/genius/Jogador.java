package genius;

public class Jogador {
	Placar _placar;
	String _nome;
	String _apelido;

	public Jogador(String nome, String apelido) {
		_nome = nome;
		_apelido = apelido;
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
