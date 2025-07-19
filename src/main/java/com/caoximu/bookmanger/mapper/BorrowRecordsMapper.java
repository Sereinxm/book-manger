package com.caoximu.bookmanger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caoximu.bookmanger.domain.response.AuthorBorrowStatisticsResponse;
import com.caoximu.bookmanger.domain.response.BorrowRecordResponse;
import com.caoximu.bookmanger.entity.BorrowRecords;
import com.caoximu.bookmanger.entity.enums.BorrowStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 借阅记录表 Mapper 接口
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Mapper
public interface BorrowRecordsMapper extends BaseMapper<BorrowRecords> {

    Page<BorrowRecordResponse> getBorrowRecords(@Param("page") Page<BorrowRecords> page, @Param("userId") Long userId, @Param("status") String status);

    /**
     * 获取作者图书借阅统计
     */
    List<AuthorBorrowStatisticsResponse> getAuthorBorrowStatistics(@Param("authorId") Long authorId);

    default int getActiveBorrowCount(Long userId){
        return Math.toIntExact(selectCount(Wrappers.<BorrowRecords>lambdaQuery()
                .eq(BorrowRecords::getUserId, userId)
                .eq(BorrowRecords::getStatus, BorrowStatus.ACTIVE.getCode())
        ));
    }
}
