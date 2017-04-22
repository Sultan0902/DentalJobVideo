package com.vishant.DentalJobVideo.utils.QB.configs;

import com.google.gson.Gson;
import com.quickblox.sample.core.utils.configs.ConfigParser;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;
import com.vishant.DentalJobVideo.model.QB.ChatConfigs;

import java.io.IOException;

public class ConfigUtils extends CoreConfigUtils {

    public static ChatConfigs getSampleConfigs(String fileName) throws IOException {
        ConfigParser configParser = new ConfigParser();
        Gson gson = new Gson();
        return gson.fromJson(configParser.getConfigsAsJsonString(fileName), ChatConfigs.class);
    }
}
