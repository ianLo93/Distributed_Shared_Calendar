package com.project1.server;

import java.util.Comparator;

public class MeetingCompare implements Comparator<Meeting> {

    public int compare(Meeting m1, Meeting m2) {
        return m1.getName().compareTo(m2.getName());
    }
}
