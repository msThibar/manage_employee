package com.example.demo.Enum;

public enum Week {
    Mon(0), Tue(1), Wed(2), Thu(3), Fri(4), Sat(5), Sun(6);
    private int startWeek;

    Week(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getStartWeek() {
        return startWeek;
    }

}
