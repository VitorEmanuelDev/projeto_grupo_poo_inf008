package genius;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Representa um quadradona GUI do jogo
 */
public class QuadradosCores {
    private Color corPrincipal;
    private int largura;
    private int altura;
    private int posicaoX;
    private int posicaoY;
    private boolean devePiscar;
    private static boolean jogoTerminado; // TODO considerar remoção
    private static Color corJogoTerminado = new Color(30, 30, 30);

    /**
     * Cria um quadrado com uma determinada cor principal, tamanho e posicao 
     * @param corPrincipal    cor principal do quadrado
     * @param largura         largura do quadrado
     * @param altura          altura do quadrado
     * @param posicaoX        posicao no plano x
     * @param posicaoY        posicao no plano y
     */
    public QuadradosCores(Color corPrincipal, int largura, int altura, int posicaoX, int posicaoY) {
        this.corPrincipal = corPrincipal;
        this.largura = largura;
        this.altura = altura;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        devePiscar = false;
        jogoTerminado = false;
    }

    /**
     * Desenha a corPrincipal do quadrado sendo mostrado
     * @param g     contexto no qual sera desenhado
     */
    public void desenharColorirQuadrado(Graphics2D g) {
        if (jogoTerminado) {
            g.setColor(corJogoTerminado);
        } else if (devePiscar) {
            g.setColor(corPrincipal);
        } else {
            g.setColor(corPrincipal.darker().darker());
        }
        g.fillRect(posicaoX, posicaoY, largura, altura);
    }

    /**
     * @param x     x abcissa
     * @param y     y ordenada
     * @return      true se o clique estiver dentro das coordenadas do quadrado
     */
    public boolean estaDentroDasCoordenadas(int x, int y) {
        if (x < posicaoX || y < posicaoY) {
            return false;
        }
        if (x < (posicaoX + largura) && y < (posicaoY + altura)) {
            return true;
        }
        return false;
    }

    /**
     * @param bool      mostra se o quadrado deve piscar ou nao
     */
    public void setPiscada(boolean bool) {
        devePiscar = bool;
    }

    /**
     * @param bool      mostra se o quadrado deve estar em modo de jogo terminado
     */
    public void setJogoTerminado(boolean bool) {
        jogoTerminado = bool;
    }

}
