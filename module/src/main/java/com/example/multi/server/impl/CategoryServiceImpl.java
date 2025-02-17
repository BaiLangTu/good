package com.example.multi.server.impl;

import com.example.multi.entity.Category;
import com.example.multi.entity.Goods;
import com.example.multi.mapper.CategoryMapper;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author 小白-945
 * @since 2024-12-22
 */
@Service
public class CategoryServiceImpl  {
     @Resource
     private CategoryMapper mapper;

    public List<Category> getAll() {

        return mapper.getAll();

    }



     public Category getById(BigInteger id) {

         return mapper.getById(id);
    }

    public List<Category> getByIds(List<BigInteger> ids) {

         if (ids == null || ids.size() == 0) {
             return new ArrayList<>();
         }

        // 将分类ids转为字符串再用“ ，”分割
        StringBuilder idsStr = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            idsStr.append(ids.get(i).toString());
            if (i != ids.size() - 1) {
                idsStr.append(",");
            }
        }

        return mapper.getByIds(idsStr.toString());
    }



    public Category extractById(BigInteger id) {
    return mapper.extractById(id);
    }



     // 获取所有父类目
     public List<Category> getByParentAll() {
         return mapper.getByParentAll();
     }

     //获取父类目下子类目数据(二级分类）
     public List<Category> getCategoryAll(BigInteger parentId) {
         return mapper.getCategoryAll(parentId);
     }


    // 获取子类目列表
    public List<Category> getCategories() {
         return mapper.getCategories();
     }

     // 获取类目下商品列表
    public List<Goods> getGoodsByCategoryId(BigInteger categoryId,int page,int pageSize) {
         return mapper.getGoodsByCategoryId(categoryId,page,pageSize);
    }

    // 获取类目下的商品总数
    public Long getCategoryGoodsCount(BigInteger categoryId) {
        return mapper.getCategoryGoodsCount(categoryId);
    }



     //创建商品类目
     public int insert(String name,String image){
         Category category = new Category();
         int timestamp = (int) (System.currentTimeMillis() / 1000);
         category.setName(name);
         category.setImage(image);
         category.setCreatedTime(timestamp);
         category.setUpdatedTime(timestamp);
         category.setIsDeleted(0);

         return mapper.insert(category);
     }

    //更新商品类目
     public int update(BigInteger id,String name,String image){
         Category category = new Category();
         category.setId(id);
         category.setName(name);
         category.setImage(image);
         int timestamp = (int) (System.currentTimeMillis() / 1000);
         category.setUpdatedTime(timestamp);
         category.setIsDeleted(0);
         return mapper.update(category);

     }

    // 删除商品类目
     public int delete(BigInteger id) {

          return mapper.delete(id, (int) (System.currentTimeMillis() / 1000));
     }

}
