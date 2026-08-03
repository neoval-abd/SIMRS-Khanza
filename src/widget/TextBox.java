
package widget;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.BorderFactory;

/**
 *
 * @author usu
 */
public class TextBox extends JTextField {
    public TextBox() {
        super();
        setFont(new java.awt.Font("Tahoma", 0, 11));
        setSelectionColor(new Color(255,252,252));
        setSelectedTextColor(new Color(255,0,0));
        setForeground(new Color(50,50,50));
        setBackground(new Color(255,255,255));
        setHorizontalAlignment(LEFT);
        setSize(WIDTH, 26);

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(1,8,1,8));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 12;

        // background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);

        // border abu tegas
        g2.setColor(new Color(60,60,60));
        g2.setStroke(new BasicStroke(1f));

        // biar ga kepotong
        g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, arc, arc);

        super.paintComponent(g);
        g2.dispose();
    }
}
