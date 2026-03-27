import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Components.*;

public class Main {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        //Window Properties
        int WindowWidth = 1280;
        int WindowHeight = 720;
        String BackgroundColorHex = "#e9e9e9";
        Color BackgroundColor = Color.decode(BackgroundColorHex);
        String HeaderBGHex = "#f4f4f4";
        Color HeaderBG = Color.decode(HeaderBGHex);

        //Header
        int HeaderX = 0;
        int HeaderY = 0;
        int HeaderWidth = 1280;
        int HeaderHeight = 80;

        // Left Panel Properties
        int LeftPanelX = 0;
        int LeftPanelY = HeaderHeight;
        int LeftPanelWidth = (int) Math.round(WindowWidth * 0.25);
        int LeftPanelHeight = WindowHeight;

        // Mid Panel Properties
        int MidPanelX = LeftPanelWidth;
        int MidPanelY = HeaderHeight;
        int MidPanelWidth = (int) Math.round(WindowWidth * 0.5);
        int MidPanelHeight = WindowHeight;

        // Right Panel Properties
        // int RightPanelX = LeftPanelWidth + MidPanelWidth;
        int RightPanelX = 0;
        int RightPanelY = HeaderHeight;
        int RightPanelWidth = (int) Math.round(WindowWidth * 0.25);
        int RightPanelHeight = WindowHeight;



        //Frame Properties
        JFrame frame = new JFrame("GradeX");
        frame.setSize(WindowWidth, WindowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Header
        JPanel Header = new JPanel(new BorderLayout());
        Header.setPreferredSize(new Dimension(WindowWidth, 80));
        Header.setBackground(HeaderBG);

        JLabel hero = new JLabel("GradeX");
        hero.setFont(new Font("Raleway", Font.BOLD, 24));
        hero.setHorizontalAlignment(SwingConstants.LEFT);   // left horizontally
        hero.setVerticalAlignment(SwingConstants.CENTER);   // center vertically

        String BorderBottomHex = "#c9c9c9";
        Color BorderBottom = Color.decode(BorderBottomHex);

        Border LeftMargin = new EmptyBorder(0, 25, 0, 0);
        Border BottomBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, BorderBottom);

        Header.setBorder(BorderFactory.createCompoundBorder(BottomBorder, LeftMargin));
        Header.add(hero, BorderLayout.WEST);


        //Left Panel
        JPanel LeftPanel = new JPanel();
        LeftPanel.setPreferredSize(new Dimension(WindowWidth/4, WindowHeight));
        LeftPanel.setBackground(BackgroundColor);
        
        //Middle Panel
        JPanel MidPanel = new JPanel();
        MidPanel.setBackground(BackgroundColor);

        //Right Panel
        JPanel RightPanel = new JPanel();
        // RightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        RightPanel.setPreferredSize(new Dimension(WindowWidth/4, WindowHeight));
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

        // Header.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        // LeftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 
        // MidPanel.setBorder(BorderFactory.createLineBorder(Color.red, 2)); 
        // RightPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); 

        // Add panels to the frame
        frame.add(Header, BorderLayout.NORTH);
        frame.add(LeftPanel, BorderLayout.WEST);
        frame.add(MidPanel, BorderLayout.CENTER);
        frame.add(RightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
