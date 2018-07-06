/**
CustomWindow class - by Eduardo Maltez

GUI class that gives various options for the Rainmeter skin
*/


/*============
IMPORTS
===========*/

import java.io.FileWriter;
import java.io.PrintWriter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class CustomWindow extends JFrame
{
    public CustomWindow()
    {
        // window setup stuff
        super("VUColorometer Customization Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int winWidth = screenSize.width;
        int winHeight = screenSize.height;
        setSize((int)(winWidth*0.40), (int)(winHeight*0.50));

        

        setVisible(true);
    }

    /*================
    HELPER METHODS
    ================*/

    private void drawIO()
    {

    }

    private void createConfig(String filename) throws IllegalArgumentException
    {
        if(!filename.substring(filename.length()-4).equals(".cfg"))
            throw new IllegalArgumentException("setup file must be .cfg");


    }
}
