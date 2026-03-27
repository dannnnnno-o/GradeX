package Components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;


class RoundedBorder extends JButton implements Border {
    private int radius;
    //constructor
    RoundedBorder(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false); // prevent default background fill
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }
    public boolean isBorderOpaque() {
        return true;
    }
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }

}