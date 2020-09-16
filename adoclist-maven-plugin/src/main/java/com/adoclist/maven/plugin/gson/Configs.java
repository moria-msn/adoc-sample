
package com.adoclist.maven.plugin.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configs {

    @SerializedName("configs")
    @Expose
    private List<Config> configs = null;

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

}
