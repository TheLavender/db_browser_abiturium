package com.company.classes;


import com.company.classes.DataB;
import com.company.panels.Main;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

public class SetNewDataBase {
    public String res = null;

    public SetNewDataBase(String res)
    {
        this.res = res;
    }

    public void main()
    {
        //Connecting to database and configuring it
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(DataB.class).cascadeOnDelete(true);
        config.common().objectClass(DataB.class).cascadeOnUpdate(true);
        config.common().objectClass(DataB.class).cascadeOnActivate(true);
        ObjectContainer db4objects = Db4oEmbedded.openFile(config, res);

        DataB db = new DataB();

        db4objects.store(db);

        db4objects.commit();
        db4objects.close();
        return;
    }
}
