package com.top.effitopia.domain;

import com.top.effitopia.enumeration.MemberRole;
import com.top.effitopia.enumeration.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private MemberStatus status;
    private MemberRole role;
    private Address address;
    private String businessNumber;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public Member(int id) {
        this.id = id;
    }


}
