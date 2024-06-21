package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FacilityName {
    NONE("-"),
    JUICE_WRAPPER_1("즙 포장기1"),
    JUICE_WRAPPER_2("즙 포장기2"),
    JELLY_WRAPPER_1("스틱 포장기1"),
    JELLY_WRAPPER_2("스틱 포장기2"),
    EXTRACTOR_1("추출기1"),
    EXTRACTOR_2("추출기2"),
    STERILIZER_1("살균기1"),
    STERILIZER_2("살균기2"),
    MIXER("혼합기"),
    FILTER("여과기"),
    BOX_WRAPPER("Box 포장기"),
    DETECTOR("금속검출기");

    private final String description;
}
