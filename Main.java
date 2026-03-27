import javax.swing.*;
import java.awt.*;
import Components.*;

public class Main {
    // @SuppressWarnings("unused")
    public static void main(String[] args) {
        //Window Properties
        int WindowWidth = 1280;
        int WindowHeight = 720;
        String BackgroundColorHex = "#e9e9e9";
        Color BackgroundColor = Color.decode(BackgroundColorHex);

        

        // Left Panel Properties
        int LeftPanelX = 0;
        int LeftPanelY = 0;
        int LeftPanelWidth = (int) Math.round(WindowWidth * 0.25);
        int LeftPanelHeight = WindowHeight;

        // Mid Panel Properties
        int MidPanelX = LeftPanelWidth;
        int MidPanelY = 0;
        int MidPanelWidth = (int) Math.round(WindowWidth * 0.5);
        int MidPanelHeight = WindowHeight;

        // Right Panel Properties
        // int RightPanelX = LeftPanelWidth + MidPanelWidth;
        int RightPanelX = 0;
        int RightPanelY = 0;
        int RightPanelWidth = (int) Math.round(WindowWidth * 0.25);
        int RightPanelHeight = WindowHeight;



        //Frame Properties
        JFrame frame = new JFrame("Main Window");
        frame.setSize(WindowWidth, WindowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Left Panel
        JPanel LeftPanel = new JPanel();
        LeftPanel.setBounds(LeftPanelX, LeftPanelY, LeftPanelWidth, LeftPanelHeight);
        LeftPanel.setBackground(BackgroundColor);
        
        //Middle Panel
        JPanel MidPanel = new JPanel();
        MidPanel.setBounds(MidPanelX, MidPanelY, MidPanelWidth, MidPanelHeight);
        MidPanel.setBackground(BackgroundColor);

        //Right Panel
        JPanel RightPanel = new JPanel();
        RightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        RightPanel.setBounds(RightPanelX, RightPanelY, RightPanelWidth, RightPanelHeight);
        RightPanel.setBackground(BackgroundColor);

        //Left Panel Components
        MyButton subject = new MyButton("Subjects");
        MyButton calendar = new MyButton("Calendar");

        LeftPanel.add(subject);
        LeftPanel.add(calendar);

        //Mid Panel Components


        //Right Panel Components
        MyButton AddSubject = new MyButton("+ Add Subject");
        // AddSubject.setBounds(RightPanelX, RightPanelY, RightPanelWidth, RightPanelHeight);
        RightPanel.add(AddSubject);

        LeftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 
        MidPanel.setBorder(BorderFactory.createLineBorder(Color.red, 2)); 
        RightPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); 

        // Add panels to the frame
        frame.add(LeftPanel, BorderLayout.WEST);
        frame.add(MidPanel, BorderLayout.CENTER);
        frame.add(RightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
