package com.example.multi.console.controller;

import com.example.multi.console.domain.ConsoleVo;
import com.example.multi.entity.Category;
import com.example.multi.service.GoodsService;
import com.example.multi.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 类目表 前端控制器
 * </p>
 *
 * @author 小白-945
 * @since 2024-12-22
 */
@RestController
public class CategoryController {
     @Autowired
     private CategoryServiceImpl service;

     @Autowired
     private GoodsService goodsService;

     @RequestMapping("/category/info")
     public Category categoryInfo (@RequestParam(name = "categoryId") BigInteger categoryId) {
          return service.getById(categoryId);
     }
     @RequestMapping("/category/all")
     public List<Category> categoryAll(){
          return service.getAll();
     }
     @RequestMapping("/category/add")
     public String insertCategory(@RequestParam(name = "name") String name,@RequestParam(name = "image") String image) {

          int result = service.insert(name, image);
          return 1 == result ? "成功" : "失败";

     }
     @RequestMapping("/category/update")
     public String updateCategory(@RequestParam(name = "categoryId") BigInteger categoryId,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "image") String image) {
          int result = service.update(categoryId, name, image);
          return 1 == result ? "成功" : "失败";

     }

     @RequestMapping("/category/delete")
     public ConsoleVo delete (@RequestParam(name = "categoryId") BigInteger categoryId)
     {

          int category= service.delete(categoryId);

          int goods =goodsService.deleteGoods(categoryId);
          ConsoleVo consoleVo = new ConsoleVo();

          if( category == 1){
               if(goods == 1){
                    consoleVo.setMessage("商品类目删除成功");
               }
          } else {
               consoleVo.setMessage("商品类目删除失败");
          }
          return consoleVo;

     }

}
