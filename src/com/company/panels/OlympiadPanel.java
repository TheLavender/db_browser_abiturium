package com.company.panels;


import com.company.classes.Subject;
import com.company.classes.OlympEvent;
import com.company.classes.Olympiad;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class OlympiadPanel {

    public MultiWindowTextGUI gui;
    public Olympiad o;
    public boolean b;

    public OlympiadPanel(MultiWindowTextGUI gui, Olympiad o)
    {
        this.gui = gui;
        this.o = o;
        this.b = true;
    }

    public void refresh(ComboBox<String> combo, String name)
    {
        combo.clearItems();
        combo.addItem("---Выберите---");
        for (int i = 0; i < o.events.size(); ++i)
        {
            combo.addItem(o.events.get(i).toString());
            if (o.events.get(i).toString().equals(name))
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
        txtshortname.setText(o.shortname);
        main_panel.addComponent(txtshortname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Название"));
        TextBox txtname = new TextBox(new TerminalSize(30, 1));
        txtname.setText(o.name);
        main_panel.addComponent(txtname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Описание"));
        TextBox txtabout = new TextBox(new TerminalSize(30, 3));
        txtabout.setText(o.info);
        main_panel.addComponent(txtabout);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Ссылки"));
        TextBox txtlinks = new TextBox(new TerminalSize(30, 4));
        StringBuffer links = new StringBuffer("");
        for (int i = 0; i < o.links.size(); ++i)
        {
            if (i != 0) links.append("\n");
            links.append(o.links.get(i));
        }
        txtlinks.setText(String.valueOf(links));
        main_panel.addComponent(txtlinks);

        main_panel.addComponent(new EmptySpace());
        main_panel.addComponent(new Label("События"));

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
                o.events.remove(comboE.getSelectedIndex() - 1);
                refresh(comboE, "---Выберите---");
            }
        }));

        additional_panel.addComponent(new Button("Перейти", new Runnable() {
            @Override
            public void run() {
                if (comboE.getSelectedIndex() == 0)
                {
                    return;
                }
                OlympEvent e = o.events.get(comboE.getSelectedIndex() - 1);
                OlympEventPanel olympEventPanel = new OlympEventPanel(gui, e);
                olympEventPanel.process();
                refresh(comboE, e.name);
                Main.save();
            }
        }));


        main_panel.addComponent(new Button("Добавить событие", new Runnable() {
            @Override
            public void run() {
                OlympEvent e = new OlympEvent();
                OlympEventPanel olympEventPanel = new OlympEventPanel(gui, e);
                olympEventPanel.process();
                if (olympEventPanel.b)
                {
                    o.events.add(e);
                    refresh(comboE, e.name);
                    Main.save();
                }
            }
        }));

        main_panel.addComponent(new EmptySpace());
        main_panel.addComponent(new Label("Предметы"));
        CheckBoxList<Subject> checkS = new CheckBoxList<Subject>(new TerminalSize(30, 8));
        ArrayList<Subject> sub = new ArrayList<Subject>(Arrays.asList(Subject.values()));
        for (int i = 0; i < sub.size(); ++i)
        {
            checkS.addItem(sub.get(i));
        }
        for (int i = 0; i < o.subjects.size(); ++i)
        {
            checkS.setChecked(o.subjects.get(i), true);
        }
        main_panel.addComponent(checkS);

        //ComboBox <String> comboS = new ComboBox<String>();
        //refresh(comboS, "---Выберите---");
        //main_panel.addComponent(comboS);


        BasicWindow window = new BasicWindow();

        main_panel.addComponent(new EmptySpace());

        Panel exit_panel = new Panel();
        exit_panel.setLayoutManager(new GridLayout(2));

        exit_panel.addComponent(new Button("Сохранить и выйти", new Runnable() {
            @Override
            public void run() {
                o.shortname = txtshortname.getText();
                o.name = txtname.getText();
                o.info = txtabout.getText();
                o.links = new ArrayList<String>();
                o.subjects = new ArrayList<Subject>();
                Scanner sc = new Scanner(txtlinks.getText());
                while (sc.hasNextLine())
                {
                    o.links.add(new String(sc.nextLine()));
                }
                ArrayList<Subject> result = new ArrayList<Subject>(checkS.getCheckedItems());
                for (int i = 0; i < result.size(); ++i)
                {
                    for (int j = 0; j < sub.size(); ++j)
                    {
                        if (result.get(i) == sub.get(j))
                        {
                            o.subjects.add(sub.get(j));
                        }
                    }
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
