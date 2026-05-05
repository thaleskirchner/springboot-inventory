package com.thaleskirchner.inventory.entities.enums;

public enum MovementType {
    ENTRY(1),
    EXIT(2),
    ADJUSTMENT(3),
    RETURN(4);

    private int code;

    private MovementType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MovementType valueOf(int code) {
        for (MovementType value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new IllegalArgumentException("Invalid MovementType code: " + code);
    }
}
