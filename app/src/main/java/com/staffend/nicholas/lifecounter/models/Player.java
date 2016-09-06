package com.staffend.nicholas.lifecounter.models;

/**
 * Player object
 * Created by Nicholas on 1/1/2016.
 */
public class Player {
    private long id;
    private String name;

    /**
     * Get ID number fo the player
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * SEt the ID number fo the player
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * SEts a new name for this player
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * gets a new name for this player
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * REturn the name. ToString() override for use by generic Android classes/methods
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Override equals to check equality of 2 players and allow for Collections class to compare objects
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return getId() == player.getId();

    }


    /**
     * Override to check equality of 2 players and allow for Collections class to compare objects
     * @return
     */
    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
