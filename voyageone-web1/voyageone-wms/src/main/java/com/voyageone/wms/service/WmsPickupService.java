package com.voyageone.wms.service;

import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.FormPickupBean;
import com.voyageone.wms.formbean.FormReservation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/21/2015.
 *
 * @author Jonas
 */
public interface WmsPickupService {

	/**
	 * 【捡货页面】初始化
	 * @param user 用户登录信息
     * @param reserveType reserve类型
     * @return Map 页面初始化项目
	 */
    Map<String, Object> doInit(UserSessionBean user,String reserveType);

    /**
     * 【捡货画面】根据检索条件查询取得需要捡货的一览
     * @param paramMap 检索参数
     * @param user 用户登录信息
     * @return List<FormReservation> 捡货记录
     */
    List<FormReservation> getPickupInfo(Map<String, Object> paramMap, UserSessionBean user,String reserveType);

    /**
     * 【捡货页面】根据检索条件查询取得需要捡货的一览件数
     * @param paramMap 检索参数
     * @param user 用户登录信息
     * @return long 捡货记录的件数
     */
    int getPickupCount(Map<String, Object> paramMap, UserSessionBean user,String reserveType);

    /**
     * 【捡货页面】根据扫描的内容取得相关记录
     * @param paramMap 扫描参数
     * @param user 用户登录信息
     * @param reserveType reserve类型
     * @return Map 扫描记录
     */
    Map<String, Object> getScanInfo(Map<String, Object> paramMap, UserSessionBean user,String reserveType);

    /**
     * 【捡货画面】下载可捡货列表
     * @param type 下载类型
     * @param user 用户登录信息
     * @return ResponseEntity<byte[]> 可捡货列表
     */
    byte[] downloadPickup(String type, UserSessionBean user);


    /**
     * 【捡货画面】下载已捡货报告
     * @param store_id 画面的检索参数
     * @param from 画面的检索参数
     * @param to
     * @param user 用户登录信息
     * @return ResponseEntity<byte[]> 可捡货列表
     */
    byte[] downloadReportPicked(String store_id,String from,String to, UserSessionBean user,String reserveType);


}
