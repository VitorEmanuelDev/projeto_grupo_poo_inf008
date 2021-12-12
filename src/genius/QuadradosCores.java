package genius;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Representa um quadradona GUI do jogo
 */
public class QuadradosCores {
    private Color _corPrincipal;
    private int _largura;
    private int _altura;
    private int _posicaoX;
    private int _posicaoY;
    private boolean _devePiscar;
    private static boolean jogoTerminado; // TODO considerar remoção
    private static Color corJogoTerminado = new Color(30, 30, 30);

    /**
     * Cria um quadrado com uma determinada cor principal, tamanho e posicao 
     * @param col       cor principal do quadrado
     * @param x         largura do quadrado
     * @param y         altura do quadrado
     * @param posicaoX   posicao no plano x
     * @param posicaoY   posicao no plano y
     */
    public QuadradosCores(Color corPrincipal, int x, int y, int posicaoX, int posicaoY) {
        _corPrincipal = corPrincipal;
        _largura = x;
        _altura = y;
        _posicaoX = posicaoX;
        _posicaoY = posicaoY;
        _devePiscar = false;
        jogoTerminado = false;
    }

    /**
     * Desenha a _corPrincipal do quadrado sendo mostrado
     * @param g     contexto no qual sera desenhado
     */
    public void desenharColorirQuadrado(Graphics2D g) {
        if (jogoTerminado) {
            g.setColor(corJogoTerminado);
        } else if (_devePiscar) {
            g.setColor(_corPrincipal);
        } else {
            g.setColor(_corPrincipal.darker().darker());
        }
        g.fillRect(_posicaoX, _posicaoY, _largura, _altura);
    }

    /**
     * @param x     x abcissa
     * @param y     y ordenada
     * @return      true se o clique estiver dentro das coordenadas do quadrado
     */
    public boolean estaDentroDasCoordenadas(int x, int y) {
        if (x < _posicaoX || y < _posicaoY) {
            return false;
        }
        if (x < (_posicaoX + _largura) && y < (_posicaoY + _altura)) {
            return true;
        }
        return false;
    }

    /**
     * @param bool      mostra se o quadrado deve piscar ou nao
     */
    public void setPiscada(boolean bool) {
        _devePiscar = bool;
    }

    /**
     * @param bool      mostra se o quadrado deve estar em modo de jogo terminado
     */
    public void setJogoTerminado(boolean bool) {
        jogoTerminado = bool;
    }

}
