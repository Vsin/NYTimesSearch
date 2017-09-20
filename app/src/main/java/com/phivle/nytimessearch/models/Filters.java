package com.phivle.nytimessearch.models;

/**
 * Created by Vsin on 9/19/17.
 */

public class Filters {
    private static String mBeginDateFilter;
    private static String mSortOrder;
    private static String[] mNewsDesk;

    public static String getBeginDateFilter() {
        return mBeginDateFilter;
    }

    public static String getSortOrder() {
        return mSortOrder;
    }

    public static String getNewsDesk() {
        return "";
    }

    public static void setBeginDateFilter(String mBeginDateFilter) {
        Filters.mBeginDateFilter = mBeginDateFilter;
    }

    public static void setSortOrder(String mSortOrder) {
        Filters.mSortOrder = mSortOrder;
    }

    public static void setNewsDesk(String mNewsDesk) {
    }
}
