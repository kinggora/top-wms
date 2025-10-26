package com.top.effitopia.mapper;

import com.top.effitopia.domain.Contract;
import com.top.effitopia.domain.WarehouseCost;
import com.top.effitopia.dto.ContractDTO;
import com.top.effitopia.dto.PageRequestDTO;
import com.top.effitopia.dto.WarehouseCostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper {
    int insert(Contract contract);
    List<Contract> selectListAll(PageRequestDTO pageRequestDTO);
    List<Contract> selectListByStatus(PageRequestDTO pageRequestDTO);
    int update(Contract contract);
    boolean updateApprovalList(List<Integer> id);
    boolean updateRejectList(List<Integer> id);
    int getCount(PageRequestDTO pageRequestDTO);
    WarehouseCost getOne(Integer id);
    Integer checkMember(String name);
    Integer selectUserId(String name);
}
