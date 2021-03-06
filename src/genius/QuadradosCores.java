package genius;

import java.awt.Color;
import java.awt.Graphics2D;

public class QuadradosCores {

	/**
	 * Representa um quadrado com determinada cor na interface do jogo
	 */
    private Color corPrincipal;
    private final int ALTURA_OFFSET = 40;
    private int largura;
    private int altura;
    private int posicaoX;
    private int posicaoY;
    private boolean devePiscar;
    private static boolean estaDesabilitado;
    private static Color corDesabilitada = new Color(30, 30, 30);

    /**
     * Cria um quadrado com uma determinada cor principal, posição e dimensão
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
        this.devePiscar = false;
        estaDesabilitado = false;
    }

    /**
     * Desenha a corPrincipal do quadrado sendo mostrado
     * @param grafico     contexto no qual será desenhado
     */
    public void desenharColorirQuadrado(Graphics2D grafico) {
        if (estaDesabilitado) {
            grafico.setColor(corDesabilitada);
        } else if (devePiscar) {
            grafico.setColor(corPrincipal);
        } else {
            grafico.setColor(corPrincipal.darker().darker());
        }
        grafico.fillRect(posicaoX, posicaoY, largura, altura);
    }

    /**
     * @param x     x abcissa
     * @param y     y ordenada
     * @return      true se o clique estiver dentro das coordenadas do quadrado, false caso contrário
     */
    public boolean estaDentroDasCoordenadas(int x, int y) {
    	// altura offset é o valor que devemos adicionar para 
    	// chegar à altura da área clicável que esteja em sincronia
    	// com o quadrado colorido observável pelo usuário
        if (x < posicaoX || y < (posicaoY + ALTURA_OFFSET)) {
            return false;
        }
        if (x < (posicaoX + largura) && y < (posicaoY + altura + ALTURA_OFFSET)) {
            return true;
        }
        return false;
    }

    /**
     * @param bool      mostra se o quadrado deve piscar ou não
     */
    public void setPiscada(boolean bool) {
        devePiscar = bool;
    }

    /**
     * @param bool      mostra se o quadrado deve estar em modo de jogo terminado
     */
    public void setDesabilitado(boolean bool) {
        estaDesabilitado = bool;
    }
}
