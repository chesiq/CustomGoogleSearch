package ua.antonk.googlecustomsearch.database.entities;

import java.util.List;

/**
 * Created by Anton on 01.08.2015.
 */
public class SearchResult {

    private List<Item> items;
    private String error;

    public List<Item> getItems() {
        return items;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
