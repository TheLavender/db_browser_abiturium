package com.thelavender.panels;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.thelavender.abiturium_utils.classes.DataB;
import com.thelavender.abiturium_utils.classes.Olympiad;

public class OlyDBPanel {
    public MultiWindowTextGUI gui;
    public DataB db;

    public OlyDBPanel(MultiWindowTextGUI gui, DataB db)
    {
        this.gui = gui;
        this.db = db;
    }

    public void refresh(ComboBox<String> combo, String shortname)
    {
        combo.clearItems();
        combo.addItem("---Выберите---");
        for (int i = 0; i < db.olympiads.size(); ++i)
        {
            combo.addItem(db.olympiads.get(i).shortname);
            if (db.olympiads.get(i).shortname.equals(shortname))
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
        main_panel.addComponent(new Label("Олимпиады"));
        main_panel.addComponent(new EmptySpace());

        // Creating comboBox
        ComboBox <String> comboO = new ComboBox<String>();
        refresh(comboO, "---Выберите---");
        //comboU.setPreferredSize(new TerminalSize(25, 1)]);
        main_panel.addComponent(comboO);


        Panel additional_panel = new Panel();
        additional_panel.setLayoutManager(new GridLayout(2));

        additional_panel.addComponent(new Button("Удалить", new Runnable() {
            @Override
            public void run() {
                if (comboO.getSelectedIndex() == 0)
                {
                    return;
                }
                MessageDialogButton del = MessageDialog.showMessageDialog(gui, "", "Вы уверены, что хотите это сделать?", MessageDialogButton.No, MessageDialogButton.Yes);
                if (del == MessageDialogButton.No)
                {
                    return;
                }
                db.olympiads.remove(comboO.getSelectedIndex() - 1);
                refresh(comboO, "---Выберите---");
            }
        }));

        additional_panel.addComponent(new Button("Перейти", new Runnable() {
            @Override
            public void run() {
                if (comboO.getSelectedIndex() == 0)
                {
                    return;
                }

                Olympiad cur = db.olympiads.get(comboO.getSelectedIndex() - 1); // Because of "---Выберите---" string

                OlympiadPanel olympiadPanel = new OlympiadPanel(gui, cur);
                olympiadPanel.process();
                refresh(comboO, cur.shortname);
                Main.save();
            }
        }));

        main_panel.addComponent(additional_panel);

        main_panel.addComponent(new Button("Добавить олимпиаду", new Runnable() {
            @Override
            public void run() {

                Olympiad cur = new Olympiad();

                OlympiadPanel olympiadPanel = new OlympiadPanel(gui, cur);
                olympiadPanel.process();
                if (olympiadPanel.b)
                {
                    db.olympiads.add(cur);
                    refresh(comboO, cur.shortname);
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
