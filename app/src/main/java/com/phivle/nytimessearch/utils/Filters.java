package com.phivle.nytimessearch.utils;

import java.util.Iterator;
import java.util.Set;

public class Filters {
    public static String formatNewsDesk(Set<String> newsDesk) {
        StringBuilder formattedNewsDesk = new StringBuilder();
        if (!newsDesk.isEmpty()) {
            formattedNewsDesk.append("news_desk:(");
            Iterator<String> iterator = newsDesk.iterator();
            while(iterator.hasNext()) {
                formattedNewsDesk.append(iterator.next());
                if (iterator.hasNext()) {
                    formattedNewsDesk.append(" ");
                }
            }
            formattedNewsDesk.append(")");
        }
        return formattedNewsDesk.toString();
    }
}
