package com.staffend.nicholas.lifecounter.models;

/**
 * Created by Nicholas on 1/1/2016.
 */
public class Player {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return getId() == player.getId();

    }



    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
