package com.egyetem.szakdolgozat.ranking.serialization;

public enum RankEnum {
    IRON((short)0),
    BRONZE((short)1),
    SILVER((short)2),
    GOLD((short)3),
    PLATINUM((short)4),
    DIAMOND((short)5),
    MASTER((short)6),
    GRANDMASTER((short)7),
    CHALLENGER((short)8);

    private final short value;

    private RankEnum(short value) {
        this.value = value;
    }

    public short getValue(){
        return value;
    }
}
