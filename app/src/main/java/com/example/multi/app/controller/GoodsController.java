package com.example.multi.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.multi.app.domain.*;
import com.example.multi.utility.AliOssUtility;
import com.example.multi.utility.Utility;
import com.example.multi.dto.GoodsDTO;
import com.example.multi.entity.Category;
import com.example.multi.entity.Goods;
import com.example.multi.server.GoodsService;
import com.example.multi.server.impl.CategoryServiceImpl;
import com.example.multi.wrapper.ImageInfo;
import com.example.multi.wrapper.Wp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @RequestMapping("/goods/category_list")
    public ParentVo getCategoryAll() {

        // 获取父类目数据
        List<Category> parentCategory = categoryService.getByParentAll();

        // 创建父类目展示对象
        List<ParentCategoryV0> parentCategoryV0List = new ArrayList<>();
        for (Category parentCategories : parentCategory) {
            ParentCategoryV0 parentCategoryV0 = new ParentCategoryV0();
            parentCategoryV0.setId(parentCategories.getId());
            parentCategoryV0.setName(parentCategories.getName());
            parentCategoryV0.setImage(parentCategories.getImage());

            // 获取父类目下子类目数据
            List<Category> categoryList = categoryService.getCategoryAll(parentCategories.getId());

            // 创建子类目展示对象
            List<CategoryItemVo> categoryItemVoList = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryItemVo categoryItemVo = new CategoryItemVo();
                categoryItemVo.setId(category.getId());
                categoryItemVo.setParentId(category.getParentId());
                categoryItemVo.setName(category.getName());
                categoryItemVo.setImage(category.getImage());
                categoryItemVoList.add(categoryItemVo);

            }
            // 子类目列表放在父类目中
            parentCategoryV0.setCategoryList(categoryItemVoList);

            // 将parentCategoryV0内容添加到parentCategoryV0List列表
            parentCategoryV0List.add(parentCategoryV0);

        }

        ParentVo parentVo = new ParentVo();
        parentVo.setParentList(parentCategoryV0List);
        return parentVo;

    }

    @RequestMapping("/goods/category_goods")
    public CategoryGoodsVO getCategoryGoodsItem(@RequestParam(name = "categoryId", required = false) BigInteger categoryId,
                                                @RequestParam(name = "wp", required = false) String wp) {

        int page;
        int pageSize;
        if (wp != null) {
            // Base64解码
            String decodedWp = URLDecoder.decode(wp, StandardCharsets.UTF_8);

            byte[] decodedBytes = Base64.getDecoder().decode(decodedWp);
            String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);

            String decodeWp = URLDecoder.decode(jsonString, StandardCharsets.UTF_8);
            System.out.println("JSON string: " + decodeWp);

            // 解析 JSON 字符串为 Wp 对象
            Wp wpJson = JSON.parseObject(decodeWp, Wp.class);
            page = wpJson.getPage();
            pageSize = wpJson.getPageSize();
        } else {
            page = 1;
            pageSize = 10;

        }

        // 获取类目数据
        List<Category> parentCategory = categoryService.getCategories();

        List<CategoryVO> categories = parentCategory.stream()
                .map(parentCategories -> {
                    CategoryVO categoryVO = new CategoryVO();
                    categoryVO.setId(parentCategories.getId());
                    categoryVO.setName(parentCategories.getName());
                    categoryVO.setImage(parentCategories.getImage());
                    return categoryVO;
                }).collect(Collectors.toList());



        // 获取商品数据
        List<Goods> goodsList = categoryService.getGoodsByCategoryId(categoryId, page, pageSize);

        // 创建商品展示对象列表
        List<GoodsItemVo> goodsVoList = new ArrayList<>();


        // 获取商品分类id
        List<BigInteger> ids = goodsList.stream()
                .map(Goods::getCategoryId)  // 提取每个商品的分类ID
                .collect(Collectors.toList());  // 将结果收集到一个List中

        List<Category> categoryList = categoryService.getByIds(ids);

        // 创建 HashMap
        Map<BigInteger, String> categoryMap = new HashMap<>();
        // 循环分类列表
        for (Category category : categoryList) {

            // 上传HashMap的键值对
            categoryMap.put(category.getId(), category.getName());
        }

        // 遍历商品列表，将每个商品转换为 goodsItemVO
        for (Goods goods : goodsList) {

            GoodsItemVo goodsItemVo = new GoodsItemVo();

            // 判断类目id是否为空，若为空跳过商品，若不为空则在map里获取类目信息
            String categoryName = categoryMap.get(goods.getCategoryId());

            if (categoryName == null) {
                continue;
            }

            // 将轮播图图片用 “ $ ” 连接
            String[] images = goods.getGoodsImages().split("\\$");

            Utility utility = new Utility();

            // 获取图片信息，包含 AR 和 URL
            ImageInfo imageInfo = utility.getImageInfo(images[0]);

            goodsItemVo.setId(goods.getId())
                    .setCategoryName(categoryName)
                    .setGoodsImage(imageInfo)
                    .setTitle(goods.getTitle())
                    .setPrice(goods.getPrice())
                    .setSales(goods.getSales());
            goodsVoList.add(goodsItemVo);

        }

        //创建对象设置商品列表最终返回
        GoodsVo goodsVo = new GoodsVo();
        Utility utility = new Utility();
        goodsVo.setList(goodsVoList);

        // 判断是否是最后一页（分页结束），如果当前页获取到的商品数量小于每页数量说明分页结束
        Boolean isEnd = goodsList.size() < pageSize;
        String nextWp = utility.categoryWp(categoryId,page + 1, pageSize); // 自动生成下一页
        goodsVo.setWP(nextWp);
        goodsVo.setIsEnd(isEnd);

        // 5. 返回最终数据
        CategoryGoodsVO categoryGoodsVO = new CategoryGoodsVO();
        categoryGoodsVO.setCategories(categories);  // 类目列表
        categoryGoodsVO.setGoodsItem(goodsVo);  // 商品分页列表

        return categoryGoodsVO;

    }


    @RequestMapping("/goods/list")
    public GoodsVo getGoodsAll(@RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "wp", required = false) String wp) {

        int page;
        int pageSize;
        String name;

        if (wp != null) {
            // Base64解码
            String decodedWp = URLDecoder.decode(wp, StandardCharsets.UTF_8);

            byte[] decodedBytes = Base64.getDecoder().decode(decodedWp);
            String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);

            String decodeWp = URLDecoder.decode(jsonString, StandardCharsets.UTF_8);
            System.out.println("JSON string: " + decodeWp);

            // 解析 JSON 字符串为 Wp 对象
            Wp wpJson = JSON.parseObject(decodeWp, Wp.class);
            page = wpJson.getPage();
            pageSize = wpJson.getPageSize();
            name = wpJson.getName();
        } else {
            page = 1;
            pageSize = 10;
            name = keyword;

        }

        // 获取商品数据
        List<Goods> goodsList = goodsService.getAllGoodsInfo(name, page, pageSize);

        // 创建商品展示对象列表
        List<GoodsItemVo> goodsVoList = new ArrayList<>();

        // 获取商品分类id
//        List<BigInteger> ids = new ArrayList<>();
//        for (Goods goods : goodsList) {
//            ids.add(goods.getCategoryId());
//        }

        // 获取商品分类id
        List<BigInteger> ids = goodsList.stream()
                .map(Goods::getCategoryId)  // 提取每个商品的分类ID
                .collect(Collectors.toList());  // 将结果收集到一个List中


        List<Category> categories = categoryService.getByIds(ids);

        // 创建 HashMap
        Map<BigInteger, String> categoryMap = new HashMap<>();
        // 循环分类列表
        for (Category category : categories) {

            // 上传HashMap的键值对
            categoryMap.put(category.getId(), category.getName());
        }

        // 遍历商品列表，将每个商品转换为 goodsItemVO
        for (Goods goods : goodsList) {

            GoodsItemVo goodsItemVo = new GoodsItemVo();

            // 判断类目id是否为空，若为空跳过商品，若不为空则在map里获取类目信息
            String categoryName = categoryMap.get(goods.getCategoryId());
            ;
            if (categoryName == null) {
                continue;
            }

            // 将轮播图图片用 “ $ ” 连接
            String[] images = goods.getGoodsImages().split("\\$");

            Utility utility = new Utility();

            // 获取图片信息，包含 AR 和 URL
            ImageInfo imageInfo = utility.getImageInfo(images[0]);



            goodsItemVo.setId(goods.getId())
                    .setCategoryName(categoryName)
                    .setGoodsImage(imageInfo)
                    .setTitle(goods.getTitle())
                    .setPrice(goods.getPrice())
                    .setSales(goods.getSales());
            goodsVoList.add(goodsItemVo);

        }

        //创建对象设置商品列表最终返回
        GoodsVo goodsVo = new GoodsVo();
        Utility utility = new Utility();
        goodsVo.setList(goodsVoList);

        // 判断是否是最后一页（分页结束），如果当前页获取到的商品数量小于每页数量说明分页结束
        Boolean isEnd = goodsList.size() < pageSize;
        goodsVo.setIsEnd(isEnd);

        String nextWp = utility.encodeWp(page + 1, pageSize, name); // 自动生成下一页
        goodsVo.setWP(nextWp);

        return goodsVo;

    }

    @RequestMapping("/goods/new_list")
    public GoodsVo getGoodsList(@RequestParam(name = "keyword", required = false) String keyword,
                                @RequestParam(name = "wp", required = false) String wp) {

        int page;
        int pageSize;
        String name;

        if (wp != null) {
            // Base64解码
            String decodedWp = URLDecoder.decode(wp, StandardCharsets.UTF_8);

            byte[] decodedBytes = Base64.getDecoder().decode(decodedWp);
            String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);

            String decodeWp = URLDecoder.decode(jsonString, StandardCharsets.UTF_8);
            System.out.println("JSON string: " + decodeWp);

            // 解析 JSON 字符串为 Wp 对象
            Wp wpJson = JSON.parseObject(decodeWp, Wp.class);
            page = wpJson.getPage();
            pageSize = wpJson.getPageSize();
            name = wpJson.getName();
        } else {
            page = 1;
            pageSize = 10;
            name = keyword;

        }

        // 获取商品数据
        List<GoodsDTO> goodsList = goodsService.getAllGoods(name, page, pageSize);

        // 创建商品展示对象列表
        List<GoodsItemVo> goodsVoList = new ArrayList<>();

        // 遍历商品列表，将每个商品转换为 goodsItemVO
        for (GoodsDTO goodsDTO : goodsList) {

            GoodsItemVo goodsItemVo = new GoodsItemVo();

            // 将轮播图图片用 “ $ ” 连接
            String[] images = goodsDTO.getGoodsImages().split("\\$");


            Utility utility = new Utility();

            // 获取图片信息，包含 AR 和 URL
            ImageInfo imageInfo = utility.getImageInfo(images[0]);


            goodsItemVo.setId(goodsDTO.getId())
                    .setCategoryName(goodsDTO.getCategoryName())
                    .setGoodsImage(imageInfo)
                    .setTitle(goodsDTO.getTitle())
                    .setPrice(goodsDTO.getPrice())
                    .setSales(goodsDTO.getSales());

            goodsVoList.add(goodsItemVo);

        }

        //创建对象设置商品列表最终返回
        GoodsVo goodsVo = new GoodsVo();
        Utility utility = new Utility();
        goodsVo.setList(goodsVoList);

        // 判断是否是最后一页（分页结束），如果当前页获取到的商品数量小于每页数量说明分页结束
        Boolean isEnd = goodsList.size() < pageSize;
        goodsVo.setIsEnd(isEnd);

        String nextWp = utility.encodeWp(page + 1, pageSize, name); // 自动生成下一页
        goodsVo.setWP(nextWp);

        return goodsVo;

    }

    @RequestMapping("/goods/info")
    public GoodsInfoVo goodsInfo(@RequestParam(name = "goodsId") BigInteger goodsId) {
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return null;
        }

        // 类目
        Category category = categoryService.getById(goods.getCategoryId());
        String categoryName = category != null ? category.getName() : "未分类";
        String categoryImages = category != null ? category.getImage() : "未上传类目图";

        // 创建 GoodsInfoVo 并设置相应的字段
        GoodsInfoVo goodsInfoVo = new GoodsInfoVo();

        //返回类目名和类目图
        goodsInfoVo.setCategoryName(categoryName);
        goodsInfoVo.setCategoryImage(categoryImages);

        // 设置商品图片轮播图，将商品图片字符串转换为 List
        String[] imagesArray = goods.getGoodsImages().split("\\$");
        goodsInfoVo.setGoodsImages(Arrays.asList(imagesArray));
        // 返回商品信息
        goodsInfoVo.setSource(goods.getSource());
        goodsInfoVo.setPrice(goods.getPrice());
        goodsInfoVo.setSales(goods.getSales());
        goodsInfoVo.setGoodsName(goods.getGoodsName());
        goodsInfoVo.setSevenDayReturn(goods.getSevenDayReturn());
        goodsInfoVo.setGoodsDetails(goods.getGoodsDetails());
        return goodsInfoVo;
    }

    // 直接在代码中指定文件上传路径
    private static final String UPLOAD_DIR = "/Users/liuzefeng/Documents/JavaWeb/good/app/image";  // 保存文件的目录

    // 上传图片接口
    @RequestMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 生成唯一文件名，避免文件重名
        String newFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // 定义文件保存的路径
        File dest = new File(UPLOAD_DIR + newFileName);

        // 如果文件夹不存在，创建文件夹
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        // 保存文件到本地
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 返回文件的访问路径
        return "文件上传成功，文件访问路径为:" + newFileName;
    }

    @Autowired
    private AliOssUtility aliOssUtility;

    @RequestMapping("/uploadImage")
    public String uploadImages(@RequestParam("file") MultipartFile file) {

        return "上传成功,图片地址："+aliOssUtility.uploadImage(file);
    }

    @RequestMapping("/uploadVideo")
    public String uploadVideo(@RequestParam("file") MultipartFile file) {

        return "上传成功，视频地址:"+aliOssUtility.uploadVideo(file);
    }

    @RequestMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        return "上传成功，文件地址:"+aliOssUtility.uploadFile(file);
    }

}


