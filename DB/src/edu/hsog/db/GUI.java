package edu.hsog.db;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Blob;

public class GUI {

    public GUI() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });

        initConPoolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Globals.initConnectionPool();
                connectionLabel.setText("verbunden");

            }
        });


        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int c = DBQueries.count();
                countLabel.setText("Count: " + c);

            }
        });

        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setName("imageJFch");
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    //Hier erscheint das Bild im jLabel2
                    Icon icon = Converter.loadIconFromFile(selectedFile.getAbsolutePath());
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String user = userTextField.getText();
                String passwd = passwordTextField.getText();
                if (DBQueries.login(user, passwd)) {
                    connectionLabel.setText("logged in");
                } else {
                    connectionLabel.setText("not logged in");
                }

            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            comment_textArea.setText("");
            comment_textField.setText("");
            description_textArea1.setText("");
            keywordsTextField.setText("");
            gadgetTextField.setText("");
            ownerLabel.setText("");;
            userTextField.setText("");
            passwordTextField.setText("");
            imageLabel.setText("");
            imageLabel.setIcon(null);

            }
        });

        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object[] o = DBQueries.getFirst();
                String comments = DBQueries.getComments((String) o[0]);


                if (connectionLabel.getText().equals("logged in")) {
                    gadgetTextField.setText((String) o[0]);
                    ownerLabel.setText((String) o[1]);
                    keywordsTextField.setText((String) o[2]);
                    description_textArea1.setText((String) o[3]);
                    imageLabel.setIcon(Converter.blob2ImageIcon((Blob) o[4]));

                    comment_textArea.setText(comments);
                    ratingLabel.setText(DBQueries.getRating((String) o[0]));
                }

            }
        });

        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] o = DBQueries.getLast();
                String comments = DBQueries.getComments((String) o[0]);

                if (connectionLabel.getText().equals("logged in")) {
                    gadgetTextField.setText((String) o[0]);
                    ownerLabel.setText((String) o[1]);
                    keywordsTextField.setText((String) o[2]);
                    description_textArea1.setText((String) o[3]);
                    imageLabel.setIcon(Converter.blob2ImageIcon((Blob) o[4]));
                    comment_textArea.setText(comments);
                    ratingLabel.setText(DBQueries.getRating((String) o[0]));
                }

            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] o = DBQueries.getNext(gadgetTextField.getText());
                String comments = DBQueries.getComments((String) o[0]);

                if (connectionLabel.getText().equals("logged in")) {
                gadgetTextField.setText((String) o[0]);
                ownerLabel.setText((String) o[1]);
                keywordsTextField.setText((String) o[2]);
                description_textArea1.setText((String) o[3]);
                imageLabel.setIcon(Converter.blob2ImageIcon((Blob) o[4]));
                comment_textArea.setText(comments);
                ratingLabel.setText(DBQueries.getRating((String) o[0]));
                }
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] o = DBQueries.getPrevious(gadgetTextField.getText());
                String comments = DBQueries.getComments((String) o[0]);

                if (connectionLabel.getText().equals("logged in")) {
                    gadgetTextField.setText((String) o[0]);
                    ownerLabel.setText((String) o[1]);
                    keywordsTextField.setText((String) o[2]);
                    description_textArea1.setText((String) o[3]);
                    imageLabel.setIcon(Converter.blob2ImageIcon((Blob) o[4]));
                    comment_textArea.setText(comments);
                    ratingLabel.setText(DBQueries.getRating((String) o[0]));
                }



            }
        });


        saveItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (connectionLabel.getText().equals("logged in")) {

                        DBQueries.saveItem(gadgetTextField.getText(), userTextField.getText(), keywordsTextField.getText(), description_textArea1.getText(), imageLabel.getIcon(), DBQueries.checkIFInDB(gadgetTextField.getText()));

                }

            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        saveRatingWithCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBQueries.addCommentAndRating(gadgetTextField.getText(), userTextField.getText(),likeslider1.getValue(), comment_textField.getText());

            }
        });
        deleteItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBQueries.delete(gadgetTextField.getText());

            }
        });
    }



    public JPanel getMasterPanel() {
        return masterPanel;
    }

    private JPanel masterPanel;
    private JButton exitButton;
    private JButton initConPoolButton;
    private JButton countButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton firstButton;
    private JButton searchButton;
    private JLabel connectionLabel;
    private JPanel Jpanel;
    private JLabel countLabel;
    private JButton lastButton;
    private JTextField userTextField;
    private JTextField passwordTextField;
    private JButton loginButton;
    private JButton registerButton;
    private JTextField keywordsTextField;
    private JTextArea description_textArea1;
    private JTextArea comment_textArea;
    private JTextField gadgetTextField;
    private JButton loadImageButton;
    private JButton saveItemButton;
    private JButton deleteItemButton;
    private JTextField comment_textField;
    private JButton saveRatingWithCommentButton;
    private JSlider likeslider1;
    private JLabel ownerLabel;
    private JLabel ratingLabel;
    private JButton clearButton;
    private JLabel imageLabel;
    private JPanel Jpnel;
}
