package com.qiguangit.unitool.util;

import org.junit.Test;

public class SharedPreferencesTest {
    @Test
    public void sharedPreferencesTest() {
        SharedPreferences sp = SharedPreferences.getSharedPreferences("testSp");
        final SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("aa", false);
        sp.edit()
                .putString("str", "str str str 中文")
                .putInt("int", 10)
                .putLong("long", 100000L)
                .putFloat("float", 10.2F)
                .putBoolean("bool", false)
                .commit();
        final String str = sp.getString("str", "");
        System.out.println(str);
        sp.getAll().forEach((k, v) -> {
            System.out.println("key:" + k + " value:" + v + " value type:" + v.getClass().getSimpleName());
        });

        edit.remove("int");
        edit.commit();

        System.out.println("\r\n");
        sp.getAll().forEach((k, v) -> {
            System.out.println("key:" + k + " value:" + v + " value type:" + v.getClass().getSimpleName());
        });
    }

    @Test
    public void clearTest() {
        SharedPreferences sp = SharedPreferences.getSharedPreferences("testSp");
        sp.edit().clear().commit();
    }
}
