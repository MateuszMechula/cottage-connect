package pl.cottageconnect.util;

import pl.cottageconnect.owner.Owner;
import pl.cottageconnect.owner.OwnerEntity;

public class TestDataFactoryOwner {

    public static Owner testOwner() {
        return Owner.builder()
                .ownerId(1L)
                .firstname("Jan")
                .lastname("Kowalski")
                .phone("505404330")
                .userId(1)
                .build();
    }

    public static OwnerEntity testOwnerEntity() {
        return OwnerEntity.builder()
                .ownerId(1L)
                .firstname("Jan")
                .lastname("Kowalski")
                .phone("505404330")
                .userId(1)
                .build();
    }
}
