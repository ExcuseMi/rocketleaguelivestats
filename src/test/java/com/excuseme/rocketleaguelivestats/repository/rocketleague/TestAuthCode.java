package com.excuseme.rocketleaguelivestats.repository.rocketleague;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TestAuthCode {

    @Test
    public void findPattern() throws IOException {
        final List<String> lines = IOUtils.readLines(Thread.currentThread().getContextClassLoader().getResourceAsStream("auth_codes.txt"));
        final List<String> patterns = IOUtils.readLines(Thread.currentThread().getContextClassLoader().getResourceAsStream("auth_code_pattern.txt"));

        final int max = lines.stream().mapToInt(String::length).max().getAsInt();

        final StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i<max; i++) {
            final int index = i;
            final Set<Character> set = lines.stream().map(l -> l.charAt(index)).collect(Collectors.toSet());
            if(set.size() == 1) {
                stringBuilder.append(set.iterator().next());
            } else {
                stringBuilder.append("x");
            }
        }
        assertEquals(patterns.get(0), stringBuilder.toString());
    }

    @Test
    public void hex() {
        System.out.println(Hex.encodeHexString("160212.65765.127596".getBytes()));
    }

    @Test
    public void unhex() throws DecoderException {
        System.out.println(Hex.decodeHex("2858B5391813211F".toCharArray()));
    }
}
