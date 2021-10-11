package jp.choqi.crawltool.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "ad_address")
public class AdAddress implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "town_id")
    private Long townId;

    @Column(name = "zip")
    private String zip;

    @Column(name = "office_flg")
    private Boolean officeFlg;

    @Column(name = "delete_flg")
    private Boolean deleteFlg;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "city_furi")
    private String cityFuri;

    @Column(name = "town_name")
    private String townName;

    @Column(name = "town_furi")
    private String townFuri;

    @Column(name = "town_memo")
    private String townMemo;

    @Column(name = "kyoto_street")
    private String kyotoStreet;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_furi")
    private String blockFuri;

    @Column(name = "memo")
    private String memo;

    @Column(name = "office_name")
    private String officeName;

    @Column(name = "office_furi")
    private String officeFuri;

    @Column(name = "office_address")
    private String officeAddress;

    @Column(name = "new_id")
    private String newId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pref_id")
    private Prefecture prefecture;
}
