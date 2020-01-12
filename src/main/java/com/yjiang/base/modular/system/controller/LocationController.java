package com.yjiang.base.modular.system.controller;

import com.yjiang.base.core.log.LogObjectHolder;
import com.yjiang.base.core.util.GmapUtil;
import com.yjiang.base.modular.system.model.Location;
import com.yjiang.base.modular.system.service.ILocationService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-12-08 13:09:59
 */
@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {

    private String PREFIX = "/system/location/";

    @Autowired
    private ILocationService locationService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "location.html";
    }

    @RequestMapping("/location_search")
    @ResponseBody
    public Object locationSearch(@RequestParam("skeyword") String skeyword,@RequestParam("city") String city) {
        System.out.println(skeyword + "---" + city);
        List<Map> list = GmapUtil.getMapByKeyWordAndCity(skeyword, city);
        if(!CollectionUtils.isEmpty(list)){
            return list.stream().map(m->{
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name",m.get("name"));
                String[] address = String.valueOf(m.get("location")).split(",");
                map.put("longitude",address[0]);
                map.put("latitude",address[1]);
                return map;
            }).collect(toList());
        }
        return Collections.emptyList();
    }

    @RequestMapping("/location_Map")
    @ResponseBody
    public Object location_Map(@RequestParam("longitude")String longitude, @RequestParam("latitude")String latitude) {
        String mapStr = GmapUtil.getMap(longitude, latitude);
        return mapStr;
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/location_add")
    public String locationAdd() {
        return PREFIX + "location_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/location_update/{locationId}")
    public String locationUpdate(@PathVariable Integer locationId, Model model) {
        Location location = locationService.selectById(locationId);
        model.addAttribute("item",location);
        LogObjectHolder.me().set(location);
        return PREFIX + "location_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return locationService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Location location) {
        locationService.insert(location);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long locationId) {
        locationService.deleteById(locationId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Location location) {
        locationService.updateById(location);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{locationId}")
    @ResponseBody
    public Object detail(@PathVariable("locationId") Long locationId) {
        return locationService.selectById(locationId);
    }
}
