package io.renren.modules.platform.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.platform.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.platform.service.MemberService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author xiaozhao
 * @email 484235492@qq.com
 * @date 2023-07-19 15:24:23
 */
@RestController
@RequestMapping("platform/user")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("platform:user:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("platform:user:info")
    public R info(@PathVariable("id") Long id){
        MemberEntity user = memberService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("platform:user:save")
    public R save(@RequestBody MemberEntity user){
        memberService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("platform:user:update")
    public R update(@RequestBody MemberEntity user){
        memberService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("platform:user:delete")
    public R delete(@RequestBody Long[] ids){
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
