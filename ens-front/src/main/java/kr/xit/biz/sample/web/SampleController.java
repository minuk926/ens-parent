package kr.xit.biz.sample.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.biz.sample.web
 * fileName    : SampleController
 * author      : xitdev
 * date        : 2023-05-02
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-02    xitdev       최초 생성
 *
 * </pre>
 */
@Tag(name = "SampleController", description = "예제")
@RestController
@RequestMapping(value = "/ens/biz/sample")
public class SampleController {

    @GetMapping(value = "/hello")
    public ModelAndView hello(){
        ModelAndView mav = new ModelAndView();

        List<String> list = new ArrayList<>();
        list.add("1111");
        list.add("222");
        list.add("333");

        Map<String,Object> map = new HashMap<>();
        map.put("key1", "map-data1");
        map.put("key2", "map-data1");
        map.put("key3", "map-data1");

        mav.addObject("list", list);
        mav.addObject("map", map);
        mav.addObject("data1", "data1~~~~~~~~");
        mav.addObject("data2", "data2~~~~~~~~");
        mav.setViewName("hello");
        return mav;
    }

    @GetMapping(value = "/home")
    public ModelAndView home(){
        ModelAndView mav = new ModelAndView();

        List<String> resultList = new ArrayList<String>();
        resultList.add("AAA");
        resultList.add("BBB");
        resultList.add("CCC");
        resultList.add("DDD");
        resultList.add("EEE");
        resultList.add("FFF");

        mav.addObject("resultList",resultList);
        mav.addObject("title", "home~~~");
        mav.setViewName("th/home");

        return mav;
    }
}
