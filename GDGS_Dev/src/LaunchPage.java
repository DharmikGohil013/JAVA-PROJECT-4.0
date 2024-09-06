import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class LaunchPage implements ActionListener{
    
    
 
 JFrame frame = new JFrame();
 JButton myButton = new JButton("Admin");
 JButton my1Button = new JButton("Student");
 JButton my2Button  = new JButton("Faculty"); 
 JButton my3Button  = new JButton("Exit"); 
 JLabel labal  = new JLabel("GDGS System",SwingConstants.CENTER);
 
 LaunchPage(){
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int labelWidth = 600;
        int labelHeight = 100;
        int labelX = (screenSize.width - labelWidth) / 2;  // Horizontally centered
        int labelY = 100;  // Vertically position the label
        labal.setBounds(labelX, labelY, labelWidth, labelHeight); // Set bounds for the label
        labal.setFont(new Font("Arial", Font.PLAIN, 50));
        labal.setForeground(Color.RED);
        frame.add(labal);
    
    
    
  
  myButton.setBounds(880,300,200,50);
  myButton.setFocusable(false);
  myButton.addActionListener(this);
  myButton.setForeground(Color.white);
  my1Button.setBounds(880,360,200,50);
  my1Button.setFocusable(false);
  my1Button.addActionListener(this);
  my1Button.setForeground(Color.white);
  my2Button.setBounds(880,420,200,50);
  my2Button.setFocusable(false);
  my2Button.addActionListener(this);
  my2Button.setForeground(Color.white);
  my3Button.setBounds(880,480,200,50);
  my3Button.setFocusable(false);
  my3Button.addActionListener(this);
  my3Button.setForeground(Color.white);
  myButton.setFont(new Font("Arial" ,Font.PLAIN,24));
  my1Button.setFont(new Font("Arial" ,Font.PLAIN,24));
  my2Button.setFont(new Font("Arial" ,Font.PLAIN,24));
  my3Button.setFont(new Font("Arial" ,Font.PLAIN,24));
  myButton.setBackground(Color.GRAY);
  my1Button.setBackground(Color.GRAY);
  my2Button.setBackground(Color.GRAY);
  my3Button.setBackground(Color.GRAY);
  
  frame.add(myButton);
  frame.add(my1Button);
  frame.add(my2Button);
  frame.add(my3Button);
  
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  frame.setLayout(null);
  frame.setVisible(true);
  
 }

 @Override
 public void actionPerformed(ActionEvent e) {
  
  if(e.getSource()==myButton) {
   frame.dispose();
   NewWindow myWindow = new NewWindow();
  }
 }
}