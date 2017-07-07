package com.thelavender.panels;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InfoPanel {
    MultiWindowTextGUI gui;

    public InfoPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;
    }

    public void process()
    {
        //Setting width of used panels
        int wid = gui.getActiveWindow().getPosition().getColumn() + gui.getActiveWindow().getDecoratedSize().getColumns() + 1;


        // Creating panel that holds everything
        Panel main_panel = new Panel();

        BasicWindow window = new BasicWindow();
        window.setComponent(main_panel);

        main_panel.addComponent(new Button("Назад", new Runnable() {
            @Override
            public void run() {
                gui.removeWindow(window);
                return;
            }
        }));

        main_panel.addComponent(new EmptySpace());


        Panel add_panel = new Panel();
        TextBox txtinfo = new TextBox(new TerminalSize(40, 8));
        txtinfo.setReadOnly(true);
        File f = new File("info.txt");
        FileReader reader = null;
        try {
            reader = new FileReader(f);
            txtinfo.setText(new String(Files.readAllBytes(Paths.get("info.txt"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        txtinfo.addTo(add_panel);
        add_panel.addTo(main_panel);

        gui.addWindow(window);
        window.setPosition(new TerminalPosition(wid, 1));
        gui.waitForWindowToClose(window);
    }
}
