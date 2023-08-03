package io.renren.modules.platform.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.platform.entity.ImgDetailEntity;
import io.renren.modules.platform.service.ImgDetailService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author xiaozhao
 * @email 484235492@qq.com
 * @date 2023-07-19 19:42:35
 */
@RestController
@RequestMapping("platform/imgdetail")
public class ImgDetailController {
    @Autowired
    private ImgDetailService imgDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("platform:imgdetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = imgDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("platform:imgdetail:info")
    public R info(@PathVariable("id") Long id){
		ImgDetailEntity imgDetail = imgDetailService.getById(id);

        return R.ok().put("imgDetail", imgDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("platform:imgdetail:save")
    public R save(@RequestBody ImgDetailEntity imgDetail){
		imgDetailService.save(imgDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("platform:imgdetail:update")
    public R update(@RequestBody ImgDetailEntity imgDetail){
		imgDetailService.updateById(imgDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("platform:imgdetail:delete")
    public R delete(@RequestBody Long[] ids){
		imgDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
