package org.liky.game.frame;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyChessFrame extends JFrame {
    public MyChessFrame(){

        this.setTitle("五子棋");
        this.setSize(200,300);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        //System.out.println("宽度为: " + width +"高度为: " + height);
        this.setLocation((width-200)/2,(height-100)/2);

        //this.addMouseListener(this);

        this.setVisible(true);
    }

    public void paint(Graphics g){
        g.drawString("五子棋游戏",20,40);//绘制字符串
        //g.drawOval(20,40,40,40);//绘制圆形
        g.fillOval(20,40,40,40);//绘制实心圆形
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("E:/progames/fivechess/1.jpg"));//图片导入
        }catch(IOException e){
            e.printStackTrace();
        }
        g.drawImage(image,0,0,this);//绘制一个已经存在的图片,将图片显示到窗口中
    }




    /*
    public void mouseClicked(MouseEvent e){
    //鼠标点击事件
        System.out.println("鼠标点击");
        JOptionPane.showMessageDialog(this,"鼠标点击");
    }
    public void mouseEntered(MouseEvent e){
        System.out.print("鼠标进入");
        JOptionPane.showMessageDialog(this,"鼠标进入");
    }
    public void mouseExited(MouseEvent e){
        System.out.print("鼠标离开");
        JOptionPane.showMessageDialog(this,"鼠标离开 ");
    }
    public void mousePressed(MouseEvent e){
        System.out.print("鼠标按压");
        JOptionPane.showMessageDialog(this,"鼠标离开 ");
    }
    public void mouseReleased(MouseEvent e){

    }*/
}
