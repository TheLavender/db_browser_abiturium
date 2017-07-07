package com.thelavender.panels;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.thelavender.abiturium_utils.classes.OlympEvent;
import com.thelavender.abiturium_utils.classes.Olympiad;
import com.thelavender.abiturium_utils.enums.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OlympEventPanel {

    public MultiWindowTextGUI gui;
    public OlympEvent e;
    public boolean b;
    public Olympiad olympiad;

    public OlympEventPanel(MultiWindowTextGUI gui, OlympEvent e, Olympiad olympiad)
    {
        this.olympiad = olympiad;
        this.gui = gui;
        this.e = e;
        this.b = true;
    }

    public void process()
    {
        int wid = gui.getActiveWindow().getPosition().getColumn() + gui.getActiveWindow().getDecoratedSize().getColumns() + 1;


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        sdf.setLenient(false);

        Panel main_panel = new Panel();
        main_panel.addComponent(new Label("Название"));
        TextBox txtname = new TextBox(new TerminalSize(30, 1));
        txtname.setText(e.name);
        main_panel.addComponent(txtname);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Описание"));
        TextBox txtinfo = new TextBox(new TerminalSize(30, 3));
        txtinfo.setText(e.info);
        main_panel.addComponent(txtinfo);

        main_panel.addComponent(new EmptySpace());

        main_panel.addComponent(new Label("Дата"));
        RadioBoxList <String> radioI = new RadioBoxList<String>();
        radioI.addItem("Единовременное");
        radioI.addItem("Период");
        radioI.setCheckedItemIndex(0);
        main_panel.addComponent(radioI);
        main_panel.addComponent(new EmptySpace());
        Panel tour_panel = new Panel();
        main_panel.addComponent(tour_panel);
        tour_panel.setLayoutManager(new GridLayout(2));
        TextBox txtbegin = new TextBox(new TerminalSize(14, 1));
        TextBox txtend = new TextBox(new TerminalSize(14, 1));
        tour_panel.addComponent(txtbegin);
        tour_panel.addComponent(txtend);
        if (e.begin != null)
            txtbegin.setText(sdf.format(e.begin.getTime()));
        else
            txtbegin.setText("01.01.1970");
        if (e.end != null)
            txtend.setText(sdf.format(e.end.getTime()));
        else
            txtend.setText("01.01.1970");

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


        RadioBoxList<Category> radioC = new RadioBoxList<Category>();
        ArrayList<Category> categories = new ArrayList<Category>(Arrays.asList(Category.values()));
        for (int i = 0; i < categories.size(); ++i)
        {
            radioC.addItem(categories.get(i));
            if (i == 0)
            {
                radioC.setCheckedItemIndex(0);
            }
            if (categories.get(i) == e.cat)
            {
                radioC.setCheckedItem(e.cat);
            }
        }
        main_panel.addComponent(radioC);

        //ComboBox <String> comboS = new ComboBox<String>();
        //refresh(comboS, "---Выберите---");
        //main_panel.addComponent(comboS);


        BasicWindow window = new BasicWindow();

        main_panel.addComponent(new EmptySpace());

        Panel exit_panel = new Panel();
        exit_panel.setLayoutManager(new GridLayout(2));

        Panel panel = exit_panel.addComponent(new Button("Сохранить и выйти", new Runnable() {
            @Override
            public void run() {
                try {
                    Calendar buf = Calendar.getInstance();
                    buf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                    buf.setTime(sdf.parse(txtbegin.getText()));
                    e.begin = buf;
                    if (radioI.getCheckedItem().equals("Период"))
                    {
                        buf = Calendar.getInstance();
                        buf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                        buf.setTime(sdf.parse(txtend.getText()));
                        e.end = buf;
                    }
                } catch (ParseException e1) {
                    MessageDialogButton err = MessageDialog.showMessageDialog(gui, "", "Проверьте формат даты: DD.MM.YYYY", MessageDialogButton.OK);
                    return;
                }
                e.olympiad = olympiad;
                e.name = txtname.getText();
                e.info = txtinfo.getText();
                e.links = new ArrayList<String>();
                Scanner sc = new Scanner(txtlinks.getText());
                while (sc.hasNextLine()) {
                    e.links.add(sc.nextLine());
                }
                e.immediate = !radioI.getCheckedItem().equals("Период");
                e.cat = radioC.getCheckedItem();
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
