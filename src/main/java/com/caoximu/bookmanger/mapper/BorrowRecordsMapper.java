package com.caoximu.bookmanger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caoximu.bookmanger.entity.BorrowRecords;
import org.apache.ibatis.annotations.Mapper;

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

}
