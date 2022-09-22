package com.poho.stuup.dao;


import com.poho.common.custom.PageData;
import com.poho.stuup.model.Certificate;
import com.poho.stuup.model.dto.CertificateDTO;
import com.poho.stuup.model.dto.CertificateSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CertificateMapper extends BaseDao<Certificate> {

    int selectTotal(@Param("searchDTO") CertificateSearchDTO searchDTO);

    List<CertificateDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") CertificateSearchDTO searchDTO);
}