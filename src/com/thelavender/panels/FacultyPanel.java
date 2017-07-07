package com.thelavender.panels;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.thelavender.abiturium_utils.classes.EduProgram;
import com.thelavender.abiturium_utils.classes.Faculty;
import com.thelavender.abiturium_utils.classes.Olympiad;
import com.thelavender.abiturium_utils.classes.University;

import java.util.ArrayList;
import java.util.Scanner;

public class FacultyPanel {

    public MultiWindowTextGUI gui;
    public Faculty f;
    public boolean b;
    public ArrayList <Olympiad> olympiads;
    public University university;


    public FacultyPanel(MultiWindowTextGUI gui, Faculty f, ArrayList <Olympiad> olympiads, University university)
    {
        this.university = university;
        this.gui = gui;
        this.f = f;
        this.b = true;
        this.olympiads = olympiads;
    }

    public void refresh(ComboBox <String> combo, String shortname)
    {
        combo.clearItems();
        combo.addItem("---Выберите---");
        for (int i = 0; i < f.eduPrograms.size(); ++i)
        {
            combo.addItem(f.eduPrograms.get(i).shortname);
            if (f.eduPrograms.get(i).shortname.equals(shortname))
            {
                combo.setSelectedIndex(i + 1);
            }
        }

    }

    public void process()
    {
        int wid = gui.getActiveWindow().getPosition().getColumn() + gui.getActiveWindow().getDecoratedSize().getColumns() + 1;

        Panel main_panel = new Panel();

        main_panel.addComponent(new Label("Краткое название"));
        TextBox txtshortname = new TextBox(new TerminalSize(30, 1));
        txtshortname.setText(f.shortname);
        main_panel.addComponent(txtshortname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Название"));
        TextBox txtname = new TextBox(new TerminalSize(30, 1));
        txtname.setText(f.name);
        main_panel.addComponent(txtname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Описание"));
        TextBox txtabout = new TextBox(new TerminalSize(30, 3));
        txtabout.setText(f.info);
        main_panel.addComponent(txtabout);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Ссылки"));
        TextBox txtlinks = new TextBox(new TerminalSize(30, 4));

        StringBuffer links = new StringBuffer("");
        for (int i = 0; i < f.links.size(); ++i)
        {
            if (i != 0) links.append("\n");
            links.append(f.links.get(i));
        }
        txtlinks.setText(String.valueOf(links));

        main_panel.addComponent(txtlinks);

        main_panel.addComponent(new EmptySpace());

        ComboBox <String> comboE = new ComboBox<String>();
        refresh(comboE, "---Выберите---");
        main_panel.addComponent(comboE);

        Panel additional_panel = new Panel();
        additional_panel.setLayoutManager(new GridLayout(2));
        main_panel.addComponent(additional_panel);

        additional_panel.addComponent(new Button("Удалить", new Runnable() {
            @Override
            public void run() {
                if (comboE.getSelectedIndex() == 0)
                {
                    return;
                }
                MessageDialogButton del = MessageDialog.showMessageDialog(gui, "", "Вы уверены, что хотите удалить?", MessageDialogButton.No, MessageDialogButton.Yes);
                if (del == MessageDialogButton.No)
                {
                    return;
                }
                f.eduPrograms.remove(comboE.getSelectedIndex() - 1);
                refresh(comboE, "---Выберите---");
            }
        }));

        main_panel.addComponent(new Button("Перейти", new Runnable() {
            @Override
            public void run() {
                if (comboE.getSelectedIndex() == 0)
                {
                    return;
                }
                EduProgram e = f.eduPrograms.get(comboE.getSelectedIndex() - 1);
                EduProgramPanel eduProgramPanel = new EduProgramPanel(gui, e, olympiads, f);
                eduProgramPanel.process();
                refresh(comboE, e.shortname);
                Main.save();
            }
        }));


        main_panel.addComponent(new Button("Добавить программу", new Runnable() {
            @Override
            public void run() {
                EduProgram e = new EduProgram();
                EduProgramPanel eduProgramPanel = new EduProgramPanel(gui, e, olympiads, f);
                eduProgramPanel.process();
                if (eduProgramPanel.b)
                {
                    f.eduPrograms.add(e);
                    refresh(comboE, e.shortname);
                    Main.save();
                }
            }
        }));

        BasicWindow window = new BasicWindow();

        main_panel.addComponent(new EmptySpace());

        Panel exit_panel = new Panel();
        exit_panel.setLayoutManager(new GridLayout(2));

        exit_panel.addComponent(new Button("Сохранить и выйти", new Runnable() {
            @Override
            public void run() {
                f.university = university;
                f.shortname = txtshortname.getText();
                f.name = txtname.getText();
                f.info = txtabout.getText();
                f.links = new ArrayList<String>();
                Scanner sc = new Scanner(txtlinks.getText());
                while (sc.hasNextLine())
                {
                    f.links.add(new String(sc.nextLine()));
                }
                b = true;
                gui.removeWindow(window);
                Main.save();
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
