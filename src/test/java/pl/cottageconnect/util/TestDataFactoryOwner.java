package pl.cottageconnect.util;

import pl.cottageconnect.owner.domain.Owner;

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
}
