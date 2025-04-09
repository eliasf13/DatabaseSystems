package edu.hsog.db;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        JFrame frame = generateJFrame();
        frame.setVisible(true);



    }
    public static JFrame generateJFrame() {
        GUI gui = new GUI();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setContentPane(gui.getMasterPanel());
        gui.getMasterPanel().setPreferredSize(new Dimension(800, 600));
        return frame;
    }
}