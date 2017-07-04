package com.company.panels;

import com.company.classes.*;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static ObjectContainer db4objects = null;
    public static DataB db = null;
    public static String res = "";

    public static void main(String[] args) throws IOException {

        //Connecting to database and configuring it
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();

        config.common().objectClass(DataB.class).cascadeOnActivate(true);
        config.common().objectClass(University.class).cascadeOnActivate(true);
        config.common().objectClass(Faculty.class).cascadeOnActivate(true);
        config.common().objectClass(EduProgram.class).cascadeOnActivate(true);
        config.common().objectClass(Olympiad.class).cascadeOnActivate(true);
        config.common().objectClass(OlympEvent.class).cascadeOnActivate(true);
        config.common().activationDepth(6);

        config.common().objectClass(DataB.class).cascadeOnUpdate(true);
        config.common().objectClass(University.class).cascadeOnUpdate(true);
        config.common().objectClass(Faculty.class).cascadeOnUpdate(true);
        config.common().objectClass(EduProgram.class).cascadeOnUpdate(true);
        config.common().objectClass(Olympiad.class).cascadeOnUpdate(true);
        config.common().objectClass(OlympEvent.class).cascadeOnUpdate(true);
        config.common().updateDepth(6);



        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        res = TextInputDialog.showDialog(gui, "Название файла", "Введите название файла:", "");
        if (res.equals("CreateNewDataBase"))
        {
            res = TextInputDialog.showDialog(gui, "Название файла", "Введите название нового файла:", "data.data");
            SetNewDataBase setNewDataBase = new SetNewDataBase(res);
            setNewDataBase.main();
        }
        db4objects = Db4oEmbedded.openFile(config, res);


        // Getting object from the database
        db = db4objects.query(new Predicate<DataB>() {
            @Override
            public boolean match(DataB target) {
                return true;
            }
        }).next();

        System.out.println(db4objects.query(new Predicate<DataB>() {
            @Override
            public boolean match(DataB target) {
                return true;
            }
        }).size());

        // Create panel to hold components
        Panel main_panel = new Panel();

        // Creating menu
        ActionListBox menu = new ActionListBox();
        main_panel.addComponent(menu);

        //Creating window that holds everything
        BasicWindow window = new BasicWindow();

        menu.addItem("Университеты", new Runnable() {
            @Override
            public void run() {
                UniDBPanel udbpanel = new UniDBPanel(gui, db, db.olympiads);
                udbpanel.process();
                db4objects.store(db);
                db4objects.commit();
            }
        });

        menu.addItem("Олимпиады", new Runnable() {
            @Override
            public void run() {
                OlyDBPanel olyDBPanel = new OlyDBPanel(gui, db);
                olyDBPanel.process();
                System.out.println(db.olympiads.size());
                db4objects.store(db);
                db4objects.commit();
            }
        });

        menu.addItem("Прочитайте это", new Runnable() {
            @Override
            public void run() {
                try {
                    MessageDialogButton res = MessageDialog.showMessageDialog(gui, "", new String(Files.readAllBytes(Paths.get("info.txt"))), MessageDialogButton.OK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        menu.addItem("Выход", new Runnable() {
            @Override
            public void run() {
                db4objects.close();
                gui.removeWindow(window);
                System.exit(0);
            }
        });

        window.setComponent(main_panel);
        gui.addWindowAndWait(window);
    }

    public static void save()
    {
        db4objects.store(db);
        db4objects.commit();
    }
}