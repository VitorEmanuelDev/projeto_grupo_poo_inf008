package genius;

import java.util.Random;

/**
 * Representa o placar
 */
public class Placar {
	
    private int fase;
    private int pontuacao;
    private Random random;
    private static final int BONUS_DE_PASSAGEM_DE_NIVEL = 1500;
    private static final int PONTUACAO_MINIMA = 100;

    /**
     * Contrutor cria um novo placar
     */
    public Placar() {
        fase = 0;
        pontuacao = 0;
        random = new Random();
    }

    /**
     * Reinicia o placar
     */
    public void reinicializar() {
        fase = 0;
        pontuacao = 0;
    }

    /**
     * Incrementa a fase e atribui uma pontuacao
     */
    public void proximaFase() {
        fase++;
        if (fase != 1)
            pontuacao += fase * BONUS_DE_PASSAGEM_DE_NIVEL;
    }

    /**
     * Aumenta a pontuacao de acordo com o nivel
     */
    public void aumentarPontuacao() {
        pontuacao += fase * (PONTUACAO_MINIMA + random.nextInt(10));
    }

    /**
     * @return  informa a fase do jogador
     */
    public int getFase() {
        return fase;
    }

    /**
     * @return informa a pontuacao do jogador
     */
    public int getPontuacao() {
        return pontuacao;
    }
}
