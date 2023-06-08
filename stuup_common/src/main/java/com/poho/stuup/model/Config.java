package com.poho.stuup.model;

import javax.validation.constraints.NotBlank;

public class Config {

    @NotBlank(message = "配置键不能为空")
    private String configKey;

    @NotBlank(message = "配置值不能为空")
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