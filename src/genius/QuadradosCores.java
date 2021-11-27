package genius;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Representa um quadradona GUI do jogo
 */
public class QuadradosCores {
    private Color cor;
    private int TAMANHO_X;
    private int TAMANHO_Y;
    private int POSICAO_X;
    private int POSICAO_Y;
    private boolean pisca;
    private static boolean jogoTerminado;
    private static Color corJogoTerminado = new Color(30, 30, 30);

    /**
     * Cria um quadrado com uma determinada cor, tamanho e posicao 
     * @param col       cor do quadrado
     * @param x         largura do quadrado
     * @param y         altura do quadrado
     * @param posicaoX   posicao no plano x
     * @param posicaoY   posicao no plano y
     */
    public QuadradosCores(Color cor, int x, int y, int posicaoX, int posicaoY) {
        this.cor = cor;
        TAMANHO_X = x;
        TAMANHO_Y = y;
        POSICAO_X = posicaoX;
        POSICAO_Y = posicaoY;
        pisca = false;
        jogoTerminado = false;
    }

    /**
     * Desenha a cor do quadrado sendo mostrado
     * @param g     contexto no qual sera desenhado
     */
    public void desenharColorirQuadrado(Graphics2D g) {
        if (jogoTerminado)
            g.setColor(corJogoTerminado);
        else if (pisca)
            g.setColor(cor);
        else
            g.setColor(cor.darker().darker());
        g.fillRect(POSICAO_X, POSICAO_Y, TAMANHO_X, TAMANHO_Y);
    }

    /**
     * @param x     x abcissa
     * @param y     y ordenada
     * @return      true se o clique estiver dentro das coordenadas do quadrado
     */
    public boolean estaDentroDasCoordenadas(int x, int y) {
        if (x < POSICAO_X || y < POSICAO_Y)
            return false;
        if (x < (POSICAO_X + TAMANHO_X) && y < (POSICAO_Y + TAMANHO_Y))
            return true;
        return false;
    }

    /**
     * @param bool      mostra se o quadrado deve piscar ou nao
     */
    public void setPiscada(boolean bool) {
        pisca = bool;
    }

    /**
     * @param bool      mostra se o quadrado deve estar em modo de jogo terminado
     */
    public void setJogoTerminado(boolean bool) {
        jogoTerminado = bool;
    }

}
