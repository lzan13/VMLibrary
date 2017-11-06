package com.vmloft.develop.library.example.http;

import org.json.JSONArray;

/**
 * Created by lzan13 on 2017/7/13.
 * Subscription account entity
 */
public class EMSubscription {
    private String paid = null;
    private String name = null;
    private String descripation = null;
    private String logo = null;
    private String agentUser = null;
    private JSONArray menu = null;

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripation() {
        return descripation;
    }

    public void setDescripation(String descripation) {
        this.descripation = descripation;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAgentUser() {
        return agentUser;
    }

    public void setAgentUser(String agentUser) {
        this.agentUser = agentUser;
    }

    public JSONArray getMenu() {
        return menu;
    }

    public void setMenu(JSONArray menu) {
        this.menu = menu;
    }
}
