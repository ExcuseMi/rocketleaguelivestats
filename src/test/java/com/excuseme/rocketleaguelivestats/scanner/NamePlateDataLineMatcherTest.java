package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.scanner.matcher.NamePlateDataLineMatcher;
import com.excuseme.rocketleaguelivestats.scanner.model.NamePlate;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamePlateDataLineMatcherTest {

    private final NamePlateDataLineMatcher namePlateDataLineMatcher = new NamePlateDataLineMatcher();
    @Test
    public void testMatch() throws Exception {
        NamePlate namePlate = namePlateDataLineMatcher.match("[0051.72] Nameplate: RefreshNameplates NameplateData Excuse Me Row=0");
        assertNotNull(namePlate);
        assertEquals("Excuse Me", namePlate.getName());
        assertEquals(0, namePlate.getRow());
        namePlate = namePlateDataLineMatcher.match("[0092.43] Nameplate: RefreshNameplates NameplateData bo-kw-saleen Row=3");
        assertNotNull(namePlate);
        assertEquals("bo-kw-saleen", namePlate.getName());
        assertEquals(3, namePlate.getRow());
    }
}