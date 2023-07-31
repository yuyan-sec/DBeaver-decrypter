package me.gv7.woodpecker.plugin;

import me.gv7.woodpecker.helper.DBeaverPasswdDcrypter;

public class WoodpeckerPluginManager implements IPluginManager {
    public void registerPluginManagerCallbacks(IPluginManagerCallbacks iPluginManagerCallbacks) {
        DBeaverPasswdDcrypter echoTextConverter = new DBeaverPasswdDcrypter();
        iPluginManagerCallbacks.registerHelperPlugin(echoTextConverter);
    }
}
