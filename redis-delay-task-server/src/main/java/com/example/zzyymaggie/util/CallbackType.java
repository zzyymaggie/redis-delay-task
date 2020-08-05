package com.example.zzyymaggie.util;

public enum CallbackType {
    HTTP(0, "http"),

    TARSRPC(1, "tarsrpc");

    int type;
    String value;
    CallbackType(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public static CallbackType fromType(int type) {
        switch (type) {
            case 0:
                return HTTP;
            case 1:
                return TARSRPC;
        }
        throw new IllegalArgumentException(type + " can not map enum");
    }

    /**
     * 回调类型是否合规
     * @param type
     * @return
     */
    public static boolean isCorrectType(int type) {
        boolean isPass = false;
        for (int i = 0; i < CallbackType.values().length; i++) {
            if (type == CallbackType.values()[i].type) {
                isPass = true;
            }
        }
        return isPass;
    }
}
