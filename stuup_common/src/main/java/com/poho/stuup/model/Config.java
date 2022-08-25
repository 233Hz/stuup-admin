package com.poho.stuup.model;

public class Config {
    private String configKey;

    private String configValue;

    private String configNote;

    private Integer configYear;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey == null ? null : configKey.trim();
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue == null ? null : configValue.trim();
    }

    public String getConfigNote() {
        return configNote;
    }

    public void setConfigNote(String configNote) {
        this.configNote = configNote == null ? null : configNote.trim();
    }

    public Integer getConfigYear() {
        return configYear;
    }

    public void setConfigYear(Integer configYear) {
        this.configYear = configYear;
    }
}