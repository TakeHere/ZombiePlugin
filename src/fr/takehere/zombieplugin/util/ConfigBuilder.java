package fr.takehere.zombieplugin.util;

import fr.takehere.zombieplugin.PluginMain;


import java.util.List;

public class ConfigBuilder {
    public FileManager configs;
    public static ConfigBuilder instance;

    public ConfigBuilder(FileManager fileManager) {
        this.configs = fileManager;
        instance = this;
    }

    public String getString(String path, String config){
        return configs.getConfig(config).get().getString(path).replaceAll("&","§");
    }

    public int getInt(String path, String config){
        return configs.getConfig(config).get().getInt(path);
    }

    public double getDouble(String path, String config) {
        return configs.getConfig(config).get().getDouble(path);
    }

    public double getDouble(String path) {
        return PluginMain.getInstance().getConfig().getDouble(path);
    }

    public Boolean getBoolean(String path, String config){
        return configs.getConfig(config).get().getBoolean(path);
    }

    public void setString(String path, String data, String config){
        configs.getConfig(config).set(path, data);
        configs.getConfig(config).save();
    }

    public void setInt(String path, int data, String config){
        configs.getConfig(config).set(path, data);
        configs.getConfig(config).save();
        configs.getConfig(config).reload();
    }

    public void setBoolean(String path, boolean data, String config){
        configs.getConfig(config).set(path, data);
        configs.getConfig(config).save();
    }

    public void setDouble(String path, double data, String config){
        configs.getConfig(config).set(path, data);
        configs.getConfig(config).save();
    }

    public String getString(String path){
        return PluginMain.getInstance().getConfig().getString(path).replaceAll("&","§");
    }

    public int getInt(String path){
        return PluginMain.getInstance().getConfig().getInt(path);
    }

    public boolean getBoolean(String path){
        return PluginMain.getInstance().getConfig().getBoolean(path);
    }

    public void set(String path, Object data){
        PluginMain.getInstance().getConfig().set(path, data);
        PluginMain.getInstance().saveConfig();
        PluginMain.getInstance().reloadConfig();
    }

    public List<?> getList(String path){
        return PluginMain.getInstance().getConfig().getList(path);
    }

    public List<?> getList(String path, String config){
        return configs.getConfig(config).get().getList(path);
    }

    public List<String> getListString(String path){
        return (List<String>) PluginMain.getInstance().getConfig().getList(path);
    }

    public List<String> getListString(String path, String config){
        return (List<String>) configs.getConfig(config).get().getList(path);
    }

    public List<Integer> getListInt(String path){
        return (List<Integer>) PluginMain.getInstance().getConfig().getList(path);
    }

    public List<Integer> getListInt(String path, String config){
        return (List<Integer>) configs.getConfig(config).get().getList(path);
    }
}
