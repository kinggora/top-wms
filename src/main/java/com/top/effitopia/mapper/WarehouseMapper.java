package com.top.effitopia.mapper;

import com.top.effitopia.domain.*;
import com.top.effitopia.dto.CellDTO;
import com.top.effitopia.dto.PageRequestDTO;
import com.top.effitopia.dto.WarehouseUtilizationDTO;
import com.top.effitopia.dto.WarehouseDTO;
import com.top.effitopia.enumeration.MemberStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    List<Warehouse> selectWarehouseList(PageRequestDTO<WarehouseDTO> pageRequestDTO);
    List<Cell> selectCellList(PageRequestDTO<CellDTO> pageRequestDTO);
    List<Stock> findStockList(PageRequestDTO<CellDTO> pageRequestDTO);
    int update(Warehouse warehouse);
    int delete(Integer warehouse_id);
    Warehouse selectId(Integer id);
    String selectName(String name);
    int insert(Warehouse warehouse);
    int getWarehouseCount(PageRequestDTO pageRequestDTO);
    int getCellCount(PageRequestDTO pageRequestDTO);
    List<WarehouseType> selectAllType();
    List<WarehouseUtilizationDTO> getWarehouseUtilizationList();
    double getTotalUtilizationAverage();
    List<Member> selectAssignableWarehouseManagerList();
}
