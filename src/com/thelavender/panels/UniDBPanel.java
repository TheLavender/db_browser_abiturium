package com.thelavender.panels;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.thelavender.abiturium_utils.classes.DataB;
import com.thelavender.abiturium_utils.classes.Olympiad;
import com.thelavender.abiturium_utils.classes.University;

import java.util.ArrayList;

public class UniDBPanel {
    public MultiWindowTextGUI gui;
    public DataB db;
    public ArrayList <Olympiad> olympiads;

    public UniDBPanel(MultiWindowTextGUI gui, DataB db, ArrayList <Olympiad> olympiads)
    {
        this.gui = gui;
        this.db = db;
        this.olympiads = olympiads;

    }

    public void refresh(ComboBox <String> combo, String shortname)
    {
        combo.clearItems();
        combo.addItem("---Выберите---");
        for (int i = 0; i < db.universities.size(); ++i)
        {
            combo.addItem(db.universities.get(i).shortname);
            if (db.universities.get(i).shortname.equals(shortname))
            {
                combo.setSelectedIndex(i + 1);
            }
        }
    }

    public void process(){

        //Setting width of used panels
        int wid = gui.getActiveWindow().getPosition().getColumn() + gui.getActiveWindow().getDecoratedSize().getColumns() + 1;


        // Creating panel that holds everything
        Panel main_panel = new Panel();

        // Setting title
        main_panel.addComponent(new Label("Университеты"));
        main_panel.addComponent(new EmptySpace());

        // Creating comboBox
        ComboBox <String> comboU = new ComboBox<String>();
        refresh(comboU, "---Выберите---");
        //comboU.setPreferredSize(new TerminalSize(25, 1)]);
        main_panel.addComponent(comboU);

        Panel additional_panel = new Panel();
        additional_panel.setLayoutManager(new GridLayout(2));
        main_panel.addComponent(additional_panel);

        additional_panel.addComponent(new Button("Удалить", new Runnable() {
            @Override
            public void run() {
                if (comboU.getSelectedIndex() == 0)
                {
                    return;
                }
                MessageDialogButton del = MessageDialog.showMessageDialog(gui, "", "Вы уверены, что хотите удалить?", MessageDialogButton.No, MessageDialogButton.Yes);
                if (del == MessageDialogButton.No)
                {
                    return;
                }
                db.universities.remove(comboU.getSelectedIndex() - 1);
                refresh(comboU, "---Выберите---");
            }
        }));

        additional_panel.addComponent(new Button("Перейти", new Runnable() {
            @Override
            public void run() {
                if (comboU.getSelectedIndex() == 0)
                {
                    return;
                }

                University cur = db.universities.get(comboU.getSelectedIndex() - 1); // Because of "---Выберите---" string

                UniversityPanel unipanel = new UniversityPanel(gui, cur, olympiads);
                unipanel.process();
                refresh(comboU, cur.shortname);
                Main.save();
            }
        }));


        main_panel.addComponent(new Button("Добавить университет", new Runnable() {
            @Override
            public void run() {

                University cur = new University();

                UniversityPanel universityPanel = new UniversityPanel(gui, cur, olympiads);
                universityPanel.process();
                if (universityPanel.b)
                {
                    db.universities.add(cur);
                    refresh(comboU, cur.shortname);
                    Main.save();
                }
            }
        }));


        BasicWindow window = new BasicWindow();

        main_panel.addComponent(new EmptySpace());
        main_panel.addComponent(new Button("Назад", new Runnable() {
            @Override
            public void run() {
                gui.removeWindow(window);
                return;
            }
        }));

        window.setComponent(main_panel);

        gui.addWindow(window);
        window.setPosition(new TerminalPosition(wid, 1));
        gui.waitForWindowToClose(window);
    }
}
