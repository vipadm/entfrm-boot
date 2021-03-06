package com.entfrm.biz.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.system.entity.Shortcut;
import com.entfrm.biz.system.service.ShortcutService;
import com.entfrm.core.base.api.R;
import com.entfrm.core.log.annotation.OperLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author entfrm
 * @date 2019-08-25 22:56:58
 * @description 快捷方式Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/shortcut")
public class ShortcutController {

    private final ShortcutService shortcutService;

    private QueryWrapper<Shortcut> getQueryWrapper(Shortcut shortcut) {
        return new QueryWrapper<Shortcut>().like(StrUtil.isNotBlank(shortcut.getName()), "name", shortcut.getName()).eq(StrUtil.isNotBlank(shortcut.getRegion()), "region", shortcut.getRegion())
                .between(StrUtil.isNotBlank(shortcut.getBeginTime()) && StrUtil.isNotBlank(shortcut.getEndTime()), "create_time", shortcut.getBeginTime(), shortcut.getEndTime()).orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('shortcut_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Page page, Shortcut shortcut) {
        IPage<Shortcut> shortcutPage = shortcutService.page(page, getQueryWrapper(shortcut));
        return R.ok(shortcutPage.getRecords(), shortcutPage.getTotal());
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(shortcutService.getById(id));
    }

    @OperLog("快捷方式新增")
    @PreAuthorize("@ps.hasPerm('shortcut_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody Shortcut shortcut) {
        shortcutService.saveOrUpdate(shortcut);
        return R.ok();
    }

    @OperLog("快捷方式修改")
    @PreAuthorize("@ps.hasPerm('shortcut_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@RequestBody Shortcut shortcut) {
        shortcutService.updateById(shortcut);
        return R.ok();
    }

    @OperLog("快捷方式删除")
    @PreAuthorize("@ps.hasPerm('shortcut_del')")
    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public R remove(@PathVariable("id") Integer id) {
        return R.ok(shortcutService.removeById(id));
    }
}
