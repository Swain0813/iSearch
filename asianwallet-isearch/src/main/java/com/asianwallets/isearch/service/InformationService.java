package com.asianwallets.isearch.service;

import com.asianwallets.common.entity.Information;
import com.asianwallets.isearch.dto.DeleteInformationDTO;
import com.asianwallets.isearch.dto.InformationDTO;
import com.asianwallets.isearch.dto.InformationHistoryDTO;
import org.springframework.data.domain.Page;

/**
 * 新闻
 */

public interface InformationService {

    /**
     * 查询最新新闻
     *
     * @param informationDTO
     * @return
     */
    Page<Information> selectNewestInformation(InformationDTO informationDTO);

    /**
     * 用户查询新闻
     *
     * @param informationDTO
     * @return
     */
    Page<Information> selectUserInformation(InformationDTO informationDTO);

    /**
     * 新增用户点击过的新闻
     *
     * @param informationHistory
     */
    void addUserInformationHistory(InformationHistoryDTO informationHistory);

    /**
     * 查询用户历史新闻 分页
     *
     * @param informationDTO
     * @return
     */
    Page<Information> selectUserInformationHistory(InformationDTO informationDTO);

    /**
     * 删除用户的历史记录
     *
     * @param deleteInformationDTO
     * @return
     */
    long deleteUserInformationHistory(DeleteInformationDTO deleteInformationDTO);
}
