package com.excuseme.rocketleaguelivestats.scanner.matcher;

public interface LineMatcher<T> {

    T match(String line);
}
