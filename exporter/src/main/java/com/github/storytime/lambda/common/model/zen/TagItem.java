package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TagItem implements Serializable {

    private String parent;
    private long color;
    private boolean budgetOutcome;
    private boolean showIncome;
    private String icon;
    private String title;
    private boolean required;
    private Object picture;
    private boolean budgetIncome;
    private String id;
    private boolean showOutcome;
    private int user;
    private int changed;
}