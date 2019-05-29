import com.sun.prism.RectShadowGraphics;
import sun.awt.CustomCursor;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends JFrame {
    JButton music;
    JPanel eraserPanel, stampPanel1, shaperPanel, copyPanel;
    BufferedImage img, copyImage;
    int copyMode = 0;
    Image stampImg;
    String fileName;
    Color color;
    int type = 0;
    int erasertype = 0;
    int shapetype = 0;
    int xPrev, yPrev, shX, shY;
    PaintPanel pPanel;
    private Graphics g;
    private Graphics2D g2;
    ImageObserver iObs = new ImageObserver() {
        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    };

    Font font = new Font("Comic Sans MS", Font.BOLD, 15);

    Main() {
        init();
        listeners(this);
    }

    private void init() {
        this.setSize(1920, 1080);
        this.setLayout(null);
        setMenuBar(this);
        add(UpperToolBar(this));
        add(DefaultBottomToolBar(this));
        pPanel = new PaintPanel();
        pPanel.setBounds(100, 100, 1720, 780);
        g = pPanel.getGraphics();
        g2 = (Graphics2D) g;
        music = new JButton("play");
        music.setBounds(0, 50, 100, 100);
        music.setBackground(Color.CYAN);
        playSound();
        pPanel.setFocusable(true);
        pPanel.requestFocus();
        add(pPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setMenuBar(JFrame f) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, -20, 200, 25);
        f.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        menuBar.add(fileMenu);

        Action newAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                img = null;
                pPanel.setSize(1700,750);
                pPanel = new PaintPanel();
            }
        };
        JMenuItem newItem = new JMenuItem(newAction);
        newItem.setFont(font);
        fileMenu.add(newItem);

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

    private JPanel UpperToolBar(JFrame f) {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 1920, 100);
        p.setFocusable(false);
        int x = 0;
        ImageIcon frameImg = new ImageIcon("assets\\colorFrame.png");
        JButton cFrame = new JButton(frameImg);
        cFrame.setBorderPainted(false);
//       cFrame.setContentAreaFilled(false);
        cFrame.setBounds(6, 18, 88, 64);
//       cFrame.setSize(88,64);
        p.add(cFrame);
        x += 100;

        ImageIcon color1Img = new ImageIcon("assets\\colorRed.png");
        JButton color1 = new JButton(color1Img);
        color1.setBorderPainted(false);
        color1.setContentAreaFilled(false);
        color1.setBounds(x, 0, 70, 100);
        p.add(color1);
        color1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.RED;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color2Img = new ImageIcon("assets\\colorOrange.png");
        JButton color2 = new JButton(color2Img);
        color2.setBorderPainted(false);
        color2.setContentAreaFilled(false);
        color2.setBounds(x, 0, 70, 100);
        p.add(color2);
        color2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.ORANGE;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color3Img = new ImageIcon("assets\\colorYellow.png");
        JButton color3 = new JButton(color3Img);
        color3.setBorderPainted(false);
        color3.setContentAreaFilled(false);
        color3.setBounds(x, 0, 70, 100);
        p.add(color3);
        color3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.YELLOW;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color4Img = new ImageIcon("assets\\colorGreen.png");
        JButton color4 = new JButton(color4Img);
        color4.setBorderPainted(false);
        color4.setContentAreaFilled(false);
        color4.setBounds(x, 0, 70, 100);
        p.add(color4);
        color4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.GREEN;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color5Img = new ImageIcon("assets\\colorGreenDark.png");
        JButton color5 = new JButton(color5Img);
        color5.setBorderPainted(false);
        color5.setContentAreaFilled(false);
        color5.setBounds(x, 0, 70, 100);
        p.add(color5);
        color5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = new Color(0, 153, 0);
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color6Img = new ImageIcon("assets\\colorCyan.png");
        JButton color6 = new JButton(color6Img);
        color6.setBorderPainted(false);
        color6.setContentAreaFilled(false);
        color6.setBounds(x, 0, 70, 100);
        p.add(color6);
        color6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.CYAN;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color7Img = new ImageIcon("assets\\colorBlue.png");
        JButton color7 = new JButton(color7Img);
        color7.setBorderPainted(false);
        color7.setContentAreaFilled(false);
        color7.setBounds(x, 0, 70, 100);
        p.add(color7);
        color7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.BLUE;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color8Img = new ImageIcon("assets\\colorBrown.png");
        JButton color8 = new JButton(color8Img);
        color8.setBorderPainted(false);
        color8.setContentAreaFilled(false);
        color8.setBounds(x, 0, 70, 100);
        p.add(color8);
        color8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = new Color(204, 102, 0);
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color9Img = new ImageIcon("assets\\colorBrownDark.png");
        JButton color9 = new JButton(color9Img);
        color9.setBorderPainted(false);
        color9.setContentAreaFilled(false);
        color9.setBounds(x, 0, 70, 100);
        p.add(color9);
        color9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = new Color(102, 102, 0);
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color10Img = new ImageIcon("assets\\colorPink.png");
        JButton color10 = new JButton(color10Img);
        color10.setBorderPainted(false);
        color10.setContentAreaFilled(false);
        color10.setBounds(x, 0, 70, 100);
        p.add(color10);
        color10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = new Color(255, 204, 153);
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color11Img = new ImageIcon("assets\\colorPurple.png");
        JButton color11 = new JButton(color11Img);
        color11.setBorderPainted(false);
        color11.setContentAreaFilled(false);
        color11.setBounds(x, 0, 70, 100);
        p.add(color11);
        color11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.MAGENTA;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color12Img = new ImageIcon("assets\\colorBlack.png");
        JButton color12 = new JButton(color12Img);
        color12.setBorderPainted(false);
        color12.setContentAreaFilled(false);
        color12.setBounds(x, 0, 70, 100);
        p.add(color12);
        color12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.BLACK;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color13Img = new ImageIcon("assets\\colorGrayDark.png");
        JButton color13 = new JButton(color13Img);
        color13.setBorderPainted(false);
        color13.setContentAreaFilled(false);
        color13.setBounds(x, 0, 70, 100);
        p.add(color13);
        color13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.GRAY;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color15Img = new ImageIcon("assets\\colorGray.png");
        JButton color15 = new JButton(color15Img);
        color15.setBorderPainted(false);
        color15.setContentAreaFilled(false);
        color15.setBounds(x, 0, 70, 100);
        p.add(color15);
        color15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.LIGHT_GRAY;
                cFrame.setBackground(color);
            }
        });
        x += 70;
        ImageIcon color16Img = new ImageIcon("assets\\colorWhite.png");
        JButton color16 = new JButton(color16Img);
        color16.setBorderPainted(false);
        color16.setContentAreaFilled(false);
        color16.setBounds(x, 0, 70, 100);
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

    private JPanel DefaultBottomToolBar(JFrame f) {
        setCursor(f, "pen1");
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        p.setBounds(100, 880, 900, 100);
        p.setBackground(Color.MAGENTA);
        p.setFocusable(false);
        ImageIcon pen1Img = new ImageIcon("assets\\pen1icon.png");
        JButton pen1 = new JButton(pen1Img);
        pen1.setBorderPainted(false);
        pen1.setContentAreaFilled(false);
        pen1.setBounds(100, 864, 100, 100);
        p.add(pen1);
//        f.add(pen1);
        pen1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 0;
                setCursor(f, "pen1");
            }
        });

        ImageIcon pen2Img = new ImageIcon("assets\\pen2icon.png");
        JButton pen2 = new JButton(pen2Img);
        pen2.setBorderPainted(false);
        pen2.setContentAreaFilled(false);
        pen2.setBounds(200, 864, 100, 100);
        p.add(pen2);
        pen2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 1;
                setCursor(f, "pen2");
            }
        });

        ImageIcon pen3Img = new ImageIcon("assets\\pen3icon.png");
        JButton pen3 = new JButton(pen3Img);
        pen3.setBorderPainted(false);
        pen3.setContentAreaFilled(false);
        pen3.setBounds(300, 864, 100, 100);
        p.add(pen3);
        pen3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 2;
                setCursor(f, "pen3");
            }
        });

        ImageIcon stampImg = new ImageIcon("assets\\stampicon.png");
        JButton stamp = new JButton(stampImg);
        stamp.setBorderPainted(false);
        stamp.setContentAreaFilled(false);
        stamp.setBounds(400, 864, 100, 100);
        p.add(stamp);
        stamp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type != 3)
                    addStampPanel1(f);
                type = 3;
                setCursor(f, "stamp");
                SwingUtilities.updateComponentTreeUI(f);
            }
        });

        ImageIcon brushImg = new ImageIcon("assets\\brushicon.png");
        JButton brush = new JButton(brushImg);
        brush.setBorderPainted(false);
        brush.setContentAreaFilled(false);
        brush.setBounds(600, 864, 100, 100);
        p.add(brush);
        brush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 5;
                setCursor(f, "brush");
            }
        });

        ImageIcon eraserImg = new ImageIcon("assets\\erasericon.png");
        JButton eraser = new JButton(eraserImg);
        eraser.setBorderPainted(false);
        eraser.setContentAreaFilled(false);
        eraser.setBounds(500, 864, 100, 100);
        p.add(eraser);
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type != 4)
                    addEraserPanel(f);
                type = 4;
                setCursor(f, "eraser");
                SwingUtilities.updateComponentTreeUI(f);
            }
        });

        ImageIcon shaperImg = new ImageIcon("assets\\shaperIcon.png");
        JButton shaper = new JButton(shaperImg);
        shaper.setBorderPainted(false);
        shaper.setContentAreaFilled(false);
        shaper.setBounds(700, 864, 100, 100);
        p.add(shaper);
        shaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type != 6) {
                    addShaperPanel(f);
                }
                type = 6;
                setCursor(f, "shaper");
                SwingUtilities.updateComponentTreeUI(f);
            }
        });

        ImageIcon copyImg = new ImageIcon("assets\\copy.png");
        JButton copy = new JButton(copyImg);
        copy.setBorderPainted(false);
        copy.setContentAreaFilled(false);
        copy.setBounds(800, 864, 100, 100);
        p.add(copy);
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type != 7) {
                    copyMode = 1;
                    addCopyPanel(f);
                }
                type = 7;
                setCursor(f, "copycursor");
                SwingUtilities.updateComponentTreeUI(f);
            }
        });

        ImageIcon textImg = new ImageIcon("assets\\texticon.png");
        JButton text = new JButton(textImg);
        text.setBorderPainted(false);
        text.setContentAreaFilled(false);
        text.setBounds(900, 864, 100, 100);
        p.add(text);
        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 8;
                pPanel.requestFocus();
                setCursor(f, "text");
            }
        });


        return p;
    }

    private void addEraserPanel(JFrame f) {
        eraserPanel = new JPanel();
        eraserPanel.setLayout(new GridLayout(6, 1));
        eraserPanel.setBounds(0, 100, 100, 800);
        eraserPanel.setFocusable(false);

        ImageIcon er1icon = new ImageIcon("assets\\eraser1icon.png");
        JButton er1 = new JButton(er1icon);
        er1.setSize(64, 69);
        er1.setContentAreaFilled(false);
        er1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erasertype = 0;
                setCursor(f, "eraser1cursor");
            }
        });
        eraserPanel.add(er1);

        ImageIcon er2icon = new ImageIcon("assets\\eraser2icon.png");
        JButton er2 = new JButton(er2icon);
        er2.setSize(64, 69);
        er2.setContentAreaFilled(false);
        er2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erasertype = 1;
                setCursor(f, "eraser2cursor");
            }
        });
        eraserPanel.add(er2);

        ImageIcon er3icon = new ImageIcon("assets\\eraser3icon.png");
        JButton er3 = new JButton(er3icon);
        er3.setSize(64, 69);
        er3.setContentAreaFilled(false);
        er3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erasertype = 2;
                setCursor(f, "eraser3cursor");
            }
        });
        eraserPanel.add(er3);

        ImageIcon er4icon = new ImageIcon("assets\\eraser4icon.png");
        JButton er4 = new JButton(er4icon);
        er4.setSize(64, 69);
        er4.setContentAreaFilled(false);
        er4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erasertype = 3;
                setCursor(f, "eraser4cursor");
            }
        });
        eraserPanel.add(er4);

        ImageIcon er5icon = new ImageIcon("assets\\eraser5icon.png");
        JButton er5 = new JButton(er5icon);
        er5.setSize(64, 69);
        er5.setContentAreaFilled(false);
        er5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erasertype = 4;
                setCursor(f, "eraser5cursor");
            }
        });
        eraserPanel.add(er5);

        ImageIcon er6icon = new ImageIcon("assets\\eraser6icon.png");
        JButton er6 = new JButton(er6icon);
        er6.setSize(64, 69);
        er6.setContentAreaFilled(false);
        er6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erasertype = 5;
                setCursor(f, "eraser6cursor");
            }
        });
        eraserPanel.add(er6);

        f.add(eraserPanel);
    }

    private void erase(int x, int y) {
        ImageIcon i;
        Image erImage;
        switch (erasertype) {
            case 0:
                i = new ImageIcon("assets\\eraser1.png");
                erImage = i.getImage();
                g2.drawImage(erImage, x - 100, y - 150, color, iObs);
                break;
            case 1:
                i = new ImageIcon("assets\\eraser2.png");
                erImage = i.getImage();
                g2.drawImage(erImage, x - 100, y - 160, color, iObs);
                break;
            case 2:
                i = new ImageIcon("assets\\eraser3.png");
                erImage = i.getImage();
                g2.drawImage(erImage, x - 100, y - 166, color, iObs);
                break;
            case 3:
                i = new ImageIcon("assets\\eraser4.png");
                erImage = i.getImage();
                g2.drawImage(erImage, x - 100, y - 170, color, iObs);
                break;
            case 4:
                i = new ImageIcon("assets\\eraser5.png");
                erImage = i.getImage();
                g2.drawImage(erImage, x - 116, y - 170, color, iObs);
                break;
            case 5:
                i = new ImageIcon("assets\\eraser6.png");
                erImage = i.getImage();
                g2.drawImage(erImage, x - 116, y - 170, color, iObs);
                break;
        }


    }

    private void addStampPanel1(JFrame f) {
        stampPanel1 = new JPanel();
        stampPanel1.setLayout(new GridLayout(12, 1));
        stampPanel1.setBounds(0, 100, 100, 800);
        stampPanel1.setFocusable(false);

        ImageIcon st1icon = new ImageIcon("assets\\stamps\\grass.png");
        JButton st1 = new JButton(st1icon);
        st1.setSize(64, 69);
        st1.setContentAreaFilled(false);
        st1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\grass.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st1);

        ImageIcon st2icon = new ImageIcon("assets\\stamps\\flowers.png");
        JButton st2 = new JButton(st2icon);
        st2.setSize(64, 69);
        st2.setContentAreaFilled(false);
        st2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\flowers.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st2);

        ImageIcon st3icon = new ImageIcon("assets\\stamps\\brick.png");
        JButton st3 = new JButton(st3icon);
        st3.setSize(64, 69);
        st3.setContentAreaFilled(false);
        st3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\brick.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st3);

        ImageIcon st4icon = new ImageIcon("assets\\stamps\\window.png");
        JButton st4 = new JButton(st4icon);
        st4.setSize(64, 69);
        st4.setContentAreaFilled(false);
        st4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\window.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st4);

        ImageIcon st5icon = new ImageIcon("assets\\stamps\\fence.png");
        JButton st5 = new JButton(st5icon);
        st5.setSize(64, 69);
        st5.setContentAreaFilled(false);
        st5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\fence.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st5);

        ImageIcon st6icon = new ImageIcon("assets\\stamps\\ladder.png");
        JButton st6 = new JButton(st6icon);
        st6.setSize(64, 69);
        st6.setContentAreaFilled(false);
        st6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\ladder.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st6);

        ImageIcon st7icon = new ImageIcon("assets\\stamps\\mario.png");
        JButton st7 = new JButton(st7icon);
        st7.setSize(64, 69);
        st7.setContentAreaFilled(false);
        st7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\mario.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st7);

        ImageIcon st8icon = new ImageIcon("assets\\stamps\\mushroom.png");
        JButton st8 = new JButton(st8icon);
        st8.setSize(64, 69);
        st8.setContentAreaFilled(false);
        st8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\mushroom.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st8);

        ImageIcon st9icon = new ImageIcon("assets\\stamps\\star.png");
        JButton st9 = new JButton(st9icon);
        st9.setSize(64, 69);
        st9.setContentAreaFilled(false);
        st9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\star.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st9);

        ImageIcon st10icon = new ImageIcon("assets\\stamps\\heart.png");
        JButton st10 = new JButton(st10icon);
        st10.setSize(64, 69);
        st10.setContentAreaFilled(false);
        st10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\heart.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st10);

        ImageIcon st11icon = new ImageIcon("assets\\stamps\\smile.png");
        JButton st11 = new JButton(st11icon);
        st11.setSize(64, 69);
        st11.setContentAreaFilled(false);
        st11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\smile.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st11);

        ImageIcon st12icon = new ImageIcon("assets\\stamps\\eye.png");
        JButton st12 = new JButton(st12icon);
        st12.setSize(64, 69);
        st12.setContentAreaFilled(false);
        st12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon ic = new ImageIcon("assets\\stamps\\eye.png");
                stampImg = ic.getImage();
            }
        });
        stampPanel1.add(st12);

        f.add(stampPanel1);
    }

    private void addShaperPanel(JFrame f) {
        shaperPanel = new JPanel();
        shaperPanel.setLayout(new GridLayout(6, 1));
        shaperPanel.setBounds(0, 100, 100, 800);
        shaperPanel.setFocusable(false);

        ImageIcon sh1icon = new ImageIcon("assets\\lineicon.png");
        JButton sh1 = new JButton(sh1icon);
        sh1.setSize(64, 69);
        sh1.setContentAreaFilled(false);
        sh1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapetype = 0;
            }
        });
        shaperPanel.add(sh1);

        ImageIcon sh2icon = new ImageIcon("assets\\recticon.png");
        JButton sh2 = new JButton(sh2icon);
        sh2.setSize(64, 69);
        sh2.setContentAreaFilled(false);
        sh2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapetype = 1;
            }
        });
        shaperPanel.add(sh2);

        ImageIcon sh3icon = new ImageIcon("assets\\rectfillicon.png");
        JButton sh3 = new JButton(sh3icon);
        sh3.setSize(64, 69);
        sh3.setContentAreaFilled(false);
        sh3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapetype = 2;
            }
        });
        shaperPanel.add(sh3);

        ImageIcon sh4icon = new ImageIcon("assets\\circleicon.png");
        JButton sh4 = new JButton(sh4icon);
        sh4.setSize(64, 69);
        sh4.setContentAreaFilled(false);
        sh4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapetype = 3;
            }
        });
        shaperPanel.add(sh4);

        ImageIcon sh5icon = new ImageIcon("assets\\circlefillicon.png");
        JButton sh5 = new JButton(sh5icon);
        sh5.setSize(64, 69);
        sh5.setContentAreaFilled(false);
        sh5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapetype = 4;
            }
        });
        shaperPanel.add(sh5);

        f.add(shaperPanel);
    }

    private void addCopyPanel(JFrame f) {
        copyPanel = new JPanel();
        copyPanel.setLayout(new GridLayout(6, 1));
        copyPanel.setBounds(0, 100, 100, 800);
        copyPanel.setFocusable(false);


        ImageIcon pasteIcon = new ImageIcon("assets\\copy.png");
        JButton pasteButton = new JButton(pasteIcon);
        pasteButton.setSize(64, 60);
        pasteButton.setEnabled(false);
        pasteButton.setContentAreaFilled(false);
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyMode = 2;
            }
        });

        ImageIcon copyIcon = new ImageIcon("assets\\copycursor.png");
        JButton copyButton = new JButton(copyIcon);
        copyButton.setSize(64, 60);
        copyButton.setContentAreaFilled(false);
//        copyButton.setBorderPainted(false);
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyMode = 1;
                pasteButton.setEnabled(true);
            }
        });
        copyPanel.add(copyButton);
        copyPanel.add(pasteButton);

        f.add(copyPanel);
    }

    private void listeners(JFrame f) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                g = img.getGraphics();
                g2 = (Graphics2D) g;
                shX = e.getX();
                shY = e.getY();
                super.mouseClicked(e);
                if (type == 3) {
                    color = null;
                    g2.drawImage(stampImg, e.getX() - 132, e.getY() - 192, color, iObs);
                }
                if (type == 7 && copyImage != null && copyMode == 2) {
                    g2.drawImage(copyImage, (e.getX() - 100 - copyImage.getHeight() / 2), e.getY() - 160 - copyImage.getHeight() / 2, color, iObs);
                }
                SwingUtilities.updateComponentTreeUI(f);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                shX = e.getX();
                shY = e.getY();
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                g = img.getGraphics();
                g2 = (Graphics2D) g;
                super.mouseReleased(e);
                int x1 = shX, x2 = e.getX(), y1 = shY, y2 = e.getY();
                x1 += -100;
                x2 += -100;
                y1 += -160;
                y2 += -160;
                if ((copyMode == 1) || (shapetype != 0 && type == 6)) {
                    if (x1 > x2) {
                        int c = x1;
                        x1 = x2;
                        x2 = c;
                    }
                    if (y1 > y2) {
                        int c = y1;
                        y1 = y2;
                        y2 = c;
                    }
                }
                if (type == 6) {
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(5));
                    switch (shapetype) {
                        case 0:
                            g2.drawLine(x1, y1, x2, y2);
                            break;
                        case 1:
                            g2.drawRect(x1, y1, x2 - x1, y2 - y1);
                            break;
                        case 2:
                            Rectangle2D r = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
                            g2.fill(r);
                            break;
                        case 3:
                            g2.drawOval(x1, y1, x2 - x1, y2 - y1);
                            break;
                        case 4:
                            Ellipse2D o = new Ellipse2D.Double(x1, y1, x2 - x1, y2 - y1);
                            g2.fill(o);
                            break;
                    }
                    SwingUtilities.updateComponentTreeUI(f);
                } else if (copyMode == 1 && (x2 != x1 && y1 != y2)) {
                    copyImage = img.getSubimage(x1, y1, x2 - x1, y2 - y1);
                }
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                g = img.getGraphics();
                g2 = (Graphics2D) g;
                switch (type) {
                    case 0:
                        g2.setColor(color);
                        g2.setStroke(new BasicStroke(5));
                        g2.drawLine(xPrev - 100, yPrev - 160, e.getX() - 100, e.getY() - 160);
                        break;
                    case 1:
                        g2.setColor(color);
                        g2.setStroke(new BasicStroke(10));
                        g2.drawLine(xPrev - 100, yPrev - 160, e.getX() - 100, e.getY() - 160);
                        break;
                    case 2:
                        g2.setColor(color);
                        g2.setStroke(new BasicStroke(20));
                        g2.drawLine(xPrev - 100, yPrev - 160, e.getX() - 100, e.getY() - 160);
                        break;
                    case 3:
                        color = null;
                        g2.drawImage(stampImg, e.getX() - 132, e.getY() - 192, color, iObs);
                        break;
                    case 4:
                        erase(e.getX(), e.getY());
                        break;
                    case 5:
                        g2.setColor(color);
                        g2.setStroke(new BasicStroke(10));
                        g2.drawLine(0, e.getY() - 160, pPanel.getWidth(), e.getY() - 160);
                        break;
                }
                xPrev = e.getX();
                yPrev = e.getY();
                SwingUtilities.updateComponentTreeUI(f);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (type != 4 && eraserPanel != null) {
                    remove(eraserPanel);
                }
                if (type != 3 && stampPanel1 != null) {
                    remove(stampPanel1);
                }
                if (type != 6 && shaperPanel != null) {
                    remove(shaperPanel);
                }
                if (type != 7 && copyPanel != null) {
                    remove(copyPanel);
                    copyMode = 0;
                }
                xPrev = e.getX();
                yPrev = e.getY();
                SwingUtilities.updateComponentTreeUI(f);
            }
        });
        pPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (type == 8) {
                    Graphics g = img.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(10));
                    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
                    String str = "";
                    str += e.getKeyChar();
                    g2.drawString(str, shX - 100, shY - 160);
                    shX += 20;
                    SwingUtilities.updateComponentTreeUI(f);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pPanel.requestFocus();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pPanel.requestFocus();
            }
        });
    }

    class TextFileFilter extends FileFilter {
        private String ext;

        public TextFileFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(java.io.File file) {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }

        public String getDescription() {
            return "*" + ext;
        }
    }

    private void setCursor(JFrame f, String cName) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("assets\\" + cName + ".png");
        f.setCursor(toolkit.createCustomCursor(image, new Point(0,
                0), "img"));
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Programming\\11SUmmEr\\MerioPaint\\BGM1.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(100);
        } catch (Exception ex) {
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
                this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
            }
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
        }

        @Override
        public int getWidth() {
            return super.getWidth();
        }

        public int getHeight() {
            return super.getHeight();
        }
    }
}
