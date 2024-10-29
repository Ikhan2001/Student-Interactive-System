package com.cleducate.entity.courseAssets;

import com.cleducate.entity.BaseEntity;
import com.cleducate.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCourseAssets extends BaseEntity {

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Asset asset;

    private ZonedDateTime downloadDate;

    @ColumnDefault("false")
    private boolean isViewed;
    @ColumnDefault("true")
    private boolean isActive;


}
