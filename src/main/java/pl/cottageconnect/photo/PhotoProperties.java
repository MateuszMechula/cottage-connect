package pl.cottageconnect.photo;

import java.util.Map;

class PhotoProperties {
    private static final Integer MAX_USER_PHOTO_VALUE = 1;
    private static final Integer MAX_VILLAGE_PHOTO_VALUE = 1;
    private static final Integer MAX_COTTAGE_PHOTO_VALUE = 5;
    static final Map<PhotoableType, Integer> PHOTO_LIMITS = Map.of(
            PhotoableType.USER, MAX_USER_PHOTO_VALUE,
            PhotoableType.COTTAGE, MAX_COTTAGE_PHOTO_VALUE,
            PhotoableType.VILLAGE, MAX_VILLAGE_PHOTO_VALUE
    );
    static final String PHOTO_URL = "http://localhost:8080/images/";
}
