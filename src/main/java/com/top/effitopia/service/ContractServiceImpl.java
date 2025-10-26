package com.top.effitopia.service;

import com.top.effitopia.domain.Contract;
import com.top.effitopia.domain.Member;
import com.top.effitopia.domain.Warehouse;
import com.top.effitopia.domain.WarehouseCost;
import com.top.effitopia.dto.*;
import com.top.effitopia.mapper.ContractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService{

    private final ModelMapper modelMapper;

    private final ContractMapper contractMapper;

    @Override
    public boolean save(ContractDTO contractDTO) {
        Contract contract = changedVO(contractDTO);
        return contractMapper.insert(contract) > 0;
    }

    @Override
    public PageResponseDTO<ContractDTO> getListAll(PageRequestDTO<ContractDTO> pageRequestDTO) {
        List<Contract> contractList = contractMapper.selectListAll(pageRequestDTO);
        List<ContractDTO> contractDTOList = changedListDTO(contractList);
        int total = contractMapper.getCount(pageRequestDTO);
        PageResponseDTO pageResponseDTO = PageResponseDTO
                .<ContractDTO>withAll()
                .dtoList(contractDTOList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public PageResponseDTO getListByStatus(PageRequestDTO pageRequestDTO) {
        List<Contract> contractList = contractMapper.selectListByStatus(pageRequestDTO);
        List<ContractDTO> contractDTOList = contractList.stream().map(vo->modelMapper.map(vo, ContractDTO.class)).collect(Collectors.toList());
        int total = contractMapper.getCount(pageRequestDTO);
        PageResponseDTO pageResponseDTO = PageResponseDTO
                .<ContractDTO>withAll()
                .dtoList(contractDTOList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public boolean modify(ContractDTO contractDTO) {
        Contract contract = modelMapper.map(contractDTO, Contract.class);
        return contractMapper.update(contract) > 0;
    }

    @Override
    public boolean modifyApprovalList(List<Integer> id) {
        boolean complete = contractMapper.updateApprovalList(id);
        return complete;
    }

    @Override
    public boolean modifyRejectList(List<Integer> id) {
        boolean complete = contractMapper.updateRejectList(id);
        return complete;
    }

    @Override
    public Contract changedVO(ContractDTO contractDTO) {
        Warehouse warehouse = modelMapper.map((contractDTO.getWarehouseDTO()), Warehouse.class);
        Member member = modelMapper.map((contractDTO.getMemberDTO()), Member.class);

        return new Contract(contractDTO.getId(),
                warehouse,
                member,
                contractDTO.getStatus(),
                contractDTO.getStartDate(),
                contractDTO.getDate(),
                contractDTO.getEndDate(),
                contractDTO.getRegDate(),
                contractDTO.getModDate());
    }

    @Override
    public WarehouseCostDTO get(Integer id) {
        WarehouseCost warehouseCost = contractMapper.getOne(id);

        Warehouse warehouse = warehouseCost.getWarehouse();

        Member member = warehouse.getMember();


        MemberDTO memberDTO = MemberDTO.from(member);


        WarehouseDTO warehouseDTO = modelMapper.map(warehouse, WarehouseDTO.class);

        warehouseDTO.setMemberDTO(memberDTO);

        WarehouseCostDTO warehouseCostDTO = modelMapper.map(warehouseCost,WarehouseCostDTO.class);
        warehouseCostDTO.setWarehouseDTO(warehouseDTO);

        return warehouseCostDTO;
    }

    @Override
    public Integer check(String name) {
        Integer id = contractMapper.checkMember(name);
        return id;
    }

    @Override
    public Integer getUserId(String name){
        Integer id = contractMapper.selectUserId(name);
        return id;
    }

    public List<ContractDTO> changedListDTO(List<Contract> contractList) {
        List<ContractDTO> contractDTOList = new ArrayList<>();
        contractList.stream()
                .forEach(contract -> {
                    Warehouse warehouse = contract.getWarehouse();

                    WarehouseDTO warehouseDTO = modelMapper.map(warehouse, WarehouseDTO.class);

                    Member member = contract.getMember();
                    MemberDTO memberDTO =  modelMapper.map(member, MemberDTO.class);

                    ContractDTO contractDTO = modelMapper.map(contract, ContractDTO.class);

                    contractDTO.setWarehouseDTO(warehouseDTO);
                    contractDTO.setMemberDTO(memberDTO);

                    contractDTOList.add(contractDTO);
                });

        return contractDTOList;
    }
}
