package com.kdgcsoft.web.base.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.common.exception.BizException;
import com.kdgcsoft.common.util.TreeUtil;
import com.kdgcsoft.web.base.entity.BaseMenu;
import com.kdgcsoft.web.base.entity.BaseOrg;
import com.kdgcsoft.web.base.enums.YesNo;
import com.kdgcsoft.web.base.mapper.BaseMenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YINFAN
 */
@Service
public class BaseMenuService extends ServiceImpl<BaseMenuMapper, BaseMenu> {

    public List<BaseMenu> listAllByOrder() {
        return baseMapper.selectList(new LambdaQueryWrapper<BaseMenu>().orderByAsc(BaseMenu::getOrderNo));
    }

    public List<BaseMenu> tree() {
        return TreeUtil.buildTree(this.listAllByOrder());
    }

    public BaseMenu saveBaseMenu(BaseMenu entity) {
        if (hasRepeat(entity)) {
            throw new BizException(StrUtil.format("菜单编码[{}]重复", entity.getCode()));
        }
        //前台新增的都是非内置的菜单
        if (entity.getId() == null) {
            entity.setEmbed(YesNo.N);
        } else {
            BaseMenu exist = baseMapper.selectById(entity.getId());
            //如果修改的是内置菜单则使用原来的编码 保证内置的菜单编码和状态不会被改变
            if (exist != null && exist.getEmbed() == YesNo.Y) {
                entity.setEmbed(exist.getEmbed());
                entity.setCode(exist.getCode());
            }
        }
        saveOrUpdate(entity);
        return entity;
    }

    public void deleteById(Long id) {
        BaseMenu entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BizException("菜单不存在");
        }
        if (entity.getEmbed() == YesNo.Y) {
            throw new BizException("内置菜单不可删除");
        }
        baseMapper.deleteById(id);
    }

    public boolean hasRepeat(BaseMenu entity) {
        return baseMapper.exists(new LambdaQueryWrapper<BaseMenu>()
                .eq(BaseMenu::getCode, entity.getCode())
                .ne(entity.getId() != null, BaseMenu::getId, entity.getId())
        );
    }
}




