package com.santechture;

import java.util.HashMap;
import java.util.Map;

public enum Regulator {
    BOTH(0),
    HAAD(1),
    DHA(2),
    WHO(3),
    RIAYATI(4);
    private final int val;
    Regulator(int val) {
        this.val = val;
    }
    public int getValue() {
        return val;
    }
    private static final Map<Integer, Regulator> _map = new HashMap<Integer, Regulator>();
    static {
        for (Regulator codeType : Regulator.values()) {
            _map.put(codeType.val, codeType);
        }
    }
    public static Regulator from(int value) {
        return _map.get(value);
    }
}
