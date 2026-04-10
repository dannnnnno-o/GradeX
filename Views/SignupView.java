package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.BiConsumer;
import Components.MyButton;

public class SignupView extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private MyButton signupBtn;

    public SignupView(BiConsumer<String, String> onSignup) {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#e9e9e9"));
        setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.gridx = 0;

        // Username Label
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Raleway", Font.PLAIN, 14));
        userLabel.setForeground(Color.GRAY);
        gbc.gridy = 0;
        add(userLabel, gbc);

        // Username Field
        usernameField = createRoundedField();
        gbc.gridy = 1;
        add(usernameField, gbc);

        // Password Label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Raleway", Font.PLAIN, 14));
        passLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 5, 0);
        add(passLabel, gbc);

        // Password Field
        passwordField = createRoundedPasswordField();
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        add(passwordField, gbc);

        // Confirm Password Label
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Raleway", Font.PLAIN, 14));
        confirmLabel.setForeground(Color.GRAY);
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 0, 5, 0);
        add(confirmLabel, gbc);

        // Confirm Password Field
        confirmPasswordField = createRoundedPasswordField();
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        add(confirmPasswordField, gbc);

        // Signup Button
        signupBtn = new MyButton("Signup");
        signupBtn.setActive(true);
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        signupBtn.setPreferredSize(new Dimension(150, 45));

        signupBtn.addActionListener(e -> {
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());
            if (pass.equals(confirm)) {
                onSignup.accept(usernameField.getText(), pass);
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Signup Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(signupBtn, gbc);
    }

    private JTextField createRoundedField() {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#dcdcdc"));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setFont(new Font("Raleway", Font.PLAIN, 16));
        return field;
    }

    private JPasswordField createRoundedPasswordField() {
        JPasswordField field = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#dcdcdc"));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setFont(new Font("Raleway", Font.PLAIN, 16));
        return field;
    }
}
