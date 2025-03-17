package com.aixming.bestoj.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AixMing
 * @since 2025-03-17 17:44:57
 */
@Getter
public enum QuestionSubmitEnum {
    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    WAITING("待判题", 0),
    JUDGING("判题中", 1),
    SUCCESS("成功", 2),
    FAILED("失败", 3);

    private final String text;

    private final Integer value;

    QuestionSubmitEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有值
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(QuestionSubmitEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据值获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitEnum anEnum : values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
