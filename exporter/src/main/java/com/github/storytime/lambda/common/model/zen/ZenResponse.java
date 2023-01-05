package com.github.storytime.lambda.common.model.zen;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder(toBuilder = true)
public class ZenResponse implements Serializable {
    private long serverTimestamp;
    private List<CountryItem> country;
    private List<ReminderMarkerItem> reminderMarker;
    private List<ReminderItem> reminder;
    private List<MerchantItem> merchant;
    private List<InstrumentItem> instrument;
    private List<CompanyItem> company;
    private List<TagItem> tag;
    private List<UserItem> user;
    private List<AccountItem> account;
    private List<TransactionItem> transaction;
    private List<BudgetItem> budget;
}