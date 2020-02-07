package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TaskGiverWindow extends JFrame {
    Random r=new Random();
    JSpinner amount=new JSpinner();
    JTextField name=new JTextField();
    JTextField folder=new JTextField();
    JPanel tadd=new JPanel();
    JPanel tadd3=new JPanel();
    JPanel tadd2=new JPanel();
    JButton jb=new JButton("Создать работу!");
    JTextArea jtext =new JTextArea();
    //JScrollPane jscroll =new JScrollPane();
    public TaskGiverWindow(){
        super("Создание задания");
        amount.setValue(10);
        jtext.setLineWrap(true);
        jtext.setOpaque(false);
        jtext.setBackground(new Color(255,255,255,0));
        jtext.setEnabled(false);
        jtext.setDisabledTextColor(new Color(0,0,0));
        //jscroll.getViewport().setOpaque(false);
        //jscroll.setOpaque(false);
        jtext.setEditable(false);
        jtext.setColumns(30);
        tadd2.setLayout(new BorderLayout());
        tadd.setLayout(new BoxLayout(tadd,BoxLayout.PAGE_AXIS));
        tadd.add(Box.createVerticalStrut(10));
        JLabel label=new JLabel("Параметры работы     ");
        Font f = label.getFont();
        label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        tadd.add(label);
        tadd.add(Box.createVerticalStrut(30));
        tadd.add(new JLabel("Название работы или имя ученика:     "));
        tadd.add(name);
        tadd.add(Box.createVerticalStrut(30));
        tadd.add(new JLabel("Папка с заданиями:     "));
        tadd.add(folder);
        tadd.add(Box.createVerticalStrut(30));
        tadd.add(new JLabel("Количество заданий:     "));
        tadd.add(amount);
        tadd.add(Box.createVerticalStrut(30));
        tadd.add(jb);
        tadd.add(Box.createVerticalStrut(10));
        //tadd.add(new Box.Filler(new Dimension(0,0),new Dimension(0, 0),new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)));
        tadd2.add(tadd, BorderLayout.CENTER);
        tadd2.add(Box.createHorizontalStrut(20),BorderLayout.WEST);
        tadd2.add(Box.createHorizontalStrut(20),BorderLayout.EAST);
        tadd2.add(Box.createVerticalStrut(20),BorderLayout.SOUTH);
        tadd2.add(Box.createVerticalStrut(20),BorderLayout.NORTH);
        //jscroll.add(jtext);

        JLabel labell=new JLabel("Вывод программы:");
        Font fl = labell.getFont();
        labell.setFont(fl.deriveFont(fl.getStyle() | Font.BOLD));
        JPanel tadd4=new JPanel();
        tadd4.setLayout(new BorderLayout());
        tadd4.add(labell, BorderLayout.NORTH);
        tadd3.setLayout(new BorderLayout());
        tadd3.add(Box.createHorizontalStrut(20),BorderLayout.WEST);
        tadd3.add(Box.createHorizontalStrut(20),BorderLayout.EAST);
        tadd3.add(Box.createVerticalStrut(20),BorderLayout.SOUTH);
        tadd3.add(Box.createVerticalStrut(30),BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(tadd2,BorderLayout.WEST);
        //add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.CENTER);
        tadd4.add(jtext, BorderLayout.CENTER);
        tadd3.add(tadd4);
        add(tadd3,BorderLayout.EAST);
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void printLine(String text) {
        jtext.setText(text+System.lineSeparator()+ jtext.getText());
        /*try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        repaint();
    }
}
