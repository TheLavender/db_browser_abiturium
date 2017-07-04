package com.company.panels;

import com.company.classes.Faculty;
import com.company.classes.Olympiad;
import com.company.classes.University;
import com.company.panels.FacultyPanel;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.ArrayList;
import java.util.Scanner;

public class UniversityPanel {

    public MultiWindowTextGUI gui;
    public University u;
    public boolean b;
    public ArrayList <Olympiad> olympiads;

    public UniversityPanel(MultiWindowTextGUI gui, University u, ArrayList <Olympiad> olympiads)
    {
        this.gui = gui;
        this.u = u;
        this.b = true;
        this.olympiads = olympiads;
    }

    public void refresh(ComboBox <String> combo, String shortname)
    {
        combo.clearItems();
        combo.addItem("---Выберите---");
        for (int i = 0; i < u.faculties.size(); ++i)
        {
            combo.addItem(u.faculties.get(i).shortname);
            if (u.faculties.get(i).shortname.equals(shortname))
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
        txtshortname.setText(u.shortname);
        main_panel.addComponent(txtshortname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Название"));
        TextBox txtname = new TextBox(new TerminalSize(30, 1));
        txtname.setText(u.name);
        main_panel.addComponent(txtname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Описание"));
        TextBox txtabout = new TextBox(new TerminalSize(30, 3));
        txtabout.setText(u.about);
        main_panel.addComponent(txtabout);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Ссылки"));
        TextBox txtlinks = new TextBox(new TerminalSize(30, 4));
        StringBuffer links = new StringBuffer("");
        for (int i = 0; i < u.links.size(); ++i)
        {
            if (i != 0) links.append("\n");
            links.append(u.links.get(i));
        }
        txtlinks.setText(String.valueOf(links));
        main_panel.addComponent(txtlinks);

        main_panel.addComponent(new EmptySpace());

        ComboBox <String> comboF = new ComboBox<String>();
        refresh(comboF, "---Выберите---");
        main_panel.addComponent(comboF);


        Panel additional_panel = new Panel();
        additional_panel.setLayoutManager(new GridLayout(2));
        main_panel.addComponent(additional_panel);

        additional_panel.addComponent(new Button("Удалить", new Runnable() {
            @Override
            public void run() {
                if (comboF.getSelectedIndex() == 0)
                {
                    return;
                }
                MessageDialogButton del = MessageDialog.showMessageDialog(gui, "", "Вы уверены, что хотите удалить?", MessageDialogButton.No, MessageDialogButton.Yes);
                if (del == MessageDialogButton.No)
                {
                    return;
                }
                u.faculties.remove(comboF.getSelectedIndex() - 1);
                refresh(comboF, "---Выберите---");
            }
        }));

        additional_panel.addComponent(new Button("Перейти", new Runnable() {
            @Override
            public void run() {
                if (comboF.getSelectedIndex() == 0)
                {
                    return;
                }
                Faculty f = u.faculties.get(comboF.getSelectedIndex() - 1);
                FacultyPanel F = new FacultyPanel(gui, f, olympiads);
                F.process();
                refresh(comboF, f.shortname);
                Main.save();
            }
        }));


        main_panel.addComponent(new Button("Добавить факультет", new Runnable() {
            @Override
            public void run() {
                Faculty f = new Faculty();
                FacultyPanel facultyPanel = new FacultyPanel(gui, f, olympiads);
                facultyPanel.process();
                if (facultyPanel.b)
                {
                    u.faculties.add(f);
                    refresh(comboF, f.shortname);
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
                u.shortname = txtshortname.getText();
                u.name = txtname.getText();
                u.about = txtabout.getText();
                u.links = new ArrayList<String>();
                Scanner sc = new Scanner(txtlinks.getText());
                while (sc.hasNextLine())
                {
                    u.links.add(new String(sc.nextLine()));
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