package com.coffeester.ticketing.domain;

/**
 * Created by amitsehgal on 1/30/16.
 */
public enum Level {

    ORCHESTRA(1),
    main(2),
    BALCONY_1(3),
    BALCONY_2(4);

    private int level;

    private Level(int l) {
        level = l;
    }

    public int getLevel() {
        return level;
    }

}
