package com.example.multi.console.controller;

import com.example.multi.console.controller.domain.ConsoleInfoVo;
import com.example.multi.console.controller.domain.ConsoleItemVo;
import com.example.multi.console.controller.domain.ConsoleListVo;
import com.example.multi.console.controller.domain.ConsoleVo;
import com.example.multi.entity.Goods;
import com.example.multi.server.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;


    @RequestMapping("goods/console_list")
    public ConsoleListVo getConsoleAll(@RequestParam(name = "keyword",required = false) String keyword,
                                       @RequestParam(name = "page") int page,
                                       @RequestParam(name = "pageSize") int pageSize) {

        // 获取商品数据
        List<Goods> consoleList = goodsService.getAllGoodsInfo(keyword, page, pageSize);

        Long count = goodsService.count();

        // 创建商品展示对象列表

        List<ConsoleItemVo> consoleItemVoList = new ArrayList<>();

        for (Goods goods : consoleList) {
            ConsoleItemVo consoleItemVo = new ConsoleItemVo();
            String[] images = goods.getGoodsImages().split("\\$");
            consoleItemVo.setId(goods.getId())
                    .setGoodImage(images[0])
                    .setTitle(goods.getTitle())
                    .setSales(goods.getSales())
                    .setPrice(goods.getPrice());

            consoleItemVoList.add(consoleItemVo);
        }


        ConsoleListVo consoleListVo = new ConsoleListVo();
        consoleListVo.setTotal(count);
        consoleListVo.setPageSize(pageSize);
        consoleListVo.setItems(consoleItemVoList);
        return consoleListVo;
    }

    @RequestMapping("/goods/console_info")
    public ConsoleInfoVo consoleInfoVo(@RequestParam(name = "goodsId") BigInteger goodsId) {

        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return null;
        }

        ConsoleInfoVo ConsoleInfo = new ConsoleInfoVo();

        // 设置商品图片轮播图，将商品图片字符串转换为 List
        String[] imagesArray = goods.getGoodsImages().split("\\$");
        ConsoleInfo.setGoodsImages(Arrays.asList(imagesArray));

        ConsoleInfo.setPrice(goods.getPrice());
        ConsoleInfo.setSales(goods.getSales());
        ConsoleInfo.setGoodsName(goods.getGoodsName());
        ConsoleInfo.setSevenDayReturn(goods.getSevenDayReturn());
        ConsoleInfo.setGoodsDetails(goods.getGoodsDetails());

        Instant instant = Instant.ofEpochSecond(goods.getCreatedTime());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = zonedDateTime.format(formatter);
        ConsoleInfo.setCreatedTime(formattedDate);

        Instant instant_updated = Instant.ofEpochSecond(goods.getUpdatedTime());
        ZonedDateTime zonedDateTimeUpdated = ZonedDateTime.ofInstant(instant_updated, ZoneId.systemDefault());
        DateTimeFormatter formatterUpdated = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateUpdated = zonedDateTimeUpdated.format(formatterUpdated);
        ConsoleInfo.setUpdatedTime(formattedDateUpdated);
        return ConsoleInfo;

    }


    @RequestMapping("/goods/add")
    public ConsoleVo addGoods(@RequestParam(name = "goodsId" ,required = false ) BigInteger goodsId,
                              @RequestParam(name = "categoryId") BigInteger categoryId,
                              @RequestParam(name = "title") String title,
                              @RequestParam(name = "goodsImages") String goodsImages,
                              @RequestParam(name = "sales") Integer sales,
                              @RequestParam(name = "goodsName") String goodsName,
                              @RequestParam(name = "price") Integer price,
                              @RequestParam(name = "source") String source,
                              @RequestParam(name = "sevenDayReturn") Integer sevenDayReturn,
                              @RequestParam(name = "goodsDetails") String goodsDetails) {

        // 调用 service 层新增商品
        ConsoleVo consoleVo = new ConsoleVo();
        try {
            BigInteger result = goodsService.edit(goodsId,categoryId,title,goodsImages,sales,goodsName,price,source,sevenDayReturn,goodsDetails);

            if (result != null) {
                consoleVo.setMessage("商品添加成功");
                consoleVo.setId(result.toString());

            } else {
                consoleVo.setMessage("商品添加失败");
                consoleVo.setId(result.toString());

            }
        } catch (IllegalArgumentException e) {
            consoleVo.setMessage("参数错误:" + e.getMessage());
        } catch (RuntimeException e) {
            consoleVo.setMessage("操作失败:" + e.getMessage());

        }

        return consoleVo;
    }

    @RequestMapping("/goods/update")
    public ConsoleVo updateGoods(
                                  @RequestParam(name = "goodsId") BigInteger goodsId,
                                  @RequestParam(name = "categoryId") BigInteger categoryId,
                                  @RequestParam(name = "title") String title,
                                  @RequestParam(name = "goodsImages") String goodsImages,
                                  @RequestParam(name = "sales") Integer sales,
                                  @RequestParam(name = "goodsName") String goodsName,
                                  @RequestParam(name = "price") Integer price,
                                  @RequestParam(name = "source") String source,
                                  @RequestParam(name = "sevenDayReturn") Integer sevenDayReturn,
                                  @RequestParam(name = "goodsDetails") String goodsDetails) {
        // 调用 service 层修改商品
        ConsoleVo consoleVo = new ConsoleVo();
        try {

            BigInteger result = goodsService.edit(goodsId,categoryId,title.trim(), goodsImages, sales, goodsName.trim(), price, source.trim(), sevenDayReturn, goodsDetails.trim());

            if (result != null) {
                consoleVo.setMessage("商品修改成功");
                consoleVo.setId(result.toString());
            } else {
                consoleVo.setMessage("商品修改失败");
                consoleVo.setId(result.toString());
            }
        } catch (IllegalArgumentException e) {
            consoleVo.setMessage("参数错误:" + e.getMessage());
        } catch (RuntimeException e) {
            consoleVo.setMessage("操作失败:" + e.getMessage());
        }
        return consoleVo;
    }


    @RequestMapping("/goods/delete")
    public ConsoleVo goodsDelete (@RequestParam(name = "goodsId") BigInteger goodsId) {
        int result = goodsService.deleteGoods(goodsId);
        ConsoleVo consoleVo = new ConsoleVo();
        if( result == 1){
            consoleVo.setMessage("商品删除成功");
        } else {
            consoleVo.setMessage("商品删除失败");
        }
        return consoleVo;

    }


}


