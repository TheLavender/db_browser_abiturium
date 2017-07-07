package com.thelavender.classes;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.thelavender.abiturium_utils.classes.DataB;

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
