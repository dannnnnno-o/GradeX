package Components;
import javax.swing.*;

import java.awt.*;

public class MyButton extends JButton{
    
    String text;
    String BackgroundHex = "#c9c9c9";
    // String TextHex = "#0";
    Color BackgroundColor = Color.decode(BackgroundHex);
    // Color TextColor = Color.decode(TextHex);
    // boolean mode; //active or inactive

    int radius = 25;
    

    public MyButton(String text){
        this.text = text;
        setText(text);
        setBackground(BackgroundColor);
        setForeground(Color.BLACK);
        setMargin(new Insets(5, 5, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setFont(new Font("Raleway", Font.BOLD, 18));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public void setActive(boolean active) {
        if (active) {
            setBackground(Color.decode("#222222"));
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.decode("#c9c9c9"));
            setForeground(Color.BLACK);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw text
        super.paintComponent(g2);
        g2.dispose();
    }
}
