package widget;

import java.awt.Color;
import java.awt.Insets;

/**
 *
 * @author usu
 */
public class Button extends usu.widget.ButtonGlass {

    /*
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    public Button() {
        super();
        setFont(new java.awt.Font("Tahoma", 1, 11));
        setForeground(new Color(50,50,50));
        setGlassColor(new Color(250,250,250));
        setMargin(new Insets(2, 7, 2, 7));
        setIconTextGap(4);
        setRoundRect(false);
    }
    
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

        super.paintComponent(g);
        g2.dispose();
    }
}
