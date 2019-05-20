import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends JFrame {
    JButton music;
    BufferedImage img;
    String fileName;
    Color color;
//    int brushSize=5;
    int type = 0;
    PaintPanel pPanel;
    private Graphics g;
    private Graphics2D g2;

    Font font = new Font("Comic Sans MS", Font.BOLD, 15);
    Main(){
        init();
        listeners();
    }

   private void init(){
       this.setSize(1920,1080);
       this.setLayout(null);
       setMenuBar(this);
       addBottomToolBar(this);
       add(UpperToolBar(this));
       PaintPanel p = new PaintPanel();
       p.setBounds(100,100,1720,780);

       music = new JButton("play");
       music.setBounds(0,50,100,100);
       music.setBackground(Color.CYAN);
       add(music);

       add(p);
       this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   }

   private void setMenuBar(JFrame f){
       JMenuBar menuBar = new JMenuBar();
       menuBar.setBounds(0, -20, 200, 25);
       f.setJMenuBar(menuBar);

       JMenu fileMenu = new JMenu("File");
       fileMenu.setFont(font);
       menuBar.add(fileMenu);

       Action loadAction = new AbstractAction("Open") {
           public void actionPerformed(ActionEvent event) {
               JFileChooser jf = new JFileChooser();
               int result = jf.showOpenDialog(null);
               if (result == JFileChooser.APPROVE_OPTION) {
                   try {
                       // the frame size depends on the picture size
                       fileName = jf.getSelectedFile().getAbsolutePath();
                       File iF = new File(fileName);
                       jf.addChoosableFileFilter(new TextFileFilter(".png"));
                       jf.addChoosableFileFilter(new TextFileFilter(".jpg"));
                       img = ImageIO.read(iF);
                       f.update(f.getGraphics());
                       pPanel.setSize(img.getWidth(), img.getWidth());
                       pPanel.repaint();
                   } catch (FileNotFoundException ex) {
                       JOptionPane.showMessageDialog(f, "This file doesn't exist");
                   } catch (IOException ex) {
                       JOptionPane.showMessageDialog(f, "The exception of input/output");
                   } catch (Exception ex) {
                   }
               }
           }
       };
       JMenuItem loadMenu = new JMenuItem(loadAction);
       loadMenu.setFont(font);
       fileMenu.add(loadMenu);

       Action saveAction = new AbstractAction("Save") {
           public void actionPerformed(ActionEvent event) {
               try {
                   JFileChooser jf = new JFileChooser();
                   // Creating file filters
                   TextFileFilter pngFilter = new TextFileFilter(".png");
                   TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                   if (fileName == null) {
                       // Adding filters
                       jf.addChoosableFileFilter(pngFilter);
                       jf.addChoosableFileFilter(jpgFilter);
                       int result = jf.showSaveDialog(null);
                       if (result == JFileChooser.APPROVE_OPTION) {
                           fileName = jf.getSelectedFile().getAbsolutePath();
                       }
                   }
                   // check which filter was choosen
                   if (jf.getFileFilter() == pngFilter) {
                       ImageIO.write(img, "png", new File(fileName + ".png"));
                   } else {
                       ImageIO.write(img, "jpeg", new File(fileName + ".jpg"));
                   }
               } catch (IOException ex) {
                   JOptionPane.showMessageDialog(f, "Error of input/output");
               }
           }
       };
       JMenuItem saveMenu = new JMenuItem(saveAction);
       saveMenu.setFont(font);
       fileMenu.add(saveMenu);

       Action saveasAction = new AbstractAction("Save as...") {
           public void actionPerformed(ActionEvent event) {
               try {
                   JFileChooser jf = new JFileChooser();
                   // Creating filters
                   TextFileFilter pngFilter = new TextFileFilter(".png");
                   TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                   // Adding filters
                   jf.addChoosableFileFilter(pngFilter);
                   jf.addChoosableFileFilter(jpgFilter);
                   int result = jf.showSaveDialog(null);
                   if (result == JFileChooser.APPROVE_OPTION) {
                       fileName = jf.getSelectedFile().getAbsolutePath();
                   }
                   // check which filter was choosen
                   if (jf.getFileFilter() == pngFilter) {
                       ImageIO.write(img, "png", new File(fileName + ".png"));
                   } else {
                       ImageIO.write(img, "jpeg", new File(fileName + ".jpg"));
                   }
               } catch (IOException ex) {
                   JOptionPane.showMessageDialog(f, "Error of input/output");
               }
           }
       };
       JMenuItem saveasMenu = new JMenuItem(saveasAction);
       saveasMenu.setFont(font);
       fileMenu.add(saveasMenu);

       JMenuItem exitItem = new JMenuItem("Exit");
       exitItem.setFont(font);
       menuBar.add(exitItem);

       JMenuItem empty = new JMenuItem("");
       empty.setEnabled(true);
       menuBar.add(empty);

       exitItem.addActionListener(e -> System.exit(0));

       JToolBar toolbar = new JToolBar("Toolbar", JToolBar.VERTICAL);
   }

   private JPanel UpperToolBar(JFrame f){
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0,0,1920,100);
        int x=0;
       ImageIcon frameImg = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorFrame.png");
       JButton cFrame = new JButton(frameImg);
       cFrame.setBorderPainted(false);
//       cFrame.setContentAreaFilled(false);
       cFrame.setBounds(6,18,88,64);
//       cFrame.setSize(88,64);
       p.add(cFrame);
       x+=100;

       ImageIcon color1Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorRed.png");
       JButton color1 = new JButton(color1Img);
       color1.setBorderPainted(false);
       color1.setContentAreaFilled(false);
       color1.setBounds(x,0,70,100);
       p.add(color1);
       color1.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.RED;
               cFrame.setBackground(color);
           }
       });
        x+=70;
       ImageIcon color2Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorOrange.png");
       JButton color2 = new JButton(color2Img);
       color2.setBorderPainted(false);
       color2.setContentAreaFilled(false);
       color2.setBounds(x,0,70,100);
       p.add(color2);
       color2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.ORANGE;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color3Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorYellow.png");
       JButton color3 = new JButton(color3Img);
       color3.setBorderPainted(false);
       color3.setContentAreaFilled(false);
       color3.setBounds(x,0,70,100);
       p.add(color3);
       color3.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.YELLOW;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color4Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorGreen.png");
       JButton color4 = new JButton(color4Img);
       color4.setBorderPainted(false);
       color4.setContentAreaFilled(false);
       color4.setBounds(x,0,70,100);
       p.add(color4);
       color4.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.GREEN;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color5Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorGreenDark.png");
       JButton color5 = new JButton(color5Img);
       color5.setBorderPainted(false);
       color5.setContentAreaFilled(false);
       color5.setBounds(x,0,70,100);
       p.add(color5);
       color5.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = new Color(0,153,0);
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color6Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorCyan.png");
       JButton color6 = new JButton(color6Img);
       color6.setBorderPainted(false);
       color6.setContentAreaFilled(false);
       color6.setBounds(x,0,70,100);
       p.add(color6);
       color6.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.CYAN;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color7Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorBlue.png");
       JButton color7 = new JButton(color7Img);
       color7.setBorderPainted(false);
       color7.setContentAreaFilled(false);
       color7.setBounds(x,0,70,100);
       p.add(color7);
       color7.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.BLUE;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color8Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorBrown.png");
       JButton color8 = new JButton(color8Img);
       color8.setBorderPainted(false);
       color8.setContentAreaFilled(false);
       color8.setBounds(x,0,70,100);
       p.add(color8);
       color8.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = new Color(204,102,0);
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color9Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorBrownDark.png");
       JButton color9 = new JButton(color9Img);
       color9.setBorderPainted(false);
       color9.setContentAreaFilled(false);
       color9.setBounds(x,0,70,100);
       p.add(color9);
       color9.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = new Color(102,102,0);
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color10Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorPink.png");
       JButton color10 = new JButton(color10Img);
       color10.setBorderPainted(false);
       color10.setContentAreaFilled(false);
       color10.setBounds(x,0,70,100);
       p.add(color10);
       color10.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = new Color(255,204,153);
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color11Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorPurple.png");
       JButton color11 = new JButton(color11Img);
       color11.setBorderPainted(false);
       color11.setContentAreaFilled(false);
       color11.setBounds(x,0,70,100);
       p.add(color11);
       color11.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.MAGENTA;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color12Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorBlack.png");
       JButton color12 = new JButton(color12Img);
       color12.setBorderPainted(false);
       color12.setContentAreaFilled(false);
       color12.setBounds(x,0,70,100);
       p.add(color12);
       color12.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.BLACK;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color13Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorGrayDark.png");
       JButton color13 = new JButton(color13Img);
       color13.setBorderPainted(false);
       color13.setContentAreaFilled(false);
       color13.setBounds(x,0,70,100);
       p.add(color13);
       color13.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.GRAY;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color15Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorGray.png");
       JButton color15 = new JButton(color15Img);
       color15.setBorderPainted(false);
       color15.setContentAreaFilled(false);
       color15.setBounds(x,0,70,100);
       p.add(color15);
       color15.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.LIGHT_GRAY;
               cFrame.setBackground(color);
           }
       });
       x+=70;
       ImageIcon color16Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\colorWhite.png");
       JButton color16 = new JButton(color16Img);
       color16.setBorderPainted(false);
       color16.setContentAreaFilled(false);
       color16.setBounds(x,0,70,100);
       p.add(color16);
       color16.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               color = Color.WHITE;
               cFrame.setBackground(color);
           }
       });
       return p;
   }

   private void addBottomToolBar(JFrame f){
        ImageIcon pen1Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\pen1icon.png");
        JButton pen1 = new JButton(pen1Img);
        pen1.setBorderPainted(false);
        pen1.setContentAreaFilled(false);
        pen1.setBounds(100,864,100,100);
        f.add(pen1);
        pen1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type=0;
            }
        });

       ImageIcon pen2Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\pen2icon.png");
       JButton pen2 = new JButton(pen2Img);
       pen2.setBorderPainted(false);
       pen2.setContentAreaFilled(false);
       pen2.setBounds(200,864,100,100);
       f.add(pen2);
       pen2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               type=1;
           }
       });

       ImageIcon pen3Img = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\pen3icon.png");
       JButton pen3 = new JButton(pen3Img);
       pen3.setBorderPainted(false);
       pen3.setContentAreaFilled(false);
       pen3.setBounds(300,864,100,100);
       f.add(pen3);
       pen3.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               type=2;
           }
       });

       ImageIcon stampImg = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\stampicon.png");
       JButton stamp = new JButton(stampImg);
       stamp.setBorderPainted(false);
       stamp.setContentAreaFilled(false);
       stamp.setBounds(400,864,100,100);
       f.add(stamp);
       stamp.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               type=3;
           }
       });

       ImageIcon brushImg = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\brushicon.png");
       JButton brush = new JButton(brushImg);
       brush.setBorderPainted(false);
       brush.setContentAreaFilled(false);
       brush.setBounds(600,864,100,100);
       f.add(brush);
       brush.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               type=5;
           }
       });

    ImageIcon eraserImg = new ImageIcon("C:\\Programming\\11SUmmEr\\MerioPaint\\assets\\erasericon.png");
    JButton eraser = new JButton(eraserImg);
       eraser.setBorderPainted(false);
       eraser.setContentAreaFilled(false);
       eraser.setBounds(500,864,100,100);
       f.add(eraser);
       eraser.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            type=4;
        }
    });


}

   private void listeners(){
       music.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               playSound();
           }
       });
   }

    class TextFileFilter extends FileFilter
    {
        private String ext;
        public TextFileFilter(String ext)
        {
            this.ext=ext;
        }
        public boolean accept(java.io.File file)
        {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }
        public String getDescription()
        {
            return "*"+ext;
        }
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Programming\\11SUmmEr\\MerioPaint\\BGM1.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(100);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main s = new Main();
        s.setVisible(true);
    }

    class PaintPanel extends JPanel {
        PaintPanel() {
        }

        public void paintComponent(Graphics g) {
            if (img == null) {
                img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = img.createGraphics();
                d2.setColor(Color.white);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
        }
    }
}
