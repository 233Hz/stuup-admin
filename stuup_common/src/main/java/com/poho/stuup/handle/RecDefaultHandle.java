package com.poho.stuup.handle;

import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import com.poho.stuup.model.vo.RecLogVO;

/**
 * @author BUNGA
 * @description: 默认导入处理
 * @date 2023/5/26 10:54
 */
public class RecDefaultHandle implements RecExcelHandle {
    @Override
    public RecLogDetailsVO<RecLogVO, RecCaucus> getImportRec(Long batchCode) {
        return null;
    }
}
