package com.company.panels;

import com.company.classes.Olympiad;
import com.company.classes.EduProgram;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;

import java.util.ArrayList;
import java.util.Scanner;

public class EduProgramPanel
{
    public MultiWindowTextGUI gui;
    public EduProgram e;
    public boolean b;
    public ArrayList <Olympiad> olympiads;

    public EduProgramPanel(MultiWindowTextGUI gui, EduProgram e, ArrayList <Olympiad> olympiads)
    {
        this.gui = gui;
        this.e = e;
        this.b = true;
        this.olympiads = olympiads;
    }

    public void process()
    {

        int wid = gui.getActiveWindow().getPosition().getColumn() + gui.getActiveWindow().getDecoratedSize().getColumns() + 1;

        Panel main_panel = new Panel();

        main_panel.addComponent(new Label("Краткое название"));
        TextBox txtshortname = new TextBox(new TerminalSize(30, 1));
        txtshortname.setText(e.shortname);
        main_panel.addComponent(txtshortname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Название"));
        TextBox txtname = new TextBox(new TerminalSize(30, 1));
        txtname.setText(e.name);
        main_panel.addComponent(txtname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Описание"));
        TextBox txtabout = new TextBox(new TerminalSize(30, 3));
        txtabout.setText(e.about);
        main_panel.addComponent(txtabout);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Ссылки"));
        TextBox txtlinks = new TextBox(new TerminalSize(30, 4));

        StringBuffer links = new StringBuffer("");
        for (int i = 0; i < e.links.size(); ++i)
        {
            if (i != 0) links.append("\n");
            links.append(e.links.get(i));
        }
        txtlinks.setText(String.valueOf(links));

        main_panel.addComponent(txtlinks);

        main_panel.addComponent(new EmptySpace());

        BasicWindow window = new BasicWindow();

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Олимпиады, дающие льготу"));
        CheckBoxList<Olympiad> checkbviO = new CheckBoxList<Olympiad>();
        for (int i = 0; i < olympiads.size(); ++i)
        {
            checkbviO.addItem(olympiads.get(i));
        }
        main_panel.addComponent(checkbviO);

        for (int i = 0; i < e.olympiads.size(); ++i)
        {
            checkbviO.setChecked(e.olympiads.get(i), true);
        }


        Panel exit_panel = new Panel();
        exit_panel.setLayoutManager(new GridLayout(2));

        exit_panel.addComponent(new Button("Сохранить и выйти", new Runnable() {
            @Override
            public void run() {
                e.shortname = txtshortname.getText();
                e.name = txtname.getText();
                e.about = txtabout.getText();
                e.links = new ArrayList<String>();
                Scanner sc = new Scanner(txtlinks.getText());
                while (sc.hasNextLine())
                {
                    e.links.add(new String(sc.nextLine()));
                }
                b = true;
                gui.removeWindow(window);
                return;
            }
        }));

        exit_panel.addComponent(new Button("Назад", new Runnable() {
            @Override
            public void run() {
                gui.removeWindow(window);
                b = false;
                return;
            }
        }));

        main_panel.addComponent(exit_panel);
        window.setComponent(main_panel);

        gui.addWindow(window);
        window.setPosition(new TerminalPosition(wid, 1));
        gui.waitForWindowToClose(window);
    }
}
