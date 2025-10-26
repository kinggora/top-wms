package com.top.effitopia.service;

import com.top.effitopia.domain.Contract;
import com.top.effitopia.dto.*;

import java.util.List;

public interface ContractService {
    boolean save(ContractDTO contractDTO);
    PageResponseDTO<ContractDTO> getListAll(PageRequestDTO<ContractDTO> pageRequestDTO);
    PageResponseDTO<ContractDTO> getListByStatus(PageRequestDTO<ContractDTO>  pageRequestDTO);
    boolean modify(ContractDTO contractDTO);
    boolean modifyApprovalList(List<Integer> id);
    boolean modifyRejectList(List<Integer> id);
    Contract changedVO(ContractDTO contractDTO);
    WarehouseCostDTO get(Integer id);
    Integer check(String name);
    Integer getUserId(String name);
}
