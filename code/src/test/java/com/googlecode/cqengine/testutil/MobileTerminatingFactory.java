package com.googlecode.cqengine.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MobileTerminatingFactory {

    public static Collection<MobileTerminating> getCollectionOfMobileTerminating() {
        
        List<MobileTerminating> mobileTerminating = new ArrayList<>(
            Arrays.asList(
                new MobileTerminating("353", "na", "ireland", "emea"),
                new MobileTerminating("35380", "op1", "ireland", "emea"),
                new MobileTerminating("35381", "op2", "ireland", "emea"),
                new MobileTerminating("35382", "op3", "ireland", "emea"),
                new MobileTerminating("353822", "op4", "ireland", "emea"),
                new MobileTerminating("35387", "op5", "ireland", "emea"),
                new MobileTerminating("35387123", "op6", "ireland", "emea"),
                new MobileTerminating("44123", "op7", "uk", "emea"),
                new MobileTerminating("4480", "op8", "uk", "emea"),
                new MobileTerminating("4480", "op9", "uk", "emea2"), //deliberate duplication of codes
                new MobileTerminating("33380", "op10", "france", "emea"),
                new MobileTerminating("33381", "op11", "france", "emea"),
                new MobileTerminating("1234", "op12", "usa", "nar"),
                new MobileTerminating("111", "op13", "usa", "nar")
                ));
        
        
        
        return mobileTerminating;
    }
}
