package org.liky.game.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FiveChess extends JFrame implements MouseListener,Runnable {
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    //背景图片
    BufferedImage image = null;
    //保存棋子坐标
    int x = 0;
    int y = 0;
    //保存之前下过的棋子坐标,属性0表示无棋子，1表示黑子，2表示该点是白子
    int[][] allChess = new int[19][19];
    //标识当时是黑棋还是白棋
    boolean isBlack = true;
    boolean canPlay = true;

    String message = "黑方先行";
    // 保存最大限制时间,线程类做倒计时
    int maxTime = 0;
    Thread t = new Thread();
    int blacktime =0;
    int whitetime = 0;
    String blacktMessage = "无限制";
    String whitetMessage = "无限制";

    public  FiveChess(){
        //System.out.println("宽度为: " + width +"高度为: " + height);
        this.setTitle("五子棋");
        this.setSize(500,500);
        this.setLocation((width-500)/2,(height-500)/2);
        this.setResizable(false);//窗体大小不可变
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//将窗体的关闭方式设置为默认关闭后程序结束
        this.addMouseListener(this);
        this.setVisible(true);

        t.start();
        t.suspend();

        try {
            image = ImageIO.read(new File("E:/progames/fivechess/2.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g){
        //双缓冲技术防止屏幕闪烁
        BufferedImage bi = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = bi.createGraphics();

        //绘制背景
        g2.drawImage(image,4,20,this);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("游戏信息: "+message,150,60);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("开始游戏",400,100);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("游戏设置",400,150);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("游戏说明",400,200);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("认输",400,300);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("关于",400,350);
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("退出",400,400);
        g2.setFont(new Font("宋体",130,14));
        g2.drawString("黑方时间:"+ whitetMessage,30,470);
        g2.drawString("白方时间"+blacktMessage,230,470);

        //绘制棋盘
        for(int i=0;i<19;i++){
        g2.drawLine(10,70+20*i,370,70+20*i);
        g2.drawLine(10+20*i,70,10+20*i,430);
        }
        //标注点位
        g2.fillOval(68,128,4,4);
        g2.fillOval(308,128,4,4);
        g2.fillOval(308,368,4,4);
        g2.fillOval(68,368,4,4);
        g2.fillOval(308,248,4,4);
        g2.fillOval(188,128,4,4);
        g2.fillOval(68,248,4,4);
        g2.fillOval(188,368,4,4);
        g2.fillOval(188,248,4,4);


        //绘制棋子


        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(allChess[i][j]==1){
                    //黑子
                    int tempx = 10+i*20;
                    int tempy = 70+j*20;
                    g2.fillOval(tempx-7,tempy-7,14,14);

                }
                if(allChess[i][j]==2){
                    //白子
                    int tempx = 10+i*20;
                    int tempy = 70+j*20;
                    g2.setColor(Color.WHITE);
                    g2.fillOval(tempx-7,tempy-7,14,14);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x-7,y-7,14,14);

                }
            }
        }
        g.drawImage(bi,0,0,this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("X" + e.getX());
        //System.out.println("Y" + e.getY());

        if(canPlay == true){
            x = e.getX();
            y = e.getY();
            if(x>=10 && x<=370 && y>=70 &&y<=430) {
                //System.out.println("在棋盘范围内:" + x +"——"+ y);
                x = (x-10)/20;
                y = (y-70)/20;
                if(allChess[x][y]==0){
                    if(isBlack == true){
                        allChess[x][y] =1;
                        isBlack = false;
                        message = "轮到黑方";
                    }else{
                        allChess[x][y] =2;
                        isBlack = true;
                        message = "轮到白方";
                    }
                    boolean winFlag = this.checkWin();
                    if(winFlag == true){
                        JOptionPane.showMessageDialog(this,"游戏结束"+(allChess[x][y]==1?"黑方":"白方")+"获胜");
                        canPlay =false;
                    }
                }else{
                    JOptionPane.showMessageDialog(this,"当前位置已有棋子，请重新落子");
                }
                this.repaint();
            }
        }
        //重新开始游戏
        if(e.getX()>=400 && e.getX()<=470 && e.getY()>=70&&e.getY()<=100){
            int result = JOptionPane.showConfirmDialog(this,"是否从新开始游戏?");
            if(result == 0){
                //重新开始游戏
                //（1）棋盘清空,allChess[][]数据全部归0,（2）游戏信息改回开始位置，（3）将下一步下棋方设置为黑方
                for(int i=0;i<19;i++){
                    for(int j=0;j<19;j++){
                        allChess[i][j] = 0;
                    }
                }
                message = "黑方先行";
                isBlack = true;
                blacktime = maxTime;
                whitetime = maxTime;
                blacktMessage = maxTime/3600+":" +(maxTime/60-maxTime/3600*60)+":" +(maxTime-maxTime/60*60);
                whitetMessage = maxTime/3600+":" +(maxTime/60-maxTime/3600*60)+":" +(maxTime-maxTime/60*60);
                t.resume();
                this.repaint();//窗体需重新绘制
            }

        }
        if(e.getX()>=400 && e.getX()<=470 && e.getY()>=120&&e.getY()<=150){
            //JOptionPane.showMessageDialog(this,"游戏设置");
            String input = JOptionPane.showInputDialog("请输入游戏最大时间(分钟）:");
            try{
                maxTime = Integer.parseInt(input)*60;
                if(maxTime<0){
                    JOptionPane.showMessageDialog(this,"请输入正确信心，不允许输入负数");
                }
            }catch(NumberFormatException e1){
                JOptionPane.showMessageDialog(this,"请正确输入信息");
            }


        }
        if(e.getX()>=400 && e.getX()<=470 && e.getY()>=170&&e.getY()<=200){
            JOptionPane.showMessageDialog(this,"这是一个五子棋游戏，黑白双方轮流下棋，当某一方连到五子时，游戏结束");
        }
        //认输
        if(e.getX()>=400 && e.getX()<=470 && e.getY()>=270&&e.getY()<=300){
            int result = JOptionPane.showConfirmDialog(this,"是否确认认输？");
            if(result == 0){
                if(isBlack){
                    JOptionPane.showConfirmDialog(this,"黑方认输，游戏结束");
                }else{
                    JOptionPane.showConfirmDialog(this,"白方认输，游戏结束");
                }
                canPlay =false;
            }
        }
        if(e.getX()>=400 && e.getX()<=470 && e.getY()>=320&&e.getY()<=350){
            JOptionPane.showMessageDialog(this,"关于");
        }
        if(e.getX()>=400 && e.getX()<=470 && e.getY()>=370&&e.getY()<=400){
            JOptionPane.showMessageDialog(this,"游戏结束");
            System.exit(0);
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    private boolean checkWin(){
        boolean flag =false;
        //保存共有几个颜色棋子相连
        int count =1;
        int color = allChess[x][y];
        int i=1;
        //判断横向是否有五个棋子相连，纵坐标是相同的，即allChess[][y]的y是一致的
        while(color == allChess[x+i][y]){
            i++;
            count++;
        }
        i =1;
        while(color == allChess[x-i][y]){
            i++;
            count++;
        }
        if(count>=5){
            flag = true;
        }
        //判断纵向
        int count2 =1;
        int i2=1;
        while(color == allChess[x][y+i2]){
            i2++;
            count2++;
        }
        i2 =1;
        while(color == allChess[x][y-i2]){
            i2++;
            count2++;
        }
        if(count2>=5){
            flag = true;
        }
        //右斜方向的判断
        int count3 =1;
        int i3=1;
        while(color == allChess[x+i3][y-i3]){
            i3++;
            count3++;
        }
        i3 =1;
        while(color == allChess[x-i3][y+i3]){
            i3++;
            count3++;
        }
        if(count3>=5){
            flag = true;
        }
        //左斜方向判断
        int count4 =1;
        int i4=1;
        while(color == allChess[x-i4][y-i4]){
            i4++;
            count4++;
        }
        i4 =1;
        while(color == allChess[x+i4][y+i4]){
            i4++;
            count4++;
        }
        if(count4>=5){
            flag = true;
        }
        return flag;
    }

    @Override
    public void run() {
       while(true){
           if(isBlack){
               blacktime--;
           }else{
               whitetime--;
           }
           blacktMessage = blacktime/3600+":" +(blacktime/60-blacktime/3600*60)+":" +(blacktime-blacktime/60*60);
           whitetMessage = whitetime/3600+":" +(whitetime/60-whitetime/3600*60)+":" +(whitetime-whitetime/60*60);
           this.repaint();
           try{
               Thread.sleep(1000);
           }catch(InterruptedException e){
               e.printStackTrace();
           }

       }
    }
}
