package swings;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RemoveMaxAndMinButton extends JDialog{
  public RemoveMaxAndMinButton(JFrame frame, String str){
    super(frame,str);
    frame.setUndecorated(true);
    addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent evt){
        System.exit(0);
            }
        });
  }
  public static void main(String[] args){
    try{
      RemoveMaxAndMinButton frame = new RemoveMaxAndMinButton(new JFrame(),
            "Remove the Minimize and Maximize button from the Title Bar");
      JPanel panel = new JPanel();
      panel.setSize(200,200);
      JLabel lbl = new JLabel("Kiran: This is frame with no minimize and maximize buttons.");
      panel.add(lbl);
      frame.add(panel);
      frame.setSize(400, 400);
      frame.setVisible(true);
    }
    catch(IllegalArgumentException e){
      System.exit(0);
    }
  } 
} 